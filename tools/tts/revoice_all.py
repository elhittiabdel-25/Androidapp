"""Re-voice every card clip voiced before the enhanced Kareem (kareem-ft-e399), in place in the app's assets.

Clips listed in SKIP (one card:key per line: the cards voiced since 2026-11-15) already use the enhanced voice.
The model is loaded once; progress is kept in DONE (one card:key per line), so a stopped run resumes where it was.
Old clips stay in git history: tools/tts/revoice_pick.py puts an old clip back where the speech check scores
the new one clearly worse.

    TTS_TOOLS=DIR python3 tools/tts/revoice_all.py DONE SKIP
"""
import json, os, sys, tempfile

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
from build_content import spoken, encode       # noqa: E402
from kareem import speak                       # noqa: E402

ASSETS = os.path.normpath(os.path.join(HERE, "..", "..", "app", "src", "main", "assets"))
TARGET = {"face": "face", "deep": "deeper", "had": "hadith"}


def raw_text(sp, key):
    if key == "title":
        return sp["title"]
    kind, i = key.split("-")
    return sp[TARGET[kind]][int(i)]


def main(done_path, skip_path):
    speech = json.load(open(os.path.join(HERE, "speech.json"), encoding="utf-8"))
    skip = set(open(skip_path).read().split())
    done = set(open(done_path).read().split()) if os.path.exists(done_path) else set()
    tmp = tempfile.mkdtemp()
    jobs = []
    for cid in sorted(os.listdir(f"{ASSETS}/audio")):
        if cid.startswith("_") or cid not in speech:
            continue
        for f in sorted(os.listdir(f"{ASSETS}/audio/{cid}")):
            key, ext = os.path.splitext(f)
            path = f"{ASSETS}/audio/{cid}/{f}"
            if ext != ".ogg" or f"{cid}:{key}" in done or f"{cid}:{key}" in skip:
                continue
            jobs.append((cid, key, path))
    print(len(jobs), "clips to re-voice", flush=True)
    log = open(done_path, "a")
    for n, (cid, key, path) in enumerate(jobs):
        wav = os.path.join(tmp, "c.wav")
        speak(spoken(raw_text(speech[cid], key)), wav)
        encode(wav, path)
        log.write(f"{cid}:{key}\n"); log.flush()
        if n % 200 == 0:
            print(n, cid, key, flush=True)
    print("done", len(jobs), flush=True)


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])
