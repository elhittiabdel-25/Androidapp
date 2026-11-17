"""Shorten scene beats that now outlast their sentence (after a re-voice made the clips quicker).

For every card with a scene, each beat's playing time is estimated from the voice as in beatdur.py. A beat whose
beatMillis is more than 10% longer than the shortest estimate for its kind is set to that estimate (rounded down
to 250 ms, never under 800 ms); beats are never lengthened. Edits Art.kt in place and prints what changed.

    python3 tools/scenes/retime.py            (from the repo root)
"""
import json, re, subprocess, sys
sys.path.insert(0, 'tools/cards')
from scenes import SCENES

ART = 'app/src/main/java/app/usulcards/ui/Art.kt'
speech = json.load(open('tools/tts/speech.json'))
HARAKAT = re.compile('[ً-ْٰـ]')
plain = lambda s: HARAKAT.sub('', s)
DUR = {}


def clip_text(cid, at):
    sp = speech[cid]
    if at == 'title':
        return sp['title']
    part, n = at.rsplit('-', 1)
    v = sp[{'face': 'face', 'deep': 'deeper', 'had': 'hadith'}[part]]
    return v[int(n)] if isinstance(v, list) else v


def dur(cid, at):
    f = f'app/src/main/assets/audio/{cid}/{at}.ogg'
    if f not in DUR:
        out = subprocess.run(['ffprobe', '-v', 'quiet', '-show_entries', 'format=duration', '-of', 'csv=p=0', f],
                             capture_output=True, text=True).stdout.strip()
        DUR[f] = float(out) * 1000 if out else 0
    return DUR[f]


est = {}
for cid, sc in SCENES.items():
    if cid not in speech:
        continue
    at, rows = None, []
    for it in sc['items']:
        at = it.get('at', at)
        if at is None:
            continue
        try:
            txt = plain(clip_text(cid, at))
        except (KeyError, IndexError, ValueError):
            continue
        p = txt.find(plain(it['cue'])) if it.get('cue') else 0
        rows.append((it['kind'], at, max(p, 0), len(txt)))
    for i, (kind, at, pos, n) in enumerate(rows):
        nxt = rows[i + 1][2] if i + 1 < len(rows) and rows[i + 1][1] == at else n
        d = dur(cid, at)
        if d and nxt > pos:
            est.setdefault(kind, []).append(d * (nxt - pos) / max(1, n))

s = open(ART).read()
head = s.index('internal fun beatMillis')
end = s.index('    else -> 1000\n', head)
block = s[head:end]
changed = {}


def fix(m):
    kinds = re.findall(r'"([^"]+)"', m.group(1))
    ms = int(m.group(2))
    keep, moved = [], []
    for k in kinds:
        if k in est:
            new = max(800, int(min(est[k]) // 250 * 250))
            if ms > new * 1.1:
                moved.append(k); changed[k] = (ms, new); continue
        keep.append(k)
    if not moved:
        return m.group(0)
    return ('    ' + ', '.join(f'"{k}"' for k in keep) + f' -> {ms}\n') if keep else ''


block = re.sub(r'^    ((?:"[^"]+"(?:, )?)+) -> (\d+)\n', fix, block, flags=re.M)
groups = {}
for k, (_, new) in changed.items():
    groups.setdefault(new, []).append(k)
add = '    // shortened for the enhanced voice (retime.py)\n' + ''.join(
    '    ' + ', '.join(f'"{k}"' for k in sorted(ks)) + f' -> {v}\n' for v, ks in sorted(groups.items()))
s = s[:head] + block + (add if changed else '') + s[end:]
open(ART, 'w').write(s)
print(len(changed), 'beats shortened;', len(est), 'kinds estimated')
for k, (o, n) in sorted(changed.items()):
    print(f'  {k}: {o} -> {n}')
