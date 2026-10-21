package app.usulcards.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextLayoutResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/*
 * Wave 13, round 6 of the redraw (2026-10-21): «ألفاظ وأحكام تمس التوحيد». A shop where a man of authority swaps
 * the tags of lawful and unlawful, a writing board where «وَ» is struck out for «ثُمَّ», a town square on a windy
 * evening (the manners of the tongue), a merchant who swears and a commander who keeps Allah's covenant safe, and
 * a tent where a delegation praises him ﷺ and he answers.
 */

// ═════════════ 1. shelfshop: the tags swapped by a man of authority (itaat-ulama) ═════════════

private object ShopC13 {
    val Wall = Color(0xFFE6D8BC)
    val WallLow = Color(0xFFCDBB96)
    val Floor = Color(0xFF9A7A58)
    val Shelf = Color(0xFF7A5232)
    val ShelfDark = Color(0xFF4E3320)
    val Bread = Color(0xFFD9A55A)
    val Date = Color(0xFF6B3A1E)
    val Milk = Color(0xFFF2EEE4)
    val Wine = Color(0xFF6A1E2A)
    val Dice = Color(0xFFF4F0E8)
    val Halal = Color(0xFF2E7A4A)
    val Haram = Color(0xFFB0302A)
    val Robe = Color(0xFF1E2A5A)
    val Trim = Color(0xFFD8B05A)
    val Ink = Color(0xFF2A2026)
    val Back = Color(0xE6201810)
}

/**
 * A shop by day with two shelves. Beats: shelves (on the left bread, dates and milk under a green tag; on the right
 * jars of wine and dice under a red one), swaptags (a man in a fine robe crosses over and swaps the two tags),
 * obeyfollow (people come, take from the shelf his tag now calls lawful, push the bread away, and bow to him), dais
 * (a dais rises under him and they bow lower: their obedience in this is their worship of him), explained (his ﷺ light
 * at the door with ʿAdī's name; the tags fly back to where they belong, the dais sinks, and they stand up).
 */
internal fun DrawScope.shelfshopScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "shelves" to Shot(Offset(w * 0.5f, h * 0.5f), 1.1f),
        "swaptags" to Shot(Offset(w * 0.5f, h * 0.42f), 1.3f),
        "obeyfollow" to Shot(Offset(w * 0.55f, h * 0.6f), 1.15f),
        "dais" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "explained" to Shot(Offset(w * 0.6f, h * 0.48f), 1.1f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { shopRoom(a) }
}

