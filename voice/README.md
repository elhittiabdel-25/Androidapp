# Kareem fine-tuned on the owner's reading

`data/`: 214 lines of cards 1-29 of the reading script (التوحيد and الشرك), cut from the owner's recordings of
2026-11-12 and matched to the script by speech recognition (only lines matching at 0.88 or better). Each line's
`phoneme_ids` come from the app's own phonemizer (tools/tts/arabic_g2p.py), so the trained voice keeps the app's
pronunciation rules. Private: the owner's voice, for this app only.

Push a change to `train.cfg` to start a run (.github/workflows/voice-train.yml); results go to the `voice-ft` release.

Run 8 on: every clip lowered from the owner's reading pitch (about 175 Hz) to Kareem's (about 110 Hz) with
rubberband (pitch 0.632, formants preserved), so the voice learns the delivery without moving up to the owner's register.
