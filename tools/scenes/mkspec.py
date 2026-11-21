"""mkspec.py ROWS.json OUT.py "label": write a wave spec (SC + META) from {card: [[label, where, sub], x4]}.
Each label is an unvowelled phrase of the item text; sub is the vowelled tag text, parts split by |."""
import sys, json
# mkspec.py rows.json out.py label
rows = json.load(open(sys.argv[1])); out = ["SC = '''", f"    # ── {sys.argv[3]} ──"]; L = []
for cid, items in rows.items():
    lay = cid.replace("-", ""); L.append(lay)
    out.append(f'    "{cid}": S("art", [')
    for i, (lab, where, sub) in enumerate(items):
        out.append(f'        I("{lab}", None, "{where}", sub="{sub}", kind="{lay}{i}"),')
    out.append(f'    ], layout="{lay}"),')
out += ["'''", f'META = {{L: dict(kinds=[f"{{L}}{{i}}" for i in range(4)]) for L in {L!r}}}']
open(sys.argv[2], "w").write("\n".join(out) + "\n"); print(" ".join(L))
