"""Re-voice clips flagged by the speech check as the best of N takes, in place in the app's assets.

The voice is not deterministic: each take of the same text sounds a little different, and some takes say the
first consonant or a hard word more clearly than others. Every take is encoded like the app's clips,
transcribed with the same Whisper-small as the speech check, and replaces the clip only if it scores clearly
better (by more than 0.05 WER) than the clip already in the app.

    TTS_TOOLS=DIR python3 tools/tts/best_take.py N card:key[,card:key...]      e.g. 4 husn-zann:title,qadar:face-2
"""
import json, os, re, shutil, subprocess, sys, tempfile, concurrent.futures as cf

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
from arabic_tts import prepare                          # noqa: E402
from roundtrip import wer                               # noqa: E402
from build_content import spoken, letters, encode       # noqa: E402

T = os.environ.get("TTS_TOOLS", HERE)
B = f"{T}/sherpa-onnx-v1.12.20-linux-x64-static/bin"
M = f"{T}/vits-piper-ar_JO-kareem-medium"
W = f"{T}/sherpa-onnx-whisper-small"
ASSETS = os.path.normpath(os.path.join(HERE, "..", "..", "app", "src", "main", "assets"))
TARGET = {"face": "face", "deep": "deeper", "had": "hadith"}
speech = json.load(open(os.path.join(HERE, "speech.json"), encoding="utf-8"))
TMP = tempfile.mkdtemp()


def raw(cid, key):
    sp = speech[cid]
    if key == "title":
        return sp["title"]
    kind, i = key.split("-")
    return sp[TARGET[kind]][int(i)]


def asr(audio, tag):
    w16 = os.path.join(TMP, tag + ".16k.wav")
    subprocess.run(["ffmpeg", "-y", "-v", "error", "-i", audio, "-ar", "16000", "-ac", "1", w16], check=True)
    r = subprocess.run([f"{B}/sherpa-onnx-offline", f"--whisper-encoder={W}/small-encoder.int8.onnx",
                        f"--whisper-decoder={W}/small-decoder.int8.onnx", f"--tokens={W}/small-tokens.txt",
                        "--whisper-language=ar", "--whisper-task=transcribe", "--num-threads=1", w16],
                       capture_output=True, text=True)
    m = re.findall(r'"text"\s*:\s*"([^"]*)"', r.stdout + r.stderr)
    return m[-1] if m else ""


def take(job):
    cid, key, n = job
    say = spoken(raw(cid, key))
    tag = f"{cid}__{key}__{n}"
    if n == 0:                                   # the clip in the app, scored the same way
        f = os.path.join(ASSETS, "audio", cid, key + ".ogg")
    else:
        wav = os.path.join(TMP, tag + ".wav")
        f = os.path.join(TMP, tag + ".ogg")
        subprocess.run([f"{B}/sherpa-onnx-offline-tts", f"--vits-model={M}/ar_JO-kareem-medium.onnx",
                        f"--vits-tokens={M}/tokens.txt", f"--vits-data-dir={M}/espeak-ng-data", "--num-threads=1",
                        f"--output-filename={wav}", prepare(say)], check=True, capture_output=True)
        encode(wav, f)
    hyp = asr(f, tag)
    return cid, key, n, wer(letters(say), letters(hyp)), hyp, f


def main(n, pairs):
    jobs = [(c, k, i) for c, k in pairs for i in range(n + 1)]
    with cf.ThreadPoolExecutor(os.cpu_count() or 2) as ex:
        res = list(ex.map(take, jobs))
    by, kept = {}, 0
    for r in res:
        by.setdefault((r[0], r[1]), []).append(r)
    for (cid, key), rs in by.items():
        base = next(r for r in rs if r[2] == 0)
        best = min((r for r in rs if r[2] > 0), key=lambda r: r[3])
        swap = best[3] + 0.05 < base[3]
        if swap:
            shutil.copy(best[5], os.path.join(ASSETS, "audio", cid, key + ".ogg"))
            kept += 1
        print(f"{cid}:{key}  app {base[3]:.2f} -> best take {best[3]:.2f} {'REPLACED' if swap else 'kept'}"
              f"  | {best[4] if swap else base[4]}", flush=True)
    print(f"{kept} of {len(by)} clips replaced")
    shutil.rmtree(TMP, ignore_errors=True)


if __name__ == "__main__":
    main(int(sys.argv[1]), [p.split(":") for p in sys.argv[2].split(",")])
