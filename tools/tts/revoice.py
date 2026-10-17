"""Regenerate chosen voice clips from speech.json, in place in the app's assets (same voice and settings
as build_content.py), e.g. after rewording a sentence that the speech check flagged.

    python3 tools/tts/revoice.py TOOLS_DIR card:key [card:key ...]      e.g. qadar:face-2 riya:title
"""
import json, os, subprocess, sys, tempfile

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
from arabic_tts import prepare, strip            # noqa: E402
from build_content import spoken                  # noqa: E402

ASSETS = os.path.normpath(os.path.join(HERE, "..", "..", "app", "src", "main", "assets"))
TARGET = {"face": "face", "deep": "deeper", "had": "hadith"}


def raw_text(sp, key):
    if key == "title":
        return sp["title"]
    kind, i = key.split("-")
    return sp[TARGET[kind]][int(i)]


def main(tools, pairs):
    b = f"{tools}/sherpa-onnx-v1.12.20-linux-x64-static/bin"
    m = f"{tools}/vits-piper-ar_JO-kareem-medium"
    speech = json.load(open(os.path.join(HERE, "speech.json"), encoding="utf-8"))
    tmp = tempfile.mkdtemp()
    for pair in pairs:
        cid, key = pair.split(":")
        say = spoken(raw_text(speech[cid], key))
        if strip(say).startswith("ر"):
            print(f"!! {pair} starts with ر — the voice weakens it; add a lead word, e.g. {{وَ}}")
        wav = os.path.join(tmp, f"{cid}__{key}.wav")
        out = os.path.join(ASSETS, "audio", cid, f"{key}.mp3")
        subprocess.run([f"{b}/sherpa-onnx-offline-tts", f"--vits-model={m}/ar_JO-kareem-medium.onnx", f"--vits-tokens={m}/tokens.txt",
                        f"--vits-data-dir={m}/espeak-ng-data", "--num-threads=2", f"--output-filename={wav}", prepare(say)],
                       check=True, capture_output=True)
        subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav, "-ac", "1", "-ar", "22050", "-b:a", "48k", out], check=True)
        print(f"{pair}: {say}")


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2:])
