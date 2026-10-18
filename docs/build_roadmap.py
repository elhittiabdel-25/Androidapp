"""Generate docs/roadmap.html (the 78-book roadmap page) from the matched book list."""
import csv, html, os

HERE = os.path.dirname(os.path.abspath(__file__))
SRC = os.environ.get("BOOKS_CSV", os.path.join(HERE, "books78.csv"))
rows = [r for r in csv.DictReader(open(SRC, encoding="utf-8-sig")) if not r["status"].startswith("open")]

IN_APP = {"113931", "113912", "15382", "90307", "90367", "90453"}   # plus 8391 (al-Fawzan's sharh of al-Qawaid), not in the list

# Planned category and release wave for each book; card estimates are rough (short treatise ~6, long book ~10-25, only key ideas)
PLAN = {
    # ── Aqeedah ──
    "113931": ("التوحيد", "1.0", 6), "15382": ("التوحيد", "1.0", 20), "107145": ("التوحيد", "1.1", 6),
    "4873": ("التوحيد", "1.1", 8), "4865": ("التوحيد", "1.1", 8), "15892": ("التوحيد", "1.1", 10), "15714": ("التوحيد", "1.1", 6),
    "116093": ("الشرك وما يضاد التوحيد", "1.1", 4), "90307": ("الشرك وما يضاد التوحيد", "1.0", 5), "5646": ("الشرك وما يضاد التوحيد", "1.1", 6),
    "90453": ("الإيمان", "1.0", 8), "90367": ("الإيمان", "1.0", 4), "90456": ("الإيمان", "1.1", 8), "89743": ("الإيمان", "1.1", 12),
    "5845": ("الإيمان", "1.1", 6), "116143": ("الإيمان", "1.1", 8), "115995": ("الإيمان", "1.1", 6),
    "113912": ("منهج أهل السنة", "1.0", 7), "7650": ("منهج أهل السنة", "1.1", 8), "90444": ("منهج أهل السنة", "1.1", 6),
    "90384": ("منهج أهل السنة", "1.1", 6), "628": ("منهج أهل السنة", "1.1", 8), "97696": ("منهج أهل السنة", "1.1", 8), "765": ("منهج أهل السنة", "1.1", 4),
    # ── Sunnah & Seerah ──
    "106498": ("الأربعون وجوامع الكلم", "1.2", 42), "9295": ("الأذكار والآداب", "1.2", 30), "492": ("الأذكار والآداب", "1.2", 25),
    "6773": ("السيرة والشمائل", "1.2", 10), "539": ("الأربعون وجوامع الكلم", "2.0", 15), "4378": ("الأذكار والآداب", "2.0", 12),
    "116109": ("الأذكار والآداب", "2.0", 8), "115859": ("أحاديث الأحكام", "2.0", 12), "15947": ("أحاديث الأحكام", "2.0", 12),
    "436": ("أحاديث الأحكام", "2.0", 15), "4626": ("مصطلح الحديث", "2.0", 8),
    "16399": ("السيرة والشمائل", "1.5", 12), "16398": ("السيرة والشمائل", "1.5", 12), "16397": ("السيرة والشمائل", "1.5", 12),
    "16396": ("السيرة والشمائل", "1.5", 12), "6704": ("السيرة والشمائل", "1.5", 10), "115877": ("السيرة والشمائل", "1.5", 12),
    "106765": ("السيرة والشمائل", "1.5", 8), "20627": ("السيرة والشمائل", "1.5", 10), "107325": ("السيرة والشمائل", "1.5", 6),
    # ── Fiqh ──
    "107576": ("الطهارة والصلاة", "1.3", 25), "37168": ("الطهارة والصلاة", "1.3", 20), "8728": ("الطهارة والصلاة", "1.3", 8),
    "114079": ("أحكام المرأة والأسرة", "1.3", 6), "73620": ("الصيام ورمضان", "1.3", 12), "115992": ("الصيام ورمضان", "1.3", 5),
    "115993": ("الصيام ورمضان", "1.3", 5), "114224": ("الحج والزكاة والأضحية", "1.3", 12), "115196": ("الحج والزكاة والأضحية", "1.3", 6),
    "116115": ("الحج والزكاة والأضحية", "2.0", 4), "12493": ("الطهارة والصلاة", "2.0", 20), "16034": ("متون الفقه", "2.0", 15),
    "7683": ("متون الفقه", "2.0", 8), "12498": ("متون الفقه", "2.0", 10), "133": ("متون الفقه", "2.0", 12),
    "19903": ("متون الفقه", "2.0", 10), "16043": ("متون الفقه", "2.0", 6), "12502": ("متون الفقه", "2.0", 6),
    # ── Quran ──
    "3423": ("التفسير", "1.4", 20), "3523": ("التفسير", "1.4", 15), "74054": ("التفسير", "1.4", 12),
    "116021": ("التجويد والتلاوة", "1.4", 8), "15622": ("التجويد والتلاوة", "1.4", 8), "19943": ("علوم القرآن وفضائله", "1.4", 6),
    "2889": ("التفسير", "2.0", 15), "3476": ("التفسير", "2.0", 15), "3328": ("التفسير", "2.0", 15), "19873": ("التفسير", "2.0", 10),
    "112309": ("علوم القرآن وفضائله", "2.0", 6), "90318": ("علوم القرآن وفضائله", "2.0", 8), "90277": ("علوم القرآن وفضائله", "2.0", 5),
    "18628": ("علوم القرآن وفضائله", "2.0", 10), "4136": ("التجويد والتلاوة", "2.0", 8), "90412": ("علوم القرآن وفضائله", "2.0", 10),
}
missing = [r["content_id"] for r in rows if r["content_id"] not in PLAN]
assert not missing, missing

