import re,sys,json
# usage: wire2.py '<json: {layouts:[...], beats:{kind:ms}, big:[...], day:[...], label:"..."}>'
cfg=json.loads(open(sys.argv[1]).read())
p='app/src/main/java/app/usulcards/ui/Art.kt'; s=open(p).read()
body=s[s.index('internal fun beatMillis'):]; body=body[:body.index('else -> 1000')]
existing=set(re.findall(r'"([^"]+)"',body))
dup=[k for k in cfg['beats'] if k in existing]
if dup: sys.exit("dup kinds: %s"%dup)
anchor='        else -> drawRect(fallback)\n'
s=s.replace(anchor,''.join(f'        "{L}" -> {L}Scene(a)\n' for L in cfg['layouts'])+anchor,1)
from collections import defaultdict
g=defaultdict(list)
for k,v in cfg['beats'].items(): g[v].append(k)
lines=''.join('    '+', '.join(f'"{k}"' for k in ks)+f' -> {v}\n' for v,ks in sorted(g.items()))
s=s.replace('    else -> 1000\n',f'    // {cfg["label"]}\n'+lines+'    else -> 1000\n',1)
if cfg.get('big'):
    s=re.sub(r'(internal val BIG_TEXT_LAYOUTS = setOf\([^)]*)\)', lambda m: m.group(1)+', '+', '.join(f'"{x}"' for x in cfg['big'])+')', s, count=1)
open(p,'w').write(s)
c='app/src/main/java/app/usulcards/ui/ArtCinema.kt'; t=open(c).read()
if cfg.get('day'):
    t=re.sub(r'(val day = setOf\([^)]*)\)', lambda m: m.group(1)+', '+', '.join(f'"{x}"' for x in cfg['day'])+')', t, count=1)
open(c,'w').write(t)
print("wired", cfg['layouts'])
