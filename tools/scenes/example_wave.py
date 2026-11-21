SC = '''
    # ── v1.4: the Quran, «فضائل القرآن وآداب تلاوته» (wave 93) ──
    "fq-shafi": S("art", [
        I("وردت عن النبي ﷺ أحاديث كثيرة في فضل تلاوة القرآن", None, "face-0", sub="في فضل تلاوة القرآن والعناية به", kind="manyhadith"),
        I("منها أنه يأتي يوم القيامة شفيعا لأصحابه", None, "face-1", sub="شفيعا لأصحابه|الذين كانوا يعملون به في الدنيا", kind="intercedes"),
        I("ومنها أن خير الناس من تعلم القرآن وعلمه", None, "face-2", sub="تعلم القرآن|وعلمه", kind="learnteach"),
        I("فالشرف لمن جمع بين التعلم والتعليم، والعمل بما تعلم", None, "deep-2", sub="التعلم|والتعليم|والعمل بما تعلم", kind="honorall"),
    ], layout="shafibook"),
    "fq-harf": S("art", [
        I("من قرأ حرفا من كتاب الله فله به حسنة، والحسنة بعشر أمثالها", None, "face-0", sub="فله به حسنة|والحسنة بعشر أمثالها", kind="tenfold"),
        I("بل كل حرف منها له حسنة", "بل", "face-1", sub="أَلِفٌ|لَامٌ|مِيمٌ|بل كل حرف منها له حسنة", kind="eachletter"),
        I("اقرأ وارتق ورتل كما كنت ترتل في الدنيا", None, "face-2", sub="اقرأ وارتق ورتل|فإن منزلتك عند آخر آية تقرؤها", kind="harfclimb"),
    ], layout="harfgood"),
    "fq-bayt": S("art", [
        I("الذي ليس في جوفه شيء من القرآن كالبيت الخرب", None, "face-0", sub="كالبيت الخرب", kind="ruined"),
        I("وإن الله يرفع بهذا الكتاب أقواما ويضع به آخرين", None, "face-1", sub="يرفع بهذا الكتاب أقواما|ويضع به آخرين", kind="raiselower"),
        I("ولهذا ولى نافع بن عبد الحارث على أهل مكة مولى", None, "face-2", sub="مولى|لأنه قارئ للقرآن|عالم بالفرائض", kind="mawla"),
        I("فرفعة المرء بالقرآن، لا بنسبه ولا بماله", None, "deep-2", sub="فرفعة المرء بالقرآن|لا بنسبه ولا بماله", kind="notlineage"),
    ], layout="ruinhouse"),
    "fq-nabi": S("art", [
        I("كان النبي ﷺ أعظم الناس اهتماما بالقرآن", None, "face-0", sub="آناء الليل وأطراف النهار", kind="allhours"),
        I("فافتتح سورة البقرة حتى ختمها، يقرأ مترسلا غير مستعجل", "فافتتح", "face-1", sub="سورة البقرة|مترسلا غير مستعجل", kind="slowrecite"),
        I("فإذا مر بآية فيها تسبيح سبح، وإذا مر بسؤال سأل، وإذا مر بتعوذ تعوذ", None, "face-2", sub="سبح|سأل|تعوذ", kind="respond"),
        I("فكان منهم من يختم كل ثلاث ليال", None, "deep-1", sub="كل ثلاث ليال|كل ليلتين|كل ليلة", kind="khatm"),
    ], layout="nightrecite"),
    "fq-mushaf": S("art", [
        I("لا يجوز لغير المتوضئ أن يمس المصحف", "لا", "face-0", sub="يمسه بحائل|كقفاز أو قماش", kind="withbarrier"),
        I("ويجوز لغير المتوضئ أن يقرأ من المصاحف المشتملة على التفسير", None, "face-1", sub="المصاحف المشتملة على التفسير", kind="tafsirpage"),
        I("ويجوز له أن يقرأ من الأجهزة كالجوال والحاسوب", None, "face-2", sub="كالجوال|والحاسوب", kind="devices"),
        I("وأما الجنب فلا يجوز له تلاوة القرآن حتى يغتسل", None, "deep-0", sub="حتى يغتسل", kind="junub"),
    ], layout="mushaftouch"),
    "fq-adab": S("art", [
        I("من آداب التلاوة أن يستعيذ القارئ بالله من الشيطان الرجيم", None, "face-0", sub="أن يستعيذ القارئ بالله|قبل أن يقرأ", kind="istiadha"),
        I("ثم يبسمل عند بداية كل سورة إلا سورة براءة", None, "face-1", sub="يبسمل|براءة", kind="basmala"),
        I("وينبغي له أن يحرص على الخشوع والتدبر", None, "face-2", sub="الخشوع|والتدبر", kind="tadabbur"),
        I("وقد قام ﷺ ليلة بآية واحدة يرددها", None, "deep-2", sub="بآية واحدة يرددها", kind="oneverse"),
    ], layout="recitemanners"),
    "fq-sujud": S("art", [
        I("إذا قرأ ابن آدم السجدة فسجد اعتزل الشيطان يبكي", None, "face-0", sub="فسجد|اعتزل الشيطان يبكي", kind="shaytanweeps"),
        I("وسجود التلاوة سنة لا واجب", None, "face-1", sub="سنة|لا واجب", kind="sunnahnot"),
        I("والمأموم في الصلاة تابع لإمامه", None, "face-2", sub="إن سجد الإمام للتلاوة سجد معه|وإن لم يسجد لم يسجد", kind="imamled"),
        I("وفي القرآن 15 سجدة", None, "deep-2", sub="15", kind="fifteen"),
    ], layout="tilawasujud"),
    "fq-sujud-sifa": S("art", [
        I("يقول فيه ما يقول في سجود الصلاة، كسبحان ربي الأعلى", "يقول", "face-0", sub="سبحان ربي الأعلى", kind="sameaswalat"),
        I("ومن قال فيه: سجد وجهي للذي خلقه وصوره", None, "face-1", sub="سجد وجهي للذي خلقه وصوره|سمعه|وبصره", kind="wajhisajad"),
        I("ولا تشترط له الطهارة ولا استقبال القبلة", None, "face-2", sub="الطهارة|استقبال القبلة|لا تشترط|وهو اختيار ابن باز", kind="nocondition"),
        I("وإن كان خارج الصلاة، فاختار ابن باز أنه يكبر عند السجود", None, "deep-1", sub="يكبر عند السجود|ولا يكبر عند الرفع", kind="takbiroutside"),
    ], layout="sujuddhikr"),
'''
META = {
    "shafibook": dict(figure=True, kinds=["manyhadith", "intercedes", "learnteach", "honorall"]),
    "harfgood": dict(figure=True, kinds=["tenfold", "eachletter", "harfclimb"]),
    "ruinhouse": dict(figure=True, kinds=["ruined", "raiselower", "mawla", "notlineage"], day=True),
    "nightrecite": dict(kinds=["allhours", "slowrecite", "respond", "khatm"]),
    "mushaftouch": dict(kinds=["withbarrier", "tafsirpage", "devices", "junub"]),
    "recitemanners": dict(kinds=["istiadha", "basmala", "tadabbur", "oneverse"]),
    "tilawasujud": dict(figure=True, kinds=["shaytanweeps", "sunnahnot", "imamled", "fifteen"]),
    "sujuddhikr": dict(figure=True, kinds=["sameaswalat", "wajhisajad", "nocondition", "takbiroutside"]),
}