TOPIC = {"Aqeedah": "العقيدة", "Sunnah & Seerah": "الحديث والسيرة", "Fiqh": "الفقه", "Quran & Tafsir": "القرآن وعلومه"}
WAVES = [
    ("1.0", "Now", "First public release", "The 4 Aqeedah categories already in the app"),
    ("1.1", "+3 batches", "Aqeedah complete", "18 more books on tawhid, iman and the method of Ahl al-Sunnah"),
    ("1.2", "+3 batches", "Hadith and adhkar", "The 40 Nawawi, Hisn al-Muslim, a selection of Riyad al-Salihin, the Shama'il"),
    ("1.3", "+3 batches", "Fiqh essentials", "Purification and prayer, fasting and Ramadan, zakat, hajj and udhiyah, rulings for women"),
    ("1.4", "+2 batches", "Quran essentials", "Tafsir of Juz Amma and the last tenth, tajweed basics, virtues of the Quran"),
    ("1.5", "+3 batches", "Seerah", "The four Zad seerah levels, Zad al-Ma'ad abridged, the mothers of the believers"),
    ("2.0", "+5 batches", "All 78 books", "Intermediate and reference works: fiqh texts, larger tafsir, hadith of rulings"),
]
CARD_VOICE_MB = 8.6 / 324 * (324 / 46)      # Opus 24 kbps, measured on the current 46 cards
VERSE_MB = 0.155                            # 64 kbps recitation, as published
VERSES_PER_CARD = {"العقيدة": 2.2, "الحديث والسيرة": 0.8, "الفقه": 1.2, "القرآن وعلومه": 4.0}

by_wave = {}
for r in rows:
    cat, wave, est = PLAN[r["content_id"]]
    by_wave.setdefault(wave, []).append((r, cat, est))
cum, cum_mb, wave_rows = 0, 0.0, []
for wave, when, title, what in WAVES:
    items = by_wave.get(wave, [])
    cards = sum(e for _, _, e in items) if wave != "1.0" else 46
    if wave == "1.0":
        mb_add = 41.6   # the built 1.0 APK
    else:
        mb_add = sum(e * (CARD_VOICE_MB + VERSES_PER_CARD[TOPIC[r["topic"]]] * VERSE_MB * 0.7) for r, _, e in items)  # 0.7: verses shared between cards
    cum += cards
    cum_mb += mb_add
    when = when if wave == "1.0" else f"~{max(1, round(cards / 35))} batches"
    wave_rows.append((wave, when, title, what, len(items) if wave != "1.0" else 7, cards, cum, cum_mb))

