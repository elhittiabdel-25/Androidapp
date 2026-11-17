package app.usulcards.ui

import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 96 (2026-11-15): v1.5, the 24 Sira cards of batches 26 and 27 (Mothers of the Believers, his dealings), as boards (see ArtKit94.kt). Each beat names what it shows
 * with the card's own words. The Prophet ﷺ is only a named light; companions, Jibril and the angels are name tiles,
 * never drawn. Generated from the table in tools/scenes/w96_table.py; edit the table, not this file.
 */

private val B_TMAHL = listOf(
    Bt94("tmahl0", ROW94, listOf(It94("light", 0, 1), It94("heart", 1), It94("sun", 2))),
    Bt94("tmahl1", FLOW94, listOf(It94("house", 0), It94("mat", 1))),
    Bt94("tmahl2", TILES94, listOf(It94("txt", 0), It94("txt", 1))),
    Bt94("tmahl3", ROW94, listOf(It94("hands", 0, 2))),
)

internal fun DrawScope.tmahlScene(a: ArtCtx) = board94(a, 1, B_TMAHL)

private val B_TMWAFA = listOf(
    Bt94("tmwafa0", ROW94, listOf(It94("heart", 0), It94("txt", 1))),
    Bt94("tmwafa1", FAN94, listOf(It94("people", -1), It94("people", -1), It94("people", -1), It94("heart", 0))),
    Bt94("tmwafa2", ROW94, listOf(It94("heart", 0), It94("txt", 1))),
    Bt94("tmwafa3", ROW94, listOf(It94("txt", 0), It94("lock", 1))),
)

internal fun DrawScope.tmwafaScene(a: ArtCtx) = board94(a, 2, B_TMWAFA)

private val B_TMBANAT = listOf(
    Bt94("tmbanat0", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2), It94("txt", 3, 1))),
    Bt94("tmbanat1", ROW94, listOf(It94("light", 0, 1), It94("lips", 1), It94("txt", 2))),
    Bt94("tmbanat2", VS94, listOf(It94("star", 0, 1, Qn.Jade), It94("people", 1))),
    Bt94("tmbanat3", ROW94, listOf(It94("flame", 0, 0, Qn.Ember), It94("txt", 1))),
)

internal fun DrawScope.tmbanatScene(a: ArtCtx) = board94(a, 3, B_TMBANAT)

