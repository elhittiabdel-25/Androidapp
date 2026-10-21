# Visual codex (agreed 2026-10-21)

How beings, places and ideas are drawn in the card scenes. The illustrated version is the "Re-Iman Visual Codex"
artifact. `variety.py` checks what can be checked at build time.

## Beings
| Who | Drawn as | Never |
|---|---|---|
| Allah ﷻ | nothing: only His names and verses, as words | any shape, light or symbol |
| The Prophet ﷺ | bright warm golden light with soft rays (`sirajMunir`, beat `light`), always with «رَسُولُ اللَّهِ ﷺ» written beside it so it is never taken for the sun | lantern, outline, body, a light without his name |
| Other prophets | the same warm light, smaller, name written under it (`light`, value 1) | lantern, figure |
| Angels | cool white light; Jibril larger | wings, figure |
| Closest companions (4 caliphs, the ten, Ahl al-Bayt, Mothers of the Believers) | name in calligraphy, in a roundel of its own colour | figure |
| Other companions | plainer name roundel, one muted colour | figure |
| Imams and scholars | their book: bound volume, title, author's name | figure |
| Ordinary people | faceless figures in a few strokes; men, women (modest silhouettes), children | a named person |
| Animals | silhouettes | faces |
| Jinn | pale smokeless blue flame | |
| Shaytan | dark smoke winding toward the heart | horned creature |
| Idols | carved stones, shrines, trees | |

## The unseen and sacred places
- Throne and Kursi: never drawn, only their verses; the heavens may be layers below, fading out.
- Sirat (hair-thin bridge over fire), Mizan (scale), Hawd (basin): simple labelled symbols.
- Paradise (gardens, rivers beneath), Hell (fire, smoke): as the texts describe, never a full detailed picture.
- Kaaba, mosque, mihrab: drawn as the buildings they are.

## Text inside a scene
- Never imitation script: a page, a scroll or a book shows its lines as plain ruled strokes.
- Real words only where they carry the idea, and only real, sourced quotes: a verse in ornate brackets ﴿ ﴾ (set
  in the Qur'an's font), a hadith as narrated; the sources go in the scene's note.
- No label that repeats the caption under the picture; names (in roundels, on a book) and one or two key words.

## Variety
Registers to rotate: night cinema (at most a third), dawn and daylight, ink and calligraphy, geometric pattern,
map, story vignette, object close-up, nature in motion.
Explanation shapes: story with a turn, split screen, build-up, transformation, zoom, myth then correction,
count, question and answer.
Rules: a layout serves at most 6 cards; neighbouring cards in a path never share a layout, a register or an
explanation shape; every path mixes at least 3 registers; the lantern only means knowledge or guidance carried
by people (at most 6 cards) and never stands for a prophet.

## Decisions after the scene review (2026-10-21)
- Faceless figures stay, though the image-makers card (المصورون) warns against image-making. Scholars differ on
  faceless figures; a headless version was tried and read as empty garments. If a scholar the app follows says
  otherwise, the figures change in one place (`figure`, `sitting`, `praying`, `robed`, `camel`).
- In الغلو في الصالحين the righteous men stay as lights going out: the light is the knowledge they carried.
- The lantern check now reads every beat that draws a lantern (see `LANTERN` in variety.py), the sentence it is
  drawn in, and the people its tag names. Cards still to be redrawn are listed in `PENDING`.
