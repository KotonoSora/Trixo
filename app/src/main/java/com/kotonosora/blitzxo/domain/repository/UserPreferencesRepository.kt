package com.kotonosora.blitzxo.domain.repository

import com.kotonosora.blitzxo.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateCoins(coins: Int)
    suspend fun addCoins(amount: Int)
    suspend fun addHints(amount: Int)
    suspend fun consumeHint(): Boolean
    suspend fun addUndos(amount: Int)
    suspend fun consumeUndo(): Boolean
    suspend fun spendCoins(amount: Int): Boolean
    suspend fun incrementGamesPlayed()
    suspend fun incrementGamesWon()
    suspend fun setSoundEnabled(enabled: Boolean)
    suspend fun setMusicEnabled(enabled: Boolean)
    suspend fun updateLastChallengeResetTime(time: Long)
    suspend fun updateChallengeProgress(id: String, progress: Int)
    suspend fun markChallengeClaimed(id: String)
}
