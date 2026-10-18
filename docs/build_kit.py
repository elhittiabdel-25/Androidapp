"""Generate docs/launch-kit.html (the Google Play launch kit page) from the listing texts in store/listing.py."""
import html, json, os, sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(HERE, "store"))
from listing import LISTING, NOTES  # noqa: E402

PRIVACY = "https://claude.ai/artifact/Q15UeL4ewuizXCnQmK3p58"
fid = 0


def field(label, value, lang="", rows=False):
    """A labelled value with a copy button."""
    global fid
    fid += 1
    d = ' dir="rtl"' if lang == "ar" else ""
    cls = "val long" if rows else "val"
    return (f'<div class="field"><div class="flabel">{label}</div>'
            f'<div class="frow"><div class="{cls}" id="f{fid}" lang="{lang or "en"}"{d}>{html.escape(value)}</div>'
            f'<button class="copy" data-for="f{fid}" type="button">Copy</button></div></div>')


def step(n, title, fr, body):
    return (f'<section class="step" id="s{n}"><header class="shead"><span class="num">{n}</span>'
            f'<div class="stitle"><h2>{title}</h2>{f"<div class=fr>{fr}</div>" if fr else ""}</div>'
            f'<label class="done"><input type="checkbox" id="done{n}" data-step="{n}"> Done</label></header>'
            f'<div class="sbody">{body}</div></section>')


L = LISTING
TESTER = {
    "ar": "السلام عليكم، أطلقتُ تطبيقًا مجانيًا اسمه Re-Iman لتعلّم العقيدة ببطاقات قصيرة مسموعة. يشترط Google Play أن يجرّبه 12 شخصًا لمدة 14 يومًا قبل نشره للجميع، فهل تساعدني؟\n1) أرسل لي بريد Gmail المرتبط بهاتفك Android.\n2) افتح رابط الاختبار الذي سأرسله لك واضغط «أصبح مختبِرًا».\n3) ثبّت التطبيق من Google Play، وأبقِه مثبّتًا 14 يومًا، وافتحه من حين لآخر.\nوإن لاحظت خطأ أو اقتراحًا فأخبرني. جزاك الله خيرًا!",
    "fr": "Salam alaykoum ! J'ai créé une appli gratuite, Re-Iman, pour apprendre l'aqîda avec de courtes fiches audio. Google Play exige que 12 personnes la testent pendant 14 jours avant de la publier pour tous. Tu peux m'aider ?\n1) Envoie-moi l'adresse Gmail de ton téléphone Android.\n2) Ouvre le lien de test que je t'enverrai et appuie sur « Devenir testeur ».\n3) Installe l'appli depuis Google Play, garde-la installée 14 jours et ouvre-la de temps en temps.\nSi tu vois une erreur ou as une idée, dis-le-moi. Barak Allahou fik !",
    "en": "Assalamu alaykum! I've built a free app, Re-Iman, for learning Islamic belief through short audio cards. Google Play requires 12 people to test it for 14 days before it can be published. Could you help?\n1) Send me the Gmail address you use on your Android phone.\n2) Open the test link I'll send you and tap \"Become a tester\".\n3) Install the app from Google Play, keep it installed for 14 days, and open it now and then.\nIf you spot a mistake or have an idea, tell me. JazakAllahu khayran!",
}
PROD = [
    ("How easy was it to recruit testers for your app?",
     "Fairly easy. Testers are family members, friends and people from my local community, invited personally by email and messaging apps."),
    ("Describe the engagement you received from testers during your closed test",
     "Testers used the app in short daily sessions: reading cards, listening to the voice and the Quran recitations, opening the deeper layer, taking the self-tests, browsing categories and saving cards. [Add what you observed: how often they opened it, which parts they used most.]"),
    ("Summarise the feedback that you received from testers. Include how you collected the feedback.",
     "Feedback was collected through messages and email. [Fill in the main points, e.g. pronunciation of some words, text corrections, layout remarks.]"),
    ("Who is the intended audience of your app?",
     "Arabic-reading Muslims aged 13 and over who want to learn and review the foundations of Islamic belief in short daily sessions."),
    ("Describe how your app provides value to users",
     "Short cards drawn from classical books, fully vowelled, with Quran and hadith evidence and the book and page for every card. Each sentence is read aloud, verses play a real recitation, and a light animation follows the voice. It is free, has no ads, works offline and collects no data."),
    ("How many installs do you expect your app to have in its first year?",
     "0 to 10,000"),
    ("What changes did you make to your app based on what you learned during your closed test?",
     "[Fill in after testing, e.g. fixed the pronunciation of some words, corrected card texts, adjusted the layout on small screens.]"),
    ("How did you decide that your app is ready for production?",
     "All features worked on every tester's device, no crashes were reported, the card texts were reviewed against their sources, and the feedback from the closed test was addressed in the latest release."),
]

