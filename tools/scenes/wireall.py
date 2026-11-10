import json, re, runpy, subprocess, sys
SP = '/tmp/claude-0/-home-claude/d708ba9f-c3ae-50d1-bb80-371d1a649380/scratchpad'
layouts, ids, day = [], [], []
for n in range(int(sys.argv[1]), int(sys.argv[2]) + 1):
    m = runpy.run_path(f'{SP}/sc/w{n}.py')
    layouts += list(m['META'].keys())
    day += [L for L, v in m['META'].items() if v.get('day')]
    ids += re.findall(r'^    "([\w-]+)": S\(', m['SC'], re.M)
out = subprocess.run(['python3', f'{SP}/tools/beatdur.py'] + ids, capture_output=True, text=True)
if out.returncode: sys.exit(out.stderr)
beats = json.loads(out.stdout.strip().splitlines()[-1])
beats = {k: max(1500, min(6000, v)) for k, v in beats.items()}
json.dump({"layouts": layouts, "beats": beats, "day": day}, open(sys.argv[3], 'w'), ensure_ascii=False)
print(len(layouts), 'layouts', len(ids), 'cards', len(beats), 'beats', len(day), 'day')