private fun DrawScope.shopRoom(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val sv = a.b("shelves")
    val sw = a.b("swaptags")
    val fo = a.b("obeyfollow")
    val da = a.b("dais")
    val ex = a.b("explained")
    val floorY = h * 0.84f
    drawRect(Brush.verticalGradient(listOf(ShopC13.Wall, ShopC13.WallLow), endY = floorY))
    drawRect(ShopC13.Floor, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    // the door at the top right, open on daylight
    val doorC = Offset(w * 0.9f, floorY)
    drawPath(horseshoe(doorC.x, h * 0.36f, w * 0.06f, w * 0.05f, floorY), Color(0xFFFFF1C8))
    drawPath(horseshoe(doorC.x, h * 0.36f, w * 0.06f, w * 0.05f, floorY), ShopC13.ShelfDark, style = Stroke(2.4f * dp))
    // two shelves
    fun shelf(cx: Float) {
        for (k in 0 until 3) {
            val y = h * (0.36f + 0.16f * k)
            drawRect(ShopC13.Shelf, topLeft = Offset(cx - w * 0.14f, y), size = Size(w * 0.28f, h * 0.02f))
        }
        for (sd in listOf(-1f, 1f)) drawRect(ShopC13.ShelfDark, topLeft = Offset(cx + sd * w * 0.14f - w * 0.006f, h * 0.3f), size = Size(w * 0.012f, floorY - h * 0.3f))
    }
    val lx = w * 0.22f
    val rx = w * 0.62f
    shelf(lx)
    shelf(rx)
    // the lawful things on the left shelf
    for (k in 0 until 3) {
        drawOval(ShopC13.Bread, topLeft = Offset(lx - w * 0.12f + k * w * 0.08f, h * 0.31f), size = Size(w * 0.065f, h * 0.05f))
        drawArc(Color(0xFF8A5A2A), 180f, 180f, true, topLeft = Offset(lx - w * 0.11f + k * w * 0.08f, h * 0.48f), size = Size(w * 0.05f, h * 0.06f))
        for (d in 0 until 3) drawOval(ShopC13.Date, topLeft = Offset(lx - w * 0.105f + k * w * 0.08f + d * w * 0.012f, h * 0.495f), size = Size(w * 0.01f, h * 0.016f))
        drawRoundRect(ShopC13.Milk, topLeft = Offset(lx - w * 0.1f + k * w * 0.08f, h * 0.6f), size = Size(w * 0.03f, h * 0.08f), cornerRadius = CornerRadius(4f * dp))
    }
    // the unlawful things on the right shelf
    for (k in 0 until 3) {
        for (row in 0 until 2) {
            val x = rx - w * 0.1f + k * w * 0.08f
            val y = h * (0.36f + 0.16f * row)
            drawOval(ShopC13.Wine, topLeft = Offset(x - w * 0.018f, y - h * 0.08f), size = Size(w * 0.036f, h * 0.07f))
            drawRect(ShopC13.Wine, topLeft = Offset(x - w * 0.006f, y - h * 0.095f), size = Size(w * 0.012f, h * 0.02f))
        }
        val dc = Offset(rx - w * 0.1f + k * w * 0.08f, h * 0.66f)
        drawRoundRect(ShopC13.Dice, topLeft = dc - Offset(w * 0.014f, h * 0.022f), size = Size(w * 0.028f, h * 0.044f), cornerRadius = CornerRadius(3f * dp))
        for (p in 0 until 3) drawCircle(ShopC13.Ink, 1.2f * dp, dc + Offset((p - 1) * w * 0.006f, (p - 1) * h * 0.01f))
    }
    // the two tags: swapped by him, and flown back at the end
    val swap = easeInOut(seg(sw, 0.35f, 0.75f)) * (1f - easeInOut(seg(ex, 0.3f, 0.7f)))
    val tagL = Offset(lx, h * 0.23f)
    val tagR = Offset(rx, h * 0.23f)
    val show = seg(sv, 0.2f, 0.5f)
    val g = tagL + (tagR - tagL) * swap + Offset(0f, -h * 0.08f * bump(swap))
    val r = tagR + (tagL - tagR) * swap + Offset(0f, -h * 0.08f * bump(swap))
    for ((p, home) in listOf(g to tagL, r to tagR)) drawLine(ShopC13.ShelfDark, p + Offset(0f, h * 0.03f), Offset(p.x, h * 0.3f), 0.8f * dp, alpha = show)
    tagAt(a.tag("shelves", 0, 0), g, show, back = ShopC13.Halal, ink = Color.White)
    tagAt(a.tag("shelves", 0, 1), r, show, back = ShopC13.Haram, ink = Color.White)

    val tall = h * 0.36f
    // the man of authority: crosses to swap the tags, then stands at the middle; a dais rises under him
    val rise = easeOut(seg(da, 0f, 0.5f)) * (1f - easeOut(seg(ex, 0.4f, 0.8f)))
    val mx = when {
        sw > 0f && sw < 0.8f -> mix(w * 0.42f, w * 0.42f, 0f)
        else -> w * 0.42f
    }
    val mFoot = Offset(mx, floorY + h * 0.02f - h * 0.08f * rise)
    if (sv > 0.3f) {
        if (rise > 0f) drawRect(ShopC13.Shelf, topLeft = Offset(mx - w * 0.07f, floorY + h * 0.02f - h * 0.08f * rise), size = Size(w * 0.14f, h * 0.08f * rise + h * 0.01f))
        val reach = bump(seg(sw, 0.3f, 0.8f))
        figure(mFoot, tall * 1.05f, ShopC13.Robe, arms = 0.3f + 0.7f * reach, lean = -0.05f * reach)
        // his robe and its trim
        drawPath(Path().apply {
            moveTo(mFoot.x - tall * 0.1f, mFoot.y - tall * 0.78f); lineTo(mFoot.x + tall * 0.1f, mFoot.y - tall * 0.78f)
            lineTo(mFoot.x + tall * 0.17f, mFoot.y); lineTo(mFoot.x - tall * 0.17f, mFoot.y); close()
        }, ShopC13.Robe)
        drawLine(ShopC13.Trim, mFoot + Offset(-tall * 0.17f, -tall * 0.01f), mFoot + Offset(tall * 0.17f, -tall * 0.01f), 2f * dp)
        drawLine(ShopC13.Trim, mFoot + Offset(0f, -tall * 0.78f), mFoot + Offset(0f, 0f), 1f * dp, alpha = 0.7f)
        drawLine(ShopC13.ShelfDark, mFoot + Offset(tall * 0.22f, 0f), mFoot + Offset(tall * 0.2f, -tall * 1.05f), 2f * dp, StrokeCap.Round)
    }
    // the people who follow his tags
    if (fo > 0f) {
        val come = easeInOut(seg(fo, 0f, 0.35f))
        val take = easeOut(seg(fo, 0.35f, 0.55f)) * (1f - easeOut(seg(ex, 0.4f, 0.7f)))
        val bow = easeOut(seg(fo, 0.6f, 0.9f)) * (1f - easeOut(seg(ex, 0.5f, 0.85f)))
        val deeper = easeOut(seg(da, 0.3f, 0.8f)) * (1f - easeOut(seg(ex, 0.5f, 0.85f)))
        for (k in 0 until 3) {
            val foot = Offset(mix(w * (1.05f + 0.06f * k), w * (0.62f + 0.08f * k), come), floorY + h * 0.08f)
            if (bow > 0.3f) {
                w8Worshipper(foot, tall, ShopC13.Ink, 0f, min(1f, 0.4f * bow + 0.6f * deeper), 1f)
            } else {
                figure(foot, tall, ShopC13.Ink, walk = if (come < 1f) t * 6f + k else 0f, arms = 0.2f + 0.7f * take)
            }
            if (take > 0.3f && k < 2) {
                val jar = foot + Offset(-tall * 0.3f, -tall * 0.55f)
                drawOval(ShopC13.Wine, topLeft = jar - Offset(w * 0.014f, h * 0.025f), size = Size(w * 0.028f, h * 0.05f), alpha = take)
            }
        }
        // the bread pushed away
        if (take > 0.3f) drawOval(ShopC13.Bread, topLeft = Offset(lx + w * 0.16f, floorY + h * 0.03f), size = Size(w * 0.06f, h * 0.045f), alpha = take)
    }
    // explained: his light at the door with ʿAdī's name
    if (ex > 0f) {
        val lc = doorC + Offset(0f, -h * 0.34f)
        softGlow(lc, w * 0.12f, Color(0xFFFFC870), 0.6f * easeOut(seg(ex, 0f, 0.25f)))
        sirajMunir(lc, 5.2f * dp, easeOut(seg(ex, 0f, 0.25f)), t, nameAt = lc + Offset(-w * 0.13f, -h * 0.02f))
        nameRoundel(doorC + Offset(-w * 0.07f, -h * 0.13f), w * 0.065f, a.big("explained", 0, 0), Roundel.PlainFill, Roundel.PlainRim, easeOut(seg(ex, 0.15f, 0.35f)))
    }
}

// ═════════════ 2. lawh: «وَ» struck out, «ثُمَّ» written in (alfadh-shirk) ═════════════

private object LawhC13 {
    val Wall = Color(0xFFDCCFB4)
    val WallLow = Color(0xFFC6B48E)
    val Board = Color(0xFFEFE2C2)
    val BoardEdge = Color(0xFF8A6440)
    val BoardDark = Color(0xFF5A3E26)
    val Ink = Color(0xFF2A1A0E)
    val Wrong = Color(0xFFC0392B)
    val Right = Color(0xFF2E7A4A)
    val Pot = Color(0xFF4A3A2A)
    val Reed = Color(0xFFB8945A)
}

/** The box, in the drawn picture, of [word] inside [tl] drawn centred at [c] at scale [sc] (null if not found). */
private fun wordBox13(tl: TextLayoutResult?, word: String, c: Offset, sc: Float): Rect? {
    if (tl == null) return null
    val text = tl.layoutInput.text.text
    fun bare(s: String) = s.filter { it.code !in 0x064B..0x065F && it.code != 0x0670 }
    var i = text.indexOf(word)
    var len = word.length
    if (i < 0) {
        // the scene may be shown without harakat: find the bare word, keeping the original indices
        val map = ArrayList<Int>()
        val sb = StringBuilder()
        for ((k, ch) in text.withIndex()) if (ch.code !in 0x064B..0x065F && ch.code != 0x0670) { sb.append(ch); map.add(k) }
        val j = sb.indexOf(bare(word))
        if (j < 0) return null
        i = map[j]
        len = map[min(map.size - 1, j + bare(word).length - 1)] - i + 1
    }
    var box: Rect? = null
    for (k in i until min(text.length, i + len)) {
        val b = tl.getBoundingBox(k)
        box = box?.let { Rect(min(it.left, b.left), min(it.top, b.top), max(it.right, b.right), max(it.bottom, b.bottom)) } ?: b
    }
    val bb = box ?: return null
    val o = c - Offset(tl.size.width / 2f, tl.size.height / 2f)
    fun map(p: Offset) = c + (o + p - c) * sc
    return Rect(map(bb.topLeft), map(bb.bottomRight))
}

/**
 * A wooden writing board, as in the old Qur'an schools, leaning on the wall, with an inkpot and a reed pen. Beats:
 * oath (the pen writes «وَحَيَاتِكَ», an oath by other than Allah, and it is struck through in red), wawa (it
 * writes «مَا شَاءَ اللَّهُ وَشِئْتَ», and the «وَشِئْتَ» that sets a man beside Allah is ringed in red), thumma
 * (the right words underneath: «مَا شَاءَ اللَّهُ ثُمَّ شَاءَ فُلَانٌ», «ثُمَّ» in green), lawla (and «وَلَوْلَا
 * اللَّهُ ثُمَّ فُلَانٌ», «ثُمَّ» in green).
 */
internal fun DrawScope.lawhScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "oath" to Shot(Offset(w * 0.5f, h * 0.3f), 1.3f),
        "wawa" to Shot(Offset(w * 0.5f, h * 0.42f), 1.25f),
        "thumma" to Shot(Offset(w * 0.5f, h * 0.55f), 1.2f),
        "lawla" to Shot(Offset(w * 0.5f, h * 0.5f), 1.05f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.1f) { lawhBoard(a) }
}

