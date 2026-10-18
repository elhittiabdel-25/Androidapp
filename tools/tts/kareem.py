"""The Kareem voice (Piper ar_JO, VITS) run directly on our phonemes with onnxruntime.

Same model, settings and pauses as the sherpa-onnx engine used before; only the phonemes differ (see
arabic_g2p.py). Each sentence is one model call: ids ^ _ p1 _ p2 _ ... pn _ $ (Piper's layout), scales
noise / length / noise_w, then long silences are cut to 20% like sherpa-onnx does (--tts-silence-scale 0.2).

    speak(text, "out.wav")
"""
import json, os, unicodedata, wave
import numpy as np
import onnxruntime as ort

TOOLS = os.environ.get("TTS_TOOLS", "/home/claude/tts-tools")
M = os.path.join(TOOLS, "vits-piper-ar_JO-kareem-medium")
RATE = 22050
NOISE, LENGTH, NOISE_W = 0.667, 1.0, 0.8          # the voice's own defaults (its .onnx.json)
_cfg = None
_sess = None


def _ids():
    global _cfg
    if _cfg is None:
        _cfg = json.load(open(os.path.join(M, "ar_JO-kareem-medium.onnx.json"), encoding="utf-8"))
    return {k: v[0] for k, v in _cfg["phoneme_id_map"].items()}


def session():
    global _sess
    if _sess is None:
        o = ort.SessionOptions()
        o.intra_op_num_threads = int(os.environ.get("TTS_THREADS", "2"))
        o.inter_op_num_threads = 1
        _sess = ort.InferenceSession(os.path.join(M, "ar_JO-kareem-medium.onnx"), o, providers=["CPUExecutionProvider"])
    return _sess


def ids(phonemes):
    table = _ids()
    out = [table["^"], table["_"]]
    for ch in unicodedata.normalize("NFD", phonemes):
        if ch not in table:
            raise ValueError(f"unknown phoneme {ch!r} in {phonemes}")
        out += [table[ch], table["_"]]
    return out + [table["$"]]


def synth(sentences, noise=NOISE, length=LENGTH, noise_w=NOISE_W):
    parts = []
    for ph in sentences:
        x = np.array([ids(ph)], dtype=np.int64)
        audio = session().run(None, {"input": x, "input_lengths": np.array([x.shape[1]], dtype=np.int64),
                                     "scales": np.array([noise, length, noise_w], dtype=np.float32)})[0]
        parts.append(audio.reshape(-1))
    return np.concatenate(parts) if parts else np.zeros(0, dtype=np.float32)


def scale_silence(a, scale=0.2):
    """Shorten every pause longer than 0.2 s to `scale` of its length (sherpa-onnx's ScaleSilence)."""
    thr = int(RATE * 0.2)
    quiet = np.abs(a) <= 0.01
    out, i, last, n = [], 0, -1, len(a)
    for j in range(n):
        if quiet[j]:
            if last == -1:
                last = j
            continue
        if last != -1 and j - last >= thr:
            out.append(a[i:last + int((j - last) * scale)])
            i = j
        last = -1
    if last != -1 and n - last > thr:
        out.append(a[i:last + int((n - last) * scale)])
        i = n
    out.append(a[i:])
    return np.concatenate(out)


def write_wav(path, audio):
    pcm = (np.clip(audio, -1, 1) * 32767).astype("<i2")
    with wave.open(path, "wb") as w:
        w.setnchannels(1)
        w.setsampwidth(2)
        w.setframerate(RATE)
        w.writeframes(pcm.tobytes())


def speak(text, wav, **scales):
    """Vowelled Arabic text -> WAV with the Kareem voice, through our phonemizer."""
    from arabic_g2p import phonemize
    from espeak_ph import phonemize as espeak
    write_wav(wav, scale_silence(synth(phonemize(text, espeak=espeak), **scales)))
    return wav
