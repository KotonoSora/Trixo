package local.trixo.ui.infrastructure.persistence

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

private val Context.trixoStatsDataStore: DataStore<Preferences> by preferencesDataStore(name = "trixo_stats")

/**
 * Trixo-specific stats that intentionally stay OUT of :core-ui:
 * games counters + daily-challenge progress. Coins/inventory/audio/history
 * live in core (user_settings + game_history.db).
 *
 * Key names mirror the legacy user_preferences file so a future data
 * migration can copy values verbatim.
 */
class TrixoStatsRepository(private val context: Context) {

    private object Keys {
        val GAMES_PLAYED = intPreferencesKey("games_played")
        val GAMES_WON = intPreferencesKey("games_won")
        val LAST_CHALLENGE_RESET_TIME = longPreferencesKey("last_challenge_reset_time")
        const val CHALLENGE_PROGRESS_PREFIX = "challenge_progress_"
        const val CHALLENGE_CLAIMED_PREFIX = "challenge_claimed_"
    }

    val gamesPlayedFlow: Flow<Int> = context.trixoStatsDataStore.data
        .map { it[Keys.GAMES_PLAYED] ?: 0 }

    val gamesWonFlow: Flow<Int> = context.trixoStatsDataStore.data
        .map { it[Keys.GAMES_WON] ?: 0 }

    val challengeProgressFlow: Flow<Map<String, Int>> = context.trixoStatsDataStore.data
        .map { prefs ->
            buildMap {
                prefs.asMap().forEach { (key, value) ->
                    if (key.name.startsWith(Keys.CHALLENGE_PROGRESS_PREFIX)) {
                        put(key.name.removePrefix(Keys.CHALLENGE_PROGRESS_PREFIX), value as? Int ?: 0)
                    }
                }
            }
        }

    val challengeClaimedFlow: Flow<Map<String, Boolean>> = context.trixoStatsDataStore.data
        .map { prefs ->
            buildMap {
                prefs.asMap().forEach { (key, value) ->
                    if (key.name.startsWith(Keys.CHALLENGE_CLAIMED_PREFIX)) {
                        put(key.name.removePrefix(Keys.CHALLENGE_CLAIMED_PREFIX), value as? Boolean ?: false)
                    }
                }
            }
        }

    val lastChallengeResetTimeFlow: Flow<Long> = context.trixoStatsDataStore.data
        .map { it[Keys.LAST_CHALLENGE_RESET_TIME] ?: 0L }

    suspend fun incrementGamesPlayed() {
        context.trixoStatsDataStore.edit { prefs ->
            prefs[Keys.GAMES_PLAYED] = (prefs[Keys.GAMES_PLAYED] ?: 0) + 1
        }
    }

    suspend fun incrementGamesWon() {
        context.trixoStatsDataStore.edit { prefs ->
            prefs[Keys.GAMES_WON] = (prefs[Keys.GAMES_WON] ?: 0) + 1
        }
    }

    suspend fun updateLastChallengeResetTime(time: Long) {
        context.trixoStatsDataStore.edit { prefs ->
            prefs[Keys.LAST_CHALLENGE_RESET_TIME] = time
            // Also reset all challenge progress
            val keysToRemove = prefs.asMap().keys.filter {
                it.name.startsWith(Keys.CHALLENGE_PROGRESS_PREFIX) ||
                    it.name.startsWith(Keys.CHALLENGE_CLAIMED_PREFIX)
            }
            keysToRemove.forEach { prefs.remove(it) }
        }
    }

    suspend fun updateChallengeProgress(id: String, progress: Int) {
        context.trixoStatsDataStore.edit { prefs ->
            prefs[intPreferencesKey(Keys.CHALLENGE_PROGRESS_PREFIX + id)] = progress
        }
    }

    suspend fun markChallengeClaimed(id: String) {
        context.trixoStatsDataStore.edit { prefs ->
            prefs[booleanPreferencesKey(Keys.CHALLENGE_CLAIMED_PREFIX + id)] = true
        }
    }
}