CONTENT_FORMS = [
    ("Privacy policy", "Règles de confidentialité", "Paste the privacy page link (step 2)."),
    ("App access", "Accès à l'application", "All functionality is available without any access restrictions."),
    ("Ads", "Annonces", "No, my app does not contain ads."),
    ("Advertising ID", "Identifiant publicitaire", "No, my app does not use advertising ID."),
    ("Content rating", "Classification du contenu", "Category: Reference, news or educational. Answer No to every question (violence, fear, sexuality, language, substances, humour, gambling, user interaction, location sharing, purchases). Expected result: everyone / 3+."),
    ("Target audience and content", "Public cible et contenu", "Age groups: 13–15, 16–17 and 18 and over. Could the store listing unintentionally appeal to children? No."),
    ("Data safety", "Sécurité des données", "Does your app collect or share any of the required user data types? No. The summary then reads: no data collected, no data shared."),
    ("News app", "Applications d'actualités", "No."),
    ("Government app", "Applications gouvernementales", "No."),
    ("Financial features", "Fonctionnalités financières", "My app doesn't provide any financial features."),
    ("Health apps", "Applications de santé", "My app does not have any health features."),
]

RIGHTS = [
    ("Book texts", "Islamic Content Encyclopedia (islamcontent.com)", "No licence or contact published; the site calls itself a free reference and offers a developer API. Cards summarise in our own words, with short quotes, the book, the page and a link.", "Reasonable, no formal permission", "amber"),
    ("Quran text", "Tanzil.net via AlQuran.cloud", "Free for non-commercial use, unaltered, with its diacritics and attribution.", "Clear, credited in the app", "green"),
    ("Recitation", "Mishary Alafasy via AlQuran.cloud", "Free non-commercial redistribution at the published bitrate (64 kbps, not re-encoded). Switched from everyayah.com in this build.", "Clear, credited in the app", "green"),
    ("Card voice", "Piper voice \"Kareem\"", "The voice collection is MIT-licensed; the voice's training data states no licence.", "Mostly clear, small uncertainty", "amber"),
    ("Fonts", "Amiri Quran, Noto Naskh Arabic, Reem Kufi, IBM Plex Sans Arabic", "SIL Open Font License 1.1; the licence texts ship inside the app.", "Clear", "green"),
    ("Signing key", "Upload key (new)", "The old key and its password were removed from the repository. The new upload key lives only in GitHub secrets and with you; Google holds the app signing key.", "Fixed", "green"),
]

body = []
body.append(step(1, "Add the upload key to GitHub", "", f"""
<p>Release builds are signed with a new <b>upload key</b> that never goes into the code. The four values are in the files I sent in the chat (<code>reiman-upload-secrets.txt</code> and <code>reiman-upload.p12.base64</code>).</p>
<ol class="sub">
<li>On GitHub, open the repository, then <b>Settings → Secrets and variables → Actions → New repository secret</b>.</li>
<li>Add these four secrets, one at a time:</li>
</ol>
<table class="grid"><thead><tr><th>Name</th><th>Value</th></tr></thead><tbody>
<tr><td><code>UPLOAD_KEYSTORE_BASE64</code></td><td>the whole content of <code>reiman-upload.p12.base64</code></td></tr>
<tr><td><code>UPLOAD_KEYSTORE_PASSWORD</code></td><td>the password in <code>reiman-upload-secrets.txt</code></td></tr>
<tr><td><code>UPLOAD_KEY_ALIAS</code></td><td><code>reiman-upload</code></td></tr>
<tr><td><code>UPLOAD_KEY_PASSWORD</code></td><td>the same password</td></tr>
</tbody></table>
<ol class="sub" start="3">
<li>Open <b>Actions → Build APK → Run workflow</b>. After about 5 minutes, the new release must say <i>“Signed with the upload key”</i>. That <code>.aab</code> is the file for Google Play.</li>
<li>Keep the key files in a safe place (a password manager or a private drive). If they're lost, Google can reset the upload key, but it takes a few days.</li>
</ol>"""))

