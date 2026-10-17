"""Prepare fully-vowelled Arabic text for the Piper/espeak-ng Arabic voice.

Fixes found by inspecting the phonemes espeak-ng produces:
  1. espeak-ng expects SHADDA *before* the short vowel. Unicode canonical order (NFC)
     puts the vowel first, which makes espeak drop the vowel: مُحَمَّدٌ -> "muħammdun".
  2. The name of Allah needs explicit spellings, otherwise the article's lam plus the
     doubled lam become a triple "lll" with a short a: اللَّهُ -> "ʔalllhu".
  3. Pausal forms (waqf) at every pause mark and at the end of a segment.
"""
import re

FATHATAN, DAMMATAN, KASRATAN = "ً", "ٌ", "ٍ"
FATHA, DAMMA, KASRA, SHADDA, SUKUN, DAGGER = "َ", "ُ", "ِ", "ّ", "ْ", "ٰ"
VOWELS = FATHATAN + DAMMATAN + KASRATAN + FATHA + DAMMA + KASRA
MARKS = VOWELS + SHADDA + SUKUN + DAGGER
PAUSE = ".،؛:؟!"
AR = "ء-يٱ"

def strip(w):
    return re.sub(f"[{MARKS}ـ]", "", w).replace("ٱ", "ا")

def final_vowel(w):
    m = re.search(f"([{VOWELS}{SUKUN}])[{SHADDA}]?$", w)
    return m.group(1) if m else ""

# -- 3. pausal endings -------------------------------------------------------
def waqf_word(w):
    core = w.rstrip(MARKS)
    tail = w[len(core):]
    if FATHATAN in tail and core.endswith("ا") is False and w.endswith("ا"):
        pass
    if w.endswith("ا") and FATHATAN in w[-3:]:            # فِعْلًا -> فِعْلَا  (-an -> -ā)
        return w.replace(FATHATAN, FATHA)
    if core.endswith("ة"):                                 # بَاطِلَةٌ -> بَاطِلَهْ
        return core[:-1] + "ه" + SUKUN
    if not tail:
        return w
    return core + (SHADDA if SHADDA in tail else "") + SUKUN

def apply_waqf(text):
    out = []
    tokens = re.split(r"(\s+)", text)
    word_idx = [i for i, t in enumerate(tokens) if re.search(f"[{AR}]", t)]
    last = word_idx[-1] if word_idx else -1
    for i, tok in enumerate(tokens):
        m = re.match(f"^([{AR}{MARKS}]+)([{PAUSE}]*)$", tok)
        if m and (m.group(2) or i == last):
            tok = waqf_word(m.group(1)) + m.group(2)
        out.append(tok)
    return "".join(out)

# -- 2. the name of Allah ----------------------------------------------------
ALLAH = {   # bare form -> spelling (case vowel appended separately)
    "الله": "اللاه", "والله": "وَلَّاه", "فالله": "فَلَّاه", "تالله": "تَلَّاه",
    "لله": "لِلَّاه", "ولله": "وَلِلَّاه", "فلله": "فَلِلَّاه",
    "بالله": "بِلَّاه", "وبالله": "وَبِلَّاه",
    "اللهم": "اللاهُمَّ",
}
def fix_allah(text):
    def repl(m):
        w = m.group(0); base = strip(w)
        if base not in ALLAH: return w
        sp = ALLAH[base]
        if base == "اللهم": return sp
        v = final_vowel(w)
        if base in ("لله", "ولله", "فلله", "بالله", "وبالله") and v not in (SUKUN, ""):
            v = KASRA
        return sp + (v if v != SUKUN else "")
    return re.sub(f"[{AR}{MARKS}]+", repl, text)

# -- 1. shadda before vowel --------------------------------------------------
def shadda_first(text):
    return re.sub(f"([{VOWELS}])({SHADDA})", r"\2\1", text)

# -- 4. article + sun letter: espeak already doubles it, so drop the written shadda
SUN = "تثدذرزسشصضطظلن"
def sun_letters(text):
    # only word-initial article; after و/ف/ك the written shadda is still needed
    return re.sub(f"(?<![{AR}{MARKS}])(ال)([{SUN}])([{VOWELS}]?){SHADDA}([{VOWELS}]?)", r"\1\2\3\4", text)

# -- 6. bi- + article: espeak reads the silent alif as a long ā (بِالْحَقِّ -> "biāl...")
def bi_article(text):
    def repl(m):
        nxt = m.group(1)
        return "ب" + KASRA + nxt if nxt in SUN else "ب" + KASRA + "ل" + SUKUN + nxt
    return re.sub(f"(?<![{AR}{MARKS}])ب{KASRA}?ال{SUKUN}?([{AR}])", repl, text)

# -- 5. doubled waw / ya: write the doubling out, or espeak makes a long vowel
def double_wy(text):
    return re.sub(f"([وي])(?:{SHADDA}([{VOWELS}])|([{VOWELS}]){SHADDA})", lambda m: m.group(1) + SUKUN + m.group(1) + (m.group(2) or m.group(3)), text)

# -- 7. silent (wasl) alif: espeak reads it as a long ā (الِاسْتِعَانَة -> "ali-āsti...", فَاعْبُدْ -> "fāʕbud")
def wasl_alif(text):
    text = re.sub(f"(?<![{AR}{MARKS}])ال{KASRA}ا", "ال" + KASRA, text)                          # الِاسْتِعَانَة -> الِسْتِعَانَة
    text = re.sub(f"(?<![{AR}{MARKS}])([وف]{FATHA})ال{KASRA}ا", r"\1ل" + KASRA, text)            # وَالِاسْتِعَاذَة -> وَلِسْتِعَاذَة
    text = re.sub(f"(?<![{AR}{MARKS}])([وفبلك][{FATHA}{KASRA}])ا(?=[\u0621-\u0643\u0645-\u064A]{SUKUN})", r"\1", text)  # لِابْنِ -> لِبْنِ
    return text

# -- 8. waw/ya carrying a vowel after a short vowel: espeak makes a diphthong (سَوَاءٌ -> "sa-u-āʔ").
#       A tatweel between them is invisible to the listener and keeps the consonant: سَـوَاءٌ -> "sawāʔ".
def glide_consonant(text):
    text = text.replace("آ", "ء" + FATHA + "ا")                                                  # آيَةُ -> ءَايَةُ ("ʔāyatu")
    return re.sub(f"([{FATHA}{DAMMA}{KASRA}])([وي])(?=[{FATHATAN}{DAMMATAN}{KASRATAN}{FATHA}{DAMMA}{KASRA}{SHADDA}])", "\\1\u0640\\2", text)

def prepare(text):
    text = text.replace("«", "").replace("»", "").replace("ﷺ", "صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ")
    text = apply_waqf(text)
    text = fix_allah(text)
    text = bi_article(text)
    text = wasl_alif(text)
    text = sun_letters(text)
    text = double_wy(text)
    text = shadda_first(text)
    text = glide_consonant(text)
    return text
