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
    private val uthmani: Map<String, String>,
    private val recitations: Set<String>,
) {
    val byId: Map<String, Card> = cards.associateBy { it.id }
    val paths: List<String> = cards.map { it.path }.distinct()

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
            return ContentRepo(books, cards, uthmani, recitations)
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
            )
        }
    }
}

private fun JSONArray.objects(): List<JSONObject> = (0 until length()).map { getJSONObject(it) }
private fun JSONArray.strings(): List<String> = (0 until length()).map { getString(it) }

fun arabicDigits(n: Int): String = n.toString().map { c -> if (c in '0'..'9') '٠' + (c - '0') else c }.joinToString("")
