package app.usulcards.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import app.usulcards.audio.AudioPlayer
import app.usulcards.data.ProgressStore
import com.google.android.play.core.review.ReviewManagerFactory

/*
 * Google Play in-app review, asked once, only after a positive moment (a finished path or a right quiz answer),
 * after 2 distinct days of use and 5 finished cards, and never while the voice is playing. Play itself decides
 * whether the sheet shows; the app never asks again either way (owner request 2026-11-17).
 */
internal fun maybeAskReview(ctx: Context, progress: ProgressStore, player: AudioPlayer) {
    if (!progress.reviewDue() || player.isPlaying) return
    val activity = ctx.findActivity() ?: return
    progress.markReviewAsked()
    val manager = ReviewManagerFactory.create(activity)
    manager.requestReviewFlow().addOnCompleteListener { task ->
        if (task.isSuccessful && !activity.isFinishing) manager.launchReviewFlow(activity, task.result)
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
