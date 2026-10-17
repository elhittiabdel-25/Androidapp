"""Voice test on a GitHub runner: Chatterbox Multilingual V3 (MIT), built-in neutral voice, Arabic."""
import json, os, subprocess, sys, time
import torch, torchaudio as ta
from chatterbox.mtl_tts import ChatterboxMultilingualTTS

torch.set_num_threads(os.cpu_count() or 4)
here = os.path.dirname(os.path.abspath(__file__))
out_dir = os.path.join(here, "out", "chatterbox")
os.makedirs(out_dir, exist_ok=True)
items = json.load(open(os.path.join(here, "sentences.json"), encoding="utf-8"))
t0 = time.time()
model = ChatterboxMultilingualTTS.from_pretrained(device="cpu")
print(f"model loaded in {time.time() - t0:.0f}s", flush=True)
log = []
for it in items:
    t = time.time()
    torch.manual_seed(1234)                 # reproducible takes
    wav = model.generate(it["text"], language_id="ar")   # no audio prompt: the model's built-in neutral voice
    wav_path = os.path.join(out_dir, it["id"] + ".wav")
    ta.save(wav_path, wav, model.sr)
    mp3 = wav_path[:-4] + ".mp3"
    subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav_path, "-ac", "1", "-b:a", "64k", mp3], check=True)
    os.remove(wav_path)
    secs = wav.shape[-1] / model.sr
    log.append({"id": it["id"], "gen_seconds": round(time.time() - t, 1), "audio_seconds": round(secs, 2)})
    print(f"{it['id']:18s} {time.time() - t:5.1f}s for {secs:4.1f}s of audio", flush=True)
json.dump(log, open(os.path.join(out_dir, "timing.json"), "w"), indent=1)
