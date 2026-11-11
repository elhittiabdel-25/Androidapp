package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.sin

/*
 * Wave 69 (2026-11-03): v1.3 fiqh, the first six cards of «الحج والعمرة» (Ibn Bāz). The fifth pillar; before the journey; at the
 * miqat; the five miqats on a map; the three ways of the rites; the child's hajj. Pilgrims are faceless figures; no one named is drawn.
 */

private object HajjC69 {
    val Sky = Color(0xFF0E1630)
    val SkyLow = Color(0xFF3A3A6A)
    val Ground = Color(0xFF1E1A24)
    val Pillar = Color(0xFFD8CCB4)
    val Lit = Color(0xFFF2C860)
    val Road = Color(0xFF4A4038)
    val Garden = Color(0xFF2E6A4A)
    val Leaf = Color(0xFF4E9A6A)
    val Speck = Color(0xFF4A3A4A)
    val DayTop = Color(0xFF8EC2E6)
    val DayLow = Color(0xFFF2E2C0)
    val Person = Color(0xFFE8DCC4)
    val Map = Color(0xFFE9DAB8)
    val MapInk = Color(0xFF7A5E3A)
    val Sea = Color(0xFF6FA8C8)
}

// ═════════════ 1. hajj-wujub: the fifth pillar ═════════════

/**
 * Beats: fifthpillar (five pillars rise under one roof and the fifth turns gold with the House at its foot: «أَحَدَ أَرْكَانِ
 * الْإِسْلَامِ»), hastenhajj (a pilgrim hurries down the road to the House while a clock turns: «عَلَى الْفَوْرِ»), oncelife (a life's
 * line with one gold mark, «مَرَّةً وَاحِدَةً», then small green lights after it, «تَطَوُّعًا»), umrahspan (two Houses joined by an arch,
 * the dark specks between them fading, «كَفَّارَةٌ لِمَا بَيْنَهُمَا», then a garden: «إِلَّا الْجَنَّةُ»).
 */