def size_chip(mb):
    # Play Console size limits (checked Oct 2026): the base module may be up to 500 MB compressed;
    # above 200 MB, people on mobile data see a skippable notice suggesting Wi-Fi.
    if mb <= 200:
        return '<span class="chip ok">one download</span>'
    if mb <= 500:
        return '<span class="chip warn">one download, Wi-Fi notice</span>'
    return '<span class="chip pad">asset packs</span>'

wave_html = "".join(
    f'<tr><td><b>v{w}</b><div class="dim">{html.escape(when)}</div></td><td><b>{html.escape(t)}</b><div class="dim">{html.escape(what)}</div></td>'
    f'<td class="n">{b}</td><td class="n">{c}</td><td class="n">{cu}</td><td class="n">{mb:.0f} MB</td><td>{size_chip(mb)}</td></tr>'
    for w, when, t, what, b, c, cu, mb in wave_rows)

map_html = []
for topic_en, topic_ar in TOPIC.items():
    trs = []
    items = sorted((r for r in rows if r["topic"] == topic_en), key=lambda r: (PLAN[r["content_id"]][1], PLAN[r["content_id"]][0]))
    total = sum(PLAN[r["content_id"]][2] for r in items)
    for r in items:
        cat, wave, est = PLAN[r["content_id"]]
        status = {"original in library": "the book itself", "commentary/abridgment of target": "commentary", "replacement (target not in library)": "replacement"}[r["status"]]
        inapp = '<span class="chip ok">in the app</span>' if r["content_id"] in IN_APP else f'<span class="chip wave">v{wave}</span>'
        trs.append(f'<tr><td dir="rtl" lang="ar" class="ar">{html.escape(r["title"])}<div class="dim">{html.escape(r["author"])}</div></td>'
                   f'<td dir="rtl" lang="ar" class="ar">{html.escape(cat)}</td><td>{status}{" · large" if r["large"].strip() else ""}</td>'
                   f'<td class="n">~{est}</td><td>{inapp}</td></tr>')
    map_html.append(f'<details{" open" if topic_en == "Aqeedah" else ""}><summary><span class="ar" lang="ar">{topic_ar}</span> · {topic_en} '
                    f'<span class="dim">({len(items)} books, about {total} cards)</span></summary>'
                    f'<div class="scroll"><table class="grid"><thead><tr><th>Book</th><th>Category</th><th>Source</th><th>Cards</th><th>Release</th></tr></thead>'
                    f'<tbody>{"".join(trs)}</tbody></table></div></details>')

cross = next((w for w in wave_rows if w[7] > 200), None)
cross_text = f"around v{cross[0]} ({cross[2].lower()})" if cross else "only at the very end"