private fun DrawScope.lawhBoard(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    drawRect(Brush.verticalGradient(listOf(LawhC13.Wall, LawhC13.WallLow)))
    lightShafts(Offset(-w * 0.05f, -h * 0.05f), PI.toFloat() * 0.25f, 0.4f, h * 1.2f, Color(0xFFFFF4D6), 0.5f, t, 4)
    // the board, with its rounded top and handle
    val bl = w * 0.12f
    val br = w * 0.88f
    val bt = h * 0.12f
    val bb = h * 0.9f
    drawRoundRect(Color.Black, topLeft = Offset(bl + 4f * dp, bt + 6f * dp), size = Size(br - bl, bb - bt), cornerRadius = CornerRadius(14f * dp), alpha = 0.25f)
    drawRoundRect(LawhC13.BoardEdge, topLeft = Offset(bl, bt), size = Size(br - bl, bb - bt), cornerRadius = CornerRadius(14f * dp))
    drawRoundRect(LawhC13.Board, topLeft = Offset(bl + w * 0.015f, bt + h * 0.025f), size = Size(br - bl - w * 0.03f, bb - bt - h * 0.05f), cornerRadius = CornerRadius(10f * dp))
    drawArc(LawhC13.BoardEdge, 180f, 180f, true, topLeft = Offset(w * 0.44f, bt - h * 0.07f), size = Size(w * 0.12f, h * 0.14f))
    drawCircle(LawhC13.Board, w * 0.018f, Offset(w * 0.5f, bt - h * 0.01f))
    for (k in 0 until 12) drawLine(LawhC13.BoardEdge, Offset(bl + w * 0.04f, bt + h * (0.06f + 0.065f * k)), Offset(br - w * 0.04f, bt + h * (0.06f + 0.065f * k) + h * 0.004f), 0.6f * dp, alpha = 0.12f)
    // the inkpot and the reed pen on the ledge
    drawRoundRect(LawhC13.Pot, topLeft = Offset(w * 0.86f, h * 0.88f), size = Size(w * 0.05f, h * 0.07f), cornerRadius = CornerRadius(6f * dp))

    // the lines, written right to left by the reed as their beats come
    val lines = listOf(
        Triple("oath", h * 0.26f, LawhC13.Ink),
        Triple("wawa", h * 0.42f, LawhC13.Ink),
        Triple("thumma", h * 0.6f, LawhC13.Right),
        Triple("lawla", h * 0.76f, LawhC13.Right),
    )
    var penAt: Offset? = null
    for ((kind, y, ink) in lines) {
        val p = a.b(kind)
        if (p <= 0f) continue
        val tl = a.big(kind, 0, 0) ?: continue
        val c = Offset(w * 0.5f, y)
        val sc = min(1.05f, (br - bl - w * 0.12f) / max(1f, tl.size.width.toFloat()))
        val tw = tl.size.width * sc
        val write = easeInOut(seg(p, 0f, 0.55f))
        val right = c.x + tw / 2f
        val left = right - tw * write
        clipRect(left, y - tl.size.height * sc, right + 2f, y + tl.size.height * sc) { bigText(tl, c, sc, ink, 1f) }
        if (write < 1f) penAt = Offset(left, y)
        when (kind) {
            "oath" -> {
                val strike = easeOut(seg(p, 0.65f, 0.85f))
                if (strike > 0f) drawLine(LawhC13.Wrong, Offset(right + w * 0.01f, y), Offset(right + w * 0.01f - (tw + w * 0.02f) * strike, y - h * 0.01f), 3f * dp, StrokeCap.Round)
            }
            "wawa" -> {
                val ring = easeOut(seg(p, 0.6f, 0.85f))
                val box = wordBox13(tl, "وَشِئْتَ", c, sc)
                if (box != null && ring > 0f) {
                    drawOval(LawhC13.Wrong, topLeft = box.topLeft - Offset(w * 0.01f, h * 0.01f), size = Size(box.width + w * 0.02f, box.height + h * 0.02f), alpha = ring, style = Stroke(2.4f * dp))
                    drawLine(LawhC13.Wrong, Offset(box.right + w * 0.01f, box.center.y), Offset(box.right + w * 0.01f - (box.width + w * 0.02f) * seg(p, 0.85f, 1f), box.center.y), 2.4f * dp, StrokeCap.Round)
                }
            }
            else -> {
                val mark = easeOut(seg(p, 0.6f, 0.85f))
                val box = wordBox13(tl, "ثُمَّ", c, sc)
                if (box != null && mark > 0f) {
                    drawRoundRect(LawhC13.Right, topLeft = box.topLeft - Offset(w * 0.006f, h * 0.006f), size = Size(box.width + w * 0.012f, box.height + h * 0.012f), cornerRadius = CornerRadius(6f * dp), alpha = 0.2f * mark)
                    drawLine(LawhC13.Right, Offset(box.left, box.bottom + h * 0.012f), Offset(box.right, box.bottom + h * 0.012f), 2.4f * dp, StrokeCap.Round, alpha = mark)
                }
            }
        }
    }
    // the reed pen: at the head of the line being written, or at rest on the ledge
    val tip = penAt ?: Offset(w * 0.8f, h * 0.93f)
    val butt = if (penAt != null) tip + Offset(w * 0.06f, -h * 0.12f) else tip + Offset(w * 0.1f, -h * 0.01f)
    drawLine(LawhC13.Reed, tip, butt, 3f * dp, StrokeCap.Round)
    drawLine(LawhC13.Ink, tip, tip + (butt - tip) * 0.08f, 3f * dp, StrokeCap.Round)
}

