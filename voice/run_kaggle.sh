#!/bin/bash
# Re-Iman: fine-tune the Kareem voice (Piper VITS medium) on the reader's clips, on a Kaggle GPU.
# Three stages of 100 epochs; after each, the voice is exported to /kaggle/working/kareem-ft-eNNN.onnx.
set -x
STAGES=${STAGES:-3}; PER=${PER:-100}; BATCH=${BATCH:-16}; HOURS=${HOURS:-11}
HERE=$(cd "$(dirname "$0")" && pwd); W=/kaggle/working; T=/tmp/rv; mkdir -p $T $W; cd $T
nvidia-smi -L
pip install -q uv && uv venv -q -p 3.10 --seed venv || exit 1
. venv/bin/activate
python -m pip install -q "pip<24.1"
pip install -q --extra-index-url https://download.pytorch.org/whl/cu117 "torch==1.13.1+cu117" "pytorch-lightning==1.7.7" \
  "torchmetrics==0.11.4" "numpy<1.24" "librosa==0.9.2" "numba<0.57" "cython<1" "onnxruntime==1.20.1" "onnx<1.16" six "tensorboard<2.12" "protobuf<4" "setuptools<70" wheel || exit 1
python -c "import torch; print('torch', torch.__version__, 'cuda', torch.cuda.is_available(), torch.cuda.get_device_name(0))" || exit 1
[ -d piper ] || git clone -q --depth 1 https://github.com/rhasspy/piper piper || exit 1
(cd piper/src/python && bash build_monotonic_align.sh) || exit 1
H=https://huggingface.co/datasets/rhasspy/piper-checkpoints/resolve/main/ar/ar_JO/kareem/medium
curl -sfL -o kareem.json "$H/config.json" || exit 1
[ -f start.ckpt ] || curl -sfL -o start.ckpt "$H/epoch%3D5079-step%3D1682020.ckpt" || exit 1
DATA=$(dirname "$(find "$HERE" /kaggle/input -name metadata.jsonl | head -1)")
PYTHONPATH=piper/src/python python "$HERE/prep.py" "$DATA" ds kareem.json || exit 1
E0=$(python -c "import torch; print(torch.load('start.ckpt', map_location='cpu')['epoch'])"); echo "start epoch $E0"
END=$(( $(date +%s) + HOURS * 3600 )); CK=$T/start.ckpt
cd piper/src/python
for k in $(seq 1 $STAGES); do
  LEFT=$(( (END - $(date +%s)) / 60 )); [ $LEFT -lt 20 ] && break
  timeout ${LEFT}m python -m piper_train --dataset-dir $T/ds --accelerator gpu --devices 1 \
    --batch-size $BATCH --validation-split 0 --num-test-examples 0 --max_epochs $((E0 + PER * k)) \
    --resume_from_checkpoint $CK --checkpoint-epochs 1 --precision 32 \
    --max-phoneme-ids 800 --quality medium --log_every_n_steps 10 2>&1 | grep -v "DEBUG"
  C=$(ls -t $T/ds/lightning_logs/version_*/checkpoints/*.ckpt 2>/dev/null | head -1); echo "stage $k checkpoint: $C"
  [ -z "$C" ] && break
  cp "$C" $W/last.ckpt; CK=$W/last.ckpt
  N=$(printf "%03d" $((PER * k)))
  python -m piper_train.export_onnx $W/last.ckpt $W/kareem-ft-e$N.onnx && cp $T/kareem.json $W/kareem-ft-e$N.onnx.json
  ls -la $W
done
echo DONE
