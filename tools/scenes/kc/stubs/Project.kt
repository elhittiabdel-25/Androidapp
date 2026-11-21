@file:Suppress("unused", "UNUSED_PARAMETER")
package app.usulcards.ui

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.TextLayoutResult

internal object Cinema { var prophetName: TextLayoutResult? = null; var dayFrame = false; var darkFrame = false }

internal object ArtInk {
    val Ivory = Color(0xFFFBF3D5)
    val Gold = Color(0xFFF2C45A)
    val Brass = Color(0xFFD29C45)
    val BrassLight = Color(0xFFF4D68A)
    val BrassDark = Color(0xFF7E5520)
    val Ember = Color(0xFFE8794A)
    val Jade = Color(0xFF86D6AE)
    val Shade = Color(0xFF070B1A)
}
internal fun beatMillis(kind: String): Int = TODO()
internal class ArtCtx(
    private val kinds: List<String>,
    private val beats: List<Float>,
    val t: Float,
    private val tags: List<List<TextLayoutResult>>,
    val extras: List<TextLayoutResult>,
    private val sides: List<Int> = emptyList(),
    private val values: List<Float> = emptyList(),
    /** The app is in its dark theme (the notebook scenes follow it; the others are night scenes either way). */
    val dark: Boolean = true,
    /** The tags again, large in naskh, for the scenes in BIG_TEXT_LAYOUTS. */
    private val bigs: List<List<TextLayoutResult>> = emptyList(),
) {
    /** Items by position, for scenes whose beats act on a target (`side`) with a strength (`value`). */
    val size: Int get() = kinds.size
    fun kindAt(i: Int): String = kinds[i]
    fun beatAt(i: Int): Float = beats.getOrElse(i) { 0f }
    fun sideAt(i: Int): Int = sides.getOrElse(i) { -1 }
    fun valueAt(i: Int): Float = values.getOrElse(i) { 0f }
    fun tagOf(i: Int, part: Int = 0): TextLayoutResult? = tags.getOrNull(i)?.getOrNull(part)
    fun parts(i: Int): Int = tags.getOrNull(i)?.size ?: 0
    /** The n-th item of [kind], or -1. */
    fun nth(kind: String, n: Int = 0): Int = index(kind, n)

    private fun index(kind: String, n: Int): Int {
        var c = 0
        for (i in kinds.indices) {
            if (kinds[i] == kind) {
                if (c == n) return i
                c++
            }
        }
        return -1
    }

    fun b(kind: String, n: Int = 0): Float = index(kind, n).let { if (it < 0) 0f else beats.getOrElse(it) { 0f } }
    fun tag(kind: String, n: Int = 0, part: Int = 0): TextLayoutResult? =
        index(kind, n).let { if (it < 0) null else tags.getOrNull(it)?.getOrNull(part) }
    fun count(kind: String): Int = kinds.count { it == kind }
    /** The n-th [kind] item's tag in large calligraphy (falls back to the small tag). */
    fun big(kind: String, n: Int = 0, part: Int = 0): TextLayoutResult? =
        index(kind, n).let { if (it < 0) null else bigs.getOrNull(it)?.getOrNull(part) ?: tags.getOrNull(it)?.getOrNull(part) }
}
internal val BIG_TEXT_LAYOUTS: Set<String> = TODO()
internal fun seg(p: Float, a: Float, b: Float): Float = TODO()
internal fun mix(a: Float, b: Float, f: Float): Float = TODO()
internal fun easeOut(x: Float): Float = TODO()
internal fun easeInOut(x: Float): Float = TODO()
internal fun bump(x: Float): Float = TODO()
internal fun fract(x: Float): Float = TODO()
internal fun DrawScope.tagAt(tl: TextLayoutResult?, c: Offset, alpha: Float, back: Color = Color(0xB3101426), ink: Color = Color.Unspecified) {}
internal fun starPath(c: Offset, r: Float): Path = TODO()
internal fun horseshoe(cx: Float, cy: Float, r: Float, j: Float, floorY: Float): Path = TODO()
internal fun cubic(p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float): Offset = TODO()
internal fun cubicPart(p0: Offset, p1: Offset, p2: Offset, p3: Offset, from: Float, to: Float, shift: Offset = Offset.Zero): Path = TODO()
internal fun DrawScope.lantern(ring: Offset, s: Float, glow: Float, flicker: Float) {}
internal fun DrawScope.hills(color: Color, base: Float, amp: Float, f1: Float, f2: Float, ph: Float) {}
internal fun DrawScope.throughCamera(layout: String, progress: Float, t: Float, block: DrawScope.() -> Unit) {}
internal fun DrawScope.drawAir(layout: String, t: Float, dark: Boolean) {}
internal fun DrawScope.bloom(c: Offset, r: Float, color: Color, strength: Float = 1f) {}
internal fun DrawScope.lightShafts(from: Offset, angle: Float, spread: Float, length: Float, color: Color, strength: Float, t: Float, rays: Int = 7) {}
internal fun cameraProgress(beats: List<Float>): Float = TODO()
internal fun DrawScope.glint(c: Offset, r: Float, color: Color, alpha: Float) {}
internal fun DrawScope.deepSky(t: Float, horizon: Float, top: Color, low: Color, glow: Color = Color(0xFF6E5A9A), starAlpha: Float = 1f, shine: Float = 0f, twinkles: Boolean = true) {}
internal fun DrawScope.farHills(near: Color, sky: Color, base: Float, amp: Float, ph: Float) {}
internal fun lerpColor(a: Color, b: Color, f: Float): Color = TODO()
internal fun DrawScope.softGlow(c: Offset, r: Float, color: Color, alpha: Float) {}
internal fun DrawScope.sirajMunir(c: Offset, r: Float, strength: Float, t: Float, nameAt: Offset? = null, named: Boolean = true) {}
internal fun DrawScope.sit12(seat: Offset, tall: Float, color: Color, alpha: Float, facing: Float, bob: Float = 0f) {}
internal fun DrawScope.pray12(foot: Offset, tall: Float, color: Color, alpha: Float) {}
internal fun DrawScope.bow12(foot: Offset, tall: Float, color: Color, alpha: Float, deep: Float) {}
internal fun DrawScope.woman12(foot: Offset, tall: Float, color: Color, alpha: Float, walk: Float = 0f) {}
internal fun DrawScope.womanSit12(seat: Offset, tall: Float, color: Color, alpha: Float, facing: Float = 1f) {}
internal fun DrawScope.palm12(foot: Offset, s: Float, trunk: Color, leaf: Color, alpha: Float, t: Float = 0f) {}
internal fun DrawScope.house12(foot: Offset, s: Float, wall: Color, trim: Color, alpha: Float, lit: Float = 0f) {}
internal fun DrawScope.door12(sill: Offset, wd: Float, ht: Float, open: Float, frame: Color, leaf: Color, alpha: Float, light: Color = Color(0xFFFFE2A0)) {}
internal fun DrawScope.mosque12(foot: Offset, s: Float, body: Color, dome: Color, alpha: Float, lit: Float = 0f) {}
internal fun DrawScope.crescent12(c: Offset, r: Float, color: Color, sky: Color, alpha: Float) {}
internal fun DrawScope.moon12(c: Offset, r: Float, alpha: Float) {}
internal fun DrawScope.sun12(c: Offset, r: Float, alpha: Float) {}
internal fun DrawScope.daySky12(top: Color, low: Color, horizon: Float, ground: Color) {}
internal fun DrawScope.hands12(c: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.beads12(c: Offset, r: Float, color: Color, alpha: Float, turn: Float = 0f) {}
internal fun DrawScope.cup12(foot: Offset, s: Float, color: Color, alpha: Float, fill: Color? = null) {}
internal fun DrawScope.balance12(foot: Offset, s: Float, tilt: Float, color: Color, alpha: Float, left: Color = color, right: Color = color) {}
internal fun balancePans12(foot: Offset, s: Float, tilt: Float): Pair<Offset, Offset> = TODO()
internal fun DrawScope.guardDome12(c: Offset, r: Float, rise: Float, t: Float, alpha: Float, color: Color = Color(0xFFBFE6FF)) {}
internal fun DrawScope.heart12(c: Offset, s: Float, color: Color, alpha: Float, fill: Boolean = false) {}
internal fun DrawScope.coin12(c: Offset, r: Float, alpha: Float, color: Color = Color(0xFFE8C25A)) {}
internal fun DrawScope.child12(foot: Offset, tall: Float, color: Color, alpha: Float, walk: Float = 0f, arms: Float = 0f) {}
internal fun DrawScope.rain12(x0: Float, x1: Float, top: Float, bottom: Float, t: Float, color: Color, alpha: Float, n: Int = 28) {}
internal fun DrawScope.smoke12(from: Offset, to: Offset, p: Float, t: Float, alpha: Float, color: Color = Color(0xFF2A2430)) {}
internal fun DrawScope.voice12(c: Offset, r: Float, t: Float, color: Color, alpha: Float, dir: Float = 1f) {}
internal fun DrawScope.qFit(tl: TextLayoutResult?, c: Offset, maxW: Float, maxH: Float, color: Color, alpha: Float, glow: Color? = null) {}
internal fun DrawScope.qRosette(c: Offset, r: Float, alpha: Float, fill: Color = Qn.Teal) {}
internal fun DrawScope.qPlate(c: Offset, wd: Float, name: TextLayoutResult?, alpha: Float) {}
internal fun DrawScope.glyphCard(c: Offset, s: Float, tl: TextLayoutResult?, alpha: Float, rim: Color = Qn.Gold, lit: Float = 0f, icon: DrawScope.(Offset, Float, Float) -> Unit) {}
internal fun DrawScope.qIcon(name: String, c: Offset, s: Float, color: Color, alpha: Float, t: Float = 0f) {}
internal fun DrawScope.qFlame(base: Offset, s: Float, color: Color, alpha: Float, t: Float) {}
internal fun DrawScope.qBeam(from: Offset, to: Offset, p: Float, color: Color, alpha: Float, wd: Float) {}
internal fun DrawScope.qPlant(base: Offset, ht: Float, grow: Float, alpha: Float, t: Float = 0f) {}
internal fun DrawScope.qNamePlate(c: Offset, tl: TextLayoutResult?, alpha: Float, fill: Color = Color(0xFF3A3340), ink: Color = ArtInk.Ivory) {}
internal fun DrawScope.qPage(c: Offset, wd: Float, ht: Float, alpha: Float, lines: Int = 7, frame: Boolean = true) {}
internal fun DrawScope.qSmoke(from: Offset, to: Offset, p: Float, t: Float, alpha: Float, r: Float) {}
internal fun DrawScope.nightSky(t: Float, top: Color, bottom: Color, starsTo: Float, starAlpha: Float = 1f) {}
internal fun DrawScope.lanternScene(a: ArtCtx) {}
internal fun DrawScope.trailScene(a: ArtCtx) {}
internal fun DrawScope.ridgeScene(a: ArtCtx) {}
internal fun DrawScope.treeScene(a: ArtCtx) {}
internal fun DrawScope.streamsScene(a: ArtCtx) {}
internal fun DrawScope.majlisScene(a: ArtCtx) {}
internal fun DrawScope.laneScene(a: ArtCtx) {}
internal fun DrawScope.hilltopScene(a: ArtCtx) {}
internal fun DrawScope.waysideScene(a: ArtCtx) {}
internal fun DrawScope.flockScene(a: ArtCtx) {}
internal fun DrawScope.deckScene(a: ArtCtx) {}
internal fun DrawScope.damScene(a: ArtCtx) {}
internal fun DrawScope.aerialScene(a: ArtCtx) {}
internal fun DrawScope.soukScene(a: ArtCtx) {}
internal fun DrawScope.bridgeScene(a: ArtCtx) {}
internal fun DrawScope.refugeScene(a: ArtCtx) {}
internal fun DrawScope.shelfshopScene(a: ArtCtx) {}
internal fun DrawScope.lawhScene(a: ArtCtx) {}
internal fun DrawScope.plazaScene(a: ArtCtx) {}
internal fun DrawScope.stallScene(a: ArtCtx) {}
internal fun DrawScope.tentScene(a: ArtCtx) {}
internal fun DrawScope.posed14(hip: Offset, neck: Offset, handA: Offset, handB: Offset, footA: Offset, footB: Offset, tall: Float, color: Color, alpha: Float = 1f) {}
internal fun DrawScope.nabiLight14(c: Offset, r: Float, strength: Float, t: Float, name: TextLayoutResult?, nameAt: Offset) {}
internal fun DrawScope.tick14(c: Offset, r: Float, p: Float, color: Color = Color(0xFF2E9A55)) {}
internal fun DrawScope.cross14(c: Offset, r: Float, p: Float, color: Color = Color(0xFFD23C30)) {}
internal fun DrawScope.scroll14(c: Offset, wd: Float, ht: Float, open: Float, alpha: Float) {}
internal fun DrawScope.astrolabeScene(a: ArtCtx) {}
internal fun DrawScope.stageScene(a: ArtCtx) {}
internal fun DrawScope.edenScene(a: ArtCtx) {}
internal fun DrawScope.qMark14(c: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.casketScene(a: ArtCtx) {}
internal fun DrawScope.globeScene(a: ArtCtx) {}
internal fun DrawScope.plainScene(a: ArtCtx) {}
internal fun DrawScope.riverScene(a: ArtCtx) {}
internal fun DrawScope.millaScene(a: ArtCtx) {}
internal fun DrawScope.crow18(c: Offset, s: Float, t: Float, color: Color, alpha: Float) {}
internal fun DrawScope.boothScene(a: ArtCtx) {}
internal fun DrawScope.omenroadScene(a: ArtCtx) {}
internal fun DrawScope.pitroadScene(a: ArtCtx) {}
internal fun DrawScope.buildingScene(a: ArtCtx) {}
internal fun DrawScope.gateScene(a: ArtCtx) {}
internal fun DrawScope.shrineScene(a: ArtCtx) {}
internal fun DrawScope.hourglassScene(a: ArtCtx) {}
internal fun DrawScope.qmark21(c: Offset, h: Float, color: Color, alpha: Float) {}
internal fun DrawScope.cross21(c: Offset, r: Float, color: Color, p: Float, width: Float) {}
internal fun DrawScope.struckTag21(tl: TextLayoutResult?, c: Offset, alpha: Float, strike: Float, red: Color, back: Color, ink: Color) {}
internal fun DrawScope.check21(c: Offset, r: Float, color: Color, p: Float, width: Float) {}
internal fun DrawScope.spine21(c: Offset, w: Float, h: Float, color: Color, bands: Int, alpha: Float = 1f) {}
internal fun DrawScope.openBook21(c: Offset, w: Float, page: Color, rule: Color, alpha: Float, glow: Float = 0f) {}
internal fun DrawScope.padlock21(c: Offset, s: Float, body: Color, open: Float, alpha: Float = 1f, rot: Float = 0f) {}
internal fun DrawScope.key21(c: Offset, s: Float, color: Color, alpha: Float = 1f, rot: Float = 0f) {}
internal fun DrawScope.locksScene(a: ArtCtx) {}
internal fun DrawScope.roadsScene(a: ArtCtx) {}
internal fun DrawScope.forkScene(a: ArtCtx) {}
internal fun DrawScope.centuriesScene(a: ArtCtx) {}
internal fun DrawScope.galaxy22(c: Offset, r: Float, rot: Float, alpha: Float, tint: Color = Color(0xFFCFD8FF)) {}
internal fun DrawScope.clock22(c: Offset, r: Float, color: Color, spin: Float, alpha: Float) {}
internal fun DrawScope.grid22(c: Offset, wide: Float, deep: Float, color: Color, open: Float, alpha: Float) {}
internal fun DrawScope.die22(c: Offset, s: Float, spots: Int, rot: Float, alpha: Float) {}
internal fun DrawScope.cosmosScene(a: ArtCtx) {}
internal fun DrawScope.billiardScene(a: ArtCtx) {}
internal fun DrawScope.dominoesScene(a: ArtCtx) {}
internal fun DrawScope.panelsScene(a: ArtCtx) {}
internal fun DrawScope.forgeScene(a: ArtCtx) {}
internal fun DrawScope.scalesScene(a: ArtCtx) {}
internal fun DrawScope.desertScene(a: ArtCtx) {}
internal fun DrawScope.cellScene(a: ArtCtx) {}
internal fun DrawScope.planksScene(a: ArtCtx) {}
internal fun DrawScope.labScene(a: ArtCtx) {}
internal fun DrawScope.screenScene(a: ArtCtx) {}
internal fun DrawScope.kunafaScene(a: ArtCtx) {}
internal fun DrawScope.sieveScene(a: ArtCtx) {}
internal fun DrawScope.kitchenScene(a: ArtCtx) {}
internal fun DrawScope.karyotypeScene(a: ArtCtx) {}
internal fun DrawScope.anatomyScene(a: ArtCtx) {}
internal fun DrawScope.fitBig26(tl: TextLayoutResult?, c: Offset, maxW: Float, color: Color, alpha: Float, glow: Color? = null) {}
internal fun DrawScope.angelLight26(c: Offset, r: Float, t: Float, alpha: Float) {}
internal fun DrawScope.camel26(foot: Offset, s: Float, color: Color, alpha: Float, t: Float, walk: Float = 0f) {}
internal fun DrawScope.sheep26(c: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.heightsScene(a: ArtCtx) {}
internal fun archPath26(cx: Float, top: Float, bottom: Float, hw: Float): Path = TODO()
internal fun DrawScope.revelationScene(a: ArtCtx) {}
internal fun DrawScope.gazeScene(a: ArtCtx) {}
internal fun DrawScope.resurrectionScene(a: ArtCtx) {}
internal fun DrawScope.nameswallScene(a: ArtCtx) {}
internal fun DrawScope.registerScene(a: ArtCtx) {}
internal fun DrawScope.unseenScene(a: ArtCtx) {}
internal fun DrawScope.tensignsScene(a: ArtCtx) {}
internal fun DrawScope.barzakhScene(a: ArtCtx) {}
internal fun DrawScope.idol27(foot: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.fourrulesScene(a: ArtCtx) {}
internal fun DrawScope.anwatScene(a: ArtCtx) {}
internal fun DrawScope.offeringScene(a: ArtCtx) {}
internal fun DrawScope.stormScene(a: ArtCtx) {}
internal fun DrawScope.twosealsScene(a: ArtCtx) {}
internal fun DrawScope.plaingraveScene(a: ArtCtx) {}
internal fun DrawScope.footstepsScene(a: ArtCtx) {}
internal fun DrawScope.sixrootsScene(a: ArtCtx) {}
internal fun DrawScope.courtsScene(a: ArtCtx) {}
internal fun DrawScope.verseguardScene(a: ArtCtx) {}
internal fun DrawScope.heartjarScene(a: ArtCtx) {}
internal fun DrawScope.decreeScene(a: ArtCtx) {}
internal fun DrawScope.wholemanScene(a: ArtCtx) {}
internal fun DrawScope.heartlightsScene(a: ArtCtx) {}
internal fun DrawScope.threelivesScene(a: ArtCtx) {}
internal fun DrawScope.kaaba29(foot: Offset, s: Float, alpha: Float) {}
internal fun DrawScope.domed29(foot: Offset, s: Float, body: Color, dome: Color, alpha: Float) {}
internal fun DrawScope.twolandsScene(a: ArtCtx) {}
internal fun DrawScope.proofscrollScene(a: ArtCtx) {}
internal fun DrawScope.lastbrickScene(a: ArtCtx) {}
internal fun DrawScope.nightjourneyScene(a: ArtCtx) {}
internal fun DrawScope.nightScene(a: ArtCtx) {}
internal fun DrawScope.grainScene(a: ArtCtx) {}
internal fun DrawScope.vesselsScene(a: ArtCtx) {}
internal fun DrawScope.caveScene(a: ArtCtx) {}
internal fun DrawScope.allegianceScene(a: ArtCtx) {}
internal fun DrawScope.companionsScene(a: ArtCtx) {}
internal fun DrawScope.jamaaScene(a: ArtCtx) {}
internal fun DrawScope.householdScene(a: ArtCtx) {}
internal fun DrawScope.twowingsScene(a: ArtCtx) {}
internal fun DrawScope.dawnfearScene(a: ArtCtx) {}
internal fun DrawScope.springScene(a: ArtCtx) {}
internal fun DrawScope.elephant32(c: Offset, s: Float, alpha: Float, glow: Float, t: Float) {}
internal fun DrawScope.elephantScene(a: ArtCtx) {}
internal fun DrawScope.detectorScene(a: ArtCtx) {}
internal fun DrawScope.safaScene(a: ArtCtx) {}
internal fun DrawScope.mistpathScene(a: ArtCtx) {}
internal fun DrawScope.tawaturScene(a: ArtCtx) {}
internal fun DrawScope.brethrenScene(a: ArtCtx) {}
internal fun DrawScope.threepeaksScene(a: ArtCtx) {}
internal fun DrawScope.kedarScene(a: ArtCtx) {}
internal fun DrawScope.openhouseScene(a: ArtCtx) {}
internal fun DrawScope.vineyardScene(a: ArtCtx) {}
internal fun DrawScope.statueScene(a: ArtCtx) {}
internal fun DrawScope.sceptreScene(a: ArtCtx) {}
internal fun DrawScope.awaitedScene(a: ArtCtx) {}
internal fun DrawScope.overseerScene(a: ArtCtx) {}
internal fun DrawScope.mythsScene(a: ArtCtx) {}
internal fun DrawScope.gatesScene(a: ArtCtx) {}
internal fun DrawScope.nebulaScene(a: ArtCtx) {}
internal fun DrawScope.earthmoonScene(a: ArtCtx) {}
internal fun DrawScope.twowatersScene(a: ArtCtx) {}
internal fun DrawScope.spineScene(a: ArtCtx) {}
internal fun DrawScope.nightglobeScene(a: ArtCtx) {}
internal fun DrawScope.listenerScene(a: ArtCtx) {}
internal fun DrawScope.challengeScene(a: ArtCtx) {}
internal fun DrawScope.lifelineScene(a: ArtCtx) {}
internal fun DrawScope.darkvaleScene(a: ArtCtx) {}
internal fun DrawScope.foretoldScene(a: ArtCtx) {}
internal fun DrawScope.siffinScene(a: ArtCtx) {}
internal fun DrawScope.conquestsScene(a: ArtCtx) {}
internal fun DrawScope.skylineScene(a: ArtCtx) {}
internal fun DrawScope.greenarabiaScene(a: ArtCtx) {}
internal fun DrawScope.cloud38(c: Offset, r: Float, color: Color, alpha: Float) {}
internal fun DrawScope.fogstonesScene(a: ArtCtx) {}
internal fun DrawScope.sunproofScene(a: ArtCtx) {}
internal fun DrawScope.postmanScene(a: ArtCtx) {}
internal fun DrawScope.acidbaseScene(a: ArtCtx) {}
internal fun DrawScope.clinicScene(a: ArtCtx) {}
internal fun DrawScope.popcornScene(a: ArtCtx) {}
internal fun DrawScope.madroadScene(a: ArtCtx) {}
internal fun DrawScope.trialroadScene(a: ArtCtx) {}
internal fun DrawScope.thermoScene(a: ArtCtx) {}
internal fun DrawScope.fourgatesScene(a: ArtCtx) {}
internal fun DrawScope.examtrainScene(a: ArtCtx) {}
internal fun DrawScope.lifeguardScene(a: ArtCtx) {}
internal fun DrawScope.howwhyScene(a: ArtCtx) {}
internal fun DrawScope.twopathsScene(a: ArtCtx) {}
internal fun DrawScope.teacherdeskScene(a: ArtCtx) {}
internal fun DrawScope.steadyrevelationScene(a: ArtCtx) {}
internal fun DrawScope.isolatedboxScene(a: ArtCtx) {}
internal fun DrawScope.stormcauseScene(a: ArtCtx) {}
internal fun DrawScope.datedscrollsScene(a: ArtCtx) {}
internal fun DrawScope.tworamsScene(a: ArtCtx) {}
internal fun DrawScope.dotsvoiceScene(a: ArtCtx) {}
internal fun DrawScope.palimpsestScene(a: ArtCtx) {}
internal fun DrawScope.sevenstreamsScene(a: ArtCtx) {}
internal fun DrawScope.stairstepsScene(a: ArtCtx) {}
internal fun DrawScope.sunbowScene(a: ArtCtx) {}
internal fun DrawScope.caveboulderScene(a: ArtCtx) {}
internal fun DrawScope.wagesroomScene(a: ArtCtx) {}
internal fun DrawScope.justscaleScene(a: ArtCtx) {}
internal fun DrawScope.templesandsScene(a: ArtCtx) {}
internal fun DrawScope.twoladdersScene(a: ArtCtx) {}
internal fun DrawScope.bridgegapScene(a: ArtCtx) {}
internal fun DrawScope.scrollrackScene(a: ArtCtx) {}
internal fun DrawScope.chainlinksScene(a: ArtCtx) {}
internal fun DrawScope.onemessengerScene(a: ArtCtx) {}
internal fun DrawScope.cloakfullScene(a: ArtCtx) {}
internal fun DrawScope.hundredswapScene(a: ArtCtx) {}
internal fun DrawScope.courtgateScene(a: ArtCtx) {}
internal fun DrawScope.emptyhouseScene(a: ArtCtx) {}
internal fun DrawScope.lamphousesScene(a: ArtCtx) {}
internal fun DrawScope.threepartsScene(a: ArtCtx) {}
internal fun DrawScope.shieldtentScene(a: ArtCtx) {}
internal fun DrawScope.ledgerscaleScene(a: ArtCtx) {}
internal fun DrawScope.fairhousesScene(a: ArtCtx) {}
internal fun DrawScope.truemeasureScene(a: ArtCtx) {}
internal fun DrawScope.brainsetScene(a: ArtCtx) {}
internal fun DrawScope.falsewavesScene(a: ArtCtx) {}
internal fun DrawScope.whymuslimScene(a: ArtCtx) {}
internal fun DrawScope.surrenderScene(a: ArtCtx) {}
internal fun DrawScope.parentsgateScene(a: ArtCtx) {}
internal fun DrawScope.kinthreadsScene(a: ArtCtx) {}
internal fun DrawScope.neighborsScene(a: ArtCtx) {}
internal fun DrawScope.orphangardenScene(a: ArtCtx) {}
internal fun DrawScope.homeledgerScene(a: ArtCtx) {}
internal fun DrawScope.sixrightsScene(a: ArtCtx) {}
internal fun DrawScope.sickroomScene(a: ArtCtx) {}
internal fun DrawScope.reconcileScene(a: ArtCtx) {}
internal fun DrawScope.heavyscaleScene(a: ArtCtx) {}
internal fun DrawScope.gentlerainScene(a: ArtCtx) {}
internal fun DrawScope.pardonbookScene(a: ArtCtx) {}
internal fun DrawScope.townsquareScene(a: ArtCtx) {}
internal fun DrawScope.truthroadScene(a: ArtCtx) {}
internal fun DrawScope.mercyrainScene(a: ArtCtx) {}
internal fun DrawScope.guestnightScene(a: ArtCtx) {}
internal fun DrawScope.brightfaceScene(a: ArtCtx) {}
internal fun DrawScope.tonguegateScene(a: ArtCtx) {}
internal fun DrawScope.backbiteScene(a: ArtCtx) {}
internal fun DrawScope.rumorchainScene(a: ArtCtx) {}
internal fun DrawScope.cursereturnScene(a: ArtCtx) {}
internal fun DrawScope.watchScene(a: ArtCtx) {}
internal fun DrawScope.crowdScene(a: ArtCtx) {}
internal fun DrawScope.deedsScene(a: ArtCtx) {}
internal fun DrawScope.campScene(a: ArtCtx) {}
internal fun DrawScope.giftsScene(a: ArtCtx) {}
internal fun DrawScope.turnbackScene(a: ArtCtx) {}
internal fun DrawScope.salamstreetScene(a: ArtCtx) {}
internal fun DrawScope.threeknocksScene(a: ArtCtx) {}
internal fun DrawScope.sharedtrayScene(a: ArtCtx) {}
internal fun DrawScope.majlisringScene(a: ArtCtx) {}
internal fun DrawScope.wakedawnScene(a: ArtCtx) {}
internal fun DrawScope.newrobeScene(a: ArtCtx) {}
internal fun DrawScope.eightgatesScene(a: ArtCtx) {}
internal fun DrawScope.threewordsScene(a: ArtCtx) {}
internal fun DrawScope.rightfootScene(a: ArtCtx) {}
internal fun DrawScope.afterprayerScene(a: ArtCtx) {}
internal fun DrawScope.twodoorsScene(a: ArtCtx) {}
internal fun DrawScope.morningeveningScene(a: ArtCtx) {}
internal fun DrawScope.covenantScene(a: ArtCtx) {}
internal fun DrawScope.noharmScene(a: ArtCtx) {}
internal fun DrawScope.bedtimeScene(a: ArtCtx) {}
internal fun DrawScope.nightdoorsScene(a: ArtCtx) {}
internal fun DrawScope.springheartScene(a: ArtCtx) {}
internal fun DrawScope.deepseaScene(a: ArtCtx) {}
internal fun DrawScope.campguardScene(a: ArtCtx) {}
internal fun DrawScope.roughgroundScene(a: ArtCtx) {}
internal fun DrawScope.mountaindebtScene(a: ArtCtx) {}
internal fun DrawScope.betterjarScene(a: ArtCtx) {}
internal fun DrawScope.healingbedScene(a: ArtCtx) {}
internal fun DrawScope.rainwindScene(a: ArtCtx) {}
internal fun DrawScope.journeyScene(a: ArtCtx) {}
internal fun DrawScope.iftarScene(a: ArtCtx) {}
internal fun DrawScope.sneezebookScene(a: ArtCtx) {}
internal fun DrawScope.weddinglightsScene(a: ArtCtx) {}
internal fun DrawScope.alivetreeScene(a: ArtCtx) {}
internal fun DrawScope.lightheavyScene(a: ArtCtx) {}
internal fun DrawScope.tenfoldScene(a: ArtCtx) {}
internal fun DrawScope.seventyhundredScene(a: ArtCtx) {}
internal fun DrawScope.raisedhandsScene(a: ArtCtx) {}
internal fun DrawScope.intentlampScene(a: ArtCtx) {}
internal fun DrawScope.questionerScene(a: ArtCtx) {}
internal fun DrawScope.fivepillarsScene(a: ArtCtx) {}
internal fun DrawScope.fourwritScene(a: ArtCtx) {}
internal fun DrawScope.raddScene(a: ArtCtx) {}
internal fun DrawScope.himaedgeScene(a: ArtCtx) {}
internal fun DrawScope.fivecounselScene(a: ArtCtx) {}
internal fun DrawScope.threeshownScene(a: ArtCtx) {}
internal fun DrawScope.canbearScene(a: ArtCtx) {}
internal fun DrawScope.goodonlyScene(a: ArtCtx) {}
internal fun DrawScope.doubtfreeScene(a: ArtCtx) {}
internal fun DrawScope.notmyconcernScene(a: ArtCtx) {}
internal fun DrawScope.likeforbrotherScene(a: ArtCtx) {}
internal fun DrawScope.inviolableScene(a: ArtCtx) {}
internal fun DrawScope.goodorsilentScene(a: ArtCtx) {}
internal fun DrawScope.bigText(tl: TextLayoutResult?, c: Offset, sc: Float, color: Color, alpha: Float) {}
internal fun DrawScope.amuletScene(a: ArtCtx) {}
internal fun DrawScope.knotsScene(a: ArtCtx) {}
internal fun DrawScope.lightsScene(a: ArtCtx) {}
internal fun DrawScope.ranksScene(a: ArtCtx) {}
internal fun DrawScope.pillarsScene(a: ArtCtx) {}
internal fun DrawScope.neonScene(a: ArtCtx) {}
internal fun DrawScope.oathScene(a: ArtCtx) {}
internal fun DrawScope.kinScene(a: ArtCtx) {}
internal fun DrawScope.angeremberScene(a: ArtCtx) {}
internal fun DrawScope.ihsaneverythingScene(a: ArtCtx) {}
internal fun DrawScope.taqwaeverywhereScene(a: ArtCtx) {}
internal fun DrawScope.guardhimScene(a: ArtCtx) {}
internal fun DrawScope.modestyScene(a: ArtCtx) {}
internal fun DrawScope.straightpathScene(a: ArtCtx) {}
internal fun DrawScope.intojannaScene(a: ArtCtx) {}
internal fun DrawScope.halfofimanScene(a: ArtCtx) {}
internal fun DrawScope.noinjusticeScene(a: ArtCtx) {}
internal fun DrawScope.notonlywealthScene(a: ArtCtx) {}
internal fun DrawScope.everyjointScene(a: ArtCtx) {}
internal fun DrawScope.askyourheartScene(a: ArtCtx) {}
internal fun DrawScope.holdfastScene(a: ArtCtx) {}
internal fun DrawScope.headofmatterScene(a: ArtCtx) {}
internal fun DrawScope.fourrulingsScene(a: ArtCtx) {}
internal fun DrawScope.zuhdloveScene(a: ArtCtx) {}
internal fun DrawScope.harmblockedScene(a: ArtCtx) {}
internal fun DrawScope.proofonclaimantScene(a: ArtCtx) {}
internal fun DrawScope.changeevilScene(a: ArtCtx) {}
internal fun DrawScope.nojealousyScene(a: ArtCtx) {}
internal fun DrawScope.relievegriefScene(a: ArtCtx) {}
internal fun DrawScope.recordbookScene(a: ArtCtx) {}
internal fun DrawScope.awliyaScene(a: ArtCtx) {}
internal fun DrawScope.pardonedScene(a: ArtCtx) {}
internal fun DrawScope.strangerScene(a: ArtCtx) {}
internal fun DrawScope.followsrevealedScene(a: ArtCtx) {}
internal fun DrawScope.skyhighforgiveScene(a: ArtCtx) {}
internal fun DrawScope.moonfaceScene(a: ArtCtx) {}
internal fun DrawScope.garmentsScene(a: ArtCtx) {}
internal fun DrawScope.humbletableScene(a: ArtCtx) {}
internal fun DrawScope.namesringScene(a: ArtCtx) {}
internal fun DrawScope.simplelivingScene(a: ArtCtx) {}
internal fun DrawScope.clearspeechScene(a: ArtCtx) {}
internal fun DrawScope.truejestScene(a: ArtCtx) {}
internal fun DrawScope.gratefulservantScene(a: ArtCtx) {}
internal fun DrawScope.recitelightScene(a: ArtCtx) {}
internal fun DrawScope.onlyaservantScene(a: ArtCtx) {}
internal fun DrawScope.noblemannersScene(a: ArtCtx) {}
internal fun DrawScope.lastdayScene(a: ArtCtx) {}
internal fun DrawScope.echoadhanScene(a: ArtCtx) {}
internal fun DrawScope.hajjpillarScene(a: ArtCtx) {}
internal fun DrawScope.beforehajjScene(a: ArtCtx) {}
internal fun DrawScope.miqatwhiteScene(a: ArtCtx) {}
internal fun DrawScope.fivemiqatScene(a: ArtCtx) {}
internal fun DrawScope.threenusukScene(a: ArtCtx) {}
internal fun DrawScope.childpilgrimScene(a: ArtCtx) {}
internal class Shot(val at: Offset, val zoom: Float)
internal fun shotsFor(a: ArtCtx, byKind: Map<String, Shot>): List<Shot?> = TODO()
internal fun DrawScope.followBeats(a: ArtCtx, shots: List<Shot?>, wide: Shot, dim: Float = 0.28f, block: DrawScope.() -> Unit) {}
internal object Roundel {
    val Fills = listOf(Color(0xFF0E3B2E), Color(0xFF1B2A5C), Color(0xFF5A1E2B), Color(0xFF3D2A10))
    val Rim = Color(0xFFE7C77A)
    val Name = Color(0xFFF6E7BE)
    val PlainFill = Color(0xFF3A3F4B)
    val PlainRim = Color(0xFFB4B8C2)
}
internal fun DrawScope.nameRoundel(c: Offset, r: Float, name: TextLayoutResult?, fill: Color, rim: Color, grow: Float, ink: Color = Roundel.Name) {}
internal fun DrawScope.callout(c: Offset, tl: TextLayoutResult?, maxW: Float, alpha: Float, ink: Color, rim: Color) {}
internal fun DrawScope.inkScene(a: ArtCtx) {}
internal fun DrawScope.lensScene(a: ArtCtx) {}
internal fun DrawScope.zelligeScene(a: ArtCtx) {}
internal fun DrawScope.studyScene(a: ArtCtx) {}
internal fun DrawScope.canyonScene(a: ArtCtx) {}
internal fun DrawScope.mapScene(a: ArtCtx) {}
internal fun DrawScope.booksScene(a: ArtCtx) {}
internal fun DrawScope.caravanScene(a: ArtCtx) {}
internal fun DrawScope.ihramlimitsScene(a: ArtCtx) {}
internal fun DrawScope.sevenroundsScene(a: ArtCtx) {}
internal fun DrawScope.arafatdayScene(a: ArtCtx) {}
internal fun DrawScope.nahrdayScene(a: ArtCtx) {}
internal fun DrawScope.hadyofferScene(a: ArtCtx) {}
internal fun DrawScope.farewellhouseScene(a: ArtCtx) {}
internal fun DrawScope.eidofferingScene(a: ArtCtx) {}
internal fun DrawScope.fourconditionsScene(a: ArtCtx) {}
internal fun DrawScope.bestofferingScene(a: ArtCtx) {}
internal fun DrawScope.whocoversScene(a: ArtCtx) {}
internal fun DrawScope.threethirdsScene(a: ArtCtx) {}
internal fun DrawScope.dhulhijjanailsScene(a: ArtCtx) {}
internal fun DrawScope.ramNight72(t: Float, ground: Float, moon: Float = 1f) {}
internal fun DrawScope.gate72(sill: Offset, wd: Float, ht: Float, open: Float, frame: Color, glow: Color, alpha: Float, t: Float) {}
internal fun DrawScope.chain72(from: Offset, to: Offset, link: Float, color: Color, alpha: Float) {}
internal fun DrawScope.dateBowl72(foot: Offset, s: Float, alpha: Float) {}
internal fun DrawScope.quill72(nib: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.ramadangatesScene(a: ArtCtx) {}
internal fun DrawScope.fastsecretScene(a: ArtCtx) {}
internal fun DrawScope.hilalwatchScene(a: ArtCtx) {}
internal fun DrawScope.whomustfastScene(a: ArtCtx) {}
internal fun DrawScope.travelersickScene(a: ArtCtx) {}
internal fun DrawScope.excusedaysScene(a: ArtCtx) {}
internal fun DrawScope.lying73(head: Offset, len: Float, color: Color, alpha: Float) {}
internal fun DrawScope.syringe73(c: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.drip73(foot: Offset, s: Float, alpha: Float, t: Float) {}
internal fun DrawScope.eye73(c: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.ear73(c: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.lips73(c: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.saa73(foot: Offset, s: Float, fill: Float, grain: Color, alpha: Float) {}
internal fun DrawScope.heap73(c: Offset, s: Float, color: Color, alpha: Float, round: Boolean = true) {}
internal fun DrawScope.hourglass73(c: Offset, ht: Float, p: Float, sand: Color, alpha: Float, frame: Color = Color(0xFFD8C8A0)) {}
internal fun DrawScope.fastmannersScene(a: ArtCtx) {}
internal fun DrawScope.suhooriftarScene(a: ArtCtx) {}
internal fun DrawScope.fastbreakersScene(a: ArtCtx) {}
internal fun DrawScope.forgotfastScene(a: ArtCtx) {}
internal fun DrawScope.lasttenScene(a: ArtCtx) {}
internal fun DrawScope.fitrsaaScene(a: ArtCtx) {}
internal fun DrawScope.hall74(floor: Float, t: Float, lamps: Float = 1f) {}
internal fun DrawScope.row74(y: Float, x0: Float, x1: Float, n: Int, tall: Float, color: Color, alpha: Float, pose: Float = 0f) {}
internal fun DrawScope.rakah74(foot: Offset, s: Float, lit: Float, alpha: Float, odd: Boolean = false) {}
internal fun DrawScope.tarawihnightScene(a: ArtCtx) {}
internal fun DrawScope.rakatcountScene(a: ArtCtx) {}
internal fun DrawScope.withimamScene(a: ArtCtx) {}
internal fun DrawScope.witrcloseScene(a: ArtCtx) {}
internal fun DrawScope.qunuthandsScene(a: ArtCtx) {}
internal fun DrawScope.itikafstayScene(a: ArtCtx) {}
internal fun DrawScope.mosqueonlyScene(a: ArtCtx) {}
internal fun DrawScope.itikaftimesScene(a: ArtCtx) {}
internal fun DrawScope.leavingmosqueScene(a: ArtCtx) {}
internal fun DrawScope.seclusionadviceScene(a: ArtCtx) {}
internal fun DrawScope.foot76(c: Offset, s: Float, color: Color, alpha: Float, right: Boolean) {}
internal fun DrawScope.glass76(c: Offset, s: Float, murk: Float, alpha: Float) {}
internal fun DrawScope.bowl76(foot: Offset, s: Float, color: Color, alpha: Float) {}
internal fun DrawScope.purewaterScene(a: ArtCtx) {}
internal fun DrawScope.vesselrulesScene(a: ArtCtx) {}
internal fun DrawScope.restroomwayScene(a: ArtCtx) {}
internal fun DrawScope.miswakScene(a: ArtCtx) {}
internal fun DrawScope.wudusixScene(a: ArtCtx) {}
internal fun DrawScope.wudusunnahsScene(a: ArtCtx) {}
internal fun DrawScope.wudubreakersScene(a: ArtCtx) {}
internal fun DrawScope.khuffwipeScene(a: ArtCtx) {}
internal fun DrawScope.castwipeScene(a: ArtCtx) {}
internal fun DrawScope.fullwashScene(a: ArtCtx) {}
internal fun DrawScope.dryablutionScene(a: ArtCtx) {}
internal fun DrawScope.impuritiesScene(a: ArtCtx) {}
internal fun DrawScope.naturalcycleScene(a: ArtCtx) {}
internal fun DrawScope.cyclechangesScene(a: ArtCtx) {}
internal fun DrawScope.excusedprayerScene(a: ArtCtx) {}
internal fun DrawScope.excusedfastScene(a: ArtCtx) {}
internal fun DrawScope.continuousflowScene(a: ArtCtx) {}
internal fun DrawScope.afterbirthScene(a: ArtCtx) {}
internal fun DrawScope.zakatgrowthScene(a: ArtCtx) {}
internal fun DrawScope.zakatcondsScene(a: ArtCtx) {}
internal fun DrawScope.goldnisabScene(a: ArtCtx) {}
internal fun DrawScope.tradegoodsScene(a: ArtCtx) {}
internal fun DrawScope.harvestshareScene(a: ArtCtx) {}
internal fun DrawScope.livestockzakatScene(a: ArtCtx) {}
internal fun DrawScope.eightrecipientsScene(a: ArtCtx) {}
internal fun w8EaseIn(x: Float): Float = TODO()
internal fun w8Rnd(k: Int, salt: Float): Float = TODO()
internal fun w8Poly(pts: List<Offset>): Path = TODO()
internal fun w8Smooth(ctrl: List<Offset>, per: Int = 10): List<Offset> = TODO()
internal fun w8Along(pts: List<Offset>, u: Float): Offset = TODO()
internal fun w8Head(pts: List<Offset>, p: Float): List<Offset> = TODO()
internal fun w8Band(pts: List<Offset>, half: (Float) -> Float): Path = TODO()
internal fun DrawScope.w8Roundel(c: Offset, r: Float, grow: Float) {}
internal fun w8Heart(c: Offset, r: Float): Path = TODO()
internal fun DrawScope.w8Seated(seat: Offset, tall: Float, color: Color, facing: Float, alpha: Float = 1f, speak: Float = 0f) {}
internal fun DrawScope.w8Worshipper(foot: Offset, tall: Float, color: Color, raise: Float, bow: Float, alpha: Float = 1f) {}
internal fun DrawScope.w8Palm(base: Offset, tall: Float, trunk: Color, leaf: Color, t: Float, alpha: Float = 1f) {}
internal fun DrawScope.atharScene(a: ArtCtx) {}
internal fun DrawScope.deltaScene(a: ArtCtx) {}
internal fun DrawScope.loomScene(a: ArtCtx) {}
internal fun DrawScope.sabilScene(a: ArtCtx) {}
internal fun DrawScope.shoreScene(a: ArtCtx) {}
internal fun DrawScope.takbir80(foot: Offset, tall: Float, color: Color, alpha: Float, raise: Float) {}
internal fun DrawScope.kneel80(seat: Offset, tall: Float, color: Color, alpha: Float, point: Float = 0f) {}
internal fun DrawScope.prayerrankScene(a: ArtCtx) {}
internal fun DrawScope.adhancallScene(a: ArtCtx) {}
internal fun DrawScope.fivetimesScene(a: ArtCtx) {}
internal fun DrawScope.prayercondsScene(a: ArtCtx) {}
internal fun DrawScope.walktoprayerScene(a: ArtCtx) {}
internal fun DrawScope.openingtakbirScene(a: ArtCtx) {}
internal fun sevenPoints81(foot: Offset, tall: Float): List<Offset> = TODO()
internal fun DrawScope.recitationScene(a: ArtCtx) {}
internal fun DrawScope.bowingScene(a: ArtCtx) {}
internal fun DrawScope.prostrationScene(a: ArtCtx) {}
internal fun DrawScope.betweensujudScene(a: ArtCtx) {}
internal fun DrawScope.tashahhudScene(a: ArtCtx) {}
internal fun DrawScope.pillarsofprayerScene(a: ArtCtx) {}
internal fun DrawScope.wajibatprayerScene(a: ArtCtx) {}
internal fun DrawScope.dislikedactsScene(a: ArtCtx) {}
internal fun DrawScope.forgetfulnessScene(a: ArtCtx) {}
internal fun DrawScope.postprayerdhikrScene(a: ArtCtx) {}
internal fun DrawScope.twelverakatScene(a: ArtCtx) {}
internal fun DrawScope.witrnightScene(a: ArtCtx) {}
internal fun DrawScope.forenoonScene(a: ArtCtx) {}
internal fun DrawScope.recitationbowScene(a: ArtCtx) {}
internal fun DrawScope.forbiddentimesScene(a: ArtCtx) {}
internal fun DrawScope.congregationScene(a: ArtCtx) {}
internal fun DrawScope.wholeadsScene(a: ArtCtx) {}
internal fun DrawScope.sickprayerScene(a: ArtCtx) {}
internal fun DrawScope.shortenedprayerScene(a: ArtCtx) {}
internal fun DrawScope.joiningprayersScene(a: ArtCtx) {}
internal fun DrawScope.fridayprayerScene(a: ArtCtx) {}
internal fun DrawScope.fridaymannersScene(a: ArtCtx) {}
internal fun DrawScope.eidprayerScene(a: ArtCtx) {}
internal fun DrawScope.shroud85(head: Offset, len: Float, alpha: Float, layers: Int = 3) {}
internal fun DrawScope.eclipseprayerScene(a: ArtCtx) {}
internal fun DrawScope.rainprayerScene(a: ArtCtx) {}
internal fun DrawScope.preparingdeceasedScene(a: ArtCtx) {}
internal fun DrawScope.funeralprayerScene(a: ArtCtx) {}
internal fun DrawScope.burialScene(a: ArtCtx) {}
internal fun DrawScope.rabbgardenScene(a: ArtCtx) {}
internal fun DrawScope.fatihasevenScene(a: ArtCtx) {}
internal fun DrawScope.falaqnasScene(a: ArtCtx) {}
internal fun DrawScope.samadneedsScene(a: ArtCtx) {}
internal fun DrawScope.masadsignScene(a: ArtCtx) {}
internal fun DrawScope.nasrdawnScene(a: ArtCtx) {}
internal fun DrawScope.baraaScene(a: ArtCtx) {}
internal fun DrawScope.kawtharriverScene(a: ArtCtx) {}
internal fun DrawScope.maunhouseScene(a: ArtCtx) {}
internal fun DrawScope.filqurayshScene(a: ArtCtx) {}
internal fun DrawScope.humazawealthScene(a: ArtCtx) {}
internal fun DrawScope.asrhourglassScene(a: ArtCtx) {}
internal fun DrawScope.visitorsScene(a: ArtCtx) {}
internal fun DrawScope.qariadayScene(a: ArtCtx) {}
internal fun DrawScope.atomweightScene(a: ArtCtx) {}
internal fun DrawScope.bayyinascrollScene(a: ArtCtx) {}
internal fun DrawScope.qadrnightScene(a: ArtCtx) {}
internal fun DrawScope.iqrafirstScene(a: ArtCtx) {}
internal fun DrawScope.tinmapScene(a: ArtCtx) {}
internal fun DrawScope.usryusraynScene(a: ArtCtx) {}
internal fun DrawScope.rulerScene(a: ArtCtx) {}
internal fun DrawScope.mosqueScene(a: ArtCtx) {}
internal fun DrawScope.mazeScene(a: ArtCtx) {}
internal fun DrawScope.fieldScene(a: ArtCtx) {}
internal fun DrawScope.streetScene(a: ArtCtx) {}
internal fun DrawScope.duhamorningScene(a: ArtCtx) {}
internal fun DrawScope.layleasyScene(a: ArtCtx) {}
internal fun DrawScope.nafsmirrorScene(a: ArtCtx) {}
internal fun DrawScope.kabadScene(a: ArtCtx) {}
internal fun DrawScope.fajrtenScene(a: ArtCtx) {}
internal fun DrawScope.ghashiyaearthScene(a: ArtCtx) {}
internal fun DrawScope.alaguideScene(a: ArtCtx) {}
internal fun DrawScope.tariqstarScene(a: ArtCtx) {}
internal fun DrawScope.burujskyScene(a: ArtCtx) {}
internal fun DrawScope.easyreckonScene(a: ArtCtx) {}
internal fun DrawScope.raynheartScene(a: ArtCtx) {}
internal fun DrawScope.kiramScene(a: ArtCtx) {}
internal fun DrawScope.takwirScene(a: ArtCtx) {}
internal fun DrawScope.abasaScene(a: ArtCtx) {}
internal fun DrawScope.naziatScene(a: ArtCtx) {}
internal fun DrawScope.nabaScene(a: ArtCtx) {}
internal fun DrawScope.shafibookScene(a: ArtCtx) {}
internal fun DrawScope.harfgoodScene(a: ArtCtx) {}
internal fun DrawScope.ruinhouseScene(a: ArtCtx) {}
internal fun DrawScope.nightreciteScene(a: ArtCtx) {}
internal fun DrawScope.mushaftouchScene(a: ArtCtx) {}
internal fun DrawScope.recitemannersScene(a: ArtCtx) {}
internal fun DrawScope.tilawasujudScene(a: ArtCtx) {}
internal fun DrawScope.sujuddhikrScene(a: ArtCtx) {}
internal fun daysAr(n: Long): String = TODO()
internal fun currentSeason(now: Long = System.currentTimeMillis()): Season? = TODO()
internal fun SeasonCard(season: Season, repo: ContentRepo, progress: ProgressStore, onOpen: (String) -> Unit) {}
internal fun ModuleRowView(name: String, done: Int, total: Int, started: Boolean, color: Color, onClick: () -> Unit) {}
