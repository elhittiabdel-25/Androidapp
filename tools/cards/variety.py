"""Checks from the visual codex (Re-Iman visual codex artifact, agreed on 2026-10-21), run by finish_pack.py.

Codex (errors): the Prophet ﷺ and the other prophets are only light (or their name), never a lantern; a named
prophet, companion or angel is never a tag in a scene drawn with figures. The checks read the tags, so a new
scene still needs a person's eye: a figure must stand for anyone, never for someone named.
Variety (reported, and errors once VARIETY_STRICT=1): a layout serves at most MAX_USES cards; two neighbouring
cards in a path never share a layout.
"""
import collections, os, re, unicodedata

MAX_USES = 6
LANTERN_KINDS = {"lamp", "keep"}                           # beats drawn as a brass lantern
FIGURE_LAYOUTS = {"verdict", "crowd", "deeds", "gifts", "camp", "study", "canyon", "books", "caravan"}   # scenes drawn with faceless figures
# a prophet named in a tag (a lantern may carry the companions' or the scholars' knowledge, never a prophet)
PROPHET = re.compile(r"ﷺ|النبي|الرسول|رسول|نبي|الأنبياء|أنبياء|الرسل|محمد")
# a figure must never be one of these; ʿAlī is left out (عَلِيّ and عَلَيَّ look alike without harakat)
NAMED = re.compile(r"ﷺ|النبي\b|محمد|أبو بكر|أبي بكر|\bعمر\b|عثمان|بلال|الصحابي|جبريل|ملك الموت")


def _bare(s):
    return "".join(ch for ch in s if unicodedata.category(ch) != "Mn")


def codex_errors(cid, spec):
    out = []
    if spec["type"] != "art":
        return out
    for n, it in enumerate(spec["items"]):
        sub = _bare(it["sub"])
        if it["kind"] in LANTERN_KINDS and PROPHET.search(sub):
            out.append(f"item {n}: «{it['sub']}» is drawn as a lantern; a prophet is only light (kind light)")
        if spec["layout"] in FIGURE_LAYOUTS and NAMED.search(sub):
            out.append(f"item {n}: «{it['sub']}» names a prophet, companion or angel in a scene with figures")
    return out


def variety_report(cards, scenes):
    """Returns (problems, summary line)."""
    lay = {cid: (s["layout"] or s["type"]) for cid, s in scenes.items()}
    uses = collections.Counter(lay.values())
    problems = [f"layout «{k}» serves {n} cards (at most {MAX_USES})" for k, n in uses.most_common() if n > MAX_USES]
    by = collections.defaultdict(list)
    for c in cards:
        if c["id"] in lay:
            by[c["path"]].append(c)
    for p, cs in by.items():
        cs.sort(key=lambda c: c["order"])
        for a, b in zip(cs, cs[1:]):
            if lay[a["id"]] == lay[b["id"]]:
                problems.append(f"{a['id']} and {b['id']} follow each other in «{p}» with the same layout «{lay[a['id']]}»")
    top = sum(n for _, n in uses.most_common(5))
    summary = f"variety: {len(uses)} layouts for {len(lay)} scenes; the 5 most used carry {top} ({100 * top // max(len(lay), 1)}%)"
    return problems, summary


STRICT = os.environ.get("VARIETY_STRICT") == "1"