internal fun DrawScope.hajjpillarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "fifthpillar" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "hastenhajj" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "oncelife" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "umrahspan" to Shot(Offset(w * 0.5f, h * 0.52f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { hajjPillarDraw(a) }
}

private fun DrawScope.hajjPillarDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.8f
    nightSky(t, HajjC69.Sky, HajjC69.SkyLow, ground, starAlpha = 0.8f)
    drawRect(HajjC69.Ground, Offset(0f, ground), Size(w, h - ground))
    val p1 = fqIn(a, "fifthpillar", "hastenhajj")
    if (p1 > 0f) {
        val pb = a.b("fifthpillar")
        for (k in 0 until 5) {
            val x = w * (0.82f - 0.16f * k)
            val g = easeOut(seg(pb, 0.05f * k, 0.2f + 0.05f * k))
            val lit = if (k == 4) seg(pb, 0.4f, 0.6f) else 0f
            if (k == 4) softGlow(Offset(x, ground - h * 0.25f), w * 0.12f, HajjC69.Lit, p1 * lit * 0.6f)
            fqPillar(Offset(x, ground), w * 0.05f, h * 0.42f * g, lerpColor(HajjC69.Pillar, HajjC69.Lit, lit), p1 * (0.5f + 0.5f * g))
        }
        drawRect(HajjC69.Pillar, Offset(w * 0.11f, ground - h * 0.48f), Size(w * 0.78f, h * 0.035f), alpha = p1 * seg(pb, 0.3f, 0.4f))
        kaaba29(Offset(w * 0.18f, ground + h * 0.06f), w * 0.09f, p1 * seg(pb, 0.45f, 0.6f))
        tagAt(a.tag("fifthpillar"), Offset(w * 0.5f, h * 0.15f), p1 * seg(pb, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p2 = fqIn(a, "hastenhajj", "oncelife")
    if (p2 > 0f) {
        val pb = a.b("hastenhajj")
        fqRoad(Offset(w * 0.98f, ground + h * 0.12f), Offset(w * 0.2f, ground + h * 0.03f), h * 0.05f, HajjC69.Road, p2)
        softGlow(Offset(w * 0.14f, ground - h * 0.04f), w * 0.1f, HajjC69.Lit, p2 * 0.4f)
        kaaba29(Offset(w * 0.14f, ground + h * 0.04f), w * 0.1f, p2)
        val go = easeInOut(seg(pb, 0.1f, 0.95f))
        fqPilgrim(Offset(mix(w * 0.88f, w * 0.3f, go), ground + mix(h * 0.11f, h * 0.04f, go)), h * 0.22f, p2, walk = t * 14f)
        clock22(Offset(w * 0.72f, h * 0.3f), w * 0.06f, HajjC69.Pillar, t * 3f, p2)
        tagAt(a.tag("hastenhajj"), Offset(w * 0.45f, h * 0.2f), p2 * seg(pb, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p3 = fqIn(a, "oncelife", "umrahspan")
    if (p3 > 0f) {
        val pb = a.b("oncelife")
        val y = h * 0.58f
        val x0 = w * 0.92f
        val x1 = w * 0.08f
        val run = easeOut(seg(pb, 0f, 0.35f))
        drawCircle(HajjC69.Pillar, w * 0.012f, Offset(x0, y), alpha = p3)
        drawLine(HajjC69.Pillar, Offset(x0, y), Offset(mix(x0, x1, run), y), 3f * dp, StrokeCap.Round, alpha = p3 * 0.7f)
        val one = Offset(w * 0.58f, y)
        val m = easeOut(seg(pb, 0.3f, 0.45f))
        softGlow(one + Offset(0f, -h * 0.05f), w * 0.09f, HajjC69.Lit, p3 * m * 0.6f)
        kaaba29(one, w * 0.08f, p3 * m)
        tagAt(a.tag("oncelife", 0, 0), one + Offset(0f, -h * 0.22f), p3 * m, back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 3) {
            val g = seg(pb, 0.6f + 0.08f * k, 0.7f + 0.08f * k)
            val c = Offset(w * (0.42f - 0.1f * k), y)
            softGlow(c, w * 0.03f, ArtInk.Jade, p3 * g)
            drawCircle(ArtInk.Jade, w * 0.009f, c, alpha = p3 * g)
        }
        tagAt(a.tag("oncelife", 0, 1), Offset(w * 0.32f, y + h * 0.13f), p3 * seg(pb, 0.65f, 0.8f), back = Fq.Back, ink = ArtInk.Jade)
    }
    val p4 = fqIn(a, "umrahspan")
    if (p4 > 0f) {
        val pb = a.b("umrahspan")
        val y = h * 0.66f
        val r = Offset(w * 0.86f, y)
        val l = Offset(w * 0.5f, y)
        val s = w * 0.07f
        kaaba29(r, s, p4)
        kaaba29(l, s, p4 * seg(pb, 0.1f, 0.2f))
        val base = y - s * 0.95f
        drawArc(HajjC69.Lit, 180f, 180f, false, Offset(l.x, base - h * 0.18f), Size(r.x - l.x, h * 0.36f), alpha = p4 * seg(pb, 0.1f, 0.25f), style = Stroke(2f * dp))
        val fade = seg(pb, 0.3f, 0.55f)
        for (k in 0 until 9) {
            val x = mix(l.x + s, r.x - s, (k + 0.5f) / 9f)
            val yy = base - h * (0.03f + 0.1f * fract(k * 0.618f))
            drawCircle(HajjC69.Speck, w * 0.012f, Offset(x, yy), alpha = p4 * seg(pb, 0.12f, 0.22f) * (1f - fade))
        }
        tagAt(a.tag("umrahspan", 0, 0), Offset((l.x + r.x) * 0.5f, base - h * 0.28f), p4 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
        val gd = easeOut(seg(pb, 0.6f, 0.8f))
        fqMound(Offset(w * 0.18f, ground), w * 0.34f, h * 0.12f * gd, HajjC69.Garden, p4 * gd)
        palm12(Offset(w * 0.12f, ground - h * 0.06f), h * 0.3f * gd, HajjC69.Road, HajjC69.Leaf, p4 * gd, t)
        palm12(Offset(w * 0.25f, ground - h * 0.08f), h * 0.24f * gd, HajjC69.Road, HajjC69.Leaf, p4 * gd, t + 1f)
        softGlow(Offset(w * 0.18f, ground - h * 0.2f), w * 0.14f, ArtInk.Jade, p4 * gd * 0.4f)
        tagAt(a.tag("umrahspan", 0, 1), Offset(w * 0.18f, h * 0.3f), p4 * seg(pb, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Jade)
    }
}

// ═════════════ 2. hajj-tawba: before the journey ═════════════

/**
 * Beats: debtledger (a sheet where two columns are written, what is owed to him and what he owes: «مَا لَهُ», «وَمَا عَلَيْهِ»),
 * tawbanasuh (a dark ring round a man breaks and falls away: «التَّوْبَةَ النَّصُوحَ»), rightsback (he carries a parcel back to the one
 * he wronged: «يَرُدَّ الْمَظَالِمَ»), cleanpurse (a purse weighed and found clean: «نَفَقَةً طَيِّبَةً», «مَالٍ حَلَالٍ»), goodcompany (a
 * small caravan sets out together at dawn: «الْأَخْيَارَ»).
 */
internal fun DrawScope.beforehajjScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "debtledger" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "tawbanasuh" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "rightsback" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "cleanpurse" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "goodcompany" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { beforeHajjDraw(a) }
}

private fun DrawScope.beforeHajjDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.82f
    val dawn = seg(a.b("goodcompany"), 0f, 0.6f)
    nightSky(t, HajjC69.Sky, lerpColor(HajjC69.SkyLow, Color(0xFFE0A880), dawn * 0.6f), ground, starAlpha = 0.7f * (1f - dawn * 0.6f))
    drawRect(HajjC69.Ground, Offset(0f, ground), Size(w, h - ground))
    val p1 = fqIn(a, "debtledger", "tawbanasuh")
    if (p1 > 0f) {
        val pb = a.b("debtledger")
        val c = Offset(w * 0.5f, h * 0.52f)
        val sw = w * 0.5f
        val sh = h * 0.5f
        drawRect(Color.Black, Offset(c.x - sw * 0.5f + 3f * dp, c.y - sh * 0.5f + 4f * dp), Size(sw, sh), alpha = p1 * 0.3f)
        drawRect(Color(0xFFF6EBCF), Offset(c.x - sw * 0.5f, c.y - sh * 0.5f), Size(sw, sh), alpha = p1)
        drawLine(HajjC69.MapInk, Offset(c.x, c.y - sh * 0.4f), Offset(c.x, c.y + sh * 0.42f), 1.5f * dp, alpha = p1 * 0.6f)
        for (k in 0 until 4) {
            val y = c.y - sh * 0.15f + k * sh * 0.15f
            val gr = easeOut(seg(pb, 0.15f + 0.1f * k, 0.3f + 0.1f * k))
            val gl = easeOut(seg(pb, 0.45f + 0.1f * k, 0.6f + 0.1f * k))
            drawLine(Color(0xFF2E8A5A), Offset(c.x + sw * 0.43f, y), Offset(c.x + sw * 0.43f - sw * 0.36f * gr, y), 2.5f * dp, StrokeCap.Round, alpha = p1 * gr)
            drawLine(Color(0xFFB0503A), Offset(c.x - sw * 0.07f, y), Offset(c.x - sw * 0.07f - sw * 0.36f * gl, y), 2.5f * dp, StrokeCap.Round, alpha = p1 * gl)
        }
        tagAt(a.tag("debtledger", 0, 0), Offset(c.x + sw * 0.25f, c.y - sh * 0.32f), p1 * seg(pb, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Jade)
        tagAt(a.tag("debtledger", 0, 1), Offset(c.x - sw * 0.25f, c.y - sh * 0.32f), p1 * seg(pb, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Ember)
    }
    val p2 = fqIn(a, "tawbanasuh", "rightsback")
    if (p2 > 0f) {
        val pb = a.b("tawbanasuh")
        val foot = Offset(w * 0.5f, ground)
        val tall = h * 0.36f
        val brk = easeInOut(seg(pb, 0.35f, 0.75f))
        softGlow(foot + Offset(0f, -tall * 0.6f), w * 0.15f, HajjC69.Lit, p2 * brk * 0.5f)
        figure(foot, tall, HajjC69.Person, p2)
        val c = foot + Offset(0f, -tall * 0.55f)
        for (sg in listOf(-1f, 1f)) {
            val off = Offset(sg * w * 0.12f * brk, h * 0.3f * brk * brk)
            drawArc(Color(0xFF3A3040), if (sg < 0f) 90f else -90f, 180f, false, Offset(c.x - w * 0.09f, c.y - h * 0.12f) + off, Size(w * 0.18f, h * 0.24f), alpha = p2 * (1f - brk), style = Stroke(7f * dp, cap = StrokeCap.Round))
        }
        tagAt(a.tag("tawbanasuh"), Offset(w * 0.5f, h * 0.14f), p2 * seg(pb, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p3 = fqIn(a, "rightsback", "cleanpurse")
    if (p3 > 0f) {
        val pb = a.b("rightsback")
        val tall = h * 0.32f
        val me = Offset(w * 0.68f, ground)
        val him = Offset(w * 0.3f, ground)
        val go = easeInOut(seg(pb, 0.15f, 0.65f))
        figure(me + Offset(-w * 0.12f * go, 0f), tall, HajjC69.Person, p3, walk = if (go in 0.01f..0.99f) t * 10f else 0f, arms = 0.6f)
        figure(him, tall, Color(0xFFB8C0D0), p3, arms = 0f)
        val from = me + Offset(-w * 0.12f * go - tall * 0.25f, -tall * 0.62f)
        val to = him + Offset(tall * 0.2f, -tall * 0.5f)
        val give = seg(pb, 0.65f, 0.85f)
        val box = Offset(mix(from.x, to.x, give), mix(from.y, to.y, give))
        drawRect(Fq.Brass, box - Offset(w * 0.025f, w * 0.02f), Size(w * 0.05f, w * 0.04f), alpha = p3)
        drawLine(Fq.Gold, box - Offset(0f, w * 0.02f), box + Offset(0f, w * 0.02f), 1.5f * dp, alpha = p3)
        softGlow(him + Offset(0f, -tall * 0.6f), w * 0.08f, ArtInk.Jade, p3 * seg(pb, 0.85f, 1f) * 0.5f)
        tagAt(a.tag("rightsback"), Offset(w * 0.5f, h * 0.15f), p3 * seg(pb, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p4 = fqIn(a, "cleanpurse", "goodcompany")
    if (p4 > 0f) {
        val pb = a.b("cleanpurse")
        val tilt = (1f - easeOut(seg(pb, 0.2f, 0.6f))) * 0.4f
        balance12(Offset(w * 0.5f, ground), h * 0.5f, tilt, Fq.Brass, p4)
        val shine = seg(pb, 0.55f, 0.75f)
        for (k in 0 until 3) coin12(Offset(w * (0.5f + 0.16f * (k - 1)), h * 0.24f + sin(t * 2f + k) * h * 0.01f), w * 0.03f, p4 * shine)
        softGlow(Offset(w * 0.5f, h * 0.25f), w * 0.18f, HajjC69.Lit, p4 * shine * 0.4f)
        tagAt(a.tag("cleanpurse", 0, 0), Offset(w * 0.28f, h * 0.14f), p4 * seg(pb, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("cleanpurse", 0, 1), Offset(w * 0.72f, h * 0.14f), p4 * seg(pb, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Jade)
    }
    val p5 = fqIn(a, "goodcompany")
    if (p5 > 0f) {
        val pb = a.b("goodcompany")
        val go = seg(pb, 0.1f, 1f)
        val x0 = mix(w * 0.95f, w * 0.55f, go)
        camel26(Offset(x0 - w * 0.2f, ground), h * 0.32f, Color(0xFF2A2230), p5, t, walk = t * 8f)
        for (k in 0 until 3) figure(Offset(x0 + w * 0.08f * k, ground), h * 0.24f, Color(0xFF2A2230), p5, walk = t * 9f + k)
        tagAt(a.tag("goodcompany"), Offset(w * 0.5f, h * 0.2f), p5 * seg(pb, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 3. hajj-ihram: at the miqat ═════════════

/**
 * Beats: washscent (a ewer pours and a phial gives off scent: «يَغْتَسِلَ», «وَيَتَطَيَّبَ»), twowhites (two white cloths unroll, the
 * wrap and the upper cloth: «إِزَارًا», «وَرِدَاءً»), labbayk (a pilgrim in white, his voice going out: «لَبَّيْكَ عُمْرَةً», «لَبَّيْكَ
 * حَجًّا»), keephair (scissors crossed out: «لَا لِلرِّجَالِ وَلَا لِلنِّسَاءِ»), mounted (the pilgrim settled on his camel, then the
 * voice: «بَعْدَ اسْتِوَائِهِ عَلَى مَرْكُوبِهِ»).
 */
internal fun DrawScope.miqatwhiteScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "washscent" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "twowhites" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "labbayk" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "keephair" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "mountride" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { miqatWhiteDraw(a) }
}

private fun DrawScope.miqatWhiteDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val ground = h * 0.78f
    daySky12(HajjC69.DayTop, HajjC69.DayLow, ground, Fq.Sand)
    sun12(Offset(w * 0.85f, h * 0.16f), w * 0.05f, 0.9f)
    val p1 = fqIn(a, "washscent", "twowhites")
    if (p1 > 0f) {
        val pb = a.b("washscent")
        fqEwer(Offset(w * 0.66f, h * 0.5f), h * 0.3f, Fq.Brass, p1, pour = easeInOut(seg(pb, 0.1f, 0.35f)), t = t)
        drawOval(Fq.WaterDeep, Offset(w * 0.42f, ground - h * 0.04f), Size(w * 0.22f, h * 0.07f), alpha = p1 * 0.7f)
        tagAt(a.tag("washscent", 0, 0), Offset(w * 0.62f, h * 0.15f), p1 * seg(pb, 0.15f, 0.3f), back = Fq.Back, ink = Fq.Water)
        val sc = seg(pb, 0.5f, 0.65f)
        fqPhial(Offset(w * 0.22f, ground), h * 0.22f, Color(0xFFC8A0D0), p1 * sc, t)
        tagAt(a.tag("washscent", 0, 1), Offset(w * 0.22f, h * 0.22f), p1 * seg(pb, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p2 = fqIn(a, "twowhites", "labbayk")
    if (p2 > 0f) {
        val pb = a.b("twowhites")
        fqCloth(Offset(w * 0.66f, h * 0.5f), w * 0.26f, h * 0.36f, Fq.White, p2, t, open = seg(pb, 0.05f, 0.35f))
        fqCloth(Offset(w * 0.34f, h * 0.48f), w * 0.3f, h * 0.4f, Fq.White, p2, t + 1f, open = seg(pb, 0.4f, 0.7f))
        drawLine(HajjC69.Road, Offset(w * 0.18f, h * 0.28f), Offset(w * 0.82f, h * 0.28f), 3f * density, StrokeCap.Round, alpha = p2)
        tagAt(a.tag("twowhites", 0, 0), Offset(w * 0.66f, h * 0.78f), p2 * seg(pb, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("twowhites", 0, 1), Offset(w * 0.34f, h * 0.8f), p2 * seg(pb, 0.5f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p3 = fqIn(a, "labbayk", "keephair")
    if (p3 > 0f) {
        val pb = a.b("labbayk")
        val foot = Offset(w * 0.62f, ground + h * 0.04f)
        val tall = h * 0.42f
        softGlow(foot + Offset(0f, -tall * 0.6f), w * 0.14f, HajjC69.Lit, p3 * 0.3f)
        fqPilgrim(foot, tall, p3)
        voice12(foot + Offset(-tall * 0.1f, -tall * 0.85f), w * 0.1f, t, ArtInk.Gold, p3 * seg(pb, 0.3f, 0.45f), dir = -1f)
        tagAt(a.tag("labbayk", 0, 0), Offset(w * 0.28f, h * 0.24f), p3 * seg(pb, 0.4f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("labbayk", 0, 1), Offset(w * 0.28f, h * 0.44f), p3 * seg(pb, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p4 = fqIn(a, "keephair", "mountride")
    if (p4 > 0f) {
        val pb = a.b("keephair")
        val c = Offset(w * 0.5f, h * 0.45f)
        fqScissors(c, w * 0.12f, Color(0xFF6A6A78), p4, open = 0.5f + 0.5f * sin(t * 5f) * (1f - seg(pb, 0.3f, 0.4f)))
        cross14(c, w * 0.13f, seg(pb, 0.3f, 0.55f))
        tagAt(a.tag("keephair"), Offset(w * 0.5f, h * 0.8f), p4 * seg(pb, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p5 = fqIn(a, "mountride")
    if (p5 > 0f) {
        val pb = a.b("mountride")
        val s = h * 0.5f
        val foot = Offset(w * 0.55f, ground + h * 0.08f)
        camel26(foot, s, Color(0xFF8A6A48), p5, t)
        val up = easeOut(seg(pb, 0.05f, 0.35f))
        sit12(Offset(foot.x + s * 0.03f, mix(foot.y, foot.y - s * 0.9f, up)), h * 0.26f, Fq.White, p5, facing = -1f)
        voice12(Offset(foot.x - s * 0.1f, foot.y - s * 1.25f), w * 0.09f, t, ArtInk.Gold, p5 * seg(pb, 0.45f, 0.6f), dir = -1f)
        tagAt(a.tag("mountride"), Offset(w * 0.5f, h * 0.12f), p5 * seg(pb, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. hajj-mawaqit: the five miqats on a map ═════════════

/**
 * Beats: threemiqat (a map round Makkah: three stations ring it, «ذُو الْحُلَيْفَةِ», «الْجُحْفَةُ», «قَرْنُ الْمَنَازِلِ»), twomiqat
 * (two more, «يَلَمْلَمُ», «ذَاتُ عِرْقٍ», and the ring closes), passby (a traveller from far away crosses the ring and turns white),
 * fromhome (houses inside the ring light where they stand: «حَتَّى أَهْلُ مَكَّةَ يُهِلُّونَ مِنْ مَكَّةَ»), todaynames (the names they
 * carry today under three of them: «أَبْيَارُ عَلِيٍّ», «رَابِغٍ», «السَّيْلُ»), tohill (from inside the sacred bounds a dot steps out to
 * the land beyond and comes back white: «الْحَرَمِ», «الْحِلِّ»).
 */
internal fun DrawScope.fivemiqatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val c = Offset(w * 0.5f, h * 0.56f)
    val shots = shotsFor(a, mapOf(
        "threemiqat" to Shot(c, 1f),
        "twomiqat" to Shot(c, 1f),
        "passmiqat" to Shot(c + Offset(-w * 0.1f, -h * 0.1f), 1.15f),
        "fromhome" to Shot(c, 1.3f),
        "todaynames" to Shot(c + Offset(0f, -h * 0.1f), 1f),
        "tohill" to Shot(c, 1.5f),
    ))
    followBeats(a, shots, Shot(c, 1f), dim = 0.08f) { fiveMiqatDraw(a) }
}

private fun DrawScope.fiveMiqatDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val c = Offset(w * 0.5f, h * 0.56f)
    drawRect(HajjC69.Map, Offset.Zero, size)
    // the Red Sea along the west
    drawPath(Path().apply {
        moveTo(0f, 0f); lineTo(w * 0.12f, 0f); cubicTo(w * 0.2f, h * 0.3f, w * 0.26f, h * 0.6f, w * 0.34f, h)
        lineTo(0f, h); close()
    }, HajjC69.Sea, alpha = 0.55f)
    for (k in 0 until 6) drawCircle(HajjC69.MapInk, w * 0.004f, Offset(w * (0.3f + 0.13f * fract(k * 0.37f + 0.2f) * 4f), h * (0.15f + 0.13f * k)), alpha = 0.25f)
    kaaba29(c + Offset(0f, w * 0.015f), w * 0.035f, 1f)
    val miq = listOf(Offset(-0.04f, -0.44f), Offset(-0.24f, -0.24f), Offset(0.22f, -0.04f), Offset(0.08f, 0.3f), Offset(0.2f, -0.22f))
        .map { c + Offset(w * it.x, h * it.y) }
    val p1 = fqIn(a, "threemiqat")
    val tb = a.b("threemiqat")
    val mb = a.b("twomiqat")
    val ringA = p1 * seg(mb, 0.4f, 0.8f)
    if (ringA > 0f) {
        val ring = Path().apply {
            moveTo(miq[0].x, miq[0].y)
            for (i in listOf(4, 2, 3, 1, 0)) lineTo(miq[i].x, miq[i].y)
        }
        drawPath(ring, HajjC69.MapInk, alpha = ringA * 0.6f, style = Stroke(2f * dp, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(8f * dp, 6f * dp))))
    }
    for (i in 0 until 5) {
        val g = if (i < 3) easeOut(seg(tb, 0.1f + 0.2f * i, 0.25f + 0.2f * i)) else easeOut(seg(mb, 0.05f + 0.25f * (i - 3), 0.2f + 0.25f * (i - 3)))
        if (g <= 0f) continue
        drawCircle(Color(0xFFB0503A), w * 0.018f * g, miq[i], alpha = p1)
        drawCircle(Fq.White, w * 0.007f * g, miq[i], alpha = p1)
        val below = if (i == 3) Offset(w * 0.1f, 0f) else Offset(0f, -h * 0.07f)
        if (i < 3) tagAt(a.tag("threemiqat", 0, i), miq[i] + below, p1 * g, back = Fq.Back, ink = ArtInk.Ivory)
        else tagAt(a.tag("twomiqat", 0, i - 3), miq[i] + below, p1 * g, back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p3 = fqIn(a, "passmiqat", "fromhome")
    if (p3 > 0f) {
        val pb = a.b("passmiqat")
        val start = c + Offset(-w * 0.45f, -h * 0.5f)
        val end = c + Offset(-w * 0.04f, -h * 0.04f)
        val go = easeInOut(seg(pb, 0.05f, 0.9f))
        val pos = Offset(mix(start.x, end.x, go), mix(start.y, end.y, go))
        val white = seg(go, 0.45f, 0.55f)
        drawLine(HajjC69.MapInk, start, pos, 1.5f * dp, alpha = p3 * 0.5f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        if (white > 0f) softGlow(pos, w * 0.03f, Fq.White, p3 * white)
        drawCircle(lerpColor(Color(0xFF4A3A2A), Fq.White, white), w * 0.014f, pos, alpha = p3)
    }
    val p4 = fqIn(a, "fromhome", "todaynames")
    if (p4 > 0f) {
        val pb = a.b("fromhome")
        for (k in 0 until 5) {
            val ang = k * 1.3f + 0.4f
            val pos = c + Offset(w * 0.1f * sin(ang), h * 0.09f * kotlin.math.cos(ang))
            val g = seg(pb, 0.1f + 0.1f * k, 0.25f + 0.1f * k)
            house12(pos + Offset(0f, h * 0.02f), w * 0.04f, Color(0xFF8A6A48), Color(0xFF5A4030), p4, lit = g)
            softGlow(pos, w * 0.02f, Fq.White, p4 * g * 0.7f)
        }
        tagAt(a.tag("fromhome"), c + Offset(0f, h * 0.22f), p4 * seg(pb, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p5 = fqIn(a, "todaynames", "tohill")
    if (p5 > 0f) {
        val pb = a.b("todaynames")
        for (k in 0 until 3) {
            val g = seg(pb, 0.05f + 0.3f * k, 0.2f + 0.3f * k)
            val i = listOf(0, 1, 2)[k]
            val off = if (k == 1) Offset(w * 0.02f, h * 0.08f) else Offset(0f, h * 0.07f)
            tagAt(a.tag("todaynames", 0, k), miq[i] + off, p5 * g, back = Color(0xE63A2A1A), ink = ArtInk.Gold)
        }
    }
    val p6 = fqIn(a, "tohill")
    if (p6 > 0f) {
        val pb = a.b("tohill")
        val r = w * 0.09f
        drawCircle(Fq.Green, r, c, alpha = p6 * 0.18f)
        drawCircle(Fq.Green, r, c, alpha = p6 * 0.6f, style = Stroke(2f * dp))
        val out = easeInOut(seg(pb, 0.15f, 0.45f)) * (1f - easeInOut(seg(pb, 0.6f, 0.9f)))
        val pos = c + Offset(-r * 0.4f - r * 1.2f * out, r * 0.3f)
        val white = seg(pb, 0.45f, 0.55f)
        if (white > 0f) softGlow(pos, w * 0.025f, Fq.White, p6 * white)
        drawCircle(lerpColor(Color(0xFF4A3A2A), Fq.White, white), w * 0.012f, pos, alpha = p6)
        tagAt(a.tag("tohill", 0, 0), c + Offset(r * 0.2f, -r * 0.55f), p6 * seg(pb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Jade)
        tagAt(a.tag("tohill", 0, 1), c + Offset(-r * 1.9f, -r * 0.2f), p6 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Ivory)
    }
}

// ═════════════ 5. hajj-ashhur: the three ways ═════════════

/**
 * Beats: umrahsteps (out of the hajj months: round the House, between the two hills, the scissors, then the lock opens: «فَطَافَ»,
 * «وَسَعَى», «وَحَلَقَ أَوْ قَصَّرَ»), threeroads (in the months, three roads part from the miqat: «الْحَجَّ وَحْدَهُ», «وَالْعُمْرَةَ
 * وَحْدَهَا», «وَالْجَمْعَ بَيْنَهُمَا»), tamattu (one road, the ʿumra and then the hajj, glows: «عِنْدَ الْمُؤَلِّفِ», «مُتَمَتِّعًا»),
 * hajjmonths (three months on a calendar, the last only a third filled), ifheld (a barrier across the road, and the lock opens where
 * he stops: «فَإِنْ حَبَسَنِي حَابِسٌ فَمَحِلِّي حَيْثُ حَبَسْتَنِي»).
 */
internal fun DrawScope.threenusukScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "umrahsteps" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "threeroads" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "tamattu" to Shot(Offset(w * 0.45f, h * 0.45f), 1.1f),
        "hajjmonths" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "ifheld" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { threeNusukDraw(a) }
}

private fun DrawScope.threeNusukDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.8f
    daySky12(HajjC69.DayTop, HajjC69.DayLow, ground, Fq.Sand)
    val p1 = fqIn(a, "umrahsteps", "threeroads")
    if (p1 > 0f) {
        val pb = a.b("umrahsteps")
        val y = h * 0.55f
        val g1 = seg(pb, 0.05f, 0.2f)
        kaaba29(Offset(w * 0.8f, y), w * 0.06f, p1 * g1)
        fqCircling(Offset(w * 0.8f, y - w * 0.02f), w * 0.1f, h * 0.06f, t, Fq.White, p1 * g1, n = 10)
        tagAt(a.tag("umrahsteps", 0, 0), Offset(w * 0.8f, y + h * 0.15f), p1 * g1, back = Fq.Back, ink = ArtInk.Ivory)
        val g2 = seg(pb, 0.3f, 0.45f)
        fqMound(Offset(w * 0.6f, y), w * 0.08f, h * 0.05f, Fq.Stone, p1 * g2)
        fqMound(Offset(w * 0.4f, y), w * 0.08f, h * 0.05f, Fq.Stone, p1 * g2)
        val sx = mix(w * 0.6f, w * 0.4f, 0.5f + 0.5f * sin(t * 2.5f))
        drawCircle(Fq.White, w * 0.012f, Offset(sx, y - h * 0.02f), alpha = p1 * g2)
        tagAt(a.tag("umrahsteps", 0, 1), Offset(w * 0.5f, y + h * 0.15f), p1 * g2, back = Fq.Back, ink = ArtInk.Ivory)
        val g3 = seg(pb, 0.55f, 0.7f)
        fqScissors(Offset(w * 0.2f, y - h * 0.05f), w * 0.06f, Color(0xFF6A6A78), p1 * g3, open = 0.5f + 0.5f * sin(t * 6f))
        tagAt(a.tag("umrahsteps", 0, 2), Offset(w * 0.2f, y + h * 0.15f), p1 * g3, back = Fq.Back, ink = ArtInk.Ivory)
        padlock21(Offset(w * 0.5f, h * 0.22f), w * 0.06f, Fq.Brass, seg(pb, 0.8f, 0.95f), p1 * seg(pb, 0.72f, 0.8f))
    }
    val p2 = fqIn(a, "threeroads", "hajjmonths")
    val tm = a.b("tamattu")
    if (p2 > 0f) {
        val pb = a.b("threeroads")
        val from = Offset(w * 0.88f, ground)
        val ends = listOf(Offset(w * 0.12f, h * 0.3f), Offset(w * 0.12f, h * 0.52f), Offset(w * 0.12f, h * 0.74f))
        drawCircle(Color(0xFFB0503A), w * 0.02f, from, alpha = p2)
        for (k in 0 until 3) {
            val g = easeOut(seg(pb, 0.1f + 0.25f * k, 0.3f + 0.25f * k))
            val hi = if (k == 1) seg(tm, 0.1f, 0.3f) else 0f
            val end = Offset(mix(from.x, ends[k].x, g), mix(from.y, ends[k].y, g))
            drawLine(lerpColor(HajjC69.Road, HajjC69.Lit, hi), from, end, (6f + 4f * hi) * dp, StrokeCap.Round, alpha = p2 * (1f - 0.5f * seg(tm, 0.1f, 0.3f) * (if (k == 1) 0f else 1f)))
            if (k == 1 && hi > 0f) {
                kaaba29(ends[k] + Offset(w * 0.03f, 0f), w * 0.05f, p2 * hi)
                softGlow(ends[k], w * 0.08f, HajjC69.Lit, p2 * hi * 0.5f)
            }
            tagAt(a.tag("threeroads", 0, k), Offset(w * 0.45f, mix(from.y, ends[k].y, 0.6f) - h * 0.06f), p2 * g * (1f - 0.6f * seg(tm, 0.1f, 0.3f)), back = Fq.Back, ink = ArtInk.Ivory)
        }
        if (tm > 0f) {
            tagAt(a.tag("tamattu", 0, 0), Offset(w * 0.5f, h * 0.12f), p2 * seg(tm, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Ivory)
            tagAt(a.tag("tamattu", 0, 1), Offset(w * 0.3f, h * 0.9f), p2 * seg(tm, 0.35f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    val p4 = fqIn(a, "hajjmonths", "ifheld")
    if (p4 > 0f) {
        val pb = a.b("hajjmonths")
        val cell = w * 0.024f
        for (m in 0 until 3) {
            val g = seg(pb, 0.05f + 0.25f * m, 0.25f + 0.25f * m)
            val tl = Offset(w * (0.68f - 0.3f * m), h * 0.38f)
            val days = if (m == 2) 10 else 30
            fqCalendar(tl, 6, 30, cell, p4 * (0.5f + 0.5f * g), HajjC69.MapInk) { i -> if (i < days * g) HajjC69.Lit else null }
            tagAt(a.tag("hajjmonths", 0, m), tl + Offset(cell * 3.4f, -h * 0.08f), p4 * g, back = Fq.Back, ink = ArtInk.Gold)
        }
    }
    val p5 = fqIn(a, "ifheld")
    if (p5 > 0f) {
        val pb = a.b("ifheld")
        fqRoad(Offset(w * 0.95f, ground + h * 0.06f), Offset(w * 0.05f, ground + h * 0.06f), h * 0.06f, HajjC69.Road, p5)
        val bar = seg(pb, 0.2f, 0.35f)
        drawLine(Color(0xFFB0503A), Offset(w * 0.4f, ground - h * 0.12f * bar), Offset(w * 0.4f, ground + h * 0.12f), 8f * dp, StrokeCap.Round, alpha = p5 * bar)
        val go = easeOut(seg(pb, 0f, 0.3f))
        fqPilgrim(Offset(mix(w * 0.85f, w * 0.5f, go), ground + h * 0.06f), h * 0.26f, p5, walk = if (go < 0.99f) t * 10f else 0f)
        val op = seg(pb, 0.6f, 0.8f)
        padlock21(Offset(w * 0.5f, h * 0.38f), w * 0.07f, Fq.Brass, op, p5 * seg(pb, 0.4f, 0.5f))
        tagAt(a.tag("ifheld"), Offset(w * 0.5f, h * 0.14f), p5 * seg(pb, 0.3f, 0.45f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 6. hajj-sabi: the child's hajj ═════════════

/**
 * Beats: validyoung (two children beside the House with a tick, «يَصِحُّ», and an empty ring that stays empty: «لَا يُجْزِئُهُمَا عَنْ
 * حَجَّةِ الْإِسْلَامِ»), guardianlabbayk (a grown man holds a small child and calls for him: «وَلِيُّهُ», «وَلَبَّى عَنْهُ»), throwsforhim
 * (the man throws pebbles at the pillar while the child watches: «كَالرَّمْيِ»), carriedround (carried in his arms round the House:
 * «مَحْمُولَيْنِ»), guardianreward (a light settles on the guardian: «نَفْلٌ», «فَلَهُ أَجْرٌ»).
 */
internal fun DrawScope.childpilgrimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "validyoung" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "guardianlabbayk" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "throwsforhim" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "carriedround" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "guardianreward" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { childPilgrimDraw(a) }
}

private fun DrawScope.childPilgrimDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val t = a.t
    val dp = density
    val ground = h * 0.8f
    daySky12(HajjC69.DayTop, HajjC69.DayLow, ground, Fq.Sand)
    val p1 = fqIn(a, "validyoung", "guardianlabbayk")
    if (p1 > 0f) {
        val pb = a.b("validyoung")
        kaaba29(Offset(w * 0.75f, ground), w * 0.16f, p1)
        child12(Offset(w * 0.5f, ground), h * 0.2f, Fq.White, p1)
        child12(Offset(w * 0.38f, ground), h * 0.18f, Color(0xFFE8D8E8), p1)
        tick14(Offset(w * 0.44f, h * 0.4f), w * 0.05f, seg(pb, 0.15f, 0.35f))
        tagAt(a.tag("validyoung", 0, 0), Offset(w * 0.44f, h * 0.24f), p1 * seg(pb, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Jade)
        val ring = seg(pb, 0.5f, 0.65f)
        drawCircle(Fq.Brass, w * 0.05f, Offset(w * 0.16f, h * 0.45f), alpha = p1 * ring, style = Stroke(3f * dp, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp))))
        tagAt(a.tag("validyoung", 0, 1), Offset(w * 0.3f, h * 0.9f), p1 * seg(pb, 0.55f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    val p2 = fqIn(a, "guardianlabbayk", "throwsforhim")
    if (p2 > 0f) {
        val pb = a.b("guardianlabbayk")
        val foot = Offset(w * 0.55f, ground)
        val tall = h * 0.42f
        fqPilgrim(foot, tall, p2)
        drawCircle(Fq.White, tall * 0.07f, foot + Offset(-tall * 0.2f, -tall * 0.72f), alpha = p2)
        drawOval(Fq.White, Offset(foot.x - tall * 0.3f, foot.y - tall * 0.66f), Size(tall * 0.2f, tall * 0.26f), alpha = p2)
        voice12(foot + Offset(-tall * 0.05f, -tall * 0.95f), w * 0.09f, t, ArtInk.Gold, p2 * seg(pb, 0.4f, 0.55f), dir = -1f)
        tagAt(a.tag("guardianlabbayk", 0, 0), Offset(w * 0.75f, h * 0.3f), p2 * seg(pb, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("guardianlabbayk", 0, 1), Offset(w * 0.25f, h * 0.22f), p2 * seg(pb, 0.45f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p3 = fqIn(a, "throwsforhim", "carriedround")
    if (p3 > 0f) {
        val pb = a.b("throwsforhim")
        val pil = Offset(w * 0.2f, ground)
        fqPillar(pil, w * 0.06f, h * 0.45f, Fq.Stone, p3)
        val man = Offset(w * 0.62f, ground)
        val tall = h * 0.4f
        fqPilgrim(man, tall, p3, arms = 0.8f)
        child12(man + Offset(w * 0.11f, 0f), h * 0.22f, Fq.White, p3)
        tagAt(a.tag("throwsforhim", 0, 1), man + Offset(-w * 0.02f, -tall * 1.18f), p3 * seg(pb, 0.1f, 0.25f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("throwsforhim", 0, 2), man + Offset(w * 0.2f, -h * 0.3f), p3 * seg(pb, 0.15f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("throwsforhim", 0, 3), pil + Offset(w * 0.02f, -h * 0.52f), p3 * seg(pb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Ivory)
        for (k in 0 until 3) {
            val q = fract(seg(pb, 0.1f, 0.9f) * 3f - k) * (if (seg(pb, 0.1f, 0.9f) * 3f >= k) 1f else 0f)
            fqPebble(man + Offset(-tall * 0.3f, -tall * 0.75f), pil + Offset(0f, -h * 0.25f), q, w * 0.008f, p3)
        }
        tagAt(a.tag("throwsforhim"), Offset(w * 0.42f, h * 0.62f), p3 * seg(pb, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p4 = fqIn(a, "carriedround", "guardianreward")
    if (p4 > 0f) {
        val pb = a.b("carriedround")
        val c = Offset(w * 0.5f, h * 0.58f)
        fqCircling(c + Offset(0f, h * 0.02f), w * 0.3f, h * 0.12f, t, Color(0xFFDCD4C4), p4 * 0.6f, n = 16, speed = 0.08f)
        kaaba29(c + Offset(0f, h * 0.04f), w * 0.12f, p4)
        val ang = t * 0.5f
        val pos = c + Offset(w * 0.3f * kotlin.math.cos(ang), -h * 0.12f * sin(ang) + h * 0.02f)
        fqPilgrim(pos, h * 0.2f, p4, walk = t * 8f)
        drawCircle(Fq.White, h * 0.016f, pos + Offset(-h * 0.04f, -h * 0.15f), alpha = p4)
        tagAt(a.tag("carriedround"), Offset(w * 0.5f, h * 0.15f), p4 * seg(pb, 0.2f, 0.35f), back = Fq.Back, ink = ArtInk.Gold)
    }
    val p5 = fqIn(a, "guardianreward")
    if (p5 > 0f) {
        val pb = a.b("guardianreward")
        val foot = Offset(w * 0.5f, ground)
        val tall = h * 0.4f
        fqPilgrim(foot, tall, p5)
        child12(Offset(w * 0.62f, ground), h * 0.18f, Fq.White, p5)
        val down = easeOut(seg(pb, 0.4f, 0.7f))
        val glowAt = Offset(w * 0.5f, mix(h * 0.05f, foot.y - tall * 0.85f, down))
        softGlow(glowAt, w * 0.06f, HajjC69.Lit, p5 * seg(pb, 0.35f, 0.45f))
        softGlow(foot + Offset(0f, -tall * 0.6f), w * 0.15f, HajjC69.Lit, p5 * seg(pb, 0.65f, 0.85f) * 0.5f)
        tagAt(a.tag("guardianreward", 0, 0), Offset(w * 0.22f, h * 0.3f), p5 * seg(pb, 0.05f, 0.2f), back = Fq.Back, ink = ArtInk.Ivory)
        tagAt(a.tag("guardianreward", 0, 1), Offset(w * 0.78f, h * 0.3f), p5 * seg(pb, 0.6f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
