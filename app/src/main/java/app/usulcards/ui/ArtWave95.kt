package app.usulcards.ui

import androidx.compose.ui.graphics.drawscope.DrawScope

/*
 * Wave 95 (2026-11-13): v1.5, the 39 Sira cards of batches 21 to 25, as boards (see ArtKit94.kt). Each beat names what it shows
 * with the card's own words. The Prophet ﷺ is only a named light; companions, Jibril and the angels are name tiles,
 * never drawn. Generated from the table in tools/scenes/w95_table.py; edit the table, not this file.
 */

private val B_SBNASAB = listOf(
    Bt94("sbnasab0", FLOW94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2)), head = 3),
    Bt94("sbnasab1", FLOW94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2))),
    Bt94("sbnasab2", FAN94, listOf(It94("people", 0), It94("people", 1), It94("people", 2), It94("light", 3, 1))),
    Bt94("sbnasab3", ROW94, listOf(It94("crown", 0), It94("light", 1, 1))),
)

internal fun DrawScope.sbnasabScene(a: ArtCtx) = board94(a, 0, B_SBNASAB)

private val B_SBMAWLID = listOf(
    Bt94("sbmawlid0", ROW94, listOf(It94("sun", 0), It94("crescent", 1), It94("hourglass", 2))),
    Bt94("sbmawlid1", VS94, listOf(It94("kaaba", 0, 1, Qn.Jade), It94("txt", 1, 2, Qn.Ember))),
    Bt94("sbmawlid2", FLOW94, listOf(It94("light", 0), It94("house", 1))),
    Bt94("sbmawlid3", ROW94, listOf(It94("people", 0, 2), It94("people", 1, 2)), head = 2),
)

internal fun DrawScope.sbmawlidScene(a: ArtCtx) = board94(a, 2, B_SBMAWLID)

private val B_SBRIDAA = listOf(
    Bt94("sbridaa0", ROW94, listOf(It94("txt", 0), It94("txt", 1))),
    Bt94("sbridaa1", FLOW94, listOf(It94("people", 0), It94("txt", 1))),
    Bt94("sbridaa2", ROW94, listOf(It94("house", 0), It94("wheat", 1, 1, Qn.Jade), It94("drop", -1))),
    Bt94("sbridaa3", ROW94, listOf(It94("hourglass", 0)), head = 1),
)

internal fun DrawScope.sbridaaScene(a: ArtCtx) = board94(a, 3, B_SBRIDAA)

private val B_SBSHARH = listOf(
    Bt94("sbsharh0", ROW94, listOf(It94("heart", -1), It94("heart", -1)), head = 0, foot = 1),
    Bt94("sbsharh1", ROW94, listOf(It94("mountain", 0), It94("hourglass", 1)), head = 2),
    Bt94("sbsharh2", ROW94, listOf(It94("crescent", 0), It94("star", -1)), head = 1),
    Bt94("sbsharh3", FLOW94, listOf(It94("heart", 0), It94("cup", 1), It94("drop", 2, 1, Qn.Jade))),
)

internal fun DrawScope.sbsharhScene(a: ArtCtx) = board94(a, 1, B_SBSHARH)

private val B_SBYUTM = listOf(
    Bt94("sbyutm0", ROW94, listOf(It94("light", 0, 1), It94("txt", 1, 2)), head = 2),
    Bt94("sbyutm1", ROW94, listOf(It94("txt", 0, 2), It94("hourglass", 1))),
    Bt94("sbyutm2", FLOW94, listOf(It94("txt", 0), It94("txt", 1), It94("hourglass", 2))),
    Bt94("sbyutm3", ROW94, listOf(It94("heart", 0)), head = 1),
)

internal fun DrawScope.sbyutmScene(a: ArtCtx) = board94(a, 4, B_SBYUTM)

private val B_SBHIFZ = listOf(
    Bt94("sbhifz0", ROW94, listOf(It94("light", 0, 1), It94("crown", 1))),
    Bt94("sbhifz1", TILES94, listOf(It94("txt", 0, 2, Qn.Ember), It94("txt", 1, 2, Qn.Ember), It94("txt", 2, 2, Qn.Ember))),
    Bt94("sbhifz2", ROW94, listOf(It94("house", 0), It94("people", 1)), head = 2),
    Bt94("sbhifz3", FLOW94, listOf(It94("people", -1), It94("hands", 0), It94("scale", 1, 1, Qn.Jade))),
)