// ═════════════ 3. plaza: the manners of the tongue (adab-alfadh) ═════════════

private object PlazaC13 {
    val SkyTop = Color(0xFF6A7AA8)
    val SkyLow = Color(0xFFF2C890)
    val House = Color(0xFFE2CCA4)
    val HouseShade = Color(0xFFC4AC82)
    val Ground = Color(0xFFCDB488)
    val Ink = Color(0xFF2A2026)
    val Awning = Color(0xFFB8463A)
    val Dust = Color(0xFFE6D2A8)
    val Garden = Color(0xFF4E8A4A)
    val GardenLight = Color(0xFF8ACB72)
    val Coin = Color(0xFFE6B93E)
    val Glow = Color(0xFFFFF0C8)
}

/**
 * A town square on a windy evening. Beats: gust (the wind tears at an awning and his cloak; he shakes his fist at the
 * sky, a red sign over it, then lowers it and opens his palms to ask), greet (he lifts a hand to greet the sky, a red
 * sign again; he turns and greets his neighbour, hand on heart), resolve (he asks with his hands half up, wavering,
 * then raises them high and firm), askjanna (he asks for the garden, its gate shown far off as a labelled symbol,
 * while coins and goods drift past unasked), give (one in need asks him by Allah and he gives at once).
 */
internal fun DrawScope.plazaScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "gust" to Shot(Offset(w * 0.45f, h * 0.55f), 1.25f),
        "greet" to Shot(Offset(w * 0.5f, h * 0.58f), 1.3f),
        "resolve" to Shot(Offset(w * 0.45f, h * 0.55f), 1.45f),
        "askjanna" to Shot(Offset(w * 0.55f, h * 0.45f), 1.1f),
        "give" to Shot(Offset(w * 0.5f, h * 0.6f), 1.3f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { plazaSquare(a) }
}

