package local.trixo.ui.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import local.core.ui.data.CoinAccount
import local.trixo.ui.infrastructure.persistence.TrixoStatsRepository
import local.trixo.ui.presentation.AppConstants
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

sealed class DailyChallengesEvent {
    data class ClaimReward(val challengeId: String, val onRewardClaimed: (Int) -> Unit) :
        DailyChallengesEvent()

    object IncrementGamerProgress : DailyChallengesEvent()
    object IncrementWinnerProgress : DailyChallengesEvent()
    object IncrementStrategistProgress : DailyChallengesEvent()
}

class DailyChallengesViewModel(
    private val statsRepository: TrixoStatsRepository,
    private val coinAccount: CoinAccount
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(DailyChallengesUiState(challenges = AppConstants.Challenges.INITIAL_CHALLENGES))
    val uiState: StateFlow<DailyChallengesUiState> = _uiState.asStateFlow()

    init {
        observePreferences()
        checkAndResetDailyChallenges()
    }

    private fun observePreferences() {
        viewModelScope.launch {
            kotlinx.coroutines.flow.combine(
                statsRepository.challengeProgressFlow,
                statsRepository.challengeClaimedFlow
            ) { progress, claimed ->
                AppConstants.Challenges.INITIAL_CHALLENGES.map { challenge ->
                    challenge.copy(
                        progress = progress[challenge.id] ?: 0,
                        isClaimed = claimed[challenge.id] ?: false
                    )
                }
            }.collect { challenges ->
                _uiState.update { it.copy(challenges = challenges) }
            }
        }
    }

    private fun checkAndResetDailyChallenges() {
        viewModelScope.launch {
            val lastResetTime = statsRepository.lastChallengeResetTimeFlow.first()
            val currentTime = System.currentTimeMillis()

            if (isNewDay(lastResetTime, currentTime)) {
                statsRepository.updateLastChallengeResetTime(currentTime)
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

    fun onEvent(event: DailyChallengesEvent) {
        when (event) {
            is DailyChallengesEvent.ClaimReward -> claimReward(
                event.challengeId,
                event.onRewardClaimed
            )

            DailyChallengesEvent.IncrementGamerProgress -> incrementGamerProgress()
            DailyChallengesEvent.IncrementWinnerProgress -> incrementWinnerProgress()
            DailyChallengesEvent.IncrementStrategistProgress -> incrementStrategistProgress()
        }
    }

    private fun claimReward(challengeId: String, onRewardClaimed: (Int) -> Unit) {
        val challenge = _uiState.value.challenges.find { it.id == challengeId }
        if (challenge != null && (challenge.isCompleted && !challenge.isClaimed)) {
            viewModelScope.launch {
                statsRepository.markChallengeClaimed(challengeId)
                coinAccount.addCoins(challenge.rewardCoins)
                onRewardClaimed(challenge.rewardCoins)
            }
        }
    }

    private fun incrementGamerProgress() {
        updateProgress("1")
    }

    private fun incrementWinnerProgress() {
        updateProgress("2")
    }

    private fun incrementStrategistProgress() {
        updateProgress("3")
    }

    private fun updateProgress(challengeId: String) {
        val challenge = _uiState.value.challenges.find { it.id == challengeId } ?: return
        if (challenge.isCompleted) return

        viewModelScope.launch {
            val newProgress = (challenge.progress + 1).coerceAtMost(challenge.total)
            statsRepository.updateChallengeProgress(challengeId, newProgress)
        }
    }
}
