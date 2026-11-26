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
 * Wave 154 (2026-11-26): occasions of revelation from al-Wadi'i, al-Nur to al-Qasas: Ibn Ubayy's slave girls, the
 * promise of security, the keys left with the sick, 'Uqba ibn Abi Mu'ayt, the greatest sins, the expiation asked for,
 * the two verses on killing, the ten of the People of the Book, and Abu Talib. Drawn only with the owner's vocabulary
 * (kit 153): speech as waves, revelation a beam from above, mercy a beam on the person, women covered silhouettes,
 * prohibition a red cross, permission a green check, the hostile tribes on a parchment map, Hell only as flames along
 * the bottom of the scene. The Prophet ﷺ is only his named light.
 */

private val Woman154 = Color(0xFFB8AEC8)

private fun DrawScope.mountains154(alpha: Float) {
    if (alpha <= 0.01f) return
    val w = size.width
    val h = size.height
    for (k in 0 until 4) {
        val cx = w * (0.12f + 0.25f * k)
        drawPath(Path().apply {
            moveTo(cx - w * 0.18f, h * 0.86f); lineTo(cx, h * (0.5f + 0.06f * (k % 2))); lineTo(cx + w * 0.18f, h * 0.86f); close()
        }, Color(0xFF3A3040), alpha = alpha)
    }
}

private fun DrawScope.key154(c: Offset, s: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    qIcon("key", c, s, Qn.Gold, alpha)
}