private fun DrawScope.plazaSquare(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val gu = a.b("gust")
    val gr = a.b("greet")
    val rs = a.b("resolve")
    val aj = a.b("askjanna")
    val gv = a.b("give")
    val windy = (if (gu > 0f) 1f else 0f) * (1f - easeOut(seg(gu, 0.7f, 1f)) * 0.7f) * (1f - seg(gr, 0f, 0.3f))
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(PlazaC13.SkyTop, PlazaC13.SkyLow), endY = ground), size = Size(w, ground))
    // the garden's gate, far off, shown as a labelled symbol when he asks for it
    val jan = easeOut(seg(aj, 0.1f, 0.5f))
    if (jan > 0f) {
        val gc = Offset(w * 0.78f, h * 0.24f)
        softGlow(gc, w * 0.12f, PlazaC13.GardenLight, 0.6f * jan)
        drawPath(horseshoe(gc.x, gc.y, w * 0.05f, w * 0.04f, gc.y + h * 0.14f), PlazaC13.Garden, alpha = jan)
        for (k in 0 until 5) drawCircle(PlazaC13.GardenLight, w * 0.012f, gc + Offset((k - 2f) * w * 0.018f, h * 0.08f), alpha = jan)
        tagAt(a.tag("askjanna"), gc + Offset(0f, -h * 0.13f), jan, back = Color(0xE6203018), ink = PlazaC13.GardenLight)
    }
    // the houses round the square and an awning
    for ((k, x) in listOf(0.08f, 0.3f, 0.62f, 0.9f).withIndex()) {
        val top = ground - h * (0.34f + 0.06f * (k % 2))
        drawRect(if (k % 2 == 0) PlazaC13.House else PlazaC13.HouseShade, topLeft = Offset(w * x - w * 0.1f, top), size = Size(w * 0.2f, ground - top))
        drawPath(horseshoe(w * x, ground - h * 0.12f, w * 0.022f, w * 0.018f, ground), Color(0xFF5A4030))
    }
    val flap = windy * sin(t * 12f) * h * 0.02f
    drawPath(Path().apply { moveTo(w * 0.2f, h * 0.55f); lineTo(w * 0.4f, h * 0.55f); lineTo(w * 0.42f, h * 0.62f + flap); lineTo(w * 0.18f, h * 0.62f - flap); close() }, PlazaC13.Awning)
    drawRect(PlazaC13.Ground, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    // the wind and its dust
    if (windy > 0f) for (k in 0 until 18) {
        val f = fract(t * 1.2f + k / 18f)
        val y = h * (0.3f + 0.6f * w8Rnd(k, 3.1f))
        drawLine(Color.White, Offset(w * 1.1f - w * 1.3f * f, y), Offset(w * 1.1f - w * 1.3f * f + w * 0.08f, y - h * 0.01f), 1.2f * dp, StrokeCap.Round, alpha = 0.6f * windy)
        drawCircle(PlazaC13.Dust, 1.6f * dp, Offset(w * 1.05f - w * 1.2f * f, ground - h * 0.08f * w8Rnd(k, 7.7f)), alpha = 0.7f * windy)
    }

    val tall = h * 0.34f
    val me = Offset(w * 0.44f, h * 0.92f)
    val sh = me + Offset(0f, -tall * 0.76f)
    val sw0 = max(1.7f * dp, tall * 0.065f)
    // what his hands do in each beat
    val fist = easeOut(seg(gu, 0.15f, 0.35f)) * (1f - easeOut(seg(gu, 0.55f, 0.7f)))
    val palmsUp = easeOut(seg(gu, 0.6f, 0.8f)) * (1f - seg(gr, 0f, 0.15f))
    val greetUp = easeOut(seg(gr, 0.05f, 0.25f)) * (1f - easeOut(seg(gr, 0.45f, 0.6f)))
    val greetNb = easeOut(seg(gr, 0.55f, 0.75f)) * (1f - seg(rs, 0f, 0.15f))
    val waver = easeOut(seg(rs, 0f, 0.2f)) * (1f - easeOut(seg(rs, 0.5f, 0.65f)))
    val firm = easeOut(seg(rs, 0.55f, 0.8f)) * (1f - seg(gv, 0f, 0.2f))
    val askG = if (aj > 0f) firm.coerceAtLeast(easeOut(seg(aj, 0f, 0.2f))) * (1f - seg(gv, 0f, 0.2f)) else 0f
    val giveA = easeOut(seg(gv, 0.4f, 0.6f))
    // the body without arms, then the arms of the moment
    val hip = me - Offset(0f, tall * 0.45f)
    val head = sh + Offset(0f, -tall * 0.15f)
    drawLine(PlazaC13.Ink, hip, me + Offset(-tall * 0.075f, 0f), sw0, StrokeCap.Round)
    drawLine(PlazaC13.Ink, hip, me + Offset(tall * 0.075f, 0f), sw0, StrokeCap.Round)
    drawLine(PlazaC13.Ink, hip, sh, sw0, StrokeCap.Round)
    drawCircle(PlazaC13.Ink, tall * 0.1f, head)
    // his cloak in the wind
    if (windy > 0f) drawPath(Path().apply { moveTo(sh.x, sh.y); quadraticBezierTo(sh.x - tall * 0.25f, sh.y + tall * 0.2f + flap, sh.x - tall * 0.35f + flap, sh.y + tall * 0.1f) }, Color(0xFF6A3A3A), style = Stroke(3f * dp, cap = StrokeCap.Round))
    fun arm(to: Offset, alpha: Float = 1f) = drawLine(PlazaC13.Ink, sh, to, sw0 * 0.9f, StrokeCap.Round, alpha = alpha)
    val restL = sh + Offset(-tall * 0.11f, tall * 0.28f)
    val restR = sh + Offset(tall * 0.11f, tall * 0.28f)
    val upL = sh + Offset(-tall * 0.14f, -tall * 0.18f)
    val upR = sh + Offset(tall * 0.14f, -tall * 0.18f)
    val halfL = sh + Offset(-tall * 0.16f, tall * 0.02f)
    val halfR = sh + Offset(tall * 0.16f, tall * 0.02f)
    when {
        fist > 0.05f -> {
            arm(restL + (sh + Offset(-tall * 0.05f, -tall * 0.3f) - restL) * fist)
            arm(restR)
            drawCircle(PlazaC13.Ink, tall * 0.035f, restL + (sh + Offset(-tall * 0.05f, -tall * 0.3f) - restL) * fist)
            noSign11pub(sh + Offset(-tall * 0.05f, -tall * 0.36f), tall * 0.12f, fist)
        }
        greetUp > 0.05f -> {
            arm(restL + (sh + Offset(-tall * 0.06f, -tall * 0.28f) - restL) * greetUp)
            arm(restR)
            noSign11pub(sh + Offset(-tall * 0.06f, -tall * 0.34f), tall * 0.12f, seg(gr, 0.2f, 0.35f) * greetUp)
        }
        greetNb > 0.05f -> {
            arm(sh + Offset(-tall * 0.02f, tall * 0.12f))
            arm(sh + Offset(-tall * 0.24f, tall * 0.1f))
        }
        waver > 0.05f -> {
            val wob = sin(t * 9f) * tall * 0.03f
            arm(halfL + Offset(0f, wob))
            arm(halfR + Offset(0f, -wob))
        }
        giveA > 0.05f -> {
            arm(restL + (sh + Offset(-tall * 0.3f, tall * 0.08f) - restL) * giveA)
            arm(restR)
        }
        else -> {
            val up = max(palmsUp, max(firm, askG))
            arm(restL + (upL - restL) * up)
            arm(restR + (upR - restR) * up)
            if (up > 0.5f && (firm > 0.5f || askG > 0.5f)) softGlow(sh + Offset(0f, -tall * 0.2f), tall * 0.18f, PlazaC13.Glow, 0.6f * up)
        }
    }
    // the neighbour he greets
    if (gr > 0.4f && gv <= 0f) {
        val nb = Offset(w * 0.3f, h * 0.92f)
        withTransform({ scale(-1f, 1f, pivot = nb) }) { figure(nb, tall, PlazaC13.Ink, alpha = seg(gr, 0.4f, 0.55f) * (1f - seg(rs, 0f, 0.2f)), arms = 0.6f * greetNb) }
        if (greetNb > 0.5f) softGlow(Offset(w * 0.37f, h * 0.92f - tall * 0.66f), tall * 0.15f, PlazaC13.Glow, 0.5f * greetNb)
    }
    // coins and goods drift by, unasked
    if (aj > 0.2f) for (k in 0 until 6) {
        val f = fract(t * 0.12f + k / 6f)
        val c = Offset(mix(w * 0.95f, w * 0.05f, f), h * (0.32f + 0.08f * (k % 3)))
        if (k % 2 == 0) drawCircle(PlazaC13.Coin, w * 0.012f, c, alpha = 0.7f * seg(aj, 0.2f, 0.4f))
        else drawRect(Color(0xFF9A6A3A), topLeft = c - Offset(w * 0.012f, h * 0.012f), size = Size(w * 0.024f, h * 0.024f), alpha = 0.7f * seg(aj, 0.2f, 0.4f))
    }
    // his asking reaches the garden's gate
    if (jan > 0.3f && gv <= 0f) {
        val from = sh + Offset(0f, -tall * 0.3f)
        val to = Offset(w * 0.78f, h * 0.36f)
        drawPath(cubicPart(from, from + Offset(0f, -h * 0.2f), to + Offset(-w * 0.1f, 0f), to, 0f, seg(aj, 0.3f, 0.7f)), PlazaC13.Glow, alpha = 0.8f, style = Stroke(1.6f * dp, cap = StrokeCap.Round))
    }
    // give: one in need asks him by Allah, and he gives at once
    if (gv > 0f) {
        val come = easeInOut(seg(gv, 0f, 0.35f))
        val nf = Offset(mix(w * 0.05f, w * 0.28f, come), h * 0.92f)
        withTransform({ scale(-1f, 1f, pivot = nf) }) { figure(nf, tall * 0.92f, Color(0xFF4A3A30), walk = if (come < 1f) t * 5f else 0f, arms = 0.8f, lean = 0.1f) }
        val handoff = nf + Offset(tall * 0.28f, -tall * 0.62f)
        val bread = (sh + Offset(-tall * 0.3f, tall * 0.08f)) + (handoff - (sh + Offset(-tall * 0.3f, tall * 0.08f))) * easeInOut(seg(gv, 0.55f, 0.85f))
        drawOval(Color(0xFFD9A55A), topLeft = bread - Offset(w * 0.02f, h * 0.014f), size = Size(w * 0.04f, h * 0.028f), alpha = seg(gv, 0.45f, 0.55f))
        if (gv > 0.85f) softGlow(handoff, tall * 0.15f, PlazaC13.Glow, 0.6f)
    }
}

