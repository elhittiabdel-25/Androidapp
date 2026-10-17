"""Categories that group the learning paths (modules), in display order.

Every path used by a card must be listed in exactly one category; finish_pack.py checks this.
`glyph` picks the small emblem drawn for the category in the app (see ui/Brand.kt).
"""

CATEGORIES = [
    {"id": "tawhid", "glyph": "one", "title": "التوحيد",
     "about": "معناه وأصوله، وفضله وتحقيقه",
     "paths": ["أساسيات التوحيد", "فضل التوحيد وتحقيقه"]},
    {"id": "shirk", "glyph": "shield", "title": "الشرك وما يضاد التوحيد",
     "about": "صوره ووسائله، والسحر والكهانة، وألفاظ تمس التوحيد",
     "paths": ["ما يضاد التوحيد", "الشرك ووسائله", "السحر والكهانة والتنجيم", "ألفاظ وأحكام تمس التوحيد"]},
    {"id": "iman", "glyph": "pillars", "title": "الإيمان",
     "about": "أركانه، وأعمال القلوب، والصفات واليوم الآخر",
     "paths": ["أركان الإيمان", "أعمال القلوب", "الصفات واليوم الآخر"]},
    {"id": "manhaj", "glyph": "path", "title": "السيرة ومنهج أهل السنة",
     "about": "معرفة النبي ﷺ، والصحابة، والولاء والبراء، وأخلاق أهل السنة",
     "paths": ["معرفة النبي ﷺ", "منهج أهل السنة"]},
]
