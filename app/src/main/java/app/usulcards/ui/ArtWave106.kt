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
import kotlin.math.sin

/*
 * Wave 106 (2026-11-17): the sixteen tajwid cards of batch 20 (ta-*, jz-*) redrawn in the illustrated style (boards in
 * wave 94). The world is the lamp-lit study of wave 104. Sounds are pictured: the mouth and throat in profile with the
 * place of a letter lit, two letters sliding into one (idgham), a letter veiled in mist (ikhfa'), the nun turning into
 * a mim (iqlab), a held sound as a wave whose length counts beats, a stop on the road for waqf.
 */

private val Ink106 = Color(0xFFE8DCC0)

/** A word or letter in large calligraphy (falls back to the small tag). */
private fun DrawScope.word106(a: ArtCtx, kind: String, part: Int, c: Offset, alpha: Float, ink: Color = ArtInk.Ivory, back: Color = Color(0xB3101426)) =
    tagAt(a.big(kind, 0, part) ?: a.tag(kind, 0, part), c, alpha, back = back, ink = ink)

/** The mouth and throat in profile, inside [box] (left, top, width, height); each zone 0..1 lit. */
private fun DrawScope.tract106(box: Offset, bw: Float, bh: Float, alpha: Float, jawf: Float = 0f, halq: Float = 0f, lisan: Float = 0f, shafa: Float = 0f, khay: Float = 0f) {
    if (alpha <= 0.01f) return
    fun at(x: Float, y: Float) = Offset(box.x + bw * x, box.y + bh * y)
    val sw = 2.4f * density
    drawRoundRect(Color(0xFF2A2436), box, Size(bw, bh), CornerRadius(14f * density), alpha = 0.6f * alpha)
    val lip = at(0.12f, 0.45f)
    val roof = Path().apply { moveTo(lip.x, lip.y - bh * 0.05f); cubicTo(at(0.3f, 0.2f).x, at(0.3f, 0.2f).y, at(0.65f, 0.2f).x, at(0.65f, 0.2f).y, at(0.82f, 0.38f).x, at(0.82f, 0.38f).y); lineTo(at(0.82f, 0.95f).x, at(0.82f, 0.95f).y) }
    val floor = Path().apply { moveTo(lip.x, lip.y + bh * 0.05f); cubicTo(at(0.25f, 0.7f).x, at(0.25f, 0.7f).y, at(0.55f, 0.72f).x, at(0.55f, 0.72f).y, at(0.68f, 0.64f).x, at(0.68f, 0.64f).y); lineTo(at(0.68f, 0.95f).x, at(0.68f, 0.95f).y) }
    val nose = Path().apply { moveTo(at(0.06f, 0.25f).x, at(0.06f, 0.25f).y); cubicTo(at(0.3f, 0.02f).x, at(0.3f, 0.02f).y, at(0.62f, 0.05f).x, at(0.62f, 0.05f).y, at(0.8f, 0.28f).x, at(0.8f, 0.28f).y) }
    if (jawf > 0f) softGlow(at(0.45f, 0.45f), bw * 0.35f, Qn.GoldLight, 0.7f * jawf * alpha)
    if (halq > 0f) { softGlow(at(0.75f, 0.72f), bw * 0.2f, Qn.GoldLight, 0.7f * halq * alpha); for (k in 0 until 3) drawCircle(Qn.Gold, 3.5f * density, at(0.75f, 0.58f + 0.13f * k), alpha = alpha * seg(halq, 0.2f * k, 0.4f + 0.2f * k)) }
    if (khay > 0f) softGlow(at(0.42f, 0.14f), bw * 0.22f, Qn.GoldLight, 0.7f * khay * alpha)
    drawPath(nose, if (khay > 0f) Qn.Gold else Ink106, alpha = alpha * (0.5f + 0.5f * khay), style = Stroke(sw, cap = StrokeCap.Round))
    drawPath(roof, Ink106, alpha = alpha, style = Stroke(sw, cap = StrokeCap.Round))
    drawPath(floor, Ink106, alpha = alpha, style = Stroke(sw, cap = StrokeCap.Round))
    val tongue = Path().apply { moveTo(at(0.2f, 0.6f).x, at(0.2f, 0.6f).y); cubicTo(at(0.3f, 0.45f).x, at(0.3f, 0.45f).y, at(0.55f, 0.4f).x, at(0.55f, 0.4f).y, at(0.66f, 0.55f).x, at(0.66f, 0.55f).y); cubicTo(at(0.55f, 0.66f).x, at(0.55f, 0.66f).y, at(0.32f, 0.68f).x, at(0.32f, 0.68f).y, at(0.2f, 0.6f).x, at(0.2f, 0.6f).y); close() }
    drawPath(tongue, if (lisan > 0f) lerpColor(Qn.Rose, Qn.Gold, lisan) else Qn.Rose, alpha = alpha * (0.55f + 0.45f * lisan))
    for (k in listOf(-1f, 1f)) drawLine(if (shafa > 0f) Qn.Gold else Ink106, lip + Offset(-bw * 0.06f, k * bh * 0.05f), lip + Offset(bw * 0.02f, k * bh * 0.05f), sw * (1.6f + shafa), StrokeCap.Round, alpha = alpha)
    if (shafa > 0f) softGlow(lip, bw * 0.14f, Qn.GoldLight, 0.6f * shafa * alpha)
}

