"""Fully vowelled Arabic -> phonemes for the Kareem voice, following every haraka.

The voice was trained on espeak-ng's IPA for vowelled text, so the output keeps espeak's symbols and style
(stress marks before the vowel, "a." after ص ض ط ظ, "au"/"ai" for diphthongs, "dː" for a doubled stop and
"ll" for a doubled continuant). What changes is what espeak gets wrong; here every word is spelled out from
its harakat:
  - shadda doubles the letter and keeps its vowel; tanween is the vowel + n; sukun is no vowel;
  - long vowels (ا و ي ى and ٓ), diphthongs (وْ يْ after fatha), ta marbuta, every hamza seat;
  - the article: ال before a moon letter is "l", before a sun letter it doubles that letter;
  - hamzat al-wasl in connected speech: inside a sentence the article's (or a wasl word's) vowel is not said,
    the previous word runs into it (فِي الْأَرْضِ -> "fil ʔardˤ"), a long vowel before it is shortened, a word
    ending in sukun or tanween gets its linking vowel (مِنْ -> mina, هُمْ -> humu, tanween -> ni);
  - the Name of Allah: "allaːh", "lillaːh", "billaːh", joined to the word before it;
  - waqf at every pause mark and at the end: the last short vowel and tanween are not said, -an becomes -aː,
    ة becomes h (a word ending in two consonants keeps its short vowel: the voice swallows a final cluster).
Stress marks are taken from espeak's own output for the same words, so the rhythm stays the one the voice knows.

    python3 arabic_g2p.py "النص"          prints the phonemes (and espeak's, for comparison)
"""
import re, unicodedata

FATHA, DAMMA, KASRA = "َ", "ُ", "ِ"
FATHATAN, DAMMATAN, KASRATAN = "ً", "ٌ", "ٍ"
SHADDA, SUKUN, DAGGER, MADDA = "ّ", "ْ", "ٰ", "ٓ"
TATWEEL, WASLA = "ـ", "ٱ"
VOWEL_OF = {FATHA: "a", DAMMA: "u", KASRA: "i", FATHATAN: "a", DAMMATAN: "u", KASRATAN: "i"}
TANWEEN = {FATHATAN, DAMMATAN, KASRATAN}
MARKS = set(VOWEL_OF) | {SHADDA, SUKUN, DAGGER, MADDA}

CONS = {
    "ب": "b", "ت": "t", "ث": "θ", "ج": "dʒ", "ح": "ħ", "خ": "χ", "د": "d", "ذ": "ð", "ر": "r", "ز": "z",
    "س": "s", "ش": "ʃ", "ص": "s̪", "ض": "dˤ", "ط": "t̪", "ظ": "ð", "ع": "ʕ", "غ": "ɣ", "ف": "f", "ق": "q",
    "ك": "k", "ل": "l", "م": "m", "ن": "n", "ه": "h", "و": "w", "ي": "j", "ة": "t",
    "ء": "ʔ", "أ": "ʔ", "إ": "ʔ", "ؤ": "ʔ", "ئ": "ʔ",
}
THICK = set("صضطظ")                         # espeak darkens the vowels after these ("a.")
SUN = set("تثدذرزسشصضطظلن")
STOPS = {"b", "t", "dʒ", "d", "s̪", "t̪", "q", "k", "ʔ"}   # doubled as "Cː"; the rest as "CC" (espeak's style)
LETTERS = set(CONS) | set("اآىٱ")
PAUSE = {"،": ", ", ",": ", ", "؛": "; ", ";": "; ", ":": ": ", ".": ".", "؟": "?", "?": "?", "!": "!"}
SENTENCE_END = {".", "؟", "?", "!"}
WASL_NOUNS = {"ابن", "ابنة", "ابنت", "اسم", "امرؤ", "امرئ", "امرأ", "امرأة", "اثنان", "اثنين", "اثنتان", "اثنتين"}
PLURAL_MIM = re.compile("(هم|هما|كم|كما|تم|تما)$")
DOUBLED_GLIDE = "long+glide"
TAFKHIM = True                   # the heavy lam of the Name of Allah (see tafkhim); off only to compare     # how يّ / وّ are written: "training" (iː / uː, as the voice learned), "jj", "long+glide"

