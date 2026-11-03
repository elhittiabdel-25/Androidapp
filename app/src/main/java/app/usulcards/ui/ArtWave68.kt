package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.sin

/*
 * Wave 68 (2026-11-03): «حصن المسلم», answering the adhan. The call goes out from the minaret and an answer comes back; three
 * words of contentment; the two «حي على» answered by «لا حول ولا قوة إلا بالله»; the high station of al-Wasila; a prayer between the
 * adhan and the iqama that is not turned back. The Prophet ﷺ is only light; there are no figures.
 */

private val Back68 = Color(0xE6181420)

private object AdhanC68 {
    val Night = Color(0xFF101830)
    val Dawn = Color(0xFF6A6A9A)
    val DawnLow = Color(0xFFE8B890)
    val Ground = Color(0xFF1C1E2C)
    val Gold = Color(0xFFF2C860)
    val Call = Color(0xFF8AC4EA)
    val Answer = Color(0xFFF2C860)
    val Mosque = Color(0xFFD8CCB4)
    val Dome = Color(0xFF4E7A6A)
    val Tier = Color(0xFF3E7A54)
    val Hands = Color(0xFFDCD4C4)
}

/**
 * Beats: answercall (waves go out from a minaret and a roundel sends the same back; at the two calls to come, the answer changes:
 * «لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ»), radeetu (three lights rise one after another: «رَضِيتُ بِاللَّهِ رَبًّا», «وَبِمُحَمَّدٍ
 * رَسُولًا», «وَبِالْإِسْلَامِ دِينًا»), twohayya (two calls come, «حَيَّ عَلَى الصَّلَاةِ», «حَيَّ عَلَى الْفَلَاحِ», and each is met by a
 * humble low glow), highstation (garden terraces rising, and one station at the very top where the light ﷺ shines: «الْوَسِيلَةَ»),
 * betweencalls (between two calls, raised hands send a light up that is not turned back: «وَالدُّعَاءُ بَيْنَ الْأَذَانِ وَالْإِقَامَةِ لَا
 * يُرَدُّ»).
 */
