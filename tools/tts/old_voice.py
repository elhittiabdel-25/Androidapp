"""List the clips still in the old Kareem voice, so the app plays them at 1.25x (the old voice is slower).

On 2026-11-17 every clip was re-voiced with the enhanced Kareem (kareem-ft-e399), then revoice_pick.py put the old
clip back wherever the speech check understood it better (commit c4b405a). A clip is in the old voice when its bytes
are the same as at OLD_REF, the last commit before the re-voicing. Writes app/src/main/assets/audio/old-kareem.txt
(one asset path a line). Run after any re-voicing or best take, before committing."""
import os, subprocess

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
OLD_REF = "8ecec7d"
AUDIO = "app/src/main/assets/audio"


def git(*a):
    return subprocess.run(["git", "-C", ROOT, *a], capture_output=True, text=True, check=True).stdout


try:
    git("cat-file", "-e", OLD_REF + "^{commit}")
except subprocess.CalledProcessError:   # a shallow clone (CI): keep the list as committed
    print("old_voice: no", OLD_REF, "here, old-kareem.txt left as it is")
    raise SystemExit(0)
old_blob = {}
for line in git("ls-tree", "-r", OLD_REF, "--", AUDIO).splitlines():
    meta, path = line.split("\t", 1)
    if path.endswith(".ogg"):
        old_blob[path] = meta.split()[2]
paths = sorted(p for p in old_blob if os.path.exists(os.path.join(ROOT, p)))
now = git("hash-object", "--", *paths).split() if paths else []
old = [os.path.relpath(p, "app/src/main/assets") for p, h in zip(paths, now) if h == old_blob[p]]
open(os.path.join(ROOT, AUDIO, "old-kareem.txt"), "w").write("\n".join(old) + "\n")
print(len(old), "clips in the old voice of", len(paths), "that existed before the re-voicing")
