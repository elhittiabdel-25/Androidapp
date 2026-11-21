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
from cards_batch26 import NEW26, BOOKS26
from cards_batch27 import NEW27, BOOKS27
from cards_batch28 import NEW28, BOOKS28
from cards_batch29 import NEW29, BOOKS29
from cards_batch30 import NEW30, BOOKS30
from cards_batch31 import NEW31, BOOKS31
from cards_batch32 import NEW32, BOOKS32
from cards_batch33 import NEW33, BOOKS33
from cards_batch34 import NEW34, BOOKS34
from cards_batch35 import NEW35, BOOKS35
from cards_batch36 import NEW36, BOOKS36
from cards_batch37 import NEW37, BOOKS37
from cards_batch38 import NEW38, BOOKS38
from cards_batch39 import NEW39, BOOKS39
from cards_batch40 import NEW40, BOOKS40
from cards_batch41 import NEW41, BOOKS41
from cards_batch42 import NEW42, BOOKS42
from cards_batch43 import NEW43, BOOKS43
from cards_batch44 import NEW44, BOOKS44
from cards_batch45 import NEW45, BOOKS45
from cards_batch46 import NEW46, BOOKS46
from cards_batch47 import NEW47, BOOKS47
from cards_batch48 import NEW48, BOOKS48
from cards_batch49 import NEW49, BOOKS49
from cards_batch50 import NEW50, BOOKS50
from cards_batch51 import NEW51, BOOKS51
from cards_batch52 import NEW52, BOOKS52
from cards_batch53 import NEW53
from cards_batch54 import NEW54
from cards_batch55 import NEW55
from cards_batch56 import NEW56
from cards_batch57 import NEW57
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
books.update(BOOKS21); books.update(BOOKS22); books.update(BOOKS23); books.update(BOOKS24); books.update(BOOKS25); books.update(BOOKS26); books.update(BOOKS27); books.update(BOOKS28); books.update(BOOKS29); books.update(BOOKS31); books.update(BOOKS32); books.update(BOOKS33); books.update(BOOKS34); books.update(BOOKS35); books.update(BOOKS36); books.update(BOOKS37); books.update(BOOKS38); books.update(BOOKS41); books.update(BOOKS42); books.update(BOOKS43); books.update(BOOKS44); books.update(BOOKS45); books.update(BOOKS46); books.update(BOOKS47); books.update(BOOKS48); books.update(BOOKS49); books.update(BOOKS50); books.update(BOOKS51); books.update(BOOKS52)
for c in OLD:                                   # first 16 cards: same text and audio, extra sources merged in
    c = copy.deepcopy(c)
    for e in c["evidence"]:
        if e["kind"] == "quran": e.update(quran_ev(e["ref"], e.get("part")))
    add_sources(c, MERGE_SOURCES.get(c["id"], []))
    add_sources(c, MERGE_SOURCES3.get(c["id"], []))
    add_sources(c, MERGE_SOURCES4.get(c["id"], []))
    add_sources(c, MERGE_SOURCES5.get(c["id"], []))
    cards.append(c)
for c in NEW + NEW3 + NEW4 + NEW5 + NEW6 + NEW7 + NEW8 + NEW9 + NEW10 + NEW11 + NEW12 + NEW13 + NEW14 + NEW15 + NEW16 + NEW17 + NEW18 + NEW19 + NEW20 + NEW21 + NEW22 + NEW23 + NEW24 + NEW25 + NEW26 + NEW27 + NEW28 + NEW29 + NEW30 + NEW31 + NEW32 + NEW33 + NEW34 + NEW35 + NEW36 + NEW37 + NEW38 + NEW39 + NEW40 + NEW41 + NEW42 + NEW43 + NEW44 + NEW45 + NEW46 + NEW47 + NEW48 + NEW49 + NEW50 + NEW51 + NEW52 + NEW53 + NEW54 + NEW55 + NEW56 + NEW57:
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
