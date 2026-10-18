"""Check batch-3 cards before anything is built: quoted phrases against the book texts, cited pages of the
OCR'd books, verse references and excerpts, sentence and tashkeel rules, related ids, and that the voice
never reads a verse (no five words in a row from the Quran in a spoken segment).

    BOOKS_TXT=DIR python3 tools/cards/verify_batch3.py

DIR holds txt/<file>.txt (books with a text layer: tawhid, 15892, 628) and ocr/<id>.txt (OCR, with
"=== PAGE n ===" markers). OCR text has reading errors, so its phrases are matched approximately.
"""
import difflib, json, os, re, sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
sys.path.insert(0, os.path.dirname(HERE))
from cards_batch3 import NEW3, MERGE_SOURCES3, MERGE_CHECKS3, PAGE_CHECKS3   # noqa: E402
from cards_batch2 import NEW as NEW2                                         # noqa: E402
from cards_src import CARDS as OLD                                           # noqa: E402

H = re.compile(r'[ً-ْٰـ]')
ROOT = os.environ.get("BOOKS_TXT", "")
EXACT = {"tawhid": "txt/tawhid.txt", "fath": "txt/15892.txt", "alam": "txt/628.txt"}
OCR = {"nawaqid": "ocr/5646.txt", "mia": "ocr/4865.txt", "qa": "ocr/4873.txt", "taysir": "ocr/15714.txt",
       "hashiya": "ocr/107145.txt", "tawdih": "ocr/116093.txt"}


def norm(s):
    s = H.sub('', s)
    s = re.sub(r'[إأآٱ]', 'ا', s)
    s = s.replace('ى', 'ي').replace('ة', 'ه').replace('ؤ', 'و').replace('ئ', 'ي')
    return re.sub(r'[^ء-ي]', '', s)


def words(s):
    s = H.sub('', s)
    s = re.sub(r'[إأآٱ]', 'ا', s).replace('ى', 'ي').replace('ة', 'ه')
    return re.findall(r'[ء-ي]+', s)


def pages(path):
    out, page, buf = {}, 0, []
    for line in open(path, encoding='utf-8'):
        m = re.match(r'=== PAGE (\d+) ===', line)
        if m:
            out[page] = norm(''.join(buf)); page, buf = int(m.group(1)), []
        else:
            buf.append(line)
    out[page] = norm(''.join(buf))
    return out


def fuzzy_in(phrase, text, need=0.85):
    """Best similarity of `phrase` with a same-length window of `text`, anchored on 4-letter pieces."""
    p = norm(phrase)
    if p in text:
        return 1.0
    best, n = 0.0, len(p)
    for k in range(0, max(1, n - 4), 3):
        piece = p[k:k + 4]
        start = 0
        while True:
            i = text.find(piece, start)
            if i < 0:
                break
            a = max(0, i - k - 3)
            r = difflib.SequenceMatcher(None, p, text[a:a + n + 6]).ratio()
            if r > best:
                best = r
                if best >= need:
                    return best
            start = i + 1
    return best


exact = {k: norm(open(os.path.join(ROOT, v), encoding='utf-8').read()) for k, v in EXACT.items()}
ocr = {k: pages(os.path.join(ROOT, v)) for k, v in OCR.items()}
ocr_all = {k: ''.join(v.values()) for k, v in ocr.items()}

Q, QW = {}, set()
for s in json.load(open(os.path.join(HERE, 'quran-simple-clean.json'), encoding='utf-8'))['data']['surahs']:
    for a in s['ayahs']:
        Q[(s['number'], a['numberInSurah'])] = a['text']
        w = words(a['text'])
        for i in range(len(w) - 4):
            QW.add(' '.join(w[i:i + 5]))


def verse_text(ref):
    s, a = ref.split(':')
    lo, hi = (a.split('-') + [a])[:2]
    return ' '.join(Q[(int(s), i)] for i in range(int(lo), int(hi) + 1))


def spoken_text(s):
    return re.sub(r'\{([^}]*)\}', r'\1', re.sub(r'\[([^|\]]*)\|([^\]]*)\]', r'\2', s))


SPLIT = re.compile(r'(?<=[.؛!؟])\s+')
problems, n_checks = [], 0
ids = [c['id'] for c in OLD] + [c['id'] for c in NEW2] + [c['id'] for c in NEW3]
dups = sorted({i for i in ids if ids.count(i) > 1})
if dups:
    problems.append(f'duplicate ids: {dups}')

for c in NEW3:
    cid = c['id']
    for book, phrase in c.get('check', []):
        n_checks += 1
        if book in exact:
            if norm(phrase) not in exact[book]:
                problems.append(f'{cid}: not found in {book}: «{phrase}»')
        else:
            r = fuzzy_in(phrase, ocr_all[book])
            if r < 0.85:
                problems.append(f'{cid}: not found in {book} (best {r:.2f}): «{phrase}»')
    for q in c['quran']:
        ref, part = (q, None) if isinstance(q, str) else q
        try:
            vt = verse_text(ref)
        except KeyError:
            problems.append(f'{cid}: bad verse ref {ref}'); continue
        if part and norm(part) not in norm(vt):
            problems.append(f'{cid}: excerpt not in {ref}: «{part}»')
    for i, s in enumerate(c['face']):
        plain = H.sub('', s).strip()
        if len(SPLIT.split(plain)) != 1 or not plain.endswith(('.', '؟')):
            problems.append(f'{cid}: face {i} must be one sentence ending with . or ؟')
    segs = [('title', c['title'])] + [(f'face{i}', s) for i, s in enumerate(c['face'])] + \
           [(f'deep{i}', s) for i, s in enumerate(c['deeper'])] + [(f'had{i}', t) for i, (t, _) in enumerate(c['hadith'])]
    for tag, s in segs:
        if H.sub('', s).startswith('ر'):
            problems.append(f'{cid} {tag}: starts with ر (voice weakens it)')
        bare = [w for w in re.findall(r'[ء-يً-ْٰ]+', s) if len(H.sub('', w)) > 2 and not H.search(w)]
        if bare:
            problems.append(f'{cid} {tag}: words without tashkeel: {bare[:6]}')
        w = words(spoken_text(s))
        hits = {' '.join(w[i:i + 5]) for i in range(len(w) - 4)} & QW
        if hits and not tag.startswith('had'):      # a hadith may share wording with a verse
            problems.append(f'{cid} {tag}: reads Quran words: {sorted(hits)[:2]}')
    for r in c['related']:
        if r not in ids:
            problems.append(f'{cid}: related id not found: {r}')

for book, phrase in MERGE_CHECKS3:
    n_checks += 1
    if norm(phrase) not in exact[book]:
        problems.append(f'merge check not found in {book}: «{phrase}»')
for book, page, phrase in PAGE_CHECKS3:
    n_checks += 1
    r = fuzzy_in(phrase, ocr[book].get(page, ''), 0.8)
    if r < 0.8:
        problems.append(f'page check: {book} p.{page} (best {r:.2f}): «{phrase}»')
for cid in MERGE_SOURCES3:
    if cid not in ids:
        problems.append(f'merge target missing: {cid}')

print(f'{len(NEW3)} new cards, {n_checks} phrases and pages checked, {sum(len(c["quran"]) for c in NEW3)} verse refs checked')
print(f'{len(problems)} problems')
print('\n'.join(problems))
