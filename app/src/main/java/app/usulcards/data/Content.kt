package app.usulcards.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class Book(val id: String, val title: String, val author: String)

data class Evidence(
    val kind: String,          // "quran" or "hadith"
    val ref: String,           // quran: "2:177" or "103:1-3"
    val label: String,         // quran: "البقرة: 177"
    val text: String,          // verse text (simple script) or hadith text
    val takhrij: String,
    val note: String,
)

data class SourceRef(val book: String, val pages: String)
data class Flag(val level: String, val text: String)
data class AudioSeg(val key: String, val target: String, val layer: String, val file: String)
data class Quiz(val q: String, val a: String)

/**
 * One element of a card animation. It appears when the card's audio reaches clip [at]
 * (e.g. "face-0") and that clip has played [frac] of its length; with no audio the scene previews by itself.
 */
data class SceneItem(
    val label: String,
    val sub: String,
    val at: String,
    val frac: Float,
    val side: Int,      // sides: 0 = first column, 1 = second; chain: forks
    val value: Float,   // timeline: years
    val kind: String,   // tone override or role inside a custom scene
)

/** A group of learning paths (modules) shown together; [glyph] picks its emblem. */
data class Category(val id: String, val title: String, val about: String, val glyph: String, val paths: List<String>)

/** A light animation drawn above the card text; [type] picks the renderer. */
data class Scene(
    val type: String,
    val layout: String,
    val title: String,
    val tone: String,
    val sides: List<Pair<String, String>>,   // (title, tone) for the two columns of a "sides" scene
    val items: List<SceneItem>,
    val note: String,
)

data class Card(
    val id: String,
    val type: String,
    val topic: String,
    val path: String,
    val order: Int,
    val title: String,
    val faceSentences: List<String>,
    val deeper: List<String>,
    val evidence: List<Evidence>,
    val sources: List<SourceRef>,
    val related: List<String>,
    val verify: List<Flag>,
    val quiz: Quiz,
    val audio: List<AudioSeg>,
    val diac: Map<String, String>,   // vowelled text by target ("title", "face:0", "deeper:1", "hadith:0")
    val scene: Scene?,
) {
    val needsReview: Boolean get() = verify.any { it.level == "review" }
    val books: List<String> get() = sources.map { it.book }.distinct()
}

/** Verse references of an evidence item, e.g. "103:1-3" -> [(103,1),(103,2),(103,3)]. */
fun Evidence.ayat(): List<Pair<Int, Int>> {
    if (kind != "quran") return emptyList()
    val parts = ref.split(":")
    val surah = parts[0].toIntOrNull() ?: return emptyList()
    val range = parts.getOrElse(1) { "" }.split("-")
    val lo = range[0].toIntOrNull() ?: return emptyList()
    val hi = range.getOrNull(1)?.toIntOrNull() ?: lo
    return (lo..hi).map { surah to it }
}

fun ayahFile(surah: Int, ayah: Int): String = "quran/%03d%03d.mp3".format(surah, ayah)

