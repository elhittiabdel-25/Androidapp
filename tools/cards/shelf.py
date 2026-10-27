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
         {"title": "الجزء الثاني", "name": "بصائر صحة الإسلام", "paths": []},
         {"title": "الجزء الثالث", "name": "الرد على أشهر شبهات الملحدين", "paths": []},
     ]},
]
