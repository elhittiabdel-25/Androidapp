# Re-Iman (Android, Kotlin/Compose) — working notes for Claude

Read this instead of exploring. Keep replies short; the owner is a visual learner and wants results, not narration.

## Rules
- Western digits only (0-9), everywhere. «توحيد الألوهية», never «الإلهية». Contact: Reiman.app@gmail.com.
- Work on branch `batch5`; intermediate commits end the subject with `[skip ci]`. Release = one push to `main` without it
  (`git push origin HEAD:main HEAD:batch5`); CI builds the .aab into GitHub Releases. Then send release notes (ar + en,
  `<ar>…</ar><en-US>…</en-US>`) and remind: publish on internal AND closed (Alpha) tracks.
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

## Saving tokens
- Never read whole ArtWave files or cards.json; grep for the layout/kind and read ~40 lines.
- Check Kotlin locally with kc/check.sh before any CI run; use the previews workflow only for a final look, and view at
  most 2-3 sheets (each image is costly).
- Batch edits with one python script per step; one commit per wave.
- Blind scene reviews go to cheaper sub-agents (model sonnet), 15 sheets each, only when asked.
- GitHub: REST only (`gh api`, `gh run list`); GraphQL commands (`gh release list`) fail here.
