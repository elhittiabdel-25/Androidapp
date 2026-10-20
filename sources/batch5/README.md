# Batch 5: finishing Aqeedah (handoff)

Status on 2026-10-20: paused while the voice and animation work shipped (build 30).

- `notes.md`: page-referenced reading notes for al-Hamad, al-Fawzan and al-Barbahari pp. 13–104, with the
  points each card will draw on.
- `90384.txt`: text of the .doc source (no OCR needed).
- OCR of the PDFs is in `sources/ocr/<id>.txt` (`=== PAGE n ===` markers): 7650 (al-Rajihi), 90444, 97696, 765 (al-Muzani).

Still to read: al-Barbahari pp. 105–140, al-Muzani (765), al-Rajihi (7650).
Then: about 35 cards in `tools/cards/cards_batch5.py` (format of `cards_batch4.py`), a verifier like
`verify_batch4.py`, quiz choices, new paths under "منهج أهل السنة", bespoke scenes (cinema layer applies
automatically), voice with the heavy-lam converter (`tools/tts/arabic_g2p.py`), speech check, build.
