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

## Visual vocabulary (chosen by the owner, 2026-11-26)
How recurring ideas are drawn. These replace older habits (lips for speech, two raised hands for an oath).

| Idea | Drawn as |
|---|---|
| Speech | sound-wave arcs flowing from the speaker toward the listener (never lips) |
| Oath (يمين، حلف) | one raised right hand |
| A lie, a false oath | the words appear as a tag, then crack and fade grey |
| «فنزلت الآية» | a beam of light coming down from the top of the scene |
| The Prophet ﷺ present in a story | a soft warm light with «رَسُولُ اللَّهِ ﷺ» written beside it |
| Other prophets | the same named light |
| Angels | cool white light (Jibril larger) |
| Movement to a place | figures face their goal and walk toward the destination drawn at the edge; a figure never leans or faces away from where it goes |
| Grading of a report | a seal stamp: gold for sahih, silver for hasan, grey for weak or mursal |
| Prohibition (نهي، حرام) | the forbidden thing drawn, with a red cross over it |
| Permission (حلال، إباحة) | a green check beside the thing |
| Du'a | two open palms with small lights rising from them |
| Death | a simple grave mound |
| Paradise | soft green-gold light with trees and flowing rivers |
| Hellfire, punishment | rising flames in the lower part of the scene (never on a person) |
| Narration («حدثنا», «قال فلان») | the narrator's name roundel, then his words appear under it |
| Battles | Kings and Generals / History Marche style: a parchment top-down map (mountains, wells, Madina, Makka), army blocks with banners, arrows for movement; Muslims green, the enemy red, each with a name tag |
| Large numbers (70 killed, 300 men, 10 years) | dots fill one by one while the number counts up in Western digits |
| A gathering, a lesson, a sermon | faceless figures seated in a half circle around the named light or a roundel |
| Emotions | posture and colour together: head bowed and a cool blue glow for sadness, upright with gold light for joy, crouched with dark red for fear |
| Tears, weeping | a soft blue glow around the person (no drops) |
| Allah's mercy, forgiveness, accepted repentance | a soft beam of light falling on the person (Allah is never depicted) |
| Time passing | a quick day/night cycle: sun and moon crossing the sky |
| Money, spoils, charity | the real goods given: dates, grain sacks, cloth, silver vessels (coins only when the text says dinars or dirhams) |
| Animals (camel, sheep, horse) | a tiny dark silhouette far away, no detail, facing where it goes |
| Women | a fully covered faceless silhouette; a named Mother of the Believers may also have her roundel |
| Hadd punishments (stoning, lashing, cutting) | an open book of law with the ruling as a tag; never the act |
| Background | fits the story: day for daytime events, night for night, mosque, desert, Madina houses as told |
| Fiqh cards with no story | act out the card's worked example when it has one; otherwise a clean diagram (boxes, arrows, shares) |
| Tags | 2–4 words each |
| Pace | each picture changes when the voice reaches its sentence |
