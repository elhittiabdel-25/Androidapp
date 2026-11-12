# Table of the 51 board scenes of wave 94 -> ArtWave94.kt and w94.py
M = dict(R="ROW94", F="FLOW94", V="VS94", N="FAN94", T="TILES94", C="TRACT94", W="WAVE94")
TONE = dict(gold="Qn.Gold", jade="Qn.Jade", ember="Qn.Ember", rose="Qn.Rose", light="Qn.GoldLight")
S = [
# ── batch 18 ──
("tt-mafhum", "ttmafhum", 0, [
 ("face-0", "التفسير في اللغة", "R", ["eye:الإيضاح", "light:والكشف", "book:والبيان"], None, None),
 ("face-1", "فهو من صلبه", "F", ["book:بالآية", "light:المعنى المراد", "key:من صلبه:t"], None, None),
 ("face-2", "فهو من متممات التفسير وعلومه", "V", ["key:صلب التفسير:t", "scroll:متممات التفسير"], None, None),
 ("deep-0", "المعصرات هي السحاب", "R", ["cloud:السحاب", "drop:الماء المنصب"], "المعصرات", None)]),
("tt-tanawwu", "tttanawwu", 1, [
 ("face-0", "مجمعا عليه", "V", ["people:مجمعا عليه", "scale:مختلفا فيه"], None, None),
 ("face-1", "يرجع إلى معنى واحد", "N", ["scroll:-", "scroll:-", "scroll:-", "light:معنى واحد"], "ثلاثة أنواع", None),
 ("face-2", "بلا تعارض", "R", ["book:الآية", "check:بلا تعارض:t:jade"], "اختلاف التنوع", None),
 ("deep-0", "أمثلة له", "N", ["star:-", "star:-", "star:-", "crown:الاسم العام"], "أمثلة له", None)]),
("tt-tadadd", "tttadadd", 0, [
 ("face-0", "وهو نوعان", "R", ["book:الآية", "scale:نوعان"], None, None),
 ("face-1", "دخل ذلك في اختلاف التنوع", "F", ["people:الأقوال الواردة", "check:اختلاف التنوع:t:jade"], None, None),
 ("face-2", "انتفى الآخر", "V", ["check:قول:t:jade", "cross:الآخر:x:ember"], "التضاد", None),
 ("deep-1", "بأول الليل وآخره", "R", ["crescent:أول الليل", "star:وآخره"], "عسعس", None)]),
("tt-asbab", "ttasbab", 2, [
 ("face-0", "أسباب الاختلاف في التفسير كثيرة", "N", ["scroll:-", "scroll:-", "scroll:-", "book:قول واحد"], "أشبه بالصوري", None),
 ("face-1", "الاشتراك اللغوي", "F", ["lips:للفظ", "light:أكثر من معنى"], "الاشتراك اللغوي", None),
 ("face-2", "التواطؤ", "T", ["txt:كالنازعات", "txt:والخنس", "txt:والعاديات"], "التواطؤ", None),
 ("deep-0", "ملاقي ربك", "V", ["txt:ملاقي ربك", "txt:ملاقي عملك"], "الضمير", None)]),
("tt-lafz", "ttlafz", 3, [
 ("face-0", "ثلاثة", "R", ["lips:اللفظ", "light:المعنى", "scale:القياس"], None, None),
 ("face-1", "بما ورد في لغة العرب", "F", ["lips:اللفظ", "scroll:لغة العرب"], None, None),
 ("face-2", "حمل الآية على ما يشابهها", "F", ["book:الآية", "point:الإشارة"], None, None),
 ("deep-2", "هو أجل رسول الله", "F", ["scroll:سورة النصر", "hourglass:أجل"], "ابن عباس", None)]),
("tt-tabaqat", "tttabaqat", 1, [
 ("face-0", "ثلاث", "R", ["people:الصحابة", "people:التابعين::jade", "people:أتباع التابعين::light"], None, None),
 ("face-1", "وترجمان القرآن", "R", ["star:حبر الأمة", "book:وترجمان القرآن"], "عبد الله بن العباس", None),
 ("face-2", "تلاميذه", "T", ["txt:كسعيد بن جبير", "txt:ومجاهد بن جبر", "txt:وعكرمة", "txt:وعطاء بن أبي رباح"], None, None),
 ("deep-1", "فوازن بينها", "F", ["people:أقوالهم", "scale:الراجح"], "ابن جرير الطبري", None)]),
("tt-hushirat", "tthushirat", 0, [
 ("face-0", "حجية الصحابة في اللغة", "R", ["people:الصحابة", "lips:اللغة:t"], None, None),
 ("face-1", "قد لا تجده في معاجم اللغة", "V", ["scroll:تفسير", "book:معاجم اللغة:x"], None, None),
 ("deep-0", "بالاختلاط", "V", ["txt:بالاختلاط", "txt:جمع مع سوق"], "أبي بن كعب", None),
 ("deep-2", "بلازم اللفظ", "F", ["people:جمع", "chain:الاختلاط"], "بلازم اللفظ", None)]),
("tt-naba", "ttnaba", 4, [
 ("face-0", "الخبر العظيم", "F", ["people:كفار مكة", "scroll:الخبر العظيم"], None, None),
 ("face-1", "هو القرآن", "R", ["book:القرآن"], "مجاهد", None),
 ("face-2", "هو البعث", "R", ["hourglass:البعث"], "قتادة", None),
 ("deep-1", "يرجع إلى قولين", "N", ["book:-", "hourglass:-", "check:اختلاف التنوع:t:jade"], None, "قولين")]),
("tt-zuwwijat", "ttzuwwijat", 3, [
 ("face-0", "على قولين", "R", ["scale:قولين"], "تزويج النفوس", None),
 ("face-1", "ألحق كل إنسان بشكله", "R", ["people:الضرباء::jade", "people:والأمثال::jade"], "عمر بن الخطاب", None),
 ("face-2", "ردت الأرواح إلى الأجساد", "F", ["light:الأرواح", "people:الأجساد"], "عكرمة والشعبي", None),
 ("deep-1", "فيقرن الكافر مع الكافر", "V", ["people:الكافر مع الكافر::ember", "people:والمؤمن مع المؤمن::jade"], None, None)]),
("tt-fajr", "ttfajr", 2, [
 ("face-0", "فجر الصبح", "R", ["sun:فجر الصبح", "mat:صلاة الفجر"], None, None),
 ("face-1", "أول النهار", "R", ["sun:أول النهار:t"], None, None),
 ("face-2", "عشر ذي الحجة", "R", ["crescent:الليالي العشر", "kaaba:ذي الحجة"], None, None),
 ("deep-3", "لتجازون بأعمالكم", "R", ["scale:بأعمالكم"], "وجواب القسم", None)]),
("tt-shaf", "ttshaf", 0, [
 ("face-0", "بالشفع والوتر", "T", ["txt:بالشفع", "txt:والوتر"], None, None),
 ("face-1", "يوم النحر", "R", ["sun:يوم النحر", "mountain:يوم عرفة", "mat:الصلاة المكتوبة"], None, None),
 ("deep-0", "داخل في هذا القسم", "N", ["sun:-", "mountain:-", "mat:-", "crown:لعمومه"], "الطبري", None)]),
("tt-quran", "ttquran", 4, [
 ("face-0", "سؤال يتبعه جوابه", "F", ["lips:سؤال", "light:جوابه"], None, None),
 ("face-1", "يوم الدين", "R", ["scale:يوم الدين"], "سورة الانفطار", None),
 ("face-2", "حجة بلا إشكال", "F", ["book:القرآن", "book:بالقرآن", "check:حجة:t:jade"], None, None),
 ("deep-2", "النجم المضيء", "F", ["lips:الطارق", "star:النجم المضيء"], None, None)]),
("tt-tin", "tttin", 2, [
 ("face-1", "التين الذي يؤكل", "R", ["tree:التين", "tree:والزيتون::jade"], None, None),
 ("face-2", "وأماكن نباتهما", "R", ["tree:الشجرتان", "mountain:وأماكن"], None, None),
 ("deep-1", "لا تخرج عن الشام", "R", ["mountain:الشام"], "موطن كثير من النبوات", None),
 ("deep-2", "محال ثلاثة", "R", ["house:بيت المقدس", "mountain:وطور سيناء", "kaaba:ومكة"], None, None)]),
("tt-qadr", "ttqadr", 0, [
 ("face-0", "جملة واحدة", "F", ["scroll:اللوح المحفوظ", "star:السماء الدنيا"], "ليلة القدر", None),
 ("face-1", "بعضه في إثر بعض", "F", ["star:-", "book:بعضه في إثر بعض"], None, None),
 ("face-2", "مقادير السنة القادمة", "R", ["scroll:مقادير السنة القادمة", "pen:ليلة الحكم"], None, None),
 ("deep-0", "ألف شهر", "R", ["crescent:-", "scale:تعدل", "hourglass:ألف شهر"], None, None)]),
("tt-falaq", "ttfalaq", 2, [
 ("face-1", "ولم يكن لهذا السحر أثر على تبليغ الوحي", "V", ["book:تبليغ الوحي:t", "chain:السحر:x:ember"], None, None),
 ("face-2", "والفلق هو الصبح", "R", ["sun:الصبح"], "والفلق", None),
 ("deep-1", "جب في جهنم", "R", ["scroll:منكر:x:ember"], "جب في جهنم", None),
 ("deep-2", "أذكار الصباح والمساء", "R", ["mat:أدبار الصلوات", "sun:الصباح والمساء", "bed:وعند النوم"], None, None)]),
# ── batch 19 ──
("tz-mujadila", "tzmujadila", 1, [
 ("face-0", "أن يتناجوا بالإثم والعدوان", "R", ["fig:-", "lips:بالإثم والعدوان:x:ember", "fig:-"], None, None),
 ("face-1", "من تزيين الشيطان", "F", ["flame:الشيطان::ember", "heart:الحزن"], None, None),
 ("face-2", "بالتوسعة في المجلس", "R", ["fig:-", "fig:-", "fig:-"], "بالتوسعة في المجلس", "وعدم التضايق فيه"),
 ("deep-2", "وسع الله له في الجنة", "F", ["people:لإخوانه", "tree:الجنة:t:jade"], None, None)]),
("tz-hashr", "tzhashr", 3, [
 ("face-0", "اضطروا إلى الخروج من مكة", "F", ["kaaba:مكة", "people:المهاجرين"], None, None),
 ("face-1", "وأشركوهم في أموالهم ومساكنهم", "R", ["people:الأنصار::jade", "coin:أموالهم", "house:ومساكنهم"], None, None),
 ("face-2", "يقدمون المهاجرين على أنفسهم", "R", ["heart:على أنفسهم", "hands:حاجة وفقر"], None, None),
 ("deep-1", "يستغفرون للسابقين", "F", ["people:التابعون", "hands:يستغفرون", "star:للسابقين"], None, None)]),
("tz-mumtahana", "tzmumtahana", 1, [
 ("face-1", "النهي عن موالاة الكفار", "R", ["chain:موالاة الكفار:x:ember"], None, None),
 ("face-2", "كصلة الرحم ونفع الجار والضيافة", "R", ["heart:كصلة الرحم", "house:ونفع الجار", "cup:والضيافة"], "البر", None),
 ("deep-0", "كالوفاء لهم بالوعد", "R", ["hands:بالوعد", "key:الأمانة", "coin:أثمان"], "العدل", None),
 ("deep-1", "بأن يسلموا", "F", ["people:عاداهم", "heart:مودة:t:jade"], None, None)]),
("tz-saff", "tzsaff", 0, [
 ("face-0", "بأحب الأعمال", "R", ["fig:-", "heart:بأحب الأعمال"], None, None),
 ("face-1", "كره ذلك أناس منهم", "R", ["star:الجهاد", "fig:وشق عليهم:x"], None, None),
 ("face-2", "يمقت مقتا عظيما", "V", ["lips:يقول", "hands:ما لا يفعل:x:ember"], None, None),
 ("deep-2", "أمال الله قلوبهم عن الحق", "F", ["light:الحق", "heart:قلوبهم:x:ember"], None, None)]),
("tz-jumua", "tzjumua", 2, [
 ("face-0", "وجب المضي إلى ذكر الله", "F", ["fig:-", "mat:وصلاة الجمعة"], "أذن المؤذن", None),
 ("face-1", "ويترك البيع", "R", ["coin:البيع:x:ember", "scale:المعاملات:x:ember"], None, None),
 ("face-2", "انتشروا للتجارة", "F", ["mat:الصلاة", "coin:للتجارة:t:jade"], None, None),
 ("deep-0", "ذكرا كثيرا", "R", ["lips:ذكرا كثيرا", "heart:بالشكر له"], None, None)]),
("tz-munafiqun", "tzmunafiqun", 3, [
 ("face-0", "ألهتهم أموالهم وأولادهم", "R", ["coin:أموالهم", "child:وأولادهم"], None, "عن ذكره"),
 ("face-1", "فرائض الإسلام", "R", ["mat:فرائض الإسلام", "book:قراءة القرآن"], None, None),
 ("face-2", "الكامل في الخسران", "V", ["coin:بالدنيا::ember", "book:الدين"], None, "الخسران"),
 ("deep-0", "قبل أن تنزل بالعبد أسباب الموت", "F", ["coin:بالإنفاق", "hourglass:الموت"], None, None)]),
("tz-taghabun", "tztaghabun", 0, [
 ("face-0", "فبقضاء الله وقدره", "R", ["cloud:مصيبة", "pen:وقدره"], None, None),
 ("face-1", "هدى الله قلبه عند المصيبة", "R", ["cloud:المصيبة", "heart:قلبه:t:jade"], None, None),
 ("face-2", "فيسلم لقضائه ويسترجع", "R", ["fig:-", "hands:ويسترجع"], None, None),
 ("deep-0", "بلاء واختبار ومحنة", "R", ["coin:والأموال", "child:والأولاد", "scale:واختبار"], None, None)]),
("tz-talaq", "tztalaq", 1, [
 ("face-0", "في طهر لم يقع فيه جماع", "F", ["drop:طهر:t", "hourglass:عدتها"], None, None),
 ("face-1", "حتى تتم العدة", "R", ["pen:الوقت", "hourglass:العدة"], None, None),
 ("face-2", "ولا تخرج من بيتها", "R", ["house:بيتها", "hourglass:العدة"], None, None),
 ("deep-1", "كان له مخرج ومخلص", "F", ["scroll:أحكام الله", "key:مخرج:t:jade"], None, None)]),
("tz-tahrim", "tztahrim", 3, [
 ("face-0", "أن يقوا أهليهم النار", "R", ["fig:-", "child:أهليهم", "flame:النار:x:ember"], None, None),
 ("face-1", "أن نعلم أولادنا الدين والخير", "F", ["book:الدين والخير", "child:أولادنا"], None, None),
 ("deep-0", "وهي الندم بالقلب", "T", ["txt:الندم", "txt:والاستغفار", "txt:والإقلاع", "txt:والعزم"], None, None),
 ("deep-2", "وصارت بإيمانها في جنات النعيم", "V", ["crown:فرعون:x:ember", "tree:جنات النعيم:t:jade"], None, None)]),
("tz-mulk", "tzmulk", 0, [
 ("face-0", "كثر خيره وعظم", "R", ["crown:تبارك"], None, None),
 ("face-1", "الموت انقطاع تعلق الروح بالبدن", "V", ["bed:الموت", "sprout:والحياة::jade"], None, None),
 ("face-2", "ظهور كمال إحسان المحسنين", "F", ["scale:الابتلاء", "star:المحسنين:t"], None, None),
 ("deep-2", "خلق الله النجوم لثلاث", "R", ["star:زينة للسماء", "flame:ورجوما للشياطين::ember", "point:وعلامات"], "قتادة", None)]),
("tz-qalam", "tzqalam", 2, [
 ("face-0", "على منع المساكين من خيرها", "R", ["tree:الجنة::jade", "fig:المساكين:x"], None, None),
 ("face-1", "أمثلهم وأعقلهم", "R", ["fig:أمثلهم وأعقلهم", "scale:ظلم"], None, None),
 ("face-2", "بسبب ذنبهم", "F", ["chain:ذنبهم::ember", "tree:بجنتهم:x:ember"], None, None),
 ("deep-0", "راجين لعفوه", "R", ["fig:-", "hands:راجين لعفوه"], None, None)]),
("tz-haqqa", "tzhaqqa", 4, [
 ("face-0", "يعرض العباد على الله لحسابهم", "F", ["people:العباد", "scale:لحسابهم"], None, None),
 ("face-1", "خذوا كتابي فاقرءوه", "R", ["fig:-", "book:كتابه بيمينه:t:jade"], None, None),
 ("deep-0", "في عيشة مرضية", "R", ["tree:جنة::jade", "wheat:ثمارها قريبة"], None, None),
 ("deep-1", "ليتني لم أعط كتابي", "R", ["fig:-", "book:بشماله:x:ember"], None, None)]),
("tz-maarij", "tzmaarij", 1, [
 ("face-0", "كثير الجزع", "R", ["fig:-", "cloud:الفقر", "heart:الجزع:x:ember"], None, None),
 ("face-1", "كثير المنع والإمساك", "R", ["fig:-", "coin:الغنى", "lock:المنع والإمساك::ember"], None, None),
 ("face-2", "والمقيمون للصلاة", "R", ["fig:-", "mat:للصلاة:t:jade"], None, None),
 ("deep-2", "من الأمانات", "R", ["key:الأمانات", "hands:العهود"], None, None)]),
("tz-nuh", "tznuh", 3, [
 ("face-0", "أول رسول أرسله الله", "F", ["scroll:ينذر قومه", "cloud:العذاب::ember"], "نوح", None),
 ("face-2", "لا تتركوا عبادة آلهتكم", "F", ["crown:الرؤساء", "people:أتباعهم"], None, None),
 ("deep-0", "أسماء قوم صالحين", "F", ["people:قوم صالحين", "eye:صورا"], None, None),
 ("deep-2", "فابتداء عبادة الأوثان كان من ذلك الوقت", "F", ["eye:الصور", "chain:الأوثان:x:ember"], None, None)]),
("tz-jinn", "tzjinn", 0, [
 ("face-0", "استمع عدد من الجن", "F", ["book:للقرآن", "ear:استمع"], None, None),
 ("face-1", "سمعنا كلاما مقروءا عجبا", "R", ["ear:سمعنا", "star:عجبا"], None, None),
 ("face-2", "بل الرسل جميعا من الإنس", "F", ["people:بني آدم", "scroll:الرسل"], None, None),
 ("deep-2", "سفها وطغيانا", "F", ["mountain:بواد", "flame:سفها وطغيانا:x:ember"], None, None)]),
("tz-muzzammil", "tzmuzzammil", 0, [
 ("face-0", "أن يبين القارئ جميع الحروف", "F", ["lips:الترتيل", "book:جميع الحروف"], None, None),
 ("face-1", "قول ثقيل", "R", ["book:قول ثقيل", "heart:قلب"], None, None),
 ("face-2", "لحضور القلب فيه", "R", ["moon:وقيام الليل", "heart:القلب:t", "ear:هادئة"], None, None),
 ("deep-2", "الانقطاع إلى الله", "R", ["mat:بعبادته"], "والتبتل", None)]),
("tz-muddaththir", "tzmuddaththir", 3, [
 ("face-0", "وهيأ الكلام في نفسه", "R", ["fig:-", "lips:الكلام"], None, None),
 ("face-1", "لم يجد مطعنا", "V", ["book:القرآن:t", "cross:مطعنا:x:ember"], None, None),
 ("face-2", "إلا سحرا", "R", ["lips:سحرا:x:ember"], None, None),
 ("deep-0", "تسعة عشر من الملائكة", "R", ["flame:بالنار::ember", "light:الملائكة"], "تسعة عشر", None)]),
("tz-qiyama", "tzqiyama", 4, [
 ("face-0", "حسبان باطل", "F", ["chain:عظامه", "cloud:رفاتا"], None, "حسبان باطل"),
 ("face-1", "قطعة واحدة كخف البعير", "R", ["hands:أصابعه"], "قادر", None),
 ("deep-0", "في تخطيط بصمته", "R", ["hands:بنان", "eye:بصمته"], None, None),
 ("deep-1", "يقدم الذنب ويؤخر التوبة", "V", ["flame:الذنب::ember", "hourglass:التوبة"], None, None)]),
("tz-insan", "tzinsan", 1, [
 ("face-0", "يخافون يوم القيامة", "R", ["people:الأبرار", "scale:يوم القيامة"], None, None),
 ("face-1", "ويطعمون الطعام", "F", ["fig:-", "cup:الطعام", "fig:-"], None, None),
 ("face-2", "لا يتوقعون المكافأة", "R", ["coin:المكافأة:x:ember", "lips:ثناء الناس:x:ember"], None, None),
 ("deep-2", "وجزاهم جنة", "R", ["tree:جنة:t:jade", "sun:حر الشمس:x:ember"], None, None)]),
("tz-mursalat", "tzmursalat", 0, [
 ("face-0", "حافظة للناس", "V", ["people:أحياء", "bed:وأمواتا"], "الأرض", None),
 ("face-1", "يفصل فيه بين الخلائق", "V", ["light:الحق", "cloud:الباطل::ember"], "ويوم الفصل", None),
 ("deep-0", "ثلاث فرق", "R", ["cloud:-::ember", "cloud:-::ember", "cloud:-::ember"], "دخان جهنم", "ثلاث فرق"),
 ("deep-3", "فبأي حديث غير القرآن يصدقون", "R", ["book:القرآن:t"], None, None)]),
# ── batch 20 ──
("ta-nun", "tanun", 0, [
 ("face-0", "أربعة أحكام", "T", ["txt:الإظهار", "txt:والإدغام", "txt:والإقلاب", "txt:والإخفاء"], None, None),
 ("face-1", "أحرف الحلق الستة", "C", ["halq:الحلق"], None, None),
 ("face-2", "الهمزة والهاء", "T", ["txt:الهمزة والهاء", "txt:العين والحاء", "txt:الغين والخاء"], None, None),
 ("deep-1", "نطقت النون ظاهرة", "T", ["txt:النون:t"], None, "ظاهرة")]),
("ta-idgham", "taidgham", 1, [
 ("face-0", "يرملون", "T", ["txt:يرملون"], "الإدغام", None),
 ("face-1", "ينمو", "T", ["txt:ينمو"], "بغنة", None),
 ("face-2", "في اللام والراء", "T", ["txt:اللام", "txt:والراء"], "بغير غنة", None),
 ("deep-0", "دنيا وصنوان", "T", ["txt:دنيا:x:ember", "txt:وصنوان:x:ember"], "ولا إدغام", None)]),
("ta-iqlab", "taiqlab", 2, [
 ("face-0", "ميما عند الباء", "F", ["txt:النون", "txt:ميما", "txt:الباء"], "الإقلاب", None),
 ("face-1", "عند ما بقي من الحروف", "T", ["txt:الإخفاء"], None, None),
 ("face-2", "خمسة عشر حرفا", "T", ["txt:خمسة عشر حرفا"], None, None),
 ("deep-0", "صف ذا ثنا", "T", ["txt:صف ذا ثنا", "txt:كم جاد شخص", "txt:قد سما"], None, None)]),
("ta-mim", "tamim", 3, [
 ("face-0", "حرف غنة", "C", ["khayshum:الغنة"], None, None),
 ("face-1", "ثلاثة أحكام", "T", ["txt:الإخفاء", "txt:والإدغام", "txt:والإظهار"], None, None),
 ("face-2", "الإخفاء الشفوي", "C", ["shafa:الشفوي"], None, None),
 ("deep-0", "إدغاما صغيرا", "F", ["txt:ميم", "txt:مثلها"], None, "إدغاما صغيرا")]),
("ta-lam", "talam", 0, [
 ("face-0", "حالان", "T", ["txt:الإظهار", "txt:والإدغام"], "للام التعريف", None),
 ("face-1", "اللام القمرية", "R", ["moon:القمرية:t"], None, None),
 ("face-2", "اللام الشمسية", "R", ["sun:الشمسية"], None, None),
 ("deep-2", "فتظهر مطلقا", "T", ["txt:قل نعم", "txt:وقلنا", "txt:والتقى"], "لام الفعل", None)]),
("ta-mithlan", "tamithlan", 1, [
 ("face-0", "مثلان", "R", ["lips:والمخرج:t:jade", "star:الصفات:t:jade"], "مثلان", None),
 ("face-1", "متقاربان", "R", ["lips:المخرج", "star:الصفات:x:ember"], "متقاربان", None),
 ("face-2", "متجانسان", "R", ["lips:المخرج:t:jade", "star:الصفات:x:ember"], "متجانسان", None),
 ("deep-0", "سمي صغيرا", "V", ["txt:صغيرا", "txt:كبيرا"], None, None)]),
("ta-madd", "tamadd", 2, [
 ("face-0", "أصلي وفرعي", "V", ["txt:أصلي", "txt:وفرعي"], "المد", None),
 ("face-1", "الطبيعي", "W", ["2:الطبيعي"], None, None),
 ("face-2", "الهمز أو السكون", "W", ["4:الهمز", "6:السكون"], "والفرعي", None),
 ("deep-0", "نوحيها", "T", ["txt:واي", "txt:نوحيها"], None, None)]),
("ta-madd-ahkam", "tamaddahkam", 0, [
 ("face-0", "ثلاثة أحكام", "T", ["txt:الوجوب", "txt:والجواز", "txt:واللزوم"], None, None),
 ("face-1", "المد المتصل", "W", ["4:المتصل"], None, None),
 ("face-2", "المد المنفصل", "W", ["4:المنفصل"], None, None),
 ("deep-2", "مدا طويلا", "W", ["6:واللازم"], None, None)]),
("jz-wajib", "jzwajib", 4, [
 ("face-0", "مقدمة", "F", ["scroll:مقدمة", "book:قارئ القرآن"], "ابن الجزري", None),
 ("face-1", "مخارج الحروف وصفاتها", "C", ["lisan:مخارج الحروف"], None, None),
 ("face-2", "بأفصح اللغات", "R", ["lips:بأفصح اللغات:t"], None, None),
 ("deep-1", "المقطوع والموصول", "R", ["pen:المصاحف", "chain:المقطوع والموصول"], None, None)]),
("jz-makharij", "jzmakharij", 0, [
 ("face-1", "مخرجها الجوف", "C", ["jawf:الجوف"], None, None),
 ("face-2", "وللحلق ثلاثة مخارج", "C", ["halq:وللحلق"], None, None),
 ("deep-0", "في اللسان", "C", ["lisan:اللسان"], None, None),
 ("deep-2", "وللشفتين", "C", ["shafa:وللشفتين"], None, None),
 ("deep-3", "الخيشوم", "C", ["khayshum:الخيشوم"], None, None)]),
("jz-sifat", "jzsifat", 1, [
 ("face-0", "صفات لها أضداد", "V", ["txt:الجهر", "txt:الهمس"], None, None),
 ("face-1", "والاستفال", "V", ["txt:والاستفال", "txt:الاستعلاء"], None, None),
 ("face-2", "خص ضغط قظ", "T", ["txt:خص ضغط قظ"], "سبعة", None),
 ("deep-2", "قطب جد", "T", ["txt:قطب جد"], "القلقلة", None)]),
("jz-tajwid", "jztajwid", 2, [
 ("face-0", "حتم لازم", "R", ["book:بالتجويد:t"], None, None),
 ("face-2", "حلية التلاوة", "R", ["crown:حلية التلاوة", "star:وزينة الأداء"], None, None),
 ("deep-0", "إعطاء الحروف حقها", "R", ["scale:حقها", "key:ومستحقها"], None, None),
 ("deep-2", "من غير تكلف", "V", ["lips:باللطف:t:jade", "chain:تعسف:x:ember"], None, None)]),
("jz-tarqiq", "jztarqiq", 3, [
 ("face-0", "ترقق الحروف المستفلة", "T", ["txt:المستفلة"], "ترقق", None),
 ("face-1", "وتفخم حروف الاستعلاء", "T", ["txt:الاستعلاء", "txt:الإطباق"], "وتفخم", None),
 ("face-2", "بعد فتح أو ضم", "T", ["txt:عبد الله"], "وتفخم اللام", "بعد فتح أو ضم"),
 ("deep-0", "إذا كسرت", "T", ["txt:الراء"], "وترقق", "إذا كسرت")]),
("jz-waqf", "jzwaqf", 0, [
 ("face-0", "معرفة الوقوف والابتداء", "R", ["lock:الوقوف", "key:والابتداء"], None, None),
 ("face-1", "ثلاثة أقسام", "T", ["txt:تام", "txt:وكاف", "txt:وحسن"], None, None),
 ("face-2", "قبيح", "T", ["txt:قبيح:x:ember"], None, "اضطرارا"),
 ("deep-2", "وقف واجب ولا حرام", "V", ["txt:واجب:x", "txt:حرام:x"], None, None)]),
("jz-wasl", "jzwasl", 1, [
 ("face-0", "مضمومة", "F", ["txt:مضموما", "txt:مضمومة"], "بهمزة الوصل", None),
 ("face-1", "وتكسر", "T", ["txt:مكسورا", "txt:مفتوحا"], "وتكسر", None),
 ("face-2", "وتكسر في الأسماء", "T", ["txt:الأسماء"], None, "لام التعريف"),
 ("deep-0", "ابن وابنة", "T", ["txt:ابن وابنة", "txt:وامرؤ وامرأة", "txt:واثنان واثنتان", "txt:واسم"], None, None)]),
("jz-rawm", "jzrawm", 2, [
 ("face-0", "بالحركة كاملة", "T", ["txt:بالحركة كاملة:x:ember"], None, None),
 ("face-1", "ببعض الحركة", "W", ["1:الروم"], None, None),
 ("face-2", "إشارة بالضم", "C", ["shafa:الإشمام"], None, None),
 ("deep-2", "مائة وسبعة", "T", ["txt:مائة وسبعة"], "وأبياتها", None)]),
]
kt, sc, meta = [], [], []
for cid, lay, sky, beats in S:
    rows, kinds, fig, kb = [], [], False, []
    for i, (at, label, mode, items, head, foot) in enumerate(beats):
        kind = f"{lay}{i}"; kinds.append(kind); parts = []
        def part(t):
            if t in (None, "-"): return -1
            if t not in parts: parts.append(t)
            return parts.index(t)
        its = []
        for it in items:
            f = it.split(":"); icon, tag = f[0], f[1]
            mark = {"": 0, "t": 1, "x": 2}[f[2] if len(f) > 2 else ""]
            tone = TONE[f[3]] if len(f) > 3 and f[3] else None
            fig = fig or icon == "fig"
            args = [f'"{icon}"', str(part(tag))]
            if mark or tone: args.append(str(mark))
            if tone: args.append(tone)
            its.append(f"It94({', '.join(args)})")
        extra = ""
        if head: extra += f", head = {part(head)}"
        if foot: extra += f", foot = {part(foot)}"
        kb.append(f'    Bt94("{kind}", {M[mode]}, listOf({", ".join(its)}){extra}),')
        rows.append(f'        I("{label}", None, "{at}", sub="{"|".join(parts)}", kind="{kind}"),')
    kt.append(f"private val B_{lay.upper()} = listOf(\n" + "\n".join(kb) + f"\n)\n\ninternal fun DrawScope.{lay}Scene(a: ArtCtx) = board94(a, {sky}, B_{lay.upper()})\n")
    sc.append(f'    "{cid}": S("art", [\n' + "\n".join(rows) + f'\n    ], layout="{lay}"),')
    meta.append(f'    "{lay}": dict({"figure=True, " if fig else ""}kinds={kinds!r}),')
R = "/home/claude/androidapp"
open(f"{R}/app/src/main/java/app/usulcards/ui/ArtWave94.kt", "w", encoding="utf-8").write(
"package app.usulcards.ui\n\nimport androidx.compose.ui.graphics.drawscope.DrawScope\n\n/*\n * Wave 94 (2026-11-12): v1.4, the 51 cards of batches 18 to 20, as boards (see ArtKit94.kt). Each beat names what it shows\n * with the card's own words. Prophets, companions and the jinn are never drawn: a plain figure stands only for an\n * unnamed person. Generated from the table in tools/scenes/w94_table.py; edit the table, not this file.\n */\n\n" + "\n".join(kt))
open("/home/claude/sc94/w94.py", "w", encoding="utf-8").write("SC = '''\n    # ── v1.4: the Quran, batches 18 to 20 (wave 94) ──\n" + "\n".join(sc) + "\n'''\nMETA = {\n" + "\n".join(meta) + "\n}\n")
print(len(S), "scenes,", sum(len(b) for _, _, _, b in S), "beats")