/** A held sound: a wave from [from] that runs [counts] beats long, the beats lit as it goes ([u] 0..1). */
private fun DrawScope.wave106(from: Offset, counts: Int, u: Float, alpha: Float, t: Float) {
    val w = size.width
    val step = w * 0.11f
    val len = step * counts * u
    val path = Path().apply {
        moveTo(from.x, from.y)
        var x = 0f
        while (x < len) { x += w * 0.01f; lineTo(from.x - x, from.y + sin(x / (w * 0.03f) + t * 4f) * size.height * 0.025f) }
    }
    drawPath(path, Qn.GoldLight, alpha = alpha, style = Stroke(2.2f * density, cap = StrokeCap.Round))
    for (k in 0 until counts) drawCircle(Qn.Gold, w * 0.014f, Offset(from.x - step * (k + 0.5f), from.y + size.height * 0.07f), alpha = alpha * if (step * (k + 0.5f) <= len) 1f else 0.25f)
}

/** Two words that slide together and merge into the second ([u] 0..1). */
private fun DrawScope.merge106(a: ArtCtx, kind: String, pa: Int, pb: Int, y: Float, u: Float, alpha: Float) {
    val w = size.width
    val m = easeInOut(u)
    word106(a, kind, pa, Offset(mix(w * 0.7f, w * 0.5f, m), y), alpha * (1f - 0.85f * seg(u, 0.6f, 1f)))
    word106(a, kind, pb, Offset(mix(w * 0.3f, w * 0.5f, m), y), alpha, ink = ArtInk.Gold)
}

private fun DrawScope.room106(a: ArtCtx) { study104(a, 0.6f); dim104(1f, 0.35f) }

// ═════════════ ta-nun ═════════════
internal fun DrawScope.tanunScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tanun", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            word106(a, "tanun0", 0, Offset(w * 0.75f, h * 0.24f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            for (k in 0 until 4) {
                val c = Offset(w * (0.8f - 0.2f * k), h * 0.5f)
                when (k) {
                    0 -> softGlow(c, w * 0.06f, Qn.GoldLight, 0.6f * f * seg(p, 0.05f, 0.2f))
                    1 -> drawCircle(Qn.Jade, w * 0.04f, c, alpha = 0.6f * f * seg(p, 0.3f, 0.45f))
                    2 -> drawLine(Qn.Rose, c + Offset(-w * 0.04f, 0f), c + Offset(w * 0.04f, 0f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.5f, 0.65f))
                    else -> drawCircle(Color(0xFFB8C0E0), w * 0.05f, c, alpha = 0.35f * f * seg(p, 0.7f, 0.85f))
                }
                if (k > 0) word106(a, "tanun0", k, c + Offset(0f, h * 0.14f), f * seg(p, 0.1f + 0.2f * k, 0.25f + 0.2f * k))
            }
        },
        { p, f -> tract106(Offset(w * 0.15f, h * 0.12f), w * 0.7f, h * 0.7f, f, halq = easeOut(seg(p, 0.2f, 0.6f))); word106(a, "tanun1", 0, Offset(w * 0.78f, h * 0.88f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold) },
        { p, f ->
            tract106(Offset(w * 0.08f, h * 0.15f), w * 0.5f, h * 0.65f, f, halq = 1f)
            for (k in 0 until 3) {
                val c = Offset(w * 0.8f, h * (0.3f + 0.2f * k))
                drawLine(Qn.GoldLight, Offset(w * 0.47f, h * (0.5f + 0.08f * (k - 1))), c + Offset(-w * 0.12f, 0f), 1.2f * density, alpha = 0.5f * f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k))
                word106(a, "tanun2", 2 - k, c, f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k))
            }
        },
        { p, f ->
            val clear = seg(p, 0.2f, 0.5f)
            softGlow(Offset(w * 0.5f, h * 0.42f), w * 0.15f, Qn.GoldLight, 0.5f * f * clear)
            word106(a, "tanun3", 0, Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            word106(a, "tanun3", 1, Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.45f, 0.6f))
        },
    )
}

