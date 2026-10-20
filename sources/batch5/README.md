# Batch 5: finishing Aqeedah (handoff)

Status on 2026-10-20: reading done; 36 cards written and verified on the `batch5` branch; not yet voiced.

- `notes.md`: page-referenced reading notes for al-Hamad, al-Fawzan and al-Barbahari pp. 13–104.
- `90384.txt`: text of the .doc source (no OCR needed).
- OCR of the PDFs is in `sources/ocr/<id>.txt` (`=== PAGE n ===` markers): 7650 (al-Rajihi), 90444, 97696, 765 (al-Muzani).

Done on `batch5` (commit c5dc737):
- al-Barbahari pp. 105–140, al-Muzani (matn pp. 80–92) and al-Rajihi read.
- `tools/cards/cards_batch5.py`: 36 cards, five new paths under «السيرة ومنهج أهل السنة» (أهل السنة وخصائصهم،
  السنة والبدعة، ترك الجدال والأهواء، الجماعة وولاة الأمر، الحكم على أهل القبلة) and three cards in earlier
  paths; sources merged into 13 earlier cards.
- `tools/cards/verify_batch5.py` (run with `BOOKS_TXT=<this sources/ folder>`): 477 checks, 0 problems.
- quiz choices, categories, scenes on the existing layouts; `finish_pack.py` passes on a dry build.

Still to do: voice the 296 new clips with the heavy-lam converter (`tools/tts/arabic_g2p.py`, which needs the
`espeak_phon` helper built against rhasspy espeak-ng, see `tools/tts/espeak_ph.py`), speech check, merge to
main and build; then bespoke art for the new scenes if wanted (they use the shared layouts for now).
