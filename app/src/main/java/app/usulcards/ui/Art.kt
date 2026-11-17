package app.usulcards.ui

import android.content.Context
import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import app.usulcards.data.Scene
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/*
 * Illustrated scenes ("art"): each card acts out its idea with objects and places — a key and its teeth, a
 * rope over a chasm, a ship in a storm — instead of animating its text. Everything is drawn in code (no image
 * files), so the app does not grow. Each scene item is a beat that plays when the voice reaches it; its `kind`
 * names its role in the scene, its `sub` is the short tag drawn in the picture ("a|b" for two tags), and its
 * `label` is the caption under the picture. What may be drawn, and how: tools/cards/CODEX.md.
 */

internal const val ART_ASPECT = 16f / 11f

/** Colours shared by every scene: ivory and gold light, brass, and the two signal tones. */
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

// ───────────── live scene and pure frame ─────────────

/** Live: each beat plays when its item is shown (when the voice reaches it); wind, water and light keep moving. */
@Composable
internal fun ArtScene(scene: Scene, shown: Int, showDiacritics: Boolean) {
    val film = LocalFilm.current
    if (film != null) {
        ArtFrame(scene, { film.beats }, { film.t }, shown, showDiacritics)
        return
    }
    val context = LocalContext.current
    val still = remember { animationsOff(context) }
    val anims = remember(scene) { List(scene.items.size) { Animatable(0f) } }
    scene.items.forEachIndexed { i, item ->
        val on = i < shown
        key(i) {
            LaunchedEffect(scene, on) {
                when {
                    !on -> anims[i].snapTo(0f)
                    still -> anims[i].snapTo(1f)
                    else -> anims[i].animateTo(1f, tween(beatMillis(item.kind), easing = LinearEasing))
                }
            }
        }
    }
    val clock = rememberArtClock(still)
    ArtFrame(scene, { anims.map { it.value } }, { clock.value }, shown, showDiacritics)
}

/** One frame: [beats] gives each item's progress (0 to 1), [time] the seconds of ambient motion. */
@Composable
internal fun ArtFrame(scene: Scene, beats: () -> List<Float>, time: () -> Float, shown: Int, showDiacritics: Boolean) {
    val pal = LocalPalette.current
    val txt: (String) -> String = { s -> if (showDiacritics) s else stripHarakat(s) }
    val tm = rememberTextMeasurer()
    val tags = remember(scene, showDiacritics) {
        val style = TextStyle(fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = ArtInk.Ivory)
        scene.items.map { item ->
            if (item.sub.isEmpty()) emptyList<TextLayoutResult>() else item.sub.split("|").map { tm.measure(txt(it.trim()), style) }
        }
    }
    val prophetName = remember(showDiacritics) {
        tm.measure(txt("رَسُولُ اللَّهِ ﷺ"), TextStyle(fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = ArtInk.Gold))
    }
    val extras = remember(scene, showDiacritics) {
        val style = TextStyle(fontFamily = Fonts.ui, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = ArtInk.Gold)
        scene.sides.map { tm.measure(txt(it.first), style) }
    }
    // words written large in calligraphy (a page of ink, names in roundels), only for the scenes that draw them
    val bigs = remember(scene, showDiacritics) {
        if (scene.layout !in BIG_TEXT_LAYOUTS) emptyList<List<TextLayoutResult>>() else {
            val style = TextStyle(fontFamily = Fonts.naskh, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = ArtInk.Ivory)
            // a part in ornate brackets ﴿ ﴾ is a verse: it is set in the Qur'an's own font
            val verse = TextStyle(fontFamily = Fonts.quran, fontSize = 24.sp, color = ArtInk.Ivory)
            scene.items.map { item ->
                if (item.sub.isEmpty()) emptyList<TextLayoutResult>() else item.sub.split("|").map {
                    val part = it.trim()
                    tm.measure(txt(part), if (part.startsWith("﴿") || part.endsWith("﴾")) verse else style)
                }
            }
        }
    }
    Column(Modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().aspectRatio(ART_ASPECT).clipToBounds()) {
            val bs = beats()
            val a = ArtCtx(scene.items.map { it.kind }, bs, time(), tags, extras, scene.items.map { it.side }, scene.items.map { it.value }, pal == DarkPalette, bigs)
            Cinema.darkFrame = a.dark
            Cinema.prophetName = prophetName
            Cinema.dayFrame = scene.layout in Cinema.day
            throughCamera(scene.layout, cameraProgress(bs), a.t) { drawLayout(scene.layout, a, pal.line) }
            drawAir(scene.layout, a.t, a.dark)
        }
        Box(Modifier.fillMaxWidth().background(pal.bg)) { SceneCaption(scene, shown, txt) }
    }
}

