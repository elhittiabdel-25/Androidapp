"""Add the card animations from scenes.py to the app's content pack (run after tools/tts/build_content.py).

    python3 tools/cards/build_scenes.py [path/to/cards.json]     (default: the app's assets, edited in place)

For each item it finds the cue word in the spoken text of its clip and stores how far into the clip it is
(`frac`, a little early so the item lands with the word). Labels written without harakat are copied, vowelled,
from the card's own text. Fails on a missing cue, an unknown clip, items out of time order, or text it
cannot vowel; prints a note when a hand-vowelled word differs from the same word in the card.
"""
import json, os, re, sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
sys.path.insert(0, os.path.join(HERE, "..", "tts"))
from scenes import SCENES                      # noqa: E402
from build_content import spoken, letters      # noqa: E402

MARKS = "ًٌٍَُِّْٰ"
HARAKA = re.compile(f"[{MARKS}]")
LEAD = 0.03          # show each item slightly before its word
TYPES = {"steps", "sides", "chain", "timeline", "circles", "meter", "transform", "moon", "stars"}
TARGET = {"title": "title", "face": "face", "deep": "deeper", "had": "hadith"}

errors, notes = [], []


def err(cid, msg):
    errors.append(f"{cid}: {msg}")


def target_of(key):
    """Audio key -> vowelled-text key: face-0 -> face:0, deep-1 -> deeper:1, title -> title."""
    if key == "title":
        return "title"
    kind, i = key.split("-")
    return f"{TARGET[kind]}:{i}"


def speech_raw(sp, key):
    if key == "title":
        return sp["title"]
    kind, i = key.split("-")
    return sp[TARGET[kind]][int(i)]


def bare(s):
    return HARAKA.sub("", s)


def is_boundary(s, i):
    return i < 0 or i >= len(s) or not ("ء" <= s[i] <= "ي" or s[i] == "ـ")


CLITICS = "وفبلك"


def vowel_from(texts, label):
    """Find `label` (no harakat) in the vowelled texts, the item's own sentence first; return the vowelled span.

    Per text: whole words first, then after a one-letter prefix (و ف ب ل ك) that the label leaves out.
    Only if no text has it that way, any position in any text."""
    want = label.strip()

    def search(v, mode):
        kept, idx = [], []
        for j, ch in enumerate(v):
            if ch not in MARKS:
                kept.append(ch)
                idx.append(j)
        s = "".join(kept)
        start = 0
        while True:
            p = s.find(want, start)
            if p < 0:
                return None
            end = p + len(want)
            if mode == "word":
                good = is_boundary(s, p - 1) and is_boundary(s, end)
            elif mode == "clitic":
                good = p > 0 and s[p - 1] in CLITICS and is_boundary(s, p - 2) and is_boundary(s, end)
            else:
                good = True
            if good:
                a, b = idx[p], idx[end - 1] + 1
                while b < len(v) and v[b] in MARKS:
                    b += 1
                return v[a:b]
            start = p + 1

    for v in texts:
        for mode in ("word", "clitic"):
            r = search(v, mode)
            if r:
                return r
    for v in texts:
        r = search(v, "any")
        if r:
            notes.append(f"vowelled «{label}» from inside a word: «{r}»")
            return r
    return None


def words_with_marks(s):
    return re.findall(f"[ء-يـ{MARKS}]+", s)


def check_vowels(cid, text, vocab):
    """Compare each hand-vowelled word with the same word in the card (ignoring its last letter's case)."""
    for w in words_with_marks(text):
        b = bare(w)
        if len(b) < 2:
            continue
        if not HARAKA.search(w):
            err(cid, f"no harakat on «{w}» in «{text}»")
            continue
        forms = vocab.get(b)
        if not forms:
            continue

        def body(x):                      # drop the marks on the last letter
            return re.sub(f"[{MARKS}]+$", "", x)
        if body(w) not in {body(f) for f in forms}:
            notes.append(f"{cid}: «{w}» — the card has {' / '.join(sorted(forms))}")


def vowelled(cid, s, texts, vocab):
    if not s:
        return s
    if HARAKA.search(s):
        check_vowels(cid, s, vocab)
        return s
    v = vowel_from(texts, s)
    if v is None:
        err(cid, f"cannot vowel «{s}»: not in the card text (write it with harakat)")
        return s
    v = v.strip(" ،؛:.")
    check_vowels(cid, v, vocab)
    return v