body.append(step(2, "Make the privacy page public", "", f"""
<p>Google needs a privacy policy page that anyone can open. It's ready in Arabic and English. Open it, use <b>Share</b> to make it public, then use its link in Play Console.</p>
{field("Privacy policy link", PRIVACY)}
<p class="hint">When you have the dedicated contact address, send it to me and I'll add it to the page and to the app's About screen.</p>"""))

body.append(step(3, "Create the app", "Créer une application", f"""
<p class="hint">Your developer account must first finish identity verification. The Play Console dashboard lists any account tasks left, such as confirming a phone number or an Android device.</p>
<div class="two">
{field("App name", "Re-Iman")}
{field("Default language", "Arabic – ar (العربية)")}
{field("App or game", "App")}
{field("Free or paid", "Free (it can never become paid later)")}
</div>
<p>Accept the Developer Program Policies and the US export laws declarations, then create the app.</p>"""))

rows = "".join(f'<tr><td><b>{a}</b><div class="fr">{b}</div></td><td>{html.escape(c)}</td></tr>' for a, b, c in CONTENT_FORMS)
body.append(step(4, "Fill in the app content forms", "Contenu de l'application", f"""
<p>These answers match the app as built: no data collected, no ads, no internet access, no permissions shown to users.</p>
<div class="scroll"><table class="grid"><thead><tr><th>Form</th><th>Answer</th></tr></thead><tbody>{rows}</tbody></table></div>"""))

lang_blocks = []
for code, name in (("ar", "Arabic (default)"), ("en", "English translation"), ("fr", "French translation")):
    d = L[code]
    lang_blocks.append(f'<div class="lang"><h3>{name}</h3>'
                       + field("App name (max 30)", d["title"], code)
                       + field("Short description (max 80)", d["short"], code)
                       + field("Full description (max 4000)", d["full"], code, rows=True) + "</div>")
body.append(step(5, "Write the store listing", "Fiche Play Store principale", f"""
<p>Fill in the Arabic listing first. Then add English and French under <b>Manage translations</b> (Gérer les traductions).</p>
{''.join(lang_blocks)}
<h3>Graphics</h3>
<div class="gfx">
  <figure><img src="icon-512.png" alt="Re-Iman app icon, a gold renewal ring around a white eight-pointed star on blue" width="128" height="128"><figcaption>App icon, 512 × 512 PNG</figcaption></figure>
  <figure class="wide"><img src="feature-1024x500.png" alt="Feature graphic with the Re-Iman mark, name and tagline on blue" width="1024" height="500"><figcaption>Feature graphic, 1024 × 500 PNG</figcaption></figure>
</div>
<p>Both files are in the chat. <b>Phone screenshots</b> (2 to 8) must come from your phone. Take 4: a card with its animation, a card with “تعمّق” open, the الأقسام tab, and the self-test. Upload them as they are, or send them to me and I'll frame them with short captions.</p>
<div class="two">
{field("App category", "Education (Éducation)")}
{field("Contact email", "your new dedicated address")}
</div>"""))

notes = "".join(field(f"Release notes ({c})", f"<{t}>\n{NOTES[c]}\n</{t}>", "en") for c, t in (("ar", "ar"), ("en", "en-US"), ("fr", "fr-FR")))
body.append(step(6, "Run the closed test: 12 testers for 14 days", "Tests fermés", f"""
<p>Google requires new personal accounts to run a closed test with <b>at least 12 testers who stay opted in for 14 days in a row</b> before production. Someone who leaves and rejoins restarts their own count.</p>
<ol class="sub">
<li><b>Testing → Closed testing → Create track</b>. Under <i>Testers</i>, create an email list with the Gmail addresses of 12 or more people (aim for 15 to keep a margin).</li>
<li><b>Create release</b>. When asked about app signing, keep <b>Google-generated key</b> (Play App Signing). Upload the <code>.aab</code> from the release that says “Signed with the upload key”.</li>
<li>Release name: <code>1.0.0</code>. Paste the release notes below.</li>
<li><b>Review release → Start rollout</b>. Once Google approves it (often within a day or two), copy the <b>opt-in link</b> and send it to your testers.</li>
</ol>
{notes}
<h3>Message for testers</h3>
{field("Arabic", TESTER["ar"], "ar", rows=True)}
{field("French", TESTER["fr"], "fr", rows=True)}
{field("English", TESTER["en"], "en", rows=True)}"""))

