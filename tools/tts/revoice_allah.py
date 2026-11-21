"""Re-voice the clips whose pronunciation changed with the heavy lam of the Name of Allah (arabic_g2p.tafkhim).

Each clip is said N times by Kareem; Whisper-small (the speech check's model) transcribes every take, and the
take it hears best replaces the clip in the app's assets. Clips whose phonemes did not change are left alone.

    TTS_TOOLS=DIR python3 tools/tts/revoice_allah.py [N] [--dry]
"""
import json, os, sys, tempfile, subprocess
HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
import arabic_g2p                                        # noqa: E402
from arabic_g2p import phonemize                         # noqa: E402
from espeak_ph import phonemize as esp                   # noqa: E402
from build_content import spoken, letters, encode        # noqa: E402
from speech_check import raw_text, transcribe            # noqa: E402
from roundtrip import wer                                # noqa: E402
from kareem import synth, write_wav, scale_silence       # noqa: E402

ASSETS = os.path.normpath(os.path.join(HERE, "..", "..", "app", "src", "main", "assets"))
TOOLS = os.environ.get("TTS_TOOLS", "/home/claude/tts-tools")


def main(n, dry):
    cards = json.load(open(os.path.join(ASSETS, "content", "cards.json"), encoding="utf-8"))["cards"]
    speech = json.load(open(os.path.join(HERE, "speech.json"), encoding="utf-8"))
    jobs = []
    for c in cards:
        for a in c["audio"]:
            text = spoken(raw_text(speech[c["id"]], a["key"]))
            arabic_g2p.TAFKHIM = False
            old = phonemize(text, espeak=esp)
            arabic_g2p.TAFKHIM = True
            new = phonemize(text, espeak=esp)
            if old != new:
                jobs.append({"card": c["id"], "key": a["key"], "file": a["file"], "ph": new, "plain": letters(text)})
    print(len(jobs), "clips change", flush=True)
    if dry:
        return
    tmp = tempfile.mkdtemp()
    takes = []
    for j in jobs:
        for t in range(n):
            wav = os.path.join(tmp, f"{j['card']}__{j['key']}__{t}.wav")
            write_wav(wav, scale_silence(synth(j["ph"])))
            w16 = wav.replace(".wav", ".16k.wav")
            subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav, "-ar", "16000", "-ac", "1", w16], check=True)
            takes.append((j, wav, w16))
    print("voiced", len(takes), "takes", flush=True)
    heard = {}
    for i in range(0, len(takes), 12):
        heard.update(transcribe(TOOLS, [x[2] for x in takes[i:i + 12]], 2))
        print(f"heard {min(i + 12, len(takes))}/{len(takes)}", flush=True)
    report = []
    for j in jobs:
        mine = [(wer(j["plain"], heard.get(w16, "")), wav, heard.get(w16, "")) for jj, wav, w16 in takes if jj is j]
        best = min(mine, key=lambda m: m[0])
        out = os.path.join(ASSETS, j["file"])
        encode(best[1], out)
        report.append({"card": j["card"], "key": j["key"], "wer": round(best[0], 3), "takes": [round(m[0], 3) for m in mine], "heard": best[2]})
    json.dump(report, open(os.path.join(tmp, "report.json"), "w", encoding="utf-8"), ensure_ascii=False, indent=1)
    mean = sum(r["wer"] for r in report) / max(1, len(report))
    print(f"replaced {len(report)} clips, mean WER of the chosen takes {mean:.3f}; report in {tmp}/report.json")


if __name__ == "__main__":
    args = [a for a in sys.argv[1:] if not a.startswith("--")]
    main(int(args[0]) if args else 3, "--dry" in sys.argv)
