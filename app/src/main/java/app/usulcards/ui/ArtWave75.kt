package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 75 (2026-11-03): v1.3 fiqh, «زاد المعتكف» (al-ʿAmmārī). Staying in the mosque; only a mosque, with the intention; when one goes
 * in and comes out; what he may go out for; and the author's counsel to the one in seclusion. The one in iʿtikāf is a faceless
 * figure; the three mosques are their buildings only.
 */

private object ItkC {
    val Night = Color(0xFF0E1430)
    val Low = Color(0xFF2E2A5A)
    val Ground = Color(0xFF1A1826)
    val Mosque = Color(0xFFD8CCB4)
    val Dome = Color(0xFF4E7A6A)
    val Person = Color(0xFFE8DCC8)
    val Gold = Color(0xFFF2C860)
    val Thread = Color(0xFF8A94B8)
    val Hotel = Color(0xFF4A4E62)
    val Window = Color(0xFFF2D890)
}

/** A mosque seen from outside with its door lit, its base centred on [foot]; [open] lights the doorway. */
private fun DrawScope.mosqueFront75(foot: Offset, s: Float, alpha: Float, open: Float) {
    if (alpha <= 0.01f) return
    drawRect(ItkC.Mosque, Offset(foot.x - s * 0.5f, foot.y - s * 0.5f), Size(s, s * 0.5f), alpha = alpha)
    drawArc(ItkC.Dome, 180f, 180f, true, Offset(foot.x - s * 0.26f, foot.y - s * 0.76f), Size(s * 0.52f, s * 0.52f), alpha = alpha)
    drawRect(ItkC.Mosque, Offset(foot.x + s * 0.42f, foot.y - s * 0.95f), Size(s * 0.08f, s * 0.95f), alpha = alpha)
    val door = archPath26(foot.x, foot.y - s * 0.32f, foot.y, s * 0.09f)
    drawPath(door, lerpColor(Color(0xFF2A2430), ItkC.Gold, open), alpha = alpha)
    if (open > 0f) softGlow(Offset(foot.x, foot.y - s * 0.15f), s * 0.2f, ItkC.Gold, 0.5f * alpha * open)
}

/** A phone, its screen glowing by [lit]. */
private fun DrawScope.phone75(c: Offset, s: Float, lit: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    drawRoundRect(Color(0xFF2A2E3A), Offset(c.x - s * 0.28f, c.y - s * 0.5f), Size(s * 0.56f, s), CornerRadius(s * 0.08f), alpha = alpha)
    drawRoundRect(lerpColor(Color(0xFF1A1E28), Color(0xFF8AC4EA), lit), Offset(c.x - s * 0.23f, c.y - s * 0.42f), Size(s * 0.46f, s * 0.8f), CornerRadius(s * 0.04f), alpha = alpha)
}

private fun DrawScope.itkNight(t: Float, ground: Float) {
    nightSky(t, ItkC.Night, ItkC.Low, ground, starAlpha = 0.7f)
    drawRect(ItkC.Ground, Offset(0f, ground), Size(size.width, size.height - ground))
}

// ═════════════ 6. itk-hukm: what iʿtikāf is ═════════════

/**
 * Beats: staymosque (in the lit hall one sits; outside the window the moon and sun come and go and he stays: «لُزُومُ الْمَسْجِدِ»),
 * cutties (threads from him to a purse, a house and a busy road thin and fade: «وَانْقِطَاعٌ تَامٌّ عَنْ شَوَاغِلِ الدُّنْيَا»), sunnahvow (a
 * soft light, then a vow ties it fast with a lock: «سُنَّةٌ», «إِلَّا أَنْ يُوجِبَهُ الْمَرْءُ عَلَى نَفْسِهِ نَذْرًا»), lasttenstay (thirty days,
 * the last ten lit: «فِي الْعَشْرِ الْأَوَاخِرِ مِنْ رَمَضَانَ»), threemosques (the three mosques in order, the House first: «الْمَسْجِدُ
 * الْحَرَامُ», «ثُمَّ الْمَسْجِدُ النَّبَوِيُّ», «ثُمَّ الْمَسْجِدُ الْأَقْصَى»).
 */
internal fun DrawScope.itikafstayScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "staymosque" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "cutties" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "sunnahvow" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "lasttenstay" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "threemosques" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { itikafStayDraw(a) }
}

