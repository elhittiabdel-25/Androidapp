"""kindclash.py wNN.py: kinds of a wave that another layout already uses, or that Art.kt's beatMillis already names."""
import re, sys, runpy
sys.path.insert(0, '/home/claude/androidapp/tools/cards')
import importlib, finish_pack as fp
m = runpy.run_path(sys.argv[1]); META = m['META']
art = open('/home/claude/androidapp/app/src/main/java/app/usulcards/ui/Art.kt').read()
body = art[art.index('internal fun beatMillis'):]; body = body[:body.index('else -> 1000')]
named = set(re.findall(r'"([^"]+)"', body))
own = set(META)
bad = []
other = {k: L for L, ks in fp.ART_KINDS.items() if L not in own for k in ks}
for L, v in META.items():
    for k in v['kinds']:
        if k in other: bad.append(1); print(f'{L}.{k} clashes with layout {other[k]}')
        if k in named: bad.append(1); print(f'{L}.{k} already in beatMillis')
seen = {}
for L, v in META.items():
    for k in v['kinds']:
        if k in seen: bad.append(1); print(f'{L}.{k} also in {seen[k]}')
        seen[k] = L
print('checked', sum(len(v['kinds']) for v in META.values()), 'kinds')

if bad: sys.exit(1)