// ═════════════ ta-idgham ═════════════
internal fun DrawScope.taidghamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "taidgham", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            word106(a, "taidgham0", 1, Offset(w * 0.5f, h * 0.16f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            word106(a, "taidgham0", 0, Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.15f, 0.3f))
            for (k in 0 until 6) drawCircle(Qn.GoldLight, w * 0.012f, Offset(w * (0.3f + 0.08f * k), h * 0.56f), alpha = f * seg(p, 0.35f + 0.06f * k, 0.45f + 0.06f * k))
        },
        { p, f ->
            merge106(a, "taidgham1", 0, 0, h * 0.4f, seg(p, 0.1f, 0.6f), f)
            tract106(Offset(w * 0.3f, h * 0.55f), w * 0.4f, h * 0.35f, f * seg(p, 0.3f, 0.45f), khay = 0.5f + 0.5f * sin(a.t * 3f))
            word106(a, "taidgham1", 1, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            word106(a, "taidgham2", 0, Offset(w * 0.32f, h * 0.36f), f * seg(p, 0.05f, 0.2f))
            word106(a, "taidgham2", 1, Offset(w * 0.68f, h * 0.36f), f * seg(p, 0.2f, 0.35f))
            tract106(Offset(w * 0.3f, h * 0.5f), w * 0.4f, h * 0.35f, f * seg(p, 0.3f, 0.45f), lisan = 0.8f)
            drawLine(Qn.Ember, Offset(w * 0.4f, h * 0.55f), Offset(w * 0.52f, h * 0.65f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.5f, 0.65f))
            word106(a, "taidgham2", 2, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 2) {
                val c = Offset(w * (0.3f + 0.4f * k), h * 0.45f)
                word106(a, "taidgham3", k, c, f * seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k))
                drawCircle(Qn.Jade, w * 0.12f, c, alpha = 0.6f * f * seg(p, 0.45f, 0.6f), style = Stroke(2f * density))
            }
            word106(a, "taidgham3", 2, Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ta-iqlab ═════════════
internal fun DrawScope.taiqlabScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "taiqlab", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            word106(a, "taiqlab0", 3, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            val turn = easeInOut(seg(p, 0.35f, 0.65f))
            word106(a, "taiqlab0", 0, Offset(w * 0.68f, h * 0.42f), f * (1f - turn))
            word106(a, "taiqlab0", 1, Offset(w * 0.68f, h * 0.42f), f * turn, ink = ArtInk.Gold)
            word106(a, "taiqlab0", 2, Offset(w * 0.3f, h * 0.42f), f * seg(p, 0.15f, 0.3f))
            tract106(Offset(w * 0.3f, h * 0.56f), w * 0.4f, h * 0.35f, f * seg(p, 0.5f, 0.65f), shafa = turn, khay = turn)
        },
        { p, f ->
            word106(a, "taiqlab1", 0, Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            for (k in 0 until 14) {
                val x = ((a.t * 0.08f + k * 0.08f) % 1.2f) * w - w * 0.1f
                drawCircle(Color(0xFFD8DCE8), w * (0.05f + 0.01f * (k % 3)), Offset(x, h * (0.36f + 0.03f * (k % 4))), alpha = 0.18f * f * seg(p, 0.25f, 0.5f))
            }
        },
        { p, f ->
            for (k in 0 until 15) drawCircle(Qn.GoldLight, w * 0.016f, Offset(w * (0.15f + 0.07f * (k % 10)), h * (0.42f + 0.1f * (k / 10))), alpha = f * seg(p, 0.05f + 0.04f * k, 0.12f + 0.04f * k))
            word106(a, "taiqlab2", 0, Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) word106(a, "taiqlab3", k, Offset(w * 0.5f, h * (0.26f + 0.2f * k)), f * seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k), ink = if (k == 1) ArtInk.Gold else ArtInk.Ivory)
        },
    )
}

