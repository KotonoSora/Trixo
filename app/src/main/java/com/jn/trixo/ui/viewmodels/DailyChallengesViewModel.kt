package com.jn.trixo.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.trixo.data.UserPreferencesRepository
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
    private val _uiState = MutableStateFlow(DailyChallengesUiState())
    val uiState: StateFlow<DailyChallengesUiState> = _uiState.asStateFlow()

    init {
        checkAndResetDailyChallenges()
    }

    private fun checkAndResetDailyChallenges() {
        viewModelScope.launch {
            val prefs = repository.userPreferencesFlow.first()
            val lastResetTime = prefs.lastChallengeResetTime
            val currentTime = System.currentTimeMillis()

            if (isNewDay(lastResetTime, currentTime)) {
                resetChallenges()
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

    private fun resetChallenges() {
        _uiState.update { currentState ->
            currentState.copy(
                challenges = currentState.challenges.map {
                    it.copy(progress = 0, isClaimed = false)
                }
            )
        }
    }

    fun claimReward(challengeId: String, onRewardClaimed: (Int) -> Unit) {
        val challenge = _uiState.value.challenges.find { it.id == challengeId }
        if (challenge != null && (challenge.isCompleted && !challenge.isClaimed)) {
            _uiState.update { currentState ->
                currentState.copy(
                    challenges = currentState.challenges.map {
                        if (it.id == challengeId) it.copy(isClaimed = true) else it
                    }
                )
            }
            onRewardClaimed(challenge.rewardCoins)
        }
    }
}
