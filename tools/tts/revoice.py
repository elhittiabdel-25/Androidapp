"""Regenerate chosen voice clips from speech.json, in place in the app's assets (same voice and settings
as build_content.py), e.g. after rewording a sentence that the speech check flagged. A clip keeps its
format: .ogg (Opus) or .mp3 for the older approved clips.

    TTS_TOOLS=DIR python3 tools/tts/revoice.py card:key [card:key ...]      e.g. qadar:face-2 riya:title
"""
import json, os, sys, tempfile

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
from arabic_tts import strip                      # noqa: E402
from build_content import spoken, tts_clip        # noqa: E402

ASSETS = os.path.normpath(os.path.join(HERE, "..", "..", "app", "src", "main", "assets"))
TARGET = {"face": "face", "deep": "deeper", "had": "hadith"}


def raw_text(sp, key):
    if key == "title":
        return sp["title"]
    kind, i = key.split("-")
    return sp[TARGET[kind]][int(i)]


def main(pairs):
    speech = json.load(open(os.path.join(HERE, "speech.json"), encoding="utf-8"))
    tmp = tempfile.mkdtemp()
    for pair in pairs:
        cid, key = pair.split(":")
        say = spoken(raw_text(speech[cid], key))
        if strip(say).startswith("ر"):
            print(f"!! {pair} starts with ر — the voice weakens it; add a lead word, e.g. {{وَ}}")
        base = os.path.join(ASSETS, "audio", cid, key)
        out = next((base + ext for ext in (".ogg", ".mp3") if os.path.exists(base + ext)), base + ".ogg")
        tts_clip(say, out, wav=os.path.join(tmp, f"{cid}__{key}.wav"))
        print(f"{pair} -> {os.path.basename(out)}: {say}")


if __name__ == "__main__":
    main(sys.argv[1:])
