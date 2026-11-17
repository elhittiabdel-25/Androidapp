package app.usulcards.ui

import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 94 (2026-11-12): v1.4, the 51 cards of batches 18 to 20, as boards (see ArtKit94.kt). Each beat names what it shows
 * with the card's own words. Prophets, companions and the jinn are never drawn: a plain figure stands only for an
 * unnamed person. Generated from the table in tools/scenes/w94_table.py; edit the table, not this file.
 */

private val B_TANUN = listOf(
    Bt94("tanun0", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2), It94("txt", 3))),
    Bt94("tanun1", TRACT94, listOf(It94("halq", 0))),
    Bt94("tanun2", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("tanun3", TILES94, listOf(It94("txt", 0, 1)), foot = 1),
)

internal fun DrawScope.tanunScene(a: ArtCtx) = board94(a, 0, B_TANUN)

private val B_TAIDGHAM = listOf(
    Bt94("taidgham0", TILES94, listOf(It94("txt", 0)), head = 1),
    Bt94("taidgham1", TILES94, listOf(It94("txt", 0)), head = 1),
    Bt94("taidgham2", TILES94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("taidgham3", TILES94, listOf(It94("txt", 0, 2, Qn.Ember), It94("txt", 1, 2, Qn.Ember)), head = 2),
)

internal fun DrawScope.taidghamScene(a: ArtCtx) = board94(a, 1, B_TAIDGHAM)

private val B_TAIQLAB = listOf(
    Bt94("taiqlab0", FLOW94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2)), head = 3),
    Bt94("taiqlab1", TILES94, listOf(It94("txt", 0))),
    Bt94("taiqlab2", TILES94, listOf(It94("txt", 0))),
    Bt94("taiqlab3", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
)

internal fun DrawScope.taiqlabScene(a: ArtCtx) = board94(a, 2, B_TAIQLAB)

private val B_TAMIM = listOf(
    Bt94("tamim0", TRACT94, listOf(It94("khayshum", 0))),
    Bt94("tamim1", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("tamim2", TRACT94, listOf(It94("shafa", 0))),
    Bt94("tamim3", FLOW94, listOf(It94("txt", 0), It94("txt", 1)), foot = 2),
)

internal fun DrawScope.tamimScene(a: ArtCtx) = board94(a, 3, B_TAMIM)

private val B_TALAM = listOf(
    Bt94("talam0", TILES94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("talam1", ROW94, listOf(It94("moon", 0, 1))),
    Bt94("talam2", ROW94, listOf(It94("sun", 0))),
    Bt94("talam3", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2)), head = 3),
)

internal fun DrawScope.talamScene(a: ArtCtx) = board94(a, 0, B_TALAM)

private val B_TAMITHLAN = listOf(
    Bt94("tamithlan0", ROW94, listOf(It94("lips", 0, 1, Qn.Jade), It94("star", 1, 1, Qn.Jade)), head = 2),
    Bt94("tamithlan1", ROW94, listOf(It94("lips", 0), It94("star", 1, 2, Qn.Ember)), head = 2),
    Bt94("tamithlan2", ROW94, listOf(It94("lips", 0, 1, Qn.Jade), It94("star", 1, 2, Qn.Ember)), head = 2),
    Bt94("tamithlan3", VS94, listOf(It94("txt", 0), It94("txt", 1))),
)

internal fun DrawScope.tamithlanScene(a: ArtCtx) = board94(a, 1, B_TAMITHLAN)