// ═════════════ ta-mim ═════════════
internal fun DrawScope.tamimScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tamim", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f -> tract106(Offset(w * 0.15f, h * 0.12f), w * 0.7f, h * 0.7f, f, khay = easeOut(seg(p, 0.15f, 0.5f)) * (0.75f + 0.25f * sin(a.t * 3f))); word106(a, "tamim0", 0, Offset(w * 0.5f, h * 0.9f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold) },
        { p, f ->
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.45f)
                when (k) {
                    0 -> drawCircle(Color(0xFFB8C0E0), w * 0.06f, c, alpha = 0.35f * f * seg(p, 0.05f, 0.2f))
                    1 -> drawCircle(Qn.Jade, w * 0.05f, c, alpha = 0.6f * f * seg(p, 0.3f, 0.45f))
                    else -> softGlow(c, w * 0.07f, Qn.GoldLight, 0.6f * f * seg(p, 0.55f, 0.7f))
                }
                word106(a, "tamim1", k, c + Offset(0f, h * 0.16f), f * seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k))
            }
        },
        { p, f -> tract106(Offset(w * 0.15f, h * 0.12f), w * 0.7f, h * 0.7f, f, shafa = easeOut(seg(p, 0.15f, 0.5f)), khay = 0.5f * seg(p, 0.4f, 0.7f)); word106(a, "tamim2", 0, Offset(w * 0.2f, h * 0.88f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold) },
        { p, f ->
            merge106(a, "tamim3", 0, 1, h * 0.42f, seg(p, 0.1f, 0.6f), f)
            word106(a, "tamim3", 2, Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ ta-lam ═════════════
internal fun DrawScope.talamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "talam", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { nightSky(a.t, S97.Night, S97.NightLow, h * 0.9f, 1f) },
        { p, f ->
            word106(a, "talam0", 2, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            qIcon("crescent", Offset(w * 0.72f, h * 0.45f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.15f, 0.3f), a.t)
            qIcon("sun", Offset(w * 0.28f, h * 0.45f), w * 0.09f, Color(0xFFFFC870), f * seg(p, 0.45f, 0.6f), a.t)
            word106(a, "talam0", 0, Offset(w * 0.72f, h * 0.64f), f * seg(p, 0.2f, 0.35f))
            word106(a, "talam0", 1, Offset(w * 0.28f, h * 0.64f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.38f)
            drawCircle(Color(0xFFF2EEE0), w * 0.12f, c, alpha = f * seg(p, 0.05f, 0.3f))
            drawCircle(Color(0xFF0E1530), w * 0.11f, c + Offset(w * 0.05f, -w * 0.02f), alpha = f * seg(p, 0.05f, 0.3f))
            word106(a, "talam1", 0, Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            val c = Offset(w * 0.5f, h * 0.38f)
            drawRect(Color(0xFF6A8AC0), alpha = 0.6f * f * seg(p, 0.05f, 0.3f))
            drawCircle(Color(0xFFFFE08A), w * 0.11f, c, alpha = f * seg(p, 0.1f, 0.3f))
            softGlow(c, w * 0.3f, Color(0xFFFFE08A), 0.6f * f * seg(p, 0.2f, 0.5f))
            word106(a, "talam2", 0, Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.4f)
            word106(a, "talam3", 3, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.48f)
                softGlow(c, w * 0.08f, Qn.GoldLight, 0.4f * f * seg(p, 0.15f + 0.25f * k, 0.3f + 0.25f * k))
                word106(a, "talam3", k, c, f * seg(p, 0.15f + 0.25f * k, 0.3f + 0.25f * k))
            }
        },
    )
}

// ═════════════ ta-mithlan ═════════════
private fun DrawScope.pair106(a: ArtCtx, kind: String, p: Float, f: Float, sameMakhraj: Boolean, sameSifa: Boolean) {
    val w = size.width
    val h = size.height
    word106(a, kind, 2, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
    val gap = if (sameMakhraj) 0.04f else 0.16f
    for (k in 0 until 2) {
        val c = Offset(w * (0.5f + (k - 0.5f) * 2f * gap), h * 0.4f)
        drawCircle(if (k == 0) Qn.GoldLight else if (sameSifa) Qn.GoldLight else Qn.Rose, w * 0.05f, c, alpha = 0.8f * f * seg(p, 0.1f, 0.3f))
    }
    for ((k, ok) in listOf(sameMakhraj, sameSifa).withIndex()) {
        val c = Offset(w * (0.3f + 0.4f * k), h * 0.66f)
        word106(a, kind, k, c, f * seg(p, 0.3f + 0.2f * k, 0.45f + 0.2f * k))
        if (ok) qIcon("check", c + Offset(0f, h * 0.12f), w * 0.05f, Qn.Jade, f * seg(p, 0.4f + 0.2f * k, 0.55f + 0.2f * k), a.t)
        else qIcon("cross", c + Offset(0f, h * 0.12f), w * 0.05f, Qn.Ember, f * seg(p, 0.4f + 0.2f * k, 0.55f + 0.2f * k), a.t)
    }
}

internal fun DrawScope.tamithlanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tamithlan", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f -> pair106(a, "tamithlan0", p, f, true, true) },
        { p, f -> pair106(a, "tamithlan1", p, f, false, false) },
        { p, f -> pair106(a, "tamithlan2", p, f, true, false) },
        { p, f ->
            for (k in 0 until 2) {
                val c = Offset(w * (0.3f + 0.4f * k), h * 0.45f)
                val big = k == 1
                drawCircle(Qn.GoldLight, w * (if (big) 0.1f else 0.05f), c, alpha = 0.5f * f * seg(p, 0.1f + 0.3f * k, 0.3f + 0.3f * k), style = Stroke(2f * density))
                word106(a, "tamithlan3", k, c, f * seg(p, 0.1f + 0.3f * k, 0.3f + 0.3f * k))
            }
        },
    )
}

// ═════════════ ta-madd ═════════════
internal fun DrawScope.tamaddScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tamadd", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            word106(a, "tamadd0", 2, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            qIcon("tree", Offset(w * 0.5f, h * 0.5f), w * 0.12f, Qn.Jade, f * seg(p, 0.1f, 0.3f), a.t)
            word106(a, "tamadd0", 0, Offset(w * 0.5f, h * 0.72f), f * seg(p, 0.2f, 0.35f))
            word106(a, "tamadd0", 1, Offset(w * 0.78f, h * 0.38f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f -> wave106(Offset(w * 0.75f, h * 0.45f), 2, seg(p, 0.15f, 0.7f), f, a.t); word106(a, "tamadd1", 0, Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold) },
        { p, f ->
            word106(a, "tamadd2", 2, Offset(w * 0.5f, h * 0.1f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            wave106(Offset(w * 0.9f, h * 0.36f), 4, seg(p, 0.1f, 0.45f), f, a.t)
            word106(a, "tamadd2", 0, Offset(w * 0.9f, h * 0.24f), f * seg(p, 0.1f, 0.2f))
            wave106(Offset(w * 0.9f, h * 0.66f), 6, seg(p, 0.45f, 0.95f), f, a.t)
            word106(a, "tamadd2", 1, Offset(w * 0.9f, h * 0.54f), f * seg(p, 0.45f, 0.55f))
        },
        { p, f ->
            word106(a, "tamadd3", 0, Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            word106(a, "tamadd3", 1, Offset(w * 0.5f, h * 0.55f), f * seg(p, 0.4f, 0.55f))
            for (k in 0 until 3) drawCircle(Qn.GoldLight, w * 0.01f, Offset(w * (0.4f + 0.1f * k), h * 0.66f), alpha = f * seg(p, 0.55f + 0.08f * k, 0.65f + 0.08f * k))
        },
    )
}

// ═════════════ ta-madd-ahkam ═════════════
internal fun DrawScope.tamaddahkamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "tamaddahkam", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            val icons = listOf("lock", "scale", "key")
            for (k in 0 until 3) {
                val c = Offset(w * (0.78f - 0.28f * k), h * 0.42f)
                qIcon(icons[k], c, w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k), a.t)
                word106(a, "tamaddahkam0", k, c + Offset(0f, h * 0.15f), f * seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k))
            }
        },
        { p, f -> wave106(Offset(w * 0.8f, h * 0.45f), 4, seg(p, 0.15f, 0.8f), f, a.t); word106(a, "tamaddahkam1", 0, Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold) },
        { p, f -> wave106(Offset(w * 0.8f, h * 0.45f), 4, seg(p, 0.15f, 0.8f), f, a.t); drawLine(Color(0xFF8A8494), Offset(w * 0.6f, h * 0.35f), Offset(w * 0.6f, h * 0.6f), 2f * density, alpha = f); word106(a, "tamaddahkam2", 0, Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold) },
        { p, f -> wave106(Offset(w * 0.86f, h * 0.45f), 6, seg(p, 0.1f, 0.9f), f, a.t); word106(a, "tamaddahkam3", 0, Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold) },
    )
}

