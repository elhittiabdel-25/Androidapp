"""Run by the GitHub build: download recitation audio (one file per verse) and the
Uthmani text for every verse the cards cite. Missing files only disable the play button."""
import json, os, re, sys, time, urllib.request

ASSETS = sys.argv[1] if len(sys.argv) > 1 else "app/src/main/assets"
RECITER = "Alafasy_64kbps"   # everyayah.com folder: Mishary Alafasy, 64 kbps
cards = json.load(open(f"{ASSETS}/content/cards.json", encoding="utf-8"))["cards"]
ayat = set()
for c in cards:
    for e in c["evidence"]:
        if e["kind"] != "quran": continue
        s, a = e["ref"].split(":")
        lo, hi = (a.split("-") + [a])[:2]
        for n in range(int(lo), int(hi) + 1): ayat.add((int(s), n))

def get(url, tries=3):
    for i in range(tries):
        try:
            req = urllib.request.Request(url, headers={"User-Agent": "UsulCards-build"})
            with urllib.request.urlopen(req, timeout=30) as r: return r.read()
        except Exception as ex:
            print(f"  retry {i+1} {url}: {ex}"); time.sleep(2 * (i + 1))
    return None

os.makedirs(f"{ASSETS}/quran", exist_ok=True)
uthmani, ok_audio = {}, 0
for s, a in sorted(ayat):
    mp3 = f"{ASSETS}/quran/{s:03d}{a:03d}.mp3"
    if not os.path.exists(mp3):
        data = get(f"https://everyayah.com/data/{RECITER}/{s:03d}{a:03d}.mp3")
        if data and len(data) > 1000: open(mp3, "wb").write(data)
    ok_audio += os.path.exists(mp3)
    raw = get(f"https://api.alquran.cloud/v1/ayah/{s}:{a}/quran-uthmani")
    if raw:
        text = json.loads(raw)["data"]["text"]
        if a == 1 and s not in (1, 9):   # the API prefixes the basmala to the first verse of a surah
            words = text.split()
            if words and re.sub(r"[ً-ٰٟۖ-ۭ]", "", words[0]).startswith("بسم"):
                text = " ".join(words[4:])
        uthmani[f"{s}:{a}"] = text
    time.sleep(0.2)
json.dump(uthmani, open(f"{ASSETS}/quran/uthmani.json", "w", encoding="utf-8"), ensure_ascii=False)
print(f"verses cited: {len(ayat)} | recitations: {ok_audio} | uthmani texts: {len(uthmani)}")
