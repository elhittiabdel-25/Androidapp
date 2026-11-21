# Re-Iman (Android, Kotlin/Compose) — working notes for Claude

Read this instead of exploring. Keep replies short; the owner is a visual learner and wants results, not narration.

## Rules
- Western digits only (0-9), everywhere. «توحيد الألوهية», never «الإلهية». Contact: Reiman.app@gmail.com.
- Work on branch `batch5`; intermediate commits end the subject with `[skip ci]`. Release = one push to `main` without it
  (`git push origin HEAD:main HEAD:batch5`); CI builds the .aab into GitHub Releases. Then send release notes (ar + en,
  `<ar>…</ar><en-US>…</en-US>`). Testing tracks are paused: releases go to Production. Before a release, write the
  notes into distribution/whatsnew/whatsnew-ar and whatsnew-en-US (≤500 chars); once the secret
  PLAY_SERVICE_ACCOUNT_JSON exists, CI uploads the .aab to Production itself, else the owner uploads it by hand.
- Cards on a science (mustalah, transactions, inheritance, tajwid, usul): the owner finds them hard, so each one also
  gets plain explanation and worked examples ("مِثَالُهُ: …") from the same source pages, not only definitions.
- Visual codex: tools/cards/CODEX.md (Allah never drawn; the Prophet ﷺ only a named light; companions = roundels).
- A figure that draws its own arms must pass `armsAlpha = 0f` to `figure()` (else four arms).

## Where things are
- Cards: tools/cards/cards_batchNN.py (+ verify_batchNN.py) → build_cards.py → tools/tts/build_content.py (voice,
  `TTS_TOOLS=… REUSE_AUDIO=app/src/main/assets/audio`) → copy new audio + content/cards.json into assets → finish_pack.py.
- Quizzes: tools/cards/quiz_*.py. Categories: tools/cards/categories.py. Store text: docs/store/listing.py.
- Scenes: app/src/main/java/app/usulcards/ui/ArtWaveNN.kt (one `<layout>Scene(a: ArtCtx)` per card), shared kit
  ArtKit86.kt (`glyphCard`, `qIcon`, `qPlate`, `qFit`, `qSmoke`), specs in tools/cards/scenes.py.
  Per wave: write ArtWaveNN.kt + wNN.py (see tools/scenes/example_wave.py) → kindclash.py → kc/check.sh (local
  type-check, ~15 s) → add_scenes.py → wireall.py → wire2.py → finish_pack.py. Scripts in tools/scenes/ (their
  hard-coded scratch paths need adjusting per session).
- Reminder: reminder/Reminders.kt + ui/ReminderCard.kt. Summary overlay: ui/Scenes.kt (SceneRecap). Seasons: ui/Seasons.kt.
- Scene review backlog, worst first: tools/cards/REVIEW-2026-11.md (groups 24-29 not yet reviewed).

## Voice
- New cards (from 2026-11-15) use the enhanced Kareem: `kareem-ft-e399.onnx` (+ `.onnx.json`) from the `voice-ft`
  release (fine-tuned on the owner's 51 min of reading, 400 epochs; as clear as Kareem, the owner's melody). Before
  voicing, download it over `$TTS_TOOLS/vits-piper-ar_JO-kareem-medium/ar_JO-kareem-medium.onnx` (keep the original
  as `.base.onnx`). About 2000 older clips are still the old Kareem (kept where the speech check heard them better); it is slower,
  so the app plays the clips listed in assets/audio/old-kareem.txt at 1.25x (tools/tts/old_voice.py, run by
  finish_pack.py). The speed menu still shows x1.
- Clarity run 12 (kareem-ft-e548/598/648, 2026-11-18): slightly clearer, but the melody switches between speakers
  inside one sentence: REJECTED; keep e399. Voice training paused; the owner will try ElevenLabs later to make
  more training audio. Do not pursue it until he asks.
- Training: branch `voice-train`, workflow voice-kaggle (Kaggle GPU via secret KAGGLE_JSON; push voice/kaggle.cmd).

## Saving tokens
- Never read whole ArtWave files or cards.json; grep for the layout/kind and read ~40 lines.
- Check Kotlin locally with kc/check.sh before any CI run; use the previews workflow only for a final look, and view at
  most 2-3 sheets (each image is costly).
- Batch edits with one python script per step; one commit per wave.
- Blind scene reviews go to cheaper sub-agents (model sonnet), 15 sheets each, only when asked.
- GitHub: REST only (`gh api`, `gh run list`); GraphQL commands (`gh release list`) fail here.
