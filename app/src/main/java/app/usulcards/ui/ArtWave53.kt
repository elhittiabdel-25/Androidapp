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
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * Wave 53 (2026-11-03): «حصن المسلم»، the end of «أذكار اليوم والليلة» and «أذكار الأحوال» (1). Bedtime and the guard
 * over the sleeper; doors shut and jars covered at nightfall; a heart in winter that the Qur'an turns to spring; the
 * prophet of the whale, a small light in the deep; a dark mass that turns back from a camp and the fire made cool.
 * Allah is never drawn; prophets are lights with their names; angels are cool white lights; Shaytan is a dark smoke.
 */

private val Back53 = Color(0xE6181420)

// ═════════════ 1. bedtime (hisn-nawm) ═════════════

private object BedC53 {
    val Night = Color(0xFF121628)
    val Wall = Color(0xFF2A2638)
    val Floor = Color(0xFF1E1A26)
    val Mat = Color(0xFF6A4A3A)
    val Person = Color(0xFFCFC6B4)
    val Gold = Color(0xFFF2C45A)
    val Verse = Color(0xFFEFE0B0)
    val Bead = Color(0xFFD8B46A)
    val Smoke = Color(0xFF2A2430)
    val Dust = Color(0xFF8A7E70)
}

/**
 * Beats: threesurahs (the light ﷺ beside a mat; three small scrolls glow and a soft light passes over the mat three
 * times), watcher (one sleeps; a cool white light keeps watch over him and the smoke keeps away), twoverses (two lines of
 * light settle over the sleeper like a cover), rightside (one lies on his right side under an arch of light), counts
 * (three columns of beads fill: «33», «33», «34»).
 */
internal fun DrawScope.bedtimeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "threesurahs" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
        "watcher" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "twoverses" to Shot(Offset(w * 0.5f, h * 0.65f), 1.2f),
        "rightside" to Shot(Offset(w * 0.5f, h * 0.65f), 1.2f),
        "tasbihcols" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { bedNight(a) }
}

private fun DrawScope.sleeper53(head: Offset, len: Float, color: Color, alpha: Float) {
    drawLine(color, head + Offset(len * 0.12f, 0f), head + Offset(len, len * 0.04f), len * 0.13f, StrokeCap.Round, alpha = alpha)
    drawCircle(color, len * 0.1f, head, alpha = alpha)
}

