package com.jn.trixo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jn.trixo.di.AppContainer
import com.jn.trixo.domain.GameViewModel
import com.jn.trixo.ui.MainViewModel

class TrixoViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(
                    container.userPreferencesRepository,
                    container.gameHistoryRepository,
                    container.recordGameFinishedUseCase
                ) as T
            }

            modelClass.isAssignableFrom(GameViewModel::class.java) -> {
                GameViewModel(
                    container.getAiMoveUseCase,
                    container.getGameResultUseCase,
                    container.calculateScoreUseCase
                ) as T
            }

            modelClass.isAssignableFrom(DailyChallengesViewModel::class.java) -> {
                DailyChallengesViewModel(container.userPreferencesRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
