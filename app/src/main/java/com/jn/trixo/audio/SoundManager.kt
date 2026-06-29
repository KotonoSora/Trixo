package com.jn.trixo.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.staticCompositionLocalOf
import com.jn.trixo.R

class SoundManager(context: Context) {
    private val soundPool: SoundPool
    private val sounds: Map<String, Int>
    var soundEnabled: Boolean = true

    init {
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
            "lose" to soundPool.load(context, R.raw.lose, 1)
        )
    }

    fun playTap() = play("tap")
    fun playError() = play("error")
    fun playWin() = play("win")
    fun playLose() = play("lose")

    private fun play(name: String) {
        if (!soundEnabled) return
        sounds[name]?.let { id ->
            // Priority 1 ensures gameplay sounds are heard clearly
            soundPool.play(id, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }
}

val LocalSoundManager = staticCompositionLocalOf<SoundManager> {
    error("No SoundManager provided")
}