private fun DrawScope.drawLayout(layout: String, a: ArtCtx, fallback: Color) {
    when (layout) {
        "key" -> keyScene(a)
        "rope" -> ropeScene(a)
        "ship" -> shipScene(a)
        "lantern" -> lanternScene(a)
        "trail" -> trailScene(a)
        "ridge" -> ridgeScene(a)
        "tree" -> treeScene(a)
        "streams" -> streamsScene(a)
        "building" -> buildingScene(a)
        "gate" -> gateScene(a)
        "shrine" -> shrineScene(a)
        "hourglass" -> hourglassScene(a)
        "night" -> nightScene(a)
        "grain" -> grainScene(a)
        "vessels" -> vesselsScene(a)
        "cave" -> caveScene(a)
        "chart" -> chartScene(a)
        "circuit" -> circuitScene(a)
        "verdict" -> verdictScene(a)
        "board" -> boardScene(a)
        "watch" -> watchScene(a)
        "crowd" -> crowdScene(a)
        "deeds" -> deedsScene(a)
        "camp" -> campScene(a)
        "gifts" -> giftsScene(a)
        "amulet" -> amuletScene(a)
        "knots" -> knotsScene(a)
        "lights" -> lightsScene(a)
        "ranks" -> ranksScene(a)
        "pillars" -> pillarsScene(a)
        "neon" -> neonScene(a)
        "oath" -> oathScene(a)
        "kin" -> kinScene(a)
        "ink" -> inkScene(a)
        "lens" -> lensScene(a)
        "zellige" -> zelligeScene(a)
        "study" -> studyScene(a)
        "canyon" -> canyonScene(a)
        "map" -> mapScene(a)
        "books" -> booksScene(a)
        "caravan" -> caravanScene(a)
        "athar" -> atharScene(a)
        "delta" -> deltaScene(a)
        "loom" -> loomScene(a)
        "sabil" -> sabilScene(a)
        "shore" -> shoreScene(a)
        "ruler" -> rulerScene(a)
        "mosque" -> mosqueScene(a)
        "maze" -> mazeScene(a)
        "field" -> fieldScene(a)
        "street" -> streetScene(a)
        "atelier" -> atelierScene(a)
        "heavens" -> heavensScene(a)
        "chamber" -> chamberScene(a)
        "scroll" -> scrollScene(a)
        "majlis" -> majlisScene(a)
        "lane" -> laneScene(a)
        "hilltop" -> hilltopScene(a)
        "wayside" -> waysideScene(a)
        "flock" -> flockScene(a)
        "deck" -> deckScene(a)
        "dam" -> damScene(a)
        "aerial" -> aerialScene(a)
        "souk" -> soukScene(a)
        "bridge" -> bridgeScene(a)
        "refuge" -> refugeScene(a)
        "sapling" -> saplingScene(a)
        "return" -> returnScene(a)
        "shelfshop" -> shelfshopScene(a)
        "lawh" -> lawhScene(a)
        "plaza" -> plazaScene(a)
        "stall" -> stallScene(a)
        "tent" -> tentScene(a)
        "astrolabe" -> astrolabeScene(a)
        "stage" -> stageScene(a)
        "eden" -> edenScene(a)
        "casket" -> casketScene(a)
        "threads" -> threadsScene(a)
        "carpet" -> carpetScene(a)
        "bird" -> birdScene(a)
        "globe" -> globeScene(a)
        "plain" -> plainScene(a)
        "river" -> riverScene(a)
        "milla" -> millaScene(a)
        "booth" -> boothScene(a)
        "omenroad" -> omenroadScene(a)
        "pitroad" -> pitroadScene(a)
        "mankind" -> mankindScene(a)
        "obey4" -> obey4Scene(a)
        "triptych" -> triptychScene(a)
        "locks" -> locksScene(a)
        "roads" -> roadsScene(a)
        "fork" -> forkScene(a)
        "centuries" -> centuriesScene(a)
        "cosmos" -> cosmosScene(a)
        "billiard" -> billiardScene(a)
        "dominoes" -> dominoesScene(a)
        "panels" -> panelsScene(a)
        "forge" -> forgeScene(a)
        "scales" -> scalesScene(a)
        "desert" -> desertScene(a)
        "cell" -> cellScene(a)
        "planks" -> planksScene(a)
        "lab" -> labScene(a)
        "screen" -> screenScene(a)
        "kunafa" -> kunafaScene(a)
        "sieve" -> sieveScene(a)
        "kitchen" -> kitchenScene(a)
        "karyotype" -> karyotypeScene(a)
        "anatomy" -> anatomyScene(a)
        "heights" -> heightsScene(a)
        "revelation" -> revelationScene(a)
        "gaze" -> gazeScene(a)
        "resurrection" -> resurrectionScene(a)
        "nameswall" -> nameswallScene(a)
        "register" -> registerScene(a)
        "unseen" -> unseenScene(a)
        "tensigns" -> tensignsScene(a)
        "barzakh" -> barzakhScene(a)
        "fourrules" -> fourrulesScene(a)
        "anwat" -> anwatScene(a)
        "offering" -> offeringScene(a)
        "storm" -> stormScene(a)
        "twoseals" -> twosealsScene(a)
        "plaingrave" -> plaingraveScene(a)
        "footsteps" -> footstepsScene(a)
        "sixroots" -> sixrootsScene(a)
        "courts" -> courtsScene(a)
        "verseguard" -> verseguardScene(a)
        "heartjar" -> heartjarScene(a)
        "decree" -> decreeScene(a)
        "wholeman" -> wholemanScene(a)
        "heartlights" -> heartlightsScene(a)
        "threelives" -> threelivesScene(a)
        "twolands" -> twolandsScene(a)
        "proofscroll" -> proofscrollScene(a)
        "lastbrick" -> lastbrickScene(a)
        "nightjourney" -> nightjourneyScene(a)
        "allegiance" -> allegianceScene(a)
        "companions" -> companionsScene(a)
        "jamaa" -> jamaaScene(a)
        "household" -> householdScene(a)
        "twowings" -> twowingsScene(a)
        "dawnfear" -> dawnfearScene(a)
        "spring" -> springScene(a)
        "elephant" -> elephantScene(a)
        "detector" -> detectorScene(a)
        "safa" -> safaScene(a)
        "mistpath" -> mistpathScene(a)
        "tawatur" -> tawaturScene(a)
        "brethren" -> brethrenScene(a)
        "threepeaks" -> threepeaksScene(a)
        "kedar" -> kedarScene(a)
        "openhouse" -> openhouseScene(a)
        "vineyard" -> vineyardScene(a)
        "statue" -> statueScene(a)
        "sceptre" -> sceptreScene(a)
        "awaited" -> awaitedScene(a)
        "overseer" -> overseerScene(a)
        "myths" -> mythsScene(a)
        "gates" -> gatesScene(a)
        "nebula" -> nebulaScene(a)
        "earthmoon" -> earthmoonScene(a)
        "twowaters" -> twowatersScene(a)
        "spine" -> spineScene(a)
        "nightglobe" -> nightglobeScene(a)
        "listener" -> listenerScene(a)
        "challenge" -> challengeScene(a)
        "lifeline" -> lifelineScene(a)
        "darkvale" -> darkvaleScene(a)
        "foretold" -> foretoldScene(a)
        "siffin" -> siffinScene(a)
        "conquests" -> conquestsScene(a)
        "skyline" -> skylineScene(a)
        "greenarabia" -> greenarabiaScene(a)
        "fogstones" -> fogstonesScene(a)
        "sunproof" -> sunproofScene(a)
        "postman" -> postmanScene(a)
        "acidbase" -> acidbaseScene(a)
        "clinic" -> clinicScene(a)
        "popcorn" -> popcornScene(a)
        "madroad" -> madroadScene(a)
        "trialroad" -> trialroadScene(a)
        "thermo" -> thermoScene(a)
        "fourgates" -> fourgatesScene(a)
        "examtrain" -> examtrainScene(a)
        "lifeguard" -> lifeguardScene(a)
        "howwhy" -> howwhyScene(a)
        "twopaths" -> twopathsScene(a)
        "teacherdesk" -> teacherdeskScene(a)
        "steadyrevelation" -> steadyrevelationScene(a)
        "isolatedbox" -> isolatedboxScene(a)
        "stormcause" -> stormcauseScene(a)
        "datedscrolls" -> datedscrollsScene(a)
        "tworams" -> tworamsScene(a)
        "dotsvoice" -> dotsvoiceScene(a)
        "palimpsest" -> palimpsestScene(a)
        "sevenstreams" -> sevenstreamsScene(a)
        "stairsteps" -> stairstepsScene(a)
        "sunbow" -> sunbowScene(a)
        "caveboulder" -> caveboulderScene(a)
        "wagesroom" -> wagesroomScene(a)
        "justscale" -> justscaleScene(a)
        "templesands" -> templesandsScene(a)
        "twoladders" -> twoladdersScene(a)
        "bridgegap" -> bridgegapScene(a)
        "scrollrack" -> scrollrackScene(a)
        "chainlinks" -> chainlinksScene(a)
        "onemessenger" -> onemessengerScene(a)
        "cloakfull" -> cloakfullScene(a)
        "hundredswap" -> hundredswapScene(a)
        "courtgate" -> courtgateScene(a)
        "emptyhouse" -> emptyhouseScene(a)
        "lamphouses" -> lamphousesScene(a)
        "threeparts" -> threepartsScene(a)
        "shieldtent" -> shieldtentScene(a)
        "ledgerscale" -> ledgerscaleScene(a)
        "fairhouses" -> fairhousesScene(a)
        "truemeasure" -> truemeasureScene(a)
        "brainset" -> brainsetScene(a)
        "falsewaves" -> falsewavesScene(a)
        "whymuslim" -> whymuslimScene(a)
        "surrender" -> surrenderScene(a)
        "parentsgate" -> parentsgateScene(a)
        "kinthreads" -> kinthreadsScene(a)
        "neighbors" -> neighborsScene(a)
        "orphangarden" -> orphangardenScene(a)
        "homeledger" -> homeledgerScene(a)
        "sixrights" -> sixrightsScene(a)
        "sickroom" -> sickroomScene(a)
        "reconcile" -> reconcileScene(a)
        "heavyscale" -> heavyscaleScene(a)
        "gentlerain" -> gentlerainScene(a)
        "pardonbook" -> pardonbookScene(a)
        "townsquare" -> townsquareScene(a)
        "truthroad" -> truthroadScene(a)
        "mercyrain" -> mercyrainScene(a)
        "guestnight" -> guestnightScene(a)
        "brightface" -> brightfaceScene(a)
        "tonguegate" -> tonguegateScene(a)
        "backbite" -> backbiteScene(a)
        "rumorchain" -> rumorchainScene(a)
        "cursereturn" -> cursereturnScene(a)
        "turnback" -> turnbackScene(a)
        "salamstreet" -> salamstreetScene(a)
        "threeknocks" -> threeknocksScene(a)
        "sharedtray" -> sharedtrayScene(a)
        "majlisring" -> majlisringScene(a)
        "wakedawn" -> wakedawnScene(a)
        "newrobe" -> newrobeScene(a)
        "eightgates" -> eightgatesScene(a)
        "threewords" -> threewordsScene(a)
        "rightfoot" -> rightfootScene(a)
        "afterprayer" -> afterprayerScene(a)
        "twodoors" -> twodoorsScene(a)
        "morningevening" -> morningeveningScene(a)
        "covenant" -> covenantScene(a)
        "noharm" -> noharmScene(a)
        "bedtime" -> bedtimeScene(a)
        "nightdoors" -> nightdoorsScene(a)
        "springheart" -> springheartScene(a)
        "deepsea" -> deepseaScene(a)
        "campguard" -> campguardScene(a)
        "roughground" -> roughgroundScene(a)
        "mountaindebt" -> mountaindebtScene(a)
        "betterjar" -> betterjarScene(a)
        "healingbed" -> healingbedScene(a)
        "rainwind" -> rainwindScene(a)
        "journey" -> journeyScene(a)
        "iftar" -> iftarScene(a)
        "sneezebook" -> sneezebookScene(a)
        "weddinglights" -> weddinglightsScene(a)
        "alivetree" -> alivetreeScene(a)
        "lightheavy" -> lightheavyScene(a)
        "tenfold" -> tenfoldScene(a)
        "seventyhundred" -> seventyhundredScene(a)
        "raisedhands" -> raisedhandsScene(a)
        "intentlamp" -> intentlampScene(a)
        "questioner" -> questionerScene(a)
        "fivepillars" -> fivepillarsScene(a)
        "fourwrit" -> fourwritScene(a)
        "radd" -> raddScene(a)
        "himaedge" -> himaedgeScene(a)
        "fivecounsel" -> fivecounselScene(a)
        "threeshown" -> threeshownScene(a)
        "canbear" -> canbearScene(a)
        "goodonly" -> goodonlyScene(a)
        "doubtfree" -> doubtfreeScene(a)
        "notmyconcern" -> notmyconcernScene(a)
        "likeforbrother" -> likeforbrotherScene(a)
        "inviolable" -> inviolableScene(a)
        "goodorsilent" -> goodorsilentScene(a)
        "angerember" -> angeremberScene(a)
        "ihsaneverything" -> ihsaneverythingScene(a)
        "taqwaeverywhere" -> taqwaeverywhereScene(a)
        "guardhim" -> guardhimScene(a)
        "modesty" -> modestyScene(a)
        "straightpath" -> straightpathScene(a)
        "intojanna" -> intojannaScene(a)
        "halfofiman" -> halfofimanScene(a)
        "noinjustice" -> noinjusticeScene(a)
        "notonlywealth" -> notonlywealthScene(a)
        "everyjoint" -> everyjointScene(a)
        "askyourheart" -> askyourheartScene(a)
        "holdfast" -> holdfastScene(a)
        "headofmatter" -> headofmatterScene(a)
        "fourrulings" -> fourrulingsScene(a)
        "zuhdlove" -> zuhdloveScene(a)
        "harmblocked" -> harmblockedScene(a)
        "proofonclaimant" -> proofonclaimantScene(a)
        "changeevil" -> changeevilScene(a)
        "nojealousy" -> nojealousyScene(a)
        "relievegrief" -> relievegriefScene(a)
        "recordbook" -> recordbookScene(a)
        "awliya" -> awliyaScene(a)
        "pardoned" -> pardonedScene(a)
        "stranger" -> strangerScene(a)
        "followsrevealed" -> followsrevealedScene(a)
        "skyhighforgive" -> skyhighforgiveScene(a)
        "moonface" -> moonfaceScene(a)
        "garments" -> garmentsScene(a)
        "humbletable" -> humbletableScene(a)
        "namesring" -> namesringScene(a)
        "simpleliving" -> simplelivingScene(a)
        "clearspeech" -> clearspeechScene(a)
        "truejest" -> truejestScene(a)
        "gratefulservant" -> gratefulservantScene(a)
        "recitelight" -> recitelightScene(a)
        "onlyaservant" -> onlyaservantScene(a)
        "noblemanners" -> noblemannersScene(a)
        "lastday" -> lastdayScene(a)
        "echoadhan" -> echoadhanScene(a)
        "hajjpillar" -> hajjpillarScene(a)
        "beforehajj" -> beforehajjScene(a)
        "miqatwhite" -> miqatwhiteScene(a)
        "fivemiqat" -> fivemiqatScene(a)
        "threenusuk" -> threenusukScene(a)
        "childpilgrim" -> childpilgrimScene(a)
        "ihramlimits" -> ihramlimitsScene(a)
        "sevenrounds" -> sevenroundsScene(a)
        "arafatday" -> arafatdayScene(a)
        "nahrday" -> nahrdayScene(a)
        "hadyoffer" -> hadyofferScene(a)
        "farewellhouse" -> farewellhouseScene(a)
        "eidoffering" -> eidofferingScene(a)
        "fourconditions" -> fourconditionsScene(a)
        "bestoffering" -> bestofferingScene(a)
        "whocovers" -> whocoversScene(a)
        "threethirds" -> threethirdsScene(a)
        "dhulhijjanails" -> dhulhijjanailsScene(a)
        "ramadangates" -> ramadangatesScene(a)
        "fastsecret" -> fastsecretScene(a)
        "hilalwatch" -> hilalwatchScene(a)
        "whomustfast" -> whomustfastScene(a)
        "travelersick" -> travelersickScene(a)
        "excusedays" -> excusedaysScene(a)
        "fastmanners" -> fastmannersScene(a)
        "suhooriftar" -> suhooriftarScene(a)
        "fastbreakers" -> fastbreakersScene(a)
        "forgotfast" -> forgotfastScene(a)
        "lastten" -> lasttenScene(a)
        "fitrsaa" -> fitrsaaScene(a)
        "tarawihnight" -> tarawihnightScene(a)
        "rakatcount" -> rakatcountScene(a)
        "withimam" -> withimamScene(a)
        "witrclose" -> witrcloseScene(a)
        "qunuthands" -> qunuthandsScene(a)
        "itikafstay" -> itikafstayScene(a)
        "mosqueonly" -> mosqueonlyScene(a)
        "itikaftimes" -> itikaftimesScene(a)
        "leavingmosque" -> leavingmosqueScene(a)
        "seclusionadvice" -> seclusionadviceScene(a)
        "purewater" -> purewaterScene(a)
        "vesselrules" -> vesselrulesScene(a)
        "restroomway" -> restroomwayScene(a)
        "miswak" -> miswakScene(a)
        "wudusix" -> wudusixScene(a)
        "wudusunnahs" -> wudusunnahsScene(a)
        "wudubreakers" -> wudubreakersScene(a)
        "khuffwipe" -> khuffwipeScene(a)
        "castwipe" -> castwipeScene(a)
        "fullwash" -> fullwashScene(a)
        "dryablution" -> dryablutionScene(a)
        "impurities" -> impuritiesScene(a)
        "naturalcycle" -> naturalcycleScene(a)
        "cyclechanges" -> cyclechangesScene(a)
        "excusedprayer" -> excusedprayerScene(a)
        "excusedfast" -> excusedfastScene(a)
        "continuousflow" -> continuousflowScene(a)
        "afterbirth" -> afterbirthScene(a)
        "zakatgrowth" -> zakatgrowthScene(a)
        "zakatconds" -> zakatcondsScene(a)
        "goldnisab" -> goldnisabScene(a)
        "tradegoods" -> tradegoodsScene(a)
        "harvestshare" -> harvestshareScene(a)
        "livestockzakat" -> livestockzakatScene(a)
        "eightrecipients" -> eightrecipientsScene(a)
        "prayerrank" -> prayerrankScene(a)
        "adhancall" -> adhancallScene(a)
        "fivetimes" -> fivetimesScene(a)
        "prayerconds" -> prayercondsScene(a)
        "walktoprayer" -> walktoprayerScene(a)
        "openingtakbir" -> openingtakbirScene(a)
        "recitation" -> recitationScene(a)
        "bowing" -> bowingScene(a)
        "prostration" -> prostrationScene(a)
        "betweensujud" -> betweensujudScene(a)
        "tashahhud" -> tashahhudScene(a)
        "pillarsofprayer" -> pillarsofprayerScene(a)
        "wajibatprayer" -> wajibatprayerScene(a)
        "dislikedacts" -> dislikedactsScene(a)
        "forgetfulness" -> forgetfulnessScene(a)
        "postprayerdhikr" -> postprayerdhikrScene(a)
        "twelverakat" -> twelverakatScene(a)
        "witrnight" -> witrnightScene(a)
        "forenoon" -> forenoonScene(a)
        "recitationbow" -> recitationbowScene(a)
        "forbiddentimes" -> forbiddentimesScene(a)
        "congregation" -> congregationScene(a)
        "wholeads" -> wholeadsScene(a)
        "sickprayer" -> sickprayerScene(a)
        "shortenedprayer" -> shortenedprayerScene(a)
        "joiningprayers" -> joiningprayersScene(a)
        "fridayprayer" -> fridayprayerScene(a)
        "fridaymanners" -> fridaymannersScene(a)
        "eidprayer" -> eidprayerScene(a)
        "eclipseprayer" -> eclipseprayerScene(a)
        "rainprayer" -> rainprayerScene(a)
        "preparingdeceased" -> preparingdeceasedScene(a)
        "funeralprayer" -> funeralprayerScene(a)
        "burial" -> burialScene(a)
        "rabbgarden" -> rabbgardenScene(a)
        "fatihaseven" -> fatihasevenScene(a)
        "falaqnas" -> falaqnasScene(a)
        "samadneeds" -> samadneedsScene(a)
        "masadsign" -> masadsignScene(a)
        "nasrdawn" -> nasrdawnScene(a)
        "baraa" -> baraaScene(a)
        "kawtharriver" -> kawtharriverScene(a)
        "maunhouse" -> maunhouseScene(a)
        "filquraysh" -> filqurayshScene(a)
        "humazawealth" -> humazawealthScene(a)
        "asrhourglass" -> asrhourglassScene(a)
        "visitors" -> visitorsScene(a)
        "qariaday" -> qariadayScene(a)
        "atomweight" -> atomweightScene(a)
        "bayyinascroll" -> bayyinascrollScene(a)
        "qadrnight" -> qadrnightScene(a)
        "iqrafirst" -> iqrafirstScene(a)
        "tinmap" -> tinmapScene(a)
        "usryusrayn" -> usryusraynScene(a)
        "duhamorning" -> duhamorningScene(a)
        "layleasy" -> layleasyScene(a)
        "nafsmirror" -> nafsmirrorScene(a)
        "kabad" -> kabadScene(a)
        "fajrten" -> fajrtenScene(a)
        "ghashiyaearth" -> ghashiyaearthScene(a)
        "alaguide" -> alaguideScene(a)
        "tariqstar" -> tariqstarScene(a)
        "burujsky" -> burujskyScene(a)
        "easyreckon" -> easyreckonScene(a)
        "raynheart" -> raynheartScene(a)
        "kiram" -> kiramScene(a)
        "takwir" -> takwirScene(a)
        "abasa" -> abasaScene(a)
        "naziat" -> naziatScene(a)
        "naba" -> nabaScene(a)
        "shafibook" -> shafibookScene(a)
        "harfgood" -> harfgoodScene(a)
        "ruinhouse" -> ruinhouseScene(a)
        "nightrecite" -> nightreciteScene(a)
        "mushaftouch" -> mushaftouchScene(a)
        "recitemanners" -> recitemannersScene(a)
        "tilawasujud" -> tilawasujudScene(a)
        "sujuddhikr" -> sujuddhikrScene(a)
        "ttmafhum" -> ttmafhumScene(a)
        "tttanawwu" -> tttanawwuScene(a)
        "tttadadd" -> tttadaddScene(a)
        "ttasbab" -> ttasbabScene(a)
        "ttlafz" -> ttlafzScene(a)
        "tttabaqat" -> tttabaqatScene(a)
        "tthushirat" -> tthushiratScene(a)
        "ttnaba" -> ttnabaScene(a)
        "ttzuwwijat" -> ttzuwwijatScene(a)
        "ttfajr" -> ttfajrScene(a)
        "ttshaf" -> ttshafScene(a)
        "ttquran" -> ttquranScene(a)
        "tttin" -> tttinScene(a)
        "ttqadr" -> ttqadrScene(a)
        "ttfalaq" -> ttfalaqScene(a)
        "tzmujadila" -> tzmujadilaScene(a)
        "tzhashr" -> tzhashrScene(a)
        "tzmumtahana" -> tzmumtahanaScene(a)
        "tzsaff" -> tzsaffScene(a)
        "tzjumua" -> tzjumuaScene(a)
        "tzmunafiqun" -> tzmunafiqunScene(a)
        "tztaghabun" -> tztaghabunScene(a)
        "tztalaq" -> tztalaqScene(a)
        "tztahrim" -> tztahrimScene(a)
        "tzmulk" -> tzmulkScene(a)
        "tzqalam" -> tzqalamScene(a)
        "tzhaqqa" -> tzhaqqaScene(a)
        "tzmaarij" -> tzmaarijScene(a)
        "tznuh" -> tznuhScene(a)
        "tzjinn" -> tzjinnScene(a)
        "tzmuzzammil" -> tzmuzzammilScene(a)
        "tzmuddaththir" -> tzmuddaththirScene(a)
        "tzqiyama" -> tzqiyamaScene(a)
        "tzinsan" -> tzinsanScene(a)
        "tzmursalat" -> tzmursalatScene(a)
        "tanun" -> tanunScene(a)
        "taidgham" -> taidghamScene(a)
        "taiqlab" -> taiqlabScene(a)
        "tamim" -> tamimScene(a)
        "talam" -> talamScene(a)
        "tamithlan" -> tamithlanScene(a)
        "tamadd" -> tamaddScene(a)
        "tamaddahkam" -> tamaddahkamScene(a)
        "jzwajib" -> jzwajibScene(a)
        "jzmakharij" -> jzmakharijScene(a)
        "jzsifat" -> jzsifatScene(a)
        "jztajwid" -> jztajwidScene(a)
        "jztarqiq" -> jztarqiqScene(a)
        "jzwaqf" -> jzwaqfScene(a)
        "jzwasl" -> jzwaslScene(a)
        "jzrawm" -> jzrawmScene(a)
        "sbnasab" -> sbnasabScene(a)
        "sbmawlid" -> sbmawlidScene(a)
        "sbridaa" -> sbridaaScene(a)
        "sbsharh" -> sbsharhScene(a)
        "sbyutm" -> sbyutmScene(a)
        "sbhifz" -> sbhifzScene(a)
        "sbkaaba" -> sbkaabaScene(a)
        "sbkhadija" -> sbkhadijaScene(a)
        "mdhira" -> mdhiraScene(a)
        "mdmudd" -> mdmuddScene(a)
        "mdmaratib" -> mdmaratibScene(a)
        "mdsirr" -> mdsirrScene(a)
        "mdjahr" -> mdjahrScene(a)
        "mdadha" -> mdadhaScene(a)
        "mdhabasha" -> mdhabashaScene(a)
        "mdshib" -> mdshibScene(a)
        "mdtaif" -> mdtaifScene(a)
        "mdaqaba" -> mdaqabaScene(a)
        "mdhijra" -> mdhijraScene(a)
        "mbmasjid" -> mbmasjidScene(a)
        "mbmuakhat" -> mbmuakhatScene(a)
        "mbwathiqa" -> mbwathiqaScene(a)
        "mbism" -> mbismScene(a)
        "mbsukkan" -> mbsukkanScene(a)
        "mbidhn" -> mbidhnScene(a)
        "gzbadr" -> gzbadrScene(a)
        "gzuhud" -> gzuhudScene(a)
        "gzkhandaq" -> gzkhandaqScene(a)
        "gzhuday" -> gzhudayScene(a)
        "gzkhaybar" -> gzkhaybarScene(a)
        "gzmutah" -> gzmutahScene(a)
        "gzfath" -> gzfathScene(a)
        "gzhunayn" -> gzhunaynScene(a)
        "gztabuk" -> gztabukScene(a)
        "wdwufud" -> wdwufudScene(a)
        "wdbaraa" -> wdbaraaScene(a)
        "wdwada" -> wdwadaScene(a)
        "wdmarad" -> wdmaradScene(a)
        "wdwafat" -> wdwafatScene(a)
        "umkhasais" -> umkhasaisScene(a)
        "umsawda" -> umsawdaScene(a)
        "umaisha" -> umaishaScene(a)
        "umhafsa" -> umhafsaScene(a)
        "umzaynabk" -> umzaynabkScene(a)
        "umsalama" -> umsalamaScene(a)
        "umzaynabj" -> umzaynabjScene(a)
        "umjuway" -> umjuwayScene(a)
        "umhabiba" -> umhabibaScene(a)
        "umsafiyya" -> umsafiyyaScene(a)
        "ummaymuna" -> ummaymunaScene(a)
        "tmahl" -> tmahlScene(a)
        "tmwafa" -> tmwafaScene(a)
        "tmbanat" -> tmbanatScene(a)
        "tmahfad" -> tmahfadScene(a)
        "tmjar" -> tmjarScene(a)
        "tmkhadam" -> tmkhadamScene(a)
        "tmbala" -> tmbalaScene(a)
        "tmkuffar" -> tmkuffarScene(a)
        "tmjadid" -> tmjadidScene(a)
        "tmusat" -> tmusatScene(a)
        "tmnisa" -> tmnisaScene(a)
        "tmkibar" -> tmkibarScene(a)
        "tmsighar" -> tmsigharScene(a)
        "hdshaar" -> hdshaarScene(a)
        "hdlihya" -> hdlihyaScene(a)
        "hdkhatam" -> hdkhatamScene(a)
        "hdnaal" -> hdnaalScene(a)
        "hdisbal" -> hdisbalScene(a)
        "hdtib" -> hdtibScene(a)
        "hdnawm" -> hdnawmScene(a)
        "hdyaqza" -> hdyaqzaScene(a)
        "hdsawm" -> hdsawmScene(a)
        "hdbika" -> hdbikaScene(a)
        "hdmirath" -> hdmirathScene(a)
        "ahtufayl" -> ahtufaylScene(a)
        "ahadab" -> ahadabScene(a)
        "ahahabb" -> ahahabbScene(a)
        "ahansar" -> ahansarScene(a)
        "ahtaam" -> ahtaamScene(a)
        "ahmakka" -> ahmakkaScene(a)
        "ahayyam" -> ahayyamScene(a)
        "ahtayammun" -> ahtayammunScene(a)
        "ahfal" -> ahfalScene(a)
        "ahyakrah" -> ahyakrahScene(a)
        "ahummati" -> ahummatiScene(a)
        "ahdahik" -> ahdahikScene(a)
        "ahmulaaba" -> ahmulaabaScene(a)
        "ahghadab" -> ahghadabScene(a)
        "ahfarah" -> ahfarahScene(a)
        "ahhuzn" -> ahhuznScene(a)
        "ahtafakkur" -> ahtafakkurScene(a)
        "ahtahfiz" -> ahtahfizScene(a)
        "ahtazir" -> ahtazirScene(a)
        "mntazim" -> mntazimScene(a)
        "mntaa" -> mntaaScene(a)
        "mnmasajid" -> mnmasajidScene(a)
        "mnawlad" -> mnawladScene(a)
        "hdasbab" -> hdasbabScene(a)
        "hdharam" -> hdharamScene(a)
        "hdhumma" -> hdhummaScene(a)
        "hdtaun" -> hdtaunScene(a)
        "hdayn" -> hdaynScene(a)
        "hdruqya" -> hdruqyaScene(a)
        "hdkarb" -> hdkarbScene(a)
        "hdmanam" -> hdmanamScene(a)
        "hdwaswasa" -> hdwaswasaScene(a)
        "hdasma" -> hdasmaScene(a)
        "hdalfadh" -> hdalfadhScene(a)
        "ssmasadir" -> ssmasadirScene(a)
        "ssquran" -> ssquranScene(a)
        "ssnaqd" -> ssnaqdScene(a)
        "ssmaghazi" -> ssmaghaziScene(a)
        "ssibnishaq" -> ssibnishaqScene(a)
        "tqashr" -> tqashrScene(a)
        "tqsunna" -> tqsunnaScene(a)
        "tqisra" -> tqisraScene(a)
        "tqtabiin" -> tqtabiinScene(a)
        "tqray" -> tqrayScene(a)
        else -> drawRect(fallback)
    }
}

private fun animationsOff(context: Context): Boolean = try {
    Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
} catch (e: Exception) {
    false
}

/**
 * The hand-over between two ideas (2026-11-04, from users' feedback that the old idea lingered while the new one
 * came in): the idea that is leaving fades over the first BEAT_OUT of the next beat, and a beat's own drawing rises
 * over BEAT_IN, as shares of that beat (about 0.3 to 0.6 s for most beats). Before, 0.3 to 0.5 of the beat (2 to 3 s).
 */
internal const val BEAT_OUT = 0.07f
internal const val BEAT_IN = 0.1f

