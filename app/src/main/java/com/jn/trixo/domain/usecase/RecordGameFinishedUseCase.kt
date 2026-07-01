package com.jn.trixo.domain.usecase

import com.jn.trixo.domain.model.GameHistory
import com.jn.trixo.domain.repository.GameHistoryRepository
import com.jn.trixo.domain.repository.UserPreferencesRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordGameFinishedUseCase(
    private val userPrefsRepository: UserPreferencesRepository,
    private val historyRepository: GameHistoryRepository
) {
    suspend operator fun invoke(won: Boolean, score: Int, reward: Int) {
        userPrefsRepository.incrementGamesPlayed()

        if (won) {
            userPrefsRepository.incrementGamesWon()
        }

        if (reward > 0) {
            userPrefsRepository.addCoins(reward)
        }

        // Save to history
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDateTime = sdf.format(Date())
        historyRepository.insert(
            GameHistory(
                dateTime = currentDateTime,
                rewardCoins = reward,
                score = score
            )
        )
    }
}
