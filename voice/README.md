# Kareem fine-tuned on a male reader

`data/`: 199 lines of cards 1-29 of the reading script (التوحيد and الشرك), 26.8 minutes, cut from the male
reader's recordings of 2026-11-13 and matched to the script by speech recognition (lines matching at 0.8 or
better). The voice is untouched: his pitch (about 114 Hz) already sits in Kareem's register. Each line's
`phoneme_ids` come from the app's own phonemizer (tools/tts/arabic_g2p.py), so the trained voice keeps the app's
pronunciation rules. The test cards ahl-sunna, thr-tahara and ram-fadl are kept out. Private: for this app only.

The earlier recordings (a feminine voice, runs up to 9) were removed and are not used.

Push a change to `train.cfg` to start a run (.github/workflows/voice-train.yml); results go to the `voice-ft` release.