/** How long each kind of beat takes to play out. */
internal fun beatMillis(kind: String): Int = when (kind) {
    "cut" -> 1700
    "hold" -> 1500
    "later", "trumpet", "scrolls", "takbir" -> 1800
    "house", "pillars", "excess", "stone", "steps", "show", "shut" -> 1600
    "rise", "dome", "feast", "door", "bridge" -> 2200
    "pass" -> 2400
    "rival", "light", "lock", "filter", "scale", "copy", "near", "out", "tree", "names", "shirk", "bind", "lower", "gaze", "hidden", "swap" -> 1400
    "charm", "wall", "many", "raise", "level", "cloud", "roll", "shrink", "quake", "truth", "nocall", "shoot", "guide", "credit", "rain", "challenge", "breath" -> 1600
    "nights" -> 3200
    "descend", "recite", "keep", "write", "follow", "false", "first", "cup" -> 1400
    "will", "equal", "sever", "drain" -> 2000
    "create", "journey", "ascend", "garden", "pit", "fire", "palace" -> 2400
    "fifty" -> 3600
    "ring", "name", "reject", "smoke", "sunwest", "sink", "brick", "ask" -> 1400
    "untie", "return" -> 2200
    "axis", "core" -> 1200
    "gain", "trip", "guard" -> 1800
    "loss", "floor", "power" -> 1500
    "sins", "echo" -> 3200
    "cross" -> 1600
    "seed", "pin", "void", "less" -> 2000
    "breaker" -> 1100
    "fog", "pray" -> 2400
    "shield", "deny" -> 2200
    "close" -> 1800
    "march" -> 3000
    "thank" -> 2600
    "quiet", "camp", "tabuk", "other", "self" -> 2000
    "joke" -> 2400
    "rank", "node" -> 1200
    "lit" -> 1400
    "pure", "ijma", "slaves", "swear" -> 1800
    "pair", "knotted", "unlike", "foe", "hate" -> 2000
    "snip", "same", "veil", "kin" -> 2400
    "hang", "reward", "tasbih", "love" -> 2600
    "cure", "branch" -> 3000
    "burn" -> 3200
    // wave 7
    "inscribe" -> 3000
    "base", "caliphs", "pile", "single" -> 2800
    "sheet" -> 1800
    "match", "liken" -> 2400
    "fit", "freehand", "extend", "slip" -> 2600
    "defy" -> 6000
    "stack" -> 3600
    "carve" -> 4200
    "trapped" -> 4400
    "doubt" -> 3800
    "guess" -> 3000
    // wave 8 (round 1 of the redraw): each beat lasts about as long as the words it goes with
    "dawnlight" -> 3600
    "oneriver", "fray", "ebb", "veer", "coolwell", "sortout" -> 5000
    "fork", "leave", "onekept" -> 4300
    "knots72" -> 6000
    "caliphs4" -> 7000
    "spout" -> 5400
    "drink", "behind", "regret" -> 4400
    "horizon" -> 7600
    "clasp" -> 3000
    "sow" -> 7600
    // wave 9 (round 2): «براهين التوحيد»
    "chisel" -> 2400
    "daughter" -> 7000
    "stillness" -> 3600
    "call" -> 5000
    "verse" -> 3000
    "padlock" -> 3200
    "towers" -> 2400
    "furl" -> 6500
    // wave 10 (round 3): «ترك الجدال والأهواء»
    "quarrel" -> 4500
    "revealed" -> 5500
    "sinner" -> 5000
    "unharmed" -> 3800
    "zealot" -> 5500
    "sound" -> 4500
    // wave 11 (round 4): «الجماعة وولاة الأمر»
    "fold" -> 2400
    "carryon" -> 5500
    "picks" -> 6000
    "breach" -> 6000
    "rows" -> 7000
    "cheat" -> 5500
    "silent" -> 6500
    "melee" -> 7000
    "seated" -> 4000
    // wave 12 (round 5): «الحكم على أهل القبلة»
    "rooted" -> 5000
    "cloche" -> 4000
    "paint" -> 4000
    "opendoor" -> 8000
    "turnquit" -> 3600
    "westsun" -> 4500
    // wave 13 (round 6): «ألفاظ وأحكام تمس التوحيد»
    "thumma" -> 3800
    "askjanna" -> 3000
    "alone" -> 5000
    "keepseal" -> 4000
    "whisper" -> 5500
    "station" -> 6500
    // wave 14 (round 7: الإيمان بالقدر)
    "ringgeneral" -> 6500
    "twoways" -> 4000
    "submit" -> 3100
    // wave 15 (round 8: أعمال القلوب)
    "backside" -> 3300
    "wingfear" -> 700
    // wave 16 (round 9: فضل التوحيد وتحقيقه)
    "pureheart" -> 5000
    "earthful" -> 2100
    "forgiven" -> 3000
    "refine" -> 4400
    // wave 17 (round 10: ما يضاد التوحيد)
    "torrent" -> 4800
    "outmilla" -> 2600
    "belie" -> 1900
    "haughty" -> 2200
    // wave 18 (round 11: السحر والكهانة والتنجيم)
    "fortune" -> 4200
    "crowfly" -> 3400
    "turnhome" -> 5400
    "goodword" -> 2900
    // wave 19 (round 12: نواقض الإسلام)
    "road10" -> 9000
    "beware" -> 4000
    // wave 20 (round 13: أساسيات التوحيد)
    "created" -> 2100
    "guide4" -> 1000
    "rububiyya" -> 6000
    "asma" -> 7000
    // «بصائر»، الجزء الأول
    "atheist", "fromnothing" -> 1800
    "tworoads" -> 2000
    "masterkey", "sincere", "allfour", "hascreator", "twoboxes", "cooling" -> 2500
    "volumes", "eleventh", "tafsir", "nextyear", "indifferent", "trustlost", "nothingness", "emptytable", "whomade", "solid", "rainmix", "fuse" -> 3000
    "deist", "agnostic", "newcoat", "dice", "nodice", "zerosum", "deadborn", "fuzzy", "frames", "balance", "nomatter", "inject", "insideonly", "quotecard", "counts", "list180" -> 3500
    "calmheart", "thinkers", "monolith", "bubblestart", "notempty", "sealedbox", "insidebox", "formula", "noclock", "balltrace", "civ", "orchid", "care", "tiny", "forest", "sandchip", "gapsbridge", "lavabake", "pantry", "twotrays", "potato" -> 4000
    "wird", "twist", "bang", "blurorder", "made", "thrift", "planets", "readout", "infoblocks", "labfail", "prize", "target", "designer", "eyerow", "pool", "glands", "graph" -> 4500
    "deny5", "bubbles", "pairs", "domino", "soldiers", "vast", "forgeiron", "trust", "letters", "helix", "livecell", "chasm", "waitdoor", "scramble", "baker", "beaks", "lineage" -> 5000
    "race", "steps3", "satchel" -> 6000
    // round 14: «الصفات واليوم الآخر»
    "above" -> 3500
    "scrollsfly", "husna", "awwal" -> 5000
    "idolnames", "qadim" -> 6000
    // round 15: «الشرك ووسائله»
    "sunanpath" -> 3750
    "lotetree" -> 4500
    "livinghelp" -> 9000
    // round 16: «أركان الإيمان»
    "ranks" -> 9000
    // round 17: «معرفة النبي ﷺ»
    "sevenheavens" -> 9000
    // round 18: «منهج أهل السنة»
    // round 19: «السنة والبدعة»
    "twowings" -> 9000
    // «بصائر»، الجزء الثاني
    "fourq", "ishmaellight", "onelight", "heartrise", "rights", "secret", "certain", "alarm" -> 3500
    "fearstorm", "witness", "overview", "parts", "cage", "plate", "cageopen", "embers", "twopoles", "samethread", "impossible", "wonder", "sunsea", "ringclose", "twobranch", "falsefade", "likemusa", "tin", "names3", "quietstreet", "servant", "selasong", "welling", "newkeepers", "cornerset", "fillearth", "kingdoms", "yathrib", "cell", "ezra", "strikethree", "striketwo", "strikeone", "overbook", "clean", "manyverses", "radiator", "beacon", "breathe", "moonfade", "waterlife", "innerwave", "curdle", "mixing", "streak", "openquran", "offers", "returns", "dare", "pearls", "links", "topofhundred", "swapstone", "darkvale", "twobricks", "quds", "aman", "constantinople", "shepherds", "markets", "belowzero", "goldpillars", "greening" -> 5000
    "sameday", "converge", "pilgrims", "vineyard", "statue", "joints", "dense", "hira", "fleet", "twocamps" -> 7000
    // batch 8 (2026-10-27): «بصائر»، الجزء الثالث
    // v1.2: Riyad al-Salihin (waves 46-48)
    "bestdeeds", "mother3", "rughim", "hangs", "counsel", "broth", "beam", "together", "threeranks", "weakroots", "twodaughters", "homelight", "firstdinar", "sixfirst", "sixrest", "onebody", "building", "visit", "threeacts", "angels", "village", "debt", "twokeys", "twohouses", "bucket", "zanahu", "threenever", "mountains", "beautyok", "levelout", "childrenlead", "sinkdown", "toberr", "tofujur", "market", "bedrank", "kiss", "mirrorrain", "babycry", "twolights", "lampout", "onecloth" -> 6000
    // v1.2 waves 49-68: Hisn al-Muslim, the Forty of al-Nawawi, Mukhtasar al-Shama'il
    "drawnear", "twolocks", "riseword", "coal", "passedon", "fourmarks", "boomerang", "shutgates", "noaid", "backs", "greetfirst", "records", "lovechain", "whofirst", "tens", "quietnight", "sayname", "yourside", "noblame", "threebreaths", "rightward", "nosupper", "makeroom", "atedge", "clearing", "remembered", "nightwake", "lastten", "comesdown", "threegarments", "inout", "threewords", "homegreet", "pitfalls", "rightin", "fourlights", "salamturn", "hundred", "nobarrier", "likesurah", "consulted", "bookends", "dawnguard", "threeshields", "mulk", "tillsunrise", "fourparts", "dayornight", "fivesteps", "dayshukr", "scorpion", "threecontent", "threesurahs", "rightside", "tasbihcols", "callin", "coverjars", "lastpage", "lampfire", "cloudlift", "bowedtags", "springheart", "eightpairs", "praisestars", "dhunnun", "turnsback", "coolfire", "trenchwind", "neighbour", "smoothed", "nolaw", "strongweak", "whispercut", "comestoali", "mountainpaid", "heavyload", "workandask", "brokenjar", "ummsalama", "takengiven", "visitgraves", "nobas", "sevenvisits", "handonpain", "notthestar", "strongwind", "thunderhush", "mounted", "piety", "homecoming", "keptboth", "guestdua", "angelsover", "firstdates", "threeanswers", "coolword", "twopraises", "blessit", "tworings", "hasanayn", "thanksgift", "outweighs", "featherwords", "seafoam", "fourwords", "thousandgood", "tenback", "seventyplus", "turnall", "washpray", "tallyhundred", "lastthird", "duaworship", "twogoods", "openwith", "twopillarsof", "innerouter", "samewash", "hijraroads", "fourquestions", "nestedranks", "pairsplit", "tworungs", "toweringup", "fivebuild", "basefirst", "saidanddone", "twoservitudes", "orderofduty", "threestages", "fourwrit", "endings", "nesteddecree", "twogatesmeet", "bothreturned", "twomothers", "threezones", "stepclear", "nearhima", "threeshepherds", "slopedown", "limbsfollow", "thriceword", "cleanfull", "fiveto", "bookfirst", "measuringline", "pointgood", "threelit", "bloodwealth", "sevenconds", "iqal", "outerjudged", "shutandclimb", "sitpray", "manyquestions", "partwater", "pureonly", "eatandwork", "blockeddua", "fourcauses", "rayhanatan", "murkyclear", "widermargin", "steadyjitter", "betterless", "passby", "enjoinstill", "twobaskets", "anasyears", "baseremains", "mirrorgood", "faiththread", "warmnet", "threesacred", "tonguehearthand", "farewellday", "partmissing", "speakorstill", "rightscount", "guestwelcome", "threehostdays", "repeatnoanger", "pulledoff", "emberquench", "sitthenlie", "twowritings", "outweighharm", "reachesbeast", "muadhabudharr", "shieldsecret", "patchtear", "tworanks", "fourmanners", "ridebehind", "keepbounds", "crowdcannot", "easethenhard", "missandhit", "oldprophecy", "sternword", "hayagauge", "pullsback", "placebadr", "sufyanasks", "fouralong", "safeend", "bothsides", "fourticks", "heldbelief", "mercygate", "notlisted", "neverdrops", "halffilled", "praisefills", "lightproofglow", "patiencethree", "proofforagainst", "forbadeself", "wrongplace", "fourneeds", "curedone", "nothingadded", "richpoor", "dhikrcoins", "spreadsgood", "selfcharity", "maruftiers", "jointslight", "dutyorextra", "fivesadaqa", "duhatwo", "eachsunrise", "birrring", "consultheart", "answeredfirst", "movingsermon", "linkedrings", "onelitway", "tightgrip", "notbeforeus", "greatyeteasy", "tentparts", "shieldandwater", "polefalls", "sealtongue", "keepduties", "boundsfences", "silentmercy", "fourquads", "twoloves", "stepaside", "handvsheart", "zuhdrungs", "noshunhalal", "poetfour", "harmwall", "twoharms", "fourchapters", "openpasture", "threadsstrong", "bareclaims", "proofoath", "twoaimsqada", "halfwaymeet", "halfdebt", "bothcontent", "darkblots", "handtongueheart", "dimbutlit", "faithgauge", "barrierexcused", "twelvephrases", "envysmoke", "lightmytoo", "firstenvy", "fournots", "liftweight", "smallvsbig", "givetime", "cloaked", "knowledgeroad", "twocolumns", "multiply", "badleft", "camelseven", "mercywins", "upright", "twotiersawliya", "fardthennafl", "guidedsteps", "answereddua", "threeforgiven", "accidentspill", "forcedcalmheart", "answeredbaqara", "stillowes", "shoulderword", "takesonly", "dontwait", "storeahead", "needlealigns", "lovesdoes", "smallandlarge", "datestone", "threecauses", "cloudstosky", "earthfulpardon", "elixirdrop", "firstlastpage", "neithertall", "moonnotsword", "brighterthanmoon", "silvercast", "lastwhosaw", "gradedmarks", "shirtstripes", "whitecloths", "perfumekept", "namednew", "tablelifted", "basmalabless", "firstandlast", "threesips", "milkbless", "threenames", "lastnames", "yearsline", "mercynames", "coldhearth", "barleynights", "coolshade", "milkfromneighbors", "chaffblown", "spacedwords", "warmsmile", "neverbarred", "aliriding", "truejoke", "littlebird", "everycamel", "notcheap", "youngagain", "thankfulnight", "threesuras", "longstanding", "heartawake", "drawnout", "readtome", "simmering", "loudorlow", "heardatnight", "servantmessenger", "anystreet", "homechores", "plainsaddle", "neveruff", "shyerthan", "pardonsback", "windgiving", "curtainlift", "noagony", "mondayrest", "zumarverse", "citylitdark", "answercall", "radeetu", "twohayya", "highstation" -> 6000
    // v1.3 fiqh: hajj (waves 69-70)
    "fifthpillar", "oncelife", "umrahspan", "debtledger", "cleanpurse", "goodcompany", "washscent", "twowhites", "labbayk", "keephair", "mountride", "threemiqat", "fromhome", "tohill", "umrahsteps", "threeroads", "tamattu", "ifheld", "validyoung", "guardianlabbayk", "guardianreward", "nocutscent", "nosewn", "mayshade", "blackstone", "safamarwa", "tarwiya", "arafastand", "muzdalifanight", "mashardawn", "fourrites", "noharaj", "tashriqthree", "deputythrow", "hadyshare", "pureearning", "tenfast", "arafaopen", "fourdays", "stayworship", "farewelltawaf", "walkonward", "madinahmosque" -> 6000
    // v1.3 fiqh: udhiya (wave 71)
    "adhadef", "twokabsh", "sunnahor", "betterthanprice", "familyall", "anam", "fourdefects", "timewindow", "owned", "rankkinds", "amlah", "disliked", "onesheep", "seventh", "notshared", "thirds", "nosale", "designated", "noclip", "onlyhim", "tawbanokaffara", "likepilgrim", "dhakatconds" -> 6000
    // v1.3 fiqh: fasting 1 (wave 72)
    "threespans", "beyondcount", "secretheart", "muskscent", "fastshield", "twojoys", "monthlength", "childtrain", "feedperday", "newmuslim", "liftedpen", "travelchoice", "easierway", "sickgauge", "nofaultfound", "medicaldoctor", "makeupdays", "nursingfear", "beforenext", "rescuer" -> 6000
    // v1.3 fiqh: fasting 2 (wave 73)
    "twotiers", "sleptprayer", "earseyetongue", "eatdrink", "scentok", "holdandrepay", "didntknow", "notbreakers", "lasttennights", "oddnights", "beforeeidprayer", "foodnotvalue" -> 6000
    // v1.3 fiqh: tarawih (wave 74)
    "confirmedsunnah", "firsttolast", "twoimams", "evenitout", "rowsfill", "apartjoined", "twowitrforms", "qunutsunnah", "wordsfree" -> 6000
    // v1.3 fiqh: itikaf (wave 75)
    "sunnahvow", "lasttenstay", "threemosques", "mosqueandintent", "amoment", "nothome", "hotelhall", "insidewall", "enterssunset", "entersdawn", "leavesforeid", "vowthree", "outvoid", "needout", "onlyneed", "statedcondition", "notebook" -> 6000
    // v1.3 fiqh: purification 1 (wave 76)
    "fourwaters", "threechanges", "allvessels", "goldsilver", "otherspots", "silverpatch", "leftfootin", "noqibla", "waterorstones", "aracktwig", "fitrafive", "siwakbenefits", "wuduneeded", "sixfard", "wuduconds", "rewardnoblame", "startsunnahs", "rightthrice", "allthreeways" -> 6000
    // v1.3 fiqh: purification 2 (wave 77)
    "wuduglowbreaks", "deepsleep", "doubtstay", "camelmeat", "khuffallowed", "wipeperiod", "wipetop", "wipeends", "castonarm", "nolimit", "wholebody", "newmuslimwash", "twoghusl", "perfectsteps", "noewater", "strikeearth", "waterfound", "liftshadath", "waterremoves", "sprinkle", "rubwring" -> 6000
    // v1.3 fiqh: natural blood (wave 78)
    "monthrhythm", "seednourish", "nolimits", "usualyears", "easerule", "shiftingdays", "whitesign", "pregnantrule", "noprayermakeup", "onerakahtime", "fastmakeup", "ritesbutawaf", "purifiedwash", "aftersunset", "farewellwaived", "unbrokenline", "knownhabit", "wudueachprayer", "threecases", "cradlebegins", "pureearly", "fortymark", "washatforty" -> 6000
    // v1.3 fiqh: zakat (wave 79)
    "growsprout", "thirdpillar", "threewisdoms", "bondnet", "firstislam", "nisabowned", "fullyear", "fourwealth", "debtzakat", "quarterten", "twonisabs", "nocombine", "fiveof200", "jewelry", "shopshelf", "threetradeconds", "valueatyearend", "inheritedno", "rentincome", "storedgrains", "tenthorhalf", "ripenow", "threehundredsaa", "buriedfifth", "threeherds", "herdnisab", "grazingfree", "mostlyfed", "firstfour", "poorneedy", "notdependents" -> 6000
    // v1.3 fiqh: prayer 1 (wave 80)
    "miraj", "openclose", "ontimeanyway", "sevenyears", "announcetime", "kifaya", "fifteenphrases", "turnrightleft", "maghribisha", "fajrline", "middleprayer", "purityandtime", "threemoreconds", "intentheart", "condsvspillars", "smallsteps", "waitdhikr", "straightrows", "rightfootmosque", "widestance", "facequibla", "raisehands", "farapartsins", "alternate" -> 6000
    // v1.3 fiqh: prayer 2 (wave 81)
    "midlength", "longshort", "thirdfourth", "flatback", "riseupsami", "fillheavens", "handsafterrise", "kneesfirst", "sevenlimbs", "armsapart", "greatestpillar", "iftirash", "vertebrae", "followimam", "restsit", "indexpoint", "tahiyyat", "fourrefuge", "fourteenpillars", "somepillars", "sittingside", "calmness" -> 6000
    // v1.3 fiqh: prayer 3 (wave 82)
    "eightduties", "transitions", "extrareward", "threetoten", "followerpraise", "gazedown", "fidgets", "distractions", "ornatewall", "presentheart", "threeaims", "sahwcauses", "alertimam", "istighfarthree", "thirtythree", "kursiyshield", "tentimes", "imamturns", "twelvearches", "aroundprayers", "travelkeeps", "strongestnafl", "witrwindow", "oneto13", "ninejoined" -> 6000
    // v1.3 fiqh: prayer 4 (wave 83)
    "duhanafl", "twotoeight", "duhawindow", "threecounsels", "versebow", "listenerkinds", "bowsubhan", "sajadawajhi", "twodawnbans", "threedaybans", "allowedwithcause", "shadowstops", "rowsduty", "evenwar", "benefits4", "stepreward", "familyneighbors", "rankorder", "readerfaqih", "hostfirst", "unknownfault", "hardshipease", "standsitside", "nodlower", "bentstand", "sickjoin" -> 6000
    // v1.3 fiqh: prayer 5 (wave 84)
    "fourtotwo", "takerukhsa", "fourdaysrule", "charitygift", "homemissed", "twopairs", "rainmudwind", "delaytoasr", "advancewithzuhr", "fardayn", "notobligedbutvalid", "twokhutbas", "nostepping", "earlyfriday", "silentlisten", "tworakatenter", "noreserve", "eidkifaya", "eidtime", "sevenfive", "datesbefore", "otherwayback" -> 6000
    // Wave 85 (2026-11-03): v1.3 fiqh, prayer 6: eclipse, rain prayer, washing, funeral prayer, burial
    "sundarkens", "eclipsewindow", "tworukuperrakah", "longthenshorter", "secondless", "drought", "tworakatground", "repentfirst", "cloakturn", "rainarrives", "visitremind", "washdeceased", "threewhite", "fourtakbir", "foursteps", "latejoin", "duaall", "qirats", "hastenbier", "lahdniche", "spanmound", "standdua", "taziya" -> 6000
    // Wave 86 (2026-11-04): v1.4 the Quran 1: al-Fatiha, al-Falaq and al-Nas, al-Ikhlas, al-Masad
    "nurtureall", "rabbduas", "needaid", "shortholds", "fatihacontents", "everyrakah", "twohidayas", "allevils", "rabbmalikilah", "whisperretreat", "jinnins", "ahadunique", "samadneed", "nochild", "asmathree", "revealedalive", "foretoldtwo", "cametrue", "wealthnohelp" -> 6000
    // Wave 87 (2026-11-04): v1.4 the Quran 2: al-Nasr, al-Kafirun, al-Kawthar, al-Ma'un, al-Fil and Quraysh
    "gladtidings", "tasbihistighfar", "sealedwith", "nasrgrows", "subhanaka", "disown", "mixedcup", "twicesaid", "muchgood", "riverhawd", "whitesweet", "prayersacrifice", "orphanhonor", "prayerguard", "lendsmall", "sahun", "sahwfi", "yearborn", "forquraysh", "twojourneys", "worshiplord" -> 6000
    // Wave 88 (2026-11-04): v1.4 the Quran 3: al-Humaza, al-'Asr, al-Takathur, al-Qari'a, al-Zalzala
    "slanderers", "timepasses", "fourtraits", "selfothers", "rivalry", "allkinds", "graveguests", "barzakhpass", "stingy", "selfwitness", "qariastrike", "earthspeaks", "atomspeck", "evensmall" -> 6000
    // Wave 89 (2026-11-04): v1.4 the Quran 4: al-Bayyina, al-Qadr, al-'Alaq, al-Tin, al-Sharh
    "messengerbook", "purepages", "seekerclear", "greatworth", "exalted", "witrnights", "itikafnight", "firstrevealed", "commandsforbid", "forbidder", "figolive", "sinaimakkah", "threeplaces", "chestopen", "withhardship", "oneandtwo" -> 6000
    // Wave 90 (2026-11-04): v1.4 the Quran 5: al-Duha, al-Layl, al-Shams, al-Balad, al-Fajr
    "notleft", "perfectstate", "teacherkind", "easeall", "subtlesoul", "innerlife", "toil", "restwork", "wasted", "tradedgod", "dawnoath", "tennights", "besttwo" -> 6000
    // Wave 91 (2026-11-04): v1.4 the Quran 6: al-Ghashiya, al-A'la, al-Tariq, al-Buruj, al-Inshiqaq
    "goodtalk", "kindmanners", "flatround", "bigflat", "sabbih", "guideall", "remindtwo", "fearkeeps", "piercingstar", "guardian", "mansions", "wadud", "ghafurwadud", "lostmount", "rightbook", "sinveiled", "reckonstages", "underrule" -> 6000
    // Wave 92 (2026-11-04): v1.4 the Quran 7: al-Mutaffifin, al-Infitar, al-Takwir, 'Abasa, al-Nazi'at, al-Naba'
    "fairmeasure", "fairargument", "rayn", "tasnim", "ghurur", "noblescribes", "abrar", "sunfolded", "asifseen", "noblemessenger", "twocome", "gentlereproach", "knownrule", "welcomeseeker", "angelsobey", "nofear", "rowsstand", "nofalsehood" -> 6000
    // Wave 93 (2026-11-04): v1.4 the virtues of the Quran and the etiquette of reciting it
    "manyhadith", "intercedes", "harfclimb", "mawla", "allhours", "respond", "khatm", "withbarrier", "junub", "istiadha", "tadabbur", "oneverse", "shaytanweeps", "imamled", "wajhisajad", "nocondition", "takbiroutside" -> 6000
    // wave 94
    "ttmafhum1", "ttmafhum2", "ttmafhum3", "tttanawwu1", "tttanawwu2", "tttanawwu3", "tttadadd0", "tttadadd2", "tttadadd3", "ttasbab0", "ttasbab2", "ttasbab3", "ttlafz0", "ttlafz2", "tttabaqat0", "tttabaqat1", "tttabaqat2", "tttabaqat3", "tthushirat0", "tthushirat1", "tthushirat2", "tthushirat3", "ttnaba1", "ttzuwwijat1", "ttfajr0", "ttshaf1", "ttshaf2", "ttquran0", "ttquran1", "ttquran3", "tttin0", "tttin1", "tttin2", "tttin3", "ttqadr0", "ttqadr1", "ttqadr2", "ttfalaq0", "ttfalaq1", "ttfalaq2", "ttfalaq3", "tzmujadila0", "tzmujadila1", "tzmujadila2", "tzhashr0", "tzhashr1", "tzhashr2", "tzhashr3", "tzmumtahana1", "tzmumtahana2", "tzmumtahana3", "tzsaff0", "tzsaff1", "tzsaff3", "tzjumua0", "tzjumua1", "tzjumua2", "tzjumua3", "tzmunafiqun0", "tzmunafiqun3", "tztaghabun1", "tztaghabun2", "tztaghabun3", "tztalaq0", "tztalaq2", "tztalaq3", "tztahrim0", "tztahrim1", "tztahrim2", "tztahrim3", "tzmulk1", "tzmulk2", "tzmulk3", "tzqalam1", "tzqalam2", "tzhaqqa0", "tzhaqqa1", "tzhaqqa2", "tzhaqqa3", "tzmaarij3", "tznuh0", "tznuh1", "tznuh2", "tznuh3", "tzjinn0", "tzjinn1", "tzjinn2", "tzjinn3", "tzmuzzammil0", "tzmuzzammil1", "tzmuzzammil2", "tzmuzzammil3", "tzmuddaththir0", "tzmuddaththir2", "tzqiyama0", "tzqiyama1", "tzqiyama2", "tzinsan0", "tzinsan3", "tzmursalat0", "tzmursalat1", "tanun0", "tanun3", "taidgham0", "taidgham1", "taidgham3", "taiqlab0", "taiqlab3", "tamim0", "talam1", "tamadd1", "tamaddahkam1", "tamaddahkam2", "tamaddahkam3", "jzwajib1", "jzmakharij1", "jzmakharij2", "jzsifat0", "jzsifat1", "jztarqiq2", "jzwaqf2", "jzwasl3" -> 6000
    // wave 95
    "sbnasab0", "sbmawlid0", "sbmawlid1", "sbmawlid2", "sbmawlid3", "sbridaa0", "sbridaa1", "sbsharh3", "sbyutm0", "sbyutm1", "sbyutm2", "sbhifz0", "sbhifz1", "sbhifz2", "sbhifz3", "sbkaaba0", "sbkaaba2", "sbkhadija0", "sbkhadija1", "sbkhadija2", "sbkhadija3", "mdhira0", "mdhira1", "mdhira2", "mdhira3", "mdmudd1", "mdmudd3", "mdmaratib1", "mdmaratib3", "mdsirr1", "mdsirr3", "mdjahr1", "mdjahr2", "mdadha0", "mdadha1", "mdadha3", "mdhabasha0", "mdhabasha1", "mdshib0", "mdshib1", "mdshib2", "mdshib3", "mdtaif0", "mdtaif1", "mdaqaba0", "mdaqaba1", "mdaqaba2", "mdhijra0", "mdhijra1", "mdhijra2", "mbmasjid0", "mbmasjid1", "mbmuakhat0", "mbwathiqa0", "mbwathiqa1", "mbwathiqa2", "mbism0", "mbism1", "mbism2", "mbsukkan0", "mbsukkan1", "mbidhn0", "mbidhn2", "mbidhn3", "gzbadr0", "gzbadr1", "gzbadr2", "gzbadr3", "gzuhud0", "gzuhud1", "gzuhud2", "gzkhandaq0", "gzkhandaq1", "gzkhandaq2", "gzhuday0", "gzhuday1", "gzhuday2", "gzhuday3", "gzkhaybar0", "gzkhaybar1", "gzkhaybar2", "gzmutah0", "gzmutah1", "gzmutah3", "gzfath0", "gzfath1", "gzfath2", "gzfath3", "gzhunayn0", "gzhunayn1", "gzhunayn2", "gztabuk0", "gztabuk1", "gztabuk2", "wdwufud0", "wdwufud2", "wdbaraa0", "wdbaraa2", "wdbaraa3", "wdwada0", "wdwada1", "wdwada3", "wdmarad0", "wdmarad1", "wdmarad3", "wdwafat0", "wdwafat1", "wdwafat2", "wdwafat3" -> 6000
    // wave 96
    "tmwafa3", "tmjar2", "tmjadid2" -> 3250
    "umzaynabk3" -> 3500
    "umsalama3", "umsafiyya3" -> 3750
    "umhabiba3" -> 4500
    "umkhasais3", "umhafsa1", "umzaynabk0", "umzaynabj3", "ummaymuna1", "tmjar3", "tmkibar1", "tmkibar3" -> 4750
    "umsawda3", "umzaynabk2", "umsalama0", "tmnisa1" -> 5000
    "umhafsa3", "umsalama2", "umjuway3", "umsafiyya1" -> 5250
    "umkhasais2", "umjuway0", "tmahl1", "tmahl2", "tmkuffar1", "tmusat3" -> 5500
    "umkhasais0", "umaisha3", "umsafiyya2", "tmwafa1", "tmusat2", "tmkibar2", "tmsighar0" -> 5750
    "umkhasais1", "umsawda0", "umsawda1", "umsawda2", "umaisha0", "umaisha1", "umaisha2", "umhafsa0", "umhafsa2", "umzaynabk1", "umsalama1", "umzaynabj0", "umzaynabj1", "umzaynabj2", "umjuway1", "umjuway2", "umhabiba0", "umhabiba1", "umhabiba2", "umsafiyya0", "ummaymuna0", "ummaymuna2", "ummaymuna3", "tmahl0", "tmahl3", "tmwafa0", "tmwafa2", "tmbanat0", "tmbanat1", "tmbanat2", "tmbanat3", "tmahfad0", "tmahfad1", "tmahfad2", "tmahfad3", "tmjar0", "tmjar1", "tmkhadam0", "tmkhadam1", "tmkhadam3", "tmbala0", "tmbala1", "tmbala2", "tmbala3", "tmkuffar0", "tmkuffar2", "tmkuffar3", "tmjadid0", "tmjadid1", "tmjadid3", "tmusat0", "tmusat1", "tmnisa0", "tmnisa2", "tmnisa3", "tmkibar0", "tmsighar1", "tmsighar2", "tmsighar3" -> 6000
    // shortened for the enhanced voice (retime.py)
    "holdbanner", "holdcrown", "holdidol", "winghope" -> 800
    "adorn", "conceal", "fall", "forget", "itself", "nocautery", "whence" -> 1000
    "afterisha", "allcompanions", "chainedsmoke", "evenkin", "exactscale", "fihima", "khafi", "kufr", "lawfulcrack", "left", "letgo", "noomen", "noproduce", "noruqya", "permit", "praise", "refugeonly", "refused", "sealedfast", "sighting", "sinkdrop", "steal", "strife", "theten", "twoface", "ulopp", "worshiponly" -> 1250
    "add", "allcreated", "asmopp", "astray", "banners", "dust", "firmhold", "gold", "hush", "idol", "keptsafe", "nohow", "oath", "obeycmd", "orderflow", "pardon", "pleased", "rainaround", "refuse", "relay", "returned", "safeway", "sealed", "settle", "sevenrings", "shore", "shout", "shrinecall", "storm", "storm2", "topple", "umrahdone", "walker", "written" -> 1500
    "arrive", "believe", "bluntno", "cloudangel", "decree", "disobeyed", "doorshut", "featherpair", "forced", "give", "heartonly", "keeprows", "lawla", "narshut", "nogiving", "notmixed", "obeyed", "old", "open", "pitfall", "plea", "rely", "ringday", "suffice", "takbirsunan", "tenlocks", "tread", "twowords", "unequal", "wisp" -> 1750
    "absent", "allowed", "around", "basinfill", "calmfast", "court1", "court5", "court6", "debater", "erasedpage", "eyes", "fake", "fewnine", "fruitprayer", "grain", "holdtongue", "hopeonly", "incline", "jzmakharij4", "jzrawm3", "jztarqiq3", "jzwajib2", "lotetop", "mendword", "morebubbles", "nobend", "nobullet", "noprice", "notmade", "obeyfollow", "page", "prints", "restored", "sealdoor", "shutters", "sidepath", "spiral", "stones", "sun", "tawarruk", "tenring", "thirtydays", "three", "wornrenewed", "yield" -> 2000
    "abu", "against", "arch", "avoid", "banned", "byopinion", "claim", "daily", "deadleaf", "denyknown", "eachletter", "fearonly", "fifteen", "forhisface", "fuelfire", "fulfilled", "heartsun", "hufra", "idolhorns", "imamcare", "jitter", "known", "lastlit", "mushaf", "mustard", "nothing", "pipe", "pledge", "praised", "ringyear", "shelves", "smallgood", "tamadd0", "teach", "tmkhadam2", "tremble", "trialworld", "turnover", "twobytwo", "unravel", "vowbreak" -> 2250
    "allknown", "asshown", "bowidols", "bury", "cutties", "endless", "eras", "family", "fishdry", "fourharms", "goodnews", "greet", "heldup", "ignorefade", "latename", "lonewindow", "mirage", "noadhan", "nofeast", "onceorthrice", "plainshot", "predawnmeal", "rawda", "regroup", "repaid", "resolve", "restrain", "safewall", "sealofall", "sixmonths", "standingfor", "subhanmalik", "threeq", "tzmulk0", "weighflaw" -> 2500
    "agecheck", "askonly", "atforty", "behindmaqam", "bothright", "crumble", "fivelocked", "fromeverywhere", "heartbest", "horn", "jibril", "keyofprayer", "knockdoor", "loveonly", "materialscale", "mbmasjid3", "memorize", "meteor", "mikail", "moist", "movedots", "mustardgrain", "nearest", "nearfajr", "onename", "onlyallah", "onlygod", "palacegap", "rabbighfirli", "reciters", "regress", "rightsleeve", "sheathe", "sunorbit", "taidgham2", "tawqif", "tenletters", "threadlong", "threethermo", "twoconds", "twodoors", "voicechain", "warn", "winnow", "year2" -> 2750
    "ages", "beloved", "both", "branchcut", "callup", "dais", "derive", "dimtwo", "doctorfree", "energyfloor", "falsedream", "fillup", "forgotate", "foxhole", "gooddoor", "gust", "handover", "harmony", "joinpagans", "lean", "lens", "liked", "nofollow", "norecline", "nosincere", "nutritivedrip", "onewall", "organs", "palmtrunk", "quietpoor", "rainvsflood", "readbyheart", "reply", "rightfootout", "sbkaaba3", "sbridaa3", "spirit", "staymosque", "steady", "strongman", "sunsethaste", "swaptags", "tamim2", "threeroots", "threetimesword", "throwsforhim", "todaynames", "ttfajr2", "twoangels", "tworewards", "unaccepted", "unheard", "veiled", "wawa" -> 3000
    "anyclass", "arise", "badorder", "believer", "burnaway", "classify", "clockstarts", "cursed", "doubtsfall", "elevenrakat", "explained", "fromalaq", "glassbox", "gzuhud3", "harvest", "heave", "jztajwid0", "jztajwid1", "jzwasl2", "kahfread", "knock", "loveandhate", "marwantest", "mbidhn1", "mercy", "nafsroads", "nobond", "norafath", "passmiqat", "reconnect", "rescue", "sarih", "secondpillar", "sent", "sieve", "singlewitr", "tongueonly", "ttmafhum0", "twoears", "twopaths", "tzmujadila3" -> 3250
    "anytime", "betweencalls", "blindsix", "bothways", "coverveil", "curtain", "cutoff", "darknesslight", "daybreak", "doubtspark", "dukhan", "eyesopen", "eyewitness", "fewfog", "firstnight", "fourrakah", "fussilat", "houseinjannah", "hutama", "infer", "inmilla", "jzrawm0", "jzsifat3", "jztarqiq0", "jzwaqf1", "jzwasl1", "kedarhomes", "lastone", "leftlittle", "linedark", "martyrs", "mbmuakhat3", "mdsirr2", "namedwater", "nohunt", "noidol", "notlineage", "orderlyworld", "paran", "pedestal", "plateglow", "primalsea", "pullout", "qarawiyyin", "rejected", "safahill", "safering", "sealedgate", "seir", "sevencircuits", "sevenhorses", "sinai", "sinks", "skyward", "spears", "spring", "stages", "stayin", "steadydrops", "stoneheart", "tamim3", "tamithlan3", "tanun1", "threemore", "threetimes", "throne", "trace", "ttnaba3", "ttzuwwijat3", "twentyrakat", "unshackle", "updown", "vowkeep", "wdwada2", "wdwufud1", "withall", "yemenfire" -> 3500
    "abrahamic", "align", "axe", "buildonless", "chatterdoor", "crossover", "differ", "dimheart", "diploma", "eachknows", "faithsaves", "flint", "fromtext", "getup", "grandeur", "groundwash", "handless", "hastenhajj", "headlove", "ikhlasopen", "jzwajib3", "lastnightwitnessed", "learnteach", "mbmasjid2", "mbmuakhat2", "mdmaratib0", "mend", "neverclosed", "nocrowd", "nofajrjoin", "nomodel", "oldprints", "onerakah", "pillarhawa", "regretresolve", "roadshout", "sbyutm3", "sceptre", "sevenwash", "signposts", "speakright", "tamadd2", "tamithlan0", "tawbanasuh", "threequestions", "ttfajr1", "tzmunafiqun2", "tztaghabun0", "verseorder", "wakefamily", "wdwufud3", "weigh", "willed", "witnessed" -> 3750
    "basmala", "boatheld", "bounce", "claimscroll", "clearroad", "creation", "depth", "dryspot", "eightdoors", "emptied", "excused", "extract", "faithbranch", "fallword", "fastrank", "followsaid", "fourcalls", "fromabove", "gather", "gaveguarded", "guideway", "hamd", "hiddenseek", "hydra", "jannahopen", "justicecond", "jzwaqf0", "kanz", "marionette", "mindfog", "minormajor", "noharmneighbor", "nopraise", "nosaving", "olderbooks", "orchard", "othercause", "ownsource", "prayontime", "purifiedfail", "putfirst", "quarters", "rebuilt", "refer", "ruined", "ruins", "sbnasab1", "sbnasab3", "shrinklist", "sinkings", "sirat", "sowing", "speck", "straightin", "taiqlab1", "talam0", "tamaddahkam0", "tamithlan2", "threerings", "threesit", "tzqalam3", "wakeup", "zuhrshadow" -> 4000
    "aminloudlow", "aside", "braingears", "catchonerakah", "comesbynight", "forgivenpast", "goldpatch", "gzhunayn3", "gzmutah2", "hajjmonths", "hiddenknot", "howtools", "ikhlasistiana", "inkdrop", "justice", "jzmakharij0", "jzrawm2", "jzsifat2", "jztajwid3", "jztarqiq1", "lastword", "mbsukkan2", "mdhabasha3", "mdmaratib2", "movingnight", "needdua", "needmeasure", "nevernot", "nightrows", "noghulul", "notlahab", "notreturned3", "ownpurse", "plateall", "plusminus", "repent", "rightsback", "rulefour", "rulethree", "ruletwo", "sameaswalat", "sbsharh2", "soar", "surplus", "talam2", "tashbih", "tatarmosques", "threeasks", "trimexcess", "ttlafz1", "ttqadr3", "ttquran2", "ttzuwwijat0", "twocopies", "twograins", "tzmunafiqun1", "tzmursalat3", "unmixed", "venomback", "wilt" -> 4250
    "backturned", "bestgens", "blame", "chooseseasier", "cloak", "coccyx", "dhikrallowed", "doubledhere", "duasea", "eatgivegive", "everysoul", "firmgroup", "fiveconds", "foam", "fournot", "fourteen", "giftreturned", "gztabuk3", "halfdate", "heights", "honorall", "husnameaning", "israfil", "jzwajib0", "khatma", "ledprayer", "lifeafter", "listen", "loop", "mdjahr0", "mdmudd2", "messengersfirst", "moonwalk", "muddy", "nearestsujud", "nostepout", "onesheepdue", "ownwhip", "phonejail", "ransomram", "returnifnotup", "rubopp", "sbkaaba1", "sbridaa2", "scoop", "seekpath", "sharetable", "shoulder", "silencespeechsight", "sleepsister", "specialcare", "sunnahnot", "tamithlan1", "ttfajr3", "ttnaba0", "ttnaba2", "ttshaf0", "twoaskers", "twomiqat", "twopurities", "twotraits", "twoverses", "uluhiyya", "walkingvisit", "watcher", "willthird" -> 4500
    "afterbow", "allbow", "asmasifat", "asryellow", "bedside", "brightthree", "climb", "condense", "dayandnight", "devices", "dividedblessings", "fabric", "fatherofprophets", "flocks", "forward", "gatethree", "gatetwo", "greatnews", "gzkhandaq3", "gzkhaybar3", "heaviest", "jztajwid2", "keybaraa", "knowingsilence", "lastfour", "leftout", "lizardhole", "mdhabasha2", "midgate", "nightplan", "nojadd", "notisolated", "notmorethan7", "onlyone", "ownbox", "pharaoh", "pot", "praiseheart", "raiselower", "ratabtamrwater", "rememberstop", "sbnasab2", "scatter", "sharedship", "shieldback", "sixpillars", "smokeandwater", "sparks", "springwater", "swords", "tafsirpage", "taiqlab2", "tamim1", "threegifts", "threelands", "tintedcells", "trulystrong", "tttanawwu0", "ttzuwwijat2", "twelve", "twofold", "twokinds", "twopure", "tzinsan2", "tzmuddaththir1", "tzmumtahana0", "tzsaff2", "tztalaq1", "unloop", "wdbaraa1", "whispers", "widen", "witnessgate", "witrsurahs" -> 4750
    "allstars", "aminecho", "askanwat", "bestshape", "blank", "closedoor", "drawlots", "eastwest", "fatihakey", "fourmoments", "gateone", "generalcare", "goldaway", "grownchild", "guidedgive", "handsonchest", "helpedhelper", "hotsand", "jzrawm1", "jzwaqf3", "jzwasl0", "lifestages", "mdhijra3", "mdmudd0", "mdsirr0", "misertag", "mothers", "nearestbow", "nightride", "nodome", "obeysorder", "onewitness", "purecup", "rayyangate", "ringcovenant", "rootsdrink", "ruleone", "sbsharh0", "sharpenrest", "slowrecite", "smallgift", "sway", "talam3", "tanun2", "thirstgone", "threetiers", "ttlafz3", "tttadadd1", "tworakah", "twotrees", "twoverdicts", "tzmaarij0", "tzmaarij2", "tzmuddaththir3", "tzmursalat2", "tzqiyama3", "willinside" -> 5000
    "askin", "asrprofit", "behindhill", "bestform", "bestrank", "bidgood", "bondvs", "bounceback", "breaknorm", "carriedround", "countcoins", "dustinstead", "exemptions", "fastoptional", "forbrother", "freecamel", "fromstanding", "harm", "inhearts", "innocentwall", "jzmakharij3", "liars", "lookup", "manylights", "mbism3", "mbmuakhat1", "mbsukkan3", "mbwathiqa3", "mdadha2", "mdaqaba3", "mdjahr3", "mdtaif2", "mdtaif3", "meccankahf", "mechanism", "medinamarks", "movingorresting", "nearestmost", "neighborcounsel", "nohaste", "notoverlong", "onesaa", "prescription", "revealedlit", "salamsides", "sbsharh1", "sendown", "serveother", "shutdoors", "siraj", "tamadd3", "tenfold", "threetasbih", "threetraits", "tradeledger", "ttasbab1", "tzinsan1", "tzmaarij1", "tzqalam0", "usefulrain", "wdmarad2", "widens" -> 5250
    "executor", "fairscales", "flood", "khadijaonly", "reaction", "roadsign", "samebook", "selfsaw", "sevenbranches", "twoages", "twohorns" -> 5500
    "affirmverse", "everyhouse", "failgrade", "forkroads", "fourwitness", "messengers", "onmount", "samelight", "scribes", "shouldnot", "sower", "statues", "tug" -> 5750
    "askingsky", "boulder", "buriedrivers", "despair", "fortress", "isnadchain", "oneq", "qudsraised", "reproach", "rulebook", "sideroad", "stepmore" -> 6000
    "filterpaper", "fourfirst", "fourways", "missinglink", "oathsell", "repented", "saferider", "stilltrue", "twentythousand", "whysilent" -> 6250
    "branchout", "dottedpapyrus", "enter", "envoys", "fewtomany", "longestciv", "mediapile", "nostatue", "ringlife", "seizurewave", "swapped", "threeparts", "toall", "withleaders" -> 6500
    "citadel", "farchurch", "onlydoor", "pry", "syringe", "threedoors", "wail" -> 6750
    "freechoice", "muezzin", "noexcess", "popthoughts", "refusegold", "rooms", "sixsigns", "threeorigins" -> 7000
    "civculture", "danielram", "jahilidark", "noexam", "opendoors", "sahifa", "seekrefuge", "selfmade", "shaqaiq", "shieldspend", "sunnabridge", "threefogs", "threepillars", "uhudlesson" -> 7250
    "aboveref", "answers", "equalnights", "household", "proofsealed", "stonesbow" -> 7500
    "fiveconditions", "forus", "tilted", "whychain" -> 7750
    "ceilingall", "centuryanswer", "delayed", "fiftytofive", "mosquefind", "nodeeds", "qubaturn", "stillfaith", "sumthermo", "threecures", "trusted" -> 8000
    "courtfall", "eighthundred", "teacher", "trainride", "tvset", "twocompanions", "wingsfaith" -> 8250
    "cutstrings", "greatestway" -> 8500
    "amateur", "innate", "penfield", "springmass" -> 8750
    "bilaldebt", "cellglow", "choosebranches", "latescroll", "prophetsrow" -> 9000
    "addict", "timeline629" -> 9250
    "graduated", "turnaway" -> 9500
    "amanah", "boilpot", "manyproofs", "obligations", "tunecosmos" -> 9750
    "rootqara" -> 10000
    "dustworks", "everyletter", "travel", "wisdoms" -> 10250
    "readybefore" -> 10500
    "qiblaturn" -> 10750
    "closest", "cradle", "truthsame" -> 11000
    "scrollaway" -> 11250
    "fourcamels", "noask" -> 11500
    "eclipse" -> 11750
    "claim200", "fiftytwo", "jabirflask", "steles" -> 12000
    "consent" -> 14250
    "explains" -> 14750
    // wave 107: من هديه ﷺ
    "hdisbal1" -> 3750
    "hdtib2" -> 4250
    "hdbika2" -> 4500
    "hdlihya3" -> 5000
    "hdshaar3" -> 5250
    "hdisbal2" -> 5500
    "hdnaal1", "hdnawm2", "hdyaqza1" -> 5750
    "hdshaar0", "hdshaar1", "hdshaar2", "hdlihya0", "hdlihya1", "hdlihya2", "hdkhatam0", "hdkhatam1", "hdkhatam2", "hdkhatam3", "hdnaal0", "hdnaal2", "hdnaal3", "hdisbal0", "hdisbal3", "hdtib0", "hdtib1", "hdtib3", "hdnawm0", "hdnawm1", "hdnawm3", "hdyaqza0", "hdyaqza2", "hdyaqza3", "hdsawm0", "hdsawm1", "hdsawm2", "hdsawm3", "hdbika0", "hdbika1", "hdbika3", "hdmirath0", "hdmirath1", "hdmirath2", "hdmirath3" -> 6000
    // wave 108: مواقفه ﷺ وأحواله
    "ahmakka2" -> 4000
    "ahtufayl1", "ahtayammun2" -> 4250
    "ahtaam2", "ahayyam2", "ahfal0", "ahfal1" -> 5000
    "ahadab3", "ahtayammun3" -> 5250
    "ahadab2", "ahansar3" -> 5500
    "ahtufayl3" -> 5750
    "ahtufayl0", "ahtufayl2", "ahadab0", "ahadab1", "ahahabb0", "ahahabb1", "ahahabb2", "ahahabb3", "ahansar0", "ahansar1", "ahansar2", "ahtaam0", "ahtaam1", "ahtaam3", "ahmakka0", "ahmakka1", "ahmakka3", "ahayyam0", "ahayyam1", "ahayyam3", "ahtayammun0", "ahtayammun1", "ahfal2", "ahfal3", "ahyakrah0", "ahyakrah1", "ahyakrah2", "ahyakrah3" -> 6000
    // wave 109: مواقفه ﷺ وأحواله 2
    "ahghadab3" -> 2750
    "ahummati3" -> 3250
    "ahmulaaba2" -> 3750
    "ahummati1" -> 4500
    "ahfarah3", "ahhuzn1" -> 5250
    "ahhuzn2" -> 5500
    "ahghadab2", "ahhuzn3" -> 5750
    "ahummati0", "ahummati2", "ahdahik0", "ahdahik1", "ahdahik2", "ahdahik3", "ahmulaaba0", "ahmulaaba1", "ahmulaaba3", "ahghadab0", "ahghadab1", "ahfarah0", "ahfarah1", "ahfarah2", "ahhuzn0", "ahtafakkur0", "ahtafakkur1", "ahtafakkur2", "ahtafakkur3", "ahtahfiz0", "ahtahfiz1", "ahtahfiz2", "ahtahfiz3", "ahtazir0", "ahtazir1", "ahtazir2", "ahtazir3" -> 6000
    // wave 110: معرفة النبي ﷺ (المختصر المفيد)
    "mnawlad3" -> 3500
    "mntaa2" -> 3750
    "mnawlad1" -> 4250
    "mnmasajid1" -> 4500
    "mntazim1" -> 5250
    "mntazim2" -> 5500
    "mntazim0", "mntazim3", "mntaa0", "mntaa1", "mntaa3", "mnmasajid0", "mnmasajid2", "mnmasajid3", "mnawlad0", "mnawlad2" -> 6000
    // wave 111: من هديه ﷺ 2 (مختصر زاد المعاد)
    "hdtaun2" -> 3000
    "hdasbab1" -> 4250
    "hdharam1" -> 4500
    "hdhumma2", "hdwaswasa1" -> 4750
    "hdharam2" -> 5000
    "hdmanam0", "hdalfadh1" -> 5500
    "hdhumma0", "hdmanam2" -> 5750
    "hdasbab0", "hdasbab2", "hdasbab3", "hdharam0", "hdharam3", "hdhumma1", "hdhumma3", "hdtaun0", "hdtaun1", "hdtaun3", "hdayn0", "hdayn1", "hdayn2", "hdayn3", "hdruqya0", "hdruqya1", "hdruqya2", "hdruqya3", "hdkarb0", "hdkarb1", "hdkarb2", "hdkarb3", "hdmanam1", "hdmanam3", "hdwaswasa0", "hdwaswasa2", "hdwaswasa3", "hdasma0", "hdasma1", "hdasma2", "hdasma3", "hdalfadh0", "hdalfadh2", "hdalfadh3" -> 6000
    // wave 112: كيف ندرس السيرة؟
    "ssnaqd3", "ssmaghazi3" -> 4750
    "ssmaghazi2", "ssibnishaq1" -> 5250
    "ssibnishaq2" -> 5500
    "ssmasadir0", "ssmasadir1", "ssmasadir2", "ssmasadir3", "ssquran0", "ssquran1", "ssquran2", "ssquran3", "ssnaqd0", "ssnaqd1", "ssnaqd2", "ssmaghazi0", "ssmaghazi1", "ssibnishaq0", "ssibnishaq3" -> 6000
    // wave 113: أصول التفسير
    "tqray2" -> 5500
    "tqisra0", "tqray1" -> 5750
    "tqashr0", "tqashr1", "tqashr2", "tqashr3", "tqsunna0", "tqsunna1", "tqsunna2", "tqsunna3", "tqisra1", "tqisra2", "tqisra3", "tqtabiin0", "tqtabiin1", "tqtabiin2", "tqtabiin3", "tqray0", "tqray3" -> 6000
    else -> 1000
}