private fun DrawScope.bedNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ts = a.b("threesurahs")
    val wt = a.b("watcher")
    val tv = a.b("twoverses")
    val rs = a.b("rightside")
    val ct = a.b("tasbihcols")
    val floor = h * 0.8f
    drawRect(BedC53.Wall, Offset.Zero, Size(w, floor))
    drawRect(BedC53.Floor, Offset(0f, floor), Size(w, h - floor))
    drawRect(BedC53.Night, Offset(w * 0.7f, h * 0.15f), Size(w * 0.16f, h * 0.24f))
    crescent12(Offset(w * 0.78f, h * 0.25f), w * 0.02f, Color(0xFFF2EEDC), BedC53.Night, 1f)
    val matY = floor - h * 0.02f
    if (ts > 0f) {
        val f = easeOut(seg(ts, 0f, BEAT_IN)) * (1f - seg(wt, 0f, BEAT_OUT))
        drawRect(BedC53.Mat, Offset(w * 0.3f, matY), Size(w * 0.4f, h * 0.03f), alpha = f)
        val light = Offset(w * 0.22f, h * 0.48f)
        sirajMunir(light, w * 0.013f, f, t, nameAt = light + Offset(0f, -h * 0.12f))
        for (k in 0 until 3) {
            val g = seg(ts, 0.1f + 0.15f * k, 0.25f + 0.15f * k)
            scroll14(Offset(w * (0.4f + 0.1f * k), h * 0.4f), w * 0.06f, h * 0.05f, 1f, f * g)
            softGlow(Offset(w * (0.4f + 0.1f * k), h * 0.4f), w * 0.04f, BedC53.Gold, 0.4f * f * g)
        }
        val pass = fract(seg(ts, 0.55f, 1f) * 3f)
        if (ts > 0.55f) softGlow(Offset(w * (0.32f + 0.36f * pass), matY - h * 0.02f), w * 0.05f, BedC53.Gold, 0.5f * f)
    }
    if (wt > 0f) {
        val f = easeOut(seg(wt, 0f, BEAT_IN)) * (1f - seg(tv, 0f, BEAT_OUT))
        drawRect(BedC53.Mat, Offset(w * 0.3f, matY), Size(w * 0.4f, h * 0.03f), alpha = f)
        sleeper53(Offset(w * 0.34f, matY - h * 0.02f), w * 0.3f, BedC53.Person, f)
        angelLight26(Offset(w * 0.5f, h * 0.36f + h * 0.01f * sin(t)), w * 0.012f, t, f * seg(wt, 0.1f, 0.3f))
        val away = seg(wt, 0.3f, 0.8f)
        smoke12(Offset(w * 0.98f, h * 0.2f), Offset(w * (0.8f + 0.15f * away), h * 0.5f), 0.8f, t, f * (1f - away), BedC53.Smoke)
    }
    if (tv > 0f) {
        val f = easeOut(seg(tv, 0f, BEAT_IN)) * (1f - seg(rs, 0f, BEAT_OUT))
        drawRect(BedC53.Mat, Offset(w * 0.3f, matY), Size(w * 0.4f, h * 0.03f), alpha = f)
        sleeper53(Offset(w * 0.34f, matY - h * 0.02f), w * 0.3f, BedC53.Person, f)
        for (k in 0 until 2) {
            val down = easeInOut(seg(tv, 0.15f + 0.25f * k, 0.5f + 0.25f * k))
            val y = h * (0.25f + 0.05f * k) + (matY - h * (0.07f - 0.025f * k) - h * (0.25f + 0.05f * k)) * down
            drawRoundRect(BedC53.Verse, Offset(w * 0.3f, y), Size(w * 0.4f, h * 0.012f), CornerRadius(h * 0.006f), alpha = f * 0.8f)
            softGlow(Offset(w * 0.5f, y), w * 0.08f, BedC53.Gold, 0.25f * f)
        }
    }
    if (rs > 0f) {
        val f = easeOut(seg(rs, 0f, BEAT_IN)) * (1f - seg(ct, 0f, BEAT_OUT))
        drawRect(BedC53.Mat, Offset(w * 0.3f, matY), Size(w * 0.4f, h * 0.03f), alpha = f)
        val head = Offset(w * 0.66f, matY - h * 0.03f)
        drawLine(BedC53.Person, head + Offset(-w * 0.03f, 0f), head + Offset(-w * 0.3f, h * 0.01f), w * 0.04f, StrokeCap.Round, alpha = f)
        drawCircle(BedC53.Person, w * 0.03f, head, alpha = f)
        drawArc(BedC53.Gold, 180f, 180f, false, Offset(w * 0.28f, matY - h * 0.3f), Size(w * 0.44f, h * 0.6f), alpha = f * seg(rs, 0.3f, 0.6f) * 0.6f, style = Stroke(2f * dp))
    }
    if (ct > 0f) {
        val f = easeOut(seg(ct, 0f, 0.3f))
        for (col in 0 until 3) {
            val n = if (col == 2) 34 else 33
            val fill = seg(ct, 0.1f + 0.25f * col, 0.35f + 0.25f * col)
            for (k in 0 until n) {
                val c = Offset(w * (0.3f + 0.2f * col) + w * 0.018f * (k % 3 - 1), h * (0.72f - 0.024f * (k / 3)))
                drawCircle(if (k < (fill * n).toInt()) BedC53.Bead else Color(0xFF4A4458), w * 0.006f, c, alpha = f)
            }
            tagAt(a.tag("tasbihcols", 0, col), Offset(w * (0.3f + 0.2f * col), h * 0.8f), f * fill, back = Back53, ink = ArtInk.Gold)
        }
    }
}

// ═════════════ 2. nightfall (hisn-layl) ═════════════

private object DuskC53 {
    val Sky = Color(0xFF1C2038)
    val SkyLow = Color(0xFFB0605A)
    val Ground = Color(0xFF2A2430)
    val Wall = Color(0xFF6A5A60)
    val Roof = Color(0xFF3A2E36)
    val Child = Color(0xFFE6DCC8)
    val Smoke = Color(0xFF221C28)
    val Gold = Color(0xFFE8C46A)
    val Skin = Color(0xFF8A6A44)
    val Jar = Color(0xFFB08458)
    val Flame = Color(0xFFF2B04A)
    val Fire = Color(0xFFD8582E)
    val Book = Color(0xFF3E5A4A)
}

/**
 * Beats: callin (at sunset children run into their houses while wisps of smoke spread in the lanes), shutdoors (doors
 * close one by one with a small glow; the smoke tries a shut door and slides off), coverjars (a waterskin is tied, a
 * jar is covered with a stick laid across it, a lamp is put out), lastpage (a bound book closes at its last page), lampfire
 * (a lamp left burning catches a house; then it is put out).
 */
internal fun DrawScope.nightdoorsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "callin" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "shutdoors" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "coverjars" to Shot(Offset(w * 0.5f, h * 0.6f), 1.25f),
        "lastpage" to Shot(Offset(w * 0.5f, h * 0.5f), 1.2f),
        "lampfire" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { duskLanes(a) }
}

