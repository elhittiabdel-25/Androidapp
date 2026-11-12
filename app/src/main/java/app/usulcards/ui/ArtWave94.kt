package app.usulcards.ui

import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 94 (2026-11-12): v1.4, the 51 cards of batches 18 to 20, as boards (see ArtKit94.kt). Each beat names what it shows
 * with the card's own words. Prophets, companions and the jinn are never drawn: a plain figure stands only for an
 * unnamed person. Generated from the table in tools/scenes/w94_table.py; edit the table, not this file.
 */

private val B_TTMAFHUM = listOf(
    Bt94("ttmafhum0", ROW94, listOf(It94("eye", 0), It94("light", 1), It94("book", 2))),
    Bt94("ttmafhum1", FLOW94, listOf(It94("book", 0), It94("light", 1), It94("key", 2, 1))),
    Bt94("ttmafhum2", VS94, listOf(It94("key", 0, 1), It94("scroll", 1))),
    Bt94("ttmafhum3", ROW94, listOf(It94("cloud", 0), It94("drop", 1)), head = 2),
)

internal fun DrawScope.ttmafhumScene(a: ArtCtx) = board94(a, 0, B_TTMAFHUM)

private val B_TTTANAWWU = listOf(
    Bt94("tttanawwu0", VS94, listOf(It94("people", 0), It94("scale", 1))),
    Bt94("tttanawwu1", FAN94, listOf(It94("scroll", -1), It94("scroll", -1), It94("scroll", -1), It94("light", 0)), head = 1),
    Bt94("tttanawwu2", ROW94, listOf(It94("book", 0), It94("check", 1, 1, Qn.Jade)), head = 2),
    Bt94("tttanawwu3", FAN94, listOf(It94("star", -1), It94("star", -1), It94("star", -1), It94("crown", 0)), head = 1),
)

internal fun DrawScope.tttanawwuScene(a: ArtCtx) = board94(a, 1, B_TTTANAWWU)

private val B_TTTADADD = listOf(
    Bt94("tttadadd0", ROW94, listOf(It94("book", 0), It94("scale", 1))),
    Bt94("tttadadd1", FLOW94, listOf(It94("people", 0), It94("check", 1, 1, Qn.Jade))),
    Bt94("tttadadd2", VS94, listOf(It94("check", 0, 1, Qn.Jade), It94("cross", 1, 2, Qn.Ember)), head = 2),
    Bt94("tttadadd3", ROW94, listOf(It94("crescent", 0), It94("star", 1)), head = 2),
)

internal fun DrawScope.tttadaddScene(a: ArtCtx) = board94(a, 0, B_TTTADADD)

private val B_TTASBAB = listOf(
    Bt94("ttasbab0", FAN94, listOf(It94("scroll", -1), It94("scroll", -1), It94("scroll", -1), It94("book", 0)), head = 1),
    Bt94("ttasbab1", FLOW94, listOf(It94("lips", 0), It94("light", 1)), head = 2),
    Bt94("ttasbab2", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2)), head = 3),
    Bt94("ttasbab3", VS94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
)

internal fun DrawScope.ttasbabScene(a: ArtCtx) = board94(a, 2, B_TTASBAB)

private val B_TTLAFZ = listOf(
    Bt94("ttlafz0", ROW94, listOf(It94("lips", 0), It94("light", 1), It94("scale", 2))),
    Bt94("ttlafz1", FLOW94, listOf(It94("lips", 0), It94("scroll", 1))),
    Bt94("ttlafz2", FLOW94, listOf(It94("book", 0), It94("point", 1))),
    Bt94("ttlafz3", FLOW94, listOf(It94("scroll", 0), It94("hourglass", 1)), head = 2),
)

internal fun DrawScope.ttlafzScene(a: ArtCtx) = board94(a, 3, B_TTLAFZ)

