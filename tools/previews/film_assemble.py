"""Turn the recorded card frames into films with the card's voice: film-<id>.mp4.

    python3 tools/previews/film_assemble.py <snapshots dir> <plan.json> <out dir>
"""
import glob
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile


def main(src, plan_path, out):
    os.makedirs(out, exist_ok=True)
    plan = json.load(open(plan_path))
    shots = sorted(glob.glob(os.path.join(src, "**", "*.png"), recursive=True))
    for cid, p in plan.items():
        frames = sorted(s for s in shots if re.search(rf"_film__{re.escape(cid)}__f\d+\.png$", os.path.basename(s)))
        if not frames:
            print("no frames for", cid)
            continue
        tmp = tempfile.mkdtemp()
        for i, f in enumerate(frames):
            shutil.copy(f, os.path.join(tmp, f"{i:05d}.png"))
        cmd = ["ffmpeg", "-v", "error", "-y", "-framerate", "12", "-i", os.path.join(tmp, "%05d.png")]
        for c in p["clips"]:
            cmd += ["-i", c["file"]]
        mix = "".join(f"[{k + 1}:a]adelay={int(c['start'] * 1000)}:all=1[a{k}];" for k, c in enumerate(p["clips"]))
        mix += "".join(f"[a{k}]" for k in range(len(p["clips"]))) + f"amix=inputs={len(p['clips'])}:normalize=0,apad[a]"
        cmd += ["-filter_complex", mix, "-map", "0:v", "-map", "[a]", "-shortest",
                "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2,format=yuv420p", "-r", "24",
                "-c:v", "libx264", "-crf", "21", "-preset", "slow", "-c:a", "aac", "-b:a", "128k",
                "-movflags", "+faststart", os.path.join(out, f"film-{cid}.mp4")]
        subprocess.run(cmd, check=True)
        print(cid, len(frames), "frames")


if __name__ == "__main__":
    main(*sys.argv[1:4])
