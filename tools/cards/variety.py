"""Checks from the visual codex (tools/cards/CODEX.md; the "Re-Iman visual codex" artifact), run by finish_pack.py.

Codex (errors): the Prophet ﷺ and the other prophets are only light (or their name), never a lantern; a lantern
never stands for people (they are figures, or names in roundels); a named prophet, companion or angel is never a
tag in a scene drawn with figures. The checks read the tags, the captions and the sentence the lantern is drawn
in, so a new scene still needs a person's eye: a figure must stand for anyone, never for someone named.
Cards in PENDING broke a rule before the codex and are being redrawn: their breaks are notes until then.
Variety (reported, and errors once VARIETY_STRICT=1): a layout serves at most MAX_USES cards; two neighbouring
cards in a path never share a layout; the lantern is drawn in at most MAX_USES cards.
"""
import collections, os, re, unicodedata

MAX_USES = 6
# where the brass lantern is drawn: every beat of these layouts ("*"), or these beats of them
LANTERN = {"lantern": "*", "ridge": "*", "trail": {"lamp", "keep", "stuck"}, "building": {"light"},
           "cave": {"follow"}}
FIGURE_LAYOUTS = {"verdict", "crowd", "deeds", "gifts", "camp", "study", "canyon", "books", "caravan",
                  "athar", "sabil", "shore", "mosque", "maze", "field", "street",
                  "atelier", "heavens", "chamber", "scroll", "majlis", "lane", "hilltop", "wayside",
                  "flock", "deck", "dam", "aerial", "souk", "bridge", "refuge", "sapling", "return",
                  "shelfshop", "plaza", "stall", "tent", "stage", "eden", "casket", "threads", "carpet", "globe", "plain", "river", "milla", "booth", "omenroad", "pitroad", "mankind", "obey4", "triptych",
                  "roads", "fork", "centuries", "dominoes", "panels", "forge", "scales", "lab", "screen", "kunafa",
                  "kitchen", "karyotype", "anatomy", "planks",
                  "heights", "revelation", "gaze", "resurrection", "register", "unseen", "barzakh", "nameswall"}   # scenes drawn with faceless figures
# a prophet named in a tag (a lantern may carry the companions' or the scholars' knowledge, never a prophet)
PROPHET = re.compile(r"ﷺ|النبي|الرسول|رسول|نبي\b|محمد|المبعوث")       # one prophet (the guidance of "the prophets" may be a lantern)
# people a lantern must not stand for: they are drawn as figures (or as their names in roundels)
PEOPLE = re.compile(r"الغرباء|غرباء|المستضعف|معذور|عصابة|طائفة|ولاة|ولي الأمر|الأمير|السلطان|الحاكم|الإمام|"
                    r"رجل|امرأة|النساء|الرجال|الصالحين|المتبعون|المتبعين|أتباع|أولياء")
# broke a rule before the codex; each leaves this list when its scene is redrawn (review of 2026-10-21)
PENDING = {"hijra", "sunan-umam", "khatm-nubuwwa", "qawaid-arba"}
# a figure must never be one of these; ʿAlī is left out (عَلِيّ and عَلَيَّ look alike without harakat)
NAMED = re.compile(r"ﷺ|النبي\b|محمد|أبو بكر|أبي بكر|\bعمر\b|عثمان|بلال|الصحابي|جبريل|ملك الموت")


def _bare(s):
    return "".join(ch for ch in s if unicodedata.category(ch) != "Mn")


def lantern_items(spec):
    """The items of an illustrated scene that are drawn with the brass lantern."""
    kinds = LANTERN.get(spec.get("layout") or "")
    if spec["type"] != "art" or not kinds:
        return []
    return [(n, it) for n, it in enumerate(spec["items"]) if kinds == "*" or it["kind"] in kinds]


def codex_errors(cid, spec, sentence=lambda at: ""):
    """Codex breaks as (message, pending): pending ones are the cards still to be redrawn.
    [sentence] gives the spoken text of a clip, so that a lantern drawn while the Prophet ﷺ is named is caught."""
    out = []
    if spec["type"] != "art":
        return out
    kinds = LANTERN.get(spec["layout"])
    last_at = "face-0"
    for n, it in enumerate(spec["items"]):
        last_at = it.get("at") or last_at
        sub = _bare(it["sub"])
        label = _bare(it["label"])
        if kinds and (kinds == "*" or it["kind"] in kinds) and kinds != "*":
            said = _bare(sentence(last_at))
            if PROPHET.search(sub) or PROPHET.search(label) or PROPHET.search(said):
                out.append(f"item {n}: a lantern is drawn where the Prophet ﷺ (or a prophet) is meant; he is only light (kind light in trail)")
            if PEOPLE.search(sub):
                out.append(f"item {n}: a lantern stands for people («{it['sub'] or it['label']}»): draw them as figures")
        if kinds == "*" and PEOPLE.search(sub):
            out.append(f"item {n}: «{it['sub']}» names people in a scene built on a lantern: draw them as figures")
        if spec["layout"] in FIGURE_LAYOUTS and NAMED.search(sub):
            out.append(f"item {n}: «{it['sub']}» names a prophet, companion or angel in a scene with figures")
    return [(m, cid in PENDING) for m in out]


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
    lit = [cid for cid, sp in scenes.items() if lantern_items(sp)]
    if len(lit) > MAX_USES:
        problems.append(f"the lantern is drawn in {len(lit)} cards (at most {MAX_USES}): {', '.join(sorted(lit))}")
    top = sum(n for _, n in uses.most_common(5))
    summary = f"variety: {len(uses)} layouts for {len(lay)} scenes; the 5 most used carry {top} ({100 * top // max(len(lay), 1)}%)"
    return problems, summary


STRICT = os.environ.get("VARIETY_STRICT") == "1"