// ═════════════ 4. stall: the merchant who swears, and the covenant kept safe (taazim-yamin) ═════════════

private object StallC13 {
    val SkyTop = Color(0xFF3A3A6A)
    val SkyLow = Color(0xFFE8A070)
    val Cloth = listOf(Color(0xFF8A2A3A), Color(0xFF2A5A7A), Color(0xFFD8A040), Color(0xFF3A7A4A))
    val Wood = Color(0xFF6A4428)
    val Ink = Color(0xFF1E1620)
    val Coin = Color(0xFFE6B93E)
    val Dust = Color(0xFF9A8A70)
    val Hill = Color(0xFF6A5A6A)
    val Fort = Color(0xFFB8A27A)
    val FortDark = Color(0xFF8A7454)
    val Bronze = Color(0xFFB07A3A)
    val Gold = Color(0xFFF2C45A)
    val Back = Color(0xE6140E12)
}

/**
 * A cloth merchant's stall at dusk, a fortress on a far hill. Beats: oathsell (he raises his hand and swears with
 * every sale; buyers crowd in, bolts of cloth fly off and coins drop into his box), dust (the coins in his box
 * crumble to dust and blow away), alone (he swears and swears, but the buyers drift off; his stall goes dark around
 * him, silent), fortress (at the fortress gate a commander keeps a golden seal, Allah's covenant, closed in its case,
 * and holds out his own bronze seal to the envoy), keepseal (the bronze seal is given; the case stays shut and
 * glowing, kept safe).
 */
internal fun DrawScope.stallScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "oathsell" to Shot(Offset(w * 0.36f, h * 0.58f), 1.3f),
        "dust" to Shot(Offset(w * 0.34f, h * 0.66f), 1.7f),
        "alone" to Shot(Offset(w * 0.34f, h * 0.58f), 1.35f),
        "fortress" to Shot(Offset(w * 0.8f, h * 0.48f), 1.5f),
        "keepseal" to Shot(Offset(w * 0.8f, h * 0.5f), 1.65f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.14f) { stallDusk(a) }
}

