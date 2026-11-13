"""Merge the first 16 cards with batches 2 to 17 into cards.json (screen text) and speech.json (vowelled speech).

The batch files are the source of truth: their speech entries are rewritten on every run; sources found in
later books are merged into the earlier cards (MERGE_SOURCES of batch 2, then MERGE_SOURCES3, 4 and 5).
"""
import json, re, sys, copy, os
HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
sys.path.insert(0, os.path.dirname(HERE))
from cards_src import CARDS as OLD, BOOKS as OLD_BOOKS
from cards_batch2 import NEW, BOOKS2, MERGE_SOURCES
from cards_batch3 import NEW3, BOOKS3, MERGE_SOURCES3
from cards_batch4 import NEW4, BOOKS4, MERGE_SOURCES4
from cards_batch5 import NEW5, BOOKS5, MERGE_SOURCES5
from cards_batch6 import NEW6, BOOKS6
from cards_batch7 import NEW7
from cards_batch8 import NEW8
from cards_batch9 import NEW9, BOOKS9
from cards_batch10 import NEW10, BOOKS10
from cards_batch11 import NEW11, BOOKS11
from cards_batch12 import NEW12, BOOKS12
from cards_batch13 import NEW13, BOOKS13
from cards_batch14 import NEW14, BOOKS14
from cards_batch15 import NEW15, BOOKS15
from cards_batch16 import NEW16, BOOKS16
from cards_batch17 import NEW17, BOOKS17
from cards_batch18 import NEW18, BOOKS18
from cards_batch19 import NEW19, BOOKS19
from cards_batch20 import NEW20, BOOKS20
from cards_batch21 import NEW21, BOOKS21
from cards_batch22 import NEW22, BOOKS22
from cards_batch23 import NEW23, BOOKS23
from cards_batch24 import NEW24, BOOKS24
from cards_batch25 import NEW25, BOOKS25
H = re.compile(r'[ً-ْٰـ]')
def shown(s): return re.sub(r'\{[^}]*\}', '', re.sub(r'\[([^|\]]*)\|([^\]]*)\]', r'\1', s))
def plain(s): return H.sub('', shown(s))
Q, NAMES = {}, {}
for s in json.load(open(os.path.join(HERE, 'quran-simple-clean.json'), encoding='utf-8'))['data']['surahs']:
    NAMES[s['number']] = re.sub(r'^سورة\s+', '', re.sub(r'[ٓ-ٕۖ-ۭ]', '', H.sub('', s['name'])).replace('ٱ', 'ا')).strip()
    for a in s['ayahs']: Q[(s['number'], a['numberInSurah'])] = a['text']
def quran_ev(ref, part=None):
    s, a = ref.split(':'); lo, hi = (a.split('-') + [a])[:2]
    text = ' '.join(f"{Q[(int(s), i)]} ({i})" for i in range(int(lo), int(hi) + 1))
    if part: text = part + ' …'
    return {"kind": "quran", "ref": ref, "text": text, "label": f"{NAMES[int(s)]}: {a}", **({"part": part} if part else {})}
def add_sources(card, extra):
    for b, p in extra:
        if not any(s["book"] == b for s in card["sources"]):      # one entry per book
            card["sources"].append({"book": b, "pages": p})
cards, speech = [], json.load(open(os.path.join(HERE, '..', 'tts', 'speech.json'), encoding='utf-8'))
books = dict(OLD_BOOKS); books.update(BOOKS2); books.update(BOOKS3); books.update(BOOKS4); books.update(BOOKS5); books.update(BOOKS6)
books.update(BOOKS9); books.update(BOOKS10); books.update(BOOKS11)
books.update(BOOKS12); books.update(BOOKS13); books.update(BOOKS14); books.update(BOOKS15)
books.update(BOOKS16); books.update(BOOKS17)
books.update(BOOKS18); books.update(BOOKS19); books.update(BOOKS20)
books.update(BOOKS21); books.update(BOOKS22); books.update(BOOKS23); books.update(BOOKS24); books.update(BOOKS25)
for c in OLD:                                   # first 16 cards: same text and audio, extra sources merged in
    c = copy.deepcopy(c)
    for e in c["evidence"]:
        if e["kind"] == "quran": e.update(quran_ev(e["ref"], e.get("part")))
    add_sources(c, MERGE_SOURCES.get(c["id"], []))
    add_sources(c, MERGE_SOURCES3.get(c["id"], []))
    add_sources(c, MERGE_SOURCES4.get(c["id"], []))
    add_sources(c, MERGE_SOURCES5.get(c["id"], []))
    cards.append(c)
for c in NEW + NEW3 + NEW4 + NEW5 + NEW6 + NEW7 + NEW8 + NEW9 + NEW10 + NEW11 + NEW12 + NEW13 + NEW14 + NEW15 + NEW16 + NEW17 + NEW18 + NEW19 + NEW20 + NEW21 + NEW22 + NEW23 + NEW24 + NEW25:
    ev = [quran_ev(q) if isinstance(q, str) else quran_ev(*q) for q in c["quran"]]
    ev += [{"kind": "hadith", "text": plain(t), "takhrij": tk} for t, tk in c["hadith"]]
    out = {"id": c["id"], "type": c["type"], "topic": c["topic"], "path": c["path"], "order": c["order"],
           "title": plain(c["title"]), "face": " ".join(plain(s) for s in c["face"]),
           "deeper": [plain(d) for d in c["deeper"]], "evidence": ev,
           "sources": [{"book": b, "pages": p} for b, p in c["sources"]],
           "related": c["related"], "quiz": {"q": c["quiz"][0], "a": c["quiz"][1]},
           **({"verify": [{"level": l, "text": t} for l, t in c["verify"]]} if c.get("verify") else {})}
    add_sources(out, MERGE_SOURCES3.get(c["id"], []))
    add_sources(out, MERGE_SOURCES4.get(c["id"], []))
    add_sources(out, MERGE_SOURCES5.get(c["id"], []))
    cards.append(out)
    speech[c["id"]] = {"title": c["title"], "face": c["face"], "deeper": c["deeper"], "hadith": [t for t, _ in c["hadith"]]}
json.dump({"books": books, "cards": cards}, open(os.path.join(HERE, 'cards.json'), 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
json.dump(speech, open(os.path.join(HERE, '..', 'tts', 'speech.json'), 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
print(len(cards), "cards,", len(books), "books,", len({c['path'] for c in cards}), "paths")