private val B_TAMADD = listOf(
    Bt94("tamadd0", VS94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("tamadd1", WAVE94, listOf(It94("2", 0))),
    Bt94("tamadd2", WAVE94, listOf(It94("4", 0), It94("6", 1)), head = 2),
    Bt94("tamadd3", TILES94, listOf(It94("txt", 0), It94("txt", 1))),
)

internal fun DrawScope.tamaddScene(a: ArtCtx) = board94(a, 2, B_TAMADD)

private val B_TAMADDAHKAM = listOf(
    Bt94("tamaddahkam0", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("tamaddahkam1", WAVE94, listOf(It94("4", 0))),
    Bt94("tamaddahkam2", WAVE94, listOf(It94("4", 0))),
    Bt94("tamaddahkam3", WAVE94, listOf(It94("6", 0))),
)

internal fun DrawScope.tamaddahkamScene(a: ArtCtx) = board94(a, 0, B_TAMADDAHKAM)

private val B_JZWAJIB = listOf(
    Bt94("jzwajib0", FLOW94, listOf(It94("scroll", 0), It94("book", 1)), head = 2),
    Bt94("jzwajib1", TRACT94, listOf(It94("lisan", 0))),
    Bt94("jzwajib2", ROW94, listOf(It94("lips", 0, 1))),
    Bt94("jzwajib3", ROW94, listOf(It94("pen", 0), It94("chain", 1))),
)

internal fun DrawScope.jzwajibScene(a: ArtCtx) = board94(a, 4, B_JZWAJIB)

private val B_JZMAKHARIJ = listOf(
    Bt94("jzmakharij0", TRACT94, listOf(It94("jawf", 0))),
    Bt94("jzmakharij1", TRACT94, listOf(It94("halq", 0))),
    Bt94("jzmakharij2", TRACT94, listOf(It94("lisan", 0))),
    Bt94("jzmakharij3", TRACT94, listOf(It94("shafa", 0))),
    Bt94("jzmakharij4", TRACT94, listOf(It94("khayshum", 0))),
)

internal fun DrawScope.jzmakharijScene(a: ArtCtx) = board94(a, 0, B_JZMAKHARIJ)

private val B_JZSIFAT = listOf(
    Bt94("jzsifat0", VS94, listOf(It94("txt", 0), It94("txt", 1))),
    Bt94("jzsifat1", VS94, listOf(It94("txt", 0), It94("txt", 1))),
    Bt94("jzsifat2", TILES94, listOf(It94("txt", 0)), head = 1),
    Bt94("jzsifat3", TILES94, listOf(It94("txt", 0)), head = 1),
)

internal fun DrawScope.jzsifatScene(a: ArtCtx) = board94(a, 1, B_JZSIFAT)

private val B_JZTAJWID = listOf(
    Bt94("jztajwid0", ROW94, listOf(It94("book", 0, 1))),
    Bt94("jztajwid1", ROW94, listOf(It94("crown", 0), It94("star", 1))),
    Bt94("jztajwid2", ROW94, listOf(It94("scale", 0), It94("key", 1))),
    Bt94("jztajwid3", VS94, listOf(It94("lips", 0, 1, Qn.Jade), It94("chain", 1, 2, Qn.Ember))),
)

internal fun DrawScope.jztajwidScene(a: ArtCtx) = board94(a, 2, B_JZTAJWID)

private val B_JZTARQIQ = listOf(
    Bt94("jztarqiq0", TILES94, listOf(It94("txt", 0)), head = 1),
    Bt94("jztarqiq1", TILES94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("jztarqiq2", TILES94, listOf(It94("txt", 0)), head = 1, foot = 2),
    Bt94("jztarqiq3", TILES94, listOf(It94("txt", 0)), head = 1, foot = 2),
)

internal fun DrawScope.jztarqiqScene(a: ArtCtx) = board94(a, 3, B_JZTARQIQ)

private val B_JZWAQF = listOf(
    Bt94("jzwaqf0", ROW94, listOf(It94("lock", 0), It94("key", 1))),
    Bt94("jzwaqf1", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("jzwaqf2", TILES94, listOf(It94("txt", 0, 2, Qn.Ember)), foot = 1),
    Bt94("jzwaqf3", VS94, listOf(It94("txt", 0, 2), It94("txt", 1, 2))),
)

internal fun DrawScope.jzwaqfScene(a: ArtCtx) = board94(a, 0, B_JZWAQF)

private val B_JZWASL = listOf(
    Bt94("jzwasl0", FLOW94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("jzwasl1", TILES94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("jzwasl2", TILES94, listOf(It94("txt", 0)), foot = 1),
    Bt94("jzwasl3", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2), It94("txt", 3))),
)

internal fun DrawScope.jzwaslScene(a: ArtCtx) = board94(a, 1, B_JZWASL)

private val B_JZRAWM = listOf(
    Bt94("jzrawm0", TILES94, listOf(It94("txt", 0, 2, Qn.Ember))),
    Bt94("jzrawm1", WAVE94, listOf(It94("1", 0))),
    Bt94("jzrawm2", TRACT94, listOf(It94("shafa", 0))),
    Bt94("jzrawm3", TILES94, listOf(It94("txt", 0)), head = 1),
)

internal fun DrawScope.jzrawmScene(a: ArtCtx) = board94(a, 2, B_JZRAWM)
