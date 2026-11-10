"""add_scenes.py wNN.py: add a wave's scene specs to scenes.py, its beats to finish_pack.ART_KINDS, its figure
layouts to variety.FIGURE_LAYOUTS and its notebook layouts to ArtCinema's paper set (the day set goes through wire2)."""
import re, sys, runpy
R = '/home/claude/androidapp'
m = runpy.run_path(sys.argv[1])
SC, META = m['SC'], m['META']
p = f'{R}/tools/cards/scenes.py'; s = open(p).read().rstrip()
assert s.endswith('}')
ids = re.findall(r'^    "([\w-]+)": S\(', SC, re.M)
for i in ids:
    if f'"{i}": S(' in s: sys.exit(f'{i} already in scenes.py')
s = s[:-1].rstrip() + '\n' + SC.strip('\n') + '\n}\n'
open(p, 'w').write(s)
p = f'{R}/tools/cards/finish_pack.py'; s = open(p).read()
end = s.index('\n}', s.index('ART_KINDS = {'))
add = ''.join(f'    "{L}": {{{", ".join(repr(k).replace(chr(39), chr(34)) for k in v["kinds"])}}},\n' for L, v in META.items() if f'"{L}":' not in s[:end])
s = s[:end] + '\n' + add.rstrip('\n') + s[end:]
open(p, 'w').write(s)
fig = [L for L, v in META.items() if v.get('figure')]
if fig:
    p = f'{R}/tools/cards/variety.py'; s = open(p).read()
    s = s.replace('"brainset"}', '"brainset", ' + ', '.join(f'"{L}"' for L in fig) + '}', 1) if '"brainset"}' in s else re.sub(r'(FIGURE_LAYOUTS = \{[^}]*)\}', lambda mm: mm.group(1) + ', ' + ', '.join(f'"{L}"' for L in fig) + '}', s, 1)
    open(p, 'w').write(s)
pap = [L for L, v in META.items() if v.get('paper')]
if pap:
    p = f'{R}/app/src/main/java/app/usulcards/ui/ArtCinema.kt'; s = open(p).read()
    mm = re.search(r'val paper = setOf\(([^)]*)\)', s)
    s = s[:mm.end() - 1] + ''.join(f', "{L}"' for L in pap) + s[mm.end() - 1:]
    open(p, 'w').write(s)
print('added', ids, '| figure', fig, '| paper', pap, '| day', [L for L, v in META.items() if v.get('day')])
