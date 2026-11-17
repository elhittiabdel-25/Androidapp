package app.usulcards.ui

import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 95 (2026-11-13): v1.5, the 39 Sira cards of batches 21 to 25, as boards (see ArtKit94.kt). Each beat names what it shows
 * with the card's own words. The Prophet ﷺ is only a named light; companions, Jibril and the angels are name tiles,
 * never drawn. Generated from the table in tools/scenes/w95_table.py; edit the table, not this file.
 */

private val B_GZBADR = listOf(
    Bt94("gzbadr0", ROW94, listOf(It94("crescent", 0), It94("people", 1, 0, Qn.Jade)), head = 2),
    Bt94("gzbadr1", VS94, listOf(It94("people", 0, 0, Qn.Jade), It94("people", 1, 0, Qn.Ember))),
    Bt94("gzbadr2", ROW94, listOf(It94("txt", 0), It94("people", 1, 0, Qn.Ember), It94("lock", 2))),
    Bt94("gzbadr3", FLOW94, listOf(It94("lock", 0), It94("pen", 1), It94("child", 2))),
)

internal fun DrawScope.gzbadrScene(a: ArtCtx) = board94(a, 2, B_GZBADR)

private val B_GZUHUD = listOf(
    Bt94("gzuhud0", ROW94, listOf(It94("people", 0, 0, Qn.Ember), It94("hourglass", 1)), head = 2),
    Bt94("gzuhud1", ROW94, listOf(It94("mountain", 0), It94("lock", 1, 1))),
    Bt94("gzuhud2", FLOW94, listOf(It94("mountain", 0, 2, Qn.Ember), It94("txt", 1))),
    Bt94("gzuhud3", ROW94, listOf(It94("txt", 0), It94("star", 1))),
)

internal fun DrawScope.gzuhudScene(a: ArtCtx) = board94(a, 3, B_GZUHUD)

private val B_GZKHANDAQ = listOf(
    Bt94("gzkhandaq0", FAN94, listOf(It94("people", -1), It94("people", -1), It94("people", -1), It94("house", 0)), head = 1),
    Bt94("gzkhandaq1", VS94, listOf(It94("people", 0, 0, Qn.Ember), It94("trench", 1, 1, Qn.Jade)), head = 2),
    Bt94("gzkhandaq2", ROW94, listOf(It94("cloud", 0), It94("tent", 1, 2), It94("flame", 2, 2))),
    Bt94("gzkhandaq3", ROW94, listOf(It94("heart", 0), It94("scale", 1))),
)

internal fun DrawScope.gzkhandaqScene(a: ArtCtx) = board94(a, 0, B_GZKHANDAQ)

private val B_GZHUDAY = listOf(
    Bt94("gzhuday0", VS94, listOf(It94("kaaba", 0), It94("cross", 1, 2, Qn.Ember)), head = 2),
    Bt94("gzhuday1", ROW94, listOf(It94("tree", 0), It94("hands", 1, 1, Qn.Jade)), head = 2),
    Bt94("gzhuday2", ROW94, listOf(It94("scroll", 0), It94("txt", 1))),
    Bt94("gzhuday3", ROW94, listOf(It94("txt", 0), It94("light", 1, 1))),
)

internal fun DrawScope.gzhudayScene(a: ArtCtx) = board94(a, 4, B_GZHUDAY)

private val B_GZKHAYBAR = listOf(
    Bt94("gzkhaybar0", FLOW94, listOf(It94("house", 0), It94("house", 1))),
    Bt94("gzkhaybar1", TILES94, listOf(It94("txt", 0)), foot = 1),
    Bt94("gzkhaybar2", ROW94, listOf(It94("kaaba", 0, 1, Qn.Jade), It94("hourglass", 1))),
    Bt94("gzkhaybar3", FLOW94, listOf(It94("scroll", 0), It94("hands", 1))),
)

internal fun DrawScope.gzkhaybarScene(a: ArtCtx) = board94(a, 1, B_GZKHAYBAR)

private val B_GZMUTAH = listOf(
    Bt94("gzmutah0", FLOW94, listOf(It94("house", 0), It94("mountain", 1)), head = 2),
    Bt94("gzmutah1", ROW94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("gzmutah2", ROW94, listOf(It94("flag", 0), It94("txt", 1))),
    Bt94("gzmutah3", ROW94, listOf(It94("txt", 0), It94("star", 1))),
)

internal fun DrawScope.gzmutahScene(a: ArtCtx) = board94(a, 3, B_GZMUTAH)

private val B_GZFATH = listOf(
    Bt94("gzfath0", VS94, listOf(It94("people", 0, 0, Qn.Ember), It94("people", 1, 0, Qn.Jade)), foot = 2),
    Bt94("gzfath1", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("door", 2)), head = 3),
    Bt94("gzfath2", ROW94, listOf(It94("kaaba", 0), It94("cross", 1, 2, Qn.Ember), It94("lips", 2))),
    Bt94("gzfath3", TILES94, listOf(It94("txt", 0, 2), It94("txt", 1, 2))),
)

