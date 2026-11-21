package app.usulcards.audio

/** What a card reads from the player to follow the voice: which clip, how far into it, and whose card it is. */
interface PlaybackView {
    val currentKey: String?
    val owner: String?
    val isPlaying: Boolean
    fun positionFraction(): Float
}
