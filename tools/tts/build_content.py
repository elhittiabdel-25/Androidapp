"""Build the app's content pack: cards JSON + per-sentence audio.

Inputs : ../cards/cards.json (display text, written from the books)
         speech.json        (fully vowelled spoken text for each display segment)
Outputs: OUT/content/cards.json, OUT/audio/<card>/<key>.ogg (new clips, Opus) or .mp3 (approved older clips
         kept as they are, see REUSE_AUDIO), OUT/report.json
Checks : segment counts match, spoken text matches display text (unless marked paraphrase),
         and every audio clip is transcribed back with Whisper and scored (run with --asr).
"""
import json, re, subprocess, sys, os, difflib, shutil, concurrent.futures as cf
from arabic_tts import prepare, strip
from roundtrip import asr, wer

HERE = os.path.dirname(os.path.abspath(__file__))
TOOLS = os.environ.get("TTS_TOOLS", HERE)      # folder holding the sherpa-onnx engine and the Kareem voice
B = f"{TOOLS}/sherpa-onnx-v1.12.20-linux-x64-static/bin"
M = f"{TOOLS}/vits-piper-ar_JO-kareem-medium"
SPLIT = re.compile(r"(?<=[.؛!؟])\s+")

def sentences(t): return [s for s in SPLIT.split(t) if s]

# Speech text notation: "[shown|spoken]" swaps a word for the voice only; "{x}" is spoken but not shown.
def spoken(s):
    return re.sub(r"\{([^}]*)\}", r"\1", re.sub(r"\[([^|\]]*)\|([^\]]*)\]", r"\2", s))
def shown(s):
    return re.sub(r"\{[^}]*\}", "", re.sub(r"\[([^|\]]*)\|([^\]]*)\]", r"\1", s))

def letters(t):
    t = re.sub(r"﴿[^﴾]*﴾", " ", t); t = re.sub(r"\[[^\]]*\]", " ", t)
    t = t.replace("ﷺ", "صلى الله عليه وسلم")
    t = strip(t); t = re.sub(r"[إأآٱ]", "ا", t).replace("ة", "ه").replace("ى", "ي")
    return " ".join(re.sub(r"[^ء-ي ]", " ", t).split())

def encode(wav, out):
    """Encode a voice clip: Opus 16 kbps mono in Ogg for .ogg (chosen by ear against 24 kbps: the same
    clarity for this voice at two thirds of the size; Android plays it natively), MP3 48 kbps for .mp3."""
    if out.endswith(".ogg"):
        args = ["-c:a", "libopus", "-b:a", "16k", "-ac", "1"]
    else:
        args = ["-ac", "1", "-ar", "22050", "-b:a", "48k"]
    subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav, *args, out], check=True)


VOICE = os.environ.get("VOICE", "g2p")      # g2p: our phonemizer (arabic_g2p.py) + the model run directly;
                                            # espeak: the older path (sherpa-onnx's espeak-ng on prepare()d text)


def tts_wav(text, wav):
    """Voice `text` with Piper Kareem into a WAV file."""
    if VOICE == "espeak":
        subprocess.run([f"{B}/sherpa-onnx-offline-tts", f"--vits-model={M}/ar_JO-kareem-medium.onnx", f"--vits-tokens={M}/tokens.txt",
                        f"--vits-data-dir={M}/espeak-ng-data", "--num-threads=2", f"--output-filename={wav}", prepare(text)],
                       check=True, capture_output=True)
    else:
        from kareem import speak
        speak(text, wav)
    return wav


def tts_clip(text, out, wav=None):
    """Voice `text` with Piper Kareem into `out` (.ogg or .mp3); the WAV is kept next to it (or at `wav`)."""
    wav = tts_wav(text, wav or os.path.splitext(out)[0] + ".wav")
    encode(wav, out)
    return wav