private val B_TTTABAQAT = listOf(
    Bt94("tttabaqat0", ROW94, listOf(It94("people", 0), It94("people", 1, 0, Qn.Jade), It94("people", 2, 0, Qn.GoldLight))),
    Bt94("tttabaqat1", ROW94, listOf(It94("star", 0), It94("book", 1)), head = 2),
    Bt94("tttabaqat2", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2), It94("txt", 3))),
    Bt94("tttabaqat3", FLOW94, listOf(It94("people", 0), It94("scale", 1)), head = 2),
)

internal fun DrawScope.tttabaqatScene(a: ArtCtx) = board94(a, 1, B_TTTABAQAT)

private val B_TTHUSHIRAT = listOf(
    Bt94("tthushirat0", ROW94, listOf(It94("people", 0), It94("lips", 1, 1))),
    Bt94("tthushirat1", VS94, listOf(It94("scroll", 0), It94("book", 1, 2))),
    Bt94("tthushirat2", VS94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("tthushirat3", FLOW94, listOf(It94("people", 0), It94("chain", 1)), head = 2),
)

internal fun DrawScope.tthushiratScene(a: ArtCtx) = board94(a, 0, B_TTHUSHIRAT)

private val B_TTNABA = listOf(
    Bt94("ttnaba0", FLOW94, listOf(It94("people", 0), It94("scroll", 1))),
    Bt94("ttnaba1", ROW94, listOf(It94("book", 0)), head = 1),
    Bt94("ttnaba2", ROW94, listOf(It94("hourglass", 0)), head = 1),
    Bt94("ttnaba3", FAN94, listOf(It94("book", -1), It94("hourglass", -1), It94("check", 0, 1, Qn.Jade)), foot = 1),
)

internal fun DrawScope.ttnabaScene(a: ArtCtx) = board94(a, 4, B_TTNABA)

private val B_TTZUWWIJAT = listOf(
    Bt94("ttzuwwijat0", ROW94, listOf(It94("scale", 0)), head = 1),
    Bt94("ttzuwwijat1", ROW94, listOf(It94("people", 0, 0, Qn.Jade), It94("people", 1, 0, Qn.Jade)), head = 2),
    Bt94("ttzuwwijat2", FLOW94, listOf(It94("light", 0), It94("people", 1)), head = 2),
    Bt94("ttzuwwijat3", VS94, listOf(It94("people", 0, 0, Qn.Ember), It94("people", 1, 0, Qn.Jade))),
)

internal fun DrawScope.ttzuwwijatScene(a: ArtCtx) = board94(a, 3, B_TTZUWWIJAT)

private val B_TTFAJR = listOf(
    Bt94("ttfajr0", ROW94, listOf(It94("sun", 0), It94("mat", 1))),
    Bt94("ttfajr1", ROW94, listOf(It94("sun", 0, 1))),
    Bt94("ttfajr2", ROW94, listOf(It94("crescent", 0), It94("kaaba", 1))),
    Bt94("ttfajr3", ROW94, listOf(It94("scale", 0)), head = 1),
)

internal fun DrawScope.ttfajrScene(a: ArtCtx) = board94(a, 2, B_TTFAJR)

private val B_TTSHAF = listOf(
    Bt94("ttshaf0", TILES94, listOf(It94("txt", 0), It94("txt", 1))),
    Bt94("ttshaf1", ROW94, listOf(It94("sun", 0), It94("mountain", 1), It94("mat", 2))),
    Bt94("ttshaf2", FAN94, listOf(It94("sun", -1), It94("mountain", -1), It94("mat", -1), It94("crown", 0)), head = 1),
)

internal fun DrawScope.ttshafScene(a: ArtCtx) = board94(a, 0, B_TTSHAF)

private val B_TTQURAN = listOf(
    Bt94("ttquran0", FLOW94, listOf(It94("lips", 0), It94("light", 1))),
    Bt94("ttquran1", ROW94, listOf(It94("scale", 0)), head = 1),
    Bt94("ttquran2", FLOW94, listOf(It94("book", 0), It94("book", 1), It94("check", 2, 1, Qn.Jade))),
    Bt94("ttquran3", FLOW94, listOf(It94("lips", 0), It94("star", 1))),
)

internal fun DrawScope.ttquranScene(a: ArtCtx) = board94(a, 4, B_TTQURAN)

private val B_TTTIN = listOf(
    Bt94("tttin0", ROW94, listOf(It94("tree", 0), It94("tree", 1, 0, Qn.Jade))),
    Bt94("tttin1", ROW94, listOf(It94("tree", 0), It94("mountain", 1))),
    Bt94("tttin2", ROW94, listOf(It94("mountain", 0)), head = 1),
    Bt94("tttin3", ROW94, listOf(It94("house", 0), It94("mountain", 1), It94("kaaba", 2))),
)

internal fun DrawScope.tttinScene(a: ArtCtx) = board94(a, 2, B_TTTIN)

private val B_TTQADR = listOf(
    Bt94("ttqadr0", FLOW94, listOf(It94("scroll", 0), It94("star", 1)), head = 2),
    Bt94("ttqadr1", FLOW94, listOf(It94("star", -1), It94("book", 0))),
    Bt94("ttqadr2", ROW94, listOf(It94("scroll", 0), It94("pen", 1))),
    Bt94("ttqadr3", ROW94, listOf(It94("crescent", -1), It94("scale", 0), It94("hourglass", 1))),
)

internal fun DrawScope.ttqadrScene(a: ArtCtx) = board94(a, 0, B_TTQADR)

private val B_TTFALAQ = listOf(
    Bt94("ttfalaq0", VS94, listOf(It94("book", 0, 1), It94("chain", 1, 2, Qn.Ember))),
    Bt94("ttfalaq1", ROW94, listOf(It94("sun", 0)), head = 1),
    Bt94("ttfalaq2", ROW94, listOf(It94("scroll", 0, 2, Qn.Ember)), head = 1),
    Bt94("ttfalaq3", ROW94, listOf(It94("mat", 0), It94("sun", 1), It94("bed", 2))),
)

internal fun DrawScope.ttfalaqScene(a: ArtCtx) = board94(a, 2, B_TTFALAQ)

private val B_TZMUJADILA = listOf(
    Bt94("tzmujadila0", ROW94, listOf(It94("fig", -1), It94("lips", 0, 2, Qn.Ember), It94("fig", -1))),
    Bt94("tzmujadila1", FLOW94, listOf(It94("flame", 0, 0, Qn.Ember), It94("heart", 1))),
    Bt94("tzmujadila2", ROW94, listOf(It94("fig", -1), It94("fig", -1), It94("fig", -1)), head = 0, foot = 1),
    Bt94("tzmujadila3", FLOW94, listOf(It94("people", 0), It94("tree", 1, 1, Qn.Jade))),
)

internal fun DrawScope.tzmujadilaScene(a: ArtCtx) = board94(a, 1, B_TZMUJADILA)

private val B_TZHASHR = listOf(
    Bt94("tzhashr0", FLOW94, listOf(It94("kaaba", 0), It94("people", 1))),
    Bt94("tzhashr1", ROW94, listOf(It94("people", 0, 0, Qn.Jade), It94("coin", 1), It94("house", 2))),
    Bt94("tzhashr2", ROW94, listOf(It94("heart", 0), It94("hands", 1))),
    Bt94("tzhashr3", FLOW94, listOf(It94("people", 0), It94("hands", 1), It94("star", 2))),
)

internal fun DrawScope.tzhashrScene(a: ArtCtx) = board94(a, 3, B_TZHASHR)

private val B_TZMUMTAHANA = listOf(
    Bt94("tzmumtahana0", ROW94, listOf(It94("chain", 0, 2, Qn.Ember))),
    Bt94("tzmumtahana1", ROW94, listOf(It94("heart", 0), It94("house", 1), It94("cup", 2)), head = 3),
    Bt94("tzmumtahana2", ROW94, listOf(It94("hands", 0), It94("key", 1), It94("coin", 2)), head = 3),
    Bt94("tzmumtahana3", FLOW94, listOf(It94("people", 0), It94("heart", 1, 1, Qn.Jade))),
)

internal fun DrawScope.tzmumtahanaScene(a: ArtCtx) = board94(a, 1, B_TZMUMTAHANA)

private val B_TZSAFF = listOf(
    Bt94("tzsaff0", ROW94, listOf(It94("fig", -1), It94("heart", 0))),
    Bt94("tzsaff1", ROW94, listOf(It94("star", 0), It94("fig", 1, 2))),
    Bt94("tzsaff2", VS94, listOf(It94("lips", 0), It94("hands", 1, 2, Qn.Ember))),
    Bt94("tzsaff3", FLOW94, listOf(It94("light", 0), It94("heart", 1, 2, Qn.Ember))),
)

internal fun DrawScope.tzsaffScene(a: ArtCtx) = board94(a, 0, B_TZSAFF)

private val B_TZJUMUA = listOf(
    Bt94("tzjumua0", FLOW94, listOf(It94("fig", -1), It94("mat", 0)), head = 1),
    Bt94("tzjumua1", ROW94, listOf(It94("coin", 0, 2, Qn.Ember), It94("scale", 1, 2, Qn.Ember))),
    Bt94("tzjumua2", FLOW94, listOf(It94("mat", 0), It94("coin", 1, 1, Qn.Jade))),
    Bt94("tzjumua3", ROW94, listOf(It94("lips", 0), It94("heart", 1))),
)

internal fun DrawScope.tzjumuaScene(a: ArtCtx) = board94(a, 2, B_TZJUMUA)

private val B_TZMUNAFIQUN = listOf(
    Bt94("tzmunafiqun0", ROW94, listOf(It94("coin", 0), It94("child", 1)), foot = 2),
    Bt94("tzmunafiqun1", ROW94, listOf(It94("mat", 0), It94("book", 1))),
    Bt94("tzmunafiqun2", VS94, listOf(It94("coin", 0, 0, Qn.Ember), It94("book", 1)), foot = 2),
    Bt94("tzmunafiqun3", FLOW94, listOf(It94("coin", 0), It94("hourglass", 1))),
)

internal fun DrawScope.tzmunafiqunScene(a: ArtCtx) = board94(a, 3, B_TZMUNAFIQUN)

private val B_TZTAGHABUN = listOf(
    Bt94("tztaghabun0", ROW94, listOf(It94("cloud", 0), It94("pen", 1))),
    Bt94("tztaghabun1", ROW94, listOf(It94("cloud", 0), It94("heart", 1, 1, Qn.Jade))),
    Bt94("tztaghabun2", ROW94, listOf(It94("fig", -1), It94("hands", 0))),
    Bt94("tztaghabun3", ROW94, listOf(It94("coin", 0), It94("child", 1), It94("scale", 2))),
)

internal fun DrawScope.tztaghabunScene(a: ArtCtx) = board94(a, 0, B_TZTAGHABUN)

private val B_TZTALAQ = listOf(
    Bt94("tztalaq0", FLOW94, listOf(It94("drop", 0, 1), It94("hourglass", 1))),
    Bt94("tztalaq1", ROW94, listOf(It94("pen", 0), It94("hourglass", 1))),
    Bt94("tztalaq2", ROW94, listOf(It94("house", 0), It94("hourglass", 1))),
    Bt94("tztalaq3", FLOW94, listOf(It94("scroll", 0), It94("key", 1, 1, Qn.Jade))),
)

internal fun DrawScope.tztalaqScene(a: ArtCtx) = board94(a, 1, B_TZTALAQ)

private val B_TZTAHRIM = listOf(
    Bt94("tztahrim0", ROW94, listOf(It94("fig", -1), It94("child", 0), It94("flame", 1, 2, Qn.Ember))),
    Bt94("tztahrim1", FLOW94, listOf(It94("book", 0), It94("child", 1))),
    Bt94("tztahrim2", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2), It94("txt", 3))),
    Bt94("tztahrim3", VS94, listOf(It94("crown", 0, 2, Qn.Ember), It94("tree", 1, 1, Qn.Jade))),
)

