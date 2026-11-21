package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 77 (2026-11-03): v1.3 fiqh, «الفقه الميسر», purification 2. What breaks wuḍūʾ, wiping over khuffs and over a cast, the full
 * wash, tayammum and washing away impurity. All of it is told with objects (a drop of light, a boot, a cast, a cloth, a bowl) and,
 * where a body is needed, the plain faceless figure; nothing of the body is shown beyond hands and feet.
 */

/** The light of wuḍūʾ as a drop, cracked by [crack]. */
private fun DrawScope.wuduDrop77(c: Offset, r: Float, crack: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    softGlow(c, r * 2.4f, ThrC.Water, 0.5f * alpha * (1f - crack))
    fqDrop(c, r, lerpColor(ThrC.Water, Color(0xFF5A6278), crack), alpha)
    if (crack > 0f) {
        val p = Path().apply { moveTo(c.x, c.y - r * 1.6f); lineTo(c.x + r * 0.25f, c.y - r * 0.6f); lineTo(c.x - r * 0.2f, c.y - r * 0.1f); lineTo(c.x + r * 0.3f, c.y + r * 0.6f) }
        drawPath(p, Color(0xFF14182A), alpha = alpha * crack, style = Stroke(r * 0.12f))
    }
}

/** A leather khuff, side view, toe to the left, its sole on [foot]. */
private fun DrawScope.khuff77(foot: Offset, s: Float, color: Color, alpha: Float) {
    if (alpha <= 0.01f) return
    drawPath(Path().apply {
        moveTo(foot.x + s * 0.25f, foot.y - s); lineTo(foot.x + s * 0.5f, foot.y - s); lineTo(foot.x + s * 0.5f, foot.y)
        lineTo(foot.x - s * 0.45f, foot.y); quadraticTo(foot.x - s * 0.55f, foot.y - s * 0.2f, foot.x - s * 0.2f, foot.y - s * 0.3f)
        lineTo(foot.x + s * 0.25f, foot.y - s * 0.45f); close()
    }, color, alpha = alpha)
    drawLine(lerpColor(color, Color.Black, 0.35f), Offset(foot.x - s * 0.45f, foot.y), Offset(foot.x + s * 0.5f, foot.y), s * 0.06f, alpha = alpha)
}

/** A cloth hanging flat around [c], with a dark stain cleaned by [clean]. */
private fun DrawScope.stainCloth77(c: Offset, wd: Float, ht: Float, clean: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFFE8E2D4), Offset(c.x - wd / 2f, c.y - ht / 2f), Size(wd, ht), CornerRadius(wd * 0.04f), alpha = alpha)
    for (k in 0 until 3) drawCircle(Color(0xFF6A5A3A), wd * (0.08f + 0.03f * k), c + Offset(wd * (0.06f * k - 0.05f), ht * (0.05f * k - 0.05f)), alpha = alpha * 0.7f * (1f - clean))
}

private fun DrawScope.thrDark(t: Float) {
    nightSky(t, ThrC.Top, Color(0xFF243A58), size.height, starAlpha = 0.35f)
}

// ═════════════ 7. thr-nawaqid: what breaks wuḍūʾ ═════════════

/**
 * Beats: wuduglowbreaks (the drop of wuḍūʾ shines and then cracks: «نَوَاقِضِ الْوُضُوءِ»), deepsleep (one lying deep in sleep under the moon,
 * struck out; one sitting who only nods, ticked: «نَوْمٍ مُسْتَغْرِقٍ», «أَمَّا النَّوْمُ الْيَسِيرُ فَلَا يَنْقُضُ»), doubtstay (one at prayer;
 * a grey cloud of doubt passes and he stays: «حَتَّى يَسْمَعَ صَوْتًا أَوْ يَجِدَ رِيحًا»), camelmeat (a camel and a plate, and the drop dims:
 * «عِنْدَ مُؤَلِّفِي الْكِتَابِ», «أَكْلُ لَحْمِ الْإِبِلِ»).
 */
internal fun DrawScope.wudubreakersScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "wuduglowbreaks" to Shot(Offset(w * 0.5f, h * 0.5f), 1.08f),
        "deepsleep" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "doubtstay" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "camelmeat" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { wuduBreakersDraw(a) }
}