page = f"""<title>Re-Iman 78-Book Roadmap</title>
<meta name="claude:viewport-fit-sides" content="cover">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=IBM+Plex+Sans:wght@400;600&family=IBM+Plex+Sans+Arabic:wght@400;600&display=swap">
<style>
  /* Plan column: summary, release waves (the spine), size plan, then the full book map by topic. */
  :root {{
    --bg: #eef1f7; --surface: #ffffff; --ink: #162033; --muted: #586377; --line: #d8dee9; --field: #f5f7fb;
    --accent: #23439a; --accent-soft: #e3e9f8; --band: #1c3a86; --band-ink: #ffffff; --ring: #e5b54b;
    --green: #1f7a52; --green-soft: #e1f2e9; --amber: #9a5a0c; --amber-soft: #fbeedb; --violet: #5b3fa8; --violet-soft: #ece6fb;
    --sans: "IBM Plex Sans", "IBM Plex Sans Arabic", "Segoe UI", Tahoma, Arial, sans-serif;
  }}
  @media (prefers-color-scheme: dark) {{
    :root:not([data-theme="light"]) {{
      --bg: #0d121b; --surface: #161e2b; --ink: #e5eaf3; --muted: #9aa5b8; --line: #2a3447; --field: #111925;
      --accent: #92aaf2; --accent-soft: #1e2a46; --band: #13285f; --band-ink: #f1f4fb; --ring: #e5b54b;
      --green: #66c79d; --green-soft: #15302a; --amber: #f0a75a; --amber-soft: #33240f; --violet: #b9a5f5; --violet-soft: #251d3d; color-scheme: dark;
    }}
  }}
  :root[data-theme="dark"] {{
    --bg: #0d121b; --surface: #161e2b; --ink: #e5eaf3; --muted: #9aa5b8; --line: #2a3447; --field: #111925;
    --accent: #92aaf2; --accent-soft: #1e2a46; --band: #13285f; --band-ink: #f1f4fb; --ring: #e5b54b;
    --green: #66c79d; --green-soft: #15302a; --amber: #f0a75a; --amber-soft: #33240f; --violet: #b9a5f5; --violet-soft: #251d3d; color-scheme: dark;
  }}
  body {{ background: var(--bg); color: var(--ink); font-family: var(--sans); font-size: 15px; line-height: 1.6; }}
  .wrap {{ max-width: 920px; margin: 0 auto; padding-left: max(16px, env(safe-area-inset-left, 0px)); padding-right: max(16px, env(safe-area-inset-right, 0px)); }}
  .band {{ background: var(--band); color: var(--band-ink); }}
  .band .wrap {{ display: flex; gap: 14px; align-items: center; padding-block: 20px; flex-wrap: wrap; }}
  .mark {{ width: 46px; height: 46px; flex: none; }}
  .mark .ring {{ fill: none; stroke: var(--ring); stroke-width: 4.3; stroke-linecap: round; }}
  .mark .head, .mark .dot {{ fill: var(--ring); }} .mark .star {{ fill: var(--band-ink); }}
  .word {{ font-weight: 600; font-size: 1.45rem; line-height: 1.1; }} .word span {{ color: var(--ring); }}
  main .wrap {{ display: grid; gap: 18px; padding-block: 22px 40px; }}
  section {{ background: var(--surface); border: 1px solid var(--line); border-radius: 14px; padding: 18px; min-width: 0; }}
  h2 {{ font-size: 1.15rem; margin: 0 0 8px; text-wrap: balance; }}
  p {{ margin: 0 0 10px; }}
  .stats {{ display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 12px; margin-top: 6px; }}
  .stat {{ background: var(--field); border: 1px solid var(--line); border-radius: 10px; padding: 10px 12px; }}
  .stat b {{ display: block; font-size: 1.35rem; font-variant-numeric: tabular-nums; }}
  .stat span {{ color: var(--muted); font-size: .85rem; }}
  .scroll {{ overflow-x: auto; }}
  table.grid {{ width: 100%; border-collapse: collapse; font-size: .9rem; }}
  .grid th, .grid td {{ text-align: start; vertical-align: top; padding: 7px 9px; border-bottom: 1px solid var(--line); }}
  .grid th {{ font-size: .76rem; text-transform: uppercase; letter-spacing: .05em; color: var(--muted); white-space: nowrap; }}
  .n {{ text-align: end !important; font-variant-numeric: tabular-nums; white-space: nowrap; }}
  .dim {{ color: var(--muted); font-size: .84rem; }}
  .ar {{ font-family: "IBM Plex Sans Arabic", var(--sans); }}
  td.ar {{ text-align: right !important; }}
  .chip {{ font-size: .78rem; font-weight: 600; border-radius: 999px; padding: 2px 9px; white-space: nowrap; }}
  .chip.ok {{ color: var(--green); background: var(--green-soft); }} .chip.warn {{ color: var(--amber); background: var(--amber-soft); }}
  .chip.pad {{ color: var(--violet); background: var(--violet-soft); }} .chip.wave {{ color: var(--accent); background: var(--accent-soft); }}
  details {{ border-top: 1px solid var(--line); padding-block: 10px; }}
  details:first-of-type {{ border-top: 0; }}
  summary {{ cursor: pointer; font-weight: 600; }}
  summary:focus-visible {{ outline: 2px solid var(--accent); outline-offset: 2px; border-radius: 4px; }}
  ul {{ margin: 6px 0 0; padding-inline-start: 20px; }} li {{ margin: 4px 0; }}
  code {{ background: var(--field); border: 1px solid var(--line); border-radius: 5px; padding: 0 4px; font-size: .88em; }}
  footer {{ color: var(--muted); font-size: .85rem; text-align: center; padding-block: 0 30px; }}
</style>

<header class="band"><div class="wrap">
  <svg class="mark" viewBox="18 18 72 72" aria-hidden="true">
    <path class="ring" d="M73.745,42.6 A22.8,22.8 0 1,1 54,31.2"/><path class="head" d="M61.2,31.2 L52.8,24.96 L52.8,37.44 Z"/>
    <path class="star" d="M45.09,45.09 L62.91,45.09 L62.91,62.91 L45.09,62.91 Z"/><path class="star" d="M54,41.4 L66.6,54 L54,66.6 L41.4,54 Z"/><circle class="dot" cx="54" cy="54" r="3.12"/>
  </svg>
  <div><div class="word"><span>Re</span>-Iman</div><div>From 7 books to 78</div></div>
</div></header>

<main><div class="wrap">
  <section>
    <h2>Where we are, where we're going</h2>
    <p>Version 1.0 has 46 cards from 7 books. The goal is the 78 books matched in the library: about {wave_rows[-1][6]} cards across four topics. Cards keep the same standard as today: one idea each, full tashkeel, evidence with book and page, voice, recitation and an animation.</p>
    <div class="stats">
      <div class="stat"><b>{len(rows)}</b><span>books in the plan</span></div>
      <div class="stat"><b>6 + 1</b><span>already in the app (+ al-Fawzan's sharh)</span></div>
      <div class="stat"><b>~{wave_rows[-1][6]}</b><span>cards at v2.0</span></div>
      <div class="stat"><b>~{round((wave_rows[-1][6] - 46) / 35)}</b><span>batches of ~35 cards</span></div>
    </div>
  </section>

  <section>
    <h2>Release waves</h2>
    <p class="dim">Each wave is a normal Google Play update; users keep their progress. Sizes include the voice in Opus and the Quran recitations.</p>
    <div class="scroll"><table class="grid"><thead><tr><th>Version</th><th>Content</th><th class="n">Books</th><th class="n">New cards</th><th class="n">Total</th><th class="n">App size</th><th>Delivery</th></tr></thead>
    <tbody>{wave_html}</tbody></table></div>
  </section>

  <section>
    <h2>Keeping the app light</h2>
    <ul>
      <li><b>Voice in Opus.</b> Measured on today's 324 clips: <b>8.6 MB in Opus 24 kbps against 16.4 MB in MP3</b>. New batches are voiced straight to Opus (Android plays it natively); today's clips stay as they are until then.</li>
      <li><b>Recitations stay as published</b> (64 kbps, about 0.15 MB per verse), because their licence allows redistribution only at the published bitrate. Verses shared between cards are stored once.</li>
      <li><b>Google Play's size limits.</b> The app's main download can be up to <b>500 MB</b> (compressed). Above 200 MB, people on mobile data see a notice suggesting Wi-Fi, which they can skip. The plan passes 200 MB {cross_text} and ends near {wave_rows[-1][7]:.0f} MB, so all 78 books fit in one download. Asset packs (Play Asset Delivery) stay in reserve: needed only above 500 MB, or to keep the first download under 200 MB.</li>
      <li><b>Texts are small.</b> Today's 46 cards take 242 KB; 700 cards come to about 4 MB.</li>
    </ul>
  </section>

  <section>
    <h2>First step, done today</h2>
    <ul>
      <li>The full list of 78 books is rebuilt and mapped to categories, release waves and card estimates (below).</li>
      <li>The download workflow now finds each book's file on its library page. The 18 remaining Aqeedah books for <b>v1.1</b> are downloaded (18 of 18, about 69 MB) and stored with the other sources.</li>
      <li>Next session: write batch 3 (Aqeedah), voiced straight to Opus, with the same checks as before: text against the book, tashkeel, pronunciation, animation.</li>
    </ul>
  </section>

  <section>
    <h2>Book map</h2>
    <p class="dim">Card counts are estimates: only the key ideas of each book become cards, and ideas found in several books are merged into one card that cites them all.</p>
    {''.join(map_html)}
  </section>
</div></main>
<footer>Re-Iman · elh24</footer>
"""
open(os.path.join(HERE, "roadmap.html"), "w", encoding="utf-8").write(page)
print("waves:", [(w[0], w[5], w[6], round(w[7])) for w in wave_rows])
