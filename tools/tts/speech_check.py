"""Speech check of the app's clips: transcribe each mp3 with Whisper and score it against its spoken text.

    python3 tools/tts/speech_check.py TOOLS_DIR OUT_DIR [card,card,...]

TOOLS_DIR holds the sherpa-onnx engine and sherpa-onnx-whisper-small; with no card list every card is checked.
Writes OUT_DIR/speech-check.json (one row per clip) and OUT_DIR/speech-check.txt (clips above the threshold).
The model is loaded once per batch of clips, and batches run in parallel.
"""
import json, os, re, subprocess, sys, concurrent.futures as cf

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
from roundtrip import wer                       # noqa: E402
from build_content import spoken, letters       # noqa: E402

ROOT = os.path.normpath(os.path.join(HERE, "..", ".."))
ASSETS = os.path.join(ROOT, "app", "src", "main", "assets")
THRESHOLD = 0.25
BATCH = 12
TARGET = {"face": "face", "deep": "deeper", "had": "hadith"}


def raw_text(sp, key):
    if key == "title":
        return sp["title"]
    kind, i = key.split("-")
    return sp[TARGET[kind]][int(i)]


def transcribe(tools, wavs, threads):
    b = f"{tools}/sherpa-onnx-v1.12.20-linux-x64-static/bin/sherpa-onnx-offline"
    w = f"{tools}/sherpa-onnx-whisper-small"
    r = subprocess.run([b, f"--whisper-encoder={w}/small-encoder.int8.onnx", f"--whisper-decoder={w}/small-decoder.int8.onnx",
                        f"--tokens={w}/small-tokens.txt", "--whisper-language=ar", "--whisper-task=transcribe",
                        f"--num-threads={threads}", *wavs], capture_output=True, text=True)
    out, current = {}, None
    names = {os.path.basename(x): x for x in wavs}
    for line in (r.stdout + "\n" + r.stderr).splitlines():
        line = line.strip()
        if os.path.basename(line) in names:
            current = names[os.path.basename(line)]
        elif current and line.startswith("{") and '"text"' in line:
            try:
                out[current] = json.loads(line)["text"].strip()
            except ValueError:
                m = re.search(r'"text"\s*:\s*"([^"]*)"', line)
                out[current] = m.group(1).strip() if m else ""
            current = None
    return out


def main(tools, out_dir, ids):
    os.makedirs(out_dir, exist_ok=True)
    wav_dir = os.path.join(out_dir, "wav")
    os.makedirs(wav_dir, exist_ok=True)
    cards = json.load(open(os.path.join(ASSETS, "content", "cards.json"), encoding="utf-8"))["cards"]
    speech = json.load(open(os.path.join(HERE, "speech.json"), encoding="utf-8"))
    jobs = []
    for c in cards:
        if ids and c["id"] not in ids:
            continue
        for a in c["audio"]:
            wav = os.path.join(wav_dir, f"{c['id']}__{a['key']}.wav")
            subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", os.path.join(ASSETS, a["file"]),
                            "-ar", "16000", "-ac", "1", wav], check=True)
            jobs.append({"card": c["id"], "key": a["key"], "wav": wav,
                         "plain": letters(spoken(raw_text(speech[c["id"]], a["key"])))})
    print(len(jobs), "clips", flush=True)
    cpus = os.cpu_count() or 2
    workers = max(1, cpus // 2)
    batches = [jobs[i:i + BATCH] for i in range(0, len(jobs), BATCH)]
    heard = {}
    with cf.ThreadPoolExecutor(max_workers=workers) as ex:
        for n, res in enumerate(ex.map(lambda bt: transcribe(tools, [j["wav"] for j in bt], 2), batches)):
            heard.update(res)
            print(f"batch {n + 1}/{len(batches)}", flush=True)
    rows = []
    for j in jobs:
        h = heard.get(j["wav"], "")
        rows.append({"card": j["card"], "key": j["key"], "wer": round(wer(j["plain"], h), 3), "plain": j["plain"], "heard": h})
    json.dump(rows, open(os.path.join(out_dir, "speech-check.json"), "w", encoding="utf-8"), ensure_ascii=False, indent=1)
    bad = [r for r in rows if r["wer"] > THRESHOLD]
    lines = [f"{len(rows)} clips, mean WER {sum(r['wer'] for r in rows) / max(len(rows), 1):.3f}, {len(bad)} above {THRESHOLD}"]
    for r in sorted(bad, key=lambda r: -r["wer"]):
        lines += [f"{r['card']} {r['key']} WER={r['wer']}", f"  want:  {r['plain']}", f"  heard: {r['heard']}"]
    open(os.path.join(out_dir, "speech-check.txt"), "w", encoding="utf-8").write("\n".join(lines) + "\n")
    print("\n".join(lines))


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], set(filter(None, (sys.argv[3] if len(sys.argv) > 3 else "").split(","))))