private fun DrawScope.wuduBreakersDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ground = h * 0.82f
    thrDark(t)
    drawRect(ThrC.Ground, Offset(0f, ground), Size(w, h - ground))
    val wb = fqIn(a, "wuduglowbreaks", "deepsleep")
    val ds = fqIn(a, "deepsleep", "doubtstay")
    val dt = fqIn(a, "doubtstay", "camelmeat")
    val cm = fqIn(a, "camelmeat")
    if (wb > 0f) {
        val p = a.b("wuduglowbreaks")
        wuduDrop77(Offset(w * 0.5f, h * 0.5f), h * 0.1f, seg(p, 0.45f, 0.7f), wb)
        tagAt(a.tag("wuduglowbreaks"), Offset(w * 0.5f, h * 0.86f), wb * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ds > 0f) {
        val p = a.b("deepsleep")
        val g0 = seg(p, 0.05f, 0.25f)
        drawRoundRect(Color(0xFF5A4A3E), Offset(w * 0.55f, ground - h * 0.05f), Size(w * 0.4f, h * 0.05f), CornerRadius(h * 0.01f), alpha = ds * g0)
        lying73(Offset(w * 0.59f, ground - h * 0.09f), w * 0.33f, ThrC.Person, ds * g0)
        moon12(Offset(w * 0.75f, h * 0.2f), w * 0.03f, ds * g0)
        cross14(Offset(w * 0.75f, ground - h * 0.3f), w * 0.04f, seg(p, 0.3f, 0.45f))
        tagAt(a.tag("deepsleep", 0, 0), Offset(w * 0.75f, ground + h * 0.08f), ds * g0, back = Fq.Back, ink = ArtInk.Ember)
        val g1 = seg(p, 0.5f, 0.7f)
        val nod = 0.15f + 0.1f * sin(t * 2f)
        figure(Offset(w * 0.24f, ground), h * 0.24f, ThrC.Person, ds * g1, lean = nod)
        tick14(Offset(w * 0.24f, ground - h * 0.36f), w * 0.04f, seg(p, 0.7f, 0.85f))
        tagAt(a.tag("deepsleep", 0, 1), Offset(w * 0.3f, ground + h * 0.08f), ds * g1, back = Fq.Back, ink = ArtInk.Jade)
    }
    if (dt > 0f) {
        val p = a.b("doubtstay")
        val foot = Offset(w * 0.6f, ground)
        pray12(foot, h * 0.36f, ThrC.Person, dt)
        val cloud = seg(p, 0.1f, 0.3f) * (1f - seg(p, 0.55f, 0.8f))
        cloud38(foot + Offset(-w * 0.15f, -h * 0.42f), w * 0.07f, Color(0xFF8A8A98), dt * cloud)
        softGlow(foot + Offset(0f, -h * 0.2f), w * 0.12f, ThrC.Gold, 0.4f * dt * seg(p, 0.6f, 0.85f))
        tagAt(a.tag("doubtstay"), Offset(w * 0.4f, h * 0.14f), dt * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (cm > 0f) {
        val p = a.b("camelmeat")
        camel26(Offset(w * 0.72f, ground), h * 0.4f, Color(0xFFC8A880), cm, t)
        drawOval(Color(0xFFDCD8D0), Offset(w * 0.3f - w * 0.1f, ground - h * 0.04f), Size(w * 0.2f, h * 0.05f), alpha = cm)
        drawOval(Color(0xFF9A5A4A), Offset(w * 0.3f - w * 0.05f, ground - h * 0.06f), Size(w * 0.1f, h * 0.035f), alpha = cm)
        wuduDrop77(Offset(w * 0.3f, h * 0.38f), h * 0.07f, seg(p, 0.5f, 0.7f), cm)
        tagAt(a.tag("camelmeat", 0, 0), Offset(w * 0.5f, h * 0.12f), cm * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("camelmeat", 0, 1), Offset(w * 0.5f, h * 0.24f), cm * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 8. thr-mash: wiping over khuffs and socks ═════════════

/**
 * Beats: khuffallowed (a leather khuff and a sock, each ticked: «بِاتِّفَاقِ أَهْلِ السُّنَّةِ», «وَيُلْحَقُ بِهِمَا الْجَوَارِبُ إِذَا كَانَتْ
 * سَاتِرَةً»), wipeperiod (a drop before the boot goes on, then a day and a night at home and three days on the road: «عَلَى طَهَارَةٍ», «يَوْمًا
 * وَلَيْلَةً لِلْمُقِيمِ», «وَثَلَاثَةَ أَيَّامٍ بِلَيَالِيهِنَّ لِلْمُسَافِرِ»), wipetop (a wet stroke along the top; the sole struck out: «أَكْثَرَ
 * أَعْلَى الْخُفِّ», «وَلَا يُجْزِئُ مَسْحُ أَسْفَلِهِ»), clockstarts (a clock that starts at the first wipe: «مِنْ أَوَّلِ مَسْحٍ بَعْدَ الْحَدَثِ»),
 * wipeends (the boot taken off, a heel showing, the time run out, each ending it: «وَبِنَزْعِ الْخُفَّيْنِ», «وَبِظُهُورِ بَعْضِ الْقَدَمِ»,
 * «وَبِانْقِضَاءِ الْمُدَّةِ»).
 */
internal fun DrawScope.khuffwipeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "khuffallowed" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "wipeperiod" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "wipetop" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "clockstarts" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "wipeends" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { khuffWipeDraw(a) }
}

private fun DrawScope.khuffWipeDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    thrDark(t)
    drawRect(ThrC.Ground, Offset(0f, ground), Size(w, h - ground))
    val leather = Color(0xFF7A4E2E)
    val sock = Color(0xFF5A6A8A)
    val ka = fqIn(a, "khuffallowed", "wipeperiod")
    val wp = fqIn(a, "wipeperiod", "wipetop")
    val wt = fqIn(a, "wipetop", "clockstarts")
    val cs = fqIn(a, "clockstarts", "wipeends")
    val we = fqIn(a, "wipeends")
    if (ka > 0f) {
        val p = a.b("khuffallowed")
        val g0 = seg(p, 0.05f, 0.25f)
        khuff77(Offset(w * 0.68f, ground), h * 0.36f, leather, ka * g0)
        tick14(Offset(w * 0.72f, ground - h * 0.45f), w * 0.04f, seg(p, 0.2f, 0.35f))
        tagAt(a.tag("khuffallowed", 0, 0), Offset(w * 0.68f, h * 0.12f), ka * g0, back = Fq.Back, ink = ArtInk.Gold)
        val g1 = seg(p, 0.5f, 0.7f)
        khuff77(Offset(w * 0.28f, ground), h * 0.3f, sock, ka * g1)
        tick14(Offset(w * 0.32f, ground - h * 0.38f), w * 0.04f, seg(p, 0.65f, 0.8f))
        tagAt(a.tag("khuffallowed", 0, 1), Offset(w * 0.3f, h * 0.24f), ka * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (wp > 0f) {
        val p = a.b("wipeperiod")
        val g0 = seg(p, 0.05f, 0.2f)
        fqDrop(Offset(w * 0.5f, h * 0.2f), w * 0.025f, ThrC.Water, wp * g0)
        tagAt(a.tag("wipeperiod", 0, 0), Offset(w * 0.5f, h * 0.08f), wp * g0, back = Fq.Back, ink = ArtInk.Ivory)
        for (r in 0 until 2) {
            val g = seg(p, 0.3f + 0.3f * r, 0.45f + 0.3f * r)
            val y = h * (0.42f + 0.24f * r)
            val days = if (r == 0) 1 else 3
            for (d in 0 until days) {
                val x = w * (0.78f - 0.16f * d)
                sun12(Offset(x, y - h * 0.03f), w * 0.02f, wp * g)
                moon12(Offset(x - w * 0.06f, y - h * 0.03f), w * 0.017f, wp * g)
            }
            if (r == 0) house12(Offset(w * 0.9f, y + h * 0.04f), w * 0.06f, Color(0xFFCDBFA6), Color(0xFF6A5A48), wp * g)
            else camel26(Offset(w * 0.92f, y + h * 0.05f), h * 0.13f, Color(0xFFC8A880), wp * g, t)
            tagAt(a.tag("wipeperiod", 0, 1 + r), Offset(w * 0.3f, y), wp * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    if (wt > 0f) {
        val p = a.b("wipetop")
        val foot = Offset(w * 0.5f, ground)
        val s = h * 0.5f
        khuff77(foot, s, leather, wt)
        val stroke = seg(p, 0.1f, 0.45f)
        val from = foot + Offset(-s * 0.3f, -s * 0.36f)
        val to = foot + Offset(s * 0.2f, -s * 0.5f)
        drawLine(ThrC.Water, from, from + (to - from) * stroke, s * 0.05f, StrokeCap.Round, alpha = wt * 0.85f)
        tagAt(a.tag("wipetop", 0, 0), Offset(w * 0.5f, h * 0.12f), wt * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Jade)
        drawLine(Color(0xFFC0503A), foot + Offset(-s * 0.45f, s * 0.03f), foot + Offset(s * 0.5f, s * 0.03f), 3f * dp, alpha = wt * seg(p, 0.55f, 0.7f))
        cross14(foot + Offset(s * 0.7f, 0f), w * 0.035f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("wipetop", 0, 1), Offset(w * 0.5f, h * 0.92f), wt * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (cs > 0f) {
        val p = a.b("clockstarts")
        khuff77(Offset(w * 0.3f, ground), h * 0.3f, leather, cs)
        val wipe = seg(p, 0.1f, 0.3f)
        drawLine(ThrC.Water, Offset(w * 0.3f - h * 0.09f, ground - h * 0.11f), Offset(w * 0.3f + h * 0.06f, ground - h * 0.15f), h * 0.015f, StrokeCap.Round, alpha = cs * wipe)
        clock22(Offset(w * 0.7f, h * 0.45f), w * 0.1f, ThrC.Gold, seg(p, 0.35f, 1f) * 0.6f, cs * seg(p, 0.25f, 0.4f))
        tagAt(a.tag("clockstarts"), Offset(w * 0.5f, h * 0.12f), cs * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (we > 0f) {
        val p = a.b("wipeends")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.25f * k, 0.25f + 0.25f * k)
            val x = w * (0.8f - 0.3f * k)
            when (k) {
                0 -> khuff77(Offset(x, ground - h * 0.15f * g), h * 0.22f, leather, we * g)
                1 -> {
                    khuff77(Offset(x, ground), h * 0.22f, leather, we * g)
                    drawCircle(Color(0xFFD8B898), h * 0.025f, Offset(x + h * 0.08f, ground - h * 0.05f), alpha = we * g)
                }
                else -> clock22(Offset(x, ground - h * 0.12f), w * 0.06f, ThrC.Gold, 1f, we * g)
            }
            cross14(Offset(x, ground - h * 0.38f), w * 0.03f, seg(p, 0.15f + 0.25f * k, 0.3f + 0.25f * k))
            tagAt(a.tag("wipeends", 0, k), Offset(x, h * (0.12f + 0.08f * (k % 2))), we * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
}

// ═════════════ 9. thr-jabira: wiping over a cast ═════════════

/**
 * Beats: castonarm (a forearm, a break in it, and a white cast bound over it: «كَالْجِبْسِ»), nolimit (a wet hand passes over the cast; days go by
 * with no end set until the cast comes off: «فِي الْحَدَثِ الْأَصْغَرِ وَالْأَكْبَرِ», «إِلَى نَزْعِهَا أَوْ شِفَاءِ مَا تَحْتَهَا»), trimexcess (a cast
 * longer than the hurt; what is beyond it is cut away: «لَزِمَهُ نَزْعُ مَا زَادَ»), needmeasure (the cast and the hurt match on a measure:
 * «وَالضَّرُورَةُ تُقَدَّرُ بِقَدَرِهَا»).
 */
internal fun DrawScope.castwipeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "castonarm" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "nolimit" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "trimexcess" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "needmeasure" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { castWipeDraw(a) }
}

private fun DrawScope.castWipeDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    thrDark(t)
    val co = fqIn(a, "castonarm", "nolimit")
    val nl = fqIn(a, "nolimit", "trimexcess")
    val tx = fqIn(a, "trimexcess", "needmeasure")
    val nm = fqIn(a, "needmeasure")
    val skin = Color(0xFFD8B898)
    val castC = Color(0xFFF2F0EA)
    val arm = { y: Float, alpha: Float, x0: Float, x1: Float, castFrom: Float, castTo: Float ->
        drawLine(skin, Offset(w * x0, y), Offset(w * x1, y), h * 0.09f, StrokeCap.Round, alpha = alpha)
        if (castTo > castFrom) drawRoundRect(castC, Offset(w * castFrom, y - h * 0.065f), Size(w * (castTo - castFrom), h * 0.13f), CornerRadius(h * 0.04f), alpha = alpha)
    }
    if (co > 0f) {
        val p = a.b("castonarm")
        val y = h * 0.5f
        drawLine(skin, Offset(w * 0.15f, y), Offset(w * 0.85f, y), h * 0.09f, StrokeCap.Round, alpha = co)
        drawLine(Color(0xFFC0503A), Offset(w * 0.5f, y - h * 0.04f), Offset(w * 0.52f, y + h * 0.04f), 2.5f * dp, alpha = co * (1f - seg(p, 0.3f, 0.45f)))
        val wrap = easeOut(seg(p, 0.3f, 0.7f))
        arm(y, co, 0.15f, 0.85f, 0.42f, 0.42f + 0.18f * wrap)
        tagAt(a.tag("castonarm"), Offset(w * 0.5f, h * 0.24f), co * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (nl > 0f) {
        val p = a.b("nolimit")
        val y = h * 0.4f
        arm(y, nl, 0.15f, 0.85f, 0.42f, 0.6f)
        val sweep = fract(t * 0.5f)
        drawLine(ThrC.Water, Offset(w * (0.6f - 0.18f * sweep), y - h * 0.07f), Offset(w * (0.6f - 0.18f * sweep), y + h * 0.07f), 4f * dp, StrokeCap.Round, alpha = nl * seg(p, 0.05f, 0.2f))
        tagAt(a.tag("nolimit", 0, 0), Offset(w * 0.5f, h * 0.14f), nl * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        val days = (seg(p, 0.4f, 0.9f) * 8f).toInt()
        for (d in 0 until 8) {
            val c = Offset(w * (0.85f - 0.09f * d), h * 0.7f)
            drawRoundRect(if (d < days) ThrC.Gold else Color(0xFF3A4468), Offset(c.x - w * 0.03f, c.y - w * 0.03f), Size(w * 0.06f, w * 0.06f), CornerRadius(w * 0.01f), alpha = nl * seg(p, 0.35f, 0.45f))
        }
        tagAt(a.tag("nolimit", 0, 1), Offset(w * 0.5f, h * 0.88f), nl * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tx > 0f) {
        val p = a.b("trimexcess")
        val y = h * 0.5f
        val cut = easeInOut(seg(p, 0.35f, 0.7f))
        arm(y, tx, 0.15f, 0.85f, 0.42f, mix(0.8f, 0.6f, cut))
        drawLine(Color(0xFFC0503A), Offset(w * 0.5f, y - h * 0.04f), Offset(w * 0.52f, y + h * 0.04f), 2.5f * dp, alpha = tx * 0.4f)
        if (cut > 0f && cut < 1f) fqScissors(Offset(w * mix(0.8f, 0.6f, cut), y - h * 0.14f), w * 0.07f, ThrC.Silver, tx, 0.5f + 0.5f * sin(t * 8f))
        tagAt(a.tag("trimexcess"), Offset(w * 0.5f, h * 0.2f), tx * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nm > 0f) {
        val p = a.b("needmeasure")
        val y = h * 0.45f
        arm(y, nm, 0.15f, 0.85f, 0.42f, 0.6f)
        val g = seg(p, 0.2f, 0.45f)
        for (x in listOf(0.42f, 0.6f)) drawLine(ThrC.Gold, Offset(w * x, y + h * 0.1f), Offset(w * x, y + h * 0.22f), 2f * dp, alpha = nm * g)
        drawLine(ThrC.Gold, Offset(w * 0.42f, y + h * 0.16f), Offset(w * 0.6f, y + h * 0.16f), 2f * dp, alpha = nm * g)
        softGlow(Offset(w * 0.51f, y), w * 0.12f, ThrC.Gold, 0.35f * nm * g)
        tagAt(a.tag("needmeasure"), Offset(w * 0.5f, h * 0.84f), nm * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 10. thr-ghusl: the full wash ═════════════

/**
 * Beats: wholebody (water runs over a plain figure until a line of light holds all of it: «تَعْمِيمُ الْبَدَنِ بِالْمَاءِ»), newmuslimwash (one comes
 * through a lit doorway into falling water: «وَإِسْلَامُ الْكَافِرِ»), twoghusl (two panels: one full pour that suffices, and a fuller way in
 * steps: «كَيْفِيَّةُ إِجْزَاءٍ», «وَكَيْفِيَّةُ كَمَالٍ»), perfectsteps (the steps light in turn: a jug for wuḍūʾ, three pours on the head, then
 * the rest of the body: «ثُمَّ يَتَوَضَّأُ», «ثُمَّ يُفِيضُ الْمَاءَ عَلَى رَأْسِهِ ثَلَاثًا», «ثُمَّ يَغْسِلُ سَائِرَ جَسَدِهِ»).
 */
internal fun DrawScope.fullwashScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "wholebody" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "newmuslimwash" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "twoghusl" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "perfectsteps" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fullWashDraw(a) }
}

private fun DrawScope.fullWashDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.86f
    thrDark(t)
    drawRect(ThrC.Ground, Offset(0f, ground), Size(w, h - ground))
    val wb = fqIn(a, "wholebody", "newmuslimwash")
    val nm = fqIn(a, "newmuslimwash", "twoghusl")
    val tg = fqIn(a, "twoghusl", "perfectsteps")
    val ps = fqIn(a, "perfectsteps")
    if (wb > 0f) {
        val p = a.b("wholebody")
        val foot = Offset(w * 0.5f, ground)
        figure(foot, h * 0.6f, ThrC.Person, wb)
        rain12(w * 0.38f, w * 0.62f, h * 0.08f, ground, t * 1.4f, ThrC.Water, wb * seg(p, 0.05f, 0.2f), 36)
        val cover = seg(p, 0.2f, 0.7f)
        drawRoundRect(ThrC.Water, Offset(foot.x - h * 0.12f, mix(ground, ground - h * 0.66f, cover)), Size(h * 0.24f, h * 0.66f * cover), CornerRadius(h * 0.1f), alpha = wb * 0.6f, style = Stroke(3f * dp))
        tagAt(a.tag("wholebody"), Offset(w * 0.2f, h * 0.5f), wb * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (nm > 0f) {
        val p = a.b("newmuslimwash")
        val door = Offset(w * 0.3f, ground)
        drawPath(archPath26(door.x, ground - h * 0.6f, ground, w * 0.1f), ThrC.Gold, alpha = nm * 0.6f)
        softGlow(door + Offset(0f, -h * 0.3f), w * 0.15f, ThrC.Gold, 0.5f * nm)
        val u = easeInOut(seg(p, 0.1f, 0.6f))
        figure(Offset(mix(door.x, w * 0.65f, u), ground), h * 0.5f, ThrC.Person, nm, walk = if (u < 1f) t * 6f else 0f)
        rain12(w * 0.55f, w * 0.75f, h * 0.1f, ground, t * 1.4f, ThrC.Water, nm * seg(p, 0.55f, 0.7f), 30)
        tagAt(a.tag("newmuslimwash"), Offset(w * 0.65f, h * 0.12f), nm * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tg > 0f) {
        val p = a.b("twoghusl")
        for (k in 0 until 2) {
            val g = seg(p, 0.05f + 0.45f * k, 0.25f + 0.45f * k)
            val c = Offset(w * (0.73f - 0.46f * k), h * 0.5f)
            drawRoundRect(Color(0xFF0E1A2E), Offset(c.x - w * 0.2f, c.y - h * 0.32f), Size(w * 0.4f, h * 0.64f), CornerRadius(w * 0.02f), alpha = tg * g)
            if (k == 0) {
                figure(Offset(c.x, c.y + h * 0.26f), h * 0.4f, ThrC.Person, tg * g)
                rain12(c.x - w * 0.1f, c.x + w * 0.1f, c.y - h * 0.28f, c.y + h * 0.26f, t * 1.4f, ThrC.Water, tg * g, 26)
            } else for (s in 0 until 4) {
                val sc = Offset(c.x + w * (0.13f - 0.087f * s), c.y)
                drawCircle(ThrC.Water, w * 0.03f, sc, alpha = tg * g * 0.8f)
                if (s < 3) drawLine(ThrC.Water, sc + Offset(-w * 0.032f, 0f), sc + Offset(-w * 0.055f, 0f), 2f * dp, alpha = tg * g)
            }
            tagAt(a.tag("twoghusl", 0, k), Offset(c.x, c.y - h * 0.38f), tg * g, back = Fq.Back, ink = if (k == 0) ArtInk.Ivory else ArtInk.Gold)
        }
    }
    if (ps > 0f) {
        val p = a.b("perfectsteps")
        val foot = Offset(w * 0.5f, ground)
        figure(foot, h * 0.5f, ThrC.Person, ps * 0.9f)
        val g0 = seg(p, 0.05f, 0.25f)
        fqEwer(Offset(w * 0.82f, ground), h * 0.22f, ThrC.Clay, ps * g0, pour = g0 * (1f - seg(p, 0.3f, 0.35f)), t = t)
        tagAt(a.tag("perfectsteps", 0, 0), Offset(w * 0.82f, h * 0.45f), ps * g0, back = Fq.Back, ink = ArtInk.Ivory)
        for (k in 0 until 3) {
            val u = seg(p, 0.35f + 0.08f * k, 0.43f + 0.08f * k)
            if (u > 0f && u < 1f) rain12(w * 0.46f, w * 0.54f, h * 0.15f, h * 0.45f, t * 2f, ThrC.Water, ps, 14)
            fqDrop(Offset(w * (0.36f + 0.03f * k), h * 0.3f), w * 0.012f, ThrC.Gold, ps * seg(u, 0.9f, 1f))
        }
        tagAt(a.tag("perfectsteps", 0, 1), Offset(w * 0.5f, h * 0.08f), ps * seg(p, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        val g2 = seg(p, 0.7f, 0.85f)
        rain12(w * 0.4f, w * 0.6f, h * 0.2f, ground, t * 1.4f, ThrC.Water, ps * g2, 30)
        tagAt(a.tag("perfectsteps", 0, 2), Offset(w * 0.2f, h * 0.6f), ps * g2, back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 11. thr-tayammum: purifying with earth ═════════════

/**
 * Beats: noewater (an empty jug turned over and a dry well, the earth beside them: «وَمِنْ خَصَائِصِ هَذِهِ الْأُمَّةِ», «عِنْدَ الْعَجْزِ عَنِ
 * اسْتِعْمَالِ الْمَاءِ»), strikeearth (two hands strike the ground once, a puff of dust, then wipe: «ضَرْبَةً وَاحِدَةً», «ثُمَّ يَمْسَحُ بِهِمَا وَجْهَهُ
 * وَكَفَّيْهِ»), waterfound (a spring breaks out and the dust's light gives way: «وَبِوُجُودِ الْمَاءِ», «وَبِزَوَالِ الْعُذْرِ»), liftshadath (a drop of
 * earth and a drop of water side by side, held level: «عِنْدَ مُؤَلِّفِي الْكِتَابِ أَنَّهُ رَافِعٌ لِلْحَدَثِ»).
 */
internal fun DrawScope.dryablutionScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "noewater" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "strikeearth" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "waterfound" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "liftshadath" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { dryAblutionDraw(a) }
}

private fun DrawScope.dryAblutionDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.76f
    daySky12(Color(0xFFC8B08A), Color(0xFFF2DCB0), ground, Fq.Sand)
    val nw = fqIn(a, "noewater", "strikeearth")
    val se = fqIn(a, "strikeearth", "waterfound")
    val wf = fqIn(a, "waterfound", "liftshadath")
    val lh = fqIn(a, "liftshadath")
    if (nw > 0f) {
        val p = a.b("noewater")
        val well = Offset(w * 0.7f, ground)
        drawRect(Fq.Stone, Offset(well.x - w * 0.1f, well.y - h * 0.12f), Size(w * 0.2f, h * 0.12f), alpha = nw)
        drawOval(Color(0xFF3A2E24), Offset(well.x - w * 0.08f, well.y - h * 0.14f), Size(w * 0.16f, h * 0.04f), alpha = nw)
        fqEwer(Offset(w * 0.4f, ground), h * 0.2f, ThrC.Clay, nw, pour = 1f * seg(p, 0.2f, 0.4f))
        tagAt(a.tag("noewater", 0, 0), Offset(w * 0.5f, h * 0.14f), nw * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("noewater", 0, 1), Offset(w * 0.5f, h * 0.9f), nw * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (se > 0f) {
        val p = a.b("strikeearth")
        val strike = seg(p, 0.05f, 0.2f)
        val lift = seg(p, 0.35f, 0.55f)
        val y = mix(mix(h * 0.45f, ground - h * 0.06f, strike), h * 0.4f, lift)
        for (sg in listOf(-1f, 1f)) {
            val c = Offset(w * 0.5f + sg * w * 0.08f, y)
            drawRoundRect(Color(0xFFD8B898), Offset(c.x - w * 0.05f, c.y - h * 0.05f), Size(w * 0.1f, h * 0.1f), CornerRadius(w * 0.03f), alpha = se)
        }
        val puff = seg(p, 0.18f, 0.4f)
        for (k in 0 until 10) {
            val ang = PI.toFloat() * (k / 9f)
            drawCircle(Color(0xFFE8D8B0), w * 0.01f, Offset(w * 0.5f, ground) + Offset(-cos(ang), -sin(ang)) * (w * 0.2f * puff), alpha = se * bump(puff))
        }
        tagAt(a.tag("strikeearth", 0, 0), Offset(w * 0.5f, h * 0.14f), se * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val wipe = seg(p, 0.6f, 0.85f)
        drawArc(Fq.Sand, 200f, 140f * wipe, false, Offset(w * 0.4f, h * 0.25f), Size(w * 0.2f, h * 0.2f), alpha = se * wipe, style = Stroke(4f * dp, cap = StrokeCap.Round))
        tagAt(a.tag("strikeearth", 0, 1), Offset(w * 0.5f, h * 0.9f), se * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (wf > 0f) {
        val p = a.b("waterfound")
        val spring = Offset(w * 0.3f, ground)
        val g = seg(p, 0.1f, 0.35f)
        for (k in 0 until 8) {
            val u = fract(t * 0.9f + k / 8f)
            fqDrop(spring + Offset(w * 0.06f * (k - 3.5f) / 3.5f * u, -h * 0.25f * sin(PI.toFloat() * u)), w * 0.01f, ThrC.Water, wf * g * (1f - u * 0.5f))
        }
        drawOval(ThrC.Water, Offset(spring.x - w * 0.1f, ground - h * 0.02f), Size(w * 0.2f, h * 0.04f), alpha = wf * g)
        softGlow(Offset(w * 0.7f, h * 0.5f), w * 0.06f, Fq.Sand, wf * (1f - seg(p, 0.35f, 0.6f)))
        tagAt(a.tag("waterfound", 0, 0), Offset(w * 0.3f, h * 0.2f), wf * g, back = Fq.Back, ink = ArtInk.Jade)
        tagAt(a.tag("waterfound", 0, 1), Offset(w * 0.7f, h * 0.2f), wf * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (lh > 0f) {
        val p = a.b("liftshadath")
        balance12(Offset(w * 0.5f, ground), h * 0.5f, 0f, Fq.Brass, lh)
        fqDrop(Offset(w * 0.33f, h * 0.36f), w * 0.03f, Fq.SandDark, lh * seg(p, 0.1f, 0.3f))
        fqDrop(Offset(w * 0.67f, h * 0.36f), w * 0.03f, ThrC.Water, lh * seg(p, 0.1f, 0.3f))
        tagAt(a.tag("liftshadath"), Offset(w * 0.5f, h * 0.12f), lh * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 12. thr-najasa: impurities and washing them ═════════════

/**
 * Beats: waterremoves (a stained cloth, and water washes the stain away: «وَالْأَصْلُ الَّذِي تُزَالُ بِهِ الْمَاءُ»), groundwash (a patch of
 * ground and one pour of water over it: «غَسْلَةٌ تَذْهَبُ بِعَيْنِهَا»), sevenwash (a bowl, and seven counts beside it, one of them earth:
 * «يُغْسَلُ سَبْعًا إِحْدَاهُنَّ بِالتُّرَابِ»), sprinkle (two cloths: one only sprinkled, the other washed: «يَكْفِي فِيهِ النَّضْحُ», «أَمَّا بَوْلُ
 * الْجَارِيَةِ فَيُغْسَلُ»), rubwring (a cloth rubbed and wrung once until clean: «مَعَ الْفَرْكِ وَالْعَصْرِ», «وَتَكْفِي فِيهَا مَرَّةٌ وَاحِدَةٌ»).
 */
internal fun DrawScope.impuritiesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "waterremoves" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "groundwash" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "sevenwash" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sprinkle" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "rubwring" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { impuritiesDraw(a) }
}

private fun DrawScope.impuritiesDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    thrDark(t)
    drawRect(ThrC.Ground, Offset(0f, ground), Size(w, h - ground))
    val wr = fqIn(a, "waterremoves", "groundwash")
    val gw = fqIn(a, "groundwash", "sevenwash")
    val sw = fqIn(a, "sevenwash", "sprinkle")
    val sp = fqIn(a, "sprinkle", "rubwring")
    val rw = fqIn(a, "rubwring")
    if (wr > 0f) {
        val p = a.b("waterremoves")
        val c = Offset(w * 0.5f, h * 0.48f)
        stainCloth77(c, w * 0.36f, h * 0.4f, seg(p, 0.3f, 0.7f), wr)
        rain12(c.x - w * 0.15f, c.x + w * 0.15f, h * 0.1f, h * 0.68f, t * 1.4f, ThrC.Water, wr * bump(seg(p, 0.2f, 0.8f)), 28)
        tagAt(a.tag("waterremoves"), Offset(w * 0.5f, h * 0.88f), wr * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (gw > 0f) {
        val p = a.b("groundwash")
        val c = Offset(w * 0.45f, ground + h * 0.06f)
        drawOval(Color(0xFF4A3E30), Offset(c.x - w * 0.15f, c.y - h * 0.04f), Size(w * 0.3f, h * 0.08f), alpha = gw * (1f - seg(p, 0.4f, 0.7f)))
        val tip = seg(p, 0.15f, 0.35f)
        fqEwer(Offset(w * 0.72f, ground - h * 0.12f), h * 0.3f, ThrC.Silver, gw, pour = tip, t = t)
        drawOval(ThrC.Water, Offset(c.x - w * 0.15f, c.y - h * 0.04f), Size(w * 0.3f, h * 0.08f), alpha = gw * 0.6f * seg(p, 0.35f, 0.55f))
        tagAt(a.tag("groundwash"), Offset(w * 0.45f, h * 0.2f), gw * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sw > 0f) {
        val p = a.b("sevenwash")
        bowl76(Offset(w * 0.25f, ground), w * 0.24f, Color(0xFF8AA0B8), sw)
        for (k in 0 until 7) {
            val g = seg(p, 0.1f + 0.09f * k, 0.18f + 0.09f * k)
            val c = Offset(w * (0.88f - 0.075f * k), h * 0.5f)
            if (k == 0) fqDrop(c, w * 0.022f, Fq.SandDark, sw * (0.25f + 0.75f * g))
            else fqDrop(c, w * 0.022f, ThrC.Water, sw * (0.25f + 0.75f * g))
        }
        tagAt(a.tag("sevenwash"), Offset(w * 0.6f, h * 0.24f), sw * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sp > 0f) {
        val p = a.b("sprinkle")
        val g0 = seg(p, 0.05f, 0.25f)
        val c0 = Offset(w * 0.72f, h * 0.45f)
        drawRoundRect(Color(0xFFE8E2D4), Offset(c0.x - w * 0.12f, c0.y - h * 0.16f), Size(w * 0.24f, h * 0.32f), CornerRadius(w * 0.02f), alpha = sp * g0)
        for (k in 0 until 9) drawCircle(ThrC.Water, w * 0.008f, c0 + Offset(w * 0.08f * (fract(k * 0.618f) - 0.5f) * 2f, h * 0.1f * (fract(k * 0.381f) - 0.5f) * 2f), alpha = sp * seg(p, 0.15f, 0.3f))
        tagAt(a.tag("sprinkle", 0, 0), Offset(c0.x, h * 0.84f), sp * g0, back = Fq.Back, ink = ArtInk.Jade)
        val g1 = seg(p, 0.5f, 0.7f)
        val c1 = Offset(w * 0.28f, h * 0.45f)
        drawRoundRect(Color(0xFFE8E2D4), Offset(c1.x - w * 0.12f, c1.y - h * 0.16f), Size(w * 0.24f, h * 0.32f), CornerRadius(w * 0.02f), alpha = sp * g1)
        rain12(c1.x - w * 0.1f, c1.x + w * 0.1f, h * 0.1f, h * 0.62f, t * 1.4f, ThrC.Water, sp * g1, 24)
        tagAt(a.tag("sprinkle", 0, 1), Offset(c1.x, h * 0.84f), sp * g1, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (rw > 0f) {
        val p = a.b("rubwring")
        val c = Offset(w * 0.5f, h * 0.45f)
        val twist = seg(p, 0.35f, 0.7f)
        val clean = seg(p, 0.3f, 0.75f)
        if (twist < 0.3f) stainCloth77(c, w * 0.36f, h * 0.36f, clean, rw)
        else {
            for (k in 0 until 6) {
                val x = c.x - w * 0.2f + w * 0.08f * k
                drawLine(Color(0xFFE8E2D4), Offset(x, c.y - h * 0.05f * sin(k + t * 3f)), Offset(x + w * 0.08f, c.y + h * 0.05f * sin(k + t * 3f)), h * 0.07f, StrokeCap.Round, alpha = rw)
            }
            for (k in 0 until 5) {
                val u = fract(t * 1.2f + k / 5f)
                fqDrop(Offset(c.x - w * 0.1f + w * 0.05f * k, c.y + h * 0.06f + u * h * 0.25f), w * 0.008f, ThrC.Water, rw * (1f - u))
            }
        }
        tagAt(a.tag("rubwring", 0, 0), Offset(w * 0.5f, h * 0.12f), rw * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tick14(Offset(w * 0.82f, h * 0.45f), w * 0.04f, seg(p, 0.75f, 0.9f))
        tagAt(a.tag("rubwring", 0, 1), Offset(w * 0.5f, h * 0.88f), rw * seg(p, 0.75f, 0.9f), back = Fq.Back, ink = ArtInk.Jade)
    }
}
