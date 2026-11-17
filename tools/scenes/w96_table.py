# Table of the 24 board scenes of wave 96 (v1.5, the Sira part 2) -> ArtWave95.kt and w95.py
# CODEX: the Prophet ﷺ is only a named light; companions and Jibril are name tiles; no angel, animal or body drawn.
M = dict(R="ROW94", F="FLOW94", V="VS94", N="FAN94", T="TILES94", C="TRACT94", W="WAVE94")
TONE = dict(gold="Qn.Gold", jade="Qn.Jade", ember="Qn.Ember", rose="Qn.Rose", light="Qn.GoldLight")
NB = "light:النَّبِيُّ ﷺ:t"
NB = "light:النَّبِيُّ ﷺ:t"
S = [
# ── أمهات المؤمنين ──
("um-khasais", "umkhasais", 0, [
 ("face-0", "أمهات المؤمنين", "R", ["crown:أمهات المؤمنين", "txt:تشريفا لهن"], None, None),
 ("face-1", "في وجوب التعظيم والإجلال", "R", ["star:التعظيم", "crown:والإجلال", "lock:تحريم نكاحهن"], None, None),
 ("face-2", "ولسن كالأمهات في المحرمية", "V", ["check:التعظيم:t:jade", "cross:المحرمية:x"], None, None),
 ("deep-1", "المطهرات المبرآت من كل سوء", "R", ["drop:المطهرات", "star:المبرآت"], "الترضي عنهن", None)]),
("um-sawda", "umsawda", 1, [
 ("face-0", "وهاجرا إلى الحبشة", "F", ["kaaba:مَكَّةُ", "crown:الحبشة"], "سودة", None),
 ("face-1", "فكانت أول امرأة تزوجها بعدها", "R", ["txt:خديجة", "txt:سودة:t"], None, None),
 ("face-2", "وهبت يومها وليلتها لعائشة", "F", ["txt:سودة", "sun:يومها", "txt:عائشة"], None, None),
 ("deep-1", "حب النبي ﷺ", "R", ["heart:آثرت", NB], None, None)]),
("um-aisha", "umaisha", 2, [
 ("face-0", "عقد عليها بمكة", "F", ["kaaba:بمكة", "house:المدينة"], "عائشة", None),
 ("face-1", "هذا جبريل يقرئك السلام", "R", ["txt:جبريل", "lips:يقرئك السلام"], None, None),
 ("face-2", "أفقه نساء الأمة", "R", ["book:أفقه نساء الأمة:t", "pen:تبلغ عنه"], None, None),
 ("deep-0", "كفضل الثريد على سائر الطعام", "V", ["cup:الثريد:t:jade", "wheat:سائر الطعام"], None, None)]),
("um-hafsa", "umhafsa", 3, [
 ("face-0", "فتوفي عنها بعد بدر", "R", ["txt:خنيس بن حذافة", "star:بعد بدر"], "حفصة", None),
 ("face-1", "في السنة الثالثة للهجرة", "R", [NB, "hourglass:السنة الثالثة"], None, None),
 ("face-2", "فإنها صوامة قوامة", "T", ["txt:صوامة", "txt:قوامة"], "راجعها", None),
 ("deep-0", "وكانت الصحف التي جمعها أبو بكر عند حفصة", "F", ["scroll:الصحف", "txt:حفصة", "book:مصحف واحد"], None, None)]),
("um-zaynab-k", "umzaynabk", 4, [
 ("face-0", "تدعى أم المساكين", "R", ["hands:أم المساكين:t:jade", "wheat:معروفها"], "زينب بنت خزيمة", None),
 ("face-1", "وكانت تحت عبد الله بن جحش", "F", ["txt:عبد الله بن جحش", NB], None, None),
 ("face-2", "شهرين أو ثلاثة", "R", ["hourglass:شهرين أو ثلاثة"], None, None),
 ("deep-1", "في ربيع الآخر سنة أربع", "R", ["crescent:ربيع الآخر", "hourglass:سنة أربع"], None, None)]),
("um-salama", "umsalama", 0, [
 ("face-0", "إلى الحبشة، ثم إلى المدينة", "F", ["crown:الحبشة", "house:المدينة"], "أم سلمة", None),
 ("face-1", "وأخلف لي خيرا منها", "F", ["hands:اللهم أجرني", NB], None, None),
 ("face-2", "أن ينحر ويحلق", "F", ["lips:أشارت", "people:فتبعوه::jade"], "يوم الحديبية", None),
 ("deep-1", "مقتل الحسين", "R", ["txt:الحسين", "drop:فحزنت::rose"], None, None)]),
("um-zaynab-j", "umzaynabj", 1, [
 ("face-0", "تحت زيد بن حارثة", "R", ["txt:زَيْنَبُ", "txt:زيد بن حارثة"], None, None),
 ("face-1", "من أحكام التبني", "V", ["scroll:التبني:x:ember", "book:أَبْطَلَ:t:jade"], None, None),
 ("face-2", "من فوق سبع سماوات", "V", ["people:زوجكن أهاليكن", "star:من فوق سبع سماوات:t"], None, None),
 ("deep-2", "أطولهن يدا بالصدقة", "R", ["hands:أطولهن يدا", "coin:بالصدقة"], None, None)]),
("um-juwayriya", "umjuway", 2, [
 ("face-0", "سبية في غزوة بني المصطلق", "R", ["txt:جويرية", "lock:سبية"], None, None),
 ("face-1", "فأداها عنها وتزوجها", "F", ["scroll:كتابتها", "coin:فأداها", NB], None, None),
 ("face-2", "مائة أهل بيت", "N", ["house:-", "house:-", "house:-", "key:مائة أهل بيت:t:jade"], "أصهار رسول الله ﷺ", None),
 ("deep-0", "وكان اسمها برة", "F", ["txt:برة", "txt:جويرية:t"], None, None)]),
("um-habiba", "umhabiba", 3, [
 ("face-0", "إلى الحبشة", "F", ["kaaba:مَكَّةُ", "crown:الحبشة"], "أم حبيبة", None),
 ("face-1", "وأصدقها عنه أربعمائة دينار", "R", ["crown:النجاشي", "coin:أربعمائة دينار"], None, None),
 ("face-2", "طوت عنه فراش رسول الله ﷺ", "V", ["txt:أبو سفيان", "bed:فراش رسول الله ﷺ:t"], None, None),
 ("deep-1", "سنة أربع وأربعين", "R", ["hourglass:سنة أربع وأربعين"], None, None)]),
("um-safiyya", "umsafiyya", 4, [
 ("face-0", "ووقعت في سبي خيبر", "R", ["txt:صفية", "house:خيبر"], "ذرية هارون عليه السلام", None),
 ("face-1", "وجعل عتقها صداقها", "F", ["key:أعتقها", "coin:صداقها"], None, None),
 ("face-2", "فصار ذلك سنة للأمة", "R", ["book:سنة للأمة:t"], None, None),
 ("deep-1", "سنة خمسين", "R", ["hourglass:سنة خمسين"], None, None)]),
("um-maymuna", "ummaymuna", 0, [
 ("face-0", "في سفره لعمرة القضاء", "R", ["kaaba:عمرة القضاء", "hourglass:السنة السابعة"], None, None),
 ("face-1", "فسماها النبي ﷺ ميمونة", "F", ["txt:برة", "txt:ميمونة:t"], None, None),
 ("face-2", "وهي خالة ابن عباس وخالد بن الوليد", "R", ["txt:ابن عباس", "txt:ميمونة", "txt:خالد بن الوليد"], None, None),
 ("deep-1", "وماتت بسرف", "R", ["mountain:بسرف"], None, None)]),
# ── تعامله ﷺ مع الناس ──
("tm-ahl", "tmahl", 1, [
 ("face-0", "جميل المعاشرة، دائم البشر", "R", [NB, "heart:جميل المعاشرة", "sun:دائم البشر"], None, None),
 ("face-1", "كان يكون في مهنة أهله", "F", ["house:مهنة أهله", "mat:الصلاة"], None, None),
 ("face-2", "وكان يخيط ثوبه", "T", ["txt:يخيط ثوبه", "txt:يخصف نعله"], None, None),
 ("deep-0", "وما ضرب شيئا قط بيده", "R", ["hands:ما ضرب:x"], None, None)]),
("tm-wafa", "tmwafa", 2, [
 ("face-0", "يكثر ذكر خديجة بعد وفاتها", "R", ["heart:يكثر ذكر", "txt:خديجة"], None, None),
 ("face-1", "في صدائق خديجة", "N", ["people:-", "people:-", "people:-", "heart:صدائق خديجة"], None, None),
 ("face-2", "أحب الناس إليه", "R", ["heart:أحب الناس إليه", "txt:عائشة"], None, None),
 ("deep-1", "ولم يتزوج عليها غيرها", "R", ["txt:خديجة", "lock:حتى ماتت"], None, None)]),
("tm-banat", "tmbanat", 3, [
 ("face-0", "كان للنبي ﷺ أربع بنات", "T", ["txt:زَيْنَبُ", "txt:رُقَيَّةُ", "txt:أُمُّ كُلْثُومٍ", "txt:فاطمة:t"], None, None),
 ("face-1", "مرحبا بابنتي", "R", [NB, "lips:مرحبا بابنتي", "txt:فاطمة"], None, None),
 ("face-2", "فهذا خير لكما من خادم", "V", ["star:التسبيح:t:jade", "people:خادم"], None, None),
 ("deep-0", "أنقذي نفسك من النار", "R", ["flame:من النار::ember", "txt:فاطمة"], None, None)]),
("tm-ahfad", "tmahfad", 4, [
 ("face-0", "منهم الحسن والحسين", "R", ["txt:الحسن", "txt:الحسين"], "ثمانية من الأحفاد", None),
 ("face-1", "فأذن في أذن الحسن حين ولد", "T", ["txt:أذن", "txt:وعق", "txt:الأسماء الحسنة"], None, None),
 ("face-2", "كخ كخ", "V", ["coin:الصدقة:x:ember", "lips:كخ كخ"], None, None),
 ("deep-1", "بل هو حسن", "F", ["txt:حربا:x", "txt:حسن:t:jade"], None, None)]),
("tm-jar", "tmjar", 0, [
 ("face-0", "جيران سوء", "R", ["house:جيران سوء::ember", "mat:وهو يصلي"], None, None),
 ("face-1", "ما زال جبريل يوصيني بالجار", "F", ["txt:جبريل", "house:بالجار:t:jade"], None, None),
 ("face-2", "تشمل المسلم وغير المسلم", "R", ["house:المسلم", "house:وغير المسلم"], None, None),
 ("deep-1", "احتمال الأذى", "V", ["cross:كف الأذى", "heart:احتمال الأذى:t:jade"], None, None)]),
("tm-khadam", "tmkhadam", 1, [
 ("face-0", "معاملة الوالد الشفوق لولده", "R", [NB, "heart:الوالد الشفوق"], None, None),
 ("face-1", "أن يطعمهم مما يأكل", "R", ["cup:مما يأكل", "scroll:مما يلبس"], None, None),
 ("face-2", "ألك حاجة؟", "R", ["lips:ألك حاجة؟"], None, None),
 ("deep-0", "يا أنيس", "R", ["txt:أنيس", "sun:وهو يضحك"], None, None)]),
("tm-bala", "tmbala", 2, [
 ("face-0", "أهل البلاء وأصحاب العاهات", "R", ["heart:أرق الناس", "hands:أهل البلاء"], None, None),
 ("face-1", "إلى البصير نعوده", "V", ["eye:أعمى:x", "eye:البصير:t:jade"], None, None),
 ("face-2", "دلوني على قبرها", "R", ["mat:تقم المسجد", "check:فصلى عليها:t:jade"], None, None),
 ("deep-0", "شر الطعام طعام الوليمة", "V", ["people:الأغنياء", "people:الفقراء:x"], None, None)]),
("tm-kuffar", "tmkuffar", 3, [
 ("face-0", "على العدل وعدم الظلم", "R", ["scale:العدل:t:jade"], None, None),
 ("face-1", "من قتل معاهدا", "V", ["scroll:معاهدا", "tree:رائحة الجنة:x"], None, None),
 ("face-2", "الحمد لله الذي أنقذه من النار", "F", ["bed:غلاما يهوديا", "check:فأسلم:t:jade"], None, None),
 ("deep-0", "وإنما بعثت رحمة", "V", ["lips:لعانا:x", "heart:رحمة:t:jade"], None, None)]),
("tm-jadid", "tmjadid", 4, [
 ("face-0", "يفرح بإسلام من أسلم", "R", ["sun:يفرح", "txt:عكرمة", "txt:عدي بن حاتم"], None, None),
 ("face-1", "علمه الصلاة", "F", ["mat:الصلاة", "hands:اللهم اغفر لي"], None, None),
 ("face-2", "ولو بالمال", "R", ["coin:بالمال", "heart:يتألفهم"], None, None),
 ("deep-1", "ارجعوا إلى أهليكم فعلموهم", "F", ["people:مالك بن الحويرث", "house:أهليكم"], None, None)]),
("tm-usat", "tmusat", 0, [
 ("face-0", "رفيقا رحيما بالعصاة", "R", ["heart:رفيقا رحيما"], None, None),
 ("face-1", "فأدناه وحاوره برفق", "R", [NB, "lips:حاوره برفق", "people:شاب"], None, None),
 ("face-2", "ثم وضع يده عليه ودعا له", "R", ["hands:ودعا له:t:jade"], None, None),
 ("deep-2", "ما بال أقوام", "V", ["people:أقوام", "lips:دون التصريح"], None, None)]),
("tm-nisa", "tmnisa", 1, [
 ("face-0", "ألا واستوصوا بالنساء خيرا", "R", ["kaaba:حجة الوداع", "lips:استوصوا بالنساء خيرا"], None, None),
 ("face-1", "إنما النساء شقائق الرجال", "V", ["people:النساء::rose", "people:الرجال"], "شقائق", None),
 ("face-2", "رفقا بالقوارير", "R", ["cup:القوارير:t:light"], None, None),
 ("deep-1", "إذا سمع بكاء الصبي", "F", ["child:بكاء الصبي", "mat:يخفف صلاته"], None, None)]),
("tm-kibar", "tmkibar", 2, [
 ("face-0", "كالأطفال والنساء وكبار السن", "T", ["txt:الأطفال", "txt:النساء", "txt:كبار السن"], "الضعفاء", None),
 ("face-1", "ليس منا من لم يرحم صغيرنا", "V", ["child:صغيرنا", "people:كبيرنا"], None, None),
 ("face-2", "كبر كبر", "R", ["lips:كبر كبر"], None, None),
 ("deep-1", "ثم أجلسه بين يديه", "R", ["txt:أبي قحافة", "check:فأسلم:t:jade"], None, None)]),
("tm-sighar", "tmsighar", 3, [
 ("face-0", "شديد الحب للصغار", "R", ["heart:شديد الحب", "child:للصغار"], None, None),
 ("face-1", "يا أبا عمير، ما فعل النغير؟", "R", ["child:أبا عمير", "lips:ما فعل النغير؟"], None, None),
 ("face-2", "ويمسح رءوسهم", "R", ["hands:يمسح رءوسهم", "child:صبيانهم"], None, None),
 ("deep-1", "فعلمه برفق", "F", ["tree:نخل الأنصار", "child:غلام", "heart:برفق"], None, None)]),
]