class ContentRepo(
    val books: Map<String, Book>,
    val cards: List<Card>,
    val categories: List<Category>,
    private val uthmani: Map<String, String>,
    private val recitations: Set<String>,
) {
    val byId: Map<String, Card> = cards.associateBy { it.id }

    /** Paths in category order, then any path that no category lists. */
    val paths: List<String> = run {
        val used = cards.map { it.path }.distinct()
        categories.flatMap { it.paths }.filter { it in used } + used.filter { p -> categories.none { p in it.paths } }
    }

    fun category(id: String?): Category? = categories.firstOrNull { it.id == id }
    fun categoryOf(path: String): Category? = categories.firstOrNull { path in it.paths }

    /** Cards of one path (in path order), of one category (path by path), or all cards. */
    fun cardsFor(categoryId: String?, path: String?): List<Card> {
        val cat = category(categoryId)
        return when {
            path != null -> cards.filter { it.path == path }.sortedBy { it.order }
            cat != null -> cards.filter { it.path in cat.paths }.sortedWith(compareBy<Card>({ cat.paths.indexOf(it.path) }, { it.order }))
            else -> cards
        }
    }

    /** Verse text in Uthmani script when the build fetched it, otherwise the simple text from the card. */
    fun verseText(e: Evidence): String {
        val ayat = e.ayat()
        val parts = ayat.map { (s, a) -> uthmani["$s:$a"] }
        return if (parts.isNotEmpty() && parts.all { it != null }) {
            ayat.zip(parts).joinToString(" ") { (sa, t) -> "$t ﴿${arabicDigits(sa.second)}﴾" }
        } else e.text
    }

    fun recitationFiles(e: Evidence): List<String> =
        e.ayat().map { (s, a) -> ayahFile(s, a) }.filter { it in recitations }

    companion object {
        fun load(ctx: Context): ContentRepo {
            val root = JSONObject(ctx.assets.open("content/cards.json").bufferedReader(Charsets.UTF_8).use { it.readText() })
            val booksJson = root.getJSONObject("books")
            val books = booksJson.keys().asSequence().associateWith { id ->
                val b = booksJson.getJSONObject(id)
                Book(id, b.getString("title"), b.getString("author"))
            }
            val cards = root.getJSONArray("cards").objects().map { parseCard(it) }
            val uthmani = try {
                val u = JSONObject(ctx.assets.open("quran/uthmani.json").bufferedReader(Charsets.UTF_8).use { it.readText() })
                u.keys().asSequence().associateWith { u.getString(it) }
            } catch (e: Exception) {
                emptyMap()
            }
            val recitations = (ctx.assets.list("quran") ?: emptyArray()).filter { it.endsWith(".mp3") }.map { "quran/$it" }.toSet()
            val categories = (root.optJSONArray("categories") ?: JSONArray()).objects().map {
                Category(it.getString("id"), it.getString("title"), it.optString("about", ""), it.optString("glyph", ""),
                    it.getJSONArray("paths").strings())
            }.ifEmpty {
                // older packs: one category per topic
                cards.groupBy { it.topic }.map { (topic, cs) -> Category(topic, topic, "", "", cs.map { it.path }.distinct()) }
            }
            return ContentRepo(books, cards, categories, uthmani, recitations)
        }

        private fun parseCard(o: JSONObject): Card {
            val quiz = o.getJSONObject("quiz")
            val diacJson = o.optJSONObject("diac") ?: JSONObject()
            return Card(
                id = o.getString("id"),
                type = o.getString("type"),
                topic = o.getString("topic"),
                path = o.getString("path"),
                order = o.optInt("order", 0),
                title = o.getString("title"),
                faceSentences = o.getJSONArray("faceSentences").strings(),
                deeper = o.getJSONArray("deeper").strings(),
                evidence = o.getJSONArray("evidence").objects().map {
                    Evidence(
                        kind = it.getString("kind"),
                        ref = it.optString("ref", ""),
                        label = it.optString("label", ""),
                        text = it.optString("text", ""),
                        takhrij = it.optString("takhrij", ""),
                        note = it.optString("note", ""),
                    )
                },
                sources = o.getJSONArray("sources").objects().map { SourceRef(it.getString("book"), it.getString("pages")) },
                related = o.getJSONArray("related").strings(),
                verify = (o.optJSONArray("verify") ?: JSONArray()).objects().map { Flag(it.getString("level"), it.getString("text")) },
                quiz = Quiz(quiz.getString("q"), quiz.getString("a")),
                audio = (o.optJSONArray("audio") ?: JSONArray()).objects().map {
                    AudioSeg(it.getString("key"), it.getString("target"), it.getString("layer"), it.getString("file"))
                },
                diac = diacJson.keys().asSequence().associateWith { diacJson.getString(it) },
                scene = o.optJSONObject("scene")?.let { parseScene(it) },
            )
        }

        private fun parseScene(s: JSONObject): Scene = Scene(
            type = s.getString("type"),
            layout = s.optString("layout", ""),
            title = s.optString("title", ""),
            tone = s.optString("tone", ""),
            sides = (s.optJSONArray("sides") ?: JSONArray()).objects().map { it.optString("title", "") to it.optString("tone", "") },
            items = s.getJSONArray("items").objects().map {
                SceneItem(
                    label = it.optString("label", ""),
                    sub = it.optString("sub", ""),
                    at = it.optString("at", ""),
                    frac = it.optDouble("frac", 0.0).toFloat(),
                    side = it.optInt("side", -1),
                    value = it.optDouble("value", 0.0).toFloat(),
                    kind = it.optString("kind", ""),
                )
            },
            note = s.optString("note", ""),
        )
    }
}

private fun JSONArray.objects(): List<JSONObject> = (0 until length()).map { getJSONObject(it) }
private fun JSONArray.strings(): List<String> = (0 until length()).map { getString(it) }

fun arabicDigits(n: Int): String = n.toString().map { c -> if (c in '0'..'9') '٠' + (c - '0') else c }.joinToString("")