# words written without the small alif they are read with: (bare stem pattern, index of the letter that gets it)
DAGGER_WORDS = [
    (re.compile("^هذ(ا|ه|ان|ين)"), 0),           # هَذَا هَذِهِ: haːðaː, haːðihi
    (re.compile("^ذلك"), 0), (re.compile("^لكن"), 0), (re.compile("^هؤلاء"), 0), (re.compile("^هكذا"), 0),
    (re.compile("^أولئك"), 2),                    # أُولَئِكَ: ʔulaːʔika (the waw is silent too)
    (re.compile("^الرحمن"), 4), (re.compile("^رحمن"), 2),
    (re.compile("^الإله"), 3), (re.compile("^إله"), 1),
]
ALLAH = re.compile("^(?:[وفبت]|وب|فب|فو)?الله(?:م)?$|^[وف]?لله$")


def bare(s):
    return "".join(ch for ch in s if ch in LETTERS)


class U:
    """One letter with its marks."""
    __slots__ = ("ch", "v", "tan", "sh", "suk", "dag")

    def __init__(self, ch):
        self.ch, self.v, self.tan, self.sh, self.suk, self.dag = ch, None, False, False, False, False

    def __repr__(self):
        return self.ch + (self.v or "") + ("N" if self.tan else "") + ("ː" if self.sh else "") + ("°" if self.suk else "")


def letters_of(word):
    us = []
    for ch in word:
        if ch in LETTERS:
            us.append(U(ch))
        elif us and ch in VOWEL_OF:
            us[-1].v = VOWEL_OF[ch]
            us[-1].tan = ch in TANWEEN
        elif us and ch == SHADDA:
            us[-1].sh = True
        elif us and ch == SUKUN:
            us[-1].suk = True
        elif us and ch == DAGGER:
            us[-1].dag = True
    return us


def normalize(text):
    text = text.replace("ﷺ", " صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ ")
    text = unicodedata.normalize("NFC", text)        # ا+ٓ -> آ, ا+ٔ -> أ ...
    text = text.replace(TATWEEL, "")
    text = re.sub("[«»\"“”'()\\[\\]{}—–-]", " ", text)
    return text


class Word:
    def __init__(self, text):
        self.text = text
        self.us = letters_of(text)
        self.toks = []        # phoneme tokens (dicts)
        self.wasl = None      # None, "article" or "alif": starts with hamzat al-wasl (no proclitic before it)
        self.sun = False      # article before a sun letter
        self.wasl_vowel = "i"


def tok_c(sym, gem=False):
    return {"t": "C", "s": sym, "gem": gem}


def tok_v(q, long=False, dark=False):
    return {"t": "V", "s": q, "long": long, "dark": dark, "st": ""}


def article_lam(us, k):
    """us[k] is a lam right after a wasl alif (or after لِ): is it the article's lam?"""
    lam = us[k]
    if k + 1 >= len(us):
        return False
    nxt = us[k + 1]
    if lam.v == "i":
        return nxt.ch == "ا"                         # الِاسْتِعَانَة: the lam carries the linking kasra
    if lam.v or lam.tan:
        return False
    if lam.suk:
        return not (nxt.ch in SUN and nxt.ch != "ل" and not nxt.sh)   # الْتَزَمَ is a wasl verb, not the article
    return True                                       # a doubled sun letter follows (or a moon letter, lam unmarked)


