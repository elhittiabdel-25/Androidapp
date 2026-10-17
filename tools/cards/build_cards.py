"""Merge the first 16 cards with batch 2 into cards.json (screen text) and speech.json (vowelled speech)."""
import json, re, sys, copy, os
HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
sys.path.insert(0, os.path.dirname(HERE))
from cards_src import CARDS as OLD, BOOKS as OLD_BOOKS
from cards_batch2 import NEW, BOOKS2, MERGE_SOURCES
H = re.compile(r'[ً-ْٰـ]')
def shown(s): return re.sub(r'\{[^}]*\}', '', re.sub(r'\[([^|\]]*)\|([^\]]*)\]', r'\1', s))
def plain(s): return H.sub('', shown(s))
Q, NAMES = {}, {}
for s in json.load(open(os.path.join(HERE, 'quran-simple-clean.json'), encoding='utf-8'))['data']['surahs']:
    NAMES[s['number']] = re.sub(r'^سورة\s+', '', re.sub(r'[\u0653-\u0655\u06D6-\u06ED]', '', H.sub('', s['name'])).replace('ٱ', 'ا')).strip()
    for a in s['ayahs']: Q[(s['number'], a['numberInSurah'])] = a['text']
def quran_ev(ref, part=None):
    s, a = ref.split(':'); lo, hi = (a.split('-') + [a])[:2]
    text = ' '.join(f"{Q[(int(s), i)]} ({i})" for i in range(int(lo), int(hi) + 1))
    if part: text = part + ' …'
    return {"kind": "quran", "ref": ref, "text": text, "label": f"{NAMES[int(s)]}: {a}", **({"part": part} if part else {})}
cards, speech = [], json.load(open(os.path.join(HERE, '..', 'tts', 'speech.json'), encoding='utf-8'))
books = dict(OLD_BOOKS); books.update(BOOKS2)
for c in OLD:                                   # first 16 cards: same text and audio, extra sources merged in
    c = copy.deepcopy(c)
    for e in c["evidence"]:
        if e["kind"] == "quran": e.update(quran_ev(e["ref"], e.get("part")))
    for b, p in MERGE_SOURCES.get(c["id"], []):
        c["sources"].append({"book": b, "pages": p})
    cards.append(c)
for c in NEW:
    ev = [quran_ev(q) if isinstance(q, str) else quran_ev(*q) for q in c["quran"]]
    ev += [{"kind": "hadith", "text": plain(t), "takhrij": tk} for t, tk in c["hadith"]]
    cards.append({"id": c["id"], "type": c["type"], "topic": c["topic"], "path": c["path"], "order": c["order"],
                  "title": plain(c["title"]), "face": " ".join(plain(s) for s in c["face"]),
                  "deeper": [plain(d) for d in c["deeper"]], "evidence": ev,
                  "sources": [{"book": b, "pages": p} for b, p in c["sources"]],
                  "related": c["related"], "quiz": {"q": c["quiz"][0], "a": c["quiz"][1]},
                  **({"verify": [{"level": l, "text": t} for l, t in c["verify"]]} if c.get("verify") else {})})
    speech[c["id"]] = {"title": c["title"], "face": c["face"], "deeper": c["deeper"], "hadith": [t for t, _ in c["hadith"]]}
json.dump({"books": books, "cards": cards}, open(os.path.join(HERE, 'cards.json'), 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
json.dump(speech, open(os.path.join(HERE, '..', 'tts', 'speech.json'), 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
print(len(cards), "cards,", len(books), "books,", len({c['path'] for c in cards}), "paths")