internal fun DrawScope.sbhifzScene(a: ArtCtx) = board94(a, 2, B_SBHIFZ)

private val B_SBKAABA = listOf(
    Bt94("sbkaaba0", ROW94, listOf(It94("kaaba", 0), It94("light", 1, 1), It94("txt", 2))),
    Bt94("sbkaaba1", ROW94, listOf(It94("txt", 0), It94("lips", 1))),
    Bt94("sbkaaba2", ROW94, listOf(It94("lips", 0))),
    Bt94("sbkaaba3", ROW94, listOf(It94("lock", 0, 1, Qn.Jade), It94("light", 1, 1))),
)

internal fun DrawScope.sbkaabaScene(a: ArtCtx) = board94(a, 0, B_SBKAABA)

private val B_SBKHADIJA = listOf(
    Bt94("sbkhadija0", FLOW94, listOf(It94("kaaba", 0), It94("coin", 1), It94("mountain", 2))),
    Bt94("sbkhadija1", ROW94, listOf(It94("lips", 0), It94("key", 1), It94("wheat", 2))),
    Bt94("sbkhadija2", FLOW94, listOf(It94("txt", 0), It94("txt", 1), It94("light", 2, 1))),
    Bt94("sbkhadija3", TILES94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2), It94("txt", 3), It94("txt", 4), It94("txt", 5))),
)

internal fun DrawScope.sbkhadijaScene(a: ArtCtx) = board94(a, 1, B_SBKHADIJA)

private val B_MDHIRA = listOf(
    Bt94("mdhira0", ROW94, listOf(It94("mountain", 0), It94("crescent", 1)), head = 2),
    Bt94("mdhira1", VS94, listOf(It94("txt", 0, 1), It94("lips", 1)), head = 2),
    Bt94("mdhira2", ROW94, listOf(It94("book", 0, 1)), foot = 1),
    Bt94("mdhira3", VS94, listOf(It94("book", 0, 1, Qn.Jade), It94("scroll", 1, 2, Qn.Ember))),
)

internal fun DrawScope.mdhiraScene(a: ArtCtx) = board94(a, 3, B_MDHIRA)

private val B_MDMUDD = listOf(
    Bt94("mdmudd0", ROW94, listOf(It94("hourglass", 0), It94("heart", 1))),
    Bt94("mdmudd1", FLOW94, listOf(It94("txt", 0), It94("house", 1), It94("lips", 2))),
    Bt94("mdmudd2", ROW94, listOf(It94("book", 0, 1)), foot = 1),
    Bt94("mdmudd3", ROW94, listOf(It94("lips", 0), It94("light", 1), It94("heart", 2)), head = 3),
)

internal fun DrawScope.mdmuddScene(a: ArtCtx) = board94(a, 4, B_MDMUDD)

private val B_MDMARATIB = listOf(
    Bt94("mdmaratib0", ROW94, listOf(It94("bed", 0, 1)), head = 1),
    Bt94("mdmaratib1", ROW94, listOf(It94("heart", 0), It94("txt", 1))),
    Bt94("mdmaratib2", WAVE94, listOf(It94("1", 0)), foot = 1),
    Bt94("mdmaratib3", ROW94, listOf(It94("star", 0), It94("mat", 1)), head = 2),
)

internal fun DrawScope.mdmaratibScene(a: ArtCtx) = board94(a, 1, B_MDMARATIB)

private val B_MDSIRR = listOf(
    Bt94("mdsirr0", ROW94, listOf(It94("lock", 0), It94("kaaba", 1))),
    Bt94("mdsirr1", FAN94, listOf(It94("txt", 0), It94("txt", 1), It94("txt", 2), It94("light", 3, 1))),
    Bt94("mdsirr2", ROW94, listOf(It94("mat", 0, 1))),
    Bt94("mdsirr3", ROW94, listOf(It94("house", 0), It94("people", 1))),
)

internal fun DrawScope.mdsirrScene(a: ArtCtx) = board94(a, 0, B_MDSIRR)