/** Seconds since the scene appeared (stays at 0 when the system's animations are off). */
@Composable
private fun rememberArtClock(still: Boolean): State<Float> {
    val t = remember { mutableFloatStateOf(0f) }
    if (!still) {
        LaunchedEffect(Unit) {
            var last = withFrameNanos { it }
            while (true) {
                withFrameNanos { now ->
                    t.floatValue += ((now - last) / 1_000_000_000f).coerceAtMost(0.05f)
                    last = now
                }
            }
        }
    }
    return t
}

/** What a scene's drawing reads: beat progress and tags by kind (the n-th item of that kind), and the clock. */
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

/** Scenes that write their tags large (see ArtCtx.big). */
internal val BIG_TEXT_LAYOUTS = setOf("tanun", "taidgham", "taiqlab", "tamim", "talam", "tamithlan", "tamadd", "tamaddahkam", "jzwajib", "jzmakharij", "jzsifat", "jztajwid", "jztarqiq", "jzwaqf", "jzwasl", "jzrawm", "tzmujadila", "tzhashr", "tzmumtahana", "tzsaff", "tzjumua", "tzmunafiqun", "tztaghabun", "tztalaq", "tztahrim", "tzmulk", "tzqalam", "tzhaqqa", "tzmaarij", "tznuh", "tzjinn", "tzmuzzammil", "tzmuddaththir", "tzqiyama", "tzinsan", "tzmursalat", "ttmafhum", "tttanawwu", "tttadadd", "ttasbab", "ttlafz", "tttabaqat", "tthushirat", "ttnaba", "ttzuwwijat", "ttfajr", "ttshaf", "ttquran", "tttin", "ttqadr", "ttfalaq", "tmahl", "tmwafa", "tmbanat", "tmahfad", "tmjar", "tmkhadam", "tmbala", "tmkuffar", "tmjadid", "tmusat", "tmnisa", "tmkibar", "tmsighar", "umkhasais", "umsawda", "umaisha", "umhafsa", "umzaynabk", "umsalama", "umzaynabj", "umjuway", "umhabiba", "umsafiyya", "ummaymuna", "wdwufud", "wdbaraa", "wdwada", "wdmarad", "wdwafat", "gzbadr", "gzuhud", "gzkhandaq", "gzhuday", "gzkhaybar", "gzmutah", "gzfath", "gzhunayn", "gztabuk", "mbmasjid", "mbmuakhat", "mbwathiqa", "mbism", "mbsukkan", "mbidhn", "ink", "map", "lens", "loom", "atelier", "chamber", "scroll", "hilltop", "shelfshop", "lawh", "tent", "omenroad", "triptych", "heights", "revelation", "gaze", "nameswall", "register", "unseen", "fourrules", "twoseals", "storm", "sixroots", "verseguard", "decree", "threelives", "twolands", "proofscroll", "allegiance", "companions", "household", "morningevening", "mercyrain", "deepsea", "mountaindebt", "betterjar", "healingbed", "weddinglights", "alivetree", "tenfold", "radd", "threeshown", "doubtfree", "likeforbrother", "taqwaeverywhere", "guardhim", "modesty", "straightpath", "noinjustice", "askyourheart", "headofmatter", "proofonclaimant", "awliya", "stranger", "moonface", "simpleliving", "clearspeech", "truejest", "gratefulservant", "recitelight", "onlyaservant", "noblemanners", "lastday", "rabbgarden", "fatihaseven", "falaqnas", "samadneeds", "masadsign", "nasrdawn", "baraa", "kawtharriver", "maunhouse", "filquraysh", "humazawealth", "asrhourglass", "visitors", "qariaday", "atomweight", "bayyinascroll", "qadrnight", "iqrafirst", "tinmap", "usryusrayn", "duhamorning", "layleasy", "nafsmirror", "kabad", "fajrten", "ghashiyaearth", "alaguide", "tariqstar", "burujsky", "easyreckon", "raynheart", "kiram", "takwir", "abasa", "naziat", "naba", "shafibook", "harfgood", "ruinhouse", "nightrecite", "mushaftouch", "recitemanners", "tilawasujud", "sujuddhikr", "sbnasab", "sbmawlid", "sbridaa", "sbsharh", "sbyutm", "sbhifz", "sbkaaba", "sbkhadija", "mdhira", "mdmudd", "mdmaratib", "mdsirr", "mdjahr", "mdadha", "mdhabasha", "mdshib", "mdtaif", "mdaqaba", "mdhijra", "hdshaar", "hdlihya", "hdkhatam", "hdnaal", "hdisbal", "hdtib", "hdnawm", "hdyaqza", "hdsawm", "hdbika", "hdmirath", "ahtufayl", "ahadab", "ahahabb", "ahansar", "ahtaam", "ahmakka", "ahayyam", "ahtayammun", "ahfal", "ahyakrah", "ahummati", "ahdahik", "ahmulaaba", "ahghadab", "ahfarah", "ahhuzn", "ahtafakkur", "ahtahfiz", "ahtazir", "mntazim", "mntaa", "mnmasajid", "mnawlad", "hdasbab", "hdharam", "hdhumma", "hdtaun", "hdayn", "hdruqya", "hdkarb", "hdmanam", "hdwaswasa", "hdasma", "hdalfadh", "ssmasadir", "ssquran", "ssnaqd", "ssmaghazi", "ssibnishaq", "tqashr", "tqsunna", "tqisra", "tqtabiin", "tqray")

