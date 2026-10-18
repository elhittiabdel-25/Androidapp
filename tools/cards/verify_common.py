"""Helpers shared by the batch verifiers: Arabic normalisation, page-split OCR texts, approximate matching,
the Quran text (for verse refs and for keeping the voice off Quran wording)."""
import difflib
import json
import os
import re

HERE = os.path.dirname(os.path.abspath(__file__))
H = re.compile(r'[ً-ْٰـ]')
SPLIT = re.compile(r'(?<=[.؛!؟])\s+')


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
            out[page] = norm(''.join(buf))
            page, buf = int(m.group(1)), []
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


Q, QW = {}, set()
for _s in json.load(open(os.path.join(HERE, 'quran-simple-clean.json'), encoding='utf-8'))['data']['surahs']:
    for _a in _s['ayahs']:
        Q[(_s['number'], _a['numberInSurah'])] = _a['text']
        _w = words(_a['text'])
        for _i in range(len(_w) - 4):
            QW.add(' '.join(_w[_i:_i + 5]))


def verse_text(ref):
    s, a = ref.split(':')
    lo, hi = (a.split('-') + [a])[:2]
    return ' '.join(Q[(int(s), i)] for i in range(int(lo), int(hi) + 1))


def spoken_text(s):
    return re.sub(r'\{([^}]*)\}', r'\1', re.sub(r'\[([^|\]]*)\|([^\]]*)\]', r'\2', s))