def analyze(word):
    """Spell one word into tokens. A word-initial wasl (article or alif) is left for the clause to resolve."""
    us = word.us
    b = bare(word.text)
    if ALLAH.match(b):                               # اللَّه -> اللَّاه: the long alif after the doubled lam
        for k, u in enumerate(us):
            if u.ch == "ل" and u.sh and k + 1 < len(us) and us[k + 1].ch == "ه":
                us.insert(k + 1, U("ا"))
                break
    for pat, at in DAGGER_WORDS:                     # هَذَا -> haːðaː ...
        skip = next((n for n in (0, 1, 2) if all(c in "وفبكل" for c in b[:n]) and pat.match(b[n:])), None)
        if skip is None:
            continue
        target = us[skip + at] if skip + at < len(us) else None
        if target is not None and target.v in (None, "a") and not target.suk:
            target.dag = True
            if b[skip:].startswith("أولئك"):
                del us[skip + 1]                     # silent waw
        break
    if re.match("^(?:[وفبل]|وب|فب|ول)?مائ(?:ة|ت)", b):     # مِائَة: the alif is silent
        us[:] = [u for k, u in enumerate(us) if not (u.ch == "ا" and k > 0 and us[k - 1].ch == "م" and k + 1 < len(us) and us[k + 1].ch == "ئ")]
    if re.match("^(?:[وفل]?)أول(?:و|ي|ات|اء)$", b):    # أُولُو، أُولِي: the waw is silent
        k = next((k for k, u in enumerate(us) if u.ch == "و" and k > 0 and us[k - 1].ch == "أ"), None)
        if k is not None:
            del us[k]
    if len(us) >= 2 and us[-1].ch == "و" and us[-2].tan:     # عَمْرٌو
        us.pop()

    # proclitics (وَ فَ بِ كَ لِ), then a wasl alif or the article
    start = 0
    while start < 2 and start + 2 < len(us):
        u, nxt = us[start], us[start + 1]
        if u.ch not in "وفبكل" or u.v not in ("a", "i") or u.sh or u.tan:
            break
        if nxt.ch == "ا" and not nxt.v:
            after = us[start + 2]
            if (after.ch == "ل" and article_lam(us, start + 2)) or after.suk or (after.sh and u.ch != "ك"):
                start += 1                               # وَال / فَاعْبُدْ / بِاسْمِ / وَاتَّقُوا
            break
        if u.ch == "ل" and u.v == "i" and nxt.ch == "ل" and article_lam(us, start + 1):
            start += 1                                   # لِلْ: li + the article written without its alif
            break
        if u.ch in "وف" and nxt.ch in "وبكل" and nxt.v in ("a", "i") and not nxt.sh:
            start += 1                                   # وَبِ، فَلِ، فَوَ: another proclitic may follow
            continue
        break

    toks = []
    for u in us[:start]:
        toks += [tok_c(CONS[u.ch]), tok_v(u.v)]
    rest = us[start:]
    kind = None
    if start == 0 and us and us[0].suk and len(us) > 1:
        us.insert(0, U("ا"))                             # بْنُ (between two names): read as ابْنُ
        rest = us
    if rest and rest[0].ch in "اٱ" and not rest[0].v and len(rest) > 1:
        body = rest[1:]
        kind = "article" if body[0].ch == "ل" and article_lam(rest, 1) else "alif"
    elif start and us[start - 1].ch == "ل" and rest and rest[0].ch == "ل" and us[start - 1].v == "i":
        body = rest                                      # لِلْ...: the article's lam with no alif before it
        kind = "article"
        rest = [U("ا")] + rest
    if kind == "article":
        lam, nxt = body[0], body[1]
        sun = not lam.v and not lam.suk and nxt.sh and nxt.ch in SUN
        rest = body[1:] if sun else body                 # a sun letter takes the lam's place (doubled)
        word.art_sun = sun
        if start == 0:
            word.wasl, word.sun = "article", sun
    elif kind == "alif":
        rest = body
        if start == 0:
            word.wasl = "alif"
            first = body[0]
            second = body[1] if len(body) > 1 else None
            if rest and us[0].v:
                word.wasl_vowel = us[0].v
            elif first.v == "u":                         # اُتُّخِذَ (passive)
                word.wasl_vowel = "u"
            elif first.suk and second is not None and second.v == "u" and len(body) > 2 and not b.startswith(tuple(WASL_NOUNS)):
                word.wasl_vowel = "u"                    # اُنْظُرْ، اُخْرُجْ، اُبْتُلِيتُمْ
    word.start_index = len(toks)
    toks += spell(rest, prev_vowel=toks[-1]["s"] if toks and toks[-1]["t"] == "V" else None)
    if kind == "article" and getattr(word, "art_sun", False) and len(toks) > word.start_index:
        toks[word.start_index]["art"] = True
    word.toks = toks
    return word