private val B_TMAHFAD = listOf(
    Bt94("tmahfad0", ROW94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("tmahfad1", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("tmahfad2", VS94, listOf(It94("coin", 0, 2, Qn.Ember), It94("lips", 1))),
    Bt94("tmahfad3", FLOW94, listOf(It94("txt", 0, 2), It94("txt", 1, 1, Qn.Jade))),
)

internal fun DrawScope.tmahfadScene(a: ArtCtx) = board94(a, 4, B_TMAHFAD)

private val B_TMJAR = listOf(
    Bt94("tmjar0", ROW94, listOf(It94("house", 0, 0, Qn.Ember), It94("mat", 1))),
    Bt94("tmjar1", FLOW94, listOf(It94("txt", 0), It94("house", 1, 1, Qn.Jade))),
    Bt94("tmjar2", ROW94, listOf(It94("house", 0), It94("house", 1))),
    Bt94("tmjar3", VS94, listOf(It94("cross", 0), It94("heart", 1, 1, Qn.Jade))),
)

internal fun DrawScope.tmjarScene(a: ArtCtx) = board94(a, 0, B_TMJAR)

private val B_TMKHADAM = listOf(
    Bt94("tmkhadam0", ROW94, listOf(It94("light", 0, 1), It94("heart", 1))),
    Bt94("tmkhadam1", ROW94, listOf(It94("cup", 0), It94("scroll", 1))),
    Bt94("tmkhadam2", ROW94, listOf(It94("lips", 0))),
    Bt94("tmkhadam3", ROW94, listOf(It94("txt", 0), It94("sun", 1))),
)

internal fun DrawScope.tmkhadamScene(a: ArtCtx) = board94(a, 1, B_TMKHADAM)

private val B_TMBALA = listOf(
    Bt94("tmbala0", ROW94, listOf(It94("heart", 0), It94("hands", 1))),
    Bt94("tmbala1", VS94, listOf(It94("eye", 0, 2), It94("eye", 1, 1, Qn.Jade))),
    Bt94("tmbala2", ROW94, listOf(It94("mat", 0), It94("check", 1, 1, Qn.Jade))),
    Bt94("tmbala3", VS94, listOf(It94("people", 0), It94("people", 1, 2))),
)

internal fun DrawScope.tmbalaScene(a: ArtCtx) = board94(a, 2, B_TMBALA)

private val B_TMKUFFAR = listOf(
    Bt94("tmkuffar0", ROW94, listOf(It94("scale", 0, 1, Qn.Jade))),
    Bt94("tmkuffar1", VS94, listOf(It94("scroll", 0), It94("tree", 1, 2))),
    Bt94("tmkuffar2", FLOW94, listOf(It94("bed", 0), It94("check", 1, 1, Qn.Jade))),
    Bt94("tmkuffar3", VS94, listOf(It94("lips", 0, 2), It94("heart", 1, 1, Qn.Jade))),
)

internal fun DrawScope.tmkuffarScene(a: ArtCtx) = board94(a, 3, B_TMKUFFAR)

private val B_TMJADID = listOf(
    Bt94("tmjadid0", ROW94, listOf(It94("sun", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("tmjadid1", FLOW94, listOf(It94("mat", 0), It94("hands", 1))),
    Bt94("tmjadid2", ROW94, listOf(It94("coin", 0), It94("heart", 1))),
    Bt94("tmjadid3", FLOW94, listOf(It94("people", 0), It94("house", 1))),
)

internal fun DrawScope.tmjadidScene(a: ArtCtx) = board94(a, 4, B_TMJADID)

private val B_TMUSAT = listOf(
    Bt94("tmusat0", ROW94, listOf(It94("heart", 0))),
    Bt94("tmusat1", ROW94, listOf(It94("light", 0, 1), It94("lips", 1), It94("people", 2))),
    Bt94("tmusat2", ROW94, listOf(It94("hands", 0, 1, Qn.Jade))),
    Bt94("tmusat3", VS94, listOf(It94("people", 0), It94("lips", 1))),
)

internal fun DrawScope.tmusatScene(a: ArtCtx) = board94(a, 0, B_TMUSAT)

private val B_TMNISA = listOf(
    Bt94("tmnisa0", ROW94, listOf(It94("kaaba", 0), It94("lips", 1))),
    Bt94("tmnisa1", VS94, listOf(It94("people", 0, 0, Qn.Rose), It94("people", 1)), head = 2),
    Bt94("tmnisa2", ROW94, listOf(It94("cup", 0, 1, Qn.GoldLight))),
    Bt94("tmnisa3", FLOW94, listOf(It94("child", 0), It94("mat", 1))),
)

internal fun DrawScope.tmnisaScene(a: ArtCtx) = board94(a, 1, B_TMNISA)

private val B_TMKIBAR = listOf(
    Bt94("tmkibar0", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2)), head = 3),
    Bt94("tmkibar1", VS94, listOf(It94("child", 0), It94("people", 1))),
    Bt94("tmkibar2", ROW94, listOf(It94("lips", 0))),
    Bt94("tmkibar3", ROW94, listOf(It94("txt", 0), It94("check", 1, 1, Qn.Jade))),
)

internal fun DrawScope.tmkibarScene(a: ArtCtx) = board94(a, 2, B_TMKIBAR)

private val B_TMSIGHAR = listOf(
    Bt94("tmsighar0", ROW94, listOf(It94("heart", 0), It94("child", 1))),
    Bt94("tmsighar1", ROW94, listOf(It94("child", 0), It94("lips", 1))),
    Bt94("tmsighar2", ROW94, listOf(It94("hands", 0), It94("child", 1))),
    Bt94("tmsighar3", FLOW94, listOf(It94("tree", 0), It94("child", 1), It94("heart", 2))),
)

internal fun DrawScope.tmsigharScene(a: ArtCtx) = board94(a, 3, B_TMSIGHAR)
