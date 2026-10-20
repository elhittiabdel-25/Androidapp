"""Builds the Re-Iman promo as two HyperFrames projects: land/ (1920x1080) and port/ (1080x1920).

One template, two layouts. Timing lives in T below and is shared by both cuts.
"""
import os, random, shutil

HERE = os.path.dirname(os.path.abspath(__file__))
ASSETS = os.path.join(HERE, "build", "assets")

# ---- timing (seconds, root timeline) -------------------------------------------------------
SEG = {  # video clips: start, length (lengths are the pre-cut files in assets/)
    "A": (7.6, 15.733),
    "B": (23.1, 7.8),
    "C": (30.65, 6.033),
    "D": (36.45, 7.9),
}
END_AT = 44.35          # phone leaves, end card builds
TOTAL = 50.8
FLICKS = [0.3, 1.05, 1.8, 2.5]   # short thumb flicks
LAST_FLICK = (3.2, 4.2)          # long flick that is stopped dead
IMPACT = 4.2

SFX = [  # id, file, start, volume
    ("sx-fl1", "whoosh-short", 0.28, 0.16),
    ("sx-fl2", "whoosh-short", 1.03, 0.16),
    ("sx-fl3", "whoosh-short", 1.78, 0.16),
    ("sx-fl4", "whoosh-short", 2.48, 0.16),
    ("sx-fl5", "whoosh-short", 3.55, 0.2),
    ("sx-stop", "impact-bass-1", IMPACT, 0.2),
    ("sx-logo", "sparkle", 5.0, 0.22),
    ("sx-toA", "whoosh-short", 7.5, 0.26),
    ("sx-toB", "whoosh-short", 23.05, 0.24),
    ("sx-toC", "whoosh-short", 30.6, 0.24),
    ("sx-tap", "click-soft", 30.65 + 3.25, 0.5),
    ("sx-right", "chime", 30.65 + 3.42, 0.4),
    ("sx-toD", "whoosh-short", 36.4, 0.24),
    ("sx-count", "pop", 38.25, 0.2),
    ("sx-toEnd", "whoosh-short", END_AT - 0.05, 0.26),
    ("sx-end", "sparkle", 45.25, 0.22),
    ("sx-word", "impact-bass-2", 45.95, 0.12),
    ("sx-p1", "pop", 47.05, 0.16),
    ("sx-p2", "pop", 47.25, 0.16),
    ("sx-p3", "pop", 47.45, 0.16),
]
SFX_LEN = {"whoosh-short": 0.574, "impact-bass-1": 2.115, "impact-bass-2": 2.592, "sparkle": 1.802,
           "click-soft": 0.365, "chime": 2.544, "pop": 0.72}

# ---- copy (Arabic line(s) + English translation under it) ---------------------------------
COPY = {
    "hook": (["كم دقيقة ضاعت اليوم", "في التمرير؟"], "How many minutes went to scrolling today?"),
    "swap": (["نفس الحركة…", "لكن ما يبقى هذه المرة", "هو العلم بالله"], "Same gesture. This time, what stays is knowledge of Allah."),
    "a1": (["كل جملة تُقرأ بصوت", "وتُظلَّل وأنت تتابع"], "Every sentence is read aloud and highlighted as you follow."),
    "a2": (["ورسمٌ يتحرك مع الصوت", "يشرح الفكرة"], "A drawing moves with the voice to explain the idea."),
    "b": (["أركان الإيمان تظهر", "نجمةً بعد نجمة"], "The pillars of faith appear, star by star."),
    "c": (["ثم اختبر فهمك"], "Then test what you understood."),
}
# when each panel line comes in / goes out
SAY = {
    "hook": (0.35, IMPACT),
    "swap": (5.15, 8.3),
    "a1": (8.7, 15.0),
    "a2": (15.4, 22.8),
    "b": (23.5, 30.3),
    "c": (31.0, 35.9),
    "d": (36.85, END_AT - 0.1),
}

