"""Turn voice/data (wav clips + metadata.jsonl with phoneme_ids) into a Piper training set: silence trimmed and
normalized audio and spectrograms cached the way piper_train.preprocess does, dataset.jsonl, and Kareem's config."""
import json, sys
from pathlib import Path
import piper_train.vits.lightning  # noqa: F401  (fail early)
from piper_train.norm_audio import cache_norm_audio, make_silence_detector

src, out, cfg = Path(sys.argv[1]), Path(sys.argv[2]), Path(sys.argv[3])
(out / "cache").mkdir(parents=True, exist_ok=True)
config = json.load(open(cfg, encoding="utf-8"))
sr = int(config["audio"]["sample_rate"])
det = make_silence_detector()
n = 0
with open(out / "dataset.jsonl", "w", encoding="utf-8") as f:
    for line in open(src / "metadata.jsonl", encoding="utf-8"):
        u = json.loads(line)
        norm, spec = cache_norm_audio(src / u["wav"], out / "cache", det, sr)
        f.write(json.dumps({"phoneme_ids": u["phoneme_ids"], "audio_norm_path": str(norm.absolute()),
                            "audio_spec_path": str(spec.absolute()), "text": u["text"]}, ensure_ascii=False) + "\n")
        n += 1
json.dump(config, open(out / "config.json", "w", encoding="utf-8"), ensure_ascii=False, indent=1)
print(n, "utterances")
