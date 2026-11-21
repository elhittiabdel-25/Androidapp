package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 64 (2026-11-03): «الأربعون النووية», hadiths 36–40. Lifting a brother's burden; good and bad deeds written in the ledger;
 * the friends of Allah; what is pardoned; be in the world like a stranger. Allah is never drawn; the Prophet ﷺ is only light;
 * companions are names in roundels; others are generic figures or plain roundels.
 */

private val Back64 = Color(0xE6181420)

// ═════════════ 1. lifting a burden (nw-36) ═════════════

private object ReliefC64 {
    val Night = Color(0xFF141A2C)
    val NightLow = Color(0xFF30344E)
    val Ground = Color(0xFF1E2030)
    val Gold = Color(0xFFF2C860)
    val Weight = Color(0xFF3A3040)
    val Big = Color(0xFF241C2A)
    val Sand = Color(0xFFB89A72)
    val Cloak = Color(0xFF5A6A8A)
    val Person = Color(0xFFE2DACA)
    val Page = Color(0xFFF2E8D2)
    val Book = Color(0xFF2E5A4A)
    val Garden = Color(0xFF3E7A54)
    val Road = Color(0xFF6A6280)
}

/**
 * Beats: liftweight (one roundel lifts a dark weight off another; far off, a much larger weight is lifted off him), smallvsbig (all
 * the small weights heaped together are still less than one great one), helpedhelper (one helps another carry a load, and light
 * from above helps him in turn: «وَاللَّهُ فِي عَوْنِ الْعَبْدِ مَا كَانَ الْعَبْدُ فِي عَوْنِ أَخِيهِ»), givetime (a debtor and a
 * creditor; the days pass in an hourglass while he waits, then the bill is torn: «وَيُمْهِلُهُ حَتَّى يَجِدَ»), cloaked (a cloak
 * covers a dark spot on one roundel, and a cloak of light covers the one who covered it: «سَتَرَهُ اللَّهُ فِي الدُّنْيَا
 * وَالْآخِرَةِ»), knowledgeroad (a figure walks with a book; the road under him lights up toward a garden: «سَهَّلَ اللَّهُ لَهُ بِهِ
 * طَرِيقًا إِلَى الْجَنَّةِ»).
 */
internal fun DrawScope.relievegriefScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "liftweight" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "smallvsbig" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "helpedhelper" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "givetime" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "cloaked" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "knowledgeroad" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { reliefNight(a) }
}