private fun DrawScope.duskLanes(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ci = a.b("callin")
    val sd = a.b("shutdoors")
    val cj = a.b("coverjars")
    val lp = a.b("lastpage")
    val lf = a.b("lampfire")
    val ground = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(DuskC53.Sky, DuskC53.SkyLow), 0f, ground), Offset.Zero, Size(w, ground))
    drawRect(DuskC53.Ground, Offset(0f, ground), Size(w, h - ground))
    if (ci > 0f) {
        val f = easeOut(seg(ci, 0f, BEAT_IN)) * (1f - seg(sd, 0f, BEAT_OUT))
        for (k in 0 until 3) house12(Offset(w * (0.18f + 0.32f * k), ground), w * 0.22f, DuskC53.Wall, DuskC53.Roof, f, 0.7f)
        for (k in 0 until 4) {
            val go = easeInOut(seg(ci, 0.1f + 0.1f * k, 0.5f + 0.1f * k))
            val target = w * (0.15f + 0.32f * (k % 3))
            val x = mix(w * (0.3f + 0.15f * k), target, go)
            child12(Offset(x, ground), h * 0.3f, DuskC53.Child, f * (1f - seg(go, 0.9f, 1f)), walk = t * 8f)
        }
        for (k in 0 until 3) smoke12(Offset(w * (0.1f + 0.4f * k), ground - h * 0.02f), Offset(w * (0.3f + 0.4f * k), h * 0.4f), seg(ci, 0.3f, 0.9f), t + k, f * 0.6f, DuskC53.Smoke)
    }
    if (sd > 0f) {
        val f = easeOut(seg(sd, 0f, BEAT_IN)) * (1f - seg(cj, 0f, BEAT_OUT))
        for (k in 0 until 3) {
            val sill = Offset(w * (0.2f + 0.3f * k), ground)
            val shut = seg(sd, 0.1f + 0.15f * k, 0.3f + 0.15f * k)
            door12(sill, w * 0.12f, h * 0.36f, 1f - shut, DuskC53.Wall, Color(0xFF4A3424), f)
            softGlow(sill + Offset(0f, -h * 0.18f), w * 0.04f, DuskC53.Gold, 0.6f * f * bump(shut))
        }
        val tryP = seg(sd, 0.6f, 1f)
        smoke12(Offset(w * 1.0f, h * 0.3f), Offset(w * (0.82f + 0.08f * bump(tryP)), ground - h * 0.2f), 0.9f, t, f * 0.8f, DuskC53.Smoke)
    }
    if (cj > 0f) {
        val f = easeOut(seg(cj, 0f, BEAT_IN)) * (1f - seg(lp, 0f, BEAT_OUT))
        drawRect(Color(0xFF2A2228), Offset(w * 0.06f, h * 0.2f), Size(w * 0.88f, ground - h * 0.2f), alpha = f)
        val skin = Offset(w * 0.24f, ground - h * 0.12f)
        drawOval(DuskC53.Skin, Offset(skin.x - w * 0.07f, skin.y - h * 0.08f), Size(w * 0.14f, h * 0.16f), alpha = f)
        val tie = seg(cj, 0.1f, 0.3f)
        drawLine(Color(0xFFE8D8B0), skin + Offset(-w * 0.02f, -h * 0.09f), skin + Offset(w * 0.02f, -h * 0.09f), 3f * dp * tie, StrokeCap.Round, alpha = f * tie)
        val jar = Offset(w * 0.5f, ground)
        cup12(jar, w * 0.12f, DuskC53.Jar, f, Color(0xFF6AA0C0))
        val stick = seg(cj, 0.35f, 0.55f)
        drawLine(Color(0xFF8A6A44), jar + Offset(-w * 0.08f, -h * 0.08f - h * 0.1f * (1f - stick)), jar + Offset(w * 0.08f, -h * 0.08f - h * 0.1f * (1f - stick)), 3f * dp, StrokeCap.Round, alpha = f * stick)
        val lamp = Offset(w * 0.76f, ground - h * 0.02f)
        drawOval(Color(0xFF8A5A3A), Offset(lamp.x - w * 0.03f, lamp.y - h * 0.015f), Size(w * 0.06f, h * 0.03f), alpha = f)
        val out = seg(cj, 0.65f, 0.8f)
        drawOval(DuskC53.Flame, Offset(lamp.x - w * 0.006f, lamp.y - h * 0.06f), Size(w * 0.012f, h * 0.045f * (1f - out)), alpha = f * (1f - out))
        softGlow(lamp, w * 0.1f, DuskC53.Flame, 0.4f * f * (1f - out))
    }
    if (lp > 0f) {
        val f = easeOut(seg(lp, 0f, BEAT_IN)) * (1f - seg(lf, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.5f)
        val close = easeInOut(seg(lp, 0.3f, 0.7f))
        if (close < 0.95f) openBook21(c, w * 0.4f * (1f - 0.9f * close), Color(0xFFFBF7EC), Color(0xFF9A9080), f, glow = 0.3f)
        else {
            drawRoundRect(DuskC53.Book, Offset(c.x - w * 0.09f, c.y - h * 0.16f), Size(w * 0.18f, h * 0.32f), CornerRadius(4f * dp), alpha = f)
            drawRoundRect(DuskC53.Gold, Offset(c.x - w * 0.07f, c.y - h * 0.12f), Size(w * 0.14f, h * 0.24f), CornerRadius(3f * dp), alpha = f * 0.6f, style = Stroke(1.5f * dp))
        }
        softGlow(c, w * 0.16f, DuskC53.Gold, 0.3f * f * seg(lp, 0.7f, 1f))
    }
    if (lf > 0f) {
        val f = easeOut(seg(lf, 0f, 0.3f))
        val c = Offset(w * 0.5f, ground)
        house12(c, w * 0.3f, DuskC53.Wall, DuskC53.Roof, f, 0.3f)
        val burn = seg(lf, 0.15f, 0.5f) * (1f - seg(lf, 0.65f, 0.9f))
        for (k in 0 until 6) drawOval(DuskC53.Fire, Offset(c.x - w * 0.14f + w * 0.05f * k, ground - h * (0.25f + 0.08f * sin(t * 7f + k))), Size(w * 0.04f, h * 0.18f), alpha = f * burn * 0.85f)
        for (k in 0 until 4) {
            val u = fract(t * 0.4f + k / 4f)
            drawCircle(Color(0xFF3A3238), w * 0.03f * (0.6f + u), c + Offset(w * 0.03f * sin(t + k), -h * (0.35f + 0.2f * u)), alpha = f * burn * 0.5f * (1f - u))
        }
    }
}

