"""Check batch-4 cards before anything is built: quoted phrases against the book texts, cited pages of the
OCR'd books, verse references and excerpts, sentence and tashkeel rules, related ids, and that the voice
never reads a verse (no five words in a row from the Quran in a spoken segment).

    BOOKS_TXT=DIR python3 tools/cards/verify_batch4.py

DIR holds html/90456.html (Ibn Baz's comments are footnotes there), txt/89743.plain.txt, txt/5845.txt, and
ocr/115995.txt, ocr/116143.txt (OCR with "=== PAGE n ===" markers, matched approximately).
"""
import html as htmllib
import os
import re
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
sys.path.insert(0, os.path.dirname(HERE))
import verify_common as v3                                                  # noqa: E402
from cards_batch4 import NEW4, MERGE_SOURCES4, MERGE_CHECKS4, PAGE_CHECKS4   # noqa: E402
from cards_batch3 import NEW3                                                # noqa: E402
from cards_batch2 import NEW as NEW2                                         # noqa: E402
from cards_src import CARDS as OLD                                           # noqa: E402

ROOT = os.environ.get("BOOKS_TXT", "")


def tahawi_text():
    """The matn with Ibn Baz's footnoted comments appended."""
    t = open(os.path.join(ROOT, "html/90456.html"), encoding="utf-8", errors="replace").read()
    body = re.sub(r'<div id="sdfootnote\d+">.*?</div>', '', t, flags=re.S)
    notes = re.findall(r'<div id="sdfootnote(\d+)">(.*?)</div>', t, re.S)

    def clean(x):
        x = re.sub(r'<br\s*/?>|</p>', '\n', x)
        return htmllib.unescape(re.sub(r'<[^>]+>', '', x))
    return clean(body) + "\n" + "\n".join(clean(b) for _, b in notes)


exact = {
    "tahawi": v3.norm(tahawi_text()),
    "izz": v3.norm(open(os.path.join(ROOT, "txt/89743.plain.txt"), encoding="utf-8").read()),
    "walan": v3.norm(open(os.path.join(ROOT, "txt/5845.txt"), encoding="utf-8").read()),
}
ocr = {"dalail": v3.pages(os.path.join(ROOT, "ocr/115995.txt")), "shuab": v3.pages(os.path.join(ROOT, "ocr/116143.txt"))}
ocr_all = {k: "".join(v.values()) for k, v in ocr.items()}

problems, n_checks = [], 0
ids = [c["id"] for c in OLD] + [c["id"] for c in NEW2] + [c["id"] for c in NEW3] + [c["id"] for c in NEW4]
dups = sorted({i for i in ids if ids.count(i) > 1})
if dups:
    problems.append(f"duplicate ids: {dups}")

for c in NEW4:
    cid = c["id"]
    for book, phrase in c.get("check", []):
        n_checks += 1
        if book in exact:
            if v3.norm(phrase) not in exact[book]:
                problems.append(f"{cid}: not found in {book}: «{phrase}»")
        else:
            r = v3.fuzzy_in(phrase, ocr_all[book])
            if r < 0.85:
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

for book, phrase in MERGE_CHECKS4:
    n_checks += 1
    if v3.norm(phrase) not in exact[book]:
        problems.append(f"merge check not found in {book}: «{phrase}»")
for book, page, phrase in PAGE_CHECKS4:
    n_checks += 1
    r = v3.fuzzy_in(phrase, ocr[book].get(page, ""), 0.8)
    if r < 0.8:
        problems.append(f"page check: {book} p.{page} (best {r:.2f}): «{phrase}»")
for cid in MERGE_SOURCES4:
    if cid not in ids:
        problems.append(f"merge target missing: {cid}")

print(f"{len(NEW4)} new cards, {n_checks} phrases and pages checked, {sum(len(c['quran']) for c in NEW4)} verse refs checked")
print(f"{len(problems)} problems")
print("\n".join(problems))
