package com.kotonosora.tictactoe.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.staticCompositionLocalOf
import com.kotonosora.tictactoe.R

class SoundManager(context: Context? = null) {
    private val soundPool: SoundPool?
    private val sounds: Map<String, Int>
    var soundEnabled: Boolean = true

    init {
        if (context != null) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(attributes)
                .build()

            sounds = mapOf(
                "tap" to soundPool.load(context, R.raw.tap, 1),
                "error" to soundPool.load(context, R.raw.error, 1),
                "win" to soundPool.load(context, R.raw.win, 1),
                "lose" to soundPool.load(context, R.raw.lose, 1),
                "milestone_100" to soundPool.load(context, R.raw.milestone_100, 1),
                "milestone_200" to soundPool.load(context, R.raw.milestone_200, 1),
                "milestone_300" to soundPool.load(context, R.raw.milestone_300, 1),
                "milestone_400" to soundPool.load(context, R.raw.milestone_400, 1),
                "milestone_500" to soundPool.load(context, R.raw.milestone_500, 1)
            )
        } else {
            soundPool = null
            sounds = emptyMap()
        }
    }

    fun playTap() = play("tap")
    fun playError() = play("error")
    fun playWin() = play("win")
    fun playLose() = play("lose")

    fun playMilestone(score: Int) {
        val milestone = when {
            score >= 500 -> "milestone_500"
            score >= 400 -> "milestone_400"
            score >= 300 -> "milestone_300"
            score >= 200 -> "milestone_200"
            score >= 100 -> "milestone_100"
            else -> return
        }
        play(milestone)
    }

    private fun play(name: String) {
        if (!soundEnabled) return
        sounds[name]?.let { id ->
            // Priority 1 ensures gameplay sounds are heard clearly
            soundPool?.play(id, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool?.release()
    }
}

val LocalSoundManager = staticCompositionLocalOf<SoundManager> {
    // Provide a no-op SoundManager by default to avoid crashes in Previews
    SoundManager(null)
}