def build(path):
    pack = json.load(open(path, encoding="utf-8"))
    speech = json.load(open(os.path.join(HERE, "..", "tts", "speech.json"), encoding="utf-8"))
    cards = {c["id"]: c for c in pack["cards"]}
    for cid in SCENES:
        if cid not in cards:
            err(cid, "no such card")
    done = 0
    for c in pack["cards"]:
        spec = SCENES.get(c["id"])
        c.pop("scene", None)
        if spec is None:
            continue
        cid = c["id"]
        keys = [a["key"] for a in c["audio"]]
        diac = c.get("diac", {})
        vocab = {}
        for v in diac.values():
            for w in words_with_marks(v):
                vocab.setdefault(bare(w), set()).add(w)
        if spec["type"] not in TYPES:
            err(cid, f"unknown scene type {spec['type']}")
        items, last_at, last_pos, last_order = [], None, {}, (-1, -1.0)
        for n, it in enumerate(spec["items"]):
            at = it.get("at") or last_at or "face-0"
            last_at = at
            if at not in keys:
                err(cid, f"item {n}: no clip {at}")
                continue
            heard = letters(spoken(speech_raw(speech[cid], at)))
            frac = 0.0
            if it["cue"]:
                cue = letters(it["cue"])
                p = heard.find(cue, last_pos.get(at, 0))
                if p < 0:
                    err(cid, f"item {n}: cue «{it['cue']}» not heard in {at} after position {last_pos.get(at, 0)}: {heard}")
                    continue
                last_pos[at] = p + 1
                frac = max(0.0, p / max(len(heard), 1) - LEAD)
            order = (keys.index(at), frac)
            if order < last_order:
                err(cid, f"item {n} comes before the previous one in the audio")
            last_order = order
            # look first in the item's own sentence, then in the rest of the card
            own = diac.get(target_of(at))
            texts = ([own] if own else []) + [v for k, v in diac.items() if k != target_of(at)]
            out = {"label": vowelled(cid, it["label"], texts, vocab), "at": at, "frac": round(frac, 3)}
            if it["sub"]:
                out["sub"] = vowelled(cid, it["sub"], texts, vocab)
            if it.get("side") is not None:
                out["side"] = it["side"]
            if it.get("value") is not None:
                out["value"] = it["value"]
            if it["kind"]:
                out["kind"] = it["kind"]
            items.append(out)
        texts = list(diac.values())
        scene = {"type": spec["type"], "items": items}
        if spec["layout"]:
            scene["layout"] = spec["layout"]
        if spec["title"]:
            scene["title"] = vowelled(cid, spec["title"], texts, vocab)
        if spec["tone"]:
            scene["tone"] = spec["tone"]
        if spec["sides"]:
            scene["sides"] = [{"title": vowelled(cid, s["title"], texts, vocab), "tone": s["tone"]} for s in spec["sides"]]
        if spec["note"]:
            scene["note"] = vowelled(cid, spec["note"], texts, vocab)
        # per-type sanity
        if spec["type"] == "sides" and len(spec["sides"]) != 2:
            err(cid, "a sides scene needs two side titles")
        if spec["type"] == "timeline" and any(i.get("value", 0) <= 0 for i in items):
            err(cid, "timeline items need a positive value")
        if spec["type"] == "transform":
            for k in ("ring", "fill", "turn", "safe"):
                if not any(i.get("kind") == k for i in items):
                    err(cid, f"transform scene lacks a {k} item")
        if spec["type"] == "stars":
            for k in ("decor", "meteor", "guide", "rain"):
                if not any(i.get("kind") == k for i in items):
                    err(cid, f"stars scene lacks a {k} item")
        c["scene"] = scene
        done += 1
    for n in notes:
        print("note:", n)
    if errors:
        print("\n".join("ERROR " + e for e in errors))
        sys.exit(1)
    json.dump(pack, open(path, "w", encoding="utf-8"), ensure_ascii=False)
    print(f"{done} scenes written to {path}")


if __name__ == "__main__":
    default = os.path.join(HERE, "..", "..", "app", "src", "main", "assets", "content", "cards.json")
    build(sys.argv[1] if len(sys.argv) > 1 else default)