private val B_MDJAHR = listOf(
    Bt94("mdjahr0", ROW94, listOf(It94("people", 0)), head = 1),
    Bt94("mdjahr1", FLOW94, listOf(It94("mountain", 0), It94("lips", 1), It94("people", 2))),
    Bt94("mdjahr2", ROW94, listOf(It94("people", 0), It94("check", 1, 1, Qn.Jade))),
    Bt94("mdjahr3", VS94, listOf(It94("coin", 0, 1), It94("txt", 1))),
)

internal fun DrawScope.mdjahrScene(a: ArtCtx) = board94(a, 2, B_MDJAHR)

private val B_MDADHA = listOf(
    Bt94("mdadha0", ROW94, listOf(It94("flame", 0, 0, Qn.Ember), It94("people", 1))),
    Bt94("mdadha1", ROW94, listOf(It94("kaaba", 0), It94("mat", 1))),
    Bt94("mdadha2", FLOW94, listOf(It94("txt", 0), It94("check", 1, 1, Qn.Jade))),
    Bt94("mdadha3", ROW94, listOf(It94("sun", 0), It94("kaaba", 1))),
)

internal fun DrawScope.mdadhaScene(a: ArtCtx) = board94(a, 3, B_MDADHA)

private val B_MDHABASHA = listOf(
    Bt94("mdhabasha0", FLOW94, listOf(It94("kaaba", 0), It94("crown", 1)), head = 2),
    Bt94("mdhabasha1", ROW94, listOf(It94("txt", 0), It94("txt", 1)), head = 2),
    Bt94("mdhabasha2", ROW94, listOf(It94("people", 0), It94("people", 1, 0, Qn.Rose))),
    Bt94("mdhabasha3", VS94, listOf(It94("txt", 0, 2), It94("crown", 1, 1, Qn.Jade)), foot = 2),
)

internal fun DrawScope.mdhabashaScene(a: ArtCtx) = board94(a, 4, B_MDHABASHA)

private val B_MDSHIB = listOf(
    Bt94("mdshib0", VS94, listOf(It94("people", 0, 0, Qn.Ember), It94("people", 1, 0, Qn.Jade))),
    Bt94("mdshib1", ROW94, listOf(It94("scroll", 0), It94("mountain", 1))),
    Bt94("mdshib2", ROW94, listOf(It94("hourglass", 0), It94("scroll", 1, 2))),
    Bt94("mdshib3", ROW94, listOf(It94("txt", 0, 2)), foot = 1),
)

internal fun DrawScope.mdshibScene(a: ArtCtx) = board94(a, 1, B_MDSHIB)

private val B_MDTAIF = listOf(
    Bt94("mdtaif0", FLOW94, listOf(It94("kaaba", 0), It94("mountain", 1)), foot = 2),
    Bt94("mdtaif1", ROW94, listOf(It94("people", 0, 0, Qn.Ember), It94("drop", 1, 0, Qn.Rose))),
    Bt94("mdtaif2", VS94, listOf(It94("mountain", 0, 2), It94("heart", 1, 1, Qn.Jade))),
    Bt94("mdtaif3", ROW94, listOf(It94("people", -1), It94("people", -1), It94("people", -1)), head = 0),
)

internal fun DrawScope.mdtaifScene(a: ArtCtx) = board94(a, 2, B_MDTAIF)

private val B_MDAQABA = listOf(
    Bt94("mdaqaba0", ROW94, listOf(It94("people", 0, 0, Qn.Jade))),
    Bt94("mdaqaba1", FLOW94, listOf(It94("hands", 0), It94("txt", 1)), head = 2),
    Bt94("mdaqaba2", FLOW94, listOf(It94("hands", 0), It94("tree", 1, 1, Qn.Jade)), head = 2),
    Bt94("mdaqaba3", ROW94, listOf(It94("house", -1), It94("house", -1), It94("house", -1)), head = 0),
)

internal fun DrawScope.mdaqabaScene(a: ArtCtx) = board94(a, 0, B_MDAQABA)

private val B_MDHIJRA = listOf(
    Bt94("mdhijra0", ROW94, listOf(It94("house", 0), It94("people", 1, 0, Qn.Ember))),
    Bt94("mdhijra1", VS94, listOf(It94("bed", 0), It94("eye", 1, 2))),
    Bt94("mdhijra2", FLOW94, listOf(It94("mountain", 0), It94("hourglass", 1), It94("house", 2))),
    Bt94("mdhijra3", ROW94, listOf(It94("mat", 0), It94("house", 1))),
)

