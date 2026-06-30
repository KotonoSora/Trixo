package com.jn.trixo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val coins: Int = 0,
    val hints: Int = 0,
    val undos: Int = 0,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val lastChallengeResetTime: Long = 0L
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val COINS = intPreferencesKey("coins")
        val HINTS = intPreferencesKey("hints")
        val UNDOS = intPreferencesKey("undos")
        val GAMES_PLAYED = intPreferencesKey("games_played")
        val GAMES_WON = intPreferencesKey("games_won")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val LAST_CHALLENGE_RESET_TIME = longPreferencesKey("last_challenge_reset_time")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        val coins = preferences[PreferencesKeys.COINS] ?: 0
        val hints = preferences[PreferencesKeys.HINTS] ?: 0
        val undos = preferences[PreferencesKeys.UNDOS] ?: 0
        val gamesPlayed = preferences[PreferencesKeys.GAMES_PLAYED] ?: 0
        val gamesWon = preferences[PreferencesKeys.GAMES_WON] ?: 0
        val soundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: true
        val musicEnabled = preferences[PreferencesKeys.MUSIC_ENABLED] ?: true
        val lastResetTime = preferences[PreferencesKeys.LAST_CHALLENGE_RESET_TIME] ?: 0L

        UserPreferences(
            coins = coins,
            hints = hints,
            undos = undos,
            gamesPlayed = gamesPlayed,
            gamesWon = gamesWon,
            soundEnabled = soundEnabled,
            musicEnabled = musicEnabled,
            lastChallengeResetTime = lastResetTime
        )
    }

    suspend fun updateCoins(coins: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COINS] = coins
        }
    }

    suspend fun addCoins(amount: Int) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.COINS] ?: 0
            preferences[PreferencesKeys.COINS] = current + amount
        }
    }

    suspend fun addHints(amount: Int) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.HINTS] ?: 0
            preferences[PreferencesKeys.HINTS] = current + amount
        }
    }

    suspend fun consumeHint(): Boolean {
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

    suspend fun addUndos(amount: Int) {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.UNDOS] ?: 0
            preferences[PreferencesKeys.UNDOS] = current + amount
        }
    }

    suspend fun consumeUndo(): Boolean {
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

    suspend fun spendCoins(amount: Int): Boolean {
        var spent = false
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.COINS] ?: 0
            if (current >= amount) {
                preferences[PreferencesKeys.COINS] = current - amount
                spent = true
            }
        }
        return spent
    }

    suspend fun incrementGamesPlayed() {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.GAMES_PLAYED] ?: 0
            preferences[PreferencesKeys.GAMES_PLAYED] = current + 1
        }
    }

    suspend fun incrementGamesWon() {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.GAMES_WON] ?: 0
            preferences[PreferencesKeys.GAMES_WON] = current + 1
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setMusicEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MUSIC_ENABLED] = enabled
        }
    }

    suspend fun updateLastChallengeResetTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_CHALLENGE_RESET_TIME] = time
        }
    }
}