# Cards redrawn in the illustrated style (wave 102 on): their scene function lives in ArtWave102+, not here.
REDRAWN = {"tm-ahl", "tm-wafa", "tm-banat", "tm-ahfad", "tm-jar", "tm-khadam", "tm-bala", "tm-kuffar", "tm-jadid", "tm-usat", "tm-nisa", "tm-kibar", "tm-sighar", "um-khasais", "um-sawda", "um-aisha", "um-hafsa", "um-zaynab-k", "um-salama", "um-zaynab-j", "um-juwayriya", "um-habiba", "um-safiyya", "um-maymuna"}
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
    if cid not in REDRAWN: kt.append(f"private val B_{lay.upper()} = listOf(\n" + "\n".join(kb) + f"\n)\n\ninternal fun DrawScope.{lay}Scene(a: ArtCtx) = board94(a, {sky}, B_{lay.upper()})\n")
    sc.append(f'    "{cid}": S("art", [\n' + "\n".join(rows) + f'\n    ], layout="{lay}"),')
    meta.append(f'    "{lay}": dict({"figure=True, " if fig else ""}kinds={kinds!r}),')
R = "/home/claude/androidapp"
open(f"{R}/app/src/main/java/app/usulcards/ui/ArtWave96.kt", "w", encoding="utf-8").write(
"package app.usulcards.ui\n\nimport androidx.compose.ui.graphics.drawscope.DrawScope\n\n/*\n * Wave 96 (2026-11-15): v1.5, the 24 Sira cards of batches 26 and 27 (Mothers of the Believers, his dealings), as boards (see ArtKit94.kt). Each beat names what it shows\n * with the card's own words. The Prophet ﷺ is only a named light; companions, Jibril and the angels are name tiles,\n * never drawn. Generated from the table in tools/scenes/w96_table.py; edit the table, not this file.\n */\n\n" + "\n".join(kt))
open("/home/claude/sc96/w96.py", "w", encoding="utf-8").write("SC = '''\n    # ── v1.5: the Sira, batches 26 and 27 (wave 96) ──\n" + "\n".join(sc) + "\n'''\nMETA = {\n" + "\n".join(meta) + "\n}\n")
print(len(S), "scenes,", sum(len(b) for _, _, _, b in S), "beats")