LAYOUTS = {
    "land": dict(W=1920, H=1080, sw=456, sh=914, bezel=14, prad=66, px=358, py=69,
                 panel=(960, 120, 780, 840), align="right", ar=64, en=32, num=176, brand=(None, 62, 180),
                 logo=270, word=150, tag=68, pill_ar=40, pill_en=22, bg="72% 36%", stars=110),
    "port": dict(W=1080, H=1920, sw=600, sh=1203, bezel=16, prad=84, px=224, py=604,
                 panel=(70, 150, 940, 430), align="center", ar=60, en=31, num=136, brand=("c", 58, None),
                 logo=320, word=150, tag=72, pill_ar=40, pill_en=22, bg="50% 30%", stars=150),
}

HUES = [("#7d4a92", "#d8706e"), ("#24788a", "#76c7aa"), ("#c47f34", "#e6c36a"), ("#4b5bb0", "#9a72d6"),
        ("#b0436a", "#e98d5b"), ("#3b8a5a", "#a6cb5e"), ("#8a3b3b", "#e1a15b"), ("#2b5f9e", "#62c2d8"),
        ("#9c4fa0", "#f08cb3"), ("#5a7f2f", "#d6c35a"), ("#a3483c", "#f1b06a"), ("#3f4fa0", "#5fd0c0"),
        ("#7b3f7f", "#e57a6a")]


def logo_svg(prefix, size, extra_cls=""):
    # Same geometry as the app icon (docs/store/icon512.html), without its blue tile.
    return f'''<svg id="{prefix}" class="logo {extra_cls}" viewBox="24 20 60 60" width="{size}" height="{size}" xmlns="http://www.w3.org/2000/svg">
  <path id="{prefix}-arc" d="M73.745,42.6 A22.8,22.8 0 1,1 54,31.2" stroke="#E5B54B" stroke-width="4.32" stroke-linecap="round" fill="none"/>
  <g id="{prefix}-head-w"><path id="{prefix}-head" d="M61.2,31.2 L52.8,24.96 L52.8,37.44 Z" fill="#E5B54B"/></g>
  <g id="{prefix}-star"><path d="M45.09,45.09 L62.91,45.09 L62.91,62.91 L45.09,62.91 Z" fill="#fff"/><path d="M54,41.4 L66.6,54 L54,66.6 L41.4,54 Z" fill="#fff"/></g>
  <circle id="{prefix}-dot" cx="54" cy="54" r="3.12" fill="#E5B54B"/>
</svg>'''


def icon_tile(size):
    return f'''<svg class="tile" viewBox="12 12 84 84" width="{size}" height="{size}" xmlns="http://www.w3.org/2000/svg">
  <defs><linearGradient id="tileg" x1="0" y1="0" x2="108" y2="108" gradientUnits="userSpaceOnUse"><stop offset="0" stop-color="#2F56BD"/><stop offset="1" stop-color="#172E6E"/></linearGradient></defs>
  <rect x="12" y="12" width="84" height="84" rx="18" fill="url(#tileg)"/>
  <path d="M73.745,42.6 A22.8,22.8 0 1,1 54,31.2" stroke="#E5B54B" stroke-width="4.32" stroke-linecap="round" fill="none"/>
  <path d="M61.2,31.2 L52.8,24.96 L52.8,37.44 Z" fill="#E5B54B"/>
  <path d="M45.09,45.09 L62.91,45.09 L62.91,62.91 L45.09,62.91 Z" fill="#fff"/><path d="M54,41.4 L66.6,54 L54,66.6 L41.4,54 Z" fill="#fff"/>
  <circle cx="54" cy="54" r="3.12" fill="#E5B54B"/>
</svg>'''