private fun DrawScope.reliefNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val lw = a.b("liftweight")
    val sb = a.b("smallvsbig")
    val hh = a.b("helpedhelper")
    val gt = a.b("givetime")
    val ck = a.b("cloaked")
    val kr = a.b("knowledgeroad")
    val ground = h * 0.86f
    nightSky(t, ReliefC64.Night, ReliefC64.NightLow, ground, starAlpha = 0.85f)
    drawRect(ReliefC64.Ground, Offset(0f, ground), Size(w, h - ground))
    if (lw > 0f) {
        val f = easeOut(seg(lw, 0f, BEAT_IN)) * (1f - seg(sb, 0f, BEAT_OUT))
        val a1 = Offset(w * 0.7f, h * 0.65f)
        val b1 = Offset(w * 0.45f, h * 0.65f)
        nameRoundel(a1, w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(b1, w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val lift = easeInOut(seg(lw, 0.1f, 0.4f))
        drawRect(ReliefC64.Weight, Offset(b1.x - w * 0.04f, b1.y - h * 0.11f - h * 0.15f * lift), Size(w * 0.08f, h * 0.05f), alpha = f * (1f - seg(lw, 0.35f, 0.45f)))
        softGlow(b1, w * 0.08f, ReliefC64.Gold, 0.4f * f * seg(lw, 0.35f, 0.5f))
        val far = Offset(w * 0.15f, h * 0.3f)
        val lift2 = easeInOut(seg(lw, 0.55f, 0.85f))
        drawRect(ReliefC64.Big, Offset(far.x - w * 0.09f, far.y - h * 0.06f - h * 0.2f * lift2), Size(w * 0.18f, h * 0.1f), alpha = f * seg(lw, 0.45f, 0.55f) * (1f - seg(lw, 0.8f, 0.9f)))
        drawLine(ReliefC64.Gold, a1 + Offset(-w * 0.03f, -h * 0.04f), far + Offset(w * 0.05f, h * 0.06f), 1.5f * dp, alpha = f * 0.4f * seg(lw, 0.5f, 0.6f))
    }
    if (sb > 0f) {
        val f = easeOut(seg(sb, 0f, BEAT_IN)) * (1f - seg(hh, 0f, BEAT_OUT))
        for (k in 0 until 12) drawRect(ReliefC64.Weight, Offset(w * (0.6f + 0.05f * (k % 4)), ground - h * (0.04f + 0.04f * (k / 4))), Size(w * 0.04f, h * 0.035f), alpha = f * seg(sb, 0.05f + 0.03f * k, 0.12f + 0.03f * k))
        val g = seg(sb, 0.5f, 0.75f)
        drawRect(ReliefC64.Big, Offset(w * 0.08f, ground - h * 0.45f * g), Size(w * 0.38f, h * 0.45f * g), alpha = f)
    }
    if (hh > 0f) {
        val f = easeOut(seg(hh, 0f, BEAT_IN)) * (1f - seg(gt, 0f, BEAT_OUT))
        val l = Offset(w * 0.6f, ground)
        val r = Offset(w * 0.4f, ground)
        figure(l, h * 0.3f, ReliefC64.Person, f, arms = 0.9f)
        figure(r, h * 0.3f, ReliefC64.Person, f, arms = -0.9f)
        drawRect(ReliefC64.Sand, Offset(w * 0.42f, ground - h * 0.36f), Size(w * 0.16f, h * 0.06f), alpha = f)
        lightShafts(Offset(w * 0.5f, 0f), PI.toFloat() / 2f, 0.25f, ground - h * 0.38f, ReliefC64.Gold, 0.45f * f * seg(hh, 0.4f, 0.7f), t, rays = 5)
        tagAt(a.tag("helpedhelper"), Offset(w * 0.5f, h * 0.18f), f * seg(hh, 0.2f, 0.4f), back = Back64, ink = ArtInk.Gold)
    }
    if (gt > 0f) {
        val f = easeOut(seg(gt, 0f, BEAT_IN)) * (1f - seg(ck, 0f, BEAT_OUT))
        nameRoundel(Offset(w * 0.78f, h * 0.6f), w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(Offset(w * 0.22f, h * 0.6f), w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val hg = Offset(w * 0.5f, h * 0.5f)
        val sand = seg(gt, 0.1f, 0.65f)
        drawPath(Path().apply { moveTo(hg.x - w * 0.06f, hg.y - h * 0.1f); lineTo(hg.x + w * 0.06f, hg.y - h * 0.1f); lineTo(hg.x, hg.y); lineTo(hg.x + w * 0.06f, hg.y + h * 0.1f); lineTo(hg.x - w * 0.06f, hg.y + h * 0.1f); lineTo(hg.x, hg.y); close() }, ReliefC64.Page, alpha = f * 0.8f, style = Stroke(2f * dp))
        drawPath(Path().apply { moveTo(hg.x - w * 0.05f * (1f - sand), hg.y - h * 0.08f * (1f - sand)); lineTo(hg.x + w * 0.05f * (1f - sand), hg.y - h * 0.08f * (1f - sand)); lineTo(hg.x, hg.y); close() }, ReliefC64.Sand, alpha = f)
        drawPath(Path().apply { moveTo(hg.x - w * 0.05f * sand, hg.y + h * 0.09f); lineTo(hg.x + w * 0.05f * sand, hg.y + h * 0.09f); lineTo(hg.x, hg.y + h * 0.09f - h * 0.07f * sand); close() }, ReliefC64.Sand, alpha = f)
        val tear = seg(gt, 0.7f, 0.85f)
        val bill = Offset(w * 0.78f, h * 0.38f)
        for (k in 0 until 2) {
            val dir = if (k == 0) -1f else 1f
            drawRect(ReliefC64.Page, Offset(bill.x - w * 0.04f + (if (k == 1) w * 0.04f else 0f) + dir * w * 0.03f * tear, bill.y - h * 0.04f + h * 0.04f * tear), Size(w * 0.04f, h * 0.08f), alpha = f * (1f - 0.6f * tear))
        }
        tagAt(a.tag("givetime"), Offset(w * 0.5f, h * 0.2f), f * seg(gt, 0.2f, 0.4f), back = Back64, ink = ArtInk.Gold)
    }
    if (ck > 0f) {
        val f = easeOut(seg(ck, 0f, BEAT_IN)) * (1f - seg(kr, 0f, BEAT_OUT))
        val l = Offset(w * 0.68f, h * 0.55f)
        val r = Offset(w * 0.34f, h * 0.55f)
        nameRoundel(l, w * 0.06f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(r, w * 0.06f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        drawCircle(ReliefC64.Weight, w * 0.02f, r + Offset(w * 0.01f, h * 0.01f), alpha = f * (1f - seg(ck, 0.3f, 0.4f)))
        val cover = seg(ck, 0.15f, 0.4f)
        drawArc(ReliefC64.Cloak, 180f, 180f, true, Offset(r.x - w * 0.08f, r.y - w * 0.08f), Size(w * 0.16f, w * 0.16f * cover), alpha = f * 0.9f)
        val cover2 = seg(ck, 0.5f, 0.8f)
        drawArc(ReliefC64.Gold, 200f, 140f, false, Offset(l.x - w * 0.12f, l.y - w * 0.12f), Size(w * 0.24f, w * 0.24f), alpha = f * cover2, style = Stroke(5f * dp, cap = StrokeCap.Round))
        softGlow(l, w * 0.12f, ReliefC64.Gold, 0.4f * f * cover2)
        tagAt(a.tag("cloaked"), Offset(w * 0.5f, h * 0.22f), f * seg(ck, 0.55f, 0.75f), back = Back64, ink = ArtInk.Gold)
    }
    if (kr > 0f) {
        val f = easeOut(seg(kr, 0f, 0.25f))
        val p0 = Offset(w * 0.95f, ground)
        val p3 = Offset(w * 0.12f, h * 0.45f)
        drawLine(ReliefC64.Road, p0, p3, h * 0.025f, StrokeCap.Round, alpha = f)
        val u = easeInOut(seg(kr, 0.1f, 0.85f))
        drawLine(ReliefC64.Gold, p0, p0 + (p3 - p0) * u, h * 0.01f, StrokeCap.Round, alpha = f * 0.8f)
        val foot = p0 + (p3 - p0) * u
        figure(foot, h * mix(0.3f, 0.16f, u), ReliefC64.Person, f, walk = t * 5f)
        openBook21(foot + Offset(w * 0.03f, -h * mix(0.2f, 0.1f, u)), w * 0.06f, ReliefC64.Page, ReliefC64.Book, f)
        drawRect(ReliefC64.Garden, Offset(p3.x - w * 0.1f, p3.y - h * 0.12f), Size(w * 0.2f, h * 0.12f), alpha = f * seg(kr, 0.5f, 0.8f))
        softGlow(p3 + Offset(0f, -h * 0.06f), w * 0.12f, ReliefC64.Gold, 0.5f * f * seg(kr, 0.6f, 0.9f))
        tagAt(a.tag("knowledgeroad"), Offset(w * 0.55f, h * 0.2f), f * seg(kr, 0.45f, 0.65f), back = Back64, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. the ledger (nw-37, notebook) ═════════════

/**
 * Beats: twocolumns (a ledger with two columns: «الْحَسَنَاتِ», «وَالسَّيِّئَاتِ»), multiply (an intention bubble writes one good
 * deed; done, it becomes ten, then seven hundred: «حَسَنَةً كَامِلَةً», «عَشْرًا», «سَبْعِمِئَةِ ضِعْفٍ»), badleft (a bad intention
 * dropped is written as a good deed; one done is written once: «كُتِبَتْ لَهُ حَسَنَةً كَامِلَةً», «سَيِّئَةً وَاحِدَةً»), camelseven
 * (one camel with a rein, and seven hundred small ones drawn after it), mercywins (one mark on one side, a tall stack on the other:
 * «رَحْمَةَ اللَّهِ غَلَبَتْ غَضَبَهُ»).
 */
internal fun DrawScope.recordbookScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "twocolumns" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "multiply" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "badleft" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "camelseven" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "mercywins" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0f) { ledgerPage(a) }
}

private fun DrawScope.ledgerPage(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pp = Paper(a.dark)
    paperSheet(pp)
    val tc = a.b("twocolumns")
    val mu = a.b("multiply")
    val bl = a.b("badleft")
    val cs = a.b("camelseven")
    val mw = a.b("mercywins")
    val pen = 2.2f * dp
    if (tc > 0f) {
        val f = easeOut(seg(tc, 0f, BEAT_IN)) * (1f - seg(mu, 0f, BEAT_OUT))
        penLine(listOf(Offset(w * 0.5f, h * 0.2f), Offset(w * 0.5f, h * 0.88f)), seg(tc, 0.05f, 0.25f), pp.ink, pen, f)
        penLine(listOf(Offset(w * 0.1f, h * 0.3f), Offset(w * 0.9f, h * 0.3f)), seg(tc, 0.1f, 0.3f), pp.ink, pen, f)
        note(a.tag("twocolumns", 0, 0), Offset(w * 0.72f, h * 0.24f), f * seg(tc, 0.2f, 0.4f), pp, pp.good)
        note(a.tag("twocolumns", 0, 1), Offset(w * 0.28f, h * 0.24f), f * seg(tc, 0.4f, 0.6f), pp, pp.bad)
        for (k in 0 until 4) penLine(listOf(Offset(w * 0.6f, h * (0.4f + 0.1f * k)), Offset(w * 0.85f, h * (0.4f + 0.1f * k))), seg(tc, 0.5f + 0.08f * k, 0.6f + 0.08f * k), pp.faint, pen * 0.7f, f)
    }
    if (mu > 0f) {
        val f = easeOut(seg(mu, 0f, BEAT_IN)) * (1f - seg(bl, 0f, BEAT_OUT))
        val bubble = Offset(w * 0.8f, h * 0.35f)
        drawCircle(pp.good, w * 0.05f, bubble, alpha = f * seg(mu, 0.02f, 0.15f), style = Stroke(pen, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f * dp, 4f * dp))))
        for (k in 0 until 3) {
            val g = seg(mu, 0.1f + 0.28f * k, 0.3f + 0.28f * k)
            val c = Offset(w * (0.75f - 0.25f * k), h * 0.62f)
            val n = listOf(1, 10, 40)[k]
            for (j in 0 until n) {
                val q = c + Offset(w * 0.018f * ((j % 8) - 3.5f), -h * 0.025f * (j / 8))
                drawCircle(pp.good, w * 0.006f, q, alpha = f * seg(g, j / n.toFloat() * 0.7f, j / n.toFloat() * 0.7f + 0.3f))
            }
            note(a.tag("multiply", 0, k), c + Offset(0f, h * 0.1f), f * g, pp, pp.good)
            if (k > 0) arrowHead(c + Offset(w * 0.1f, -h * 0.04f), Offset(-1f, 0f), w * 0.02f, pp.ink, pen, f * g)
        }
    }
    if (bl > 0f) {
        val f = easeOut(seg(bl, 0f, BEAT_IN)) * (1f - seg(cs, 0f, BEAT_OUT))
        val bubble = Offset(w * 0.5f, h * 0.3f)
        val drop = seg(bl, 0.1f, 0.35f)
        drawCircle(pp.bad, w * 0.05f, bubble + Offset(0f, h * 0.05f * drop), alpha = f * (1f - drop), style = Stroke(pen))
        drawCircle(pp.good, w * 0.012f, Offset(w * 0.72f, h * 0.5f), alpha = f * seg(bl, 0.3f, 0.45f))
        note(a.tag("badleft", 0, 0), Offset(w * 0.72f, h * 0.62f), f * seg(bl, 0.3f, 0.5f), pp, pp.good)
        drawCircle(pp.bad, w * 0.012f, Offset(w * 0.28f, h * 0.5f), alpha = f * seg(bl, 0.6f, 0.75f))
        note(a.tag("badleft", 0, 1), Offset(w * 0.28f, h * 0.62f), f * seg(bl, 0.6f, 0.8f), pp, pp.bad)
    }
    if (cs > 0f) {
        val f = easeOut(seg(cs, 0f, BEAT_IN)) * (1f - seg(mw, 0f, BEAT_OUT))
        camel26(Offset(w * 0.78f, h * 0.72f), h * 0.3f, pp.ink, f * seg(cs, 0.05f, 0.2f), t)
        val n = (seg(cs, 0.3f, 0.9f) * 120f).toInt()
        for (k in 0 until n) {
            val c = Offset(w * (0.08f + 0.04f * (k % 12)), h * (0.35f + 0.05f * (k / 12)))
            drawCircle(pp.amber, w * 0.008f, c, alpha = f * 0.8f)
        }
    }
    if (mw > 0f) {
        val f = easeOut(seg(mw, 0f, 0.25f))
        drawRect(pp.bad, Offset(w * 0.25f, h * 0.75f), Size(w * 0.06f, h * 0.03f), alpha = f * seg(mw, 0.1f, 0.25f))
        val tall = seg(mw, 0.3f, 0.7f)
        drawRect(pp.good, Offset(w * 0.69f, h * 0.78f - h * 0.5f * tall), Size(w * 0.06f, h * 0.5f * tall), alpha = f * 0.8f)
        penLine(listOf(Offset(w * 0.15f, h * 0.78f), Offset(w * 0.85f, h * 0.78f)), seg(mw, 0.05f, 0.2f), pp.ink, pen, f)
        note(a.tag("mercywins"), Offset(w * 0.45f, h * 0.2f), f * seg(mw, 0.55f, 0.75f), pp, pp.good, boxed = true)
    }
}

// ═════════════ 3. the friends of Allah (nw-38) ═════════════

private object WaliC64 {
    val Night = Color(0xFF101828)
    val NightLow = Color(0xFF283450)
    val Ground = Color(0xFF1A2232)
    val Gold = Color(0xFFF2C860)
    val Shield = Color(0xFFBFE6FF)
    val Dark = Color(0xFF0C0A12)
    val Block = Color(0xFFD8CCB4)
    val Small = Color(0xFF8AC4A8)
    val Person = Color(0xFFDCD4C4)
    val Path = Color(0xFF4A5070)
}

/**
 * Beats: upright (a roundel keeps straight on a lit path; dark shapes that come at it are held back by a wall of light: «الْمُسْتَقِيمُ
 * عَلَى طَاعَتِهِ»), twotiersawliya (two groups of roundels, one on the right and one set higher and nearer the light: «أَصْحَابُ
 * الْيَمِينِ», «وَالسَّابِقُونَ الْمُقَرَّبُونَ»), fardthennafl (great blocks are laid first, then smaller ones on top, and the whole
 * glows: «مِمَّا افْتَرَضَ عَلَيْهِ», «بِالنَّوَافِلِ»), guidedsteps (a figure walks a rough path without a slip, soft light at his
 * ears, eyes, hands and feet: «مُوَفَّقًا مَحْفُوظًا مِنَ الزَّلَلِ»), answereddua (a roundel with his name, «سَعْدُ بْنُ أَبِي وَقَّاصٍ»;
 * his prayer goes up as light and comes back).
 */
internal fun DrawScope.awliyaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "upright" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "twotiersawliya" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "fardthennafl" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "guidedsteps" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "answereddua" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { waliNight(a) }
}

private fun DrawScope.waliNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val up = a.b("upright")
    val tt = a.b("twotiersawliya")
    val fn = a.b("fardthennafl")
    val gs = a.b("guidedsteps")
    val ad = a.b("answereddua")
    val ground = h * 0.86f
    nightSky(t, WaliC64.Night, WaliC64.NightLow, ground, starAlpha = 0.85f)
    drawRect(WaliC64.Ground, Offset(0f, ground), Size(w, h - ground))
    if (up > 0f) {
        val f = easeOut(seg(up, 0f, BEAT_IN)) * (1f - seg(tt, 0f, BEAT_OUT))
        drawLine(WaliC64.Gold, Offset(w * 0.5f, ground), Offset(w * 0.5f, h * 0.2f), h * 0.012f, StrokeCap.Round, alpha = f * 0.6f)
        val c = Offset(w * 0.5f, mix(ground - h * 0.05f, h * 0.45f, easeInOut(seg(up, 0.05f, 0.6f))))
        nameRoundel(c, w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        for (k in 0 until 4) {
            val dir = if (k % 2 == 0) 1f else -1f
            val come = seg(up, 0.25f + 0.08f * k, 0.5f + 0.08f * k)
            val x = w * 0.5f + dir * mix(w * 0.45f, w * 0.15f, come)
            drawCircle(WaliC64.Dark, w * 0.03f, Offset(x, h * (0.35f + 0.12f * (k / 2))), alpha = f * 0.9f)
        }
        val wall = seg(up, 0.45f, 0.6f)
        for (dir in listOf(-1f, 1f)) drawLine(WaliC64.Shield, Offset(w * 0.5f + dir * w * 0.1f, h * 0.3f), Offset(w * 0.5f + dir * w * 0.1f, h * 0.3f + h * 0.4f * wall), 5f * dp, StrokeCap.Round, alpha = f)
        tagAt(a.tag("upright"), Offset(w * 0.5f, h * 0.14f), f * seg(up, 0.15f, 0.35f), back = Back64, ink = ArtInk.Gold)
    }
    if (tt > 0f) {
        val f = easeOut(seg(tt, 0f, BEAT_IN)) * (1f - seg(fn, 0f, BEAT_OUT))
        softGlow(Offset(w * 0.5f, h * 0.08f), w * 0.2f, WaliC64.Gold, 0.4f * f)
        for (k in 0 until 6) nameRoundel(Offset(w * (0.62f + 0.06f * (k % 3)), h * (0.68f + 0.07f * (k / 3))), w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(tt, 0.1f, 0.3f))
        for (k in 0 until 4) nameRoundel(Offset(w * (0.38f + 0.07f * (k % 2)), h * (0.36f + 0.07f * (k / 2))), w * 0.025f, null, Roundel.PlainFill, Roundel.PlainRim, f * seg(tt, 0.45f, 0.65f))
        softGlow(Offset(w * 0.42f, h * 0.4f), w * 0.1f, WaliC64.Gold, 0.5f * f * seg(tt, 0.5f, 0.75f))
        tagAt(a.tag("twotiersawliya", 0, 0), Offset(w * 0.68f, h * 0.88f), f * seg(tt, 0.1f, 0.3f), back = Back64, ink = ArtInk.Gold)
        tagAt(a.tag("twotiersawliya", 0, 1), Offset(w * 0.42f, h * 0.26f), f * seg(tt, 0.45f, 0.65f), back = Back64, ink = ArtInk.Gold)
    }
    if (fn > 0f) {
        val f = easeOut(seg(fn, 0f, BEAT_IN)) * (1f - seg(gs, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val g = easeOut(seg(fn, 0.05f + 0.12f * k, 0.2f + 0.12f * k))
            drawRect(WaliC64.Block, Offset(w * (0.2f + 0.2f * k), ground - h * 0.12f - h * 0.2f * (1f - g)), Size(w * 0.19f, h * 0.12f), alpha = f * g)
        }
        for (k in 0 until 5) {
            val g = easeOut(seg(fn, 0.5f + 0.07f * k, 0.6f + 0.07f * k))
            drawRect(WaliC64.Small, Offset(w * (0.22f + 0.11f * k), ground - h * 0.19f - h * 0.15f * (1f - g)), Size(w * 0.1f, h * 0.06f), alpha = f * g)
        }
        softGlow(Offset(w * 0.5f, ground - h * 0.15f), w * 0.35f, WaliC64.Gold, 0.3f * f * seg(fn, 0.85f, 1f))
        tagAt(a.tag("fardthennafl", 0, 0), Offset(w * 0.5f, ground + h * 0.07f), f * seg(fn, 0.1f, 0.3f), back = Back64, ink = ArtInk.Gold)
        tagAt(a.tag("fardthennafl", 0, 1), Offset(w * 0.5f, ground - h * 0.32f), f * seg(fn, 0.5f, 0.7f), back = Back64, ink = WaliC64.Small)
    }
    if (gs > 0f) {
        val f = easeOut(seg(gs, 0f, BEAT_IN)) * (1f - seg(ad, 0f, BEAT_OUT))
        val path = Path().apply { moveTo(w, ground); for (j in 1..20) lineTo(w - w * j / 20f, ground - h * 0.02f * sin(j * 1.7f).let { it * it }) }
        drawPath(path, WaliC64.Path, alpha = f, style = Stroke(h * 0.02f, cap = StrokeCap.Round))
        for (k in 0 until 5) drawCircle(WaliC64.Dark, w * 0.012f, Offset(w * (0.15f + 0.17f * k), ground - h * 0.01f), alpha = f)
        val x = mix(w * 0.92f, w * 0.1f, easeInOut(seg(gs, 0.05f, 0.95f)))
        val foot = Offset(x, ground - h * 0.015f)
        val tall = h * 0.32f
        figure(foot, tall, WaliC64.Person, f, walk = t * 4f)
        for (p in listOf(Offset(0f, -tall * 0.9f), Offset(-tall * 0.2f, -tall * 0.45f), Offset(tall * 0.2f, -tall * 0.45f), Offset(0f, 0f))) softGlow(foot + p, w * 0.025f, WaliC64.Gold, 0.8f * f * seg(gs, 0.15f, 0.35f))
        tagAt(a.tag("guidedsteps"), Offset(w * 0.5f, h * 0.2f), f * seg(gs, 0.3f, 0.5f), back = Back64, ink = ArtInk.Gold)
    }
    if (ad > 0f) {
        val f = easeOut(seg(ad, 0f, 0.25f))
        val c = Offset(w * 0.5f, h * 0.62f)
        nameRoundel(c, w * 0.11f, a.big("answereddua"), Roundel.Fills[1], Roundel.Rim, f * seg(ad, 0.05f, 0.25f))
        val up = seg(ad, 0.3f, 0.6f)
        softGlow(Offset(c.x, mix(c.y - h * 0.15f, h * 0.1f, up)), w * 0.025f, WaliC64.Gold, f * up * (1f - seg(ad, 0.6f, 0.65f)))
        val down = seg(ad, 0.65f, 0.9f)
        softGlow(Offset(c.x, mix(h * 0.1f, c.y - h * 0.15f, down)), w * 0.04f, WaliC64.Gold, f * down)
        softGlow(c, w * 0.18f, WaliC64.Gold, 0.4f * f * seg(ad, 0.85f, 1f))
    }
}

// ═════════════ 4. what is pardoned (nw-39) ═════════════

private object PardonC64 {
    val Sky = Color(0xFF1A1E32)
    val SkyLow = Color(0xFF3A3A5A)
    val Ground = Color(0xFF242232)
    val Gold = Color(0xFFF2C860)
    val Dark = Color(0xFF3A3040)
    val Cup = Color(0xFFD8CCB4)
    val Water = Color(0xFF8AC4EA)
    val Person = Color(0xFFE2DACA)
    val Weight = Color(0xFF2A2430)
    val Heart = Color(0xFFF2C860)
    val Jar = Color(0xFFB88A58)
    val Coin = Color(0xFFE8C25A)
}

/**
 * Beats: threeforgiven (three dark marks dissolve into light: «الْخَطَأُ», «وَالنِّسْيَانُ», «وَمَا اسْتُكْرِهُوا عَلَيْهِ»),
 * accidentspill (a cup is knocked over by accident; no mark is set against him), forcedcalmheart (a weight presses on a figure's
 * shoulders, but the light in his heart stays: «وَقَلْبُهُ مُطْمَئِنٌّ بِالْإِيمَانِ»), answeredbaqara (hands raised; the prayer is
 * answered: «إِنْ نَسُوا أَوْ أَخْطَئُوا»), stillowes (a jar broken by mistake is paid for with a coin: «ضَمِنَهُ»).
 */
internal fun DrawScope.pardonedScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threeforgiven" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "accidentspill" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "forcedcalmheart" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "answeredbaqara" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "stillowes" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { pardonNight(a) }
}

private fun DrawScope.pardonNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tf = a.b("threeforgiven")
    val ap = a.b("accidentspill")
    val fc = a.b("forcedcalmheart")
    val ab = a.b("answeredbaqara")
    val so = a.b("stillowes")
    val ground = h * 0.84f
    nightSky(t, PardonC64.Sky, PardonC64.SkyLow, ground, starAlpha = 0.85f)
    drawRect(PardonC64.Ground, Offset(0f, ground), Size(w, h - ground))
    if (tf > 0f) {
        val f = easeOut(seg(tf, 0f, BEAT_IN)) * (1f - seg(ap, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val c = Offset(w * (0.78f - 0.28f * k), h * 0.55f)
            val g = seg(tf, 0.08f + 0.25f * k, 0.25f + 0.25f * k)
            val melt = seg(tf, 0.2f + 0.25f * k, 0.4f + 0.25f * k)
            drawCircle(lerpColor(PardonC64.Dark, PardonC64.Gold, melt), w * 0.05f * (1f - 0.4f * melt), c, alpha = f * g)
            softGlow(c, w * 0.1f, PardonC64.Gold, 0.5f * f * melt)
            tagAt(a.tag("threeforgiven", 0, k), c + Offset(0f, -h * 0.12f), f * g, back = Back64, ink = ArtInk.Gold)
        }
    }
    if (ap > 0f) {
        val f = easeOut(seg(ap, 0f, BEAT_IN)) * (1f - seg(fc, 0f, BEAT_OUT))
        figure(Offset(w * 0.3f, ground), h * 0.3f, PardonC64.Person, f, walk = t * 4f * (1f - seg(ap, 0.3f, 0.35f)))
        val tip = seg(ap, 0.25f, 0.4f)
        val cup = Offset(w * 0.5f, ground)
        drawRoundRect(PardonC64.Cup, Offset(cup.x - w * 0.03f + w * 0.05f * tip, cup.y - h * 0.07f * (1f - tip) - h * 0.02f * tip), Size(w * 0.06f + w * 0.04f * tip, h * 0.07f - h * 0.04f * tip), CornerRadius(4f * dp), alpha = f)
        drawOval(PardonC64.Water, Offset(cup.x + w * 0.06f, cup.y - h * 0.01f), Size(w * 0.18f * tip, h * 0.02f), alpha = f * 0.8f)
        softGlow(Offset(w * 0.3f, ground - h * 0.2f), w * 0.08f, PardonC64.Gold, 0.4f * f * seg(ap, 0.5f, 0.8f))
    }
    if (fc > 0f) {
        val f = easeOut(seg(fc, 0f, BEAT_IN)) * (1f - seg(ab, 0f, BEAT_OUT))
        val foot = Offset(w * 0.5f, ground)
        val tall = h * 0.4f
        val press = seg(fc, 0.1f, 0.35f)
        figure(foot, tall * (1f - 0.08f * press), PardonC64.Person, f, lean = 0.05f * press)
        drawRect(PardonC64.Weight, Offset(foot.x - w * 0.1f, foot.y - tall * 1.05f - h * 0.2f * (1f - press)), Size(w * 0.2f, h * 0.06f), alpha = f)
        val heart = foot + Offset(0f, -tall * 0.62f)
        heart12(heart, w * 0.03f, PardonC64.Heart, f * seg(fc, 0.3f, 0.5f), fill = true)
        softGlow(heart, w * 0.09f, PardonC64.Gold, 0.6f * f * seg(fc, 0.35f, 0.6f))
        tagAt(a.tag("forcedcalmheart"), Offset(w * 0.5f, h * 0.15f), f * seg(fc, 0.45f, 0.65f), back = Back64, ink = ArtInk.Gold)
    }
    if (ab > 0f) {
        val f = easeOut(seg(ab, 0f, BEAT_IN)) * (1f - seg(so, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.62f)
        hands12(c, h * 0.18f, PardonC64.Person, f)
        val up = seg(ab, 0.2f, 0.5f)
        softGlow(Offset(c.x, mix(c.y - h * 0.1f, h * 0.12f, up)), w * 0.025f, PardonC64.Gold, f * up)
        lightShafts(Offset(w * 0.5f, 0f), PI.toFloat() / 2f, 0.3f, c.y - h * 0.05f, PardonC64.Gold, 0.45f * f * seg(ab, 0.55f, 0.8f), t, rays = 5)
        tagAt(a.tag("answeredbaqara"), Offset(w * 0.5f, h * 0.85f), f * seg(ab, 0.3f, 0.5f), back = Back64, ink = ArtInk.Gold)
    }
    if (so > 0f) {
        val f = easeOut(seg(so, 0f, 0.25f))
        val jar = Offset(w * 0.6f, ground)
        val broken = seg(so, 0.1f, 0.25f)
        for (k in 0 until 3) {
            val off = Offset(w * 0.04f * (k - 1) * broken, h * 0.01f * broken * k)
            drawPath(Path().apply { val x0 = jar.x - w * 0.05f + w * 0.033f * k; moveTo(x0 + off.x, jar.y - h * 0.12f + off.y + h * 0.08f * broken); lineTo(x0 + w * 0.033f + off.x, jar.y - h * 0.12f + off.y + h * 0.08f * broken); lineTo(x0 + w * 0.033f + off.x, jar.y); lineTo(x0 + off.x, jar.y); close() }, PardonC64.Jar, alpha = f)
        }
        nameRoundel(Offset(w * 0.25f, h * 0.55f), w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        nameRoundel(Offset(w * 0.85f, h * 0.55f), w * 0.045f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val give = easeInOut(seg(so, 0.45f, 0.8f))
        coin12(Offset(mix(w * 0.3f, w * 0.8f, give), h * 0.55f - h * 0.08f * sin(PI.toFloat() * give)), w * 0.025f, f * seg(so, 0.4f, 0.5f), PardonC64.Coin)
        tagAt(a.tag("stillowes"), Offset(w * 0.5f, h * 0.25f), f * seg(so, 0.5f, 0.7f), back = Back64, ink = ArtInk.Gold)
        tagAt(a.tag("stillowes", 0, 1), jar + Offset(0f, -h * 0.44f), f * seg(so, 0.5f, 0.7f), back = Color(0xE6201810), ink = ArtInk.Gold)
    }
}

// ═════════════ 5. like a stranger (nw-40) ═════════════

private object StrangerC64 {
    val Sky = Color(0xFFC6DAE8)
    val SkyLow = Color(0xFFF2E2C4)
    val Ground = Color(0xFFD2BC92)
    val Road = Color(0xFFE8DCC0)
    val Wall = Color(0xFFE2D2B4)
    val Trim = Color(0xFF8A6A44)
    val Gold = Color(0xFFD89A2A)
    val Skin = Color(0xFF8A6A44)
    val Water = Color(0xFF5AA0D0)
    val Night = Color(0xFF2A2A4A)
    val Jar = Color(0xFFB88A58)
}

/**
 * Beats: shoulderword (the light ﷺ beside a roundel with his name, «ابْنِ عُمَرَ», and the counsel over a long road: «كَأَنَّكَ
 * غَرِيبٌ أَوْ عَابِرُ سَبِيلٍ»), takesonly (a plain roundel passes a busy town without stopping and takes only a water skin),
 * dontwait (the sun goes down and comes up over a road that is never left idle: «إِذَا أَمْسَيْتَ فَلَا تَنْتَظِرِ الصَّبَاحَ»,
 * «وَإِذَا أَصْبَحْتَ فَلَا تَنْتَظِرِ الْمَسَاءَ»), storeahead (water poured from a full jar into a jar set aside: «وَخُذْ مِنْ
 * صِحَّتِكَ لِمَرَضِكَ», «وَمِنْ حَيَاتِكَ لِمَوْتِكَ»).
 */
internal fun DrawScope.strangerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shoulderword" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "takesonly" to Shot(Offset(w * 0.5f, h * 0.6f), 1.0f),
        "dontwait" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "storeahead" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.04f) { strangerDay(a) }
}

private fun DrawScope.strangerDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sw = a.b("shoulderword")
    val to = a.b("takesonly")
    val dw = a.b("dontwait")
    val sa = a.b("storeahead")
    val ground = h * 0.78f
    val night = if (dw > 0f && sa <= 0f) sin(PI.toFloat() * fract(seg(dw, 0.05f, 0.95f) * 2f)).let { 1f - it } * seg(dw, 0f, 0.2f) else 0f
    daySky12(lerpColor(StrangerC64.Sky, StrangerC64.Night, night), lerpColor(StrangerC64.SkyLow, StrangerC64.Night, night * 0.7f), ground, StrangerC64.Ground)
    if (sw > 0f) {
        val f = easeOut(seg(sw, 0f, BEAT_IN)) * (1f - seg(to, 0f, BEAT_OUT))
        drawLine(StrangerC64.Road, Offset(w, ground + h * 0.08f), Offset(0f, ground + h * 0.02f), h * 0.04f, StrokeCap.Round, alpha = f)
        val light = Offset(w * 0.65f, h * 0.42f)
        sirajMunir(light, w * 0.012f, f, t, nameAt = light + Offset(0f, -h * 0.11f))
        nameRoundel(Offset(w * 0.4f, h * 0.48f), w * 0.09f, a.big("shoulderword", 0, 0), Roundel.Fills[0], Roundel.Rim, f * seg(sw, 0.05f, 0.25f))
        tagAt(a.tag("shoulderword", 0, 1), Offset(w * 0.5f, h * 0.16f), f * seg(sw, 0.35f, 0.55f), back = Back64, ink = ArtInk.Gold)
    }
    if (to > 0f) {
        val f = easeOut(seg(to, 0f, BEAT_IN)) * (1f - seg(dw, 0f, BEAT_OUT))
        for (k in 0 until 4) house12(Offset(w * (0.3f + 0.13f * k), ground), w * 0.12f, StrangerC64.Wall, StrangerC64.Trim, f, 0.4f)
        drawLine(StrangerC64.Road, Offset(w, ground + h * 0.08f), Offset(0f, ground + h * 0.08f), h * 0.04f, StrokeCap.Round, alpha = f)
        val u = easeInOut(seg(to, 0.05f, 0.95f))
        val c = Offset(mix(w * 0.95f, w * 0.05f, u), ground + h * 0.03f)
        nameRoundel(c, w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val got = seg(to, 0.45f, 0.5f)
        drawOval(StrangerC64.Skin, Offset(c.x + w * 0.02f, c.y + h * 0.005f), Size(w * 0.025f, h * 0.035f), alpha = f * got)
        cup12(Offset(w * 0.5f, ground + h * 0.06f), w * 0.04f, StrangerC64.Jar, f * (1f - got), StrangerC64.Water)
    }
    if (dw > 0f) {
        val f = easeOut(seg(dw, 0f, BEAT_IN)) * (1f - seg(sa, 0f, BEAT_OUT))
        drawLine(StrangerC64.Road, Offset(w, ground + h * 0.08f), Offset(0f, ground + h * 0.08f), h * 0.04f, StrokeCap.Round, alpha = f)
        val cyc = fract(seg(dw, 0.05f, 0.95f) * 2f)
        sun12(Offset(w * (0.9f - 0.8f * cyc), mix(ground, h * 0.15f, sin(PI.toFloat() * cyc))), w * 0.04f, f * (1f - night))
        if (night > 0.2f) moon12(Offset(w * 0.3f, h * 0.2f), w * 0.03f, f * night)
        val c = Offset(w * (0.5f + 0.02f * sin(t)), ground + h * 0.03f)
        nameRoundel(c, w * 0.03f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        softGlow(c, w * 0.06f, StrangerC64.Gold, 0.5f * f)
        tagAt(a.tag("dontwait", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(dw, 0.05f, 0.25f) * (1f - seg(dw, 0.45f, 0.55f)), back = Back64, ink = ArtInk.Gold)
        tagAt(a.tag("dontwait", 0, 1), Offset(w * 0.5f, h * 0.3f), f * seg(dw, 0.55f, 0.7f), back = Back64, ink = ArtInk.Gold)
    }
    if (sa > 0f) {
        val f = easeOut(seg(sa, 0f, 0.25f))
        for (row in 0 until 2) {
            val y = h * (0.45f + 0.28f * row)
            val g = seg(sa, 0.1f + 0.4f * row, 0.4f + 0.4f * row)
            val full = Offset(w * 0.72f, y)
            val store = Offset(w * 0.3f, y)
            cup12(full, w * 0.1f, StrangerC64.Jar, f, StrangerC64.Water)
            drawRoundRect(StrangerC64.Jar, Offset(store.x - w * 0.05f, store.y - h * 0.12f), Size(w * 0.1f, h * 0.12f), CornerRadius(8f * dp), alpha = f, style = Stroke(3f * dp))
            drawRect(StrangerC64.Water, Offset(store.x - w * 0.04f, store.y - h * 0.11f * g), Size(w * 0.08f, h * 0.11f * g), alpha = f * 0.8f)
            if (g > 0f && g < 1f) drawPath(Path().apply { moveTo(full.x - w * 0.04f, full.y - h * 0.1f); quadraticTo(w * 0.5f, y - h * 0.2f, store.x + w * 0.02f, store.y - h * 0.1f) }, StrangerC64.Water, alpha = f * 0.8f, style = Stroke(3f * dp))
            tagAt(a.tag("storeahead", 0, row), Offset(w * 0.5f, y - h * 0.2f), f * g, back = Back64, ink = ArtInk.Gold)
        }
    }
}