prod = "".join(field(html.escape(q), a, "en", rows=len(a) > 120) for q, a in PROD)
body.append(step(7, "Apply for production (day 15 or later)", "Demander l'accès en production", f"""
<p>When 12 testers have been opted in for 14 days in a row, the dashboard shows <b>Apply for production</b>. Draft answers are below. Replace the parts in [brackets] with what really happened during the test. Google usually reviews within 7 days.</p>
{prod}"""))

body.append(step(8, "Publish, then update", "Production", """
<p>After approval: <b>Production → Create release</b>, add the same <code>.aab</code> (or a newer one), choose the countries, and roll out.</p>
<p>For every later update: I raise <code>versionName</code> (1.0.1, 1.1.0…) and push. The build produces a new <code>.aab</code> with a higher version code, and you upload it the same way. Testers and users then get the update from Google Play.</p>"""))

rights_rows = "".join(
    f'<tr><td><b>{a}</b><div class="fr">{html.escape(b)}</div></td><td>{html.escape(c)}</td><td><span class="chip {k}">{html.escape(s)}</span></td></tr>'
    for a, b, c, s, k in RIGHTS)

page = f"""<title>Re-Iman Play Launch Kit</title>
<meta name="claude:viewport-fit-sides" content="cover">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=IBM+Plex+Sans:wght@400;600&family=IBM+Plex+Sans+Arabic:wght@400;600&family=IBM+Plex+Mono&display=swap">
<style>
  /* Checklist column under a brand band; each step is a numbered card with copyable fields. */
  :root {{
    --bg: #eef1f7; --surface: #ffffff; --ink: #162033; --muted: #586377; --line: #d8dee9; --field: #f5f7fb;
    --accent: #23439a; --accent-soft: #e3e9f8; --gold: #9a6f18; --band: #1c3a86; --band-ink: #ffffff; --ring: #e5b54b;
    --green: #1f7a52; --green-soft: #e1f2e9; --amber: #9a5a0c; --amber-soft: #fbeedb;
    --sans: "IBM Plex Sans", "IBM Plex Sans Arabic", "Segoe UI", Tahoma, Arial, sans-serif;
    --mono: "IBM Plex Mono", ui-monospace, Consolas, monospace;
  }}
  @media (prefers-color-scheme: dark) {{
    :root:not([data-theme="light"]) {{
      --bg: #0d121b; --surface: #161e2b; --ink: #e5eaf3; --muted: #9aa5b8; --line: #2a3447; --field: #111925;
      --accent: #92aaf2; --accent-soft: #1e2a46; --gold: #d8ad57; --band: #13285f; --band-ink: #f1f4fb; --ring: #e5b54b;
      --green: #66c79d; --green-soft: #15302a; --amber: #f0a75a; --amber-soft: #33240f; color-scheme: dark;
    }}
  }}
  :root[data-theme="dark"] {{
    --bg: #0d121b; --surface: #161e2b; --ink: #e5eaf3; --muted: #9aa5b8; --line: #2a3447; --field: #111925;
    --accent: #92aaf2; --accent-soft: #1e2a46; --gold: #d8ad57; --band: #13285f; --band-ink: #f1f4fb; --ring: #e5b54b;
    --green: #66c79d; --green-soft: #15302a; --amber: #f0a75a; --amber-soft: #33240f; color-scheme: dark;
  }}
  body {{ background: var(--bg); color: var(--ink); font-family: var(--sans); font-size: 15px; line-height: 1.6; }}
  .wrap {{ max-width: 860px; margin: 0 auto; padding-left: max(16px, env(safe-area-inset-left, 0px)); padding-right: max(16px, env(safe-area-inset-right, 0px)); }}
  .band {{ background: var(--band); color: var(--band-ink); }}
  .band .wrap {{ display: flex; gap: 14px; align-items: center; padding-block: 20px; flex-wrap: wrap; }}
  .mark {{ width: 48px; height: 48px; flex: none; }}
  .mark .ring {{ fill: none; stroke: var(--ring); stroke-width: 4.3; stroke-linecap: round; }}
  .mark .head, .mark .dot {{ fill: var(--ring); }} .mark .star {{ fill: var(--band-ink); }}
  .word {{ font-weight: 600; font-size: 1.5rem; line-height: 1.1; }} .word span {{ color: var(--ring); }}
  .kicker {{ opacity: .88; font-size: .95rem; }}
  .meta {{ margin-inline-start: auto; display: flex; gap: 6px; flex-wrap: wrap; }}
  .pill {{ font-size: .8rem; border: 1px solid rgba(255,255,255,.35); border-radius: 999px; padding: 3px 10px; }}
  main .wrap {{ display: grid; gap: 18px; padding-block: 22px 40px; }}
  h2 {{ font-size: 1.15rem; margin: 0; line-height: 1.3; text-wrap: balance; }}
  h3 {{ font-size: 1rem; margin: 18px 0 8px; }}
  .card, .step {{ background: var(--surface); border: 1px solid var(--line); border-radius: 14px; min-width: 0; }}
  .card {{ padding: 18px; }}
  .cols {{ display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 18px; }}
  .cols ul {{ margin: 6px 0 0; padding-inline-start: 18px; }}
  .cols li {{ margin: 3px 0; }}
  .lbl {{ font-size: .78rem; letter-spacing: .06em; text-transform: uppercase; color: var(--muted); font-weight: 600; }}
  .shead {{ display: flex; gap: 12px; align-items: center; padding: 14px 18px; border-bottom: 1px solid var(--line); }}
  .num {{ width: 30px; height: 30px; border-radius: 50%; background: var(--accent); color: var(--surface); display: grid; place-items: center; font-weight: 600; flex: none; }}
  .stitle {{ flex: 1; min-width: 0; }}
  .fr {{ color: var(--muted); font-size: .85rem; }}
  .done {{ font-size: .85rem; color: var(--muted); display: flex; gap: 6px; align-items: center; cursor: pointer; white-space: nowrap; }}
  .step.is-done .num {{ background: var(--green); }}
  .step.is-done h2 {{ color: var(--green); }}
  .sbody {{ padding: 14px 18px 18px; }}
  .sbody > p {{ margin: 0 0 10px; }}
  .hint {{ color: var(--muted); font-size: .9rem; }}
  ol.sub {{ margin: 6px 0 12px; padding-inline-start: 20px; }} ol.sub li {{ margin: 4px 0; }}
  code {{ font-family: var(--mono); font-size: .86em; background: var(--field); border: 1px solid var(--line); border-radius: 5px; padding: 0 4px; }}
  .field {{ margin: 10px 0; min-width: 0; }}
  .flabel {{ font-size: .82rem; color: var(--muted); margin-bottom: 4px; }}
  .frow {{ display: flex; gap: 8px; align-items: flex-start; }}
  .val {{ flex: 1; min-width: 0; background: var(--field); border: 1px solid var(--line); border-radius: 8px; padding: 8px 10px; white-space: pre-wrap; overflow-wrap: anywhere; }}
  .val.long {{ max-height: 220px; overflow: auto; }}
  .val[lang="ar"] {{ font-family: "IBM Plex Sans Arabic", var(--sans); line-height: 1.8; }}
  .copy {{ font: inherit; font-size: .85rem; font-weight: 600; color: var(--accent); background: var(--accent-soft); border: 1px solid transparent; border-radius: 8px; padding: 7px 12px; cursor: pointer; flex: none; }}
  .copy:hover {{ border-color: var(--accent); }}
  .copy:focus-visible, .done input:focus-visible {{ outline: 2px solid var(--accent); outline-offset: 2px; }}
  .copy.ok {{ color: var(--green); background: var(--green-soft); }}
  .two {{ display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 0 14px; }}
  .lang {{ border-top: 1px solid var(--line); margin-top: 12px; }}
  .scroll {{ overflow-x: auto; }}
  table.grid {{ width: 100%; border-collapse: collapse; font-size: .92rem; }}
  .grid th, .grid td {{ text-align: start; vertical-align: top; padding: 8px 10px; border-bottom: 1px solid var(--line); }}
  .grid th {{ font-size: .78rem; text-transform: uppercase; letter-spacing: .05em; color: var(--muted); }}
  .gfx {{ display: flex; gap: 14px; align-items: flex-end; flex-wrap: wrap; }}
  .gfx figure {{ margin: 0; }} .gfx img {{ border-radius: 12px; border: 1px solid var(--line); display: block; height: auto; }}
  .gfx .wide {{ flex: 1; min-width: 220px; }} .gfx .wide img {{ width: 100%; max-width: 520px; }}
  figcaption {{ font-size: .8rem; color: var(--muted); margin-top: 4px; }}
  .chip {{ font-size: .8rem; font-weight: 600; border-radius: 999px; padding: 2px 10px; white-space: nowrap; }}
  .chip.green {{ color: var(--green); background: var(--green-soft); }} .chip.amber {{ color: var(--amber); background: var(--amber-soft); }}
  footer {{ color: var(--muted); font-size: .85rem; text-align: center; padding-block: 0 30px; }}
  @media (max-width: 520px) {{ .frow {{ flex-direction: column; }} .copy {{ align-self: flex-end; }} .shead {{ flex-wrap: wrap; }} }}
  @media (prefers-reduced-motion: no-preference) {{ .copy {{ transition: background .15s, color .15s; }} }}
</style>

<header class="band"><div class="wrap">
  <svg class="mark" viewBox="18 18 72 72" aria-hidden="true">
    <path class="ring" d="M73.745,42.6 A22.8,22.8 0 1,1 54,31.2"/><path class="head" d="M61.2,31.2 L52.8,24.96 L52.8,37.44 Z"/>
    <path class="star" d="M45.09,45.09 L62.91,45.09 L62.91,62.91 L45.09,62.91 Z"/><path class="star" d="M54,41.4 L66.6,54 L54,66.6 L41.4,54 Z"/><circle class="dot" cx="54" cy="54" r="3.12"/>
  </svg>
  <div><div class="word"><span>Re</span>-Iman</div><div class="kicker">Google Play launch kit · version 1.0.0</div></div>
  <div class="meta"><span class="pill">app.reiman</span><span class="pill">Android 16 target</span><span class="pill">developer elh24</span></div>
</div></header>

<main><div class="wrap">
  <section class="card">
    <div class="cols">
      <div><div class="lbl">Ready</div><ul>
        <li>App ID <code>app.reiman</code>, version 1.0.0, targets Android 16 (API 36)</li>
        <li>CI builds the Play bundle (<code>.aab</code>) and an APK</li>
        <li>New upload key, kept out of the code</li>
        <li>Icon 512 × 512 and feature graphic 1024 × 500</li>
        <li>Privacy page in Arabic and English</li>
        <li>Store texts in Arabic, English and French</li>
        <li>About screen with every source and licence credited</li>
      </ul></div>
      <div><div class="lbl">Your part</div><ul>
        <li>Add 4 secrets on GitHub and run the build (step 1)</li>
        <li>Make the privacy page public (step 2)</li>
        <li>Create the app and fill in the forms (steps 3 to 5)</li>
        <li>Recruit 12+ testers and keep them 14 days (step 6)</li>
        <li>Apply for production on day 15 (step 7)</li>
      </ul></div>
    </div>
  </section>
  {''.join(body)}
  <section class="card" id="rights">
    <h2>Rights check</h2>
    <p class="hint">Where every part of the app comes from and what its terms allow, for a free app with no ads.</p>
    <div class="scroll"><table class="grid"><thead><tr><th>Part</th><th>Terms found</th><th>Status</th></tr></thead><tbody>{rights_rows}</tbody></table></div>
  </section>
</div></main>
<footer>Re-Iman · elh24</footer>

<script>
  document.querySelectorAll('.copy').forEach(function (b) {{
    b.addEventListener('click', function () {{
      var el = document.getElementById(b.dataset.for), text = el.innerText;
      var ok = function () {{ b.textContent = 'Copied'; b.classList.add('ok'); setTimeout(function () {{ b.textContent = 'Copy'; b.classList.remove('ok'); }}, 1600); }};
      var fallback = function () {{ var r = document.createRange(); r.selectNodeContents(el); var s = getSelection(); s.removeAllRanges(); s.addRange(r); b.textContent = 'Selected, press Ctrl+C'; }};
      try {{ navigator.clipboard.writeText(text).then(ok, fallback); }} catch (e) {{ fallback(); }}
    }});
  }});
  document.querySelectorAll('.done input').forEach(function (c) {{
    var key = 'reiman-kit-step-' + c.dataset.step, card = c.closest('.step');
    try {{ c.checked = localStorage.getItem(key) === '1'; }} catch (e) {{}}
    card.classList.toggle('is-done', c.checked);
    c.addEventListener('change', function () {{
      card.classList.toggle('is-done', c.checked);
      try {{ localStorage.setItem(key, c.checked ? '1' : '0'); }} catch (e) {{}}
    }});
  }});
</script>
"""
open(os.path.join(HERE, "launch-kit.html"), "w", encoding="utf-8").write(page)
print("written", len(page), "chars,", fid, "copy fields")