private fun DrawScope.stallDusk(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val os = a.b("oathsell")
    val du = a.b("dust")
    val al = a.b("alone")
    val fo = a.b("fortress")
    val ks = a.b("keepseal")
    val ground = h * 0.82f
    drawRect(Brush.verticalGradient(listOf(StallC13.SkyTop, StallC13.SkyLow), endY = ground), size = Size(w, ground))
    // the far hill and its fortress
    drawPath(Path().apply { moveTo(w * 0.55f, ground); quadraticBezierTo(w * 0.8f, h * 0.34f, w * 1.05f, ground); close() }, StallC13.Hill)
    val fx = w * 0.82f
    val fTop = h * 0.36f
    drawRect(StallC13.Fort, topLeft = Offset(fx - w * 0.09f, fTop), size = Size(w * 0.18f, h * 0.16f))
    for (k in 0 until 7) drawRect(StallC13.Fort, topLeft = Offset(fx - w * 0.09f + k * w * 0.027f, fTop - h * 0.025f), size = Size(w * 0.016f, h * 0.025f))
    for (x in listOf(-0.1f, 0.1f)) drawRect(StallC13.FortDark, topLeft = Offset(fx + w * x - w * 0.02f, fTop - h * 0.06f), size = Size(w * 0.04f, h * 0.22f))
    drawPath(horseshoe(fx, fTop + h * 0.09f, w * 0.025f, w * 0.02f, fTop + h * 0.16f), Color(0xFF2A2030))
    drawRect(Color(0xFF3A3040), topLeft = Offset(0f, ground), size = Size(w, h - ground))
    // the stall
    val dark = easeOut(seg(al, 0.3f, 0.9f))
    val sl = w * 0.12f
    val sr = w * 0.56f
    drawRect(StallC13.Wood, topLeft = Offset(sl, ground - h * 0.14f), size = Size(sr - sl, h * 0.14f))
    for (x in listOf(sl, sr)) drawLine(StallC13.Wood, Offset(x, ground), Offset(x, h * 0.3f), 3f * dp)
    drawPath(Path().apply { moveTo(sl - w * 0.02f, h * 0.3f); lineTo(sr + w * 0.02f, h * 0.3f); lineTo(sr, h * 0.38f); lineTo(sl, h * 0.38f); close() }, StallC13.Cloth[0])
    // bolts of cloth on the counter; they fly off with the sales
    val sold = (6f * easeOut(seg(os, 0.2f, 0.95f))).toInt()
    for (k in 0 until 6) {
        if (k < sold) continue
        drawRoundRect(StallC13.Cloth[k % 4], topLeft = Offset(sl + w * 0.03f + k * w * 0.065f, ground - h * 0.2f), size = Size(w * 0.055f, h * 0.06f), cornerRadius = CornerRadius(8f * dp))
    }
    // his money box: coins drop in, then crumble to dust and blow away
    val box = Offset(sr - w * 0.06f, ground - h * 0.14f)
    drawRect(Color(0xFF4A2E1A), topLeft = box - Offset(w * 0.04f, h * 0.05f), size = Size(w * 0.08f, h * 0.05f))
    val crumble = easeOut(seg(du, 0.1f, 0.7f))
    val coins = (8f * easeOut(seg(os, 0.25f, 1f))).toInt()
    for (k in 0 until coins) {
        val p = box + Offset((k % 4 - 1.5f) * w * 0.014f, -h * 0.06f - h * 0.012f * (k / 4))
        drawCircle(lerpColor(StallC13.Coin, StallC13.Dust, crumble), w * 0.008f * (1f - 0.5f * crumble), p, alpha = 1f - seg(du, 0.6f, 0.9f))
    }
    if (du > 0.3f) for (k in 0 until 14) {
        val f = fract(t * 0.6f + k / 14f)
        drawCircle(StallC13.Dust, 1.4f * dp, box + Offset(w * 0.25f * f, -h * 0.08f - h * 0.15f * f + h * 0.02f * sin(f * 9f + k)), alpha = (1f - f) * seg(du, 0.3f, 0.5f) * (1f - seg(al, 0.5f, 1f)))
    }
    if (dark > 0f) drawRect(Color.Black, topLeft = Offset(sl - w * 0.05f, h * 0.28f), size = Size(sr - sl + w * 0.1f, ground - h * 0.28f + h * 0.12f), alpha = 0.45f * dark)

    val tall = h * 0.32f
    // the merchant: his right hand up as he swears
    val mFoot = Offset(w * 0.34f, ground - h * 0.0f)
    val swearUp = if (os > 0.1f && fo <= 0f) 0.5f + 0.5f * sin(t * 4f) else 0f
    figure(mFoot, tall, StallC13.Ink, arms = 0.2f)
    if (swearUp > 0f) {
        val sh = mFoot + Offset(0f, -tall * 0.76f)
        drawLine(StallC13.Ink, sh, sh + Offset(tall * 0.12f, -tall * (0.1f + 0.12f * swearUp)), max(1.7f * dp, tall * 0.065f), StrokeCap.Round)
        drawLine(StallC13.Ink, sh + Offset(tall * 0.12f, -tall * (0.1f + 0.12f * swearUp)), sh + Offset(tall * 0.13f, -tall * (0.2f + 0.14f * swearUp)), max(1.5f * dp, tall * 0.05f), StrokeCap.Round)
    }
    // the buyers: crowding in, then drifting away
    val crowd = easeOut(seg(os, 0f, 0.3f)) * (1f - easeOut(seg(al, 0.2f, 0.8f)))
    for (k in 0 until 3) {
        val foot = Offset(w * (0.12f + 0.08f * k) - w * 0.1f * seg(al, 0.2f, 0.8f), ground + h * 0.08f)
        withTransform({ scale(-1f, 1f, pivot = foot) }) { figure(foot, tall * 0.95f, Color(0xFF3A3040), alpha = crowd, arms = if (os in 0.2f..0.95f) 0.6f else 0f) }
    }
    // the fortress gate: the golden seal kept in its case, his own bronze seal held out
    if (fo > 0f) {
        val gate = Offset(fx, fTop + h * 0.16f)
        val cFoot = gate + Offset(-w * 0.05f, h * 0.01f)
        val small = tall * 0.45f
        figure(cFoot, small, StallC13.Ink, arms = 0.4f + 0.5f * easeOut(seg(ks, 0f, 0.4f)))
        val eFoot = gate + Offset(-w * 0.13f, h * 0.01f)
        withTransform({ scale(-1f, 1f, pivot = eFoot) }) { figure(eFoot, small, Color(0xFF3A3040), arms = 0.5f) }
        val caseC = cFoot + Offset(w * 0.035f, -small * 0.5f)
        val shut = 1f
        drawRoundRect(Color(0xFF5A3A22), topLeft = caseC - Offset(w * 0.016f, h * 0.012f), size = Size(w * 0.032f, h * 0.024f), cornerRadius = CornerRadius(3f * dp))
        softGlow(caseC, w * 0.03f, StallC13.Gold, (0.5f + 0.3f * sin(t * 2f)) * shut * seg(fo, 0.2f, 0.5f) * (1f + 0.5f * seg(ks, 0.3f, 0.8f)))
        drawCircle(StallC13.Gold, w * 0.006f, caseC, alpha = seg(fo, 0.2f, 0.5f))
        tagAt(a.tag("fortress"), caseC + Offset(w * 0.04f, -h * 0.07f), seg(fo, 0.3f, 0.6f), back = StallC13.Back, ink = StallC13.Gold)
        val handed = easeInOut(seg(ks, 0.2f, 0.7f))
        val sealFrom = cFoot + Offset(-small * 0.28f, -small * 0.62f)
        val sealTo = eFoot + Offset(small * 0.28f, -small * 0.62f)
        val seal = sealFrom + (sealTo - sealFrom) * handed
        drawCircle(StallC13.Bronze, w * 0.008f, seal, alpha = seg(fo, 0.4f, 0.6f))
        tagAt(a.tag("keepseal"), seal + Offset(0f, h * 0.07f), seg(ks, 0.3f, 0.6f), back = StallC13.Back, ink = Color(0xFFE8C08A))
    }
}

// ═════════════ 5. tent: «you are our master»; «the Master is Allah» (sayyid) ═════════════

private object TentC13 {
    val Sky = Color(0xFFA8D0EE)
    val SkyLow = Color(0xFFF4E8CE)
    val Sand = Color(0xFFDCC28E)
    val Tent = Color(0xFF6A4A3A)
    val TentLight = Color(0xFF8A6A54)
    val Mat = Color(0xFF8A3A34)
    val MatLine = Color(0xFFD9A75A)
    val Ink = Color(0xFF2A2026)
    val Smoke = Color(0xFF2E2A34)
    val Back = Color(0xE6201810)
}

/**
 * A tent open to the desert by day; his ﷺ light sits on the mat among his companions (plain roundels), on the same
 * level as them. Beats: praise (a delegation comes in, bowing a little; their words on a slip: «you are our
 * master»), reply (his answer on a slip; they straighten), whisper (dark smoke creeps up behind them toward their
 * ears and their praise swells; then it thins and their words come plain), rank (one pushes a raised seat toward
 * his light to lift him; it does not rise: he stays where he is, his station named under him).
 */
internal fun DrawScope.tentScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val shots = shotsFor(a, mapOf(
        "praise" to Shot(Offset(w * 0.4f, h * 0.5f), 1.25f),
        "reply" to Shot(Offset(w * 0.55f, h * 0.48f), 1.25f),
        "whisper" to Shot(Offset(w * 0.3f, h * 0.55f), 1.35f),
        "station" to Shot(Offset(w * 0.62f, h * 0.55f), 1.35f),
    ))
    followBeats(a, shots, Shot(Offset(w * 0.5f, h * 0.5f), 1f), dim = 0.12f) { tentMajlis(a) }
}

