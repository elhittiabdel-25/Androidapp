"""Keep, clip by clip, whichever voice the speech check understands better.

Takes the speech-check reports of the new clips and of the old ones (speech-check.json files, any number of each)
and puts the old clip back (from git, at OLD_REF) wherever its WER is lower than the new one's by more than MARGIN.
Ties keep the new voice. Prints a summary; `git status` shows which clips went back.

    python3 tools/tts/revoice_pick.py OLD_REF new1.json [new2.json ...] -- old1.json [old2.json ...]
"""
import json, subprocess, sys

MARGIN = 0.05


def load(paths):
    return {(r["card"], r["key"]): r["wer"] for p in paths for r in json.load(open(p))}


def main(argv):
    ref, rest = argv[0], argv[1:]
    cut = rest.index("--")
    new, old = load(rest[:cut]), load(rest[cut + 1:])
    both = [k for k in new if k in old]
    pick = sorted(k for k in both if old[k] < new[k] - MARGIN)
    done = 0
    for card, key in pick:
        path = f"app/src/main/assets/audio/{card}/{key}.ogg"
        clip = subprocess.run(["git", "show", f"{ref}:{path}"], capture_output=True).stdout
        if clip:
            open(path, "wb").write(clip)
            done += 1
    mean = lambda ks, d: sum(d[k] for k in ks) / max(1, len(ks))
    best = {k: min(new[k], old[k]) if k in pick else new[k] for k in both}
    print(f"{len(both)} clips compared; old voice kept for {done} ({len(pick) - done} had no old clip)")
    print(f"mean WER: new {mean(both, new):.3f}, old {mean(both, old):.3f}, kept {mean(both, best):.3f}")


if __name__ == "__main__":
    main(sys.argv[1:])
