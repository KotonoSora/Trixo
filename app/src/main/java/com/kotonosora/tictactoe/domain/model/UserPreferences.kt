package com.kotonosora.tictactoe.domain.model

import com.kotonosora.tictactoe.utils.AppConstants

data class UserPreferences(
    val coins: Int = AppConstants.Defaults.INITIAL_COINS,
    val highScore: Int = 0,
    val hints: Int = AppConstants.Defaults.INITIAL_HINTS,
    val undos: Int = AppConstants.Defaults.INITIAL_UNDOS,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val soundEnabled: Boolean = AppConstants.Defaults.SOUND_ENABLED,
    val lastChallengeResetTime: Long = 0L,
    val challengeProgress: Map<String, Int> = emptyMap(),
    val challengeClaimed: Map<String, Boolean> = emptyMap()
)