internal fun DrawScope.tztahrimScene(a: ArtCtx) = board94(a, 3, B_TZTAHRIM)

private val B_TZMULK = listOf(
    Bt94("tzmulk0", ROW94, listOf(It94("crown", 0))),
    Bt94("tzmulk1", VS94, listOf(It94("bed", 0), It94("sprout", 1, 0, Qn.Jade))),
    Bt94("tzmulk2", FLOW94, listOf(It94("scale", 0), It94("star", 1, 1))),
    Bt94("tzmulk3", ROW94, listOf(It94("star", 0), It94("flame", 1, 0, Qn.Ember), It94("point", 2)), head = 3),
)

internal fun DrawScope.tzmulkScene(a: ArtCtx) = board94(a, 0, B_TZMULK)

private val B_TZQALAM = listOf(
    Bt94("tzqalam0", ROW94, listOf(It94("tree", 0, 0, Qn.Jade), It94("fig", 1, 2))),
    Bt94("tzqalam1", ROW94, listOf(It94("fig", 0), It94("scale", 1))),
    Bt94("tzqalam2", FLOW94, listOf(It94("chain", 0, 0, Qn.Ember), It94("tree", 1, 2, Qn.Ember))),
    Bt94("tzqalam3", ROW94, listOf(It94("fig", -1), It94("hands", 0))),
)

