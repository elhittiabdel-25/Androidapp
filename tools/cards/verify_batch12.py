"""Check batch-12 cards (Ibn Baz on hajj, al-Uthaymin on the udhiya) before anything is built: quoted phrases
against the book texts, cited pages of the OCR'd book, verse references, sentence and tashkeel rules, related ids, and that
the voice never reads a verse (no five words in a row from the Quran in a spoken segment).

    BOOKS_TXT=DIR python3 tools/cards/verify_batch12.py

DIR is the sources branch's sources/ folder: ocr/ and text/ 114224.txt and 115196.txt (OCR and PDF text layer, "=== PAGE n ===",
matched approximately).
"""
import os
import re
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
sys.path.insert(0, os.path.dirname(HERE))
import verify_common as v3                                                  # noqa: E402
import cards_batch12 as b12                                                 # noqa: E402
from cards_batch10 import NEW10                                              # noqa: E402
from cards_batch9 import NEW9                                                # noqa: E402
from cards_batch8 import NEW8                                                # noqa: E402
from cards_batch7 import NEW7                                                # noqa: E402
from cards_batch6 import NEW6                                                # noqa: E402
from cards_batch5 import NEW5                                                # noqa: E402
from cards_batch4 import NEW4                                                # noqa: E402
from cards_batch3 import NEW3                                                # noqa: E402
from cards_batch2 import NEW as NEW2                                         # noqa: E402
from cards_src import CARDS as OLD                                           # noqa: E402

ROOT = os.environ.get("BOOKS_TXT", "")
from cards_batch11 import NEW11                                             # noqa: E402
NEW12 = b12.NEW12
PAGE_CHECKS12 = getattr(b12, "PAGE_CHECKS12", [])

exact = {}
ocr = {"hajj": v3.pages(os.path.join(ROOT, "ocr/114224.txt")), "udh": v3.pages(os.path.join(ROOT, "ocr/115196.txt"))}
ocr_all = {k: "".join(v.values()) for k, v in ocr.items()}
for k, bid in (("hajj", "114224"), ("udh", "115196")):                 # the PDF text layer, where the OCR drops lines
    ocr_all[k] += "\n" + "".join(v3.pages(os.path.join(ROOT, f"text/{bid}.txt")).values())
NEED = {"hajj": 0.8, "udh": 0.8}

problems, n_checks = [], 0
ids = [c["id"] for c in OLD] + [c["id"] for c in NEW2] + [c["id"] for c in NEW3] + [c["id"] for c in NEW4] + \
      [c["id"] for c in NEW5] + [c["id"] for c in NEW6] + [c["id"] for c in NEW7] + [c["id"] for c in NEW8] + \
      [c["id"] for c in NEW9] + [c["id"] for c in NEW10] + [c["id"] for c in NEW11] + [c["id"] for c in NEW12]
dups = sorted({i for i in ids if ids.count(i) > 1})
if dups:
    problems.append(f"duplicate ids: {dups}")

for c in NEW12:
    cid = c["id"]
    for book, phrase in c.get("check", []):
        n_checks += 1
        if book in exact:
            if v3.norm(phrase) not in exact[book]:
                problems.append(f"{cid}: not found in {book}: «{phrase}»")
        else:
            need = NEED.get(book, 0.85)
            r = v3.fuzzy_in(phrase, ocr_all[book], need)
            if r < need:
                problems.append(f"{cid}: not found in {book} (best {r:.2f}): «{phrase}»")
    for q in c["quran"]:
        ref, part = (q, None) if isinstance(q, str) else q
        try:
            vt = v3.verse_text(ref)
        except KeyError:
            problems.append(f"{cid}: bad verse ref {ref}")
            continue
        if part and v3.norm(part) not in v3.norm(vt):
            problems.append(f"{cid}: excerpt not in {ref}: «{part}»")
    for i, s in enumerate(c["face"]):
        plain = v3.H.sub("", s).strip()
        if len(v3.SPLIT.split(plain)) != 1 or not plain.endswith((".", "؟")):
            problems.append(f"{cid}: face {i} must be one sentence ending with . or ؟")
    segs = [("title", c["title"])] + [(f"face{i}", s) for i, s in enumerate(c["face"])] + \
           [(f"deep{i}", s) for i, s in enumerate(c["deeper"])] + [(f"had{i}", t) for i, (t, _) in enumerate(c["hadith"])]
    for tag, s in segs:
        if v3.H.sub("", s).startswith("ر"):
            problems.append(f"{cid} {tag}: starts with ر (voice weakens it)")
        bare = [w for w in re.findall(r"[ء-يً-ْٰ]+", s) if len(v3.H.sub("", w)) > 2 and not v3.H.search(w)]
        if bare:
            problems.append(f"{cid} {tag}: words without tashkeel: {bare[:6]}")
        w = v3.words(v3.spoken_text(s))
        hits = {" ".join(w[i:i + 5]) for i in range(len(w) - 4)} & v3.QW
        if hits and not tag.startswith("had"):
            problems.append(f"{cid} {tag}: reads Quran words: {sorted(hits)[:2]}")
    for r in c["related"]:
        if r not in ids:
            problems.append(f"{cid}: related id not found: {r}")

for book, page, phrase in PAGE_CHECKS12:
    n_checks += 1
    need = min(0.8, NEED.get(book, 0.8) - 0.03)
    r = v3.fuzzy_in(phrase, ocr[book].get(page, ""), need)
    if r < need:
        problems.append(f"page check: {book} p.{page} (best {r:.2f}): «{phrase}»")

print(f"{len(NEW12)} new cards, {n_checks} phrases and pages checked, {sum(len(c['quran']) for c in NEW12)} verse refs checked")
print(f"{len(problems)} problems")
print("\n".join(problems))