def spell(us, prev_vowel=None):
    """Letters (after any proclitic / article handling) -> tokens."""
    toks = []
    last_v = prev_vowel           # the last short vowel said (a/i/u) or None after a sukun / long vowel
    prev = None
    n = len(us)
    for j, u in enumerate(us):
        ch = u.ch
        nxt = us[j + 1] if j + 1 < n else None
        if ch in ("ا", "ٱ"):
            if u.v:                                   # an alif carrying a vowel: read as a hamza
                toks += [tok_c("ʔ"), tok_v(u.v)]
                last_v = u.v
            elif prev is not None and prev.tan:       # ـًا: the tanween alif is silent
                pass
            elif prev is not None and prev.ch == "و" and j == n - 1 and toks and toks[-1]["t"] == "V" and toks[-1]["s"] == "u" and toks[-1]["long"]:
                pass                                  # كَفَرُوا: the alif after the plural waw is silent
            elif last_v == "a" or (prev is not None and not prev.v and not prev.suk and prev.ch in CONS):
                if toks and toks[-1]["t"] == "V":
                    toks[-1]["long"] = True
                else:
                    toks.append(tok_v("a", long=True))
                last_v = None
            # otherwise (after kasra / damma / sukun) a wasl alif inside a word: silent
            prev = u
            continue
        if ch == "آ":
            toks += [tok_c("ʔ"), tok_v("a", long=True)]
            last_v = None
            prev = u
            continue
        if ch == "ى":
            if prev is not None and prev.tan:
                pass                                  # هُدًى
            elif toks and toks[-1]["t"] == "V" and not toks[-1]["long"]:
                toks[-1]["long"] = True
            else:
                toks.append(tok_v("i" if last_v == "i" else "a", long=True))
            last_v = None
            prev = u
            continue
        if ch in ("و", "ي") and not u.v and not u.sh and not u.tan:
            glide = "u" if ch == "و" else "i"
            if j > 0 and last_v == glide and toks and toks[-1]["t"] == "V" and not toks[-1]["long"]:
                toks[-1]["long"] = True              # ـُو ـِي: long vowel
                last_v = None
                prev = u
                continue
            if j > 0 and last_v == "a" and toks and toks[-1]["t"] == "V" and not toks[-1]["long"]:
                toks.append(tok_v(glide, dark=toks[-1]["dark"]))   # ـَوْ ـَيْ: diphthong au / ai
                toks[-1]["glide"] = True
                last_v = None
                prev = u
                continue
        if ch in ("و", "ي") and u.sh and DOUBLED_GLIDE != "jj":
            # a doubled و / ي: written the way the voice learned it, as the long vowel (قُوَّة -> "quːa",
            # الْجَاهِلِيَّة -> "dʒaːhiliːa"; 690 such words in its training text, none as "jj")
            glide = "u" if ch == "و" else "i"
            if toks and toks[-1]["t"] == "V" and toks[-1]["s"] == glide and not toks[-1]["long"]:
                toks[-1]["long"] = True
            else:
                toks.append(tok_v(glide, long=True, dark=bool(toks) and toks[-1]["t"] == "V" and toks[-1]["dark"]))
            if DOUBLED_GLIDE == "long+glide":
                toks.append(tok_c("w" if ch == "و" else "j"))
            v = u.v
            if v:
                toks.append(tok_v(v))
                if u.tan:
                    toks[-1]["tan"] = True
                    toks.append(tok_c("n"))
                    toks[-1]["tan"] = True
                    last_v = None
                else:
                    last_v = v
            else:
                last_v = None
            prev = u
            continue
        # a consonant
        sym = CONS.get(ch)
        if sym is None:
            prev = u
            continue
        if ch == "ة" and not u.v and not u.suk:
            u.v = last_v or "a"
        toks.append(tok_c(sym, gem=u.sh))
        dark = ch in THICK
        v = u.v
        if v is None and not u.suk and nxt is not None:      # a missing haraka before a long-vowel letter
            v = {"ا": "a", "ى": "a", "و": "u", "ي": "i"}.get(nxt.ch)
        if u.dag:
            toks.append(tok_v("a", long=True, dark=dark))
            last_v = None
        elif v:
            toks.append(tok_v(v, dark=dark and (not u.tan or v == "a")))
            if u.tan:
                toks[-1]["tan"] = True
                toks.append(tok_c("n"))
                toks[-1]["tan"] = True
                last_v = None
            else:
                last_v = v
        else:
            last_v = None
        prev = u
    # iqlab inside the word: n before b is m
    for k in range(len(toks) - 1):
        if toks[k]["t"] == "C" and toks[k]["s"] == "n" and not toks[k]["gem"] and toks[k + 1]["t"] == "C" and toks[k + 1]["s"] == "b":
            toks[k]["s"] = "m"
    return toks