// ───────────── shared helpers ─────────────

internal fun seg(p: Float, a: Float, b: Float): Float = ((p - a) / (b - a)).coerceIn(0f, 1f)
internal fun mix(a: Float, b: Float, f: Float): Float = a + (b - a) * f
internal fun easeOut(x: Float): Float = 1f - (1f - x.coerceIn(0f, 1f)).pow(3)
internal fun easeInOut(x: Float): Float {
    val c = x.coerceIn(0f, 1f)
    return if (c < 0.5f) 4f * c * c * c else 1f - (-2f * c + 2f).pow(3) / 2f
}
internal fun bump(x: Float): Float = if (x <= 0f || x >= 1f) 0f else sin(PI.toFloat() * x)
internal fun fract(x: Float): Float = x - floor(x)

/** A short tag on a rounded backing, centred at [c] and kept inside the picture. */
internal fun DrawScope.tagAt(tl: TextLayoutResult?, c: Offset, alpha: Float, back: Color = Color(0xB3101426), ink: Color = Color.Unspecified) {
    if (tl == null || alpha <= 0.01f) return
    val padX = 6f * density
    val padY = 2.5f * density
    val w = tl.size.width + 2 * padX
    val h = tl.size.height + 2 * padY
    // keep the tag inside what the camera shows, not just inside the picture
    val v = Cinema.cam.visible(size.width, size.height)
    val m = 2f * density
    val x = (c.x - w / 2).coerceIn(v[0] + m, max(v[0] + m, v[2] - w - m))
    val a = alpha.coerceIn(0f, 1f)
    // tags rise into place as they appear
    val y = (c.y - h / 2).coerceIn(v[1] + m, max(v[1] + m, v[3] - h - m)) + (1f - easeOut(a)) * 6f * density
    drawRoundRect(back, topLeft = Offset(x, y), size = Size(w, h), cornerRadius = CornerRadius(h / 2, h / 2), alpha = a)
    // a fine rim in the tag's own colour, like glass catching the light
    val rim = if (ink == Color.Unspecified) ArtInk.Ivory else ink
    drawRoundRect(rim, topLeft = Offset(x, y), size = Size(w, h), cornerRadius = CornerRadius(h / 2, h / 2), alpha = a * 0.22f, style = Stroke(0.8f * density))
    drawText(tl, color = ink, topLeft = Offset(x + padX, y + padY), alpha = a)
}

