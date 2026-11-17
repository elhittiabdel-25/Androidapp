package app.usulcards.ui

import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 95 (2026-11-13): v1.5, the 39 Sira cards of batches 21 to 25, as boards (see ArtKit94.kt). Each beat names what it shows
 * with the card's own words. The Prophet ﷺ is only a named light; companions, Jibril and the angels are name tiles,
 * never drawn. Generated from the table in tools/scenes/w95_table.py; edit the table, not this file.
 */

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