internal fun DrawScope.mdhijraScene(a: ArtCtx) = board94(a, 4, B_MDHIJRA)

private val B_MBMASJID = listOf(
    Bt94("mbmasjid0", ROW94, listOf(It94("mat", 0, 1)), head = 1),
    Bt94("mbmasjid1", FLOW94, listOf(It94("child", 0), It94("coin", 1, 1, Qn.Jade))),
    Bt94("mbmasjid2", ROW94, listOf(It94("light", 0, 1), It94("house", 1))),
    Bt94("mbmasjid3", ROW94, listOf(It94("key", 0))),
)

internal fun DrawScope.mbmasjidScene(a: ArtCtx) = board94(a, 1, B_MBMASJID)

private val B_MBMUAKHAT = listOf(
    Bt94("mbmuakhat0", ROW94, listOf(It94("people", 0), It94("hands", -1), It94("people", 1, 0, Qn.Jade)), head = 2),
    Bt94("mbmuakhat1", ROW94, listOf(It94("heart", 0), It94("coin", 1))),
    Bt94("mbmuakhat2", FLOW94, listOf(It94("coin", 0), It94("house", 1))),
    Bt94("mbmuakhat3", FAN94, listOf(It94("people", -1), It94("people", -1), It94("people", -1), It94("chain", 0, 1, Qn.Jade))),
)

internal fun DrawScope.mbmuakhatScene(a: ArtCtx) = board94(a, 3, B_MBMUAKHAT)

private val B_MBWATHIQA = listOf(
    Bt94("mbwathiqa0", ROW94, listOf(It94("people", 0, 0, Qn.Jade), It94("people", 1), It94("people", 2))),
    Bt94("mbwathiqa1", ROW94, listOf(It94("scroll", 0), It94("cross", 1), It94("shield", 2, 1, Qn.Jade))),
    Bt94("mbwathiqa2", ROW94, listOf(It94("house", -1), It94("check", 0, 1, Qn.Jade), It94("house", -1))),
    Bt94("mbwathiqa3", ROW94, listOf(It94("chain", 0, 2, Qn.Ember))),
)

internal fun DrawScope.mbwathiqaScene(a: ArtCtx) = board94(a, 2, B_MBWATHIQA)

private val B_MBISM = listOf(
    Bt94("mbism0", FLOW94, listOf(It94("txt", 0, 2), It94("txt", 1, 1, Qn.Jade))),
    Bt94("mbism1", TILES94, listOf(It94("txt", 0), It94("txt", 1))),
    Bt94("mbism2", VS94, listOf(It94("kaaba", 0), It94("house", 1)), head = 2),
    Bt94("mbism3", FLOW94, listOf(It94("heart", 0), It94("house", 1))),
)

internal fun DrawScope.mbismScene(a: ArtCtx) = board94(a, 0, B_MBISM)

private val B_MBSUKKAN = listOf(
    Bt94("mbsukkan0", ROW94, listOf(It94("child", 0), It94("txt", 1))),
    Bt94("mbsukkan1", FLOW94, listOf(It94("txt", 0), It94("light", 1, 1), It94("hands", 2))),
    Bt94("mbsukkan2", VS94, listOf(It94("mat", 0, 2), It94("house", 1, 1, Qn.Jade)), head = 2),
    Bt94("mbsukkan3", ROW94, listOf(It94("people", -1), It94("scroll", 0))),
)

internal fun DrawScope.mbsukkanScene(a: ArtCtx) = board94(a, 4, B_MBSUKKAN)

private val B_MBIDHN = listOf(
    Bt94("mbidhn0", VS94, listOf(It94("people", 0, 0, Qn.Ember), It94("people", 1, 0, Qn.Jade)), head = 2),
    Bt94("mbidhn1", ROW94, listOf(It94("book", 0, 1))),
    Bt94("mbidhn2", ROW94, listOf(It94("flag", 0), It94("txt", 1))),
    Bt94("mbidhn3", ROW94, listOf(It94("txt", 0), It94("txt", 1, 2)), foot = 2),
)

internal fun DrawScope.mbidhnScene(a: ArtCtx) = board94(a, 1, B_MBIDHN)

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