/** Eight-pointed star (two squares), the app's emblem and the unit of zellige patterns. */
internal fun starPath(c: Offset, r: Float): Path = Path().apply {
    val inner = r * 0.76f
    for (k in 0 until 16) {
        val ang = (k * 22.5f - 90f) * PI.toFloat() / 180f
        val rr = if (k % 2 == 0) r else inner
        val x = c.x + rr * cos(ang)
        val y = c.y + rr * sin(ang)
        if (k == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

/** Horseshoe arch opening: jambs [j] from the centre, arch circle [r] centred at ([cx], [cy]), down to [floorY]. */
internal fun horseshoe(cx: Float, cy: Float, r: Float, j: Float, floorY: Float): Path {
    val alpha = Math.toDegrees(acos((j / r).toDouble().coerceIn(-1.0, 1.0))).toFloat()
    val dy = sqrt(max(0f, r * r - j * j))
    return Path().apply {
        moveTo(cx - j, floorY)
        lineTo(cx - j, cy + dy)
        arcTo(Rect(cx - r, cy - r, cx + r, cy + r), 180f - alpha, 180f + 2 * alpha, false)
        lineTo(cx + j, floorY)
        close()
    }
}

/** Point on a cubic curve. */
internal fun cubic(p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float): Offset {
    val u = 1f - t
    val a = u * u * u
    val b = 3f * u * u * t
    val c = 3f * u * t * t
    val d = t * t * t
    return Offset(a * p0.x + b * p1.x + c * p2.x + d * p3.x, a * p0.y + b * p1.y + c * p2.y + d * p3.y)
}

/** The part of a cubic curve between [from] and [to] (0..1), as a polyline path, shifted by [shift]. */
internal fun cubicPart(p0: Offset, p1: Offset, p2: Offset, p3: Offset, from: Float, to: Float, shift: Offset = Offset.Zero): Path {
    val path = Path()
    val steps = 28
    for (k in 0..steps) {
        val q = cubic(p0, p1, p2, p3, mix(from, to, k / steps.toFloat())) + shift
        if (k == 0) path.moveTo(q.x, q.y) else path.lineTo(q.x, q.y)
    }
    return path
}

/** A Moroccan brass lantern hanging from its ring at [ring]; [s] is its size unit, [glow] its light (0..1). */
internal fun DrawScope.lantern(ring: Offset, s: Float, glow: Float, flicker: Float) {
    val x = ring.x
    val y0 = ring.y
    val light = (glow * (1f + flicker)).coerceIn(0f, 1.2f)
    // halo
    val bodyC = Offset(x, y0 + 2.9f * s)
    if (light > 0.01f) {
        val r = s * (4.5f + 3.5f * light)
        softGlow(bodyC, r, ArtInk.Gold, 0.42f * min(1f, light))
    }
    // ring and dome cap
    drawCircle(ArtInk.Brass, radius = 0.38f * s, center = Offset(x, y0), style = Stroke(0.16f * s))
    val cap = Path().apply {
        moveTo(x - 0.9f * s, y0 + 1.45f * s)
        cubicTo(x - 0.9f * s, y0 + 0.75f * s, x - 0.25f * s, y0 + 0.45f * s, x, y0 + 0.4f * s)
        cubicTo(x + 0.25f * s, y0 + 0.45f * s, x + 0.9f * s, y0 + 0.75f * s, x + 0.9f * s, y0 + 1.45f * s)
        close()
    }
    drawPath(cap, Brush.verticalGradient(listOf(ArtInk.BrassLight, ArtInk.BrassDark), startY = y0 + 0.4f * s, endY = y0 + 1.45f * s))
    // glass body: tapered top and bottom
    val body = Path().apply {
        moveTo(x - 0.75f * s, y0 + 1.45f * s)
        lineTo(x + 0.75f * s, y0 + 1.45f * s)
        lineTo(x + 1.05f * s, y0 + 2.6f * s)
        lineTo(x + 0.8f * s, y0 + 4.2f * s)
        lineTo(x - 0.8f * s, y0 + 4.2f * s)
        lineTo(x - 1.05f * s, y0 + 2.6f * s)
        close()
    }
    val glass = lerp(Color(0xFF3A2A18), ArtInk.Gold, (0.25f + 0.75f * min(1f, light)))
    drawPath(body, Brush.radialGradient(listOf(lerp(glass, ArtInk.Ivory, 0.5f * min(1f, light)), glass, lerp(glass, Color(0xFF2A1A0C), 0.5f)), center = bodyC, radius = 1.6f * s))
    // brass frame over the glass
    drawPath(body, ArtInk.BrassDark, style = Stroke(0.14f * s, join = StrokeJoin.Round))
    drawLine(ArtInk.BrassDark, Offset(x, y0 + 1.45f * s), Offset(x, y0 + 4.2f * s), 0.11f * s)
    drawLine(ArtInk.BrassDark, Offset(x - 1.05f * s, y0 + 2.6f * s), Offset(x + 1.05f * s, y0 + 2.6f * s), 0.11f * s)
    // flame
    if (light > 0.05f) {
        val fh = s * (0.55f + 0.35f * light)
        val f = Path().apply {
            moveTo(x, y0 + 3.3f * s - fh)
            cubicTo(x + 0.28f * s, y0 + 3.1f * s - fh * 0.4f, x + 0.25f * s, y0 + 3.35f * s, x, y0 + 3.45f * s)
            cubicTo(x - 0.25f * s, y0 + 3.35f * s, x - 0.28f * s, y0 + 3.1f * s - fh * 0.4f, x, y0 + 3.3f * s - fh)
            close()
        }
        drawPath(f, ArtInk.Ivory.copy(alpha = min(1f, 0.4f + light)))
    }
    // base and finial
    drawRoundRect(ArtInk.Brass, topLeft = Offset(x - 0.85f * s, y0 + 4.2f * s), size = Size(1.7f * s, 0.32f * s), cornerRadius = CornerRadius(0.12f * s, 0.12f * s))
    drawPath(Path().apply {
        moveTo(x - 0.45f * s, y0 + 4.52f * s)
        lineTo(x + 0.45f * s, y0 + 4.52f * s)
        lineTo(x, y0 + 5.3f * s)
        close()
    }, ArtInk.BrassDark)
}

// ───────────── key: «لا إله إلا الله» and its seven teeth ─────────────

private object KeyInk {
    val WallTop = Color(0xFF17463F)
    val WallBottom = Color(0xFF0A2421)
    val Stone = Color(0xFF2A6B61)
    val StoneEdge = Color(0xFF4A9585)
    val Wood = Color(0xFF5C3820)
    val WoodLight = Color(0xFF74482A)
    val WoodDark = Color(0xFF3A2212)
    val Iron = Color(0xFF24170D)
    val Band = Color(0xFF0E322E)
    val Floor = Color(0xFF071A18)
    val Beyond = Color(0xFFFFF4D2)
}

private val TOOTH_H = floatArrayOf(0.66f, 1f, 0.78f, 0.92f, 0.56f, 0.96f, 0.72f)

/**
 * The key of «لا إله إلا الله» gains one tooth per condition; with all seven it turns in the lock and the
 * door opens on light (Wahb ibn Munabbih: every key has teeth). Beats: key, tooth ×7, open.
 */
private fun DrawScope.keyScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val keyIn = easeOut(a.b("key"))
    val open = a.b("open")
    val toLock = easeInOut(seg(open, 0f, 0.3f))
    val turn = easeInOut(seg(open, 0.3f, 0.42f))
    val swing = easeInOut(seg(open, 0.42f, 0.82f))
    val light = easeOut(seg(open, 0.46f, 1f))

    // wall, with a soft vignette
    drawRect(Brush.verticalGradient(listOf(KeyInk.WallTop, KeyInk.WallBottom)))
    drawRect(Brush.radialGradient(listOf(Color.Transparent, Color(0x73000000)), center = Offset(w * 0.45f, h * 0.42f), radius = w * 0.8f))

    // the door, in a horseshoe arch on the left
    val cx = w * 0.165f
    val cy = h * 0.36f
    val r = w * 0.118f
    val j = r * 0.84f
    val floorY = h * 0.85f
    val opening = horseshoe(cx, cy, r, j, floorY)
    val frame = horseshoe(cx, cy, r * 1.22f, j * 1.22f, floorY)
    if (light > 0f) {
        val g = r * (2.2f + 1.2f * light)
        softGlow(Offset(cx, cy + r * 0.6f), g, ArtInk.Gold, 0.45f * light)
    }
    drawPath(frame, KeyInk.Stone)
    drawPath(frame, KeyInk.StoneEdge.copy(alpha = 0.7f), style = Stroke(1.2f * dp))
    drawPath(opening, KeyInk.WallBottom, style = Stroke(2.2f * dp))
    val keyhole = Offset(cx, floorY - (floorY - cy) * 0.42f)
    clipPath(opening) {
        if (light > 0f) {
            drawRect(Brush.verticalGradient(listOf(KeyInk.Beyond, ArtInk.Gold), startY = cy - r, endY = floorY), alpha = 0.35f + 0.65f * light)
        }
        withTransform({ scale(1f - 0.86f * swing, 1f, pivot = Offset(cx - j, floorY)) }) {
            doorLeaf(cx, cy, r, j, floorY, keyhole)
            if (swing > 0f) drawPath(opening, Color.Black.copy(alpha = 0.5f * swing))
        }
    }

    // floor with a zellige band
    drawRect(KeyInk.Floor, topLeft = Offset(0f, floorY), size = Size(w, h - floorY))
    val bandH = h * 0.055f
    drawRect(KeyInk.Band, topLeft = Offset(0f, floorY), size = Size(w, bandH))
    // the open door throws its light across the floor and into the room
    if (light > 0f) {
        val spill = Path().apply {
            moveTo(cx - j * 0.9f, floorY); lineTo(cx + j * 0.9f, floorY); lineTo(cx + w * 0.55f, h); lineTo(cx - j * 0.4f, h); close()
        }
        drawPath(spill, Brush.verticalGradient(listOf(ArtInk.Gold.copy(alpha = 0.42f * light), Color.Transparent), startY = floorY, endY = h))
        lightShafts(Offset(cx, cy + r * 0.3f), 0.05f, 0.9f, w * 0.75f, ArtInk.BrassLight, 0.8f * light, t, 8)
    }
    var sx = w * 0.035f
    var k = 0
    while (sx < w) {
        drawPath(starPath(Offset(sx, floorY + bandH / 2), bandH * 0.34f), if (k % 2 == 0) ArtInk.Brass.copy(alpha = 0.6f) else ArtInk.Ivory.copy(alpha = 0.22f))
        sx += w * 0.058f
        k++
    }
    if (light > 0f) {
        val pool = Path().apply {
            moveTo(cx - j, floorY)
            lineTo(cx + j, floorY)
            lineTo(cx + j * 2.8f, h)
            lineTo(cx - j * 2.2f, h)
            close()
        }
        drawPath(pool, Brush.verticalGradient(listOf(ArtInk.Gold.copy(alpha = 0.45f * light), Color.Transparent), startY = floorY, endY = h))
        // dust in the light
        for (m in 0 until 14) {
            val px = cx + (fract(m * 0.618f) - 0.5f) * j * 3.2f
            val py = cy + fract(m * 0.381f + t * 0.03f) * (floorY - cy) * 1.1f
            val tw = 0.5f + 0.5f * sin(t * 1.3f + m)
            drawCircle(ArtInk.Ivory.copy(alpha = 0.55f * light * tw), radius = (0.8f + fract(m * 0.27f)) * dp, center = Offset(px + sin(t * 0.7f + m) * 3f * dp, py))
        }
    }

    // the key: bow on the right, tip on the left pointing at the door
    val ky = h * 0.27f
    val tip = Offset(w * 0.31f, ky)
    val ringC = Offset(w * 0.915f, ky)
    val ringR = w * 0.058f
    val th = max(h * 0.042f, 6f * dp)
    val maxTooth = h * 0.1f
    val toothX = FloatArray(7) { i -> w * 0.84f - i * (w * 0.48f / 6f) }   // first condition next to the bow
    val teeth = FloatArray(7) { i -> a.b("tooth", i) }
    val keyAlpha = keyIn * (1f - seg(open, 0.42f, 0.52f))
    val bob = if (open > 0f) 0f else sin(t * 1.2f) * 1.5f * dp
    val pos = Offset(mix(tip.x, keyhole.x, toLock), mix(tip.y, keyhole.y, toLock))
    val sc = mix(1f, 0.3f, toLock)
    withTransform({
        translate(pos.x - tip.x, pos.y - tip.y + bob + (1f - keyIn) * 10f * dp)
        rotate(-90f * turn, pivot = tip)
        scale(sc, sc, pivot = tip)
    }) {
        drawKey(tip.x, ky, ringC, ringR, th, toothX, maxTooth, teeth, keyAlpha)
    }
    if (turn > 0f && turn < 1f || seg(open, 0.42f, 0.5f) in 0.01f..0.99f) {
        val f = bump(seg(open, 0.36f, 0.5f))
        softGlow(keyhole, 14f * dp, ArtInk.Ivory, 0.9f * f)
    }

    // tags: the word over the shaft, one tag per tooth in two staggered rows (lines first, tags over them)
    val fade = 1f - seg(open, 0f, 0.18f)
    tagAt(a.tag("key"), Offset(w * 0.6f, ky - h * 0.12f), keyIn * fade, back = Color(0xCC2A1C08), ink = ArtInk.BrassLight)
    val rowY = { i: Int -> ky + th / 2 + maxTooth + h * (0.08f + 0.09f * (i % 2)) }
    for (i in 0 until 7) {
        val p = seg(teeth[i], 0.3f, 0.9f) * fade * keyIn
        if (p <= 0f) continue
        val bottom = ky + th / 2 + maxTooth * TOOTH_H[i]
        drawLine(ArtInk.Ivory.copy(alpha = 0.35f * p), Offset(toothX[i], bottom + 2f * dp), Offset(toothX[i], rowY(i)), 1f * dp)
    }
    for (i in 0 until 7) {
        val p = seg(teeth[i], 0.3f, 0.9f) * fade * keyIn
        if (p > 0f) tagAt(a.tag("tooth", i), Offset(toothX[i], rowY(i)), p, back = Color(0xE6101426))
    }
}

private fun DrawScope.doorLeaf(cx: Float, cy: Float, r: Float, j: Float, floorY: Float, keyhole: Offset) {
    val dp = density
    val top = cy - r
    drawRect(
        Brush.horizontalGradient(listOf(KeyInk.Wood, KeyInk.WoodLight, KeyInk.Wood), startX = cx - j, endX = cx + j),
        topLeft = Offset(cx - r, top), size = Size(2 * r, floorY - top),
    )
    var x = cx - j + j * 0.42f
    while (x < cx + j) {
        drawLine(KeyInk.WoodDark, Offset(x, top), Offset(x, floorY), 1.1f * dp)
        x += j * 0.42f
    }
    for (fy in floatArrayOf(0.3f, 0.78f)) {
        val y = top + (floorY - top) * fy
        drawRect(KeyInk.Iron.copy(alpha = 0.85f), topLeft = Offset(cx - r, y - 1.6f * dp), size = Size(2 * r, 3.2f * dp))
        var sx = cx - j + j * 0.2f
        while (sx < cx + j) {
            drawCircle(ArtInk.Brass, radius = 1.5f * dp, center = Offset(sx, y))
            sx += j * 0.4f
        }
    }
    // brass plate and keyhole
    drawCircle(ArtInk.Brass, radius = 5.5f * dp, center = keyhole)
    drawCircle(ArtInk.BrassDark, radius = 5.5f * dp, center = keyhole, style = Stroke(1f * dp))
    drawCircle(KeyInk.Iron, radius = 1.7f * dp, center = keyhole - Offset(0f, 1f * dp))
    drawPath(Path().apply {
        moveTo(keyhole.x - 1f * dp, keyhole.y)
        lineTo(keyhole.x + 1f * dp, keyhole.y)
        lineTo(keyhole.x + 1.6f * dp, keyhole.y + 3.2f * dp)
        lineTo(keyhole.x - 1.6f * dp, keyhole.y + 3.2f * dp)
        close()
    }, KeyInk.Iron)
}

private fun DrawScope.drawKey(
    x0: Float, ky: Float, ringC: Offset, ringR: Float, th: Float,
    toothX: FloatArray, maxTooth: Float, teeth: FloatArray, alpha: Float,
) {
    if (alpha <= 0.01f) return
    val dp = density
    val shaftEnd = ringC.x - ringR * 0.9f
    val tw = th * 1.25f
    // teeth hang under the shaft, cut one by one
    for (i in toothX.indices) {
        val p = easeOut(seg(teeth[i], 0f, 0.6f))
        if (p <= 0f) continue
        val top = ky + th / 2 - 1f
        val bh = max(1f, maxTooth * TOOTH_H[i] * p)
        val bevel = min(tw * 0.25f, bh * 0.5f)
        val tooth = Path().apply {
            moveTo(toothX[i] - tw / 2, top)
            lineTo(toothX[i] + tw / 2, top)
            lineTo(toothX[i] + tw / 2, top + bh - bevel)
            lineTo(toothX[i] + tw / 4, top + bh)
            lineTo(toothX[i] - tw / 4, top + bh)
            lineTo(toothX[i] - tw / 2, top + bh - bevel)
            close()
        }
        drawPath(tooth, Brush.verticalGradient(listOf(ArtInk.Brass, ArtInk.BrassDark), startY = top, endY = top + maxTooth), alpha = alpha)
        val glint = 1f - seg(teeth[i], 0.45f, 1f)
        if (glint > 0f && teeth[i] > 0.15f) {
            val c = Offset(toothX[i], top + bh)
            softGlow(c, tw * 1.6f, ArtInk.Ivory, 0.9f * glint * alpha)
        }
    }
    // shaft, collar and bit end
    val brass = Brush.verticalGradient(listOf(ArtInk.BrassLight, ArtInk.Brass, ArtInk.BrassDark), startY = ky - th / 2, endY = ky + th / 2)
    drawRoundRect(brass, topLeft = Offset(x0, ky - th / 2), size = Size(shaftEnd - x0, th), cornerRadius = CornerRadius(th / 2, th / 2), alpha = alpha)
    val collar = Brush.verticalGradient(listOf(ArtInk.BrassLight, ArtInk.BrassDark), startY = ky - th, endY = ky + th)
    drawRoundRect(collar, topLeft = Offset(shaftEnd - th * 1.4f, ky - th * 0.9f), size = Size(th * 0.8f, th * 1.8f), cornerRadius = CornerRadius(th * 0.3f, th * 0.3f), alpha = alpha)
    drawRoundRect(collar, topLeft = Offset(x0 - th * 0.2f, ky - th * 0.75f), size = Size(th * 0.7f, th * 1.5f), cornerRadius = CornerRadius(th * 0.25f, th * 0.25f), alpha = alpha)
    // bow: a ring holding an eight-pointed star
    drawCircle(
        Brush.radialGradient(listOf(ArtInk.BrassLight, ArtInk.Brass, ArtInk.BrassDark), center = ringC - Offset(ringR * 0.3f, ringR * 0.3f), radius = ringR * 1.4f),
        radius = ringR, center = ringC, style = Stroke(ringR * 0.3f), alpha = alpha,
    )
    drawPath(starPath(ringC, ringR * 0.52f), ArtInk.Brass, alpha = alpha)
    drawPath(starPath(ringC, ringR * 0.52f), ArtInk.BrassDark, alpha = alpha, style = Stroke(0.8f * dp))
    drawCircle(KeyInk.WallBottom, radius = ringR * 0.13f, center = ringC, alpha = alpha)
    drawLine(ArtInk.Ivory.copy(alpha = 0.55f * alpha), Offset(x0 + th, ky - th * 0.22f), Offset(shaftEnd - th * 1.8f, ky - th * 0.22f), max(1f, th * 0.12f), StrokeCap.Round)
}

// ───────────── rope: the firmest handhold ─────────────

private object DuskInk {
    val Top = Color(0xFF171A3D)
    val Mid = Color(0xFF5B3A6B)
    val Low = Color(0xFFD88560)
    val Horizon = Color(0xFFF2B97B)
    val Sun = Color(0xFFFFE2AC)
    val FarHills = Color(0xFF3A2A57)
    val NearHills = Color(0xFF261C43)
    val Cliff = Color(0xFF120D24)
    val Rim = Color(0xFFE08A5F)
    val Deep = Color(0xFF05030D)
    val Rope = Color(0xFF8E6C45)
    val RopeDark = Color(0xFF4F3820)
}

/**
 * Taghut: whatever a servant exceeds his bounds with — worshipped, followed, obeyed — hangs on frayed ropes
 * over a chasm. Disbelief in taghut cuts them; belief in Allah is the firmest handhold, which never breaks
 * (al-Baqarah 256). Beats: rope ×3, cut, hold, meaning («لا إله» | «إلا الله»).
 */
private fun DrawScope.ropeScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val cut = a.b("cut")
    val hold = easeInOut(a.b("hold"))
    val meaning = easeOut(a.b("meaning"))

    // dusk sky, sun, hills
    drawRect(Brush.verticalGradient(0f to DuskInk.Top, 0.5f to DuskInk.Mid, 0.8f to DuskInk.Low, 1f to DuskInk.Horizon))
    val sun = Offset(w * 0.18f, h * 0.6f)
    softGlow(sun, w * 0.26f, DuskInk.Sun, 0.6f)
    lightShafts(sun, -PI.toFloat() / 2f, PI.toFloat() * 1.1f, w * 0.7f, DuskInk.Sun, 0.65f, t, 9)
    bloom(sun, w * 0.04f, DuskInk.Sun, 0.9f)
    for (st in TWINKLES) {
        if (st.y > 0.35f) continue
        drawCircle(ArtInk.Ivory.copy(alpha = 0.4f * (0.5f + 0.5f * sin(t * 1.3f + st.p)) * (1f - st.y * 2.6f)), st.r * 0.8f * dp, Offset(st.x * w, st.y * h))
    }
    hills(DuskInk.FarHills, h * 0.6f, h * 0.035f, 0.011f, 0.023f, 1.7f)
    hills(DuskInk.NearHills, h * 0.67f, h * 0.03f, 0.017f, 0.041f, 0.4f)

    // light from above for the firm rope
    val anchorG = Offset(w * 0.6f, -h * 0.02f)


    // the chasm between two cliffs
    val left = Path().apply {
        moveTo(0f, h * 0.7f); lineTo(w * 0.2f, h * 0.69f); lineTo(w * 0.36f, h * 0.73f); lineTo(w * 0.4f, h * 0.86f); lineTo(w * 0.37f, h); lineTo(0f, h); close()
    }
    val right = Path().apply {
        moveTo(w, h * 0.67f); lineTo(w * 0.8f, h * 0.69f); lineTo(w * 0.64f, h * 0.74f); lineTo(w * 0.6f, h * 0.88f); lineTo(w * 0.63f, h); lineTo(w, h); close()
    }
    drawRect(Brush.verticalGradient(listOf(Color.Transparent, DuskInk.Deep), startY = h * 0.7f, endY = h * 0.92f), topLeft = Offset(w * 0.3f, h * 0.7f), size = Size(w * 0.4f, h * 0.3f))
    drawPath(left, DuskInk.Cliff)
    drawPath(right, DuskInk.Cliff)
    drawLine(DuskInk.Rim.copy(alpha = 0.5f), Offset(0f, h * 0.7f), Offset(w * 0.2f, h * 0.69f), 1.3f * dp)
    drawLine(DuskInk.Rim.copy(alpha = 0.35f), Offset(w * 0.2f, h * 0.69f), Offset(w * 0.36f, h * 0.73f), 1.3f * dp)
    drawLine(DuskInk.Rim.copy(alpha = 0.35f), Offset(w, h * 0.67f), Offset(w * 0.8f, h * 0.69f), 1.3f * dp)
    drawLine(DuskInk.Rim.copy(alpha = 0.2f), Offset(w * 0.8f, h * 0.69f), Offset(w * 0.64f, h * 0.74f), 1.3f * dp)

    // the lantern: unsteady on frayed ropes, steady once it holds the firm one
    val unsteady = 1f - hold
    val sway = (sin(t * 1.7f) * 0.012f + sin(t * 3.1f + 1f) * 0.006f) * w * unsteady + sin(t * 1.1f) * 0.004f * w * hold
    val dip = 0.03f * h * bump(seg(cut, 0.2f, 0.6f))
    val ring = Offset(w * 0.5f + sway, h * 0.4f + dip)
    val s = w * 0.03f

    // the firm rope and its loop: faint at first, gold once held
    val g1 = Offset(anchorG.x, h * 0.18f)
    val g2 = Offset(ring.x + (anchorG.x - ring.x) * 0.3f, ring.y - h * 0.12f)
    val firm = cubicPart(anchorG, g1, g2, ring, 0f, 1f)
    val b0 = a.b("rope", 0)
    val b1 = a.b("rope", 1)
    val b2 = a.b("rope", 2)
    val base = max(0.18f + 0.3f * seg(cut, 0.2f, 0.7f), 0.25f + 0.5f * seg(b2, 0.3f, 0.6f))
    val firmA = mix(base, 1f, hold)
    if (hold > 0f) drawPath(firm, ArtInk.Gold.copy(alpha = 0.3f * hold), style = Stroke(8f * dp, cap = StrokeCap.Round))
    drawPath(
        firm, lerp(ArtInk.Ivory, ArtInk.Gold, hold).copy(alpha = firmA),
        style = Stroke(mix(1.5f, 3f, hold) * dp, cap = StrokeCap.Round, pathEffect = if (hold < 0.5f) PathEffect.dashPathEffect(floatArrayOf(4f * dp, 4f * dp)) else null),
    )
    drawCircle(lerp(ArtInk.Ivory, ArtInk.Gold, hold).copy(alpha = firmA), radius = 0.75f * s, center = ring + Offset(0f, -0.2f * s), style = Stroke(mix(1.2f, 2.4f, hold) * dp))
    if (hold > 0.5f) {
        val q = fract(t * 0.35f)
        val spark = cubic(anchorG, g1, g2, ring, q)
        drawCircle(ArtInk.Ivory.copy(alpha = (hold - 0.5f) * 2f * (1f - q)), radius = 2f * dp, center = spark)
    }

    // three frayed ropes: worshipped, followed, obeyed
    val anchors = listOf(Offset(w * 0.13f, -2f), Offset(w * 0.32f, -2f), Offset(w * 0.86f, -2f))
    for (k in 0 until 3) {
        val appear = easeOut(seg(a.b("rope", k), 0f, 0.7f))
        if (appear <= 0f) continue
        val p0 = anchors[k]
        val p1 = p0 + Offset(0f, h * 0.24f)
        val p2 = ring + Offset((p0.x - ring.x) * 0.28f, -h * 0.11f)
        val q = easeInOut(seg(cut, 0.12f * k, 0.12f * k + 0.62f))
        val brk = 0.3f
        val stampP = cubic(p0, p1, p2, ring, 0.42f) + Offset(if (k == 2) 22f * dp else -22f * dp, 26f * dp)
        if (q <= 0f) {
            val dim = 1f - 0.4f * seg(b1, 0.5f, 0.8f) * (if (k < 2) 1f else 0f)
            braid(cubicPart(p0, p1, p2, ring, 0f, appear), alpha = dim)
            if (k < 2) tagAt(a.tag("rope", 1, 1), stampP, seg(b1, 0.45f, 0.7f) * (1f - seg(b2, 0f, 0.25f)), back = Color(0xE6B02A20), ink = Color.White)
            tagAt(a.tag("rope", 2, 1), stampP, appear * seg(b2, 0.35f, 0.65f) * (1f - seg(cut, 0f, 0.15f)), back = Color(0xE6B02A20), ink = Color.White)
            frays(p0, p1, p2, ring, k, appear, t, 0.5f + 0.5f * seg(cut, 0f, 0.1f))
            val tagP = cubic(p0, p1, p2, ring, 0.42f)
            tagAt(a.tag("rope", k), tagP + Offset(if (k == 2) 22f * dp else -22f * dp, 0f), seg(a.b("rope", k), 0.35f, 0.9f), back = Color(0xCC4A1A10))
        } else {
            // the upper end springs back, the rest falls into the chasm
            val up = Offset(0f, -q * h * 0.12f)
            braid(cubicPart(p0, p1, p2, ring, 0f, brk, up), alpha = 1f - q)
            val fall = Offset((k - 1f) * q * w * 0.05f, q * q * h * 0.6f)
            braid(cubicPart(p0, p1, p2, ring, brk, 1f, fall), alpha = max(0.35f, (1f - q).pow(1.5f)))
            val tagP = cubic(p0, p1, p2, ring, 0.42f) + fall
            tagAt(a.tag("rope", k), tagP + Offset(if (k == 2) 22f * dp else -22f * dp, 0f), 1f - q, back = Color(0xCC4A1A10))
        }
    }

    if (cut > 0f) {
        val sweep = easeInOut(seg(cut, 0.05f, 0.45f))
        val bladeY = h * 0.2f
        val bx = mix(-w * 0.05f, w * 1.05f, sweep)
        val on = bump(seg(cut, 0.05f, 0.5f))
        drawLine(ArtInk.Ivory, Offset(bx - w * 0.12f, bladeY + h * 0.02f), Offset(bx, bladeY), 2.4f * dp, StrokeCap.Round, alpha = on)
        softGlow(Offset(bx, bladeY), w * 0.05f, ArtInk.Gold, 0.8f * on)
        tagAt(a.tag("cut"), Offset(w * 0.5f, h * 0.1f), seg(cut, 0.05f, 0.25f) * (1f - seg(hold, 0f, 0.3f)), back = Color(0xCC4A1A10), ink = ArtInk.Ember)
    }
    // at the start: the true line, faint, tagged
    tagAt(a.tag("rope", 0, 1), cubic(anchorG, g1, g2, ring, 0.4f) + Offset(40f * dp, 0f), seg(b0, 0.4f, 0.9f) * (1f - seg(b1, 0f, 0.2f)), back = Color(0xCC3A2808), ink = ArtInk.Gold)
    tagAt(a.tag("rope", 2, 2), cubic(anchorG, g1, g2, ring, 0.3f) + Offset(-34f * dp, 0f), seg(b2, 0.35f, 0.65f) * (1f - seg(cut, 0f, 0.15f)), back = Color(0xCC3A2808), ink = ArtInk.Gold)

    // the heart that hangs on them: unsteady, then steady and lit once it holds the firm one
    val hc = ring + Offset(0f, s * 1.9f)
    softGlow(hc, s * 3.2f, Color(0xFFFFD27A), mix(0.2f, 0.85f, hold))
    drawLine(lerp(ArtInk.Ivory, ArtInk.Gold, hold), ring, hc + Offset(0f, -s * 0.6f), 1.4f * dp)
    drawPath(w8Heart(hc, s * 1.35f), lerp(Color(0xFFB8A890), Color(0xFFFFD27A), hold))

    // tags: the firm handhold, then the meaning
    tagAt(a.tag("hold"), cubic(anchorG, g1, g2, ring, 0.58f) + Offset(44f * dp, 0f), seg(a.b("hold"), 0.3f, 0.9f), back = Color(0xCC3A2808), ink = ArtInk.Gold)
    tagAt(a.tag("meaning", 0, 0), Offset(w * 0.5f, h * 0.8f), meaning, back = Color(0xCC4A1A10), ink = ArtInk.Ember)
    tagAt(a.tag("meaning", 0, 1), Offset(w * 0.82f, h * 0.07f), meaning, back = Color(0xCC3A2808), ink = ArtInk.Gold)
}

internal fun DrawScope.hills(color: Color, base: Float, amp: Float, f1: Float, f2: Float, ph: Float) {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(0f, h)
        var x = 0f
        while (x <= w + 8f) {
            val xd = x / density
            lineTo(x, base + amp * (sin(xd * f1 + ph) * 0.6f + sin(xd * f2 + ph * 2.3f) * 0.4f))
            x += 6f * density
        }
        lineTo(w, h)
        close()
    }
    drawPath(path, color)
}

