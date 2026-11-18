"""Compare voice models on the same clips: each model voices each text in N takes, Whisper (as in the speech check)
transcribes them, and the mean WER and the share of takes above 0.3 are printed per model.

    TTS_TOOLS=... python3 tools/tts/voice_eval.py pairs.json N model1.onnx [model2.onnx ...]
"""
import json, os, sys, subprocess

HERE = os.path.dirname(os.path.abspath(__file__))


def main(pairs, n, models):
    for m in models:
        r = subprocess.run([sys.executable, __file__, "--one", pairs, str(n), m], capture_output=True, text=True,
                           env={**os.environ, "KAREEM_MODEL": m})
        print(r.stdout.strip() or r.stderr[-800:], flush=True)


def one(pairs, n, model):
    sys.path.insert(0, HERE)
    import concurrent.futures as cf
    import best_take as bt
    import kareem
    from build_content import spoken, letters
    from roundtrip import wer
    jobs = [(c, k, t) for c, k in json.load(open(pairs)) for t in range(n)]

    def go(j):
        c, k, t = j
        say = spoken(bt.raw(c, k))
        wav = os.path.join(bt.TMP, f"{c}__{k}__{t}.wav")
        kareem.speak(say, wav)
        return wer(letters(say), letters(bt.asr(wav, f"{c}__{k}__{t}")))
    with cf.ThreadPoolExecutor(os.cpu_count() or 2) as ex:
        ws = list(ex.map(go, jobs))
    print(f"{os.path.basename(model)}: mean WER {sum(ws) / len(ws):.3f}, above 0.3: {sum(w > 0.3 for w in ws)}/{len(ws)}")


if __name__ == "__main__":
    if sys.argv[1] == "--one":
        one(sys.argv[2], int(sys.argv[3]), sys.argv[4])
    else:
        main(sys.argv[1], int(sys.argv[2]), sys.argv[3:])