def feed_html(k):
    """A generic, brand-free social feed: avatar, name bars, colourful media, reactions."""
    r = lambda v: f"{v * k:.1f}px"
    out = []
    for i, (a, b) in enumerate(HUES):
        text_only = i % 4 == 2
        play = i % 3 == 1
        media = "" if text_only else (
            f'<div class="media" style="height:{r(250)};background:linear-gradient(135deg,{a},{b})">'
            + (f'<div class="play" style="width:{r(56)};height:{r(56)}"></div>' if play else "")
            + f'<div class="blob" style="width:{r(120)};height:{r(120)};left:{r(40 + 37 * (i % 5))};top:{r(30 + 23 * (i % 4))}"></div></div>')
        body = (f'<div class="bar" style="width:92%;height:{r(14)}"></div><div class="bar" style="width:78%;height:{r(14)}"></div>'
                f'<div class="bar" style="width:55%;height:{r(14)}"></div>') if text_only else \
               f'<div class="bar" style="width:{60 + 7 * (i % 4)}%;height:{r(12)}"></div>'
        out.append(f'''<div class="post" style="padding:{r(14)};border-radius:{r(22)};margin-bottom:{r(14)};gap:{r(12)}">
  <div class="who" style="gap:{r(12)}"><div class="av" style="width:{r(44)};height:{r(44)};background:linear-gradient(135deg,{b},{a})"></div>
    <div class="names" style="gap:{r(7)}"><div class="bar" style="width:{r(120 + 9 * (i % 5))};height:{r(12)}"></div><div class="bar dim" style="width:{r(80)};height:{r(10)}"></div></div></div>
  {body}{media}
  <div class="acts" style="gap:{r(22)};height:{r(30)}"><div class="heart" style="width:{r(26)};height:{r(26)}"></div><div class="ic" style="width:{r(24)};height:{r(24)}"></div><div class="ic" style="width:{r(24)};height:{r(24)}"></div><div class="bar dim" style="width:{r(70)};height:{r(10)};margin-inline-start:auto"></div></div>
</div>''')
    return "\n".join(out)


def stars_svg(W, H, n, seed):
    rnd = random.Random(seed)
    dots = []
    for _ in range(n):
        x, y = rnd.uniform(0, W), rnd.uniform(0, H)
        dots.append(f'<circle cx="{x:.0f}" cy="{y:.0f}" r="{rnd.uniform(0.8, 2.3):.1f}" fill="#fff" opacity="{rnd.uniform(0.12, 0.55):.2f}"/>')
    return f'<svg class="stars" viewBox="0 0 {W} {H}" width="{W}" height="{H}" xmlns="http://www.w3.org/2000/svg">{"".join(dots)}</svg>'


def say_block(key, k_ar, k_en):
    lines, en = COPY[key]
    ar = "".join(f'<p class="ar l{i}">{t}</p>' for i, t in enumerate(lines))
    return f'<div class="say" id="say-{key}">{ar}<p class="en">{en}</p></div>'