// ═════════════ 3. a heart that turns to spring (hisn-hamm) ═════════════

private object SpringC53 {
    val Winter = Color(0xFF6A7488)
    val WinterLow = Color(0xFFB8C0CC)
    val Ground = Color(0xFF8A8478)
    val Spring = Color(0xFF9AC8E0)
    val Grass = Color(0xFF5A9A5A)
    val Branch = Color(0xFF5A4A3A)
    val Blossom = Color(0xFFF2A8B8)
    val Person = Color(0xFF3A3040)
    val Cloud = Color(0xFF4A4A58)
    val Gold = Color(0xFFE8B84A)
    val Dark = Color(0xFF3A3444)
    val Page = Color(0xFFF4ECD6)
    val Rule = Color(0xFFB8A880)
}

/**
 * Beats: cloudlift (one sits under a dark cloud of worry; it lifts and a wide gap of light opens over him), bowedtags (one
 * bows down, and three tags rise over him: «بِعُبُودِيَّتِهِ», «نَاصِيَتَهُ بِيَدِ اللَّهِ», «وَقَضَاءَهُ عَدْلٌ»), springheart (a
 * bare winter branch in the shape of a heart buds and blossoms while four tags come: «رَبِيعَ قَلْبِهِ», «وَنُورَ
 * صَدْرِهِ», «وَجَلَاءَ حُزْنِهِ», «وَذَهَابَ هَمِّهِ»), eightpairs (four pairs of dark shapes come close and stop at a line of
 * light), endless (a scroll of ruled lines unrolls beyond the edge of the picture and is never done).
 */
internal fun DrawScope.springheartScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "cloudlift" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "bowedtags" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "springheart" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "eightpairs" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "endless" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { springDay(a) }
}