internal fun DrawScope.tzqalamScene(a: ArtCtx) = board94(a, 2, B_TZQALAM)

private val B_TZHAQQA = listOf(
    Bt94("tzhaqqa0", FLOW94, listOf(It94("people", 0), It94("scale", 1))),
    Bt94("tzhaqqa1", ROW94, listOf(It94("fig", -1), It94("book", 0, 1, Qn.Jade))),
    Bt94("tzhaqqa2", ROW94, listOf(It94("tree", 0, 0, Qn.Jade), It94("wheat", 1))),
    Bt94("tzhaqqa3", ROW94, listOf(It94("fig", -1), It94("book", 0, 2, Qn.Ember))),
)

internal fun DrawScope.tzhaqqaScene(a: ArtCtx) = board94(a, 4, B_TZHAQQA)

private val B_TZMAARIJ = listOf(
    Bt94("tzmaarij0", ROW94, listOf(It94("fig", -1), It94("cloud", 0), It94("heart", 1, 2, Qn.Ember))),
    Bt94("tzmaarij1", ROW94, listOf(It94("fig", -1), It94("coin", 0), It94("lock", 1, 0, Qn.Ember))),
    Bt94("tzmaarij2", ROW94, listOf(It94("fig", -1), It94("mat", 0, 1, Qn.Jade))),
    Bt94("tzmaarij3", ROW94, listOf(It94("key", 0), It94("hands", 1))),
)

