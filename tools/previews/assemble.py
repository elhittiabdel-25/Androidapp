"""Turn the recorded snapshots into review material: per scene a contact sheet of its beats (sheet-<id>.png),
its dark-theme still, and for motion scenes an animated GIF (anim-<id>.gif) plus the raw frames.

    python3 tools/previews/assemble.py <snapshots dir> <out dir>
"""
import glob
import os
import re
import shutil
import sys

from PIL import Image


def main(src, out):
    os.makedirs(out, exist_ok=True)
    shots = sorted(glob.glob(os.path.join(src, "**", "*.png"), recursive=True))
    groups = {}
    for p in shots:
        m = re.search(r"_([a-z0-9-]+)__(s\d+|dark|m\d+)\.png$", os.path.basename(p))
        if m:
            groups.setdefault(m.group(1), []).append((m.group(2), p))
    for sid, files in sorted(groups.items()):
        stills = sorted(p for k, p in files if k.startswith("s"))
        frames = sorted(p for k, p in files if k.startswith("m"))
        dark = [p for k, p in files if k == "dark"]
        if stills:
            ims = [Image.open(p).convert("RGB") for p in stills]
            w = 420
            ims = [im.resize((w, round(im.height * w / im.width))) for im in ims]
            cols = 3
            rows = (len(ims) + cols - 1) // cols
            hmax = max(im.height for im in ims)
            sheet = Image.new("RGB", (cols * w + (cols + 1) * 8, rows * hmax + (rows + 1) * 8), (40, 40, 48))
            for i, im in enumerate(ims):
                sheet.paste(im, (8 + (i % cols) * (w + 8), 8 + (i // cols) * (hmax + 8)))
            sheet.save(os.path.join(out, f"sheet-{sid}.png"), optimize=True)
        for p in dark:
            shutil.copy(p, os.path.join(out, f"dark-{sid}.png"))
        if frames:
            ims = [Image.open(p).convert("RGB") for p in frames]
            w = 480
            ims = [im.resize((w, round(im.height * w / im.width)), Image.LANCZOS) for im in ims]
            pal = [im.quantize(colors=200, method=Image.Quantize.MEDIANCUT) for im in ims]
            pal[0].save(os.path.join(out, f"anim-{sid}.gif"), save_all=True, append_images=pal[1:], duration=83, loop=0, optimize=True)
            fdir = os.path.join(out, "frames", sid)
            os.makedirs(fdir, exist_ok=True)
            for i, p in enumerate(frames):
                if i % 3 == 0:
                    Image.open(p).convert("RGB").save(os.path.join(fdir, f"{i:04d}.jpg"), quality=85)
        print(f"{sid}: {len(stills)} stills, {len(frames)} frames")


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])
