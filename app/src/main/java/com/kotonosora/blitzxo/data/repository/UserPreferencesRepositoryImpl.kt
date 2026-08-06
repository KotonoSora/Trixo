package com.kotonosora.blitzxo.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.kotonosora.blitzxo.domain.model.UserPreferences
import com.kotonosora.blitzxo.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepository {
    private object PreferencesKeys {
        val COINS = intPreferencesKey("coins")
        val HINTS = intPreferencesKey("hints")
        val UNDOS = intPreferencesKey("undos")
        val GAMES_PLAYED = intPreferencesKey("games_played")
        val GAMES_WON = intPreferencesKey("games_won")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val LAST_CHALLENGE_RESET_TIME = longPreferencesKey("last_challenge_reset_time")
        val CHALLENGE_PROGRESS_PREFIX = "challenge_progress_"
        val CHALLENGE_CLAIMED_PREFIX = "challenge_claimed_"
    }

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        val coins = preferences[PreferencesKeys.COINS] ?: 300
        val hints = preferences[PreferencesKeys.HINTS] ?: 0
        val undos = preferences[PreferencesKeys.UNDOS] ?: 0
        val gamesPlayed = preferences[PreferencesKeys.GAMES_PLAYED] ?: 0
        val gamesWon = preferences[PreferencesKeys.GAMES_WON] ?: 0
        val soundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: true
        val musicEnabled = preferences[PreferencesKeys.MUSIC_ENABLED] ?: true
        val lastResetTime = preferences[PreferencesKeys.LAST_CHALLENGE_RESET_TIME] ?: 0L

        val challengeProgress = mutableMapOf<String, Int>()
        val challengeClaimed = mutableMapOf<String, Boolean>()

        preferences.asMap().forEach { (key, value) ->
            if (key.name.startsWith(PreferencesKeys.CHALLENGE_PROGRESS_PREFIX)) {
                val id = key.name.removePrefix(PreferencesKeys.CHALLENGE_PROGRESS_PREFIX)
                challengeProgress[id] = value as? Int ?: 0
            } else if (key.name.startsWith(PreferencesKeys.CHALLENGE_CLAIMED_PREFIX)) {
                val id = key.name.removePrefix(PreferencesKeys.CHALLENGE_CLAIMED_PREFIX)
                challengeClaimed[id] = value as? Boolean ?: false
            }
        }

        UserPreferences(
            coins = coins,
            hints = hints,
            undos = undos,
            gamesPlayed = gamesPlayed,
            gamesWon = gamesWon,
            soundEnabled = soundEnabled,
            musicEnabled = musicEnabled,
            lastChallengeResetTime = lastResetTime,
            challengeProgress = challengeProgress,
            challengeClaimed = challengeClaimed
        )
    }

    override suspend fun updateCoins(coins: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COINS] = coins
        }
    }

    override suspend fun addCoins(amount: Int) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.COINS] ?: 300
            preferences[PreferencesKeys.COINS] = current + amount
        }
    }

    override suspend fun addHints(amount: Int) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.HINTS] ?: 0
            preferences[PreferencesKeys.HINTS] = current + amount
        }
    }

    override suspend fun consumeHint(): Boolean {
        var consumed = false
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.HINTS] ?: 0
            if (current > 0) {
                preferences[PreferencesKeys.HINTS] = current - 1
                consumed = true
            }
        }
        return consumed
    }

    override suspend fun addUndos(amount: Int) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.UNDOS] ?: 0
            preferences[PreferencesKeys.UNDOS] = current + amount
        }
    }

    override suspend fun consumeUndo(): Boolean {
        var consumed = false
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.UNDOS] ?: 0
            if (current > 0) {
                preferences[PreferencesKeys.UNDOS] = current - 1
                consumed = true
            }
        }
        return consumed
    }

    override suspend fun spendCoins(amount: Int): Boolean {
        var spent = false
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.COINS] ?: 300
            if (current >= amount) {
                preferences[PreferencesKeys.COINS] = current - amount
                spent = true
            }
        }
        return spent
    }

    override suspend fun incrementGamesPlayed() {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.GAMES_PLAYED] ?: 0
            preferences[PreferencesKeys.GAMES_PLAYED] = current + 1
        }
    }

    override suspend fun incrementGamesWon() {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.GAMES_WON] ?: 0
            preferences[PreferencesKeys.GAMES_WON] = current + 1
        }
    }

    override suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    override suspend fun setMusicEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MUSIC_ENABLED] = enabled
        }
    }

    override suspend fun updateLastChallengeResetTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_CHALLENGE_RESET_TIME] = time
            // Also reset all challenge progress
            val keysToRemove = preferences.asMap().keys.filter {
                it.name.startsWith(PreferencesKeys.CHALLENGE_PROGRESS_PREFIX) ||
                        it.name.startsWith(PreferencesKeys.CHALLENGE_CLAIMED_PREFIX)
            }
            keysToRemove.forEach { preferences.remove(it) }
        }
    }

    override suspend fun updateChallengeProgress(id: String, progress: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(PreferencesKeys.CHALLENGE_PROGRESS_PREFIX + id)] =
                progress
        }
    }

    override suspend fun markChallengeClaimed(id: String) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(PreferencesKeys.CHALLENGE_CLAIMED_PREFIX + id)] = true
        }
    }
}