// ═════════════ jz-wajib ═════════════
internal fun DrawScope.jzwajibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jzwajib", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { study104(a) },
        { p, f ->
            plain97(Offset(w * 0.24f, h * 0.24f), w * 0.07f, a, "jzwajib0", 2, f * seg(p, 0.05f, 0.25f))
            qIcon("scroll", Offset(w * 0.5f, h * 0.38f), w * 0.1f, T104.Page, f * seg(p, 0.25f, 0.4f), a.t)
            word106(a, "jzwajib0", 0, Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            word106(a, "jzwajib0", 1, Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.55f, 0.7f))
        },
        { p, f -> dim104(f, 0.6f); tract106(Offset(w * 0.15f, h * 0.12f), w * 0.7f, h * 0.65f, f, halq = seg(p, 0.1f, 0.3f), lisan = seg(p, 0.3f, 0.5f), shafa = seg(p, 0.5f, 0.7f), khay = seg(p, 0.6f, 0.8f)); word106(a, "jzwajib1", 0, Offset(w * 0.5f, h * 0.88f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold) },
        { p, f ->
            dim104(f, 0.4f)
            qIcon("lips", Offset(w * 0.5f, h * 0.36f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            for (k in 0 until 4) drawArc(Qn.GoldLight, -50f, 100f, false, Offset(w * 0.5f - w * 0.05f * (k + 1), h * 0.36f - h * 0.05f * (k + 1)), Size(w * 0.1f * (k + 1), h * 0.1f * (k + 1)), alpha = 0.45f * f * seg(p, 0.2f + 0.08f * k, 0.35f + 0.08f * k), style = Stroke(1.4f * density))
            word106(a, "jzwajib2", 0, Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.5f)
            qIcon("pen", Offset(w * 0.25f, h * 0.36f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            word106(a, "jzwajib3", 0, Offset(w * 0.25f, h * 0.52f), f * seg(p, 0.1f, 0.25f))
            val join = easeInOut(seg(p, 0.35f, 0.7f))
            drawRoundRect(T104.Page, Offset(w * (0.5f - 0.02f * join), h * 0.32f), Size(w * 0.12f, h * 0.08f), CornerRadius(3f * density), alpha = f)
            drawRoundRect(T104.Page, Offset(w * (0.68f - 0.04f * join), h * 0.32f), Size(w * 0.12f, h * 0.08f), CornerRadius(3f * density), alpha = f)
            word106(a, "jzwajib3", 1, Offset(w * 0.65f, h * 0.56f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ jz-makharij ═════════════
internal fun DrawScope.jzmakharijScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val box = Offset(w * 0.15f, h * 0.1f)
    film98(a, "jzmakharij", List(5) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a); tract106(box, w * 0.7f, h * 0.68f, 0.9f) },
        { p, f -> tract106(box, w * 0.7f, h * 0.68f, f, jawf = easeOut(seg(p, 0.1f, 0.5f))); word106(a, "jzmakharij0", 0, Offset(w * 0.5f, h * 0.88f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold) },
        { p, f -> tract106(box, w * 0.7f, h * 0.68f, f, halq = easeOut(seg(p, 0.1f, 0.5f))); word106(a, "jzmakharij1", 0, Offset(w * 0.75f, h * 0.88f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold) },
        { p, f -> tract106(box, w * 0.7f, h * 0.68f, f, lisan = easeOut(seg(p, 0.1f, 0.5f))); word106(a, "jzmakharij2", 0, Offset(w * 0.45f, h * 0.88f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold) },
        { p, f -> tract106(box, w * 0.7f, h * 0.68f, f, shafa = easeOut(seg(p, 0.1f, 0.5f))); word106(a, "jzmakharij3", 0, Offset(w * 0.2f, h * 0.88f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold) },
        { p, f -> tract106(box, w * 0.7f, h * 0.68f, f, khay = easeOut(seg(p, 0.1f, 0.5f))); word106(a, "jzmakharij4", 0, Offset(w * 0.45f, h * 0.06f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold) },
    )
}

// ═════════════ jz-sifat ═════════════
internal fun DrawScope.jzsifatScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jzsifat", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            for (k in 0 until 5) drawArc(Qn.GoldLight, -50f, 100f, false, Offset(w * 0.72f - w * 0.05f * (k + 1), h * 0.42f - h * 0.05f * (k + 1)), Size(w * 0.1f * (k + 1), h * 0.1f * (k + 1)), alpha = 0.6f * f * seg(p, 0.05f + 0.05f * k, 0.2f + 0.05f * k), style = Stroke(2.2f * density))
            for (k in 0 until 6) drawCircle(Color(0xFFB8C0E0), w * 0.006f, Offset(w * 0.28f - k * w * 0.02f, h * 0.42f + sin(a.t * 2f + k) * h * 0.02f), alpha = 0.6f * f * seg(p, 0.4f, 0.6f))
            word106(a, "jzsifat0", 0, Offset(w * 0.72f, h * 0.64f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            word106(a, "jzsifat0", 1, Offset(w * 0.28f, h * 0.64f), f * seg(p, 0.45f, 0.6f))
        },
        { p, f ->
            val up = easeInOut(seg(p, 0.4f, 0.7f))
            tract106(Offset(w * 0.25f, h * 0.18f), w * 0.5f, h * 0.5f, f, lisan = up)
            drawLine(Qn.GoldLight, Offset(w * 0.5f, h * 0.5f), Offset(w * 0.5f, h * (0.5f - 0.12f * up)), 2.5f * density, StrokeCap.Round, alpha = f * up)
            word106(a, "jzsifat1", 0, Offset(w * 0.3f, h * 0.82f), f * seg(p, 0.05f, 0.2f))
            word106(a, "jzsifat1", 1, Offset(w * 0.7f, h * 0.82f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            word106(a, "jzsifat2", 1, Offset(w * 0.5f, h * 0.16f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            val c = Offset(w * 0.5f, h * 0.48f)
            drawRoundRect(Qn.Gold, Offset(c.x - w * 0.22f, c.y - h * 0.09f), Size(w * 0.44f, h * 0.18f), CornerRadius(10f * density), alpha = 0.3f * f * seg(p, 0.2f, 0.4f))
            word106(a, "jzsifat2", 0, c, f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            drawLine(Qn.GoldLight, Offset(c.x, c.y - h * 0.11f), Offset(c.x, c.y - h * 0.2f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.45f, 0.6f))
        },
        { p, f ->
            word106(a, "jzsifat3", 1, Offset(w * 0.5f, h * 0.16f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            val bounce = kotlin.math.abs(sin(a.t * 5f)) * seg(p, 0.35f, 0.5f)
            word106(a, "jzsifat3", 0, Offset(w * 0.5f, h * 0.5f - h * 0.05f * bounce), f * seg(p, 0.15f, 0.3f))
            for (k in 0 until 3) drawArc(Qn.GoldLight, 20f, 140f, false, Offset(w * 0.5f - w * 0.06f * (k + 1), h * 0.58f), Size(w * 0.12f * (k + 1), h * 0.06f * (k + 1)), alpha = 0.5f * f * bounce, style = Stroke(1.4f * density))
        },
    )
}

// ═════════════ jz-tajwid ═════════════
internal fun DrawScope.jztajwidScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jztajwid", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { study104(a) },
        { p, f ->
            softGlow(Offset(w * 0.5f, h * 0.72f), w * 0.25f, Qn.GoldLight, 0.6f * f * seg(p, 0.1f, 0.4f))
            word106(a, "jztajwid0", 0, Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            qIcon("lock", Offset(w * 0.5f, h * 0.48f), w * 0.06f, Qn.Jade, f * seg(p, 0.45f, 0.6f), a.t)
        },
        { p, f ->
            dim104(f, 0.4f)
            qIcon("crown", Offset(w * 0.5f, h * 0.36f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.05f, 0.25f), a.t)
            for (k in 0 until 10) {
                val ang = 2f * PI.toFloat() * k / 10f + a.t * 0.2f
                drawCircle(Qn.GoldLight, w * 0.007f, Offset(w * 0.5f + kotlin.math.cos(ang) * w * 0.14f, h * 0.36f + sin(ang) * w * 0.12f), alpha = f * seg(p, 0.3f, 0.5f))
            }
            word106(a, "jztajwid1", 0, Offset(w * 0.3f, h * 0.18f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Gold)
            word106(a, "jztajwid1", 1, Offset(w * 0.7f, h * 0.56f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            dim104(f, 0.5f)
            balance12(Offset(w * 0.5f, h * 0.5f), w * 0.14f, 0.12f * (1f - seg(p, 0.3f, 0.6f)) * sin(a.t * 2f), Qn.GoldLight, f * seg(p, 0.05f, 0.2f))
            word106(a, "jztajwid2", 0, Offset(w * 0.3f, h * 0.2f), f * seg(p, 0.2f, 0.35f), ink = ArtInk.Gold)
            word106(a, "jztajwid2", 1, Offset(w * 0.7f, h * 0.2f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            dim104(f, 0.5f)
            val path = Path().apply { moveTo(w * 0.08f, h * 0.36f); for (k in 1..20) lineTo(w * (0.08f + 0.017f * k), h * 0.36f + sin(k * 0.6f) * h * 0.03f) }
            drawPath(path, Qn.Jade, alpha = f * seg(p, 0.05f, 0.25f), style = Stroke(2.4f * density, cap = StrokeCap.Round))
            val jag = Path().apply { moveTo(w * 0.58f, h * 0.36f); for (k in 1..12) lineTo(w * (0.58f + 0.028f * k), h * 0.36f + (if (k % 2 == 0) 1f else -1f) * h * 0.05f) }
            drawPath(jag, Qn.Ember, alpha = f * seg(p, 0.4f, 0.55f), style = Stroke(2.4f * density))
            word106(a, "jztajwid3", 0, Offset(w * 0.25f, h * 0.52f), f * seg(p, 0.15f, 0.3f), ink = ArtInk.Jade, back = Color(0xCC1A3020))
            word106(a, "jztajwid3", 1, Offset(w * 0.75f, h * 0.52f), f * seg(p, 0.5f, 0.65f), back = Color(0xCC401A20))
        },
    )
}

// ═════════════ jz-tarqiq ═════════════
internal fun DrawScope.jztarqiqScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jztarqiq", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            word106(a, "jztarqiq0", 1, Offset(w * 0.5f, h * 0.16f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            for (k in 0 until 3) drawCircle(Color(0xFFE8F0FF), w * 0.04f, Offset(w * (0.35f + 0.15f * k), h * 0.42f - h * 0.03f * sin(a.t + k)), alpha = 0.6f * f * seg(p, 0.1f, 0.3f), style = Stroke(1.4f * density))
            word106(a, "jztarqiq0", 0, Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.3f, 0.45f))
        },
        { p, f ->
            word106(a, "jztarqiq1", 2, Offset(w * 0.5f, h * 0.16f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            for (k in 0 until 3) drawCircle(Qn.Gold, w * 0.06f, Offset(w * (0.32f + 0.18f * k), h * 0.45f), alpha = 0.85f * f * seg(p, 0.1f, 0.3f))
            word106(a, "jztarqiq1", 0, Offset(w * 0.3f, h * 0.66f), f * seg(p, 0.3f, 0.45f))
            word106(a, "jztarqiq1", 1, Offset(w * 0.7f, h * 0.66f), f * seg(p, 0.55f, 0.7f))
        },
        { p, f ->
            word106(a, "jztarqiq2", 0, Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            softGlow(Offset(w * 0.5f, h * 0.16f), w * 0.18f, Qn.GoldLight, 0.45f * f * seg(p, 0.15f, 0.4f))
            word106(a, "jztarqiq2", 1, Offset(w * 0.5f, h * 0.48f), f * seg(p, 0.3f, 0.45f))
            word106(a, "jztarqiq2", 2, Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.55f, 0.7f))
        },
        { p, f ->
            word106(a, "jztarqiq3", 0, Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            drawLine(Qn.GoldLight, Offset(w * 0.45f, h * 0.48f), Offset(w * 0.55f, h * 0.46f), 2.5f * density, StrokeCap.Round, alpha = f * seg(p, 0.3f, 0.45f))
            word106(a, "jztarqiq3", 2, Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.35f, 0.5f))
            word106(a, "jztarqiq3", 1, Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.6f, 0.75f))
        },
    )
}

// ═════════════ jz-waqf ═════════════
internal fun DrawScope.jzwaqfScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val ground = h * 0.86f
    film98(a, "jzwaqf", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { daySky12(Qn.DayTop, Color(0xFFF0E4C0), ground, Color(0xFF8A7A5A)); drawRect(Color(0xFFB8A888), Offset(0f, ground - h * 0.03f), Size(w, h * 0.04f)) },
        { p, f ->
            val walk = seg(p, 0.05f, 0.4f)
            person99(Offset(mix(w * 0.9f, w * 0.55f, easeInOut(walk)), ground), h * 0.1f, Color(0xFFD8D0C4), f, walk = a.t * 2f * (1f - seg(p, 0.35f, 0.45f)))
            qIcon("lock", Offset(w * 0.45f, ground - h * 0.18f), w * 0.06f, Qn.Ember, f * seg(p, 0.3f, 0.45f), a.t)
            qIcon("key", Offset(w * 0.3f, ground - h * 0.18f), w * 0.06f, Qn.Jade, f * seg(p, 0.6f, 0.75f), a.t)
            word106(a, "jzwaqf0", 0, Offset(w * 0.45f, h * 0.36f), f * seg(p, 0.35f, 0.5f))
            word106(a, "jzwaqf0", 1, Offset(w * 0.3f, h * 0.5f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            for (k in 0 until 3) {
                val x = w * (0.78f - 0.28f * k)
                drawLine(Color(0xFF6A5A48), Offset(x, ground), Offset(x, ground - h * 0.2f), 3f * density, alpha = f * seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k))
                drawCircle(listOf(Qn.Jade, Qn.GoldLight, Color(0xFFB8C0E0))[k], w * 0.03f, Offset(x, ground - h * 0.22f), alpha = f * seg(p, 0.05f + 0.25f * k, 0.2f + 0.25f * k))
                word106(a, "jzwaqf1", k, Offset(x, ground - h * 0.34f), f * seg(p, 0.1f + 0.25f * k, 0.25f + 0.25f * k))
            }
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.4f * f)
            drawPath(Path().apply { moveTo(w * 0.5f, ground - h * 0.03f); lineTo(w * 0.62f, ground + h * 0.1f); lineTo(w * 0.38f, ground + h * 0.1f); close() }, Color(0xFF2A2018), alpha = f)
            person99(Offset(w * 0.68f, ground - h * 0.02f), h * 0.1f, Color(0xFFD8D0C4), f, lean = -0.2f)
            word106(a, "jzwaqf2", 0, Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.1f, 0.25f), back = Color(0xCC401A20))
            word106(a, "jzwaqf2", 1, Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            drawRect(Color(0xFF14102A), alpha = 0.5f * f)
            for (k in 0 until 2) {
                val c = Offset(w * (0.3f + 0.4f * k), h * 0.42f)
                word106(a, "jzwaqf3", k, c, f * seg(p, 0.05f + 0.3f * k, 0.2f + 0.3f * k))
                drawLine(Qn.Ember, c + Offset(-w * 0.1f, -h * 0.04f), c + Offset(w * 0.1f, h * 0.04f), 2.5f * density, StrokeCap.Round, alpha = f * seg(p, 0.2f + 0.3f * k, 0.32f + 0.3f * k))
            }
            for (k in 0 until 5) drawCircle(Qn.GoldLight, w * 0.01f, Offset(w * (0.2f + 0.15f * k), h * 0.66f), alpha = f * seg(p, 0.7f, 0.9f))
        },
    )
}

// ═════════════ jz-wasl ═════════════
internal fun DrawScope.jzwaslScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jzwasl", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            word106(a, "jzwasl0", 2, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            drawArc(Qn.GoldLight, 180f, 180f, false, Offset(w * 0.3f, h * 0.36f), Size(w * 0.4f, h * 0.2f), alpha = 0.7f * f * seg(p, 0.3f, 0.5f), style = Stroke(2.5f * density))
            word106(a, "jzwasl0", 0, Offset(w * 0.72f, h * 0.55f), f * seg(p, 0.1f, 0.25f))
            word106(a, "jzwasl0", 1, Offset(w * 0.28f, h * 0.55f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            word106(a, "jzwasl1", 2, Offset(w * 0.5f, h * 0.14f), f * seg(p, 0f, 0.15f), ink = ArtInk.Gold)
            for (k in 0 until 2) {
                val c = Offset(w * (0.7f - 0.4f * k), h * 0.5f)
                drawArc(Qn.GoldLight, 180f, 180f, false, Offset(c.x - w * 0.1f, c.y - h * 0.12f), Size(w * 0.2f, h * 0.12f), alpha = 0.6f * f * seg(p, 0.15f + 0.3f * k, 0.3f + 0.3f * k), style = Stroke(2f * density))
                word106(a, "jzwasl1", k, c + Offset(0f, h * 0.06f), f * seg(p, 0.15f + 0.3f * k, 0.3f + 0.3f * k))
            }
        },
        { p, f ->
            word106(a, "jzwasl2", 0, Offset(w * 0.5f, h * 0.38f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            word106(a, "jzwasl2", 1, Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.45f, 0.6f), back = Color(0xCC1A3020))
        },
        { p, f ->
            for (k in 0 until 4) word106(a, "jzwasl3", k, Offset(w * (if (k % 2 == 0) 0.7f else 0.3f), h * (0.3f + 0.18f * (k / 2))), f * seg(p, 0.05f + 0.2f * k, 0.2f + 0.2f * k))
        },
    )
}

// ═════════════ jz-rawm ═════════════
internal fun DrawScope.jzrawmScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "jzrawm", List(4) { Shot(Offset(w * 0.5f, h * 0.5f), 1f) },
        { room106(a) },
        { p, f ->
            wave106(Offset(w * 0.8f, h * 0.45f), 3, seg(p, 0.05f, 0.4f), f, a.t)
            drawLine(Qn.Ember, Offset(w * 0.4f, h * 0.36f), Offset(w * 0.75f, h * 0.56f), 3f * density, StrokeCap.Round, alpha = f * seg(p, 0.5f, 0.65f))
            word106(a, "jzrawm0", 0, Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.55f, 0.7f), back = Color(0xCC401A20))
        },
        { p, f ->
            wave106(Offset(w * 0.6f, h * 0.45f), 1, seg(p, 0.1f, 0.5f), f * 0.5f, a.t)
            word106(a, "jzrawm1", 0, Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
        },
        { p, f -> tract106(Offset(w * 0.15f, h * 0.12f), w * 0.7f, h * 0.68f, f, shafa = 0.5f + 0.5f * sin(a.t * 3f) * seg(p, 0.2f, 0.4f)); word106(a, "jzrawm2", 0, Offset(w * 0.2f, h * 0.88f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold) },
        { p, f ->
            dim104(f, 0.3f)
            for (k in 0 until 107) drawCircle(Qn.GoldLight, w * 0.006f, Offset(w * (0.1f + 0.08f * (k % 11)), h * (0.24f + 0.05f * (k / 11))), alpha = f * seg(p, 0.1f + 0.004f * k, 0.15f + 0.004f * k))
            word106(a, "jzrawm3", 1, Offset(w * 0.5f, h * 0.12f), f * seg(p, 0f, 0.15f))
            word106(a, "jzrawm3", 0, Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}
