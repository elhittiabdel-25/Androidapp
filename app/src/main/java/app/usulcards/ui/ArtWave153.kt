package app.usulcards.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.sin

/*
 * Wave 153 (2026-11-26): occasions of revelation from al-Wadi'i, al-Nur: li'an in the reports of Ibn 'Umar, Ibn
 * Mas'ud and Anas, why the report of Hudhayfa is weak, and the story of the slander (al-ifk). The first wave drawn
 * only with the owner's vocabulary (kit 153): speech is sound waves, an oath one raised right hand, revelation a beam
 * from above, grades are seals, women fully covered silhouettes, tears a blue glow, mercy a beam on the person, and
 * everyone faces where they go. The Prophet ﷺ is only his named light; companions and 'A'isha are roundels.
 */

private val Woman153 = Color(0xFFB8AEC8)
private val Sand153 = Color(0xFF4A3A2E)

private fun DrawScope.dunes153(alpha: Float) {
    val w = size.width
    val h = size.height
    drawRect(Sand153, Offset(0f, h * 0.86f), Size(w, h * 0.14f), alpha = 0.8f * alpha)
}

/** A chain of narrators from [top] down to [bottom]: [n] links, those in [weak] cracked grey. */
private fun DrawScope.isnad153(top: Offset, bottom: Offset, n: Int, alpha: Float, q: Float, weak: Set<Int> = emptySet(), crack: Float = 0f) {
    if (alpha <= 0.01f) return
    for (k in 0 until n) {
        val u = k / (n - 1f)
        if (u > q + 0.001f) break
        val c = Offset(mix(top.x, bottom.x, u), mix(top.y, bottom.y, u))
        val bad = k in weak
        val col = if (bad) lerpColor(Qn.GoldLight, V153.Weak, crack) else Qn.GoldLight
        drawCircle(col, size.width * 0.022f, c, alpha = alpha, style = Stroke(size.width * 0.006f))
        if (bad && crack > 0f) {
            drawLine(Color(0xFFDADADF), c + Offset(-size.width * 0.02f, -size.width * 0.012f), c + Offset(size.width * 0.02f, size.width * 0.012f), size.width * 0.004f, alpha = alpha * crack)
        }
        if (k < n - 1 && (k + 1) / (n - 1f) <= q + 0.001f) {
            val c2 = Offset(mix(top.x, bottom.x, (k + 1) / (n - 1f)), mix(top.y, bottom.y, (k + 1) / (n - 1f)))
            drawLine(Qn.GoldLight, c + (c2 - c) * 0.25f, c + (c2 - c) * 0.75f, size.width * 0.005f, alpha = 0.7f * alpha)
        }
    }
}

/** A howdah on a far camel: a small domed shelter on the hump. */
private fun DrawScope.howdah153(foot: Offset, s: Float, dir: Float, alpha: Float, walk: Float) {
    camelFar153(foot, s, dir, alpha, walk)
    drawArc(Color(0xFF6B2E2A), 180f, 180f, true, Offset(foot.x - s * 0.3f, foot.y - s * 1.12f), Size(s * 0.5f, s * 0.5f), alpha = alpha)
}

