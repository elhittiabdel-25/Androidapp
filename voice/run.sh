#!/bin/bash
# The whole training run; the workflow tees its output to run.log and publishes it with the results.
set -x
source voice/train.cfg
git clone -q --depth 1 https://github.com/rhasspy/piper piper || exit 1
python -m pip install -q "pip<24.1"
pip install -q --extra-index-url https://download.pytorch.org/whl/cpu "torch==1.13.1+cpu" "pytorch-lightning==1.7.7" \
  "torchmetrics==0.11.4" "numpy<1.24" "librosa==0.9.2" "numba<0.57" "cython<1" "onnxruntime<1.17" "onnx<1.16" || exit 1
python -c "import torch, pytorch_lightning; print('torch', torch.__version__, 'lightning', pytorch_lightning.__version__)" || exit 1
(cd piper/src/python && bash build_monotonic_align.sh) || exit 1
H=https://huggingface.co/datasets/rhasspy/piper-checkpoints/resolve/main/ar/ar_JO/kareem/medium
curl -sfL -o kareem.json "$H/config.json" || exit 1
if [ "$RESUME" = "1" ] && gh release download voice-ft -p last.ckpt -D . ; then mv last.ckpt start.ckpt
else curl -sfL -o start.ckpt "$H/epoch%3D5079-step%3D1682020.ckpt" || exit 1; fi
ls -la start.ckpt kareem.json
PYTHONPATH=piper/src/python python voice/prep.py voice/data ds kareem.json || exit 1
E0=$(python -c "import torch; print(torch.load('start.ckpt', map_location='cpu')['epoch'])")
echo "start epoch $E0"
cd piper/src/python
timeout ${MINUTES}m python -m piper_train --dataset-dir ../../../ds --accelerator cpu --devices 1 \
  --batch-size $BATCH --validation-split 0 --num-test-examples 0 --max_epochs $((E0 + EPOCHS)) \
  --resume_from_checkpoint ../../../start.ckpt --checkpoint-epochs 1 --precision 32 \
  --max-phoneme-ids 800 --quality medium --log_every_n_steps 5
echo "train exit $?"
C=$(ls -t lightning_logs/version_*/checkpoints/*.ckpt 2>/dev/null | head -1)
echo "last checkpoint: $C"
if [ -n "$C" ]; then
  mkdir -p ../../../pub && cp "$C" ../../../pub/last.ckpt
  python -m piper_train.export_onnx "../../../pub/last.ckpt" ../../../pub/kareem-ft.onnx && cp ../../../kareem.json ../../../pub/kareem-ft.onnx.json
fi