private fun DrawScope.itikafStayDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.66f
    hall74(floor, t)
    val sm = fqIn(a, "staymosque", "sunnahvow")
    val ct = fqIn(a, "cutties", "sunnahvow")
    val sv = fqIn(a, "sunnahvow", "lasttenstay")
    val lt = fqIn(a, "lasttenstay", "threemosques")
    val tm = fqIn(a, "threemosques")
    val seat = Offset(w * 0.5f, floor + h * 0.2f)
    if (sm > 0f) {
        val p = a.b("staymosque")
        val win = Offset(w * 0.125f, h * 0.36f)
        val cyc = fract(t * 0.12f)
        drawRect(lerpColor(ItkC.Night, Color(0xFF8AB8DA), bump(cyc)), Offset(win.x - w * 0.05f, win.y - h * 0.1f), Size(w * 0.1f, h * 0.2f), alpha = sm)
        if (cyc < 0.5f) moon12(win + Offset(0f, -h * 0.03f), w * 0.015f, sm * (1f - bump(cyc * 2f) * 0.5f)) else sun12(win, w * 0.018f, sm)
        sit12(seat, h * 0.26f, ItkC.Person, sm, -1f)
        softGlow(seat + Offset(0f, -h * 0.15f), w * 0.1f, ItkC.Gold, 0.35f * sm)
        tagAt(a.tag("staymosque"), Offset(w * 0.6f, h * 0.24f), sm * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ct > 0f) {
        val p = a.b("cutties")
        val from = seat + Offset(0f, -h * 0.15f)
        val ends = listOf(Offset(w * 0.85f, h * 0.2f), Offset(w * 0.85f, h * 0.55f), Offset(w * 0.18f, h * 0.18f))
        for (k in 0 until 3) {
            val fade = seg(p, 0.3f + 0.15f * k, 0.5f + 0.15f * k)
            val e = ends[k]
            drawLine(ItkC.Thread, from, e, 1.5f * dp, alpha = ct * (1f - fade), pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
            val ia = ct * (1f - 0.7f * fade)
            when (k) {
                0 -> fqCoins(e + Offset(0f, h * 0.03f), w * 0.03f, 4, ia)
                1 -> house12(e + Offset(0f, h * 0.05f), w * 0.1f, Color(0xFFCDBFA6), Color(0xFF6A5A48), ia)
                else -> phone75(e, h * 0.12f, 1f - fade, ia)
            }
        }
        tagAt(a.tag("cutties"), Offset(w * 0.5f, h * 0.9f), ct * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sv > 0f) {
        val p = a.b("sunnahvow")
        drawRect(ItkC.Night, Offset.Zero, size, alpha = 0.75f * sv)
        val c = Offset(w * 0.5f, h * 0.5f)
        softGlow(c, w * 0.15f, ItkC.Gold, 0.5f * sv * seg(p, 0.05f, 0.25f))
        drawCircle(ItkC.Gold, w * 0.03f, c, alpha = sv * seg(p, 0.05f, 0.25f))
        tagAt(a.tag("sunnahvow", 0, 0), c + Offset(0f, -h * 0.2f), sv * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        val tie = seg(p, 0.45f, 0.65f)
        drawCircle(ItkC.Thread, w * 0.08f * (1.4f - 0.4f * tie), c, alpha = sv * tie, style = Stroke(3f * dp))
        padlock21(c + Offset(w * 0.1f, h * 0.08f), w * 0.07f, Color(0xFFB8A078), 1f - seg(p, 0.6f, 0.75f), sv * tie)
        tagAt(a.tag("sunnahvow", 0, 1), c + Offset(0f, h * 0.24f), sv * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (lt > 0f) {
        val p = a.b("lasttenstay")
        drawRect(ItkC.Night, Offset.Zero, size, alpha = 0.75f * lt)
        val cell = w * 0.024f
        for (i in 0 until 30) {
            val c = Offset(w * 0.93f - i * cell * 1.27f, h * 0.5f)
            val last = i >= 20
            val g = if (last) seg(p, 0.2f + 0.02f * (i - 20), 0.35f + 0.02f * (i - 20)) else 0f
            drawRoundRect(if (last) ItkC.Gold else ItkC.Thread, Offset(c.x - cell / 2f, c.y - cell / 2f), Size(cell, cell), CornerRadius(cell * 0.2f), alpha = lt * (0.35f + 0.6f * g))
            if (last) softGlow(c, cell * 1.4f, ItkC.Gold, 0.35f * lt * g)
        }
        tagAt(a.tag("lasttenstay"), Offset(w * 0.25f, h * 0.36f), lt * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tm > 0f) {
        val p = a.b("threemosques")
        itkNight(t, h * 0.72f)
        val ground = h * 0.72f
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val x = w * (0.8f - 0.3f * k)
            softGlow(Offset(x, ground - h * 0.12f), w * 0.1f, ItkC.Gold, 0.3f * tm * g)
            when (k) {
                0 -> kaaba29(Offset(x, ground), w * 0.14f, tm * g)
                1 -> domed29(Offset(x, ground), w * 0.2f, ItkC.Mosque, Color(0xFF3E8A5E), tm * g)
                else -> domed29(Offset(x, ground), w * 0.2f, ItkC.Mosque, Color(0xFF8A8C94), tm * g)
            }
            tagAt(a.tag("threemosques", 0, k), Offset(x, ground + h * 0.1f), tm * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 7. itk-masjid: only in a mosque ═════════════

/**
 * Beats: mosqueandintent (a mosque, and a light in the heart of one going in: «إِلَّا فِي مَسْجِدٍ», «وَبِنِيَّةِ الِاعْتِكَافِ»), amoment (one steps
 * in and sits; a clock ticks a single moment: «وَأَقَلُّهُ لَحْظَةٌ», «عِنْدَ جُمْهُورِ الْفُقَهَاءِ»), fastoptional (the crescent of fasting beside
 * him, not required, then lit as the better way: «وَلَا يُشْتَرَطُ لَهُ الصَّوْمُ», «وَيُسْتَحَبُّ أَنْ يَصُومَ الْمُعْتَكِفُ»), nothome (the mosque
 * shut, and a house struck out: «لَمْ يَصِحَّ فِي الْبَيْتِ»), hotelhall (a tall hotel by the sanctuary, its prayer room struck out: «لِأَنَّهَا
 * لَا تُعَدُّ مَسْجِدًا»), insidewall (from above: the mosque's wall; a room inside it ticked, a room outside it struck out: «دَاخِلَ سُورِ
 * الْمَسْجِدِ», «وَإِنْ كَانَتْ خَارِجَهُ لَمْ يَصِحَّ»).
 */
internal fun DrawScope.mosqueonlyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "mosqueandintent" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "amoment" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "fastoptional" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "nothome" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "hotelhall" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "insidewall" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { mosqueOnlyDraw(a) }
}

private fun DrawScope.mosqueOnlyDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    itkNight(t, ground)
    val mi = fqIn(a, "mosqueandintent", "amoment")
    val am = fqIn(a, "amoment", "fastoptional")
    val fo = fqIn(a, "fastoptional", "nothome")
    val nh = fqIn(a, "nothome", "hotelhall")
    val hh = fqIn(a, "hotelhall", "insidewall")
    val iw = fqIn(a, "insidewall")
    val mosque = Offset(w * 0.62f, ground)
    if (mi > 0f || am > 0f) {
        val f = maxOf(mi, am)
        mosqueFront75(mosque, w * 0.42f, f, 0.6f)
    }
    if (mi > 0f) {
        val p = a.b("mosqueandintent")
        val foot = Offset(mix(w * 0.15f, w * 0.4f, easeOut(seg(p, 0.05f, 0.5f))), ground)
        figure(foot, h * 0.28f, ItkC.Person, mi, walk = if (p < 0.5f) t * 6f else 0f)
        val heart = foot + Offset(0f, -h * 0.19f)
        softGlow(heart, w * 0.06f, ItkC.Gold, mi * seg(p, 0.45f, 0.65f))
        heart12(heart, w * 0.03f, ItkC.Gold, mi * seg(p, 0.45f, 0.65f), fill = true)
        tagAt(a.tag("mosqueandintent", 0, 0), Offset(w * 0.62f, h * 0.14f), mi * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("mosqueandintent", 0, 1), Offset(w * 0.28f, h * 0.3f), mi * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (am > 0f) {
        val p = a.b("amoment")
        val enter = easeInOut(seg(p, 0f, 0.35f))
        sit12(Offset(mix(w * 0.4f, mosque.x, enter), ground), h * 0.2f, ItkC.Person, am * (1f - 0.5f * enter), -1f)
        clock22(Offset(w * 0.2f, h * 0.35f), w * 0.07f, ItkC.Gold, seg(p, 0.4f, 0.5f) * 0.08f, am)
        tagAt(a.tag("amoment", 0, 0), Offset(w * 0.2f, h * 0.16f), am * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("amoment", 0, 1), Offset(w * 0.62f, h * 0.14f), am * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (fo > 0f) {
        val p = a.b("fastoptional")
        hall74(h * 0.66f, t)
        sit12(Offset(w * 0.55f, h * 0.86f), h * 0.24f, ItkC.Person, fo, -1f)
        val c = Offset(w * 0.25f, h * 0.36f)
        crescent12(c, w * 0.04f, ItkC.Gold, Color(0xFF2A2436), fo * (0.4f + 0.6f * seg(p, 0.55f, 0.75f)))
        softGlow(c, w * 0.07f, ItkC.Gold, 0.5f * fo * seg(p, 0.55f, 0.75f))
        tagAt(a.tag("fastoptional", 0, 0), Offset(w * 0.6f, h * 0.2f), fo * seg(p, 0.1f, 0.3f) * (1f - seg(p, 0.5f, 0.6f)), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("fastoptional", 0, 1), Offset(w * 0.6f, h * 0.2f), fo * seg(p, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nh > 0f) {
        val p = a.b("nothome")
        mosqueFront75(Offset(w * 0.3f, ground), w * 0.34f, nh * 0.6f, 0f)
        drawLine(Color(0xFF8A6A48), Offset(w * 0.27f, ground - h * 0.08f), Offset(w * 0.33f, ground - h * 0.08f), 3f * dp, alpha = nh)
        house12(Offset(w * 0.74f, ground), w * 0.22f, Color(0xFFCDBFA6), Color(0xFF6A5A48), nh, lit = 0.4f)
        cross14(Offset(w * 0.74f, ground - h * 0.34f), w * 0.05f, seg(p, 0.35f, 0.55f))
        tagAt(a.tag("nothome"), Offset(w * 0.5f, h * 0.14f), nh * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (hh > 0f) {
        val p = a.b("hotelhall")
        kaaba29(Offset(w * 0.25f, ground), w * 0.12f, hh)
        softGlow(Offset(w * 0.25f, ground - h * 0.08f), w * 0.1f, ItkC.Gold, 0.3f * hh)
        val tower = Offset(w * 0.7f, ground)
        drawRect(ItkC.Hotel, Offset(tower.x - w * 0.12f, h * 0.12f), Size(w * 0.24f, ground - h * 0.12f), alpha = hh)
        for (r in 0 until 8) for (c in 0 until 4) drawRect(ItkC.Window, Offset(tower.x - w * 0.1f + c * w * 0.055f, h * 0.16f + r * h * 0.075f), Size(w * 0.03f, h * 0.035f), alpha = hh * (0.3f + 0.4f * fract((r * 4 + c) * 0.37f)))
        val room = Offset(tower.x, h * 0.52f)
        drawRect(ItkC.Gold, Offset(room.x - w * 0.08f, room.y - h * 0.05f), Size(w * 0.16f, h * 0.1f), alpha = hh * seg(p, 0.2f, 0.35f) * 0.7f)
        cross14(room + Offset(w * 0.16f, 0f), w * 0.045f, seg(p, 0.45f, 0.65f))
        tagAt(a.tag("hotelhall"), Offset(w * 0.4f, h * 0.88f), hh * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (iw > 0f) {
        val p = a.b("insidewall")
        drawRect(ItkC.Night, Offset.Zero, size, alpha = 0.85f * iw)
        val wall = Offset(w * 0.55f, h * 0.5f)
        val ww = w * 0.5f
        val wh = h * 0.56f
        drawRect(ItkC.Mosque, Offset(wall.x - ww / 2f, wall.y - wh / 2f), Size(ww, wh), alpha = iw * 0.15f)
        drawRect(ItkC.Mosque, Offset(wall.x - ww / 2f, wall.y - wh / 2f), Size(ww, wh), alpha = iw, style = Stroke(4f * dp))
        drawCircle(ItkC.Dome, wh * 0.18f, wall + Offset(-ww * 0.1f, 0f), alpha = iw * 0.8f)
        val inRoom = Offset(wall.x + ww * 0.32f, wall.y + wh * 0.32f)
        drawRect(ItkC.Gold, Offset(inRoom.x - ww * 0.12f, inRoom.y - wh * 0.13f), Size(ww * 0.24f, wh * 0.26f), alpha = iw * 0.6f * seg(p, 0.1f, 0.25f))
        tick14(inRoom, w * 0.035f, seg(p, 0.2f, 0.35f))
        tagAt(a.tag("insidewall", 0, 0), Offset(wall.x + ww * 0.1f, wall.y - wh * 0.62f), iw * seg(p, 0.15f, 0.35f), back = Fq.Back, ink = ArtInk.Jade)
        val outRoom = Offset(wall.x - ww * 0.5f - w * 0.12f, wall.y + wh * 0.3f)
        drawRect(Color(0xFF8A94B8), Offset(outRoom.x - w * 0.06f, outRoom.y - h * 0.07f), Size(w * 0.12f, h * 0.14f), alpha = iw * 0.6f * seg(p, 0.5f, 0.65f))
        cross14(outRoom, w * 0.035f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("insidewall", 0, 1), Offset(w * 0.3f, h * 0.92f), iw * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ember)
    }
}

// ═════════════ 8. itk-dukhul: when one goes in and comes out ═════════════

/**
 * Beats: enterssunset (the sun sets on the twentieth day and one goes in through the lit door: «مَعَ غُرُوبِ شَمْسِ لَيْلَةِ الْحَادِي
 * وَالْعِشْرِينَ»), entersdawn (the other view: he goes in after the dawn prayer; both doors stay lit: «بَعْدَ صَلَاةِ الصُّبْحِ», «مُحْتَمِلَةٌ
 * لِلْقَوْلَيْنِ»), leavesforeid (on the eid morning he walks from the mosque to the prayer ground: «إِلَى صَلَاةِ الْعِيدِ»), vowthree (a vow
 * tied to one of the three mosques; the road to a lesser one is struck out: «لَزِمَهُ الْوَفَاءُ فِيهِ»).
 */
internal fun DrawScope.itikaftimesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "enterssunset" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "entersdawn" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "leavesforeid" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "vowthree" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { itikafTimesDraw(a) }
}

private fun DrawScope.itikafTimesDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    val es = fqIn(a, "enterssunset", "entersdawn")
    val ed = fqIn(a, "entersdawn", "leavesforeid")
    val le = fqIn(a, "leavesforeid", "vowthree")
    val vt = fqIn(a, "vowthree")
    val dusk = es
    val dawn = ed + le
    nightSky(t, ItkC.Night, lerpColor(lerpColor(ItkC.Low, Color(0xFFE8906A), dusk), Color(0xFFF2D8B0), dawn.coerceAtMost(1f)), ground, starAlpha = 0.7f * (1f - dawn.coerceAtMost(1f)))
    drawRect(ItkC.Ground, Offset(0f, ground), Size(w, h - ground))
    val mosque = Offset(w * 0.68f, ground)
    if (es > 0f || ed > 0f) mosqueFront75(mosque, w * 0.4f, maxOf(es, ed), 0.8f)
    if (es > 0f) {
        val p = a.b("enterssunset")
        val sun = Offset(w * 0.12f, mix(ground - h * 0.15f, ground + h * 0.05f, easeInOut(seg(p, 0f, 0.5f))))
        sun12(sun, w * 0.045f, es * (1f - seg(p, 0.45f, 0.55f)))
        drawRect(ItkC.Ground, Offset(0f, ground), Size(w * 0.3f, h * 0.1f), alpha = es)
        val u = easeInOut(seg(p, 0.4f, 0.9f))
        figure(Offset(mix(w * 0.3f, mosque.x, u), ground), h * 0.26f * (1f - 0.3f * u), ItkC.Person, es * (1f - seg(p, 0.85f, 1f)), walk = t * 6f)
        tagAt(a.tag("enterssunset"), Offset(w * 0.45f, h * 0.16f), es * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ed > 0f) {
        val p = a.b("entersdawn")
        sun12(Offset(w * 0.12f, mix(ground + h * 0.05f, ground - h * 0.12f, easeOut(seg(p, 0.1f, 0.5f)))), w * 0.045f, ed)
        drawRect(ItkC.Ground, Offset(0f, ground), Size(w * 0.3f, h * 0.1f), alpha = ed)
        val u = easeInOut(seg(p, 0.3f, 0.8f))
        figure(Offset(mix(w * 0.3f, mosque.x, u), ground), h * 0.26f * (1f - 0.3f * u), ItkC.Person, ed * (1f - seg(p, 0.8f, 0.95f)), walk = t * 6f)
        tagAt(a.tag("entersdawn", 0, 0), Offset(w * 0.45f, h * 0.16f), ed * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("entersdawn", 0, 1), Offset(w * 0.45f, h * 0.3f), ed * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (le > 0f) {
        val p = a.b("leavesforeid")
        mosqueFront75(Offset(w * 0.82f, ground), w * 0.3f, le, 0.8f)
        fqEidMoon(Offset(w * 0.15f, h * 0.15f), w * 0.03f, Color(0xFFB8C8E0), le)
        val ground2 = Offset(w * 0.2f, ground)
        drawRect(Color(0xFFD8C8A0), Offset(ground2.x - w * 0.15f, ground - h * 0.01f), Size(w * 0.3f, h * 0.02f), alpha = le)
        row74(ground, w * 0.1f, w * 0.3f, 3, h * 0.16f, ItkC.Person, le * seg(p, 0.6f, 0.75f))
        val u = easeInOut(seg(p, 0.1f, 0.65f))
        figure(Offset(mix(w * 0.8f, w * 0.38f, u), ground), h * 0.24f, ItkC.Person, le, walk = if (u < 1f) t * 6f else 0f)
        tagAt(a.tag("leavesforeid"), Offset(w * 0.5f, h * 0.2f), le * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (vt > 0f) {
        val p = a.b("vowthree")
        drawRect(ItkC.Night, Offset.Zero, size, alpha = 0.8f * vt)
        val vow = Offset(w * 0.5f, h * 0.2f)
        scroll14(vow, w * 0.18f, h * 0.12f, seg(p, 0f, 0.2f), vt)
        val chosen = Offset(w * 0.72f, ground)
        val other = Offset(w * 0.28f, ground)
        domed29(chosen, w * 0.22f, ItkC.Mosque, Color(0xFF3E8A5E), vt)
        softGlow(chosen + Offset(0f, -h * 0.12f), w * 0.12f, ItkC.Gold, 0.4f * vt * seg(p, 0.3f, 0.5f))
        drawLine(ItkC.Gold, vow + Offset(0f, h * 0.06f), chosen + Offset(0f, -h * 0.26f), 2.5f * dp, StrokeCap.Round, alpha = vt * seg(p, 0.2f, 0.4f))
        domed29(other, w * 0.18f, ItkC.Mosque, ItkC.Dome, vt * 0.7f)
        drawLine(ItkC.Thread, vow + Offset(0f, h * 0.06f), other + Offset(0f, -h * 0.22f), 1.5f * dp, alpha = vt * seg(p, 0.5f, 0.6f), pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        cross14((vow + other) / 2f + Offset(0f, h * 0.05f), w * 0.035f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("vowthree"), Offset(w * 0.72f, ground + h * 0.1f), vt * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 9. itk-khuruj: going out of the mosque ═════════════

/**
 * Beats: outvoid (he walks out without need and the light he sat in breaks: «بِلَا عُذْرٍ يُبْطِلُ اعْتِكَافَهُ»), needout (three short trips out
 * and back, for washing, for food, for a needed purification: «كَقَضَاءِ الْحَاجَةِ», «وَالطَّعَامِ», «وَالطَّهَارَةِ الْوَاجِبَةِ»), onlyneed (a
 * short arc of time outside, and back at once: «الْحَاجَةُ تُقَدَّرُ بِقَدْرِهَا»), statedcondition (a sickbed and a bier outside, the way to
 * them shut unless a condition was written at the door: «إِلَّا إِذَا اشْتَرَطَ ذَلِكَ عِنْدَ دُخُولِهِ»).
 */
internal fun DrawScope.leavingmosqueScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "outvoid" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "needout" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "onlyneed" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "statedcondition" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { leavingMosqueDraw(a) }
}

private fun DrawScope.leavingMosqueDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    itkNight(t, ground)
    val ov = fqIn(a, "outvoid", "needout")
    val no = fqIn(a, "needout", "onlyneed")
    val on = fqIn(a, "onlyneed", "statedcondition")
    val sc = fqIn(a, "statedcondition")
    val mosque = Offset(w * 0.72f, ground)
    mosqueFront75(mosque, w * 0.44f, 1f, 0.7f)
    val door = mosque + Offset(0f, -h * 0.02f)
    if (ov > 0f) {
        val p = a.b("outvoid")
        val u = easeInOut(seg(p, 0.1f, 0.6f))
        figure(Offset(mix(mosque.x, w * 0.2f, u), ground), h * 0.26f, ItkC.Person, ov * seg(p, 0f, 0.1f), walk = if (u in 0.01f..0.99f) t * 6f else 0f)
        val brk = seg(p, 0.5f, 0.7f)
        for (k in 0 until 6) {
            val ang = 2f * PI.toFloat() * k / 6f
            drawCircle(ItkC.Gold, w * 0.012f, door + Offset(0f, -h * 0.12f) + Offset(cos(ang), sin(ang)) * (w * 0.04f + w * 0.12f * brk), alpha = ov * (1f - brk))
        }
        cross14(Offset(w * 0.2f, ground - h * 0.38f), w * 0.045f, seg(p, 0.6f, 0.8f))
        tagAt(a.tag("outvoid"), Offset(w * 0.4f, h * 0.15f), ov * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ember)
    }
    if (no > 0f) {
        val p = a.b("needout")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val spot = Offset(w * (0.12f + 0.16f * k), ground - h * (0.06f + 0.14f * k))
            drawLine(ItkC.Thread, door, spot, 1.5f * dp, alpha = no * g, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
            when (k) {
                0 -> door12(spot + Offset(0f, h * 0.06f), w * 0.05f, h * 0.1f, 0f, Color(0xFF8A94B8), Color(0xFF5A6278), no * g)
                1 -> dateBowl72(spot + Offset(0f, h * 0.04f), w * 0.09f, no * g)
                else -> fqDrop(spot, w * 0.022f, Fq.Water, no * g)
            }
            val u = fract(t * 0.35f + k / 3f)
            drawCircle(ItkC.Person, w * 0.01f, door + (spot - door) * bump(u), alpha = no * g)
            tagAt(a.tag("needout", 0, k), spot + Offset(0f, -h * 0.09f), no * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (on > 0f) {
        val p = a.b("onlyneed")
        val c = Offset(w * 0.28f, h * 0.45f)
        val r = w * 0.1f
        drawCircle(ItkC.Thread, r, c, alpha = on * 0.4f, style = Stroke(3f * dp))
        drawArc(ItkC.Gold, -90f, 60f * seg(p, 0.1f, 0.4f), false, Offset(c.x - r, c.y - r), Size(r * 2f, r * 2f), alpha = on, style = Stroke(5f * dp, cap = StrokeCap.Round))
        val go = seg(p, 0.1f, 0.3f) - seg(p, 0.35f, 0.55f)
        figure(Offset(mix(mosque.x - w * 0.05f, w * 0.48f, go.coerceIn(0f, 1f)), ground), h * 0.24f, ItkC.Person, on, walk = t * 6f)
        tagAt(a.tag("onlyneed"), Offset(w * 0.4f, h * 0.15f), on * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sc > 0f) {
        val p = a.b("statedcondition")
        val bed = Offset(w * 0.18f, ground - h * 0.05f)
        drawRoundRect(Color(0xFF6A5A4A), Offset(bed.x - w * 0.1f, bed.y), Size(w * 0.2f, h * 0.04f), CornerRadius(h * 0.01f), alpha = sc)
        drawRoundRect(Color(0xFFDAD2C2), Offset(bed.x - w * 0.08f, bed.y - h * 0.03f), Size(w * 0.14f, h * 0.035f), CornerRadius(h * 0.015f), alpha = sc)
        drawCircle(ItkC.Person, h * 0.02f, bed + Offset(w * 0.08f, -h * 0.02f), alpha = sc)
        val bier = Offset(w * 0.38f, ground - h * 0.06f)
        drawRect(Color(0xFF6A5A4A), Offset(bier.x - w * 0.09f, bier.y), Size(w * 0.18f, h * 0.02f), alpha = sc)
        drawRoundRect(Color(0xFF3E6A50), Offset(bier.x - w * 0.08f, bier.y - h * 0.045f), Size(w * 0.16f, h * 0.045f), CornerRadius(h * 0.02f), alpha = sc)
        scroll14(door + Offset(-w * 0.12f, -h * 0.3f), w * 0.14f, h * 0.1f, seg(p, 0.4f, 0.6f), sc * seg(p, 0.4f, 0.5f))
        val open = seg(p, 0.6f, 0.75f)
        drawLine(lerpColor(Color(0xFFC0503A), ItkC.Gold, open), door, bier + Offset(w * 0.08f, 0f), 2f * dp, alpha = sc, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        tagAt(a.tag("statedcondition"), Offset(w * 0.45f, h * 0.15f), sc * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 10. itk-adab: counsel to the one in seclusion ═════════════

/**
 * Beats: knockdoor (a tall door of light, and one seated before it alone: «لِيَقْرَعَ بَابَ مَلِكِ الْمُلُوكِ»), nocrowd (a ring of talkers gathers
 * round him and he lets it fade back to solitude: «فَلَا يَجْعَلُ مِنْ خَلْوَتِهِ جَلْوَةً»), phonejail (a phone goes into a closed box and its
 * buzzing stops: «وَبِحَبْسِ جَوَّالِهِ فِي سِجْنِ الصَّمْتِ»), silencespeechsight (closed lips become a thought, words become beads of dhikr,
 * the eye looks up at the stars: «مِنْ صَمْتِهِ فِكْرًا», «وَمِنْ نُطْقِهِ ذِكْرًا», «وَمِنْ نَظَرِهِ عِبْرَةً»), notebook (a little book where he
 * writes down what comes to him: «وَيُقَيِّدُ مَا يَطْرَأُ عَلَى ذِهْنِهِ»).
 */
internal fun DrawScope.seclusionadviceScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "knockdoor" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "nocrowd" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "phonejail" to Shot(Offset(w * 0.5f, h * 0.5f), 1.08f),
        "silencespeechsight" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "notebook" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { seclusionAdviceDraw(a) }
}

private fun DrawScope.seclusionAdviceDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.66f
    hall74(floor, t, lamps = 0.6f)
    val kd = fqIn(a, "knockdoor", "nocrowd")
    val nc = fqIn(a, "nocrowd", "phonejail")
    val pj = fqIn(a, "phonejail", "silencespeechsight")
    val ss = fqIn(a, "silencespeechsight", "notebook")
    val nb = fqIn(a, "notebook")
    val seat = Offset(w * 0.6f, floor + h * 0.22f)
    if (kd > 0f) {
        val p = a.b("knockdoor")
        val door = Offset(w * 0.25f, floor + h * 0.2f)
        val g = seg(p, 0.05f, 0.35f)
        softGlow(door + Offset(0f, -h * 0.3f), w * 0.2f, ItkC.Gold, 0.6f * kd * g)
        drawPath(archPath26(door.x, door.y - h * 0.62f, door.y, w * 0.11f), ItkC.Gold, alpha = kd * g * 0.6f)
        drawPath(archPath26(door.x, door.y - h * 0.62f, door.y, w * 0.11f), ItkC.Gold, alpha = kd * g, style = Stroke(3f * dp))
        sit12(seat, h * 0.24f, ItkC.Person, kd, -1f)
        tagAt(a.tag("knockdoor"), Offset(w * 0.62f, h * 0.24f), kd * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (nc > 0f) {
        val p = a.b("nocrowd")
        sit12(seat, h * 0.24f, ItkC.Person, nc, -1f)
        val gather = seg(p, 0.05f, 0.35f) * (1f - seg(p, 0.55f, 0.8f))
        for (k in 0 until 5) {
            val ang = PI.toFloat() * (0.1f + 0.2f * k)
            val foot = seat + Offset(-cos(ang) * w * 0.28f, 0f) + Offset(0f, -sin(ang) * h * 0.02f)
            sit12(foot, h * 0.2f, Color(0xFF9A92A8), nc * gather, if (foot.x < seat.x) 1f else -1f)
            voice12(foot + Offset(0f, -h * 0.22f), w * 0.03f, t + k, Color(0xFF9A92A8), nc * gather * 0.8f, if (foot.x < seat.x) 1f else -1f)
        }
        softGlow(seat + Offset(0f, -h * 0.12f), w * 0.1f, ItkC.Gold, 0.5f * nc * seg(p, 0.6f, 0.85f))
        tagAt(a.tag("nocrowd"), Offset(w * 0.5f, h * 0.2f), nc * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (pj > 0f) {
        val p = a.b("phonejail")
        drawRect(ItkC.Night, Offset.Zero, size, alpha = 0.7f * pj)
        val c = Offset(w * 0.5f, h * 0.5f)
        val down = easeInOut(seg(p, 0.15f, 0.45f))
        val ph = Offset(c.x, mix(h * 0.22f, c.y + h * 0.02f, down))
        phone75(ph, h * 0.2f, 1f - seg(p, 0.5f, 0.6f), pj)
        val buzz = 1f - seg(p, 0.45f, 0.55f)
        voice12(ph + Offset(w * 0.08f, 0f), w * 0.05f, t * 2f, Color(0xFF8AC4EA), pj * buzz, 1f)
        voice12(ph - Offset(w * 0.08f, 0f), w * 0.05f, t * 2f, Color(0xFF8AC4EA), pj * buzz, -1f)
        val box = Offset(c.x, c.y + h * 0.12f)
        drawRect(Color(0xFF5A4A3E), Offset(box.x - w * 0.12f, box.y - h * 0.2f), Size(w * 0.24f, h * 0.2f), alpha = pj * 0.85f)
        val lid = seg(p, 0.5f, 0.65f)
        drawRect(Color(0xFF7A6248), Offset(box.x - w * 0.13f, box.y - h * 0.2f - h * 0.05f * (1f - lid)), Size(w * 0.26f, h * 0.03f), alpha = pj * lid)
        tagAt(a.tag("phonejail"), Offset(w * 0.5f, h * 0.86f), pj * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ss > 0f) {
        val p = a.b("silencespeechsight")
        drawRect(ItkC.Night, Offset.Zero, size, alpha = 0.7f * ss)
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.55f)
            when (k) {
                0 -> {
                    lips73(c, w * 0.12f, ItkC.Person, ss * g)
                    for (j in 0 until 3) drawCircle(ItkC.Gold, w * (0.008f + 0.006f * j), c + Offset(w * 0.03f * j, -h * (0.1f + 0.06f * j)), alpha = ss * g)
                }
                1 -> {
                    lips73(c + Offset(0f, h * 0.06f), w * 0.1f, ItkC.Person, ss * g)
                    beads12(c + Offset(0f, -h * 0.08f), w * 0.06f, ItkC.Gold, ss * g, t * 0.4f)
                }
                else -> {
                    eye73(c + Offset(0f, h * 0.06f), w * 0.12f, ItkC.Person, ss * g)
                    for (j in 0 until 4) drawPath(starPath(c + Offset(w * (-0.05f + 0.035f * j), -h * (0.1f + 0.04f * (j % 2))), w * 0.01f), ItkC.Gold, alpha = ss * g)
                }
            }
            tagAt(a.tag("silencespeechsight", 0, k), c + Offset(0f, h * 0.2f), ss * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    if (nb > 0f) {
        val p = a.b("notebook")
        sit12(seat, h * 0.24f, ItkC.Person, nb, -1f)
        val book = Offset(w * 0.42f, floor + h * 0.17f)
        openBook21(book, w * 0.22f, Color(0xFFF2E8CE), Color(0xFFB8A888), nb)
        val wrt = seg(p, 0.2f, 0.9f)
        quill72(book + Offset(w * (0.06f - 0.12f * fract(wrt * 3f)), -h * 0.01f), h * 0.1f, Color(0xFFE8E0D0), nb)
        for (k in 0 until 3) {
            val u = fract(t * 0.3f + k / 3f)
            drawCircle(ItkC.Gold, w * 0.008f, seat + Offset(-w * 0.02f, -h * (0.26f + 0.12f * u)), alpha = nb * (1f - u))
        }
        tagAt(a.tag("notebook"), Offset(w * 0.5f, h * 0.24f), nb * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
