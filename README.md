# Re-Iman — جَدِّدْ إِيمَانَكَ

An offline Android app of knowledge cards built from books in the
[Islamic Content Encyclopedia](https://islamcontent.com/ar/content). Google Play ID: `app.reiman`.

- Each card: a short face, a deeper layer with evidence (verses and hadith with references),
  sources with page numbers, related cards, and a self-test that flips.
- Ideas found in several books are merged into one card that cites all of them.
- Audio: every sentence is read aloud with an Arabic voice and highlighted while it plays;
  Quran verses play a real recitation (Mishary Alafasy, via AlQuran.cloud), never a synthetic voice.
- A light animation on every card, timed to the voice; cards grouped in categories and paths.
- Daily goal, streak, points, learning paths, saved cards, spaced repetition ("show me again later").

## Builds and releases
Every push to `main` builds the app (GitHub Actions) and attaches two files to a GitHub release:
- `Re-Iman-<version>-<n>.apk` to install directly on a phone;
- `Re-Iman-<version>-<n>.aab` to upload to Google Play Console.

Signing uses the **upload key**, which is not in the repository: add it as four repository secrets
(Settings → Secrets and variables → Actions): `UPLOAD_KEYSTORE_BASE64`, `UPLOAD_KEYSTORE_PASSWORD`,
`UPLOAD_KEY_ALIAS`, `UPLOAD_KEY_PASSWORD`. Without them a build is signed with a temporary debug key
(fine for testing, refused by Google Play). Google Play holds the app signing key (Play App Signing).

Before each Play upload, raise `versionName` in `app/build.gradle.kts`; `versionCode` grows by itself.

## Layout
- `app/` — the Android app (Kotlin, Jetpack Compose).
- `app/src/main/assets/content/cards.json` — the cards; `assets/audio/` — one clip per sentence.
- `tools/` — the content pipeline (card sources, vowelled speech text, Arabic voice preparation).
- `tools/fetch_quran.py` — run by the build: verse recitations and Uthmani text (AlQuran.cloud / Tanzil).
- `tools/cards/finish_pack.py` — adds categories and card animations to the content pack.
- `docs/` — privacy policy page and store graphics.

Fonts: Amiri Quran, Noto Naskh Arabic, Reem Kufi, IBM Plex Sans Arabic (SIL Open Font License, see `licenses/`).
