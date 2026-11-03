package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/*
 * Wave 80 (2026-11-03): v1.3 fiqh, prayer 1 («رسالتان في الصلاة» of Ibn Baz and «الملخص الفقهي» of al-Fawzān). The rank of prayer,
 * the adhan, the five times by the sun and the shadow, the conditions, walking to the mosque, and standing for the opening takbir.
 * The one praying is a faceless figure facing left, towards the qibla. The night journey is only a rising light.
 */

internal object SalC {
    val Top = Color(0xFF101830)
    val Low = Color(0xFF2E3A5A)
    val Ground = Color(0xFF1C1E2C)
    val DayTop = Color(0xFF8AB8DA)
    val DayLow = Color(0xFFF2E2C2)
    val Sand = Color(0xFFC8B48E)
    val Person = Color(0xFFE8DCC8)
    val Gold = Color(0xFFF2C860)
    val Rim = Color(0xFF8A94B8)
    val Cell = Color(0xFF3A4468)
    val Mosque = Color(0xFFD8CCB4)
    val Dome = Color(0xFF4E7A6A)
    val Call = Color(0xFF8AC4EA)
}

/** One standing for the opening takbir, facing left; [raise] lifts the hands from the sides to the ears. */
internal fun DrawScope.takbir80(foot: Offset, tall: Float, color: Color, alpha: Float, raise: Float) {
    if (alpha <= 0.01f) return
    val hip = foot - Offset(0f, tall * 0.45f)
    val neck = hip - Offset(0f, tall * 0.31f)
    val r = raise.coerceIn(0f, 1f)
    val handA = Offset(neck.x - tall * mix(0.08f, 0.06f, r), mix(neck.y + tall * 0.28f, neck.y - tall * 0.1f, r))
    val handB = Offset(neck.x + tall * mix(0.08f, 0.06f, r), mix(neck.y + tall * 0.28f, neck.y - tall * 0.1f, r))
    posed14(hip, neck, handA, handB, foot + Offset(-tall * 0.06f, 0f), foot + Offset(tall * 0.06f, 0f), tall, color, alpha)
}

/** One sitting on the heels as in the tashahhud, facing left, [seat] on the ground under the hips; [point] lifts a finger. */
internal fun DrawScope.kneel80(seat: Offset, tall: Float, color: Color, alpha: Float, point: Float = 0f) {
    if (alpha <= 0.01f) return
    val sw = max(1.7f * density, tall * 0.065f)
    val hip = seat - Offset(0f, tall * 0.1f)
    val knee = Offset(seat.x - tall * 0.26f, seat.y - tall * 0.02f)
    val toe = Offset(seat.x + tall * 0.06f, seat.y)
    val shoulder = hip - Offset(0f, tall * 0.33f)
    val head = shoulder - Offset(0f, tall * 0.14f)
    drawLine(color, toe, knee, sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, knee, sw, StrokeCap.Round, alpha = alpha)
    drawLine(color, hip, shoulder, sw, StrokeCap.Round, alpha = alpha)
    drawCircle(color, tall * 0.1f, head, alpha = alpha)
    val hand = Offset(knee.x + tall * 0.06f, knee.y - tall * 0.08f)
    drawLine(color, shoulder, hand, sw * 0.9f, StrokeCap.Round, alpha = alpha)
    if (point > 0f) drawLine(color, hand, hand + Offset(-tall * 0.06f, -tall * 0.04f) * point, sw * 0.5f, StrokeCap.Round, alpha = alpha)
}

/** A straight stick in the ground, its foot at [foot], and the shadow it casts for the sun at [sun]. */
private fun DrawScope.shadowStick80(foot: Offset, ht: Float, sun: Offset, alpha: Float) {
    if (alpha <= 0.01f) return
    val dir = foot - sun
    val k = if (dir.y > 1f) ht / dir.y else 3f
    val len = (dir.x * k).coerceIn(-ht * 3f, ht * 3f)
    drawLine(Color(0x99201C28), foot, foot + Offset(len, 0f), ht * 0.08f, StrokeCap.Round, alpha = alpha)
    drawLine(Color(0xFF6A4A2E), foot, foot - Offset(0f, ht), ht * 0.06f, StrokeCap.Round, alpha = alpha)
}

// ═════════════ 1. sal-wujub: the rank of prayer ═════════════

/**
 * Beats: secondpillar (five pillars rise, the second one brightest: «آكَدُ أَرْكَانِ الْإِسْلَامِ بَعْدَ الشَّهَادَتَيْنِ»), miraj (a light rises through
 * seven heavens: «لَيْلَةَ الْمِعْرَاجِ فِي السَّمَاءِ»), openclose (a row of the prayer's postures between a gate that opens with the takbir and one
 * that closes with the salam: «مُفْتَتَحَةٌ بِالتَّكْبِيرِ», «مُخْتَتَمَةٌ بِالتَّسْلِيمِ»), ontimeanyway (one ill prays seated while the clock is
 * still inside the time: «بَلْ يُصَلِّيهَا فِي وَقْتِهَا عَلَى حَسَبِ حَالِهِ»), sevenyears (seven small lights for a child's years, and a grown one
 * beside him: «إِذَا بَلَغَ سَبْعَ سِنِينَ»).
 */
internal fun DrawScope.prayerrankScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "secondpillar" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "miraj" to Shot(Offset(w * 0.5f, h * 0.45f), 1f),
        "openclose" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "ontimeanyway" to Shot(Offset(w * 0.5f, h * 0.55f), 1.05f),
        "sevenyears" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { prayerRankDraw(a) }
}