def waqf(word):
    """Pausal form of the last word before a pause."""
    t = word.toks
    if not t:
        return
    last = t[-1]
    if last["t"] == "C" and last.get("tan"):                  # tanween
        vowel = t[-2]
        del t[-1]
        if vowel["s"] == "a" and not (len(t) >= 2 and t[-2]["t"] == "C" and t[-2]["s"] == "t" and word.us and word.us[-1].ch == "ة"):
            vowel["long"] = True                              # -an -> -aː
            vowel.pop("tan", None)
            return
        del t[-1]
        if t and t[-1]["t"] == "C" and t[-1]["s"] == "t" and word.us and word.us[-1].ch == "ة":
            t[-1]["s"] = "h"
            return
        _keep_cluster_vowel(t, vowel)
        return
    if last["t"] == "V" and not last["long"] and not last.get("glide"):
        vowel = t.pop()
        if t and t[-1]["t"] == "C" and t[-1]["s"] == "t" and word.us and word.us[-1].ch == "ة":
            t[-1]["s"] = "h"
            return
        _keep_cluster_vowel(t, vowel)
        return
    if last["t"] == "C" and last["s"] == "t" and word.us and word.us[-1].ch == "ة":
        last["s"] = "h"


def _keep_cluster_vowel(t, vowel):
    # a word that now ends in two consonants (شِرْكْ، الْأَرْضْ) keeps its short vowel: the voice swallows the cluster
    if len(t) >= 2 and t[-1]["t"] == "C" and not t[-1]["gem"] and t[-2]["t"] == "C":
        vowel = dict(vowel); vowel.pop("tan", None); vowel["long"] = False
        t.append(vowel)


