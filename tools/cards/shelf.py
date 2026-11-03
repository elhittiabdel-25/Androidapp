"""The books section: whole books turned into cards, shown on their own shelf (not in the categories).

Each book lists its parts in the book's order; a part's `paths` are its chapters (learning paths), in order.
A part with no paths yet shows as coming soon; a book with no ready part is left out of the pack.
Every path in a part must have cards and must not also be in a category (finish_pack.py checks both).
`book` is the source id (sources/raw/<id>.pdf); `cover` picks the drawn cover in ui/BooksScreen.kt.
"""

SHELF = [
    {"id": "basaer", "book": "73540", "title": "بصائر", "cover": "basaer",
     "subtitle": "أكبر مشروع فكري في نقد الإلحاد وبيان بعض أدلة صحة الإسلام",
     "author": "د. هيثم طلعت",
     "parts": [
         {"title": "الجزء الأول", "name": "تفنيد آراء الملحدين في الكون والحياة",
          "paths": ["الإلحاد وطريق اليقين", "نشأة الكون", "دلائل الخالق", "الحياة والمعلومة", "نقد نظرية التطور"]},
         {"title": "الجزء الثاني", "name": "بصائر صحة الإسلام",
          "paths": ["كيف ظهر الدين؟", "بشارات الكتب السابقة", "الإعجاز العلمي", "أعظم برهان", "الإخبار بالمغيبات"]},
         {"title": "الجزء الثالث", "name": "الرد على أشهر شبهات الملحدين",
          "paths": ["تفكيك الشبهة", "أسئلة الوجود", "القدر والوحي", "القرآن المحفوظ", "نصوص وشبهات", "السنة النبوية",
                    "تشريع ورحمة", "أفكار ضالة ويقين"]},
     ]},
    {"id": "nawawi", "book": "106498", "title": "الأربعون النووية", "cover": "nawawi",
     "subtitle": "42 حديثًا من جوامع الكلم، بشرح الشيخ محمد بن إبراهيم آل الشيخ",
     "author": "الإمام النووي",
     "parts": [
         {"title": "الأحاديث 1–10", "name": "أصول الدين ومبانيه", "paths": ["النية ومراتب الدين", "الحلال والحرام والنصيحة"]},
         {"title": "الأحاديث 11–20", "name": "حسن الإسلام", "paths": ["الورع وحسن الإسلام", "الغضب والإحسان والتقوى"]},
         {"title": "الأحاديث 21–30", "name": "الاستقامة وأبواب الخير", "paths": ["الاستقامة وطرق الجنة", "الصدقات والبر والسنة"]},
         {"title": "الأحاديث 31–42", "name": "الزهد والرجاء", "paths": ["الزهد والحقوق", "الحسنات والرجاء"]},
     ]},
]