private fun DrawScope.prayerRankDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.8f
    nightSky(t, SalC.Top, SalC.Low, ground, starAlpha = 0.7f)
    drawRect(SalC.Ground, Offset(0f, ground), Size(w, h - ground))
    val sp = fqIn(a, "secondpillar", "miraj")
    val mj = fqIn(a, "miraj", "openclose")
    val oc = fqIn(a, "openclose", "ontimeanyway")
    val ot = fqIn(a, "ontimeanyway", "sevenyears")
    val sy = fqIn(a, "sevenyears")
    if (sp > 0f) {
        val p = a.b("secondpillar")
        for (k in 0 until 5) {
            val g = easeOut(seg(p, 0.05f + 0.08f * k, 0.2f + 0.08f * k))
            val x = w * (0.82f - 0.16f * k)
            val top = ground - h * 0.42f * g
            val lit = if (k == 1) seg(p, 0.5f, 0.7f) else 0f
            drawRect(lerpColor(SalC.Mosque, SalC.Gold, lit), Offset(x - w * 0.035f, top), Size(w * 0.07f, ground - top), alpha = sp * (if (k == 1) 1f else 0.6f))
            if (k == 1) softGlow(Offset(x, top + h * 0.15f), w * 0.1f, SalC.Gold, 0.6f * sp * lit)
        }
        tagAt(a.tag("secondpillar"), Offset(w * 0.5f, h * 0.88f), sp * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (mj > 0f) {
        val p = a.b("miraj")
        val base = Offset(w * 0.5f, ground)
        for (k in 0 until 7) {
            val y = h * (0.7f - 0.09f * k)
            drawArc(SalC.Rim, 200f, 140f, false, Offset(w * 0.5f - w * (0.42f - 0.03f * k), y - h * 0.06f), Size(w * (0.84f - 0.06f * k), h * 0.2f), alpha = mj * 0.35f * seg(p, 0.05f * k, 0.05f * k + 0.2f), style = Stroke(1.5f * dp))
        }
        val up = easeInOut(seg(p, 0.15f, 0.8f))
        val tip = Offset(base.x, mix(ground, h * 0.06f, up))
        drawLine(SalC.Gold, base, tip, 3f * dp, StrokeCap.Round, alpha = mj * 0.7f)
        softGlow(tip, w * 0.06f, SalC.Gold, mj)
        tagAt(a.tag("miraj"), Offset(w * 0.5f, h * 0.9f), mj * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (oc > 0f) {
        val p = a.b("openclose")
        val y = ground
        val gl = seg(p, 0.05f, 0.25f)
        drawPath(archPath26(w * 0.88f, y - h * 0.44f, y, w * 0.06f), SalC.Gold, alpha = oc * gl, style = Stroke(3f * dp))
        tagAt(a.tag("openclose", 0, 0), Offset(w * 0.82f, h * 0.18f), oc * gl, back = Fq.Back, ink = ArtInk.Gold)
        val poses = listOf(0f, 0.5f, 1f, -1f)
        for (k in 0 until 4) {
            val g = seg(p, 0.2f + 0.1f * k, 0.3f + 0.1f * k)
            val foot = Offset(w * (0.72f - 0.14f * k), y)
            when {
                poses[k] < 0f -> kneel80(foot, h * 0.3f, SalC.Person, oc * g)
                poses[k] == 0f -> takbir80(foot, h * 0.3f, SalC.Person, oc * g, 1f)
                else -> bow12(foot, h * 0.3f, SalC.Person, oc * g, poses[k])
            }
        }
        val gr = seg(p, 0.6f, 0.8f)
        drawPath(archPath26(w * 0.12f, y - h * 0.44f, y, w * 0.06f), SalC.Gold, alpha = oc * gr, style = Stroke(3f * dp))
        tagAt(a.tag("openclose", 0, 1), Offset(w * 0.2f, h * 0.18f), oc * gr, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ot > 0f) {
        val p = a.b("ontimeanyway")
        val bed = Offset(w * 0.62f, ground)
        drawRoundRect(Color(0xFF6A5A4A), Offset(bed.x - w * 0.18f, bed.y - h * 0.08f), Size(w * 0.36f, h * 0.08f), CornerRadius(h * 0.01f), alpha = ot)
        sit12(bed + Offset(w * 0.05f, -h * 0.08f), h * 0.3f, SalC.Person, ot, -1f)
        val c = Offset(w * 0.2f, h * 0.36f)
        val r = w * 0.09f
        drawArc(SalC.Gold, -90f, 140f, true, Offset(c.x - r, c.y - r), Size(r * 2f, r * 2f), alpha = ot * 0.3f)
        clock22(c, r, SalC.Gold, 0.15f + 0.2f * seg(p, 0.1f, 0.8f), ot)
        tagAt(a.tag("ontimeanyway"), Offset(w * 0.55f, h * 0.18f), ot * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (sy > 0f) {
        val p = a.b("sevenyears")
        figure(Offset(w * 0.72f, ground), h * 0.4f, SalC.Person, sy, arms = 0.5f)
        child12(Offset(w * 0.55f, ground), h * 0.38f, SalC.Person, sy)
        for (k in 0 until 7) {
            val g = seg(p, 0.05f + 0.07f * k, 0.12f + 0.07f * k)
            drawCircle(SalC.Gold, w * 0.014f, Offset(w * (0.42f - 0.045f * k), h * 0.3f), alpha = sy * g)
        }
        fqRug(Offset(w * 0.25f, ground - h * 0.03f), w * 0.1f, h * 0.06f, Color(0xFF6A2A34), SalC.Gold, sy * seg(p, 0.6f, 0.75f))
        tagAt(a.tag("sevenyears"), Offset(w * 0.3f, h * 0.15f), sy * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 2. sal-adhan: the call ═════════════

/**
 * Beats: announcetime (the call goes out from a minaret as the clock reaches the time: «إِعْلَامًا بِدُخُولِ الْوَقْتِ»), kifaya (one call spreads over
 * many houses: «فَرْضُ كِفَايَةٍ عِنْدَ الْمُؤَلِّفِ», «مِنْ شَعَائِرِ الْإِسْلَامِ الظَّاهِرَةِ»), fifteenphrases (fifteen lights, one per phrase, and at
 * dawn two more: «خَمْسَ عَشْرَةَ جُمْلَةً», «الصَّلَاةُ خَيْرٌ مِنَ النَّوْمِ»), turnrightleft (the caller turns right, then left: «حَيَّ عَلَى
 * الصَّلَاةِ», «حَيَّ عَلَى الْفَلَاحِ»), nostepout (after the call, a step out of the mosque's door is struck out: «وَيَحْرُمُ الْخُرُوجُ مِنَ الْمَسْجِدِ
 * بَعْدَ الْأَذَانِ»).
 */
internal fun DrawScope.adhancallScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "announcetime" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "kifaya" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "fifteenphrases" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "turnrightleft" to Shot(Offset(w * 0.5f, h * 0.4f), 1.1f),
        "nostepout" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { adhanCallDraw(a) }
}

private fun DrawScope.adhanCallDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.84f
    val ff = fqIn(a, "fifteenphrases", "turnrightleft")
    val dawn = seg(a.b("fifteenphrases"), 0.6f, 0.8f) * (1f - seg(a.b("turnrightleft"), 0f, 0.4f))
    nightSky(t, SalC.Top, lerpColor(SalC.Low, Color(0xFFE8B890), dawn), ground, starAlpha = 0.7f * (1f - dawn))
    drawRect(SalC.Ground, Offset(0f, ground), Size(w, h - ground))
    val at = fqIn(a, "announcetime", "kifaya")
    val kf = fqIn(a, "kifaya", "fifteenphrases")
    val tr = fqIn(a, "turnrightleft", "nostepout")
    val ns = fqIn(a, "nostepout")
    val minaret = Offset(w * 0.5f, ground)
    val top = minaret + Offset(0f, -h * 0.6f)
    val tower = maxOf(at, kf, tr)
    if (tower > 0f) {
        drawRect(SalC.Mosque, Offset(minaret.x - w * 0.035f, top.y), Size(w * 0.07f, minaret.y - top.y), alpha = tower)
        drawRect(SalC.Mosque, Offset(minaret.x - w * 0.055f, top.y + h * 0.05f), Size(w * 0.11f, h * 0.025f), alpha = tower)
        drawArc(SalC.Dome, 180f, 180f, true, Offset(minaret.x - w * 0.035f, top.y - h * 0.035f), Size(w * 0.07f, h * 0.07f), alpha = tower)
    }
    if (at > 0f) {
        val p = a.b("announcetime")
        clock22(Offset(w * 0.18f, h * 0.3f), w * 0.07f, SalC.Gold, seg(p, 0f, 0.3f) * 0.25f, at)
        voice12(top + Offset(-w * 0.08f, h * 0.02f), w * 0.12f, t, SalC.Call, at * seg(p, 0.3f, 0.45f), -1f)
        voice12(top + Offset(w * 0.08f, h * 0.02f), w * 0.12f, t, SalC.Call, at * seg(p, 0.3f, 0.45f), 1f)
        tagAt(a.tag("announcetime"), Offset(w * 0.75f, h * 0.2f), at * seg(p, 0.35f, 0.55f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (kf > 0f) {
        val p = a.b("kifaya")
        for (k in 0 until 6) {
            val x = w * (0.08f + 0.17f * k + (if (k >= 3) 0.1f else 0f))
            house12(Offset(x, ground), w * 0.1f, Color(0xFFCDBFA6), Color(0xFF6A5A48), kf, lit = seg(p, 0.25f + 0.05f * k, 0.35f + 0.05f * k))
        }
        val spread = seg(p, 0.1f, 0.6f)
        drawArc(SalC.Call, 180f, 180f, false, Offset(w * 0.5f - w * 0.55f * spread, top.y + h * 0.05f - h * 0.3f * spread), Size(w * 1.1f * spread, h * 0.6f * spread), alpha = kf * 0.4f, style = Stroke(2f * dp))
        tagAt(a.tag("kifaya", 0, 0), Offset(w * 0.25f, h * 0.15f), kf * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("kifaya", 0, 1), Offset(w * 0.75f, h * 0.15f), kf * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (ff > 0f) {
        val p = a.b("fifteenphrases")
        for (k in 0 until 15) {
            val g = seg(p, 0.03f * k, 0.03f * k + 0.08f)
            drawCircle(SalC.Gold, w * 0.014f, Offset(w * (0.92f - 0.059f * k), h * 0.45f), alpha = ff * (0.25f + 0.75f * g))
        }
        tagAt(a.tag("fifteenphrases", 0, 0), Offset(w * 0.5f, h * 0.3f), ff * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
        for (k in 0 until 2) drawCircle(Color(0xFFF2D8B0), w * 0.016f, Offset(w * (0.55f - 0.06f * k), h * 0.58f), alpha = ff * seg(p, 0.7f + 0.05f * k, 0.8f + 0.05f * k))
        tagAt(a.tag("fifteenphrases", 0, 1), Offset(w * 0.5f, h * 0.7f), ff * seg(p, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (tr > 0f) {
        val p = a.b("turnrightleft")
        val gallery = top + Offset(0f, h * 0.05f)
        val turn = seg(p, 0.1f, 0.25f) - 2f * seg(p, 0.5f, 0.65f)
        val foot = gallery
        figure(foot, h * 0.12f, SalC.Person, tr, lean = -0.25f * turn)
        val right = seg(p, 0.15f, 0.25f) * (1f - seg(p, 0.45f, 0.55f))
        val left = seg(p, 0.6f, 0.7f)
        voice12(gallery + Offset(w * 0.08f, -h * 0.08f), w * 0.1f, t, SalC.Call, tr * right, 1f)
        voice12(gallery + Offset(-w * 0.08f, -h * 0.08f), w * 0.1f, t, SalC.Call, tr * left, -1f)
        tagAt(a.tag("turnrightleft", 0, 0), Offset(w * 0.8f, h * 0.3f), tr * right, back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("turnrightleft", 0, 1), Offset(w * 0.2f, h * 0.3f), tr * left, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ns > 0f) {
        val p = a.b("nostepout")
        mosque12(Offset(w * 0.62f, ground), w * 0.4f, SalC.Mosque, SalC.Dome, ns, 0.6f)
        voice12(Offset(w * 0.75f, h * 0.2f), w * 0.06f, t, SalC.Call, ns * 0.6f, 1f)
        val u = easeInOut(seg(p, 0.1f, 0.4f))
        figure(Offset(mix(w * 0.55f, w * 0.3f, u), ground), h * 0.26f, SalC.Person, ns, walk = if (u in 0.01f..0.99f) t * 6f else 0f)
        cross14(Offset(w * 0.3f, ground - h * 0.38f), w * 0.04f, seg(p, 0.4f, 0.55f))
        tagAt(a.tag("nostepout"), Offset(w * 0.4f, h * 0.12f), ns * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Ember)
    }
}

// ═════════════ 3. sal-mawaqit: the five times ═════════════

/**
 * Beats: zuhrshadow (the sun passes its height and a stick's shadow grows to its own length: «مِنْ زَوَالِ الشَّمْسِ», «ظِلُّ كُلِّ شَيْءٍ مِثْلَهُ»),
 * asryellow (the sun yellows, then sets: «إِلَى اصْفِرَارِ الشَّمْسِ», «وَإِلَى الْغُرُوبِ وَقْتُ اضْطِرَارٍ»), maghribisha (a red band of twilight fades,
 * then the night is halved: «إِلَى مَغِيبِ الشَّفَقِ الْأَحْمَرِ», «وَوَقْتُ الْعِشَاءِ إِلَى نِصْفِ اللَّيْلِ»), fajrline (a white line spreads along the
 * horizon until the sun's rim: «مِنْ طُلُوعِ الْفَجْرِ الثَّانِي», «إِلَى طُلُوعِ الشَّمْسِ»), middleprayer (five lights in a row, the middle one
 * brightest: «وَهِيَ الصَّلَاةُ الْوُسْطَى»).
 */
internal fun DrawScope.fivetimesScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "zuhrshadow" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "asryellow" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
        "maghribisha" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "fajrline" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "middleprayer" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { fiveTimesDraw(a) }
}

private fun DrawScope.fiveTimesDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ground = h * 0.78f
    val zs = fqIn(a, "zuhrshadow", "asryellow")
    val ay = fqIn(a, "asryellow", "maghribisha")
    val mi = fqIn(a, "maghribisha", "fajrline")
    val fl = fqIn(a, "fajrline", "middleprayer")
    val mp = fqIn(a, "middleprayer")
    val sunAt = { u: Float -> val ang = PI.toFloat() * (1f - u); Offset(w * 0.5f + cos(ang) * w * 0.42f, ground - sin(ang) * h * 0.62f) }
    if (zs > 0f || ay > 0f) {
        val dayF = maxOf(zs, ay)
        val pz = a.b("zuhrshadow")
        val pa = a.b("asryellow")
        val u = if (ay > 0f) mix(0.72f, 1.02f, easeInOut(seg(pa, 0.05f, 0.85f))) else mix(0.45f, 0.72f, easeInOut(seg(pz, 0.05f, 0.85f)))
        val yellow = seg(u, 0.8f, 0.92f)
        val dusk = seg(u, 0.92f, 1.02f)
        daySky12(lerpColor(SalC.DayTop, Color(0xFF6A5A8A), dusk), lerpColor(SalC.DayLow, Color(0xFFE8906A), maxOf(yellow * 0.6f, dusk)), ground, SalC.Sand)
        val sun = sunAt(u)
        drawCircle(lerpColor(Color(0xFFFFF2C0), Color(0xFFF2B040), yellow), w * 0.04f, sun, alpha = dayF)
        softGlow(sun, w * 0.08f, Color(0xFFFFE0A0), dayF * 0.5f)
        shadowStick80(Offset(w * 0.5f, ground), h * 0.22f, sun, dayF)
        if (zs > 0f) {
            val eq = seg(pz, 0.6f, 0.75f)
            drawLine(SalC.Gold, Offset(w * 0.5f, ground + h * 0.04f), Offset(w * 0.5f + h * 0.22f, ground + h * 0.04f), 2f * dp, alpha = zs * eq, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 3f * dp)))
            tagAt(a.tag("zuhrshadow", 0, 0), Offset(w * 0.5f, h * 0.1f), zs * seg(pz, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
            tagAt(a.tag("zuhrshadow", 0, 1), Offset(w * 0.68f, ground + h * 0.12f), zs * eq, back = Fq.Back, ink = ArtInk.Ivory)
        }
        if (ay > 0f) {
            tagAt(a.tag("asryellow", 0, 0), Offset(w * 0.5f, h * 0.1f), ay * seg(pa, 0.25f, 0.45f) * (1f - seg(pa, 0.6f, 0.7f)), back = Fq.Back, ink = ArtInk.Gold)
            tagAt(a.tag("asryellow", 0, 1), Offset(w * 0.5f, h * 0.1f), ay * seg(pa, 0.7f, 0.85f), back = Fq.Back, ink = ArtInk.Ember)
        }
    }
    if (mi > 0f) {
        val p = a.b("maghribisha")
        nightSky(t, SalC.Top, SalC.Low, ground, starAlpha = mi)
        drawRect(SalC.Ground, Offset(0f, ground), Size(w, h - ground), alpha = mi)
        val red = 1f - seg(p, 0.1f, 0.45f)
        drawRect(Color(0xFFC85040), Offset(0f, ground - h * 0.08f), Size(w, h * 0.08f), alpha = mi * 0.6f * red)
        tagAt(a.tag("maghribisha", 0, 0), Offset(w * 0.5f, h * 0.15f), mi * seg(p, 0.05f, 0.2f) * (1f - seg(p, 0.45f, 0.55f)), back = Fq.Back, ink = ArtInk.Ember)
        val g = seg(p, 0.55f, 0.75f)
        val y = h * 0.45f
        drawLine(SalC.Rim, Offset(w * 0.1f, y), Offset(w * 0.9f, y), 2f * dp, alpha = mi * g)
        drawLine(SalC.Gold, Offset(w * 0.5f, y - h * 0.06f), Offset(w * 0.5f, y + h * 0.06f), 3f * dp, StrokeCap.Round, alpha = mi * g)
        moon12(Offset(w * 0.5f, y - h * 0.15f), w * 0.03f, mi * g)
        tagAt(a.tag("maghribisha", 0, 1), Offset(w * 0.5f, h * 0.65f), mi * g, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (fl > 0f) {
        val p = a.b("fajrline")
        nightSky(t, SalC.Top, lerpColor(SalC.Low, Color(0xFFB8A0C8), seg(p, 0.1f, 0.6f)), ground, starAlpha = fl * (1f - seg(p, 0.3f, 0.8f)))
        drawRect(SalC.Ground, Offset(0f, ground), Size(w, h - ground), alpha = fl)
        val spread = easeOut(seg(p, 0.05f, 0.45f))
        drawLine(Color(0xFFF4F1E8), Offset(w * 0.5f - w * 0.5f * spread, ground - h * 0.01f), Offset(w * 0.5f + w * 0.5f * spread, ground - h * 0.01f), h * 0.02f, StrokeCap.Round, alpha = fl)
        tagAt(a.tag("fajrline", 0, 0), Offset(w * 0.5f, h * 0.2f), fl * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        val rise = seg(p, 0.6f, 0.85f)
        drawArc(Color(0xFFF2B040), 180f, 180f, true, Offset(w * 0.5f - w * 0.05f, ground - w * 0.05f * rise), Size(w * 0.1f, w * 0.1f * rise), alpha = fl * rise)
        tagAt(a.tag("fajrline", 0, 1), Offset(w * 0.5f, h * 0.9f), fl * rise, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (mp > 0f) {
        val p = a.b("middleprayer")
        nightSky(t, SalC.Top, SalC.Low, h, starAlpha = mp)
        for (k in 0 until 5) {
            val g = seg(p, 0.05f + 0.08f * k, 0.15f + 0.08f * k)
            val c = Offset(w * (0.82f - 0.16f * k), h * 0.5f)
            val mid = k == 2
            drawCircle(SalC.Gold, w * (if (mid) 0.04f else 0.025f), c, alpha = mp * g * (if (mid) 1f else 0.5f))
            if (mid) softGlow(c, w * 0.1f, SalC.Gold, mp * seg(p, 0.5f, 0.7f))
        }
        tagAt(a.tag("middleprayer"), Offset(w * 0.5f, h * 0.7f), mp * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 4. sal-shurut: the conditions of prayer ═════════════

/**
 * Beats: purityandtime (a drop of water and a clock at its hour: «الطَّهَارَةُ مِنَ الْحَدَثَيْنِ», «وَدُخُولُ الْوَقْتِ»), threemoreconds (a
 * garment, a clean rug, a compass to the House: «سَتْرُ الْعَوْرَةِ», «وَاجْتِنَابُ النَّجَاسَةِ فِي الْبَدَنِ وَالثَّوْبِ وَالْبُقْعَةِ», «وَاسْتِقْبَالُ
 * الْقِبْلَةِ»), intentheart (a heart lit, and words spoken aloud struck out: «وَمَحَلُّهَا الْقَلْبُ», «وَلَا يُشْرَعُ التَّلَفُّظُ بِهَا»),
 * condsvspillars (a band under and before a building, and the pillars inside it: «الشَّرْطُ يَتَقَدَّمُ عَلَى الْعِبَادَةِ وَيَسْتَمِرُّ مَعَهَا»,
 * «وَالْأَرْكَانُ مَا تَشْتَمِلُ عَلَيْهِ الْعِبَادَةُ»).
 */
internal fun DrawScope.prayercondsScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "purityandtime" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "threemoreconds" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "intentheart" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "condsvspillars" to Shot(Offset(w * 0.5f, h * 0.55f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { prayerCondsDraw(a) }
}

private fun DrawScope.prayerCondsDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    nightSky(t, SalC.Top, SalC.Low, h, starAlpha = 0.5f)
    val pt = fqIn(a, "purityandtime", "threemoreconds")
    val tm = fqIn(a, "threemoreconds", "intentheart")
    val ih = fqIn(a, "intentheart", "condsvspillars")
    val cp = fqIn(a, "condsvspillars")
    if (pt > 0f) {
        val p = a.b("purityandtime")
        val g0 = seg(p, 0.05f, 0.25f)
        fqDrop(Offset(w * 0.7f, h * 0.48f), w * 0.05f, Fq.Water, pt * g0)
        softGlow(Offset(w * 0.7f, h * 0.48f), w * 0.1f, Fq.Water, 0.4f * pt * g0)
        tagAt(a.tag("purityandtime", 0, 0), Offset(w * 0.7f, h * 0.78f), pt * g0, back = Fq.Back, ink = ArtInk.Ivory)
        val g1 = seg(p, 0.45f, 0.65f)
        clock22(Offset(w * 0.3f, h * 0.45f), w * 0.09f, SalC.Gold, 0.25f * g1, pt * g1)
        tagAt(a.tag("purityandtime", 0, 1), Offset(w * 0.3f, h * 0.78f), pt * g1, back = Fq.Back, ink = ArtInk.Gold)
    }
    if (tm > 0f) {
        val p = a.b("threemoreconds")
        for (k in 0 until 3) {
            val g = seg(p, 0.05f + 0.28f * k, 0.25f + 0.28f * k)
            val c = Offset(w * (0.8f - 0.3f * k), h * 0.45f)
            when (k) {
                0 -> fqShirt(c, w * 0.12f, Color(0xFFE8E2D4), tm * g)
                1 -> fqRug(c, w * 0.1f, h * 0.24f, Color(0xFF6A2A34), SalC.Gold, tm * g)
                else -> {
                    drawCircle(SalC.Rim, w * 0.07f, c, alpha = tm * g, style = Stroke(2f * dp))
                    drawLine(SalC.Gold, c, c + Offset(-w * 0.06f, -h * 0.04f), 3f * dp, StrokeCap.Round, alpha = tm * g)
                    kaaba29(c + Offset(-w * 0.1f, -h * 0.05f), w * 0.04f, tm * g)
                }
            }
            tagAt(a.tag("threemoreconds", 0, k), Offset(c.x, h * (if (k == 1) 0.82f else 0.18f)), tm * g, back = Fq.Back, ink = ArtInk.Ivory)
        }
    }
    if (ih > 0f) {
        val p = a.b("intentheart")
        val c = Offset(w * 0.62f, h * 0.48f)
        softGlow(c, w * 0.12f, SalC.Gold, ih * seg(p, 0.05f, 0.25f))
        heart12(c, w * 0.09f, SalC.Gold, ih * seg(p, 0.05f, 0.25f), fill = true)
        tagAt(a.tag("intentheart", 0, 0), Offset(c.x, h * 0.8f), ih * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.5f, 0.65f)
        lips73(Offset(w * 0.25f, h * 0.45f), w * 0.1f, SalC.Person, ih * g)
        voice12(Offset(w * 0.18f, h * 0.45f), w * 0.05f, t, SalC.Call, ih * g, -1f)
        cross14(Offset(w * 0.25f, h * 0.3f), w * 0.035f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("intentheart", 0, 1), Offset(w * 0.25f, h * 0.8f), ih * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (cp > 0f) {
        val p = a.b("condsvspillars")
        val ground = h * 0.72f
        val band = seg(p, 0.05f, 0.35f)
        drawRect(Fq.Water, Offset(w * (0.92f - 0.84f * band), ground), Size(w * 0.84f * band, h * 0.04f), alpha = cp * 0.7f)
        tagAt(a.tag("condsvspillars", 0, 0), Offset(w * 0.5f, h * 0.88f), cp * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        val bld = seg(p, 0.45f, 0.65f)
        drawRect(SalC.Mosque, Offset(w * 0.25f, ground - h * 0.03f), Size(w * 0.5f, h * 0.03f), alpha = cp * bld)
        drawRect(SalC.Mosque, Offset(w * 0.25f, ground - h * 0.42f), Size(w * 0.5f, h * 0.04f), alpha = cp * bld)
        for (k in 0 until 5) drawRect(SalC.Gold, Offset(w * (0.28f + 0.1f * k), ground - h * 0.38f), Size(w * 0.035f, h * 0.35f), alpha = cp * seg(p, 0.55f + 0.05f * k, 0.65f + 0.05f * k))
        tagAt(a.tag("condsvspillars", 0, 1), Offset(w * 0.5f, h * 0.15f), cp * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
}

// ═════════════ 5. sal-mashy: walking to the prayer ═════════════

/**
 * Beats: smallsteps (one walks early to the mosque with short steps, each step a spark: «لِيُدْرِكَ تَكْبِيرَةَ الْإِحْرَامِ», «وَلْيُقَارِبْ بَيْنَ
 * خُطَاهُ لِتَكْثُرَ حَسَنَاتُهُ»), waitdhikr (waiting, seated with beads and a book; interlaced fingers struck out: «بِذِكْرِ اللَّهِ وَتِلَاوَةِ الْقُرْآنِ»,
 * «وَتَجَنَّبَ الْعَبَثَ كَتَشْبِيكِ الْأَصَابِعِ»), straightrows (a row closes its gaps, shoulder to shoulder: «الصَّفِّ الْأَوَّلِ», «بِمُحَاذَاةِ
 * الْمَنَاكِبِ وَالْأَكْعُبِ وَسَدِّ الْفُرَجِ»), rightfootmosque (the right foot first through the door: «قَدَّمَ رِجْلَهُ الْيُمْنَى»), widestance (one who
 * spreads his feet too wide, struck out: «فَإِنَّ ذَلِكَ يُوجِدُ فُرَجًا وَيُؤْذِي الْمُصَلِّينَ»).
 */
internal fun DrawScope.walktoprayerScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "smallsteps" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "waitdhikr" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "straightrows" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "rightfootmosque" to Shot(Offset(w * 0.5f, h * 0.6f), 1.05f),
        "widestance" to Shot(Offset(w * 0.5f, h * 0.65f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { walkToPrayerDraw(a) }
}

private fun DrawScope.walkToPrayerDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ss = fqIn(a, "smallsteps", "waitdhikr")
    val wd = fqIn(a, "waitdhikr", "straightrows")
    val sr = fqIn(a, "straightrows", "rightfootmosque")
    val rf = fqIn(a, "rightfootmosque", "widestance")
    val ws = fqIn(a, "widestance")
    val ground = h * 0.82f
    if (ss > 0f || rf > 0f) {
        nightSky(t, SalC.Top, Color(0xFF6A6A9A), ground, starAlpha = 0.6f)
        drawRect(SalC.Ground, Offset(0f, ground), Size(w, h - ground))
    } else hall74(h * 0.62f, t)
    if (ss > 0f) {
        val p = a.b("smallsteps")
        mosque12(Offset(w * 0.18f, ground), w * 0.3f, SalC.Mosque, SalC.Dome, ss, 0.7f)
        val u = easeInOut(seg(p, 0.05f, 0.95f))
        val x = mix(w * 0.92f, w * 0.36f, u)
        figure(Offset(x, ground), h * 0.28f, SalC.Person, ss, walk = t * 9f, stride = 0.6f)
        for (k in 0 until 12) {
            val sx = w * 0.9f - k * w * 0.045f
            if (sx > x) {
                drawCircle(SalC.Gold, w * 0.007f, Offset(sx, ground + h * 0.03f), alpha = ss * 0.8f)
                glint(Offset(sx, ground + h * 0.03f), w * 0.012f, SalC.Gold, ss * 0.5f * (0.5f + 0.5f * sin(t * 3f + k)))
            }
        }
        tagAt(a.tag("smallsteps", 0, 0), Offset(w * 0.3f, h * 0.15f), ss * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("smallsteps", 0, 1), Offset(w * 0.6f, h * 0.3f), ss * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (wd > 0f) {
        val p = a.b("waitdhikr")
        val seat = Offset(w * 0.6f, h * 0.84f)
        sit12(seat, h * 0.28f, SalC.Person, wd, -1f)
        beads12(seat + Offset(-h * 0.12f, -h * 0.14f), w * 0.03f, SalC.Gold, wd * seg(p, 0.05f, 0.2f), t * 0.4f)
        openBook21(seat + Offset(-h * 0.2f, -h * 0.02f), w * 0.1f, Color(0xFFF2E8CE), Color(0xFFB8A888), wd * seg(p, 0.15f, 0.3f))
        tagAt(a.tag("waitdhikr", 0, 0), Offset(w * 0.6f, h * 0.15f), wd * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        val g = seg(p, 0.5f, 0.65f)
        val c = Offset(w * 0.2f, h * 0.45f)
        for (k in 0 until 4) {
            drawLine(SalC.Person, c + Offset(-w * 0.05f, -h * 0.04f + k * h * 0.025f), c + Offset(w * 0.05f, -h * 0.02f + k * h * 0.025f), 3f * dp, StrokeCap.Round, alpha = wd * g)
            drawLine(SalC.Person, c + Offset(-w * 0.05f, -h * 0.02f + k * h * 0.025f), c + Offset(w * 0.05f, -h * 0.04f + k * h * 0.025f), 3f * dp, StrokeCap.Round, alpha = wd * g * 0.7f)
        }
        cross14(c + Offset(0f, -h * 0.12f), w * 0.035f, seg(p, 0.6f, 0.75f))
        tagAt(a.tag("waitdhikr", 0, 1), Offset(w * 0.25f, h * 0.65f), wd * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (sr > 0f) {
        val p = a.b("straightrows")
        val close = easeInOut(seg(p, 0.3f, 0.7f))
        for (k in 0 until 6) {
            val loose = w * (0.12f + 0.15f * k) + (if (k % 2 == 0) w * 0.03f else -w * 0.02f)
            val tight = w * (0.2f + 0.12f * k)
            pray12(Offset(mix(loose, tight, close), h * 0.86f), h * 0.26f, SalC.Person, sr)
        }
        drawLine(SalC.Gold, Offset(w * 0.15f, h * 0.88f), Offset(w * 0.85f, h * 0.88f), 2f * dp, alpha = sr * seg(p, 0.65f, 0.8f))
        tagAt(a.tag("straightrows", 0, 0), Offset(w * 0.5f, h * 0.16f), sr * seg(p, 0.05f, 0.25f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("straightrows", 0, 1), Offset(w * 0.5f, h * 0.36f), sr * seg(p, 0.5f, 0.7f), back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (rf > 0f) {
        val p = a.b("rightfootmosque")
        door12(Offset(w * 0.5f, ground), w * 0.2f, h * 0.45f, 0.8f, SalC.Mosque, Color(0xFF5A4030), rf)
        val step = seg(p, 0.15f, 0.4f)
        foot76(Offset(w * 0.53f, mix(ground + h * 0.1f, ground - h * 0.05f, step)), h * 0.1f, SalC.Gold, rf, right = true)
        foot76(Offset(w * 0.45f, ground + h * 0.1f), h * 0.1f, SalC.Rim, rf * 0.5f, right = false)
        tagAt(a.tag("rightfootmosque"), Offset(w * 0.5f, h * 0.12f), rf * seg(p, 0.3f, 0.5f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ws > 0f) {
        val p = a.b("widestance")
        val foot = Offset(w * 0.5f, h * 0.86f)
        val tall = h * 0.3f
        val hip = foot - Offset(0f, tall * 0.45f)
        val neck = hip - Offset(0f, tall * 0.31f)
        val spread = tall * mix(0.06f, 0.3f, seg(p, 0.1f, 0.35f))
        posed14(hip, neck, neck + Offset(-tall * 0.05f, tall * 0.12f), neck + Offset(tall * 0.05f, tall * 0.12f), foot + Offset(-spread, 0f), foot + Offset(spread, 0f), tall, SalC.Person, ws)
        pray12(Offset(w * 0.2f, foot.y), tall, SalC.Person, ws * 0.7f)
        pray12(Offset(w * 0.8f, foot.y), tall, SalC.Person, ws * 0.7f)
        cross14(Offset(w * 0.5f, h * 0.38f), w * 0.04f, seg(p, 0.4f, 0.55f))
        tagAt(a.tag("widestance"), Offset(w * 0.5f, h * 0.15f), ws * seg(p, 0.45f, 0.65f), back = Fq.Back, ink = ArtInk.Ember)
    }
}

// ═════════════ 6. sal-takbir: standing and the opening takbir ═════════════

/**
 * Beats: facequibla (one stands turned wholly towards the House, a low screen before him: «إِلَى الْقِبْلَةِ بِجَمِيعِ بَدَنِهِ», «وَيُسَنُّ أَنْ يُصَلِّيَ
 * إِلَى سُتْرَةٍ»), raisehands (his hands rise to his shoulders, then to his ears: «رَافِعًا يَدَيْهِ إِلَى حَذْوِ مَنْكِبَيْهِ», «أَوْ إِلَى حِيَالِ
 * أُذُنَيْهِ»), handsonchest (the right hand over the left on the chest, and the opening supplication begins: «يَدَهُ الْيُمْنَى عَلَى الْيُسْرَى عَلَى
 * صَدْرِهِ», «دُعَاءَ الِاسْتِفْتَاحِ»), eastwest (a dawn on one side and a sunset on the other, the dark motes driven far apart: «كَمَا بَاعَدْتَ بَيْنَ
 * الْمَشْرِقِ وَالْمَغْرِبِ»), alternate (two pages that take turns: «بِهَذَا تَارَةً وَبِهَذَا تَارَةً»).
 */
internal fun DrawScope.openingtakbirScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "facequibla" to Shot(Offset(w * 0.5f, h * 0.6f), 1f),
        "raisehands" to Shot(Offset(w * 0.5f, h * 0.55f), 1.12f),
        "handsonchest" to Shot(Offset(w * 0.5f, h * 0.55f), 1.12f),
        "farapartsins" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
        "alternate" to Shot(Offset(w * 0.5f, h * 0.5f), 1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.08f) { openingTakbirDraw(a) }
}

private fun DrawScope.openingTakbirDraw(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val floor = h * 0.62f
    val fq = fqIn(a, "facequibla", "raisehands")
    val rh = fqIn(a, "raisehands", "handsonchest")
    val hc = fqIn(a, "handsonchest", "farapartsins")
    val ew = fqIn(a, "farapartsins", "alternate")
    val al = fqIn(a, "alternate")
    hall74(floor, t)
    val foot = Offset(w * 0.56f, floor + h * 0.3f)
    val tall = h * 0.5f
    if (fq > 0f) {
        val p = a.b("facequibla")
        takbir80(foot, tall, SalC.Person, fq, 0f)
        val arrow = seg(p, 0.1f, 0.35f)
        drawLine(SalC.Gold, foot + Offset(-w * 0.06f, -tall * 0.6f), foot + Offset(-w * 0.06f - w * 0.25f * arrow, -tall * 0.6f), 2.5f * dp, StrokeCap.Round, alpha = fq)
        kaaba29(foot + Offset(-w * 0.38f, -tall * 0.5f), w * 0.05f, fq * arrow)
        val st = seg(p, 0.5f, 0.7f)
        drawRect(Color(0xFF8A6A48), Offset(foot.x - w * 0.2f, foot.y - h * 0.1f * st), Size(w * 0.02f, h * 0.1f * st), alpha = fq)
        tagAt(a.tag("facequibla", 0, 0), Offset(w * 0.5f, h * 0.12f), fq * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("facequibla", 0, 1), Offset(w * 0.3f, h * 0.94f), fq * st, back = Fq.Back, ink = ArtInk.Ivory)
    }
    if (rh > 0f) {
        val p = a.b("raisehands")
        val r = easeOut(seg(p, 0.05f, 0.3f)) * 0.75f + 0.25f * easeOut(seg(p, 0.55f, 0.75f))
        takbir80(foot, tall, SalC.Person, rh, r)
        drawLine(SalC.Gold, foot + Offset(-tall * 0.2f, -tall * 0.15f), foot + Offset(-tall * 0.45f, -tall * 0.02f), 1.5f * dp, alpha = rh * 0.6f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)))
        tagAt(a.tag("raisehands", 0, 0), Offset(w * 0.25f, h * 0.2f), rh * seg(p, 0.15f, 0.35f) * (1f - seg(p, 0.5f, 0.6f)), back = Fq.Back, ink = ArtInk.Gold)
        tagAt(a.tag("raisehands", 0, 1), Offset(w * 0.25f, h * 0.2f), rh * seg(p, 0.6f, 0.8f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (hc > 0f) {
        val p = a.b("handsonchest")
        pray12(foot, tall, SalC.Person, hc)
        softGlow(foot + Offset(0f, -tall * 0.68f), w * 0.06f, SalC.Gold, hc * seg(p, 0.1f, 0.3f))
        tagAt(a.tag("handsonchest", 0, 0), Offset(w * 0.5f, h * 0.1f), hc * seg(p, 0.1f, 0.3f), back = Fq.Back, ink = ArtInk.Ivory)
        voice12(foot + Offset(-tall * 0.15f, -tall * 0.85f), w * 0.05f, t, SalC.Gold, hc * seg(p, 0.55f, 0.7f), -1f)
        tagAt(a.tag("handsonchest", 0, 1), Offset(w * 0.25f, h * 0.3f), hc * seg(p, 0.55f, 0.75f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (ew > 0f) {
        val p = a.b("farapartsins")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.85f * ew)
        val hz = h * 0.6f
        drawLine(SalC.Rim, Offset(0f, hz), Offset(w, hz), 1.5f * dp, alpha = ew)
        softGlow(Offset(w * 0.92f, hz), w * 0.12f, Color(0xFFF2D8B0), ew * 0.7f)
        softGlow(Offset(w * 0.08f, hz), w * 0.12f, Color(0xFFE8906A), ew * 0.7f)
        val apart = easeInOut(seg(p, 0.15f, 0.7f))
        for (k in 0 until 6) {
            val y = hz - h * (0.08f + 0.05f * (k % 3))
            val side = if (k % 2 == 0) 1f else -1f
            drawCircle(Color(0xFF6A5A7A), w * 0.015f, Offset(w * 0.5f + side * (w * 0.05f + w * 0.38f * apart), y), alpha = ew * (1f - 0.5f * apart))
        }
        softGlow(Offset(w * 0.5f, hz - h * 0.1f), w * 0.08f, SalC.Gold, ew * apart)
        tagAt(a.tag("farapartsins"), Offset(w * 0.5f, h * 0.2f), ew * seg(p, 0.4f, 0.6f), back = Fq.Back, ink = ArtInk.Gold)
    }
    if (al > 0f) {
        val p = a.b("alternate")
        drawRect(SalC.Top, Offset.Zero, size, alpha = 0.85f * al)
        val swap = fract(t * 0.35f)
        for (k in 0 until 2) {
            val front = if (k == 0) swap < 0.5f else swap >= 0.5f
            val c = Offset(w * (0.42f + 0.16f * k), h * (0.5f + (if (front) 0f else -0.04f)))
            scroll14(c, w * 0.28f, h * 0.36f, 1f, al * (if (front) 1f else 0.45f))
        }
        tagAt(a.tag("alternate"), Offset(w * 0.5f, h * 0.88f), al * seg(p, 0.2f, 0.4f), back = Fq.Back, ink = ArtInk.Gold)
    }
}