internal fun DrawScope.tzmaarijScene(a: ArtCtx) = board94(a, 1, B_TZMAARIJ)

private val B_TZNUH = listOf(
    Bt94("tznuh0", FLOW94, listOf(It94("scroll", 0), It94("cloud", 1, 0, Qn.Ember)), head = 2),
    Bt94("tznuh1", FLOW94, listOf(It94("crown", 0), It94("people", 1))),
    Bt94("tznuh2", FLOW94, listOf(It94("people", 0), It94("eye", 1))),
    Bt94("tznuh3", FLOW94, listOf(It94("eye", 0), It94("chain", 1, 2, Qn.Ember))),
)

internal fun DrawScope.tznuhScene(a: ArtCtx) = board94(a, 3, B_TZNUH)

private val B_TZJINN = listOf(
    Bt94("tzjinn0", FLOW94, listOf(It94("book", 0), It94("ear", 1))),
    Bt94("tzjinn1", ROW94, listOf(It94("ear", 0), It94("star", 1))),
    Bt94("tzjinn2", FLOW94, listOf(It94("people", 0), It94("scroll", 1))),
    Bt94("tzjinn3", FLOW94, listOf(It94("mountain", 0), It94("flame", 1, 2, Qn.Ember))),
)

internal fun DrawScope.tzjinnScene(a: ArtCtx) = board94(a, 0, B_TZJINN)