private fun DrawScope.tentMajlis(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val pr = a.b("praise")
    val rp = a.b("reply")
    val wh = a.b("whisper")
    val rk = a.b("station")
    val ground = h * 0.8f
    drawRect(Brush.verticalGradient(listOf(TentC13.Sky, TentC13.SkyLow), endY = ground), size = Size(w, ground))
    drawRect(TentC13.Sand, topLeft = Offset(0f, ground), size = Size(w, h - ground))
    hills(Color(0xFFE6CFA0), ground, h * 0.02f, 0.02f, 0.05f, 1.3f)
    // the tent: a dark roof on poles, its back cloth, the mat
    drawPath(Path().apply { moveTo(w * 0.04f, h * 0.24f); lineTo(w * 0.5f, h * 0.12f); lineTo(w * 0.96f, h * 0.24f); lineTo(w * 0.96f, h * 0.3f); lineTo(w * 0.04f, h * 0.3f); close() }, TentC13.Tent)
    drawRect(TentC13.TentLight, topLeft = Offset(w * 0.42f, h * 0.3f), size = Size(w * 0.54f, ground - h * 0.3f), alpha = 0.55f)
    for (x in listOf(0.06f, 0.42f, 0.94f)) drawLine(TentC13.Tent, Offset(w * x, h * 0.28f), Offset(w * x, ground), 2.4f * dp)
    drawRect(TentC13.Mat, topLeft = Offset(w * 0.46f, ground - h * 0.02f), size = Size(w * 0.46f, h * 0.05f))
    drawRect(TentC13.MatLine, topLeft = Offset(w * 0.47f, ground - h * 0.012f), size = Size(w * 0.44f, h * 0.03f), style = Stroke(1f * dp), alpha = 0.8f)
    // his light, on the same level as those sitting with him; plain roundels for them
    val lc = Offset(w * 0.7f, ground - h * 0.12f)
    val pushUp = bump(seg(rk, 0.2f, 0.7f))
    sirajMunir(lc, 4.6f * dp, 1f, t, nameAt = lc + Offset(0f, h * 0.15f))
    for (k in 0 until 4) {
        val x = w * (0.52f + 0.07f * k + (if (k >= 2) 0.14f else 0f))
        w8Roundel(Offset(x, ground - h * 0.08f), w * 0.022f, 1f)
    }
    // rank: a raised seat pushed toward him that does not lift him; his station under him
    if (rk > 0f) {
        val seatX = mix(w * 0.45f, w * 0.64f, easeInOut(seg(rk, 0f, 0.4f)))
        val back = easeInOut(seg(rk, 0.6f, 0.9f))
        val sx = mix(seatX, w * 0.45f, back)
        val gone = 1f - easeOut(seg(rk, 0.88f, 1f))
        drawRect(Color(0xFFB08A5A), topLeft = Offset(sx - w * 0.03f, ground - h * 0.12f - h * 0.02f * pushUp), size = Size(w * 0.06f, h * 0.12f), alpha = gone)
        // the one who pushed it takes it back to where he came from
        val pf = Offset(sx - w * 0.06f, ground + h * 0.06f)
        figure(pf, h * 0.3f, TentC13.Ink, walk = if (back in 0.01f..0.99f) t * 6f else 0f, arms = -0.8f, lean = 0.25f * (1f - back), alpha = gone)
        noSign11pub(Offset(sx, ground - h * 0.2f), w * 0.03f, seg(rk, 0.4f, 0.55f) * (1f - back))
        tagAt(a.tag("station"), lc + Offset(0f, -h * 0.14f), seg(rk, 0.5f, 0.8f), back = TentC13.Back, ink = ArtInk.Gold)
    }
    // the delegation: three who come in from the left
    val tall = h * 0.32f
    val come = easeInOut(seg(pr, 0f, 0.4f))
    val bowing = easeOut(seg(pr, 0.35f, 0.55f)) * (1f - easeOut(seg(rp, 0.3f, 0.6f)))
    val swell = easeOut(seg(wh, 0.2f, 0.5f)) * (1f - easeOut(seg(wh, 0.65f, 0.95f)))
    if (pr > 0f) for (k in 0 until 3) {
        val foot = Offset(mix(-w * 0.1f, w * (0.18f + 0.08f * k), come), ground + h * 0.06f)
        withTransform({ scale(-1f, 1f, pivot = foot) }) {
            figure(foot, tall, TentC13.Ink, walk = if (come < 1f) t * 6f + k else 0f, lean = -0.25f * bowing, arms = 0.4f + 0.4f * swell)
        }
        // their words, swelling while the smoke is at them, then plain
        if (k == 2 && (pr > 0.4f || wh > 0f)) {
            val big = 1f + 1.5f * swell
            for (r in 0 until 3) {
                val f = fract(t * 1.2f + r / 3f)
                val c = foot + Offset(tall * 0.3f + tall * 0.25f * f, -tall * 0.85f)
                val rr = (5f + 7f * f) * dp * big
                drawArc(TentC13.Ink, -40f, 80f, false, topLeft = c - Offset(rr, rr), size = Size(2 * rr, 2 * rr), alpha = 0.6f * (1f - f), style = Stroke(1.4f * dp, cap = StrokeCap.Round))
            }
        }
    }
    // whisper: the dark smoke of Shaytan creeping up behind them, then thinning away
    if (wh > 0f) {
        val creep = easeOut(seg(wh, 0f, 0.4f)) * (1f - easeOut(seg(wh, 0.6f, 1f)))
        for (k in 0 until 12) {
            val f = fract(t * 0.3f + k / 12f)
            val x = mix(w * 0.02f, w * 0.3f, creep) + w * 0.03f * sin(f * 6f + k)
            val y = ground - tall * 0.2f - tall * 0.7f * f * creep
            drawCircle(TentC13.Smoke, w * (0.02f + 0.03f * f), Offset(x, y), alpha = 0.5f * creep * (1f - f))
        }
    }
    callout(Offset(w * 0.3f, h * 0.2f), a.big("praise", 0, 0), w * 0.28f, seg(pr, 0.4f, 0.6f) * (1f - 0.7f * seg(rp, 0.3f, 0.6f)), ArtInk.Shade, Color(0xFFB0453A))
    callout(Offset(w * 0.66f, h * 0.42f), a.big("reply", 0, 0), w * 0.42f, seg(rp, 0.1f, 0.3f) * (1f - 0.6f * seg(wh, 0.3f, 0.6f)) * (1f - seg(rk, 0.3f, 0.6f)), ArtInk.Shade, Color(0xFFB0453A))
}
