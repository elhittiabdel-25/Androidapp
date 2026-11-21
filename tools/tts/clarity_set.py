"""Training lines that teach the enhanced Kareem to articulate where it is unclear.

Takes the clips where the speech check understood the original Kareem well (WER <= 0.1) and the enhanced one badly
(WER >= 0.2), voices each text again with the ORIGINAL Kareem (KAREEM_MODEL, the .base.onnx) in up to N takes, keeps
the take Whisper understands best if its WER is <= 0.1, and writes it as a clean 22.05 kHz WAV with Piper phoneme ids
from the app's own phonemizer, in the format of voice/data (metadata.jsonl).

    KAREEM_MODEL=.../ar_JO-kareem-medium.base.onnx TTS_TOOLS=... python3 tools/tts/clarity_set.py sel.json OUT N
    (sel.json: [[card, key], ...])
"""
import json, os, sys, shutil, concurrent.futures as cf

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
import best_take as bt                                  # noqa: E402  (asr, raw, TMP)
from build_content import spoken, letters              # noqa: E402
from roundtrip import wer                               # noqa: E402
import kareem                                           # noqa: E402
from arabic_g2p import phonemize                        # noqa: E402
from espeak_ph import phonemize as espeak               # noqa: E402


def one(job):
    i, (cid, key), n = job
    say = spoken(bt.raw(cid, key))
    best = None
    for t in range(n):
        wav = os.path.join(bt.TMP, f"c{i:04d}_{t}.wav")
        kareem.speak(say, wav)
        w = wer(letters(say), letters(bt.asr(wav, f"c{i:04d}_{t}")))
        if best is None or w < best[0]:
            best = (w, wav)
        if w == 0:
            break
    return i, cid, key, say, best


def main(sel, out, n):
    os.makedirs(out, exist_ok=True)
    pairs = json.load(open(sel))
    with cf.ThreadPoolExecutor(os.cpu_count() or 2) as ex:
        res = sorted(ex.map(one, [(i, p, n) for i, p in enumerate(pairs)]))
    kept = 0
    with open(os.path.join(out, "metadata.jsonl"), "w", encoding="utf-8") as f:
        for i, cid, key, say, (w, wav) in res:
            if w > 0.1:
                continue
            name = f"c{i:04d}.wav"
            shutil.copy(wav, os.path.join(out, name))
            ph = " ".join(phonemize(say, espeak=espeak))
            f.write(json.dumps({"wav": name, "text": say, "phoneme_ids": kareem.ids(ph), "from": f"{cid}:{key}", "wer": round(w, 3)},
                               ensure_ascii=False) + "\n")
            kept += 1
    print(f"{kept} of {len(res)} lines kept (WER <= 0.1)")


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], int(sys.argv[3]))