def link(prev, word):
    """Join a word that starts with hamzat al-wasl to the word before it in the same clause."""
    t = prev.toks
    if not t:
        return
    last = t[-1]
    if last["t"] == "V" and last.get("glide"):               # أَوْ / لَوْ before wasl: awi, lawi
        t[-1] = tok_c("w" if last["s"] == "u" else "j")
        t.append(tok_v("i"))
    elif last["t"] == "V" and last["long"]:
        last["long"] = False                                 # فِي الْ -> fil, عَلَى الْ -> ʕalal
    elif last["t"] == "C":
        if last.get("tan"):
            t.append(tok_v("i"))                             # tanween + wasl: -uni l-
        else:
            pb = bare(prev.text)
            v = "a" if pb in ("من", "ومن", "فمن") else "u" if PLURAL_MIM.search(pb) or pb in ("مذ",) else "i"
            t.append(tok_v(v))
    # move the first consonant of the wasl word to the end of the previous word
    w = word.toks
    k = word.start_index
    if word.wasl == "article" and word.sun:
        first = w[k]
        t.append(tok_c(first["s"]))
        first["gem"] = False
    else:
        first = w[k] if k < len(w) else None
        if first is not None and first["t"] == "C":
            if first["gem"]:
                t.append(tok_c(first["s"]))
                first["gem"] = False
            else:
                t.append(w.pop(k))


def clause_start(word):
    """A word that starts with hamzat al-wasl after a pause is said with its vowel (espeak's style)."""
    k = word.start_index
    if word.wasl == "article":
        word.toks[k:k] = [tok_c("ʔ"), tok_v("a")] if word.sun else [tok_v("a")]
    else:
        word.toks[k:k] = [tok_c("ʔ"), tok_v(word.wasl_vowel)]


def render_tok(x):
    if x["t"] == "C":
        s = x["s"]
        if x.get("gem"):
            if x.get("ph1"):
                return s + "ˤ" + s
            return s + "ː" if s in STOPS and not x.get("art") else s + s
        return s + "ˤ" if x.get("ph") else s
    s = x.get("st", "") + x["s"] + ("." if x.get("dark") else "") + ("ː" if x["long"] else "")
    return s


def render(word):
    return "".join(render_tok(x) for x in word.toks)


# ---------------------------------------------------------------- clauses, stress, the whole text

def split_clauses(text):
    """[(words, punctuation)] with punctuation one of PAUSE's keys or ''."""
    out, cur = [], []
    for tok in re.findall(r"[^\s،,؛;:.؟?!]+|[،,؛;:.؟?!]", text):
        if tok in PAUSE:
            if cur:
                out.append((cur, tok))
                cur = []
            elif out:
                out[-1] = (out[-1][0], tok)
        elif any(ch in LETTERS for ch in tok):
            cur.append(tok)
    if cur:
        out.append((cur, ""))
    return out


VOWEL_RE = re.compile(r"([ˈˌ]?)([aiu])(\.?)(ː?)")


def espeak_vowels(ph):
    """Stress of each vowel in espeak's IPA for one word: [(vowel, stress)]."""
    return [(m.group(2), m.group(1)) for m in VOWEL_RE.finditer(ph)]


def transplant_stress(word, espeak_word):
    """Copy espeak's stress marks onto this word's vowels, aligning the two spellings sound by sound."""
    import difflib
    clean, marks = [], {}
    for ch in espeak_word:
        if ch in "ˈˌ":
            marks[len(clean)] = ch
        else:
            clean.append(ch)
    mine, starts = [], {}
    for x in word.toks:
        if x["t"] == "V":
            starts[len(mine)] = x
            x["st"] = ""
        mine.extend(render_tok(x))
    sm = difflib.SequenceMatcher(None, clean, mine, autojunk=False)
    for tag, i1, i2, j1, j2 in sm.get_opcodes():
        for pos, mark in marks.items():
            if i1 <= pos < i2:
                k = j1 + (pos - i1 if tag == "equal" else 0)
                tok = starts.get(k) or next((starts[q] for q in sorted(starts) if q >= k), None)
                if tok is not None and not tok["st"]:
                    tok["st"] = mark


