# بطاقات الأصول — Usul Cards

An offline Android app of knowledge cards built from books in the
[Islamic Content Encyclopedia](https://islamcontent.com/ar/content).

- Each card: a short face, a deeper layer with evidence (verses and hadith with references),
  sources with page numbers, related cards, and a self-test that flips.
- Ideas found in several books are merged into one card that cites all of them.
- Audio: every sentence is read aloud with an Arabic voice and highlighted while it plays;
  Quran verses play a real recitation (Mishary Alafasy), never a synthetic voice.
- Daily goal, streak, points, learning paths, saved cards, spaced repetition ("show me again later").

## Install on your phone
Every push to `main` builds a signed APK automatically (GitHub Actions).
Open the repository's **Releases** page on your phone, download the latest `UsulCards-*.apk`,
and install it (allow installs from your browser when Android asks).
New builds install over the old one and keep your progress.

## Layout
- `app/` — the Android app (Kotlin, Jetpack Compose).
- `app/src/main/assets/content/cards.json` — the cards; `assets/audio/` — one clip per sentence.
- `tools/` — the content pipeline (card sources, vowelled speech text, Arabic voice preparation).
- `tools/fetch_quran.py` — run by the build: verse recitations and Uthmani text.

Fonts: Amiri Quran, Noto Naskh Arabic, Reem Kufi, IBM Plex Sans Arabic (SIL Open Font License, see `licenses/`).