internal fun DrawScope.gzfathScene(a: ArtCtx) = board94(a, 0, B_GZFATH)

private val B_GZHUNAYN = listOf(
    Bt94("gzhunayn0", ROW94, listOf(It94("people", 0, 0, Qn.Jade), It94("people", 1, 0, Qn.Ember))),
    Bt94("gzhunayn1", ROW94, listOf(It94("people", 0), It94("mountain", 1), It94("cross", 2, 2))),
    Bt94("gzhunayn2", ROW94, listOf(It94("light", 0, 1), It94("txt", 1), It94("people", 2, 0, Qn.Jade))),
    Bt94("gzhunayn3", VS94, listOf(It94("people", 0, 2), It94("heart", 1, 1, Qn.Jade))),
)

internal fun DrawScope.gzhunaynScene(a: ArtCtx) = board94(a, 2, B_GZHUNAYN)

private val B_GZTABUK = listOf(
    Bt94("gztabuk0", ROW94, listOf(It94("people", 0, 0, Qn.Ember), It94("coin", 1))),
    Bt94("gztabuk1", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("gztabuk2", FLOW94, listOf(It94("people", 0, 0, Qn.Jade), It94("cross", 1))),
    Bt94("gztabuk3", ROW94, listOf(It94("drop", 0, 2), It94("wheat", 1, 2)), head = 2),
)

internal fun DrawScope.gztabukScene(a: ArtCtx) = board94(a, 4, B_GZTABUK)

private val B_WDWUFUD = listOf(
    Bt94("wdwufud0", FAN94, listOf(It94("people", -1), It94("people", -1), It94("people", -1), It94("house", 0)), head = 1),
    Bt94("wdwufud1", ROW94, listOf(It94("people", 0))),
    Bt94("wdwufud2", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2), It94("txt", 3), It94("txt", 4), It94("txt", 5))),
    Bt94("wdwufud3", ROW94, listOf(It94("people", 0), It94("coin", 1))),
)

internal fun DrawScope.wdwufudScene(a: ArtCtx) = board94(a, 1, B_WDWUFUD)

private val B_WDBARAA = listOf(
    Bt94("wdbaraa0", ROW94, listOf(It94("kaaba", 0), It94("txt", 1))),
    Bt94("wdbaraa1", FLOW94, listOf(It94("book", 0), It94("txt", 1), It94("people", 2))),
    Bt94("wdbaraa2", TILES94, listOf(It94("txt", 0, 2, Qn.Ember), It94("txt", 1, 2, Qn.Ember))),
    Bt94("wdbaraa3", ROW94, listOf(It94("scroll", 0), It94("hourglass", 1))),
)

internal fun DrawScope.wdbaraaScene(a: ArtCtx) = board94(a, 3, B_WDBARAA)

private val B_WDWADA = listOf(
    Bt94("wdwada0", ROW94, listOf(It94("mountain", 0), It94("kaaba", 1))),
    Bt94("wdwada1", ROW94, listOf(It94("mountain", 0), It94("lips", 1), It94("hands", 2))),
    Bt94("wdwada2", ROW94, listOf(It94("kaaba", 0, 1))),
    Bt94("wdwada3", FLOW94, listOf(It94("flag", 0), It94("txt", 1), It94("mountain", 2))),
)

internal fun DrawScope.wdwadaScene(a: ArtCtx) = board94(a, 0, B_WDWADA)

private val B_WDMARAD = listOf(
    Bt94("wdmarad0", ROW94, listOf(It94("house", 0), It94("bed", 1))),
    Bt94("wdmarad1", ROW94, listOf(It94("mat", 0), It94("txt", 1, 1))),
    Bt94("wdmarad2", ROW94, listOf(It94("crown", 0), It94("txt", 1))),
    Bt94("wdmarad3", VS94, listOf(It94("coin", 0), It94("star", 1, 1, Qn.Jade))),
)

internal fun DrawScope.wdmaradScene(a: ArtCtx) = board94(a, 2, B_WDMARAD)

private val B_WDWAFAT = listOf(
    Bt94("wdwafat0", ROW94, listOf(It94("sun", 0), It94("crescent", 1), It94("hourglass", 2))),
    Bt94("wdwafat1", ROW94, listOf(It94("light", 0, 1), It94("star", 1))),
    Bt94("wdwafat2", VS94, listOf(It94("txt", 0), It94("txt", 1, 1, Qn.Jade)), head = 2),
    Bt94("wdwafat3", ROW94, listOf(It94("book", 0, 1, Qn.Jade), It94("hourglass", 1))),
)

internal fun DrawScope.wdwafatScene(a: ArtCtx) = board94(a, 4, B_WDWAFAT)