/** A braided rope stroke. */
private fun DrawScope.braid(path: Path, alpha: Float = 1f) {
    if (alpha <= 0.01f) return
    drawPath(path, DuskInk.Rope, alpha = alpha, style = Stroke(2.6f * density, cap = StrokeCap.Round))
    drawPath(path, DuskInk.RopeDark, alpha = alpha, style = Stroke(2.6f * density, pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.4f * density, 2.4f * density))))
}

/** Loose fibres along a rope; they stand out more as [strain] grows. */
private fun DrawScope.frays(p0: Offset, p1: Offset, p2: Offset, p3: Offset, k: Int, upTo: Float, t: Float, strain: Float) {
    for (m in 0 until 5) {
        val q = 0.15f + 0.16f * m + 0.05f * k
        if (q > upTo) continue
        val c = cubic(p0, p1, p2, p3, q)
        val d = cubic(p0, p1, p2, p3, min(1f, q + 0.02f)) - c
        val len = sqrt(d.x * d.x + d.y * d.y).coerceAtLeast(0.001f)
        val n = Offset(-d.y / len, d.x / len)
        val side = if ((m + k) % 2 == 0) 1f else -1f
        val l = (3f + 3f * strain + sin(t * 2f + m) * 1f) * density
        drawLine(DuskInk.Rope.copy(alpha = 0.8f), c, c + n * (l * side) + Offset(0f, 1.5f * density), 0.8f * density, StrokeCap.Round)
    }
}

