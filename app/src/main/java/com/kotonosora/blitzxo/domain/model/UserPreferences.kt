package com.kotonosora.blitzxo.domain.model

data class UserPreferences(
    val coins: Int = 300,
    val hints: Int = 0,
    val undos: Int = 0,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val lastChallengeResetTime: Long = 0L,
    val challengeProgress: Map<String, Int> = emptyMap(),
    val challengeClaimed: Map<String, Boolean> = emptyMap()
)