internal fun DrawScope.echoadhanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "answercall" to Shot(Offset(w * 0.5f, h * 0.55f), 1.0f),
        "radeetu" to Shot(Offset(w * 0.5f, h * 0.5f), 1.0f),
        "twohayya" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
        "highstation" to Shot(Offset(w * 0.5f, h * 0.45f), 1.0f),
        "betweencalls" to Shot(Offset(w * 0.5f, h * 0.55f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { adhanDawn(a) }
}

private fun DrawScope.adhanDawn(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val ac = a.b("answercall")
    val rd = a.b("radeetu")
    val th = a.b("twohayya")
    val hs = a.b("highstation")
    val bc = a.b("betweencalls")
    val ground = h * 0.84f
    nightSky(t, AdhanC68.Night, lerpColor(AdhanC68.Dawn, AdhanC68.DawnLow, 0.3f), ground, starAlpha = 0.7f)
    drawRect(AdhanC68.Ground, Offset(0f, ground), Size(w, h - ground))
    val minaret = Offset(w * 0.78f, ground)
    val top = minaret + Offset(0f, -h * 0.5f)
    val waves = { from: Offset, color: Color, alpha: Float, dir: Float ->
        for (k in 0 until 3) {
            val u = fract(t * 0.5f + k / 3f)
            val r = w * (0.05f + 0.35f * u)
            drawArc(color, if (dir < 0f) 150f else -30f, 60f, false, Offset(from.x - r, from.y - r), Size(r * 2f, r * 2f), alpha = alpha * (1f - u), style = Stroke(3f * dp, cap = StrokeCap.Round))
        }
    }
    if (ac > 0f) {
        val f = easeOut(seg(ac, 0f, 0.25f)) * (1f - seg(rd, 0f, 0.4f))
        mosque12(minaret + Offset(-w * 0.12f, 0f), w * 0.22f, AdhanC68.Mosque, AdhanC68.Dome, f, 0.6f)
        drawRect(AdhanC68.Mosque, Offset(minaret.x - w * 0.025f, top.y), Size(w * 0.05f, minaret.y - top.y), alpha = f)
        waves(top, AdhanC68.Call, f, -1f)
        val me = Offset(w * 0.22f, h * 0.55f)
        nameRoundel(me, w * 0.05f, null, Roundel.PlainFill, Roundel.PlainRim, f)
        val change = seg(ac, 0.5f, 0.6f)
        waves(me, lerpColor(AdhanC68.Call, AdhanC68.Answer, change), f * seg(ac, 0.1f, 0.25f), 1f)
        tagAt(a.tag("answercall"), Offset(w * 0.4f, h * 0.22f), f * seg(ac, 0.5f, 0.7f), back = Back68, ink = ArtInk.Gold)
    }
    if (rd > 0f) {
        val f = easeOut(seg(rd, 0f, 0.25f)) * (1f - seg(th, 0f, 0.4f))
        for (k in 0 until 3) {
            val g = seg(rd, 0.08f + 0.27f * k, 0.25f + 0.27f * k)
            val c = Offset(w * (0.78f - 0.28f * k), mix(ground, h * 0.4f, easeOut(g)))
            softGlow(c, w * 0.05f, AdhanC68.Gold, f * g)
            tagAt(a.tag("radeetu", 0, k), c + Offset(0f, h * 0.1f), f * g, back = Back68, ink = ArtInk.Gold)
        }
    }
    if (th > 0f) {
        val f = easeOut(seg(th, 0f, 0.25f)) * (1f - seg(hs, 0f, 0.4f))
        drawRect(AdhanC68.Mosque, Offset(minaret.x - w * 0.025f, top.y), Size(w * 0.05f, minaret.y - top.y), alpha = f)
        waves(top, AdhanC68.Call, f, -1f)
        for (k in 0 until 2) {
            val g = seg(th, 0.08f + 0.4f * k, 0.25f + 0.4f * k)
            val y = h * (0.3f + 0.25f * k)
            tagAt(a.tag("twohayya", 0, k), Offset(w * 0.45f, y), f * g, back = Back68, ink = AdhanC68.Call)
            softGlow(Offset(w * 0.18f, y + h * 0.08f), w * 0.04f, AdhanC68.Gold, 0.7f * f * seg(th, 0.2f + 0.4f * k, 0.35f + 0.4f * k))
        }
    }
    if (hs > 0f) {
        val f = easeOut(seg(hs, 0f, 0.25f)) * (1f - seg(bc, 0f, 0.4f))
        for (k in 0 until 6) {
            val g = seg(hs, 0.05f + 0.07f * k, 0.2f + 0.07f * k)
            val y = ground - h * 0.1f * k
            val half = w * (0.42f - 0.065f * k)
            drawPath(Path().apply { moveTo(w * 0.5f - half, y); lineTo(w * 0.5f + half, y); lineTo(w * 0.5f + half * 0.92f, y - h * 0.1f); lineTo(w * 0.5f - half * 0.92f, y - h * 0.1f); close() }, AdhanC68.Tier, alpha = f * g * (0.4f + 0.1f * k))
        }
        val peak = Offset(w * 0.5f, ground - h * 0.64f)
        sirajMunir(peak, w * 0.012f, f * seg(hs, 0.5f, 0.7f), t, nameAt = peak + Offset(0f, -h * 0.1f))
        tagAt(a.tag("highstation"), Offset(w * 0.5f, ground + h * 0.07f), f * seg(hs, 0.55f, 0.75f), back = Back68, ink = ArtInk.Gold)
    }
    if (bc > 0f) {
        val f = easeOut(seg(bc, 0f, 0.25f))
        for (k in 0 until 2) {
            val x = w * (0.85f - 0.7f * k)
            drawRect(AdhanC68.Mosque, Offset(x - w * 0.02f, ground - h * 0.4f), Size(w * 0.04f, h * 0.4f), alpha = f * 0.7f)
            softGlow(Offset(x, ground - h * 0.42f), w * 0.04f, AdhanC68.Call, f * (if (k == 0) seg(bc, 0.05f, 0.15f) * (1f - seg(bc, 0.25f, 0.35f)) else seg(bc, 0.85f, 0.95f)))
        }
        val hands = Offset(w * 0.5f, ground - h * 0.05f)
        hands12(hands, h * 0.14f, AdhanC68.Hands, f)
        val up = seg(bc, 0.3f, 0.75f)
        softGlow(Offset(w * 0.5f, mix(hands.y - h * 0.1f, h * 0.1f, up)), w * 0.03f, AdhanC68.Gold, f * sin(PI.toFloat() * up).coerceAtLeast(0f) + f * seg(bc, 0.7f, 0.8f) * 0.5f)
        tagAt(a.tag("betweencalls"), Offset(w * 0.5f, h * 0.18f), f * seg(bc, 0.4f, 0.6f), back = Back68, ink = ArtInk.Gold)
    }
}