// ───────────── ship: sincerity in the storm ─────────────

private object SeaInk {
    val CalmTop = Color(0xFF283B69)
    val CalmMid = Color(0xFF7A5A7E)
    val CalmLow = Color(0xFFF0B07A)
    val StormTop = Color(0xFF0D121C)
    val StormMid = Color(0xFF232C3D)
    val StormLow = Color(0xFF3B4658)
    val SeaCalm = Color(0xFF284A75)
    val SeaCalmDeep = Color(0xFF122746)
    val SeaStorm = Color(0xFF17212D)
    val SeaStormDeep = Color(0xFF080E16)
    val Land = Color(0xFF2B263B)
    val LandStorm = Color(0xFF151520)
    val Stone = Color(0xFF1B1726)
    val Hull = Color(0xFF2E1C11)
    val HullLight = Color(0xFF6A4528)
    val Sail = Color(0xFFEFE4C8)
    val Cloud = Color(0xFF2B3344)
}

/**
 * The polytheists of old called on Allah alone at sea in the storm, and on others once safe on land (al-Ankabut
 * 65); some later ones call on others in both. Beats: lord (they acknowledged the Creator), ease (calls split
 * to the "intercessors" on the shore), storm (one call, upward), later (the later ones: lordship claimed for
 * them too), storm2 (the split calls stay, even in the storm). Extras: the two act titles.
 */
private fun DrawScope.shipScene(a: ArtCtx) {
    val w = size.width
    val h = size.height
    val dp = density
    val t = a.t
    val lord = easeOut(a.b("lord"))
    val ease = easeOut(a.b("ease"))
    val stormA = easeInOut(seg(a.b("storm"), 0f, 0.6f)) * (1f - easeInOut(seg(a.b("later"), 0f, 0.5f)))
    val later = easeOut(a.b("later"))
    val stormB = easeInOut(seg(a.b("storm2"), 0f, 0.6f))
    val s = max(stormA, stormB)
    val horizon = h * 0.56f

    // sky: dusk calm, slate storm
    drawRect(Brush.verticalGradient(
        0f to lerp(SeaInk.CalmTop, SeaInk.StormTop, s),
        0.55f to lerp(SeaInk.CalmMid, SeaInk.StormMid, s),
        1f to lerp(SeaInk.CalmLow, SeaInk.StormLow, s),
        startY = 0f, endY = horizon,
    ), size = Size(w, horizon))
    // they acknowledged the Creator: the sky brightens at dawn of the scene
    if (lord > 0f && s < 1f) {
        val c = Offset(w * 0.6f, horizon)
        val r = w * (0.2f + 0.25f * lord)
        softGlow(c, r, ArtInk.Gold, 0.5f * lord * (1f - s))
        lightShafts(c, -PI.toFloat() / 2f, PI.toFloat() * 1.1f, w * 0.6f, Color(0xFFFFE2AC), lord * (1f - s), t, 9)
        bloom(c, w * 0.04f, Color(0xFFFFE2AC), (0.5f + 0.5f * lord) * (1f - s))
    }
    // storm clouds roll in
    if (s > 0f) {
        for (m in 0 until 9) {
            val cxx = fract(m * 0.137f + t * 0.012f * (1 + m % 3)) * (w * 1.3f) - w * 0.15f
            val cyy = h * (0.04f + 0.06f * (m % 3))
            val rr = w * (0.09f + 0.04f * (m % 4))
            drawCircle(SeaInk.Cloud.copy(alpha = 0.95f * s), radius = rr, center = Offset(cxx, cyy))
        }
        // lightning now and then
        val ph = fract(t * 0.27f + 0.3f)
        if (s > 0.55f && ph < 0.05f) {
            val f = (1f - ph / 0.05f) * s
            drawRect(Color.White.copy(alpha = 0.16f * f), size = Size(w, horizon))
            val bolt = Path().apply {
                val x0 = w * 0.16f
                moveTo(x0, h * 0.1f)
                lineTo(x0 + w * 0.03f, h * 0.22f); lineTo(x0 - w * 0.01f, h * 0.24f); lineTo(x0 + w * 0.035f, h * 0.38f)
                lineTo(x0 + w * 0.005f, h * 0.4f); lineTo(x0 + w * 0.04f, h * 0.52f)
            }
            drawPath(bolt, Color.White.copy(alpha = 0.9f * f), style = Stroke(1.6f * dp, join = StrokeJoin.Round))
        }
    }

    // sea
    drawRect(
        Brush.verticalGradient(listOf(lerp(SeaInk.SeaCalm, SeaInk.SeaStorm, s), lerp(SeaInk.SeaCalmDeep, SeaInk.SeaStormDeep, s)), startY = horizon, endY = h),
        topLeft = Offset(0f, horizon), size = Size(w, h - horizon),
    )
    for (k in 0 until 3) wave(k, horizon, s, t)

    // the shore and its standing stones (what they called on besides Allah)
    val landC = lerp(SeaInk.Land, SeaInk.LandStorm, s)
    drawPath(Path().apply {
        moveTo(w * 0.7f, h)
        lineTo(w * 0.73f, h * 0.66f)
        cubicTo(w * 0.78f, h * 0.6f, w * 0.86f, h * 0.585f, w, h * 0.58f)
        lineTo(w, h)
        close()
    }, landC)
    val stones = listOf(Offset(w * 0.82f, h * 0.6f), Offset(w * 0.885f, h * 0.588f), Offset(w * 0.945f, h * 0.585f))
    val shore = ease * (1f - stormA * (1f - later))
    for ((n, st) in stones.withIndex()) {
        val sw = w * 0.022f
        val sh = h * (0.07f + 0.012f * (n % 2))
        if (shore > 0f) {
            softGlow(st - Offset(0f, sh), sw * 2.6f, ArtInk.Ember, 0.55f * shore)
        }
        drawRoundRect(SeaInk.Stone, topLeft = Offset(st.x - sw / 2, st.y - sh), size = Size(sw, sh + 2f * dp), cornerRadius = CornerRadius(sw / 2, sw / 2))
        // the later ones: lordship claimed for them, rays to the sky
        if (later > 0f) {
            val top = st - Offset(0f, sh)
            val end = top + Offset((n - 1) * w * 0.03f, -h * 0.22f * later)
            drawLine(ArtInk.Ember.copy(alpha = 0.25f * later), top, end, 5f * dp, StrokeCap.Round)
            drawLine(ArtInk.Ember.copy(alpha = 0.9f * later), top, end, 1.8f * dp, StrokeCap.Round, PathEffect.dashPathEffect(floatArrayOf(3f * dp, 4f * dp), -t * 14f * dp))
        }
    }

    // the ship, rocking more as the storm grows
    val angle = (2f + 10f * s) * sin(t * (1.2f + 0.8f * s))
    val bob = (0.004f + 0.014f * s) * h * sin(t * (1.6f + 1.2f * s))
    val p = Offset(w * 0.36f, h * 0.7f + bob)
    val l = w * 0.16f
    withTransform({ rotate(angle, pivot = p) }) { ship(p, l, s, t) }
    val rad = angle * PI.toFloat() / 180f
    val mLocal = Offset(0.05f * l, -0.95f * l)
    val mast = p + Offset(mLocal.x * cos(rad) - mLocal.y * sin(rad), mLocal.x * sin(rad) + mLocal.y * cos(rad))
    wave(3, horizon, s, t)

    // the calls: one upward to Allah; in ease (or always, for the later ones) also to the stones
    if (ease > 0f) {
        val up = 0.55f + 0.45f * ease + 0.5f * stormA
        val top = Offset(mast.x + w * 0.05f, -4f)
        val flow = PathEffect.dashPathEffect(floatArrayOf(6f * dp, 5f * dp), t * 22f * dp)
        drawLine(ArtInk.Gold.copy(alpha = 0.25f * min(1f, up)), mast, top, 6f * dp * min(1.6f, up), StrokeCap.Round)
        drawLine(ArtInk.Gold.copy(alpha = min(1f, up)), mast, top, 1.8f * dp, StrokeCap.Round, flow)
        if (shore > 0f) {
            for ((n, st) in stones.withIndex()) {
                val end = st - Offset(0f, h * 0.085f)
                val ctrl = Offset((mast.x + end.x) / 2f, min(mast.y, end.y) - h * (0.12f + 0.03f * n))
                val arc = Path().apply {
                    moveTo(mast.x, mast.y)
                    cubicTo(ctrl.x, ctrl.y, ctrl.x, ctrl.y, end.x, end.y)
                }
                drawPath(arc, ArtInk.Ember.copy(alpha = 0.85f * shore), style = Stroke(1.4f * dp, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * dp, 5f * dp), -t * 18f * dp)))
            }
        }
    }

    // rain
    if (s > 0f) {
        val drops = (60 * s).toInt()
        for (m in 0 until drops) {
            val x0 = fract(m * 0.618f) * w * 1.1f
            val y0 = fract(t * (0.9f + 0.4f * fract(m * 0.37f)) + m * 0.13f) * h
            drawLine(Color(0xFFBFD0F5).copy(alpha = 0.45f * s), Offset(x0, y0), Offset(x0 - 4f * dp, y0 + 11f * dp), 1f * dp, StrokeCap.Round)
        }
    }

    // tags
    tagAt(a.tag("lord"), Offset(w * 0.52f, h * 0.1f), lord * (1f - s), back = Color(0xCC2A2410), ink = ArtInk.Gold)
    tagAt(a.tag("ease"), Offset(w * 0.885f, h * 0.44f), ease * (1f - 0.8f * stormA * (1f - later)), back = Color(0xCC4A1A10), ink = ArtInk.Ember)
    tagAt(a.tag("storm"), Offset(mast.x + w * 0.2f, h * 0.2f), stormA, back = Color(0xCC2A2410), ink = ArtInk.Gold)
    tagAt(a.tag("later"), Offset(w * 0.85f, h * 0.27f), later * (1f - 0.75f * stormB), back = Color(0xCC4A1A10), ink = ArtInk.Ember)
    tagAt(a.tag("storm2"), Offset(w * 0.6f, h * 0.48f), stormB, back = Color(0xE64A1A10), ink = ArtInk.Ember)
    // act titles, top right (where a right-to-left reader starts)
    val first = a.extras.getOrNull(0)
    val second = a.extras.getOrNull(1)
    if (first != null) tagAt(first, Offset(w - first.size.width / 2f - 14f * dp, 14f * dp), (1f - later) * min(1f, lord * 2f), back = Color(0x99101426))
    if (second != null) tagAt(second, Offset(w - second.size.width / 2f - 14f * dp, 14f * dp), later, back = Color(0x99101426))
}

/** Wave layer [k] (0 far … 3 front) between the horizon and the bottom. */
private fun DrawScope.wave(k: Int, horizon: Float, s: Float, t: Float) {
    val w = size.width
    val h = size.height
    val dp = density
    val y = horizon + (h - horizon) * (0.12f + 0.25f * k)
    val amp = (0.006f + 0.032f * s) * h * (1f + 0.35f * k)
    val len = w * (0.22f - 0.025f * k)
    val speed = (0.6f + 1.6f * s) * (1f + 0.2f * k)
    val col = lerp(lerp(SeaInk.SeaCalm, SeaInk.SeaCalmDeep, 0.3f * k), lerp(SeaInk.SeaStorm, SeaInk.SeaStormDeep, 0.25f * k), s)
    val path = Path().apply {
        moveTo(0f, h)
        var x = 0f
        while (x <= w + 6f * dp) {
            val ph = (x / len) * 2f * PI.toFloat() + t * speed + k * 1.7f
            lineTo(x, y + amp * sin(ph) + amp * 0.35f * sin(ph * 2.3f + 1f))
            x += 5f * dp
        }
        lineTo(w, h)
        close()
    }
    drawPath(path, col.copy(alpha = if (k == 3) 0.92f else 1f))
    // foam on the crests in the storm
    if (s > 0.2f) {
        var x = 0f
        while (x <= w) {
            val ph = (x / len) * 2f * PI.toFloat() + t * speed + k * 1.7f
            if (sin(ph) < -0.85f) drawCircle(ArtInk.Ivory.copy(alpha = 0.5f * s), radius = 1.3f * dp, center = Offset(x, y + amp * sin(ph) + amp * 0.35f * sin(ph * 2.3f + 1f)))
            x += 7f * dp
        }
    }
}

/** A dhow with a lateen sail, centred at [p] (waterline), [l] half its length. */
private fun DrawScope.ship(p: Offset, l: Float, s: Float, t: Float) {
    val dp = density
    val hull = Path().apply {
        moveTo(p.x - 1.0f * l, p.y - 0.2f * l)
        cubicTo(p.x - 0.72f * l, p.y + 0.04f * l, p.x - 0.4f * l, p.y + 0.17f * l, p.x, p.y + 0.18f * l)
        cubicTo(p.x + 0.45f * l, p.y + 0.17f * l, p.x + 0.76f * l, p.y + 0.08f * l, p.x + 0.86f * l, p.y - 0.14f * l)
        lineTo(p.x + 0.8f * l, p.y - 0.08f * l)
        lineTo(p.x - 0.88f * l, p.y - 0.1f * l)
        close()
    }
    drawPath(hull, SeaInk.Hull)
    drawLine(SeaInk.HullLight, Offset(p.x - 0.86f * l, p.y - 0.06f * l), Offset(p.x + 0.8f * l, p.y - 0.04f * l), 1.4f * dp)
    // mast and yard
    val mastTop = Offset(p.x + 0.05f * l, p.y - 0.95f * l)
    drawLine(SeaInk.Hull, Offset(p.x + 0.05f * l, p.y - 0.08f * l), mastTop, 2f * dp)
    val yardA = Offset(p.x - 0.62f * l, p.y - 0.24f * l)
    val yardB = Offset(p.x + 0.36f * l, p.y - 1.04f * l)
    val flutter = sin(t * 11f) * 0.03f * l * s
    val clew = Offset(p.x + 0.14f * l + flutter, p.y - 0.13f * l)
    val sail = Path().apply {
        moveTo(yardA.x, yardA.y)
        lineTo(yardB.x, yardB.y)
        cubicTo(p.x + 0.34f * l + flutter, p.y - 0.62f * l, p.x + 0.26f * l, p.y - 0.3f * l, clew.x, clew.y)
        close()
    }
    drawPath(sail, Brush.linearGradient(listOf(SeaInk.Sail, lerp(SeaInk.Sail, Color(0xFF8C8270), 0.55f)), start = yardB, end = clew))
    drawLine(SeaInk.Hull, yardA, yardB, 1.6f * dp, StrokeCap.Round)
    // a small lamp at the masthead
    drawCircle(ArtInk.Gold, radius = 1.8f * dp, center = mastTop)
}
