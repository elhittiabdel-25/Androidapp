"""Voice trial: the app's current Kareem clips against SILMA TTS on the same vowelled sentences.

SILMA TTS (SILMA AI, 2026; code MIT, weights Apache-2.0) is an F5-TTS model trained for Arabic with full tashkeel;
it speaks in the voice of a short reference clip. Two references are tried: a clean Kareem clip from the app (to
keep the app's voice) and the sample voice shipped with SILMA. Every clip is scored with the same Whisper-small
check as the app's clips, and kept as MP3 for listening.

    python3 tools/tts/silma_trial.py TOOLS_DIR OUT_DIR [SHARD COUNT]

With SHARD and COUNT, only every COUNT-th clip starting at SHARD is voiced (one share per parallel machine).
"""
import json, os, subprocess, sys, time
from importlib.resources import files

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
from build_content import spoken, letters          # noqa: E402
from arabic_tts import apply_waqf                   # noqa: E402
from speech_check import raw_text, transcribe       # noqa: E402
from roundtrip import wer                           # noqa: E402

ASSETS = os.path.normpath(os.path.join(HERE, "..", "..", "app", "src", "main", "assets"))
# the twelve clips the Whisper check scored worst, and six ordinary ones
CLIPS = [
    "tatayyur:title", "qawaid-arba:face-4", "qubur:had-0", "shuab-iman:face-1", "seerah-timeline:face-0",
    "tawhid-fadl:title", "ilm-ghayb:had-0", "qubur:had-1", "shuab-iman:title", "deen-levels:had-0",
    "shuab-iman:deep-1", "tatayyur:had-0",
    "purpose:face-0", "purpose:face-1", "four-masail:face-0", "tawhid-anwa:deep-3", "sahaba:face-0", "qadar:face-0",
]


def sh(*a):
    subprocess.run(list(a), check=True)


def main(tools, out, shard=0, count=1):
    import torch
    torch.set_num_threads(os.cpu_count() or 4)
    from silma_tts.api import SilmaTTS
    os.makedirs(out, exist_ok=True)
    clips = [c for c in os.environ.get("TRIAL_CLIPS", "").split(",") if c] or CLIPS
    clips = clips[shard::count]
    t0 = time.time()
    speech = json.load(open(os.path.join(HERE, "speech.json"), encoding="utf-8"))
    ref_kareem = os.path.join(out, "ref-kareem.wav")
    sh("ffmpeg", "-y", "-loglevel", "error", "-i", os.path.join(ASSETS, "audio/malaika/face-2.ogg"), "-ar", "24000", "-ac", "1", ref_kareem)
    refs = {
        "kareem-voice": (ref_kareem, letters(spoken(raw_text(speech["malaika"], "face-2")))),
        "silma-voice": (str(files("silma_tts").joinpath("infer/ref_audio_samples/ar.ref.24k.wav")),
                        "ويدقق النظر في القرآن الكريم وسائر الكتب السماوية ويتبع مسالك الرسل العظام عليهم الصلاة والسلام."),
    }
    if os.environ.get("TRIAL_VOICE") == "my-voice":
        # the app owner's own voice, recorded for this purpose, reading this sentence
        refs = {"my-voice": (os.path.join(HERE, "voices", "my-voice.wav"),
                             "أعظم ما أمر الله به التوحيد، وهو إفراد الله بالعبادة، وأعظم ما نهى عنه الشرك.")}
    tts = SilmaTTS(enable_normalizer=False, force_tashkeel=False)
    print(f"model loaded in {time.time() - t0:.0f}s", flush=True)
    rows, wavs = [], []
    for clip in clips:
        cid, key = clip.split(":")
        text = spoken(raw_text(speech[cid], key))
        row = {"clip": clip, "text": text, "plain": letters(text), "files": {}}
        # what the app plays today
        cur = os.path.join(out, f"{cid}__{key}__kareem-now")
        sh("ffmpeg", "-y", "-loglevel", "error", "-i", os.path.join(ASSETS, f"audio/{cid}/{key}.ogg"), "-ar", "16000", "-ac", "1", cur + ".16k.wav")
        sh("ffmpeg", "-y", "-loglevel", "error", "-i", os.path.join(ASSETS, f"audio/{cid}/{key}.ogg"), "-c:a", "libmp3lame", "-q:a", "4", cur + ".mp3")
        row["files"]["kareem-now"] = cur
        wavs.append(cur + ".16k.wav")
        for name, (rf, rt) in refs.items():
            base = os.path.join(out, f"{cid}__{key}__silma-{name}")
            t1 = time.time()
            tts.infer(ref_file=rf, ref_text=rt, gen_text=apply_waqf(text), nfe_step=32, seed=7,
                      normalize_numbers=False, force_tashkeel=False, file_wave=base + ".wav", remove_silence=False)
            sh("ffmpeg", "-y", "-loglevel", "error", "-i", base + ".wav", "-ar", "16000", "-ac", "1", base + ".16k.wav")
            sh("ffmpeg", "-y", "-loglevel", "error", "-i", base + ".wav", "-c:a", "libmp3lame", "-q:a", "4", base + ".mp3")
            row["files"][f"silma-{name}"] = base
            row.setdefault("seconds", {})[name] = round(time.time() - t1, 1)
            print(f"  {clip} {name}: {time.time() - t1:.0f}s", flush=True)
            wavs.append(base + ".16k.wav")
        rows.append(row)
        print("voiced", clip, flush=True)
    heard = {}
    for i in range(0, len(wavs), 12):
        heard.update(transcribe(tools, wavs[i:i + 12], 4))
    for row in rows:
        row["wer"] = {}
        for v, base in row["files"].items():
            h = heard.get(base + ".16k.wav", "")
            row["wer"][v] = round(wer(row["plain"], h), 3)
            row.setdefault("heard", {})[v] = h
            row["files"][v] = os.path.basename(base) + ".mp3"
    json.dump(rows, open(os.path.join(out, f"trial-{shard}.json"), "w", encoding="utf-8"), ensure_ascii=False, indent=1)
    names = list(rows[0]["wer"])
    lines = ["mean WER: " + ", ".join(f"{v} {sum(r['wer'][v] for r in rows) / len(rows):.3f}" for v in names)]
    for r in rows:
        lines.append(f"{r['clip']}: " + ", ".join(f"{v} {r['wer'][v]}" for v in names))
    open(os.path.join(out, f"trial-{shard}.txt"), "w", encoding="utf-8").write("\n".join(lines) + "\n")
    print("\n".join(lines))


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], *(int(x) for x in sys.argv[3:5]))
