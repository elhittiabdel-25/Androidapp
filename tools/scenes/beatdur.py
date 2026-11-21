"""Estimate each beat's playing time (ms) from the voice: time from its cue to the next cue in the same clip, or the clip's end.
usage: python3 beatdur.py id [id...]   (run from the repo root)"""
import json, re, subprocess, sys
sys.path.insert(0, 'tools/cards')
from scenes import SCENES
speech = json.load(open('tools/tts/speech.json'))
HARAKAT = re.compile('[ً-ْٰـ]')
def plain(s): return HARAKAT.sub('', s)
def clip_text(cid, at):
    sp = speech[cid]
    if at == 'title': return sp['title']
    part, n = at.rsplit('-', 1)
    key = {'face': 'face', 'deep': 'deeper', 'had': 'hadith'}[part]
    v = sp[key]
    return v[int(n)] if isinstance(v, list) else v
def dur(cid, at):
    f = f'app/src/main/assets/audio/{cid}/{at}.ogg'
    out = subprocess.run(['ffprobe', '-v', 'quiet', '-show_entries', 'format=duration', '-of', 'csv=p=0', f], capture_output=True, text=True).stdout.strip()
    return float(out) * 1000 if out else 0
res = {}
for cid in sys.argv[1:]:
    items = SCENES[cid]['items']
    at = None; rows = []
    for it in items:
        at = it.get('at', at)
        txt = plain(clip_text(cid, at))
        pos = 0
        if it['cue']:
            p = txt.find(plain(it['cue']))
            pos = p if p >= 0 else 0
        rows.append([it['kind'], at, pos, len(txt)])
    for i, r in enumerate(rows):
        kind, at, pos, n = r
        nxt = n
        if i + 1 < len(rows) and rows[i + 1][1] == at: nxt = rows[i + 1][2]
        ms = dur(cid, at) * max(0, nxt - pos) / max(1, n)
        print(f'{cid:22s} {kind:14s} {at:7s} {ms:6.0f}')
        res.setdefault(kind, []).append(ms)
print(json.dumps({k: int(round(min(v) / 250) * 250) for k, v in res.items()}))
