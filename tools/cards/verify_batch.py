"""Check batch-2 cards against the book texts and the Quran text before anything is built."""
import json, re, sys
sys.path.insert(0, '/home/claude/project/cards')
from cards_batch2 import NEW, MERGE_SOURCES, MERGE_CHECKS
from cards_src import CARDS as OLD
H = re.compile(r'[ً-ْٰـ]')
def norm(s):
    s = H.sub('', s); s = re.sub(r'[إأآٱ]', 'ا', s)
    s = s.replace('ى', 'ي').replace('ة', 'ه').replace('ؤ', 'و').replace('ئ', 'ي')
    return re.sub(r'[^ء-ي]', '', s)
books = {k: norm(open(f'/home/claude/books/txt/{k}.txt', encoding='utf-8').read()) for k in ['tawhid', 'qawaid', 'kashf', 'lum', 'harras']}
Q = {}
for s in json.load(open('/home/claude/project/cards/quran-simple-clean.json', encoding='utf-8'))['data']['surahs']:
    for a in s['ayahs']: Q[(s['number'], a['numberInSurah'])] = a['text']
def verse_text(ref):
    s, a = ref.split(':'); lo, hi = (a.split('-') + [a])[:2]
    return ' '.join(Q[(int(s), i)] for i in range(int(lo), int(hi) + 1))
SPLIT = re.compile(r'(?<=[.؛!؟])\s+')
problems, n_checks = [], 0
ids = [c['id'] for c in OLD] + [c['id'] for c in NEW]
if len(ids) != len(set(ids)): problems.append(f'duplicate ids: {[i for i in ids if ids.count(i) > 1]}')
for c in NEW:
    for book, phrase in c.get('check', []):
        n_checks += 1
        if norm(phrase) not in books[book]: problems.append(f'{c["id"]}: not found in {book}: «{phrase}»')
    for q in c['quran']:
        ref, part = (q, None) if isinstance(q, str) else q
        try: vt = verse_text(ref)
        except KeyError: problems.append(f'{c["id"]}: bad verse ref {ref}'); continue
        if part and norm(part) not in norm(vt): problems.append(f'{c["id"]}: excerpt not in {ref}: «{part}» vs «{vt[:90]}»')
    for i, s in enumerate(c['face']):
        plain = H.sub('', s)
        if len(SPLIT.split(plain.strip())) != 1 or not plain.strip().endswith(('.', '؟')):
            problems.append(f'{c["id"]}: face {i} must be one sentence ending with . or ؟')
    for tag, s in [('title', c['title'])] + [(f'face{i}', s) for i, s in enumerate(c['face'])] + [(f'deep{i}', s) for i, s in enumerate(c['deeper'])]:
        if H.sub('', s).startswith('ر'): problems.append(f'{c["id"]} {tag}: starts with ر (voice weakens it)')
        bare = [w for w in re.findall(r'[ء-يً-ْٰ]+', s) if len(H.sub('', w)) > 2 and not H.search(w)]
        if bare: problems.append(f'{c["id"]} {tag}: words without tashkeel: {bare[:5]}')
    for r in c['related']:
        if r not in ids: problems.append(f'{c["id"]}: related id not found: {r}')
for book, phrase in MERGE_CHECKS:
    n_checks += 1
    if norm(phrase) not in books[book]: problems.append(f'merge check not found in {book}: «{phrase}»')
for cid in MERGE_SOURCES:
    if cid not in ids: problems.append(f'merge target missing: {cid}')
print(f'{len(NEW)} new cards, {n_checks} quoted phrases checked, {sum(len(c["quran"]) for c in NEW)} verse refs checked')
print(f'{len(problems)} problems'); print('\n'.join(problems))