private fun DrawScope.springDay(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cl = a.b("cloudlift")
    val bt = a.b("bowedtags")
    val sh = a.b("springheart")
    val ep = a.b("eightpairs")
    val en = a.b("endless")
    val ground = h * 0.84f
    val thaw = seg(sh, 0.2f, 0.9f)
    drawRect(Brush.verticalGradient(listOf(lerpColor(SpringC53.Winter, SpringC53.Spring, thaw), lerpColor(SpringC53.WinterLow, Color(0xFFF4ECD2), thaw)), 0f, ground), Offset.Zero, Size(w, ground))
    drawRect(lerpColor(SpringC53.Ground, SpringC53.Grass, thaw), Offset(0f, ground), Size(w, h - ground))
    if (cl > 0f) {
        val f = easeOut(seg(cl, 0f, BEAT_IN)) * (1f - seg(bt, 0f, BEAT_OUT))
        sit12(Offset(w * 0.5f, ground), h * 0.3f, SpringC53.Person, f, 1f)
        val lift = easeInOut(seg(cl, 0.35f, 0.85f))
        cloud38(Offset(w * 0.5f, h * (0.38f - 0.35f * lift)), w * 0.18f, SpringC53.Cloud, f * (1f - 0.7f * lift))
        if (lift > 0f) lightShafts(Offset(w * 0.5f, h * 0.05f), PI.toFloat() / 2f, 0.6f, h * 0.7f, Color(0xFFFFF0C8), 0.4f * f * lift, t, 6)
    }
    if (bt > 0f) {
        val f = easeOut(seg(bt, 0f, BEAT_IN)) * (1f - seg(sh, 0f, 0.4f))
        bow12(Offset(w * 0.5f, ground), h * 0.32f, SpringC53.Person, f, mix(0.5f, 1f, seg(bt, 0.05f, 0.25f)))
        for (k in 0 until 3) tagAt(a.tag("bowedtags", 0, k), Offset(w * (0.25f + 0.25f * k), h * (0.3f - 0.06f * (k % 2))), f * seg(bt, 0.2f + 0.2f * k, 0.35f + 0.2f * k), back = Back53, ink = ArtInk.Gold)
    }
    if (sh > 0f) {
        val f = easeOut(seg(sh, 0f, 0.3f)) * (1f - seg(ep, 0f, BEAT_OUT))
        val c = Offset(w * 0.5f, h * 0.5f)
        heart12(c, w * 0.3f, SpringC53.Branch, f)
        for (k in 0 until 14) {
            val ang = k * 0.45f
            val p = c + Offset(cos(ang) * w * 0.12f * (0.7f + 0.3f * sin(k * 1.3f)), sin(ang) * h * 0.18f * (0.7f + 0.3f * cos(k * 0.7f)) - h * 0.03f)
            val g = seg(sh, 0.25f + 0.04f * k, 0.45f + 0.04f * k)
            drawCircle(if (k % 3 == 0) SpringC53.Grass else SpringC53.Blossom, w * 0.012f * g, p, alpha = f)
        }
        for (k in 0 until 4) tagAt(a.tag("springheart", 0, k), Offset(w * (0.14f + 0.24f * k), h * (0.14f + 0.06f * (k % 2))), f * seg(sh, 0.15f + 0.18f * k, 0.3f + 0.18f * k), back = Back53, ink = ArtInk.Gold)
    }
    if (ep > 0f) {
        val f = easeOut(seg(ep, 0f, BEAT_IN)) * (1f - seg(en, 0f, BEAT_OUT))
        val line = w * 0.5f
        drawLine(SpringC53.Gold, Offset(line, h * 0.15f), Offset(line, ground), 3f * dp, StrokeCap.Round, alpha = f * seg(ep, 0.1f, 0.3f))
        softGlow(Offset(line, h * 0.5f), w * 0.06f, SpringC53.Gold, 0.3f * f)
        for (k in 0 until 4) {
            val y = h * (0.25f + 0.15f * k)
            val come = easeInOut(seg(ep, 0.15f + 0.1f * k, 0.6f + 0.1f * k))
            for (j in 0 until 2) {
                val x = w * (0.05f + 0.1f * j) + (line - w * 0.2f - w * 0.05f) * come
                drawCircle(SpringC53.Dark, w * 0.025f, Offset(x, y), alpha = f * 0.85f)
            }
        }
    }
    if (en > 0f) {
        val f = easeOut(seg(en, 0f, 0.3f))
        val roll = easeOut(seg(en, 0.1f, 1f))
        val y = h * 0.4f
        drawRect(SpringC53.Page, Offset(w * 0.1f, y), Size(w * 1.0f * roll, h * 0.2f), alpha = f)
        for (k in 0 until 4) drawLine(SpringC53.Rule, Offset(w * 0.13f, y + h * (0.04f + 0.04f * k)), Offset(w * (0.1f + 1.0f * roll), y + h * (0.04f + 0.04f * k)), 1.5f * dp, alpha = f)
        drawCircle(Color(0xFF8A6A44), h * 0.11f, Offset(w * 0.1f, y + h * 0.1f), alpha = f)
    }
}

// ═════════════ 4. the deep sea (hisn-karb) ═════════════

private object SeaC53 {
    val Sky = Color(0xFF14182E)
    val SkyLow = Color(0xFF2E3256)
    val Sea = Color(0xFF14304A)
    val Deep = Color(0xFF0A1626)
    val Wave = Color(0xFF3A6A8A)
    val Whale = Color(0xFF2A3A4E)
    val Boat = Color(0xFF8A6A44)
    val Gold = Color(0xFFE8C46A)
    val Shore = Color(0xFFCDB88E)
    val Leaf = Color(0xFF4A8A4A)
    val Dawn = Color(0xFFE8A07A)
}

/**
 * Beats: praisestars (a storm at night; one by one stars come out over it, each a word of praise: «الْعَظِيمُ الْحَلِيمُ»,
 * «رَبُّ الْعَرْشِ الْعَظِيمِ»), boatheld (a small boat on rough water; a thin gold line holds it steady), dhunnun (under
 * the sea a whale, and in it a small light with his name, «ذِي النُّونِ»; rings of his call rise to the surface), onename
 * (the words «اللَّهُ اللَّهُ رَبِّي» stand alone over a still sea), shore (dawn: the light is set down on a shore and a
 * leafy plant grows over it).
 */
