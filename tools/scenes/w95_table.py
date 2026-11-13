# Table of the 39 board scenes of wave 95 (v1.5, the Sira) -> ArtWave95.kt and w95.py
# CODEX: the Prophet ﷺ is only a named light; companions and Jibril are name tiles; no angel, animal or body drawn.
M = dict(R="ROW94", F="FLOW94", V="VS94", N="FAN94", T="TILES94", C="TRACT94", W="WAVE94")
TONE = dict(gold="Qn.Gold", jade="Qn.Jade", ember="Qn.Ember", rose="Qn.Rose", light="Qn.GoldLight")
NB = "light:النبي ﷺ:t"
S = [
# ── قبل البعثة ──
("sb-nasab", "sbnasab", 0, [
 ("face-0", "ونسبه ينتهي إلى عدنان", "F", ["txt:عبد المطلب", "txt:هاشم", "txt:عدنان"], "محمد بن عبد الله", None),
 ("face-1", "من نسل إسماعيل", "F", ["txt:إبراهيم", "txt:إسماعيل", "txt:عدنان"], None, None),
 ("face-2", "خيار من خيار من خيار", "N", ["people:كنانة", "people:قريش", "people:بني هاشم", NB], None, None),
 ("deep-1", "ثم اصطفى من بني هاشم", "R", ["crown:اصطفى", NB], None, None)]),
("sb-mawlid", "sbmawlid", 2, [
 ("face-0", "من عام الفيل", "R", ["sun:يوم الاثنين", "crescent:ربيع الأول", "hourglass:عام الفيل"], None, None),
 ("face-1", "فرد الله كيده وحفظ بيته", "V", ["kaaba:بيته:t:jade", "txt:أبرهة:x:ember"], None, None),
 ("deep-1", "قصور بصرى", "F", ["light:نورا", "house:قصور بصرى"], None, None),
 ("deep-2", "فلم يفعله أحد من أصحابه", "R", ["people:أصحابه:x", "people:القرون الثلاثة المفضلة:x"], "الاحتفال بالمولد", None)]),
("sb-ridaa", "sbridaa", 3, [
 ("face-0", "أرضعته ثويبة", "R", ["txt:ثويبة", "txt:حمزة"], None, None),
 ("face-1", "من نصيب حليمة السعدية", "F", ["people:بني سعد", "txt:حليمة السعدية"], None, None),
 ("face-2", "ودرت البركات", "R", ["house:ذلك البيت", "wheat:البركات:t:jade", "drop:-"], None, None),
 ("deep-0", "أربع سنوات", "R", ["hourglass:أربع سنوات"], "مكث فيهم", None)]),
("sb-sharh", "sbsharh", 1, [
 ("face-0", "مرتين", "R", ["heart:-", "heart:-"], "شق الصدر", "مرتين"),
 ("face-1", "في بادية بني سعد", "R", ["mountain:بادية بني سعد", "hourglass:الرابعة من عمره"], "الأولى", None),
 ("face-2", "في ليلة الإسراء", "R", ["crescent:ليلة الإسراء", "star:-"], "والثانية", None),
 ("deep-0", "بماء زمزم", "F", ["heart:قلبه", "cup:طست من ذهب", "drop:بماء زمزم:t:jade"], None, None)]),
("sb-yutm", "sbyutm", 4, [
 ("face-0", "نشأ النبي ﷺ يتيما", "R", [NB, "txt:أبوه:x"], "يتيما", None),
 ("face-1", "ابن ست سنين", "R", ["txt:أمه آمنة:x", "hourglass:ست سنين"], None, None),
 ("face-2", "فوليه عمه أبو طالب", "F", ["txt:عبد المطلب", "txt:أبو طالب", "hourglass:ثماني سنين"], None, None),
 ("deep-0", "يؤثره على أولاده", "R", ["heart:شديد الرقة"], "جده", None)]),
("sb-hifz", "sbhifz", 2, [
 ("face-0", "على مكارم الأخلاق", "R", [NB, "crown:مكارم الأخلاق"], None, None),
 ("face-1", "لا يشرب الخمر", "T", ["txt:الخمر:x:ember", "txt:ما ذبح على النصب:x:ember", "txt:أعياد الأوثان:x:ember"], None, None),
 ("deep-0", "وشهد حلف الفضول", "R", ["house:دار ابن جدعان", "people:قبائل من قريش"], "حلف الفضول", None),
 ("deep-1", "حتى ترد عليه مظلمته", "F", ["people:-", "hands:مظلوما", "scale:ترد مظلمته:t:jade"], None, None)]),
("sb-kaaba", "sbkaaba", 0, [
 ("face-0", "لما بنت قريش الكعبة", "R", ["kaaba:الكعبة", NB, "txt:العباس"], None, None),
 ("face-1", "اجعل إزارك على رقبتك", "R", ["txt:العباس", "lips:اجعل إزارك على رقبتك"], None, None),
 ("face-2", "إزاري، إزاري", "R", ["lips:إزاري"], None, None),
 ("deep-1", "من حفظ الله له قبل البعثة", "R", ["lock:حفظ:t:jade", NB], None, None)]),
("sb-khadija", "sbkhadija", 1, [
 ("face-0", "تاجرا إلى الشام", "F", ["kaaba:مكة", "coin:مال خديجة", "mountain:الشام"], None, None),
 ("face-1", "من الأمانة والبركة", "R", ["lips:صدق حديثه", "key:الأمانة", "wheat:البركة"], None, None),
 ("face-2", "فعرضت عليه الزواج", "F", ["txt:خديجة", "txt:نفيسة بنت منبه", NB], None, None),
 ("deep-2", "وولدت له كل ولده إلا إبراهيم", "T", ["txt:القاسم", "txt:عبد الله", "txt:زينب", "txt:أم كلثوم", "txt:فاطمة", "txt:رقية"], None, None)]),
# ── الدعوة في مكة ──
("md-hira", "mdhira", 3, [
 ("face-0", "إلى غار حراء", "R", ["mountain:غار حراء", "crescent:رمضان"], "حبب إليه الخلاء", None),
 ("face-1", "ما أنا بقارئ", "V", ["txt:جبريل:t", "lips:ما أنا بقارئ"], "اقرأ", None),
 ("face-2", "ثلاث مرات", "R", ["book:أول آيات سورة العلق:t"], None, "ثلاث مرات"),
 ("deep-2", "فلا تصح نسبته", "V", ["book:البخاري:t:jade", "scroll:بلاغات الزهري:x:ember"], None, None)]),
("md-muddaththir", "mdmudd", 4, [
 ("face-0", "انقطع الوحي مدة", "R", ["hourglass:انقطع الوحي", "heart:التشوف"], None, None),
 ("face-1", "زملوني", "F", ["txt:الملك بين السماء والأرض", "house:خديجة", "lips:زملوني"], None, None),
 ("face-2", "أوائل سورة المدثر", "R", ["book:أوائل سورة المدثر:t"], None, "ثم حمي الوحي"),
 ("deep-1", "بالبلاغ والتحذير", "R", ["lips:بالبلاغ", "light:والتحذير", "heart:على نفسه"], "مبدأ رسالته", None)]),
("md-maratib", "mdmaratib", 1, [
 ("face-0", "الرؤيا الصادقة", "R", ["bed:الرؤيا الصادقة:t"], "أول مراتب الوحي", None),
 ("face-1", "في روعه وقلبه", "R", ["heart:في روعه وقلبه", "txt:يتمثل رجلا"], None, None),
 ("face-2", "صلصلة الجرس", "W", ["1:صلصلة الجرس"], None, "وهو أشده عليه"),
 ("deep-2", "ليلة المعراج", "R", ["star:فوق السماوات", "mat:فرض الصلاة"], "ليلة المعراج", None)]),
("md-sirr", "mdsirr", 0, [
 ("face-0", "بدأت الدعوة سرية", "R", ["lock:سرية", "kaaba:أهل مكة"], None, None),
 ("face-1", "على ألصق الناس به", "N", ["txt:خديجة", "txt:أبي بكر", "txt:زيد بن حارثة", NB], None, None),
 ("face-2", "الأمر بالصلاة", "R", ["mat:الأمر بالصلاة:t"], None, None),
 ("deep-2", "دار الأرقم بن أبي الأرقم", "R", ["house:دار الأرقم", "people:سرا"], None, None)]),
("md-jahr", "mdjahr", 2, [
 ("face-0", "العشيرة الأقربين", "R", ["people:العشيرة الأقربين"], "الجهر بالدعوة", None),
 ("face-1", "على الصفا", "F", ["mountain:الصفا", "lips:يا صباحاه", "people:بطون قريش"], None, None),
 ("deep-0", "ما جربنا عليك إلا صدقا", "R", ["people:قريش", "check:إلا صدقا:t:jade"], None, None),
 ("deep-1", "لا أغني عنك من الله شيئا", "V", ["coin:من مالي:t", "txt:فاطمة"], None, None)]),
("md-adha", "mdadha", 3, [
 ("face-0", "اشتد إيذاء الكفار", "R", ["flame:إيذاء::ember", "people:وأصحابه"], None, None),
 ("face-1", "وهو ساجد", "R", ["kaaba:عند البيت", "mat:وهو ساجد"], None, None),
 ("face-2", "حتى جاءت فاطمة", "F", ["txt:فاطمة", "check:فطرحته:t:jade"], None, None),
 ("deep-2", "نهارا جهارا", "R", ["sun:نهارا جهارا", "kaaba:بفناء الكعبة"], None, None)]),
("md-habasha", "mdhabasha", 4, [
 ("face-0", "لا يظلم أحد عنده", "F", ["kaaba:مكة", "crown:ملكا لا يظلم"], "أرض الحبشة", None),
 ("face-1", "أولهم عثمان وزوجته رقية", "R", ["txt:عثمان", "txt:رقية"], "الهجرة الأولى", None),
 ("face-2", "الهجرة الثانية", "R", ["people:اثنان وثمانون رجلا", "people:ثماني عشرة امرأة::rose"], None, None),
 ("deep-1", "وأعطاهم الأمان في أرضه", "V", ["txt:عمرو بن العاص:x", "crown:النجاشي:t:jade"], None, "الأمان")]),
("md-shib", "mdshib", 1, [
 ("face-0", "ألا يناكحوهم ولا يبايعوهم", "V", ["people:المشركون::ember", "people:بني هاشم وبني المطلب::jade"], None, None),
 ("face-1", "شعب أبي طالب", "R", ["scroll:صحيفة", "mountain:شعب أبي طالب"], None, None),
 ("face-2", "ومرت ثلاثة أعوام", "R", ["hourglass:ثلاثة أعوام", "scroll:نقضت:x"], None, None),
 ("deep-2", "عام الحزن", "R", ["txt:عام الحزن:x"], None, "لم يثبت")]),
("md-taif", "mdtaif", 2, [
 ("face-0", "إلى الطائف ماشيا", "F", ["kaaba:مكة", "mountain:الطائف"], None, "ومعه زيد بن حارثة"),
 ("face-1", "ردوا عليه ردا قاسيا", "R", ["people:ساداتهم::ember", "drop:الدماء::rose"], None, None),
 ("face-2", "فأجاب بالعفو عنهم", "V", ["mountain:الأخشبين:x", "heart:بالعفو:t:jade"], None, None),
 ("deep-0", "قبيلة قبيلة", "R", ["people:-", "people:-", "people:-"], "يعرض الإسلام على القبائل", None)]),
("md-aqaba", "mdaqaba", 0, [
 ("face-0", "أسلم ستة نفر من الخزرج", "R", ["people:ستة نفر من الخزرج::jade"], None, None),
 ("face-1", "أول سفير في الإسلام", "F", ["hands:اثنا عشر رجلا", "txt:مصعب بن عمير"], "العقبة الأولى", None),
 ("face-2", "على السمع والطاعة والنصرة", "F", ["hands:بضع وسبعون", "tree:الجنة:t:jade"], "العقبة الثانية", None),
 ("deep-0", "إلا وفيها رجال ونساء مسلمون", "R", ["house:-", "house:-", "house:-"], "دور الأنصار", None)]),
("md-hijra", "mdhijra", 4, [
 ("face-0", "في دار الندوة", "R", ["house:دار الندوة", "people:فتى من كل قبيلة::ember"], None, None),
 ("face-1", "أن يضطجع على فراشه", "V", ["bed:علي على فراشه", "eye:أخذ أبصارهم:x"], None, None),
 ("face-2", "إلى غار ثور", "F", ["mountain:غار ثور", "hourglass:ثلاث ليال", "house:المدينة"], None, None),
 ("deep-2", "على أبي أيوب الأنصاري", "R", ["mat:موضع مسجده", "house:أبي أيوب"], None, None)]),
# ── بناء المجتمع في المدينة ──
("mb-masjid", "mbmasjid", 1, [
 ("face-0", "بناء المسجد", "R", ["mat:المسجد:t"], "أول خطوة", None),
 ("face-1", "لغلامين يتيمين", "F", ["child:يتيمين", "coin:دفع ثمنه:t:jade"], None, None),
 ("face-2", "ينقل اللبن والحجارة", "R", [NB, "house:اللبن والحجارة"], None, None),
 ("deep-1", "أول وقف في الإسلام", "R", ["key:أول وقف"], None, None)]),
("mb-muakhat", "mbmuakhat", 3, [
 ("face-0", "بين المهاجرين والأنصار", "R", ["people:المهاجرين", "hands:-", "people:الأنصار::jade"], "المؤاخاة", None),
 ("face-1", "على المواساة", "R", ["heart:المواساة", "coin:والتوارث"], None, None),
 ("face-2", "رد الله التوارث إلى الرحم", "F", ["coin:التوارث", "house:الرحم"], None, None),
 ("deep-1", "وحدة إسلامية شاملة", "N", ["people:-", "people:-", "people:-", "chain:وحدة:t:jade"], None, None)]),
("mb-wathiqa", "mbwathiqa", 2, [
 ("face-0", "كان المسلمون قلة", "R", ["people:المسلمون::jade", "people:يهود", "people:مشركين"], None, None),
 ("face-1", "وكتب بينه وبينهم كتابا", "R", ["scroll:كتابا", "cross:لا يظاهروا عدوه", "shield:ينصروه:t:jade"], None, None),
 ("face-2", "مصالحة وحسن جوار", "R", ["house:-", "check:حسن جوار:t:jade", "house:-"], None, None),
 ("deep-0", "ولم يكره أحدا على دينه", "R", ["chain:إكراه:x:ember"], None, None)]),
("mb-ism", "mbism", 0, [
 ("face-0", "وأمر بتغييره إلى المدينة", "F", ["txt:يثرب:x", "txt:المدينة:t:jade"], None, None),
 ("face-1", "مثل طابة وطيبة", "T", ["txt:طابة", "txt:طيبة"], None, None),
 ("face-2", "حرمة كحرمة مكة", "V", ["kaaba:مكة", "house:المدينة"], "حرمة", None),
 ("deep-1", "إلى المدينة", "F", ["heart:الإيمان", "house:المدينة"], None, None)]),
("mb-sukkan", "mbsukkan", 4, [
 ("face-0", "عبد الله بن الزبير", "R", ["child:أول مولود", "txt:عبد الله بن الزبير"], None, None),
 ("face-1", "فحنكه بتمرة", "F", ["txt:أسماء", NB, "hands:دعا له"], None, None),
 ("deep-1", "ألا تحتسبون آثاركم", "V", ["mat:قرب المسجد:x", "house:آثاركم:t:jade"], "بني سلمة", None),
 ("deep-2", "أول إحصاء", "R", ["people:-", "scroll:أول إحصاء"], None, None)]),
("mb-idhn", "mbidhn", 1, [
 ("face-0", "لم تنقطع قريش عن تهديد المسلمين", "V", ["people:قريش::ember", "people:الأنصار::jade"], "تهديد", None),
 ("face-1", "نزل الإذن بالقتال", "R", ["book:الإذن بالقتال:t"], None, None),
 ("face-2", "حمزة بن عبد المطلب", "R", ["flag:أولى السرايا", "txt:حمزة"], None, None),
 ("deep-0", "أول قتيل في الإسلام", "R", ["txt:عبد الله بن جحش", "txt:عمرو بن الحضرمي:x"], None, "أول قتيل")]),
# ── الغزوات ──
("gz-badr", "gzbadr", 2, [
 ("face-0", "ثلاثمائة وثلاثة عشر رجلا", "R", ["crescent:رمضان", "people:313::jade"], "بدر", None),
 ("face-1", "والمشركون نحو ألف", "V", ["people:313::jade", "people:نحو ألف::ember"], None, None),
 ("face-2", "وأمد الله المؤمنين بالملائكة", "R", ["txt:الملائكة", "check:سبعون قتيلا", "lock:سبعون أسيرا"], None, None),
 ("deep-3", "القراءة والكتابة", "F", ["lock:الأسير", "pen:القراءة والكتابة", "child:عشرة"], None, None)]),
("gz-uhud", "gzuhud", 3, [
 ("face-0", "ثلاثة آلاف مقاتل", "R", ["people:قريش::ember", "hourglass:السنة الثالثة"], "ثلاثة آلاف", None),
 ("face-1", "خمسين راميا على الجبل", "R", ["mountain:خمسين راميا", "lock:لا يبرحوا:t"], None, None),
 ("face-2", "فالتف خالد بن الوليد", "F", ["mountain:نزلوا:x:ember", "txt:خالد بن الوليد"], None, None),
 ("deep-1", "واستشهد حمزة", "R", ["txt:حمزة", "star:شهيد"], None, None)]),
("gz-khandaq", "gzkhandaq", 0, [
 ("face-0", "عشرة آلاف مقاتل", "N", ["people:-", "people:-", "people:-", "house:المدينة"], "الأحزاب", None),
 ("face-1", "فأشار سلمان الفارسي بحفر خندق", "V", ["people:الأحزاب::ember", "trench:خندق:t:jade"], "سلمان الفارسي", None),
 ("face-2", "ريحا شديدة", "R", ["cloud:ريحا شديدة", "tent:خيامهم:x", "flame:نيرانهم:x"], None, None),
 ("deep-2", "امتحان نفوس واختبار قلوب", "R", ["heart:امتحان نفوس", "scale:اختبار قلوب"], None, None)]),
("gz-hudaybiya", "gzhuday", 4, [
 ("face-0", "يريد العمرة", "V", ["kaaba:العمرة", "cross:صدته قريش:x:ember"], "ألف وأربعمائة", None),
 ("face-1", "بايعه أصحابه تحت الشجرة", "R", ["tree:تحت الشجرة", "hands:ألا يفروا:t:jade"], "بيعة الرضوان", None),
 ("face-2", "عقد الصلح مع سهيل بن عمرو", "R", ["scroll:الصلح", "txt:سهيل بن عمرو"], None, None),
 ("deep-1", "ولن يضيعني الله أبدا", "R", ["txt:عمر", NB], None, None)]),
("gz-khaybar", "gzkhaybar", 1, [
 ("face-0", "خرج النبي ﷺ إلى خيبر", "F", ["house:المدينة", "house:خيبر"], None, None),
 ("face-1", "ثمانية حصون منيعة", "T", ["txt:ثمانية حصون"], None, "الصلح"),
 ("face-2", "عمرة القضاء", "R", ["kaaba:عمرة القضاء:t:jade", "hourglass:السنة السابعة"], None, None),
 ("deep-0", "فطلبها اليهود فردوها", "F", ["scroll:صحفا من التوراة", "hands:فردوها"], None, None)]),
("gz-mutah", "gzmutah", 3, [
 ("face-0", "ثلاثة آلاف إلى الشام", "F", ["house:المدينة", "mountain:الشام"], "ثلاثة آلاف", None),
 ("face-1", "فاستشهد الثلاثة", "R", ["txt:زيد", "txt:جعفر", "txt:عبد الله بن رواحة"], None, None),
 ("face-2", "أخذ الراية خالد بن الوليد", "R", ["flag:الراية", "txt:خالد بن الوليد"], None, None),
 ("deep-1", "فسمي ذا الجناحين", "R", ["txt:جعفر", "star:ذا الجناحين"], None, None)]),
("gz-fath", "gzfath", 0, [
 ("face-0", "نقضت قريش الصلح", "V", ["people:بني بكر::ember", "people:خزاعة::jade"], None, "نقضت الصلح"),
 ("face-1", "وأعطى الأمان", "T", ["txt:دار أبي سفيان", "txt:المسجد", "door:أغلق بابه"], "الأمان", None),
 ("face-2", "وكسر الأصنام", "R", ["kaaba:الكعبة", "cross:الأصنام:x:ember", "lips:بلال يؤذن"], None, None),
 ("deep-1", "سواعا", "T", ["txt:سواع:x", "txt:مناة:x"], None, None)]),
("gz-hunayn", "gzhunayn", 2, [
 ("face-0", "في اثني عشر ألفا", "R", ["people:اثنا عشر ألفا::jade", "people:هوازن وثقيف::ember"], None, None),
 ("face-1", "فاغتر بعض المسلمين بكثرتهم", "R", ["people:الكثرة", "mountain:الوادي", "cross:اضطربت:x"], None, None),
 ("face-2", "فثبت النبي ﷺ", "R", [NB, "txt:العباس", "people:فاجتمعوا::jade"], None, None),
 ("deep-2", "ليس بكثرة العدد والعدة", "V", ["people:العدد:x", "heart:الثبات:t:jade"], None, None)]),
("gz-tabuk", "gztabuk", 4, [
 ("face-0", "عزم الروم", "R", ["people:الروم::ember", "coin:الإنفاق"], None, None),
 ("face-1", "فتصدق أبو بكر بكل ماله", "T", ["txt:أبو بكر بكل ماله", "txt:عثمان", "txt:النساء بحليهن"], None, None),
 ("face-2", "في نحو ثلاثين ألفا", "F", ["people:ثلاثين ألفا::jade", "cross:رجعوا"], None, None),
 ("deep-0", "غزوة العسرة", "R", ["drop:الماء:x", "wheat:الزاد:x"], "العسرة", None)]),
# ── عام الوفود وحجة الوداع ──
("wd-wufud", "wdwufud", 1, [
 ("face-0", "عام الوفود", "N", ["people:-", "people:-", "people:-", "house:المدينة"], "عام الوفود", None),
 ("face-1", "ستين وفدا", "R", ["people:ستين وفدا"], None, None),
 ("face-2", "منها وفد تميم", "T", ["txt:تميم", "txt:عبد القيس", "txt:بني حنيفة", "txt:الأشعريين", "txt:دوس", "txt:طيئ"], None, None),
 ("deep-0", "وفد نجران", "R", ["people:نجران", "coin:الجزية"], None, None)]),
("wd-baraa", "wdbaraa", 3, [
 ("face-0", "وأمر أبا بكر على الحج", "R", ["kaaba:الحج", "txt:أبو بكر"], None, None),
 ("face-1", "فأرسل عليا", "F", ["book:سورة براءة", "txt:علي", "people:الناس"], None, None),
 ("face-2", "ولا يحج بعد العام مشرك", "T", ["txt:عريان:x:ember", "txt:مشرك:x:ember"], None, None),
 ("deep-0", "فعهده إلى مدته", "R", ["scroll:عهد", "hourglass:إلى مدته"], None, None)]),
("wd-wada", "wdwada", 0, [
 ("face-0", "قارنا بين العمرة والحج", "R", ["mountain:ذي الحليفة", "kaaba:العمرة والحج"], None, None),
 ("face-1", "ووقف بعرفة", "R", ["mountain:عرفة", "lips:فخطب", "hands:يدعو"], None, None),
 ("face-2", "لأنه لم يحج بعدها", "R", ["kaaba:حجة الوداع:t"], None, None),
 ("deep-2", "أسامة بن زيد", "F", ["flag:جيش", "txt:أسامة بن زيد", "mountain:الشام"], None, None)]),
("wd-marad", "wdmarad", 2, [
 ("face-0", "في بيت عائشة", "R", ["house:بيت عائشة", "bed:مرض"], None, None),
 ("face-1", "أمر أبا بكر أن يصلي بالناس", "R", ["mat:الصلاة", "txt:أبو بكر:t"], None, None),
 ("face-2", "أبرز إشارة إلى خلافته", "R", ["crown:خلافته", "txt:أبو بكر"], None, None),
 ("deep-1", "فاختار ما عنده", "V", ["coin:الدنيا", "star:ما عنده:t:jade"], None, None)]),
("wd-wafat", "wdwafat", 4, [
 ("face-0", "الثاني عشر من ربيع الأول", "R", ["sun:يوم الاثنين", "crescent:ربيع الأول", "hourglass:السنة الحادية عشرة"], None, None),
 ("face-1", "اللهم في الرفيق الأعلى", "R", [NB, "star:الرفيق الأعلى"], None, None),
 ("face-2", "فإن الله حي لا يموت", "V", ["txt:من كان يعبد محمدا", "txt:الله حي لا يموت:t:jade"], "أبو بكر", None),
 ("deep-1", "ولم تنته رسالته", "R", ["book:رسالته:t:jade", "hourglass:إلى يوم الدين"], None, None)]),
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
open(f"{R}/app/src/main/java/app/usulcards/ui/ArtWave95.kt", "w", encoding="utf-8").write(
"package app.usulcards.ui\n\nimport androidx.compose.ui.graphics.drawscope.DrawScope\n\n/*\n * Wave 95 (2026-11-13): v1.5, the 39 Sira cards of batches 21 to 25, as boards (see ArtKit94.kt). Each beat names what it shows\n * with the card's own words. The Prophet ﷺ is only a named light; companions, Jibril and the angels are name tiles,\n * never drawn. Generated from the table in tools/scenes/w95_table.py; edit the table, not this file.\n */\n\n" + "\n".join(kt))
open("/home/claude/sc95/w95.py", "w", encoding="utf-8").write("SC = '''\n    # ── v1.5: the Sira, batches 21 to 25 (wave 95) ──\n" + "\n".join(sc) + "\n'''\nMETA = {\n" + "\n".join(meta) + "\n}\n")
print(len(S), "scenes,", sum(len(b) for _, _, _, b in S), "beats")