private val B_TZMUZZAMMIL = listOf(
    Bt94("tzmuzzammil0", FLOW94, listOf(It94("lips", 0), It94("book", 1))),
    Bt94("tzmuzzammil1", ROW94, listOf(It94("book", 0), It94("heart", 1))),
    Bt94("tzmuzzammil2", ROW94, listOf(It94("moon", 0), It94("heart", 1, 1), It94("ear", 2))),
    Bt94("tzmuzzammil3", ROW94, listOf(It94("mat", 0)), head = 1),
)

internal fun DrawScope.tzmuzzammilScene(a: ArtCtx) = board94(a, 0, B_TZMUZZAMMIL)

private val B_TZMUDDATHTHIR = listOf(
    Bt94("tzmuddaththir0", ROW94, listOf(It94("fig", -1), It94("lips", 0))),
    Bt94("tzmuddaththir1", VS94, listOf(It94("book", 0, 1), It94("cross", 1, 2, Qn.Ember))),
    Bt94("tzmuddaththir2", ROW94, listOf(It94("lips", 0, 2, Qn.Ember))),
    Bt94("tzmuddaththir3", ROW94, listOf(It94("flame", 0, 0, Qn.Ember), It94("light", 1)), head = 2),
)

internal fun DrawScope.tzmuddaththirScene(a: ArtCtx) = board94(a, 3, B_TZMUDDATHTHIR)

private val B_TZQIYAMA = listOf(
    Bt94("tzqiyama0", FLOW94, listOf(It94("chain", 0), It94("cloud", 1)), foot = 2),
    Bt94("tzqiyama1", ROW94, listOf(It94("hands", 0)), head = 1),
    Bt94("tzqiyama2", ROW94, listOf(It94("hands", 0), It94("eye", 1))),
    Bt94("tzqiyama3", VS94, listOf(It94("flame", 0, 0, Qn.Ember), It94("hourglass", 1))),
)

internal fun DrawScope.tzqiyamaScene(a: ArtCtx) = board94(a, 4, B_TZQIYAMA)

private val B_TZINSAN = listOf(
    Bt94("tzinsan0", ROW94, listOf(It94("people", 0), It94("scale", 1))),
    Bt94("tzinsan1", FLOW94, listOf(It94("fig", -1), It94("cup", 0), It94("fig", -1))),
    Bt94("tzinsan2", ROW94, listOf(It94("coin", 0, 2, Qn.Ember), It94("lips", 1, 2, Qn.Ember))),
    Bt94("tzinsan3", ROW94, listOf(It94("tree", 0, 1, Qn.Jade), It94("sun", 1, 2, Qn.Ember))),
)

internal fun DrawScope.tzinsanScene(a: ArtCtx) = board94(a, 1, B_TZINSAN)

private val B_TZMURSALAT = listOf(
    Bt94("tzmursalat0", VS94, listOf(It94("people", 0), It94("bed", 1)), head = 2),
    Bt94("tzmursalat1", VS94, listOf(It94("light", 0), It94("cloud", 1, 0, Qn.Ember)), head = 2),
    Bt94("tzmursalat2", ROW94, listOf(It94("cloud", -1, 0, Qn.Ember), It94("cloud", -1, 0, Qn.Ember), It94("cloud", -1, 0, Qn.Ember)), head = 0, foot = 1),
    Bt94("tzmursalat3", ROW94, listOf(It94("book", 0, 1))),
)

internal fun DrawScope.tzmursalatScene(a: ArtCtx) = board94(a, 0, B_TZMURSALAT)

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
