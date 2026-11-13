# Kareem fine-tuned on the owner's reading

`data/`: 214 lines of cards 1-29 of the reading script (التوحيد and الشرك), cut from the owner's recordings of
2026-11-12 and matched to the script by speech recognition (only lines matching at 0.88 or better). Each line's
`phoneme_ids` come from the app's own phonemizer (tools/tts/arabic_g2p.py), so the trained voice keeps the app's
pronunciation rules. Private: the owner's voice, for this app only.

Push a change to `train.cfg` to start a run (.github/workflows/voice-train.yml); results go to the `voice-ft` release.

Run 9 on: every clip lowered to Kareem's register in two passes of rubberband: pitch and timbre (formants)
together by 0.85, then pitch alone by 0.744 (pitch about 175 -> 110 Hz, formants -15%), so the voice learns the
reader's delivery (rhythm, pauses, melody) without taking on a higher, feminine register or timbre.