// ═════════════ as-musayka: the slave girls of Ibn Ubayy ═════════════
internal fun DrawScope.asmusaykaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asmusayka", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.74f, h * 0.3f), w * 0.07f, a, "asmusayka0", 0, f * seg(p, 0.02f, 0.18f))
            figure(Offset(w * 0.74f, h * 0.84f), h * 0.22f, Dim145, f * seg(p, 0.05f, 0.2f))
            for (k in 0 until 2) woman153(Offset(w * (0.36f + 0.12f * k), h * 0.86f), h * 0.18f, Woman154, f * seg(p, 0.3f, 0.45f), lean = 0.12f)
            mood153(Offset(w * 0.42f, h * 0.74f), w * 0.14f, 0, f * seg(p, 0.4f, 0.55f))
            tagAt(a.tag("asmusayka0", 0, 1), Offset(w * 0.42f, h * 0.46f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ember)
            for (k in 0 until 3) qIcon("coin", Offset(w * (0.12f + 0.06f * k), h * 0.6f), w * 0.04f, Qn.Gold, f * seg(p, 0.65f, 0.8f), a.t)
            tagAt(a.tag("asmusayka0", 0, 2), Offset(w * 0.18f, h * 0.44f), f * seg(p, 0.65f, 0.8f))
        },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.22f, h * 0.48f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, -h * 0.13f))
            val go = seg(p, 0.05f, 0.6f)
            for (k in 0 until 2) {
                val x = mix(w * (0.72f + 0.12f * k), w * (0.46f + 0.12f * k), go)
                woman153(Offset(x, h * 0.86f), h * 0.18f, Woman154, f, lean = 0.06f)
            }
            tagAt(a.tag("asmusayka1", 0, 0), Offset(w * 0.6f, h * 0.3f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asmusayka1", 0, 1), Offset(w * 0.82f, h * 0.3f), f * seg(p, 0.15f, 0.3f))
            speech153(Offset(w * 0.4f, h * 0.68f), w * 0.05f, V153.Wave, f * seg(p, 0.6f, 0.7f), a.t, -1f)
            tagAt(a.tag("asmusayka1", 0, 2), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.6f, 0.75f))
        },
        { p, f ->
            veil131(f)
            revelation153(Offset(w * 0.5f, h * 0.4f), f * seg(p, 0.05f, 0.25f), a.t)
            for (k in 0 until 3) qIcon("coin", Offset(w * (0.68f + 0.06f * k), h * 0.6f), w * 0.04f, Qn.Gold, f, a.t)
            qIcon("cross", Offset(w * 0.74f, h * 0.6f), w * 0.1f, Qn.Rose, f * seg(p, 0.15f, 0.3f), a.t)
            tagAt(a.tag("asmusayka2", 0, 0), Offset(w * 0.74f, h * 0.4f), f * seg(p, 0.15f, 0.3f))
            for (k in 0 until 2) woman153(Offset(w * (0.24f + 0.12f * k), h * 0.86f), h * 0.18f, Woman154, f)
            mercy153(Offset(w * 0.3f, h * 0.7f), f * seg(p, 0.5f, 0.7f), a.t)
            tagAt(a.tag("asmusayka2", 0, 1), Offset(w * 0.3f, h * 0.4f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            lawBook153(Offset(w * 0.5f, h * 0.52f), w * 0.12f, f * seg(p, 0.02f, 0.18f))
            tagAt(a.tag("asmusayka3", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ember)
            tagAt(a.tag("asmusayka3", 0, 1), Offset(w * 0.5f, h * 0.84f), f * seg(p, 0.5f, 0.65f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-amn: they slept with their weapons ═════════════
internal fun DrawScope.asamnScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asamn", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            battleMap153(f)
            val c = Offset(w * 0.5f, h * 0.55f)
            army153(c, w * 0.16f, true, f * seg(p, 0.05f, 0.2f))
            val corners = listOf(Offset(w * 0.12f, h * 0.3f), Offset(w * 0.88f, h * 0.3f), Offset(w * 0.12f, h * 0.84f), Offset(w * 0.88f, h * 0.84f))
            for ((k, o) in corners.withIndex()) {
                army153(o, w * 0.12f, false, f * seg(p, 0.25f + 0.05f * k, 0.35f + 0.05f * k))
                arrow153(o, c + (o - c) * 0.3f, false, seg(p, 0.45f, 0.75f), f)
            }
            plain97(Offset(w * 0.5f, h * 0.18f), w * 0.07f, a, "asamn0", 0, f * seg(p, 0.02f, 0.15f))
            tagAt(a.tag("asamn0", 0, 1), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.6f, 0.75f))
        },
        { p, f ->
            veil131(f)
            qIcon("moon", Offset(w * 0.8f, h * 0.16f), w * 0.06f, Qn.GoldLight, f, a.t)
            for (k in 0 until 4) {
                val x = w * (0.26f + 0.16f * k)
                figure(Offset(x, h * 0.84f), h * 0.18f, Qn.Person, f * seg(p, 0.05f + 0.05f * k, 0.15f + 0.05f * k))
                drawLine(Color(0xFF9A9AA6), Offset(x + h * 0.04f, h * 0.86f), Offset(x + h * 0.06f, h * 0.6f), w * 0.006f, StrokeCap.Round, alpha = f * seg(p, 0.3f, 0.45f))
            }
            mood153(Offset(w * 0.5f, h * 0.74f), w * 0.3f, 2, 0.6f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("asamn1", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asamn1", 0, 1), Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.35f, 0.5f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asamn2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            revelation153(Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.55f, 0.75f), a.t)
            for (k in 0 until 3) qIcon("house", Offset(w * (0.28f + 0.22f * k), h * 0.72f), w * 0.14f, Qn.Parch, f, a.t)
            mood153(Offset(w * 0.5f, h * 0.72f), w * 0.3f, 1, f * seg(p, 0.6f, 0.8f))
            tagAt(a.tag("asamn2", 0, 1), Offset(w * 0.5f, h * 0.88f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            val day = seg(p, 0.1f, 0.6f)
            drawRect(lerpColor(Color(0xFF14102A), Color(0xFF6FA0C8), day), Offset.Zero, size, alpha = f)
            qIcon("sun", Offset(w * 0.78f, mix(h * 0.6f, h * 0.2f, day)), w * 0.1f, Qn.Gold, f * day, a.t)
            for (k in 0 until 4) qIcon("house", Offset(w * (0.2f + 0.2f * k), h * 0.78f), w * 0.13f, Qn.Parch, f, a.t)
            tagAt(a.tag("asamn3", 0, 0), Offset(w * 0.4f, h * 0.36f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asamn3", 0, 1), Offset(w * 0.4f, h * 0.52f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-mafatih: the keys left with the sick ═════════════
internal fun DrawScope.asmafatihScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asmafatih", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.78f, h * 0.66f), w * 0.2f, Qn.Parch, f, a.t)
            val give = seg(p, 0.15f, 0.4f)
            key154(Offset(mix(w * 0.62f, w * 0.42f, give), h * 0.62f), w * 0.07f, f * seg(p, 0.1f, 0.2f))
            figure(Offset(w * 0.62f, h * 0.86f), h * 0.2f, Qn.Person, f, arms = 0.6f * (1f - seg(p, 0.45f, 0.55f)))
            figure(Offset(w * 0.36f, h * 0.86f), h * 0.14f, Dim145, f, lean = 0.2f)
            mood153(Offset(w * 0.36f, h * 0.76f), w * 0.08f, 0, f * 0.6f)
            tagAt(a.tag("asmafatih0", 0, 0), Offset(w * 0.52f, h * 0.4f), f * seg(p, 0.15f, 0.3f))
            tagAt(a.tag("asmafatih0", 0, 1), Offset(w * 0.3f, h * 0.24f), f * seg(p, 0.5f, 0.65f))
            val go = seg(p, 0.6f, 1f)
            if (go > 0f) for (k in 0 until 3) figure(Offset(mix(w * 0.5f, w * -0.1f, go) + k * w * 0.06f, h * 0.9f), h * 0.12f, Qn.Person, f, walk = a.t * 5f + k, lean = 0.06f)
        },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.5f, h * 0.56f), w * 0.3f, Qn.Parch, f, a.t)
            dates153(Offset(w * 0.4f, h * 0.84f), w * 0.07f, f * seg(p, 0.2f, 0.35f))
            sack153(Offset(w * 0.6f, h * 0.84f), w * 0.08f, f * seg(p, 0.25f, 0.4f))
            tagAt(a.tag("asmafatih1", 0, 0), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.05f, 0.2f))
            qIcon("check", Offset(w * 0.8f, h * 0.7f), w * 0.07f, Qn.Leaf, f * seg(p, 0.4f, 0.55f), a.t)
            tagAt(a.tag("asmafatih1", 0, 1), Offset(w * 0.24f, h * 0.7f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            qIcon("house", Offset(w * 0.7f, h * 0.6f), w * 0.22f, Qn.Parch, f, a.t)
            dates153(Offset(w * 0.5f, h * 0.84f), w * 0.07f, f)
            figure(Offset(w * 0.28f, h * 0.86f), h * 0.18f, Dim145, f, lean = 0.25f)
            mood153(Offset(w * 0.28f, h * 0.76f), w * 0.1f, 2, f * (1f - seg(p, 0.55f, 0.75f)))
            qIcon("cross", Offset(w * 0.5f, h * 0.7f), w * 0.06f, Qn.Rose, f * seg(p, 0.1f, 0.25f) * (1f - seg(p, 0.5f, 0.6f)), a.t)
            tagAt(a.tag("asmafatih2", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.05f, 0.2f) * (1f - 0.5f * seg(p, 0.5f, 0.6f)), ink = ArtInk.Ember)
            revelation153(Offset(w * 0.4f, h * 0.7f), f * seg(p, 0.45f, 0.65f), a.t)
            qIcon("check", Offset(w * 0.5f, h * 0.7f), w * 0.07f, Qn.Leaf, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("asmafatih2", 0, 1), Offset(w * 0.6f, h * 0.2f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            key154(Offset(w * 0.78f, h * 0.5f), w * 0.1f, f * seg(p, 0.02f, 0.15f))
            drawLine(Qn.GoldLight, Offset(w * 0.7f, h * 0.5f), Offset(mix(w * 0.7f, w * 0.58f, seg(p, 0.2f, 0.35f)), h * 0.5f), w * 0.008f, StrokeCap.Round, alpha = f)
            qIcon("house", Offset(w * 0.5f, h * 0.5f), w * 0.14f, Qn.Parch, f * seg(p, 0.25f, 0.4f), a.t)
            drawLine(Qn.GoldLight, Offset(w * 0.42f, h * 0.5f), Offset(mix(w * 0.42f, w * 0.3f, seg(p, 0.45f, 0.6f)), h * 0.5f), w * 0.008f, StrokeCap.Round, alpha = f)
            dates153(Offset(w * 0.22f, h * 0.54f), w * 0.08f, f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("asmafatih3", 0, 0), Offset(w * 0.62f, h * 0.24f), f * seg(p, 0.05f, 0.2f))
            qIcon("check", Offset(w * 0.22f, h * 0.72f), w * 0.07f, Qn.Leaf, f * seg(p, 0.65f, 0.8f), a.t)
            tagAt(a.tag("asmafatih3", 0, 1), Offset(w * 0.3f, h * 0.86f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-uqba: he bites his hands ═════════════
internal fun DrawScope.asuqbaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asuqba", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.3f, h * 0.5f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, -h * 0.13f))
            plain97(Offset(w * 0.6f, h * 0.48f), w * 0.07f, a, "asuqba0", 0, f * seg(p, 0.05f, 0.2f))
            crowd145(w * 0.82f, h * 0.86f, 3, h * 0.14f, f * seg(p, 0.45f, 0.6f))
            speech153(Offset(w * 0.74f, h * 0.62f), w * 0.05f, V153.Wave, f * seg(p, 0.5f, 0.6f), a.t, -1f)
            tagAt(a.tag("asuqba0", 0, 1), Offset(w * 0.82f, h * 0.36f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            figure(Offset(w * 0.64f, h * 0.84f), h * 0.22f, Dim145, f)
            figure(Offset(w * 0.36f, h * 0.84f), h * 0.22f, Dim145, f, face = 1f)
            speech153(Offset(w * 0.44f, h * 0.6f), w * 0.05f, V153.Wave, f * seg(p, 0.1f, 0.2f), a.t, 1f)
            qIcon("cross", Offset(w * 0.5f, h * 0.6f), w * 0.07f, Qn.Rose, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("asuqba1", 0, 0), Offset(w * 0.64f, h * 0.34f), f * seg(p, 0.05f, 0.2f))
            tagAt(a.tag("asuqba1", 0, 1), Offset(w * 0.4f, h * 0.34f), f * seg(p, 0.35f, 0.5f))
        },
        { p, f ->
            veil131(f)
            mountains154(f)
            tagAt(a.tag("asuqba2", 0, 0), Offset(w * 0.5f, h * 0.3f), f * seg(p, 0.05f, 0.2f))
            val go = seg(p, 0.4f, 0.9f)
            figure(Offset(mix(w * 0.55f, w * 0.85f, go), h * 0.92f), h * 0.12f, Qn.Rose, f * seg(p, 0.35f, 0.45f), walk = if (go in 0.01f..0.99f) a.t * 5f else 0f, lean = 0.06f, face = 1f)
            tagAt(a.tag("asuqba2", 0, 1), Offset(w * 0.78f, h * 0.18f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            flames153(f * seg(p, 0.3f, 0.5f), a.t)
            moodFigure153(Offset(w * 0.5f, h * 0.72f), h * 0.24f, 0, f)
            tagAt(a.tag("asuqba3", 0, 0), Offset(w * 0.5f, h * 0.24f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ember)
            figure(Offset(w * 0.18f, h * 0.7f), h * 0.18f, Dim145, 0.5f * f * seg(p, 0.5f, 0.65f))
            tagAt(a.tag("asuqba3", 0, 1), Offset(w * 0.2f, h * 0.38f), f * seg(p, 0.5f, 0.65f))
        },
    )
}

// ═════════════ as-adham: which sin is greatest ═════════════
internal fun DrawScope.asadhamScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    fun steps(f: Float, lit: Int) {
        for (k in 0 until 3) {
            val hh = h * (0.36f - 0.08f * k)
            val x = w * (0.72f - 0.22f * k)
            val col = if (k == lit) Qn.Rose else Dim145
            drawRect(col, Offset(x - w * 0.08f, h * 0.9f - hh), Size(w * 0.16f, hh), alpha = f * (if (k == lit) 0.85f else 0.35f))
        }
    }
    film98(a, "asadham", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            steps(f * seg(p, 0.05f, 0.2f), 0)
            tagAt(a.tag("asadham0", 0, 0), Offset(w * 0.5f, h * 0.16f), f * seg(p, 0.05f, 0.2f))
            drawRect(Color(0xFF6A6A72), Offset(w * 0.68f, h * 0.38f), Size(w * 0.08f, h * 0.14f), alpha = f * seg(p, 0.4f, 0.55f))
            qIcon("cross", Offset(w * 0.72f, h * 0.44f), w * 0.1f, Qn.Rose, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("asadham0", 0, 1), Offset(w * 0.4f, h * 0.36f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            steps(f, 1)
            qIcon("child", Offset(w * 0.5f, h * 0.42f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.05f, 0.2f), a.t)
            qIcon("cup", Offset(w * 0.6f, h * 0.44f), w * 0.05f, Qn.GoldLight, f * seg(p, 0.4f, 0.55f), a.t)
            qIcon("cross", Offset(w * 0.55f, h * 0.43f), w * 0.1f, Qn.Rose, f * seg(p, 0.6f, 0.75f), a.t)
            tagAt(a.tag("asadham1", 0, 0), Offset(w * 0.3f, h * 0.2f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ember)
            tagAt(a.tag("asadham1", 0, 1), Offset(w * 0.7f, h * 0.2f), f * seg(p, 0.4f, 0.55f))
        },
        { p, f ->
            veil131(f)
            steps(f * (1f - seg(p, 0.5f, 0.65f)), 2)
            qIcon("house", Offset(w * 0.4f, h * 0.44f), w * 0.12f, Qn.Parch, f * seg(p, 0.05f, 0.2f) * (1f - seg(p, 0.5f, 0.65f)), a.t)
            qIcon("house", Offset(w * 0.6f, h * 0.44f), w * 0.12f, Qn.Parch, f * seg(p, 0.05f, 0.2f) * (1f - seg(p, 0.5f, 0.65f)), a.t)
            qIcon("cross", Offset(w * 0.5f, h * 0.44f), w * 0.08f, Qn.Rose, f * seg(p, 0.2f, 0.35f) * (1f - seg(p, 0.5f, 0.65f)), a.t)
            tagAt(a.tag("asadham2", 0, 0), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.1f, 0.25f) * (1f - seg(p, 0.5f, 0.65f)), ink = ArtInk.Ember)
            revelation153(Offset(w * 0.5f, h * 0.6f), f * seg(p, 0.6f, 0.8f), a.t)
            qIcon("scroll", Offset(w * 0.5f, h * 0.6f), w * 0.1f, Qn.Parch, f * seg(p, 0.65f, 0.8f), a.t)
            tagAt(a.tag("asadham2", 0, 1), Offset(w * 0.5f, h * 0.82f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            steps(f, 0)
            tagAt(a.tag("asadham3", 0, 0), Offset(w * 0.72f, h * 0.42f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ember)
            tagAt(a.tag("asadham3", 0, 1), Offset(w * 0.5f, h * 0.2f), f * seg(p, 0.2f, 0.35f))
            tagAt(a.tag("asadham3", 0, 2), Offset(w * 0.32f, h * 0.36f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-kaffara: «if only you told us there is an expiation» ═════════════
internal fun DrawScope.askaffaraScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    fun deeds(f: Float, turn: Float) {
        for (k in 0 until 24) {
            val c = Offset(w * (0.3f + 0.05f * (k % 9)), h * (0.4f + 0.06f * (k / 9)))
            val g = seg(turn, k / 24f, (k + 1) / 24f)
            drawCircle(lerpColor(Color(0xFF4A4452), Qn.GoldLight, g), w * 0.012f, c, alpha = f)
        }
    }
    film98(a, "askaffara", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.18f), w * 0.07f, a, "askaffara0", 0, f * seg(p, 0.02f, 0.15f))
            deeds(f * seg(p, 0.2f, 0.4f), 0f)
            crowd145(w * 0.5f, h * 0.9f, 5, h * 0.12f, f * seg(p, 0.1f, 0.25f))
            mood153(Offset(w * 0.5f, h * 0.82f), w * 0.3f, 2, 0.6f * f * seg(p, 0.3f, 0.5f))
            tagAt(a.tag("askaffara0", 0, 1), Offset(w * 0.78f, h * 0.36f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Ember)
            tagAt(a.tag("askaffara0", 0, 2), Offset(w * 0.22f, h * 0.36f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            val light = Offset(w * 0.22f, h * 0.5f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, -h * 0.13f))
            val go = seg(p, 0f, 0.45f)
            for (k in 0 until 4) figure(Offset(mix(w * 0.95f, w * 0.5f, go) + k * w * 0.08f, h * 0.84f), h * 0.18f, Qn.Person, f, walk = if (go < 1f) a.t * 5f + k else 0f, lean = 0.06f)
            speech153(Offset(w * 0.42f, h * 0.62f), w * 0.05f, V153.Wave, f * seg(p, 0.45f, 0.55f), a.t, -1f)
            tagAt(a.tag("askaffara1", 0, 0), Offset(w * 0.62f, h * 0.36f), f * seg(p, 0.45f, 0.6f))
            tagAt(a.tag("askaffara1", 0, 1), Offset(w * 0.62f, h * 0.52f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("askaffara2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Gold)
            mercy153(Offset(w * 0.5f, h * 0.5f), f * seg(p, 0.1f, 0.3f), a.t)
            deeds(f, seg(p, 0.25f, 0.85f))
            tagAt(a.tag("askaffara2", 0, 1), Offset(w * 0.5f, h * 0.78f), f * seg(p, 0.7f, 0.85f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            seal153(Offset(w * 0.72f, h * 0.34f), w * 0.06f, 0, f * seg(p, 0.05f, 0.2f))
            seal153(Offset(w * 0.28f, h * 0.34f), w * 0.06f, 0, f * seg(p, 0.15f, 0.3f))
            val q = seg(p, 0.35f, 0.6f)
            drawLine(Qn.GoldLight, Offset(w * 0.72f, h * 0.42f), Offset(mix(w * 0.72f, w * 0.5f, q), mix(h * 0.42f, h * 0.6f, q)), w * 0.008f, StrokeCap.Round, alpha = f * 0.7f)
            drawLine(Qn.GoldLight, Offset(w * 0.28f, h * 0.42f), Offset(mix(w * 0.28f, w * 0.5f, q), mix(h * 0.42f, h * 0.6f, q)), w * 0.008f, StrokeCap.Round, alpha = f * 0.7f)
            qIcon("scroll", Offset(w * 0.5f, h * 0.66f), w * 0.1f, Qn.Parch, f * seg(p, 0.55f, 0.7f), a.t)
            tagAt(a.tag("askaffara3", 0, 0), Offset(w * 0.5f, h * 0.86f), f * seg(p, 0.6f, 0.75f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-illaman: the two verses on killing ═════════════
internal fun DrawScope.asillamanScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asillaman", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.5f, h * 0.2f), w * 0.07f, a, "asillaman0", 0, f * seg(p, 0.02f, 0.15f))
            qIcon("scroll", Offset(w * 0.7f, h * 0.56f), w * 0.12f, Qn.Parch, f * seg(p, 0.15f, 0.3f), a.t)
            tagAt(a.tag("asillaman0", 0, 1), Offset(w * 0.7f, h * 0.8f), f * seg(p, 0.15f, 0.3f))
            qIcon("scroll", Offset(w * 0.3f, h * 0.56f), w * 0.12f, Qn.Parch, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("asillaman0", 0, 2), Offset(w * 0.3f, h * 0.8f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            crowd145(w * 0.5f, h * 0.86f, 5, h * 0.16f, f)
            mood153(Offset(w * 0.5f, h * 0.76f), w * 0.3f, 2, 0.6f * f * (1f - seg(p, 0.5f, 0.7f)))
            speech153(Offset(w * 0.5f, h * 0.56f), w * 0.06f, V153.Wave, f * seg(p, 0.05f, 0.15f) * (1f - seg(p, 0.4f, 0.5f)), a.t, -1f)
            tagAt(a.tag("asillaman1", 0, 0), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.05f, 0.2f))
            mercy153(Offset(w * 0.5f, h * 0.7f), f * seg(p, 0.5f, 0.7f), a.t)
            tagAt(a.tag("asillaman1", 0, 1), Offset(w * 0.5f, h * 0.18f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            qIcon("book", Offset(w * 0.7f, h * 0.38f), w * 0.12f, Qn.Gold, f * seg(p, 0.02f, 0.15f), a.t)
            figure(Offset(w * 0.4f, h * 0.7f), h * 0.2f, Dim145, f)
            tagAt(a.tag("asillaman2", 0, 0), Offset(w * 0.7f, h * 0.6f), f * seg(p, 0.05f, 0.2f))
            flames153(f * seg(p, 0.5f, 0.7f), a.t)
            tagAt(a.tag("asillaman2", 0, 1), Offset(w * 0.4f, h * 0.24f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.72f, h * 0.3f), w * 0.07f, a, "asillaman3", 0, f * seg(p, 0.02f, 0.15f))
            moodFigure153(Offset(w * 0.4f, h * 0.84f), h * 0.22f, 0, f)
            mercy153(Offset(w * 0.4f, h * 0.7f), f * seg(p, 0.45f, 0.65f), a.t)
            tagAt(a.tag("asillaman3", 0, 1), Offset(w * 0.4f, h * 0.3f), f * seg(p, 0.4f, 0.55f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-ashara: «revealed about ten, I am one of them» ═════════════
internal fun DrawScope.asasharaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    fun ten(f: Float, q: Float, gold: Float) {
        for (k in 0 until 10) {
            val c = Offset(w * (0.82f - 0.07f * k), h * 0.62f)
            if (k / 10f > q) break
            figure(c + Offset(0f, h * 0.2f), h * 0.14f, lerpColor(Dim145, Qn.GoldLight, gold), f)
        }
    }
    film98(a, "asashara", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            plain97(Offset(w * 0.82f, h * 0.3f), w * 0.07f, a, "asashara0", 0, f * seg(p, 0.02f, 0.15f))
            ten(f, seg(p, 0.25f, 0.6f), 0f)
            softGlow(Offset(w * 0.82f, h * 0.74f), w * 0.06f, Qn.GoldLight, f * seg(p, 0.65f, 0.8f))
            tagAt(a.tag("asashara0", 0, 1), Offset(w * 0.5f, h * 0.36f), f * seg(p, 0.3f, 0.45f))
            tagAt(a.tag("asashara0", 0, 2), Offset(w * 0.62f, h * 0.18f), f * seg(p, 0.65f, 0.8f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            ten(f, 1f, seg(p, 0.45f, 0.75f))
            qIcon("book", Offset(w * 0.72f, h * 0.3f), w * 0.1f, Qn.Parch, f * seg(p, 0.02f, 0.15f), a.t)
            tagAt(a.tag("asashara1", 0, 0), Offset(w * 0.72f, h * 0.5f), f * seg(p, 0.05f, 0.2f))
            qIcon("scroll", Offset(w * 0.3f, h * 0.3f), w * 0.1f, Qn.Gold, f * seg(p, 0.35f, 0.5f), a.t)
            tagAt(a.tag("asashara1", 0, 1), Offset(w * 0.3f, h * 0.5f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
        },
        { p, f ->
            veil131(f)
            for (side in 0 until 2) {
                val x = w * (0.68f - 0.36f * side)
                for (k in 0 until 4) {
                    if (side == 1 && k == 2) continue
                    drawCircle(Qn.GoldLight, w * 0.02f, Offset(x, h * (0.24f + 0.13f * k)), alpha = f * seg(p, 0.05f + 0.05f * k, 0.15f + 0.05f * k), style = Stroke(w * 0.006f))
                }
            }
            seal153(Offset(w * 0.68f, h * 0.84f), w * 0.05f, 0, f * seg(p, 0.35f, 0.5f))
            tagAt(a.tag("asashara2", 0, 0), Offset(w * 0.86f, h * 0.5f), f * seg(p, 0.3f, 0.45f), ink = ArtInk.Gold)
            tagAt(a.tag("asashara2", 0, 1), Offset(w * 0.14f, h * 0.5f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Ember)
        },
        { p, f ->
            veil131(f)
            for (k in 0 until 2) softGlow(Offset(w * (0.38f + 0.24f * k), h * 0.45f), w * 0.09f, Qn.GoldLight, f * seg(p, 0.2f + 0.25f * k, 0.35f + 0.25f * k))
            for (k in 0 until 2) seal153(Offset(w * (0.38f + 0.24f * k), h * 0.45f), w * 0.05f, 0, f * seg(p, 0.2f + 0.25f * k, 0.35f + 0.25f * k))
            tagAt(a.tag("asashara3", 0, 0), Offset(w * 0.5f, h * 0.76f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
        },
    )
}

// ═════════════ as-abutalib: «I will testify for you by it before Allah» ═════════════
internal fun DrawScope.asabutalibScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    film98(a, "asabutalib", List(4) { Shot(Offset(w * 0.5f, h * 0.55f), 1f) },
        { night131(a) },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asabutalib0", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.1f, 0.25f), ink = ArtInk.Gold)
            val light = Offset(w * 0.28f, h * 0.5f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, -h * 0.12f))
            speech153(Offset(w * 0.38f, h * 0.5f), w * 0.06f, V153.Wave, f * seg(p, 0.05f, 0.15f), a.t, 1f)
            qIcon("bed", Offset(w * 0.66f, h * 0.66f), w * 0.2f, Qn.Parch, f, a.t)
            tagAt(a.tag("asabutalib0", 0, 1), Offset(w * 0.66f, h * 0.86f), f * seg(p, 0.5f, 0.65f))
        },
        { p, f ->
            veil131(f)
            qIcon("bed", Offset(w * 0.66f, h * 0.66f), w * 0.2f, Qn.Parch, f, a.t)
            crowd145(w * 0.2f, h * 0.88f, 4, h * 0.14f, f * seg(p, 0.05f, 0.2f))
            speech153(Offset(w * 0.34f, h * 0.62f), w * 0.06f, V153.Wave, f * seg(p, 0.1f, 0.2f), a.t, 1f)
            tagAt(a.tag("asabutalib1", 0, 0), Offset(w * 0.3f, h * 0.36f), f * seg(p, 0.05f, 0.2f), ink = ArtInk.Ember)
            tagAt(a.tag("asabutalib1", 0, 1), Offset(w * 0.66f, h * 0.36f), f * seg(p, 0.45f, 0.6f))
        },
        { p, f ->
            veil131(f)
            tagAt(a.tag("asabutalib2", 0, 0), Offset(w * 0.5f, h * 0.14f), f * seg(p, 0.45f, 0.6f), ink = ArtInk.Gold)
            val light = Offset(w * 0.3f, h * 0.54f)
            sirajMunir(light, w * 0.012f, f, a.t, nameAt = light + Offset(0f, h * 0.13f))
            grave153(Offset(w * 0.68f, h * 0.84f), w * 0.12f, f * seg(p, 0.05f, 0.2f))
            mood153(Offset(w * 0.3f, h * 0.54f), w * 0.1f, 0, f * seg(p, 0.1f, 0.3f))
            tagAt(a.tag("asabutalib2", 0, 1), Offset(w * 0.6f, h * 0.36f), f * seg(p, 0.1f, 0.25f))
        },
        { p, f ->
            veil131(f)
            val lamp = Offset(w * 0.74f, h * 0.36f)
            softGlow(lamp, w * 0.08f, Qn.GoldLight, f * seg(p, 0.02f, 0.15f))
            for (k in 0 until 6) drawCircle(Qn.GoldLight, w * 0.008f, Offset(w * (0.7f - 0.05f * k), h * (0.6f + 0.03f * k)), alpha = f * seg(p, 0.1f + 0.04f * k, 0.15f + 0.04f * k))
            tagAt(a.tag("asabutalib3", 0, 0), Offset(w * 0.72f, h * 0.84f), f * seg(p, 0.1f, 0.25f))
            revelation153(Offset(w * 0.26f, h * 0.52f), f * seg(p, 0.5f, 0.7f), a.t)
            qIcon("heart", Offset(w * 0.26f, h * 0.52f), w * 0.08f, Qn.Gold, f * seg(p, 0.5f, 0.65f), a.t)
            tagAt(a.tag("asabutalib3", 0, 1), Offset(w * 0.26f, h * 0.84f), f * seg(p, 0.55f, 0.7f), ink = ArtInk.Gold)
        },
    )
}