internal fun DrawScope.deepseaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "praisestars" to Shot(Offset(w * 0.5f, h * 0.4f), 1.05f),
        "boatheld" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "dhunnun" to Shot(Offset(w * 0.5f, h * 0.65f), 1.15f),
        "onename" to Shot(Offset(w * 0.5f, h * 0.4f), 1.1f),
        "shore" to Shot(Offset(w * 0.5f, h * 0.6f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { seaNight(a) }
}

private fun DrawScope.seaNight(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ps = a.b("praisestars")
    val bh = a.b("boatheld")
    val dn = a.b("dhunnun")
    val on = a.b("onename")
    val sh = a.b("shore")
    val sea = h * 0.55f
    nightSky(t, SeaC53.Sky, SeaC53.SkyLow, sea, starAlpha = 0.3f)
    drawRect(Brush.verticalGradient(listOf(SeaC53.Sea, SeaC53.Deep), sea, h), Offset(0f, sea), Size(w, h - sea))
    val rough = (1f - seg(on, 0f, BEAT_OUT)) * (0.4f + 0.6f * (ps + bh).coerceAtMost(1f))
    for (k in 0 until 10) {
        val x = w * fract(k / 10f + t * 0.03f)
        drawArc(SeaC53.Wave, 180f, 180f, false, Offset(x - w * 0.05f, sea - h * 0.02f * rough), Size(w * 0.1f, h * 0.04f * (0.3f + rough)), alpha = 0.7f, style = Stroke(2f * dp))
    }
    if (ps > 0f) {
        val f = easeOut(seg(ps, 0f, BEAT_IN)) * (1f - seg(bh, 0f, BEAT_OUT))
        cloud38(Offset(w * 0.3f, h * 0.15f), w * 0.12f, Color(0xFF3A3A50), f * (1f - seg(ps, 0.5f, 0.9f)))
        for (k in 0 until 7) drawPath(starPath(Offset(w * (0.15f + 0.12f * k), h * (0.12f + 0.08f * (k % 3))), w * 0.012f), SeaC53.Gold, alpha = f * seg(ps, 0.1f + 0.1f * k, 0.25f + 0.1f * k))
        for (k in 0 until 2) tagAt(a.tag("praisestars", 0, k), Offset(w * (0.3f + 0.4f * k), h * 0.38f), f * seg(ps, 0.2f + 0.35f * k, 0.35f + 0.35f * k), back = Back53, ink = ArtInk.Gold)
    }
    if (bh > 0f) {
        val f = easeOut(seg(bh, 0f, BEAT_IN)) * (1f - seg(dn, 0f, BEAT_OUT))
        val hold = seg(bh, 0.3f, 0.6f)
        val tilt = 18f * sin(t * 2.2f) * (1f - 0.85f * hold)
        val c = Offset(w * 0.5f, sea)
        rotate(tilt, c) {
            drawPath(Path().apply { moveTo(c.x - w * 0.1f, c.y - h * 0.03f); lineTo(c.x + w * 0.1f, c.y - h * 0.03f); lineTo(c.x + w * 0.07f, c.y + h * 0.03f); lineTo(c.x - w * 0.07f, c.y + h * 0.03f); close() }, SeaC53.Boat, alpha = f)
            drawLine(SeaC53.Boat, c + Offset(0f, -h * 0.03f), c + Offset(0f, -h * 0.2f), 2.5f * dp, alpha = f)
        }
        drawLine(SeaC53.Gold, c + Offset(0f, -h * 0.2f), Offset(c.x, 0f), 1.2f * dp, alpha = f * hold)
    }
    if (dn > 0f) {
        val f = easeOut(seg(dn, 0f, BEAT_IN)) * (1f - seg(on, 0f, BEAT_OUT))
        val c = Offset(w * 0.45f, h * 0.78f)
        drawOval(SeaC53.Whale, Offset(c.x - w * 0.26f, c.y - h * 0.09f), Size(w * 0.5f, h * 0.18f), alpha = f)
        drawPath(Path().apply { moveTo(c.x + w * 0.22f, c.y); lineTo(c.x + w * 0.34f, c.y - h * 0.08f); lineTo(c.x + w * 0.33f, c.y + h * 0.07f); close() }, SeaC53.Whale, alpha = f)
        val light = c + Offset(-w * 0.04f, 0f)
        nabiLight14(light, w * 0.008f, f * seg(dn, 0.1f, 0.3f), t, a.tag("dhunnun"), c + Offset(-w * 0.04f, -h * 0.17f))
        for (k in 0 until 3) {
            val u = fract(t * 0.5f + k / 3f)
            val y = c.y - h * 0.05f - (c.y - sea) * u
            drawCircle(SeaC53.Gold, w * (0.01f + 0.04f * u), Offset(light.x, y), alpha = f * seg(dn, 0.3f, 0.5f) * (1f - u) * 0.7f, style = Stroke(1.5f * dp))
        }
    }
    if (on > 0f) {
        val f = easeOut(seg(on, 0f, BEAT_IN)) * (1f - seg(sh, 0f, 0.4f))
        fitBig26(a.big("onename"), Offset(w * 0.5f, h * 0.32f), w * 0.5f, ArtInk.Gold, f * seg(on, 0.1f, 0.35f), glow = ArtInk.Gold)
    }
    if (sh > 0f) {
        val f = easeOut(seg(sh, 0f, 0.3f))
        val dawn = seg(sh, 0.05f, 0.6f)
        drawRect(Brush.verticalGradient(listOf(lerpColor(SeaC53.Sky, Color(0xFF6A88B8), dawn), lerpColor(SeaC53.SkyLow, SeaC53.Dawn, dawn)), 0f, sea), Offset.Zero, Size(w, sea), alpha = f)
        drawPath(Path().apply { moveTo(w * 0.45f, h); quadraticTo(w * 0.62f, sea - h * 0.04f, w, sea - h * 0.06f); lineTo(w, h); close() }, SeaC53.Shore, alpha = f)
        val light = Offset(w * 0.74f, sea - h * 0.02f)
        nabiLight14(light, w * 0.008f, f * seg(sh, 0.2f, 0.4f), t, null, light)
        val grow = seg(sh, 0.45f, 0.9f)
        for (k in 0 until 5) drawOval(SeaC53.Leaf, Offset(light.x - w * 0.08f + w * 0.035f * k, light.y - h * (0.12f + 0.03f * (k % 2)) * grow), Size(w * 0.05f * grow, h * 0.06f * grow), alpha = f)
        drawLine(Color(0xFF5A4030), light + Offset(-w * 0.1f, -h * 0.02f), light + Offset(w * 0.08f, -h * 0.1f * grow), 2f * dp, alpha = f * grow)
    }
}

// ═════════════ 5. a camp and what turns back from it (hisn-aduw) ═════════════

private object CampC53 {
    val Sky = Color(0xFFC8B08A)
    val SkyTop = Color(0xFF8AA4C0)
    val Ground = Color(0xFFC4A878)
    val Tent = Color(0xFF7A5A44)
    val Mass = Color(0xFF4A3E3A)
    val Fire = Color(0xFFE0682E)
    val Cool = Color(0xFF8AD0A0)
    val Gold = Color(0xFFE8B84A)
    val Wind = Color(0xFFE8E0D0)
    val Tower = Color(0xFF6A5A5A)
    val Wall = Color(0xFFDCC6A0)
    val Trim = Color(0xFF8A6A44)
}

/**
 * Beats: turnsback (a dark mass of dust comes toward a small camp; it slows, stops and turns away), coolfire (a fire;
 * in it a small light with his name, «إِبْرَاهِيمُ», and the flames turn cool and green; beside it the light ﷺ, and dark
 * masses gathered round that come to nothing), suffice (a wind sweeps a dark mass off the plain), trenchwind (tents of
 * the confederates shake and blow down in a night wind), neighbour (a small house beside a leaning tower, kept under a
 * dome of light).
 */
internal fun DrawScope.campguardScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "turnsback" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "coolfire" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
        "suffice" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "trenchwind" to Shot(Offset(w * 0.5f, h * 0.6f), 1.1f),
        "neighbour" to Shot(Offset(w * 0.5f, h * 0.55f), 1.15f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.06f) { campPlain(a) }
}