def build(name, L):
    W, H, sw, sh, bz = L["W"], L["H"], L["sw"], L["sh"], L["bezel"]
    k = sw / 456
    pw, ph = sw + 2 * bz, sh + 2 * bz
    plx, ply, plw, plh = L["panel"]
    align = L["align"]
    bx, by, br = L["brand"]
    brand_pos = f"left:0;right:0;top:{by}px;justify-content:center" if bx == "c" else f"right:{br}px;top:{by}px"

    videos = []
    for s, (st, ln) in SEG.items():
        audio = 'data-has-audio="true" data-volume="1"' if s == "A" else "muted"
        videos.append(f'''<div class="vw" id="vw{s}"><video id="v{s}" class="clip" src="assets/seg{s}.mp4" data-start="{st}" data-duration="{ln}" data-track-index="{1 if s in 'AC' else 2}" {audio} playsinline></video></div>''')

    audios = "\n".join(
        f'<audio id="{i}" src="assets/sfx/{f}.mp3" data-start="{t:.2f}" data-duration="{SFX_LEN[f]}" data-track-index="{5 + n % 3}" data-volume="{v}"></audio>'
        for n, (i, f, t, v) in enumerate(SFX))

    says = "\n".join(say_block(key, L["ar"], L["en"]) for key in ["hook", "swap", "a1", "a2", "b", "c"])
    say_d = f'''<div class="say" id="say-d">
  <p class="ar big"><span class="num" id="n81">0</span> <span>بطاقة</span></p>
  <p class="ar l1">من <span class="num sm" id="n20">0</span> كتابًا في العقيدة</p>
  <p class="ar l2">بأدلتها ومصادرها</p>
  <p class="en">81 cards from 20 books of aqeedah, with their proofs and sources.</p>
</div>'''

    pills = [("مجاني", "Free"), ("بلا إعلانات", "No ads"), ("يعمل دون إنترنت", "Works offline")]
    pills_html = "".join(f'<div class="pill" id="pill{i}"><span class="pa">{a}</span><span class="pe">{e}</span></div>' for i, (a, e) in enumerate(pills))

    html = f'''<!doctype html>
<html lang="ar">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width={W}, height={H}" />
<title>Re-Iman promo ({name})</title>
<script src="assets/gsap.min.js"></script>
<style>
@font-face {{ font-family: "Plex"; src: url("assets/fonts/plex_regular.ttf") format("truetype"); font-weight: 400; }}
@font-face {{ font-family: "Plex"; src: url("assets/fonts/plex_semibold.ttf") format("truetype"); font-weight: 600; }}
html, body {{ margin: 0; background: #101217; }}
#root {{ position: relative; width: 100%; height: 100%; overflow: hidden; color: #fff; font-family: "Plex", sans-serif; }}
.layer {{ position: absolute; inset: 0; }}
#bg-grey {{ background: radial-gradient(circle at {L["bg"]}, #2a2d35 0%, #191b21 45%, #0e0f13 100%); }}
#bg-blue {{ opacity: 0; background: radial-gradient(circle at {L["bg"]}, #3560cc 0%, #23439A 40%, #132863 100%); }}
.stars {{ position: absolute; inset: 0; display: block; }}

#phone-float {{ position: absolute; left: {L["px"]}px; top: {L["py"]}px; width: {pw}px; height: {ph}px; }}
#phone {{ width: 100%; height: 100%; box-sizing: border-box; padding: {bz}px; border-radius: {L["prad"]}px;
  background: #06080c; box-shadow: 0 {int(40 * k)}px {int(90 * k)}px rgba(0,0,0,.5), inset 0 0 0 {max(2, int(2 * k))}px #2b3346; }}
#screen {{ position: relative; width: 100%; height: 100%; overflow: hidden; border-radius: {L["prad"] - bz}px; background: #0D121A; }}
.vw {{ position: absolute; inset: 0; opacity: 0; }}
.vw video {{ position: absolute; inset: 0; width: 100%; height: 100%; object-fit: cover; display: block; }}

#feed-wrap {{ position: absolute; inset: 0; background: #15171c; }}
#feed {{ position: absolute; left: {14 * k:.1f}px; right: {14 * k:.1f}px; top: {70 * k:.1f}px; }}
#topbar {{ position: absolute; left: 0; right: 0; top: 0; height: {58 * k:.1f}px; background: #15171c; display: flex; align-items: center;
  justify-content: space-between; padding: 0 {18 * k:.1f}px; box-sizing: border-box; border-bottom: 1px solid #262931; }}
#topbar .bar {{ width: {110 * k:.1f}px; height: {16 * k:.1f}px; }}
#topbar .ic {{ width: {26 * k:.1f}px; height: {26 * k:.1f}px; }}
.post {{ background: #1f2228; display: flex; flex-direction: column; }}
.who {{ display: flex; align-items: center; }}
.names {{ display: flex; flex-direction: column; }}
.av {{ border-radius: 50%; flex: none; }}
.bar {{ background: #3a3e48; border-radius: 99px; }}
.bar.dim {{ background: #2e3139; }}
.media {{ position: relative; border-radius: {14 * k:.1f}px; overflow: hidden; }}
.blob {{ position: absolute; border-radius: 50%; background: rgba(255,255,255,.18); }}
.play {{ position: absolute; inset: 0; margin: auto; border-radius: 50%; background: rgba(0,0,0,.35); }}
.acts {{ display: flex; align-items: center; }}
.heart {{ background: #e0566b; border-radius: 30%; }}
.ic {{ border: {max(2, int(2.5 * k))}px solid #4a4f5b; border-radius: 50%; box-sizing: border-box; }}
#touch {{ position: absolute; left: 58%; top: 68%; width: {64 * k:.0f}px; height: {64 * k:.0f}px; border-radius: 50%;
  background: rgba(255,255,255,.55); box-shadow: 0 0 0 {6 * k:.0f}px rgba(255,255,255,.18); opacity: 0; }}
#dim {{ position: absolute; inset: 0; background: #070910; opacity: 0; }}
#mark-wrap {{ position: absolute; inset: 0; display: flex; align-items: center; justify-content: center; }}
.logo {{ display: block; overflow: visible; }}

#brand {{ position: absolute; display: flex; align-items: center; gap: 16px; opacity: 0; {brand_pos}; }}
#brand .tile {{ display: block; }}
#brand .bn {{ font: 600 40px/1 "Plex", sans-serif; letter-spacing: -0.5px; direction: ltr; }}
.re {{ color: #E5B54B; }}

#panel {{ position: absolute; left: {plx}px; top: {ply}px; width: {plw}px; height: {plh}px; }}
.say {{ position: absolute; inset: 0; display: flex; flex-direction: column; justify-content: center;
  align-items: {"flex-end" if align == "right" else "center"}; gap: 14px; }}
.say p {{ margin: 0; opacity: 0; }}
.ar {{ text-wrap: balance; direction: rtl; text-align: {align}; font: 600 {L["ar"]}px/1.45 "Plex", sans-serif; color: #F7F3EA; max-width: {plw}px; }}
.en {{ text-wrap: balance; direction: ltr; text-align: {align}; font: 400 {L["en"]}px/1.4 "Plex", sans-serif; color: #C8D2EC; max-width: {plw}px; margin-top: 12px !important; }}
#say-hook .ar {{ color: #E9EBEF; font-size: {L["ar"] + 14}px; }}
#say-hook .en {{ color: #A9AEB9; }}
.ar.big {{ font-size: {L["ar"] + 10}px; display: flex; align-items: baseline; gap: 22px; direction: rtl; }}
.num {{ display: inline-block; color: #E5B54B; font: 600 {L["num"]}px/1 "Plex", sans-serif; font-variant-numeric: tabular-nums; min-width: {int(L["num"] * 1.05)}px; text-align: center; }}
.num.sm {{ font-size: {L["ar"] + 6}px; min-width: {int((L["ar"] + 6) * 1.1)}px; }}

#end {{ position: absolute; inset: 0; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 18px; }}
#end .word {{ font: 600 {L["word"]}px/1 "Plex", sans-serif; letter-spacing: -2px; direction: ltr; opacity: 0; margin-top: 10px; }}
#end .tag {{ font: 600 {L["tag"]}px/1.5 "Plex", sans-serif; direction: rtl; color: #F7F3EA; opacity: 0; }}
#pills {{ display: flex; flex-wrap: wrap; justify-content: center; gap: 18px; margin-top: 18px; direction: rtl; max-width: {W - 80}px; }}
.pill {{ display: flex; flex-direction: column; align-items: center; gap: 6px; padding: 14px 30px; border-radius: 999px;
  background: rgba(255,255,255,.1); border: 1.5px solid rgba(255,255,255,.28); opacity: 0; }}
.pa {{ font: 600 {L["pill_ar"]}px/1.3 "Plex", sans-serif; direction: rtl; }}
.pe {{ font: 400 {L["pill_en"]}px/1.2 "Plex", sans-serif; color: #C8D2EC; direction: ltr; }}
</style>
</head>
<body>
<div id="root" data-composition-id="reiman-promo" data-start="0" data-width="{W}" data-height="{H}" data-duration="{TOTAL}">
  <div class="layer" id="bg-grey"></div>
  <div class="layer" id="bg-blue">{stars_svg(W, H, L["stars"], 7)}</div>

  <div id="phone-float"><div id="phone"><div id="screen">
    <div id="feed-wrap">
      <div id="feed">
{feed_html(k)}
      </div>
      <div id="topbar"><div class="bar"></div><div class="ic"></div></div>
      <div id="touch"></div>
    </div>
    <div id="dim"></div>
    {"".join(videos)}
    <div id="mark-wrap">{logo_svg("mk", int(290 * k))}</div>
  </div></div></div>

  <div id="brand">{icon_tile(72)}<span class="bn"><span class="re">Re</span>-Iman</span></div>

  <div id="panel">
{says}
{say_d}
  </div>

  <div id="end">
    {logo_svg("el", L["logo"])}
    <div class="word" id="end-word"><span class="re">Re</span>-Iman</div>
    <div class="tag" id="end-tag">جَدِّدْ إيمانَكَ</div>
    <div id="pills">{pills_html}</div>
  </div>

{audios}
</div>

<script>
(function () {{
  const K = {k:.4f};
  const SEG = {{ {", ".join(f"{s}: [{st}, {ln}]" for s, (st, ln) in SEG.items())} }};
  const SAY = {{ {", ".join(f'"{key}": [{a}, {b}]' for key, (a, b) in SAY.items())} }};
  const tl = gsap.timeline({{ paused: true }});

  // ---- logo stroke setup (measured, not guessed) ----
  ["mk", "el"].forEach((p) => {{
    const arc = document.getElementById(p + "-arc");
    const len = arc.getTotalLength();
    arc.style.strokeDasharray = len;
    arc.style.strokeDashoffset = len;
    arc.dataset.len = len;
  }});
  function logoIn(p, t, sc) {{
    const arc = document.getElementById(p + "-arc");
    tl.fromTo("#" + p, {{ scale: sc, opacity: 0 }}, {{ scale: 1, opacity: 1, duration: 0.5, ease: "power3.out" }}, t);
    tl.fromTo(arc, {{ strokeDashoffset: +arc.dataset.len }}, {{ strokeDashoffset: 0, duration: 0.95, ease: "power2.inOut" }}, t + 0.05);
    tl.fromTo("#" + p + "-star", {{ scale: 0, rotation: -90, svgOrigin: "54 54" }}, {{ scale: 1, rotation: 0, duration: 0.7, ease: "back.out(1.6)" }}, t + 0.45);
    tl.fromTo("#" + p + "-head", {{ scale: 0, svgOrigin: "52.8 31.2" }}, {{ scale: 1, duration: 0.35, ease: "back.out(2.2)" }}, t + 0.95);
    tl.fromTo("#" + p + "-dot", {{ scale: 0, svgOrigin: "54 54" }}, {{ scale: 1, duration: 0.4, ease: "back.out(3)" }}, t + 1.05);
  }}
  function sayIn(key, t, gap) {{
    const lines = document.querySelectorAll("#say-" + key + " p");
    lines.forEach((el, i) => {{
      tl.fromTo(el, {{ opacity: 0, y: 26 }}, {{ opacity: 1, y: 0, duration: 0.55, ease: "power3.out" }}, t + i * (gap || 0.32));
    }});
  }}
  function sayOut(key, t) {{
    tl.to("#say-" + key + " p", {{ opacity: 0, y: -18, duration: 0.32, ease: "power2.in", stagger: 0.04 }}, t);
  }}

  // ---- 0–4.2s  hook: the endless feed ----
  tl.fromTo("#phone", {{ scale: 0.965 }}, {{ scale: 1, duration: 0.6, ease: "power2.out" }}, 0);
  let y = 0;
  {FLICKS}.forEach((t, i) => {{
    y -= 560 * K;
    tl.to("#feed", {{ y: y, duration: 0.6, ease: "power3.out" }}, t);
    tl.fromTo("#touch", {{ opacity: 0, y: 0, scale: 0.8 }}, {{ opacity: 1, scale: 1, duration: 0.08, ease: "none", immediateRender: i === 0 }}, t - 0.08);
    tl.to("#touch", {{ y: -250 * K, opacity: 0, duration: 0.32, ease: "power2.out" }}, t);
  }});
  tl.fromTo("#touch", {{ opacity: 0, y: 0, scale: 0.8 }}, {{ opacity: 1, scale: 1, duration: 0.08, ease: "none", immediateRender: false }}, {LAST_FLICK[0]} - 0.08);
  tl.to("#touch", {{ y: -300 * K, opacity: 0, duration: 0.4, ease: "power2.out" }}, {LAST_FLICK[0]});
  tl.to("#feed", {{ y: y - 1300 * K, duration: {LAST_FLICK[1] - LAST_FLICK[0]}, ease: "power1.in" }}, {LAST_FLICK[0]});
  // hook line: two beats, then the English under it
  sayIn("hook", SAY.hook[0], 0.6);
  tl.to("#say-hook p", {{ opacity: 0, y: -14, duration: 0.25, ease: "power2.in" }}, SAY.hook[1]);

  // ---- 4.2–7.6s  the stop and the swap ----
  tl.to("#phone", {{ x: 9 * K, duration: 0.045, ease: "none", yoyo: true, repeat: 5 }}, {IMPACT});
  tl.to("#dim", {{ opacity: 0.72, duration: 0.3, ease: "power2.out" }}, {IMPACT});
  tl.to("#bg-blue", {{ opacity: 1, duration: 1.3, ease: "power1.inOut" }}, {IMPACT} + 0.2);
  logoIn("mk", {IMPACT} + 0.35, 0.7);
  sayIn("swap", SAY.swap[0], 0.5);
  sayOut("swap", SAY.swap[1]);
  tl.to("#mk", {{ scale: 0.82, opacity: 0, duration: 0.4, ease: "power2.in" }}, 7.25);
  tl.to(["#feed-wrap", "#dim"], {{ opacity: 0, duration: 0.3, ease: "none" }}, 7.65);

  // ---- the app itself: clips enter over the previous one ----
  Object.keys(SEG).forEach((s) => {{
    tl.fromTo("#vw" + s, {{ opacity: 0, y: 30 * K }}, {{ opacity: 1, y: 0, duration: 0.3, ease: "power2.out" }}, SEG[s][0]);
  }});
  tl.to("#brand", {{ opacity: 1, duration: 0.5, ease: "power2.out" }}, 7.9);
  // phone breathes slowly while the app plays (16 half-cycles end back at rest)
  tl.fromTo("#phone-float", {{ y: 0 }}, {{ y: -8 * K, duration: 2.3, ease: "sine.inOut", yoyo: true, repeat: 15 }}, SEG.A[0]);

  ["a1", "a2", "b", "c"].forEach((key) => {{ sayIn(key, SAY[key][0]); sayOut(key, SAY[key][1]); }});

  // ---- numbers ----
  sayIn("d", SAY.d[0]);
  const c81 = {{ v: 0 }}, c20 = {{ v: 0 }};
  const e81 = document.getElementById("n81"), e20 = document.getElementById("n20");
  tl.to(c81, {{ v: 81, duration: 1.25, ease: "power2.out", onUpdate: () => {{ e81.textContent = Math.round(c81.v); }} }}, SAY.d[0] + 0.1);
  tl.fromTo("#n81", {{ scale: 0.6 }}, {{ scale: 1, duration: 1.25, ease: "power2.out" }}, SAY.d[0] + 0.1);
  tl.to(c20, {{ v: 20, duration: 1.0, ease: "power2.out", onUpdate: () => {{ e20.textContent = Math.round(c20.v); }} }}, SAY.d[0] + 0.45);
  sayOut("d", SAY.d[1]);

  // ---- end card ----
  tl.to("#phone", {{ y: 160 * K, opacity: 0, duration: 0.55, ease: "power2.in" }}, {END_AT});
  tl.to("#brand", {{ opacity: 0, duration: 0.3, ease: "none" }}, {END_AT});
  logoIn("el", {END_AT} + 0.45, 0.6);
  tl.fromTo("#end-word", {{ opacity: 0, y: 30 }}, {{ opacity: 1, y: 0, duration: 0.6, ease: "power3.out" }}, 45.9);
  tl.fromTo("#end-tag", {{ opacity: 0, y: 20 }}, {{ opacity: 1, y: 0, duration: 0.6, ease: "power3.out" }}, 46.35);
  [0, 1, 2].forEach((i) => {{
    tl.fromTo("#pill" + i, {{ opacity: 0, scale: 0.8 }}, {{ opacity: 1, scale: 1, duration: 0.45, ease: "back.out(2)" }}, 47.0 + i * 0.2);
  }});

  window.__timelines["reiman-promo"] = tl;
}})();
</script>
</body>
</html>
'''
    out = os.path.join(HERE, "out", name)
    os.makedirs(out, exist_ok=True)
    with open(os.path.join(out, "index.html"), "w", encoding="utf-8") as f:
        f.write(html)
    dst = os.path.join(out, "assets")
    if os.path.exists(dst):
        shutil.rmtree(dst)
    shutil.copytree(ASSETS, dst)
    return out


if __name__ == "__main__":
    for n, L in LAYOUTS.items():
        print(build(n, L))
