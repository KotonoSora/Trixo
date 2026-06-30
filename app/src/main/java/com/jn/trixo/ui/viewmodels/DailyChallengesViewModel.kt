package com.jn.trixo.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.trixo.data.UserPreferencesRepository
import com.jn.trixo.ui.theme.NeonCyan
import com.jn.trixo.ui.theme.NeonGreen
import com.jn.trixo.ui.theme.NeonMagenta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

data class DailyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val progress: Int,
    val total: Int,
    val rewardCoins: Int,
    val color: Color,
    val isClaimed: Boolean = false
) {
    val isCompleted: Boolean get() = progress >= total
}

data class DailyChallengesUiState(
    val challenges: List<DailyChallenge> = emptyList()
)

class DailyChallengesViewModel(private val repository: UserPreferencesRepository) : ViewModel() {
    companion object {
        val INITIAL_CHALLENGES = listOf(
            DailyChallenge("1", "GAMER", "Play 5 games", 0, 5, 50, NeonCyan),
            DailyChallenge("2", "WINNER", "Win 2 games", 0, 2, 100, NeonMagenta),
            DailyChallenge("3", "STRATEGIST", "Use 3 hints", 0, 3, 30, NeonGreen)
        )
    }

    private val _uiState = MutableStateFlow(DailyChallengesUiState(challenges = INITIAL_CHALLENGES))
    val uiState: StateFlow<DailyChallengesUiState> = _uiState.asStateFlow()

    init {
        observePreferences()
        checkAndResetDailyChallenges()
    }

    private fun observePreferences() {
        viewModelScope.launch {
            repository.userPreferencesFlow.collect { prefs ->
                _uiState.update { currentState ->
                    currentState.copy(
                        challenges = INITIAL_CHALLENGES.map { challenge ->
                            challenge.copy(
                                progress = prefs.challengeProgress[challenge.id] ?: 0,
                                isClaimed = prefs.challengeClaimed[challenge.id] ?: false
                            )
                        }
                    )
                }
            }
        }
    }

    private fun checkAndResetDailyChallenges() {
        viewModelScope.launch {
            val prefs = repository.userPreferencesFlow.first()
            val lastResetTime = prefs.lastChallengeResetTime
            val currentTime = System.currentTimeMillis()

            if (isNewDay(lastResetTime, currentTime)) {
                repository.updateLastChallengeResetTime(currentTime)
            }
        }
    }

    private fun isNewDay(lastResetTime: Long, currentTime: Long): Boolean {
        if (lastResetTime == 0L) return true

        val lastResetCalendar = Calendar.getInstance().apply { timeInMillis = lastResetTime }
        val currentCalendar = Calendar.getInstance().apply { timeInMillis = currentTime }

        return lastResetCalendar.get(Calendar.DAY_OF_YEAR) != currentCalendar.get(Calendar.DAY_OF_YEAR) ||
                lastResetCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)
    }

    fun claimReward(challengeId: String, onRewardClaimed: (Int) -> Unit) {
        val challenge = _uiState.value.challenges.find { it.id == challengeId }
        if (challenge != null && (challenge.isCompleted && !challenge.isClaimed)) {
            viewModelScope.launch {
                repository.markChallengeClaimed(challengeId)
                onRewardClaimed(challenge.rewardCoins)
            }
        }
    }

    fun incrementGamerProgress() {
        updateProgress("1")
    }

    fun incrementWinnerProgress() {
        updateProgress("2")
    }

    fun incrementStrategistProgress() {
        updateProgress("3")
    }

    private fun updateProgress(challengeId: String) {
        val challenge = _uiState.value.challenges.find { it.id == challengeId } ?: return
        if (challenge.isCompleted) return

        viewModelScope.launch {
            val newProgress = (challenge.progress + 1).coerceAtMost(challenge.total)
            repository.updateChallengeProgress(challengeId, newProgress)
        }
    }
}