def main(out, do_asr):
    cards = json.load(open(f"{HERE}/../cards/cards.json", encoding="utf-8"))
    speech = json.load(open(f"{HERE}/speech.json", encoding="utf-8"))
    os.makedirs(f"{out}/content", exist_ok=True)
    report, jobs, app_cards = [], [], []
    for c in cards["cards"]:
        sp = speech[c["id"]]; para = set(sp.get("paraphrase", []))
        face = sentences(c["face"])
        hadiths = [e for e in c["evidence"] if e["kind"] == "hadith"]
        assert len(face) == len(sp["face"]), (c["id"], "face count", len(face), len(sp["face"]))
        assert len(c["deeper"]) == len(sp["deeper"]), (c["id"], "deeper count")
        assert len(hadiths) == len(sp["hadith"]), (c["id"], "hadith count")
        segs = [("title", "title", c["title"], sp["title"], "face")]
        segs += [(f"face-{i}", f"face:{i}", d, s, "face") for i, (d, s) in enumerate(zip(face, sp["face"]))]
        segs += [(f"deep-{i}", f"deeper:{i}", d, s, "deep") for i, (d, s) in enumerate(zip(c["deeper"], sp["deeper"]))]
        segs += [(f"had-{i}", f"hadith:{i}", e["text"], s, "deep") for i, (e, s) in enumerate(zip(hadiths, sp["hadith"]))]
        os.makedirs(f"{out}/audio/{c['id']}", exist_ok=True)
        card_out = dict(c); card_out["faceSentences"] = face; card_out["audio"] = []; card_out["diac"] = {}
        for key, tag, disp, spoken_raw, layer in segs:
            is_para = tag in para
            say, show = spoken(spoken_raw), shown(spoken_raw)
            if strip(say).startswith("ر"):
                print(f"!! {c['id']} {tag}: starts with ر — the voice weakens it; add a lead word, e.g. {{وَ}}")
            sim = difflib.SequenceMatcher(None, letters(disp), letters(show)).ratio()
            if not is_para and sim < 0.9:
                print(f"!! {c['id']} {tag}: spoken text differs from display (sim {sim:.2f})\n   D: {letters(disp)}\n   S: {letters(show)}")
            reuse = os.environ.get("REUSE_AUDIO", "/nonexistent")
            old = next((p for p in (f"{reuse}/{c['id']}/{key}.{ext}" for ext in ("ogg", "mp3")) if os.path.exists(p)), None)
            if old:                          # clip already approved: keep it as it is, in its own format
                name = f"{key}{os.path.splitext(old)[1]}"
                shutil.copy(old, f"{out}/audio/{c['id']}/{name}"); wav = None
            else:                            # new clip: Opus
                name = f"{key}.ogg"
                wav = tts_clip(say, f"{out}/audio/{c['id']}/{name}")
            card_out["audio"].append({"key": key, "target": tag, "layer": layer, "file": f"audio/{c['id']}/{name}"})
            if not is_para: card_out["diac"][tag] = show        # vowelled display text, same wording as the card
            report.append({"card": c["id"], "seg": tag, "plain": letters(say), "wav": wav, "paraphrase": is_para, "sim": round(sim, 3)})
        app_cards.append(card_out)
    json.dump({"books": cards["books"], "cards": app_cards}, open(f"{out}/content/cards.json", "w", encoding="utf-8"), ensure_ascii=False)
    print(f"{len(report)} audio segments written")
    if do_asr:
        def check(r):
            hyp = asr(r["wav"]); r["heard"] = hyp; r["wer"] = round(wer(r["plain"], hyp), 3); return r
        todo = [r for r in report if r["wav"]]
        with cf.ThreadPoolExecutor(max_workers=2) as ex:
            list(ex.map(check, todo))
        report = [r for r in report if r.get("wer") is not None]
        bad = [r for r in report if r["wer"] > 0.25]
        print(f"ASR check: mean WER {sum(r['wer'] for r in report)/len(report):.3f}; {len(bad)} segments above 0.25")
        for r in bad: print(f"  {r['card']} {r['seg']} WER={r['wer']} heard: {r['heard']}")
    json.dump(report, open(f"{out}/report.json", "w", encoding="utf-8"), ensure_ascii=False, indent=1)

if __name__ == "__main__":
    main(sys.argv[1], "--asr" in sys.argv)
