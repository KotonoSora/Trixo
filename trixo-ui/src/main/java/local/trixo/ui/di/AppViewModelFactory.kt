package local.trixo.ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import local.trixo.ui.presentation.viewmodel.DailyChallengesViewModel
import local.trixo.ui.presentation.viewmodel.GameViewModel
import local.trixo.ui.presentation.viewmodel.MainViewModel

class AppViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(
                    container.coinAccount,
                    container.inventoryStore,
                    container.audioSettings,
                    container.runHistoryRepository,
                    container.statsRepository,
                    container.recordGameFinishedUseCase
                ) as T
            }

            modelClass.isAssignableFrom(GameViewModel::class.java) -> {
                GameViewModel(
                    container.aiMoveService,
                    container.gameRulesService
                ) as T
            }

            modelClass.isAssignableFrom(DailyChallengesViewModel::class.java) -> {
                DailyChallengesViewModel(
                    container.statsRepository,
                    container.coinAccount
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
