import subprocess, re, difflib, sys, json, os
B = "sherpa-onnx-v1.12.20-linux-x64-static/bin"
M = "vits-piper-ar_JO-kareem-medium"
W = "sherpa-onnx-whisper-small"
def tts(text, out, speed=1.0):
    subprocess.run([f"{B}/sherpa-onnx-offline-tts", f"--vits-model={M}/ar_JO-kareem-medium.onnx", f"--vits-tokens={M}/tokens.txt",
                    f"--vits-data-dir={M}/espeak-ng-data", f"--vits-length-scale={1/speed}", f"--output-filename={out}", text],
                   check=True, capture_output=True)
def asr(wav):
    w16 = wav.replace(".wav", ".16k.wav")
    subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav, "-ar", "16000", "-ac", "1", w16], check=True)
    r = subprocess.run([f"{B}/sherpa-onnx-offline", f"--whisper-encoder={W}/small-encoder.int8.onnx", f"--whisper-decoder={W}/small-decoder.int8.onnx",
                        f"--tokens={W}/small-tokens.txt", "--whisper-language=ar", "--whisper-task=transcribe", "--num-threads=4", w16],
                       capture_output=True, text=True)
    m = re.findall(r'"text"\s*:\s*"([^"]*)"', r.stdout + r.stderr)
    return m[-1] if m else (r.stdout + r.stderr)[-300:]
def norm(s):
    s = re.sub(r"[ً-ْٰـ]", "", s)
    s = re.sub(r"[إأآٱ]", "ا", s).replace("ة", "ه").replace("ى", "ي")
    s = re.sub(r"[^ء-ي ]", " ", s)
    return " ".join(s.split())
def wer(ref, hyp):
    r, h = norm(ref).split(), norm(hyp).split()
    sm = difflib.SequenceMatcher(None, r, h)
    match = sum(b.size for b in sm.get_matching_blocks())
    return 1 - match / max(len(r), 1)
if __name__ == "__main__":
    pairs = json.load(open(sys.argv[1], encoding="utf-8"))
    for i, (plain, diac) in enumerate(pairs):
        for tag, t in (("plain", plain), ("diac", diac)):
            f = f"rt_{i}_{tag}.wav"; tts(t, f); hyp = asr(f)
            print(f"{i} {tag:5s} WER={wer(plain, hyp):.2f} | {hyp}")