private fun DrawScope.dustMass53(c: Offset, r: Float, color: Color, alpha: Float, t: Float) {
    if (alpha <= 0.01f) return
    for (k in 0 until 9) {
        val ang = k * 0.7f + t * 0.4f
        drawCircle(color, r * (0.45f + 0.15f * sin(k + t)), c + Offset(cos(ang) * r * 0.6f, sin(ang) * r * 0.25f), alpha = alpha * 0.8f)
    }
}

private fun DrawScope.campPlain(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val tb = a.b("turnsback")
    val cf = a.b("coolfire")
    val sf = a.b("suffice")
    val tw = a.b("trenchwind")
    val nb = a.b("neighbour")
    val ground = h * 0.82f
    daySky12(CampC53.SkyTop, CampC53.Sky, ground, CampC53.Ground)
    if (tb > 0f) {
        val f = easeOut(seg(tb, 0f, BEAT_IN)) * (1f - seg(cf, 0f, BEAT_OUT))
        for (k in 0 until 3) drawPath(Path().apply { val x = w * (0.12f + 0.08f * k); moveTo(x - w * 0.05f, ground); lineTo(x, ground - h * 0.12f); lineTo(x + w * 0.05f, ground); close() }, CampC53.Tent, alpha = f)
        val come = easeOut(seg(tb, 0.05f, 0.5f))
        val back = easeInOut(seg(tb, 0.6f, 1f))
        dustMass53(Offset(w * (0.9f - 0.35f * come + 0.4f * back), ground - h * 0.08f), w * 0.14f, CampC53.Mass, f * (1f - 0.6f * back), t)
    }
    if (cf > 0f) {
        val f = easeOut(seg(cf, 0f, BEAT_IN)) * (1f - seg(sf, 0f, BEAT_OUT))
        val c = Offset(w * 0.3f, ground)
        val cool = seg(cf, 0.25f, 0.55f)
        for (k in 0 until 6) drawOval(lerpColor(CampC53.Fire, CampC53.Cool, cool), Offset(c.x - w * 0.12f + w * 0.04f * k, ground - h * (0.25f + 0.08f * sin(t * 7f + k))), Size(w * 0.04f, h * 0.24f), alpha = f * 0.85f)
        val light = c + Offset(0f, -h * 0.12f)
        nabiLight14(light, w * 0.008f, f * seg(cf, 0.05f, 0.2f), t, a.tag("coolfire"), c + Offset(0f, -h * 0.42f))
        val p = Offset(w * 0.72f, h * 0.45f)
        sirajMunir(p, w * 0.013f, f * seg(cf, 0.55f, 0.7f), t, nameAt = p + Offset(0f, -h * 0.13f))
        for (k in 0 until 3) dustMass53(Offset(w * (0.6f + 0.12f * k), ground - h * 0.05f), w * 0.06f, CampC53.Mass, f * seg(cf, 0.6f, 0.75f) * (1f - seg(cf, 0.8f, 1f)), t + k)
    }
    if (sf > 0f) {
        val f = easeOut(seg(sf, 0f, BEAT_IN)) * (1f - seg(tw, 0f, BEAT_OUT))
        val sweep = easeInOut(seg(sf, 0.3f, 0.9f))
        dustMass53(Offset(w * (0.55f + 0.6f * sweep), ground - h * 0.08f), w * 0.14f, CampC53.Mass, f * (1f - sweep), t)
        for (k in 0 until 10) {
            val y = h * (0.3f + 0.05f * k)
            val x = w * fract(t * 0.6f + k * 0.13f)
            drawLine(CampC53.Wind, Offset(x, y), Offset(x + w * 0.1f, y), 1.5f * dp, StrokeCap.Round, alpha = f * seg(sf, 0.2f, 0.4f) * 0.7f)
        }
    }
    if (tw > 0f) {
        val f = easeOut(seg(tw, 0f, BEAT_IN)) * (1f - seg(nb, 0f, BEAT_OUT))
        drawRect(Color(0xFF1C2036), Offset.Zero, Size(w, ground), alpha = f)
        drawRect(Color(0xFF3A3230), Offset(w * 0.08f, ground), Size(w * 0.84f, h * 0.03f), alpha = f)
        for (k in 0 until 5) {
            val x = w * (0.55f + 0.08f * k)
            val down = seg(tw, 0.2f + 0.1f * k, 0.5f + 0.1f * k)
            rotate(60f * down + 6f * sin(t * 6f + k) * (1f - down), Offset(x, ground)) {
                drawPath(Path().apply { moveTo(x - w * 0.04f, ground); lineTo(x, ground - h * 0.12f); lineTo(x + w * 0.04f, ground); close() }, CampC53.Tent, alpha = f)
            }
        }
        for (k in 0 until 12) {
            val y = h * (0.2f + 0.05f * k)
            val x = w * fract(t * 0.9f + k * 0.17f)
            drawLine(CampC53.Wind, Offset(x, y), Offset(x + w * 0.12f, y), 1.2f * dp, StrokeCap.Round, alpha = f * 0.5f)
        }
    }
    if (nb > 0f) {
        val f = easeOut(seg(nb, 0f, 0.3f))
        val house = Offset(w * 0.36f, ground)
        house12(house, w * 0.18f, CampC53.Wall, CampC53.Trim, f, 0.5f)
        val lean = 6f + 4f * sin(t)
        rotate(-lean, Offset(w * 0.66f, ground)) {
            drawRect(CampC53.Tower, Offset(w * 0.6f, ground - h * 0.6f), Size(w * 0.12f, h * 0.6f), alpha = f)
            drawRect(Color(0xFF4A3E3E), Offset(w * 0.58f, ground - h * 0.64f), Size(w * 0.16f, h * 0.05f), alpha = f)
        }
        guardDome12(house, w * 0.16f, seg(nb, 0.2f, 0.5f), t, f)
    }
}
