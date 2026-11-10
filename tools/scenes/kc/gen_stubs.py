"""Stub the ui package's internal API (signatures only) so new ArtWave files can be type-checked without Android.
usage: python3 gen_stubs.py SKIP_FILE... > ProjectStubs.kt   (SKIP: the new files being checked, left out)"""
import glob, os, re, sys
UI = '/home/claude/androidapp/app/src/main/java/app/usulcards/ui'
skip = {os.path.basename(s) for s in sys.argv[1:]}
KEEP_BLOCKS = {'object ArtInk', 'object Roundel', 'class Shot', 'class ArtCtx', 'class Paper'}
out = ['@file:Suppress("unused", "UNUSED_PARAMETER")', 'package app.usulcards.ui', '',
       'import androidx.compose.ui.geometry.*', 'import androidx.compose.ui.graphics.*',
       'import androidx.compose.ui.graphics.drawscope.*', 'import androidx.compose.ui.text.TextLayoutResult', '',
       'internal object Cinema { var prophetName: TextLayoutResult? = null; var dayFrame = false; var darkFrame = false }', '']
seen = set()

def header(src, i):
    """From 'internal fun' at i: the header text and whether it has a return type."""
    j, depth, opened = i, 0, False
    while j < len(src):
        ch = src[j]
        if ch == '(':
            depth += 1; opened = True
        elif ch == ')':
            depth -= 1
        elif opened and depth == 0 and ch in '{=':
            if ch == '=' and src[j:j + 2] == '==':
                j += 2; continue
            h = src[i:j].rstrip()
            close = h.rfind(')')
            ret = h[close + 1:].strip().startswith(':')
            return h, ret, ch
        j += 1
    return None, False, None

def block(src, i):
    """A class/object declaration from i: through its body if it has one, else its constructor line(s)."""
    k, depth = i, 0
    while k < len(src):
        ch = src[k]
        if ch == '(':
            depth += 1
        elif ch == ')':
            depth -= 1
        elif depth == 0 and ch == '{':
            break
        elif depth == 0 and ch == '\n' and src[i:k].count('(') == src[i:k].count(')'):
            nxt = src[k:].lstrip()
            if not nxt.startswith('{'):
                return src[i:k]
        k += 1
    d = 0
    for m in range(k, len(src)):
        if src[m] == '{': d += 1
        elif src[m] == '}':
            d -= 1
            if d == 0: return src[i:m + 1]
    return src[i:]

for f in sorted(glob.glob(f'{UI}/*.kt')):
    if os.path.basename(f) in skip:
        continue
    src = open(f, encoding='utf-8').read()
    for m in re.finditer(r'^internal (fun|val|object|class|data class) ([^\n]*)', src, re.M):
        kind, rest = m.group(1), m.group(2)
        if kind == 'fun':
            h, ret, ch = header(src, m.start())
            if h is None: continue
            name = re.match(r'internal fun (?:<[^>]*> )?([\w.]+)\(', h)
            sig = re.sub(r'\s+', ' ', h)
            if sig in seen: continue
            seen.add(sig)
            if ch == '=' and not ret:
                continue                     # expression body without a declared type: skip
            out.append(sig + (' = TODO()' if ret else ' {}'))
        elif kind == 'val':
            mm = re.match(r'(\w+)\s*:\s*([^=]+?)\s*=', rest)
            if mm:
                out.append(f'internal val {mm.group(1)}: {mm.group(2)} = TODO()')
            else:
                mm = re.match(r'(\w+)\s*=\s*setOf\(', rest)
                if mm: out.append(f'internal val {mm.group(1)}: Set<String> = TODO()')
        else:
            key = f'{kind.split()[-1]} {re.match(r"(\w+)", rest).group(1)}'
            if key in KEEP_BLOCKS and key not in seen:
                seen.add(key)
                out.append(block(src, m.start()))
BAD = re.compile(r'\b(Scene|Quiz|Card|Twinkle|Modifier|Composable|FilmFrame|Camera|ImageBitmap|TextMeasurer|Typeface|Font)\b')
print('\n'.join(l for l in out if not BAD.search(l)))
