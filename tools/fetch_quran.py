"""Run by the GitHub build: download the recitation (one file per verse) and the Uthmani text for
every verse the cards cite. Missing files only disable the play button.

Source: AlQuran.cloud (Islamic Network). Its terms allow the Quran text to be reproduced freely for
non-commercial use, unaltered and with its diacritics, with attribution (the text is Tanzil.net's), and
its recitations to be redistributed freely for non-commercial use at the bitrates it publishes. The app
credits both on its About screen. https://alquran.cloud/terms-and-conditions
"""
import json, os, re, subprocess, sys, time, urllib.request

ASSETS = sys.argv[1] if len(sys.argv) > 1 else "app/src/main/assets"
RECITER = "ar.alafasy"          # Mishary Rashid Alafasy
# Alafasy is published at 128 and 64 kbps only (48, 40 and 32 answer 403, checked 2026-10-28); the terms
# allow redistribution "at the bitrates we publish", so 64 kbps is the smallest the app may ship as is.
# QURAN_OPUS_KBPS > 0 (set in the build workflow) re-encodes the 128 kbps files to Opus at that rate, mono,
# as NNNNNN.ogg: only once AlQuran.cloud has allowed it in writing (32 kbps chosen by ear: 67 MB -> ~41 MB).
OPUS = int(os.environ.get("QURAN_OPUS_KBPS", "0") or 0)
EXT = "ogg" if OPUS else "mp3"
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
            req = urllib.request.Request(url, headers={"User-Agent": "Re-Iman-build"})
            with urllib.request.urlopen(req, timeout=30) as r: return r.read()
        except Exception as ex:
            print(f"  retry {i+1} {url}: {ex}"); time.sleep(2 * (i + 1))
    return None

os.makedirs(f"{ASSETS}/quran", exist_ok=True)
try:    # files kept from an earlier build by the workflow cache: only new verses go to the network
    cached = json.load(open(f"{ASSETS}/quran/uthmani.json", encoding="utf-8"))
except (OSError, ValueError):
    cached = {}
uthmani, ok_audio = {}, 0
rate = __import__("collections").Counter()     # recitations downloaded in this run, by bitrate
for s, a in sorted(ayat):
    if f"{s}:{a}" in cached and os.path.exists(f"{ASSETS}/quran/{s:03d}{a:03d}.{EXT}"):
        uthmani[f"{s}:{a}"] = cached[f"{s}:{a}"]
        ok_audio += 1
        continue
    # one call returns both editions: [0] the Uthmani text, [1] the recitation with its audio links
    raw = get(f"https://api.alquran.cloud/v1/ayah/{s}:{a}/editions/quran-uthmani,{RECITER}")
    links = []
    if raw:
        text_ed, audio_ed = json.loads(raw)["data"]
        text = text_ed["text"]
        if a == 1 and s not in (1, 9):   # the API prefixes the basmala to the first verse of a surah
            words = text.split()
            if words and re.sub(r"[ً-ٰٟۖ-ۭ]", "", words[0]).startswith("بسم"):
                text = " ".join(words[4:])
        uthmani[f"{s}:{a}"] = text
        links = list(audio_ed.get("audioSecondary") or []) + [audio_ed.get("audio")]   # 64 kbps first, then 128
        if OPUS:
            links = [audio_ed.get("audio")] + list(audio_ed.get("audioSecondary") or [])  # the best source first
    mp3 = f"{ASSETS}/quran/{s:03d}{a:03d}.mp3"
    out = f"{ASSETS}/quran/{s:03d}{a:03d}.{EXT}"
    if not os.path.exists(out):
        for url in filter(None, links):
            audio = get(url)
            if audio and len(audio) > 1000:
                open(mp3, "wb").write(audio)
                rate[url.split("/audio/")[1].split("/")[0] if "/audio/" in url else "?"] += 1
                break
        if OPUS and os.path.exists(mp3):
            subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", mp3, "-c:a", "libopus", "-b:a", f"{OPUS}k",
                            "-ac", "1", out], check=True)
            os.remove(mp3)
    ok_audio += os.path.exists(out)
    time.sleep(0.2)
json.dump(uthmani, open(f"{ASSETS}/quran/uthmani.json", "w", encoding="utf-8"), ensure_ascii=False)
cited = {f"{s:03d}{a:03d}.{EXT}" for s, a in ayat}
for f in os.listdir(f"{ASSETS}/quran"):     # a verse no longer cited (or in the other format) must not stay in the app
    if (f.endswith(".mp3") or f.endswith(".ogg")) and f not in cited:
        os.remove(f"{ASSETS}/quran/{f}")
size = sum(os.path.getsize(f"{ASSETS}/quran/{f}") for f in os.listdir(f"{ASSETS}/quran") if f.endswith("." + EXT))
got = ", ".join(f"{n} at {k} kbps" for k, n in sorted(rate.items())) or "none new"
got += f"; shipped as Opus {OPUS} kbps" if OPUS else "; shipped as published (MP3)"
print(f"verses cited: {len(ayat)} | recitations: {ok_audio} ({size / 1048576:.1f} MB; downloaded now: {got}) | uthmani texts: {len(uthmani)}")