// ═════════════ as-ibnumar: if he speaks, he speaks of a grave matter ═════════════
internal fun DrawScope.asibnumarScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asibnumar", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asibnumar0", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
            plain97(Offset(w * 0.7f, h * 0.42f), w * 0.09f, a, "asibnumar0", 1, f * seg(p, 0.02f, 0.18f))
            figure(Offset(w * 0.3f, h * 0.8f), h * 0.22f, Qn.Person, f * seg(p, 0.1f, 0.25f))
            speech153(Offset(w * 0.4f, h * 0.55f), w * 0.07f, V153.Wave, f * seg(p, 0.25f, 0.35f) * (1f - seg(p, 0.5f, 0.6f)), a.t, 1f)
            tagAt(a.tag("asibnumar0", 0, 2), Offset(w * 0.3f, h * 0.88f), f * seg(p, 0.25f, 0.4f))
        },
        { p, f ->
            veil131(f)
            moodFigure153(Offset(w * 0.5f, h * 0.8f), h * 0.24f, 2, f)
            speech153(Offset(w * 0.68f, h * 0.48f), w * 0.07f, V153.Wave, f * seg(p, 0.05f, 0.2f), a.t, 1f)
            tagAt(a.tag("asibnumar1", 0, 0), Offset(w * 0.74f, h * 0.3f), f * seg(p, 0.05f, 0.2f))
            speech153(Offset(w * 0.32f, h * 0.48f), w * 0.07f, V153.Wave, 0.3f * f * seg(p, 0.45f, 0.55f) * (1f - seg(p, 0.6f, 0.8f)), a.t, -1f)
            tagAt(a.tag("asibnumar1", 0, 1), Offset(w * 0.26f, h * 0.3f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            revelation153(Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.45f, 0.65f), a.t)
            figure(Offset(w * 0.3f, h * 0.82f), h * 0.2f, Qn.Person, f)
            woman153(Offset(w * 0.72f, h * 0.82f), h * 0.2f, Woman153, f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.5f), w * 0.1f, Qn.Parch, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("asibnumar2", 0, 0), Offset(w * 0.3f, h * 0.3f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asibnumar2", 0, 1), Offset(w * 0.5f, h * 0.68f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qFlame(Offset(w * 0.72f, h * 0.6f), w * 0.06f, Qn.Ember, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("asibnumar3", 0, 0), Offset(w * 0.72f, h * 0.3f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asibnumar3", 0, 1), Offset(w * 0.5f, h * 0.45f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            flames153(f * seg(p, 0.55f, 0.75f), a.t)
            tagAt(a.tag("asibnumar3", 0, 2), Offset(w * 0.28f, h * 0.3f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ember)
        },
    )
}

// ═════════════ as-iftah: «O Allah, judge» ═════════════
internal fun DrawScope.asiftahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asiftah", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("moon", Offset(w * 0.82f, h * 0.16f), w * 0.07f, Qn.GoldLight, f, a.t)
            qIcon("house", Offset(w * 0.62f, h * 0.62f), w * 0.22f, Qn.Parch, f * seg(p, 0.05f, 0.2f), a.t)
            plain97(Offset(w * 0.3f, h * 0.36f), w * 0.08f, a, "asiftah0", 0, f * seg(p, 0.1f, 0.25f))
            val x = mix(w * 0.08f, w * 0.44f, seg(p, 0.35f, 0.9f))
            figure(Offset(x, h * 0.84f), h * 0.2f, Qn.Person, f * seg(p, 0.3f, 0.4f), walk = if (p < 0.9f) a.t * 5f else 0f, lean = 0.06f, face = 1f)
            tagAt(a.tag("asiftah0", 0, 1), Offset(w * 0.62f, h * 0.9f), f * seg(p, 0.3f, 0.45f))
        },
        { p, f ->
            veil131(f)
            lawBook153(Offset(w * 0.76f, h * 0.56f), w * 0.1f, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asiftah1", 0, 0), Offset(w * 0.76f, h * 0.82f), f * seg(p, 0.05f, 0.2f))
            qIcon("cross", Offset(w * 0.5f, h * 0.5f), w * 0.08f, Qn.Rose, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("asiftah1", 0, 1), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.35f, 0.5f))
            moodFigure153(Offset(w * 0.24f, h * 0.7f), h * 0.2f, 2, f * seg(p, 0.65f, 0.8f))
            tagAt(a.tag("asiftah1", 0, 2), Offset(w * 0.24f, h * 0.82f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.5f, h * 0.55f)
            sirajMunir(light, w * 0.014f, f, a.t, nameAt = light + Offset(0f, h * 0.14f))
            dua153(Offset(w * 0.5f, h * 0.46f), w * 0.08f, Qn.GoldLight, f * seg(p, 0.1f, 0.3f) * (1f - seg(p, 0.55f, 0.7f)), a.t)
            tagAt(a.tag("asiftah2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            revelation153(light, f * seg(p, 0.6f, 0.8f), a.t)
            tagAt(a.tag("asiftah2", 0, 1), Offset(w * 0.5f, h * 0.86f), f * seg(p, 0.7f, 0.85f))
        },
        { p, f ->
            veil131(f)
            woman153(Offset(w * 0.5f, h * 0.8f), h * 0.22f, Woman153, f)
            qIcon("child", Offset(w * 0.5f, h * 0.48f), w * 0.07f, Color(0xFF3A3040), f * seg(p, 0.2f, 0.4f), a.t)
            tagAt(a.tag("asiftah3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.05f, 0.2f))
            qIcon("check", Offset(w * 0.72f, h * 0.48f), w * 0.07f, Qn.Leaf, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("asiftah3", 0, 1), Offset(w * 0.72f, h * 0.66f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-awwal-lian: the first li'an in Islam ═════════════
internal fun DrawScope.asawwallianScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asawwallian", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.78f, h * 0.24f), w * 0.07f, a, "asawwallian0", 0, f * seg(p, 0.02f, 0.18f))
            plain97(Offset(w * 0.45f, h * 0.45f), w * 0.1f, a, "asawwallian0", 1, f * seg(p, 0.15f, 0.3f))
            oathHand153(Offset(w * 0.22f, h * 0.48f), w * 0.1f, Qn.GoldLight, f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("asawwallian0", 0, 2), Offset(w * 0.45f, h * 0.8f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.45f), w * 0.09f, a, "asawwallian0", 1, f)
            plain97(Offset(w * 0.32f, h * 0.45f), w * 0.09f, a, "asawwallian1", 0, f * seg(p, 0.05f, 0.25f))
            tagAt(a.tag("asawwallian1", 0, 1), Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            val m = easeInOut(seg(p, 0.3f, 0.6f))
            revelation153(Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.6f, 0.8f), a.t)
            plain97(Offset(mix(w * 0.72f, w * 0.6f, m), h * 0.5f), w * 0.08f, a, "asawwallian0", 1, f)
            plain97(Offset(mix(w * 0.28f, w * 0.4f, m), h * 0.5f), w * 0.08f, a, "asawwallian1", 0, f)
            tagAt(a.tag("asawwallian2", 0, 0), Offset(w * 0.72f, h * 0.8f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asawwallian2", 0, 1), Offset(w * 0.28f, h * 0.8f), f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("asawwallian2", 0, 2), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            seal153(Offset(w * 0.7f, h * 0.36f), w * 0.06f, 0, f * seg(p, 0.05f, 0.2f))
            seal153(Offset(w * 0.3f, h * 0.36f), w * 0.06f, 0, f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("asawwallian3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f))
            val q = seg(p, 0.4f, 0.6f)
            drawLine(Qn.GoldLight, Offset(w * 0.7f, h * 0.44f), Offset(mix(w * 0.7f, w * 0.5f, q), mix(h * 0.44f, h * 0.6f, q)), w * 0.008f, StrokeCap.Round, alpha = f * 0.7f)
            drawLine(Qn.GoldLight, Offset(w * 0.3f, h * 0.44f), Offset(mix(w * 0.3f, w * 0.5f, q), mix(h * 0.44f, h * 0.6f, q)), w * 0.008f, StrokeCap.Round, alpha = f * 0.7f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.66f), w * 0.1f, Qn.Parch, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("asawwallian3", 0, 1), Offset(w * 0.5f, h * 0.86f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-hudhayfa: why the report of Hudhayfa is weak (a worked example of grading) ═════════════
internal fun DrawScope.ashudhayfaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val top = Offset(w * 0.5f, h * 0.2f)
    val bot = Offset(w * 0.5f, h * 0.72f)
    film98(a, "ashudhayfa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(top, w * 0.07f, a, "ashudhayfa0", 1, f * seg(p, 0.15f, 0.3f))
            isnad153(top + Offset(0f, h * 0.12f), bot, 5, f, seg(p, 0.3f, 0.6f))
            qIcon("book", bot + Offset(0f, h * 0.08f), w * 0.1f, Qn.Gold, f * seg(p, 0.02f, 0.15f), a.t)
            tagAt(a.tag("ashudhayfa0", 0, 0), Offset(w * 0.8f, h * 0.8f), f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("ashudhayfa0", 0, 2), Offset(w * 0.2f, h * 0.45f), f * seg(p, 0.6f, 0.75f))
        },
        { p, f ->
            veil131(f)
            plain97(top, w * 0.07f, a, "ashudhayfa0", 1, f)
            isnad153(top + Offset(0f, h * 0.12f), bot, 5, f, 1f, setOf(1, 2, 3), seg(p, 0.1f, 0.8f))
            tagAt(a.tag("ashudhayfa1", 0, 0), Offset(w * 0.8f, h * 0.48f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ember)
            tagAt(a.tag("ashudhayfa1", 0, 1), Offset(w * 0.2f, h * 0.38f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ember)
            tagAt(a.tag("ashudhayfa1", 0, 2), Offset(w * 0.8f, h * 0.66f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.68f, h * 0.2f), w * 0.06f, a, "ashudhayfa0", 1, f * (1f - 0.6f * seg(p, 0.5f, 0.7f)))
            isnad153(Offset(w * 0.68f, h * 0.32f), Offset(w * 0.68f, h * 0.72f), 4, f * (1f - 0.6f * seg(p, 0.5f, 0.7f)), seg(p, 0.05f, 0.3f))
            tagAt(a.tag("ashudhayfa2", 0, 0), Offset(w * 0.68f, h * 0.84f), f * seg(p, 0.05f, 0.2f))
            isnad153(Offset(w * 0.32f, h * 0.38f), Offset(w * 0.32f, h * 0.72f), 3, f, seg(p, 0.3f, 0.5f))
            tagAt(a.tag("ashudhayfa2", 0, 1), Offset(w * 0.32f, h * 0.84f), f * seg(p, 0.3f, 0.45f))
            softGlow(Offset(w * 0.32f, h * 0.55f), w * 0.15f, Qn.GoldLight, 0.4f * f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("ashudhayfa2", 0, 2), Offset(w * 0.32f, h * 0.24f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            isnad153(top + Offset(0f, h * 0.06f), bot, 5, f, 1f, setOf(1, 2, 3), 1f)
            tagAt(a.tag("ashudhayfa3", 0, 0), Offset(w * 0.78f, h * 0.4f), f * seg(p, 0.05f, 0.2f))
            val st = seg(p, 0.45f, 0.6f)
            seal153(Offset(w * 0.5f, h * 0.48f), w * mix(0.12f, 0.08f, st), 2, f * st)
            tagAt(a.tag("ashudhayfa3", 0, 1), Offset(w * 0.22f, h * 0.6f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ember)
        },
    )
}

// ═════════════ as-ifk-iqd: 'A'isha's necklace ═════════════
internal fun DrawScope.asifkiqdScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asifkiqd", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a); dunes153(1f) },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.72f, h * 0.4f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, -h * 0.12f))
            for (k in 0 until 4) {
                val lit = if (k == 1) seg(p, 0.45f, 0.6f) else 0f
                val c = Offset(w * (0.22f + 0.08f * k), h * 0.62f)
                drawLine(lerpColor(Dim145, Qn.GoldLight, lit), c, c + Offset(0f, -h * 0.12f), w * 0.012f, StrokeCap.Round, alpha = f * seg(p, 0.15f, 0.3f))
            }
            tagAt(a.tag("asifkiqd0", 0, 0), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.1f, 0.25f))
            close97(Offset(w * 0.3f, h * 0.28f), w * 0.07f, a, "asifkiqd0", 1, 3, f * seg(p, 0.55f, 0.75f))
        },
        { p, f ->
            veil131(f)
            woman153(Offset(w * 0.6f, h * 0.88f), h * 0.2f, Woman153, f, lean = 0.25f * seg(p, 0.3f, 0.5f))
            for (k in 0 until 7) {
                val c = Offset(w * (0.3f + 0.04f * k + 0.01f * sin(k * 2.1f)), h * (0.86f - 0.01f * (k % 2)))
                drawCircle(Color(0xFFE8B0A0), w * 0.008f, c, alpha = f * seg(p, 0.1f, 0.25f))
            }
            softGlow(Offset(w * 0.4f, h * 0.86f), w * 0.08f, Qn.GoldLight, 0.4f * f * seg(p, 0.1f, 0.3f))
            tagAt(a.tag("asifkiqd1", 0, 0), Offset(w * 0.4f, h * 0.6f), f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("asifkiqd1", 0, 1), Offset(w * 0.62f, h * 0.4f), f * seg(p, 0.45f, 0.6f))
        },
        { p, f ->
            veil131(f)
            val go = seg(p, 0.15f, 1f)
            for (k in 0 until 3) {
                val x = mix(w * 0.5f, w * 0.05f, go) + k * w * 0.1f
                if (k == 0) howdah153(Offset(x, h * 0.84f), h * 0.1f, -1f, f, a.t * 4f) else camelFar153(Offset(x, h * 0.84f), h * 0.1f, -1f, f, a.t * 4f + k)
            }
            woman153(Offset(w * 0.86f, h * 0.88f), h * 0.12f, Woman153, f * seg(p, 0.5f, 0.7f), sit = 1f)
            tagAt(a.tag("asifkiqd2", 0, 0), Offset(w * 0.3f, h * 0.5f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asifkiqd2", 0, 1), Offset(w * 0.3f, h * 0.3f), f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.3f, h * 0.24f), w * 0.07f, a, "asifkiqd3", 0, f * seg(p, 0.02f, 0.18f))
            val go = seg(p, 0.6f, 1f)
            camelFar153(Offset(mix(w * 0.4f, w * 0.15f, go), h * 0.86f), h * 0.16f, -1f, f * seg(p, 0.1f, 0.25f), if (go > 0f) a.t * 4f else 0f)
            woman153(Offset(w * 0.7f, h * 0.88f), h * 0.13f, Woman153, f * (1f - seg(p, 0.55f, 0.65f)), sit = 1f)
            speech153(Offset(w * 0.48f, h * 0.66f), w * 0.06f, V153.Wave, f * seg(p, 0.25f, 0.35f) * (1f - seg(p, 0.55f, 0.65f)), a.t, 1f)
            tagAt(a.tag("asifkiqd3", 0, 1), Offset(w * 0.6f, h * 0.46f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-ifk-tisa: «how is that one?» ═════════════
internal fun DrawScope.asifktisaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asifktisa", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("bed", Offset(w * 0.68f, h * 0.62f), w * 0.18f, Qn.Parch, f * seg(p, 0.02f, 0.15f), a.t)
            mood153(Offset(w * 0.68f, h * 0.58f), w * 0.14f, 0, f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asifktisa0", 0, 0), Offset(w * 0.68f, h * 0.84f), f * seg(p, 0.05f, 0.2f))
            crowd145(w * 0.25f, h * 0.84f, 4, h * 0.16f, f * seg(p, 0.3f, 0.45f))
            speech153(Offset(w * 0.25f, h * 0.5f), w * 0.06f, V153.Wave, f * seg(p, 0.35f, 0.45f), a.t, -1f)
            tagAt(a.tag("asifktisa0", 0, 1), Offset(w * 0.25f, h * 0.3f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ember)
            tagAt(a.tag("asifktisa0", 0, 2), Offset(w * 0.68f, h * 0.3f), f * seg(p, 0.65f, 0.8f))
        },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.28f, h * 0.48f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, -h * 0.13f))
            softGlow(Offset(w * 0.68f, h * 0.6f), w * 0.1f, Qn.GoldLight, 0.4f * f * (1f - seg(p, 0.05f, 0.4f)))
            qIcon("bed", Offset(w * 0.68f, h * 0.62f), w * 0.18f, Qn.Parch, f, a.t)
            tagAt(a.tag("asifktisa1", 0, 0), Offset(w * 0.68f, h * 0.38f), f * seg(p, 0.05f, 0.2f) * (1f - 0.6f * seg(p, 0.3f, 0.45f)))
            speech153(Offset(w * 0.38f, h * 0.48f), w * 0.04f, V153.Wave, 0.7f * f * seg(p, 0.5f, 0.6f), a.t, 1f)
            tagAt(a.tag("asifktisa1", 0, 1), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            qIcon("moon", Offset(w * 0.2f, h * 0.16f), w * 0.06f, Qn.GoldLight, f, a.t)
            val go = seg(p, 0f, 0.5f)
            val x = mix(w * 0.8f, w * 0.55f, go)
            val trip = seg(p, 0.45f, 0.6f) * (1f - seg(p, 0.75f, 0.9f))
            woman153(Offset(x - w * 0.12f, h * 0.84f), h * 0.2f, Woman153, f, lean = 0.5f * trip)
            woman153(Offset(x, h * 0.84f), h * 0.2f, Woman153, f, lean = 0.06f)
            tagAt(a.tag("asifktisa2", 0, 0), Offset(w * 0.42f, h * 0.36f), f * seg(p, 0.1f, 0.25f))
            speech153(Offset(x - w * 0.2f, h * 0.6f), w * 0.05f, V153.Wave, f * seg(p, 0.55f, 0.65f), a.t, -1f)
            tagAt(a.tag("asifktisa2", 0, 1), Offset(w * 0.22f, h * 0.5f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            woman153(Offset(w * 0.62f, h * 0.84f), h * 0.2f, Woman153, f, sit = 1f)
            woman153(Offset(w * 0.4f, h * 0.84f), h * 0.18f, Woman153, f, sit = 1f, lean = 0.2f)
            mood153(Offset(w * 0.4f, h * 0.74f), w * 0.12f, 0, f * seg(p, 0.4f, 0.6f))
            speech153(Offset(w * 0.54f, h * 0.66f), w * 0.04f, V153.Wave, 0.7f * f * seg(p, 0.05f, 0.15f) * (1f - seg(p, 0.4f, 0.5f)), a.t, -1f)
            tagAt(a.tag("asifktisa3", 0, 0), Offset(w * 0.66f, h * 0.36f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asifktisa3", 0, 1), Offset(w * 0.4f, h * 0.24f), f * seg(p, 0.2f, 0.35f))
        },
    )
}

// ═════════════ as-ifk-barira: «who will excuse me?» ═════════════
internal fun DrawScope.asifkbariraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asifkbarira", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.5f, h * 0.5f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, h * 0.13f))
            qIcon("hourglass", Offset(w * 0.5f, h * 0.22f), w * 0.07f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            tagAt(a.tag("asifkbarira0", 0, 0), Offset(w * 0.5f, h * 0.86f), f * seg(p, 0.05f, 0.2f))
            close97(Offset(w * 0.78f, h * 0.42f), w * 0.07f, a, "asifkbarira0", 1, 3, f * seg(p, 0.35f, 0.5f))
            plain97(Offset(w * 0.22f, h * 0.42f), w * 0.07f, a, "asifkbarira0", 2, f * seg(p, 0.45f, 0.6f))
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.75f, h * 0.28f), w * 0.07f, a, "asifkbarira1", 0, f * seg(p, 0.02f, 0.18f))
            woman153(Offset(w * 0.75f, h * 0.84f), h * 0.2f, Woman153, f * seg(p, 0.05f, 0.2f))
            qIcon("bed", Offset(w * 0.35f, h * 0.72f), w * 0.14f, Qn.Parch, f * seg(p, 0.3f, 0.45f), a.t)
            drawOval(Color(0xFFE8D8B8), Offset(w * 0.18f, h * 0.76f), Size(w * 0.1f, h * 0.05f), alpha = f * seg(p, 0.3f, 0.45f))
            camelFar153(Offset(w * 0.1f, h * 0.86f), h * 0.07f, 1f, f * seg(p, 0.6f, 0.75f))
            tagAt(a.tag("asifkbarira1", 0, 1), Offset(w * 0.35f, h * 0.48f), f * seg(p, 0.3f, 0.45f))
            tagAt(a.tag("asifkbarira1", 0, 2), Offset(w * 0.16f, h * 0.62f), f * seg(p, 0.6f, 0.75f))
        },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.5f, h * 0.36f)
            sirajMunir(light, w * 0.014f, f, a.t, nameAt = light + Offset(0f, -h * 0.13f))
            speech153(Offset(w * 0.5f, h * 0.48f), w * 0.08f, V153.Wave, f * seg(p, 0.05f, 0.15f), a.t, -1f)
            crowd145(w * 0.5f, h * 0.86f, 6, h * 0.14f, f * seg(p, 0.1f, 0.25f), gap = 0.1f)
            tagAt(a.tag("asifkbarira2", 0, 0), Offset(w * 0.5f, h * 0.58f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asifkbarira2", 0, 1), Offset(w * 0.8f, h * 0.5f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.24f), w * 0.07f, a, "asifkbarira3", 0, f * seg(p, 0.02f, 0.18f))
            val stir = seg(p, 0.4f, 0.6f) * (1f - seg(p, 0.85f, 1f))
            crowd145(w * 0.74f, h * 0.84f, 3, h * 0.16f, f, lean = -0.12f * stir * sin(a.t * 6f))
            crowd145(w * 0.26f, h * 0.84f, 3, h * 0.16f, f, lean = 0.12f * stir * sin(a.t * 6f))
            mood153(Offset(w * 0.5f, h * 0.7f), w * 0.2f, 2, f * stir)
            tagAt(a.tag("asifkbarira3", 0, 1), Offset(w * 0.74f, h * 0.48f), f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("asifkbarira3", 0, 2), Offset(w * 0.26f, h * 0.48f), f * seg(p, 0.4f, 0.55f))
        },
    )
}

// ═════════════ as-ifk-bara: «praise Allah, He has declared you innocent» ═════════════
internal fun DrawScope.asifkbaraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asifkbara", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asifkbara0", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
            val light = Offset(w * 0.72f, h * 0.5f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, h * 0.13f))
            for (k in 0 until 30) {
                val c = Offset(w * (0.14f + 0.025f * (k % 10)), h * (0.34f + 0.05f * (k / 10)))
                drawCircle(Dim145, w * 0.006f, c, alpha = f * seg(p, 0.05f + 0.01f * k, 0.1f + 0.01f * k))
            }
            tagAt(a.tag("asifkbara0", 0, 1), Offset(w * 0.25f, h * 0.62f), f * seg(p, 0.05f, 0.2f))
            woman153(Offset(w * 0.4f, h * 0.86f), h * 0.18f, Woman153, f, sit = 1f)
        },
        { p, f ->
            veil131(f)
            woman153(Offset(w * 0.5f, h * 0.84f), h * 0.2f, Woman153, f, sit = 1f)
            mood153(Offset(w * 0.5f, h * 0.74f), w * 0.14f, 0, f * (1f - 0.5f * seg(p, 0.5f, 0.7f)))
            tagAt(a.tag("asifkbara1", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.1f, 0.25f))
            tagAt(a.tag("asifkbara1", 0, 1), Offset(w * 0.5f, h * 0.42f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            val her = Offset(w * 0.32f, h * 0.84f)
            mercy153(Offset(her.x, h * 0.7f), f * seg(p, 0.3f, 0.5f), a.t)
            woman153(her, h * 0.2f, Woman153, f, sit = 1f)
            mood153(Offset(her.x, h * 0.74f), w * 0.12f, 1, f * seg(p, 0.4f, 0.6f))
            val light = Offset(w * 0.7f, h * 0.55f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, h * 0.13f))
            tagAt(a.tag("asifkbara2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Gold)
            tagAt(a.tag("asifkbara2", 0, 1), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.5f, h * 0.5f)
            revelation153(light, f * seg(p, 0.05f, 0.25f), a.t)
            sirajMunir(light, w * 0.014f, f, a.t, nameAt = light + Offset(0f, h * 0.14f))
            for (k in 0 until 6) {
                val ph = (a.t * 0.3f + k / 6f) % 1f
                drawCircle(Color(0xFFEFF4FA), w * 0.007f, Offset(w * (0.44f + 0.024f * k), h * (0.54f + 0.12f * ph)), alpha = f * (1f - ph) * seg(p, 0.2f, 0.35f))
            }
            tagAt(a.tag("asifkbara3", 0, 0), Offset(w * 0.78f, h * 0.36f), f * seg(p, 0.2f, 0.35f))
            qIcon("cloud", Offset(w * 0.22f, h * 0.22f), w * 0.1f, Color(0xFFCFD8E6), f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("asifkbara3", 0, 1), Offset(w * 0.22f, h * 0.38f), f * seg(p, 0.5f, 0.65f))
        },
    )
}

// ═════════════ as-mistah: Abu Bakr and Mistah ═════════════
internal fun DrawScope.asmistahScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asmistah", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.72f, h * 0.38f), w * 0.09f, a, "asmistah0", 0, 0, f * seg(p, 0.02f, 0.18f))
            plain97(Offset(w * 0.28f, h * 0.38f), w * 0.07f, a, "asmistah0", 1, f * seg(p, 0.3f, 0.45f))
            for (k in 0 until 2) {
                val q = (a.t * 0.4f + k * 0.5f) % 1f
                sack153(Offset(mix(w * 0.62f, w * 0.38f, q), h * 0.42f), w * 0.05f, f * seg(p, 0.45f, 0.6f) * (1f - q))
            }
        },
        { p, f ->
            veil131(f)
            close97(Offset(w * 0.72f, h * 0.38f), w * 0.09f, a, "asmistah0", 0, 0, f)
            plain97(Offset(w * 0.28f, h * 0.38f), w * 0.07f, a, "asmistah0", 1, f)
            qIcon("cross", Offset(w * 0.5f, h * 0.42f), w * 0.07f, Qn.Rose, f * seg(p, 0.1f, 0.25f), a.t)
            tagAt(a.tag("asmistah1", 0, 0), Offset(w * 0.5f, h * 0.62f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Ember)
            revelation153(Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.7f), a.t)
            tagAt(a.tag("asmistah1", 0, 1), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asmistah2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            close97(Offset(w * 0.72f, h * 0.42f), w * 0.09f, a, "asmistah0", 0, 0, f)
            mercy153(Offset(w * 0.72f, h * 0.42f), f * seg(p, 0.15f, 0.35f), a.t)
            plain97(Offset(w * 0.28f, h * 0.42f), w * 0.07f, a, "asmistah0", 1, f)
            for (k in 0 until 3) {
                val q = (a.t * 0.4f + k / 3f) % 1f
                sack153(Offset(mix(w * 0.62f, w * 0.38f, q), h * 0.46f), w * 0.05f, f * seg(p, 0.5f, 0.65f) * (1f - q))
            }
            qIcon("check", Offset(w * 0.5f, h * 0.66f), w * 0.06f, Qn.Leaf, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("asmistah2", 0, 1), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.55f, 0.7f))
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asmistah3", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
            close97(Offset(w * 0.5f, h * 0.44f), w * 0.09f, a, "asmistah3", 1, 3, f * seg(p, 0.05f, 0.25f))
            woman153(Offset(w * 0.5f, h * 0.86f), h * 0.18f, Woman153, f * seg(p, 0.1f, 0.25f))
            mercy153(Offset(w * 0.5f, h * 0.8f), f * seg(p, 0.45f, 0.65f), a.t)
        },
    )
}