def phonemize(text, espeak=None, waqf_at_pauses=True):
    """Text -> list of sentences (phoneme strings, Piper style: words split by spaces, ", " after a comma ...).

    `espeak`: optional function(list of texts) -> list of lists of (terminator, phonemes) used to copy stress
    (see espeak_ph.py); without it no stress marks are written."""
    text = normalize(text)
    clauses = split_clauses(text)
    stress = None
    if espeak is not None:
        stress = espeak_stress(clauses, espeak)
    sentences, cur = [], ""
    for ci, (words, punct) in enumerate(clauses):
        ws = [analyze(Word(w)) for w in words]
        if stress is not None:
            ew = stress[ci]
            if ew is not None and len(ew) == len(ws):
                for w, e in zip(ws, ew):
                    transplant_stress(w, e)
        for k, w in enumerate(ws):
            if w.wasl:
                if k == 0:
                    clause_start(w)
                else:
                    link(ws[k - 1], w)
        if TAFKHIM:
            tafkhim(ws)
        if waqf_at_pauses or not punct or punct in SENTENCE_END:
            waqf(ws[-1])
        cur += " ".join(render(w) for w in ws if w.toks) + PAUSE.get(punct, "")
        if punct in SENTENCE_END:
            sentences.append(cur.strip())
            cur = ""
    if cur.strip():
        sentences.append(cur.strip())
    return sentences


def tafkhim(ws):
    """The lam of the Name of Allah is heavy (مُغَلَّظَة) unless a kasra comes right before it: «اللَّهُ أَكْبَرُ»,
    «قَالَ اللَّهُ», «عَبْدُ اللَّهِ», «وَاللَّهِ» are heavy, «بِسْمِ اللَّهِ», «لِلَّهِ», «بِاللَّهِ» are light. The voice
    knows the heavy sound as the darkened long a it says after ص ض ط ظ ("a.ː"), so that vowel is darkened.
    Runs after the words are linked, so the vowel before the lam is the one actually said."""
    flat = [x for w in ws for x in w.toks]
    for w in ws:
        if not ALLAH.match(bare(w.text)):
            continue
        t = w.toks
        for j in range(1, len(t) - 1):
            x = t[j]
            if not (x["t"] == "V" and x["s"] == "a" and x["long"] and t[j - 1]["t"] == "C" and t[j - 1]["s"] == "l"
                    and t[j + 1]["t"] == "C" and t[j + 1]["s"] == "h"):
                continue
            lam = next(i for i, y in enumerate(flat) if y is t[j - 1])
            k = lam - 1
            while k >= 0 and flat[k]["t"] != "V":
                k -= 1
            if k < 0 or flat[k]["s"] != "i":
                x["dark"] = True
                if k >= 0 and flat[k]["s"] == "a":
                    flat[k]["dark"] = True           # the fatha that leads into the heavy lam takes its colour
                # and the first of the two lams is said heavy ("lˤ"): kept in the word ("ʔa.lˤla.ːh"), or at
                # the end of the word before it when the two are split by the linking ("qaːla.lˤ la.ːh")
                if t[j - 1]["gem"]:
                    t[j - 1]["ph1"] = True
                elif lam > 0 and flat[lam - 1]["t"] == "C" and flat[lam - 1]["s"] == "l":
                    flat[lam - 1]["ph"] = True
            break


def espeak_stress(clauses, espeak):
    """espeak's phonemes for each clause's words (as the old step prepared them), or None where words don't line up."""
    import os, sys
    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    from arabic_tts import prepare
    texts = [prepare(" ".join(words)) for words, _ in clauses]
    res = espeak(texts)
    out = []
    for (words, _), r in zip(clauses, res):
        ph = " ".join(p for _, p in r).split()
        out.append(ph if len(ph) == len(words) else None)
    return out


if __name__ == "__main__":
    import sys, os
    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    from espeak_ph import phonemize as esp
    t = " ".join(sys.argv[1:])
    for s in phonemize(t, espeak=esp):
        print(s)
