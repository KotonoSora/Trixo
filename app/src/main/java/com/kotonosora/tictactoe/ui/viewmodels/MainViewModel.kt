package com.kotonosora.tictactoe.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotonosora.tictactoe.domain.model.GameHistory
import com.kotonosora.tictactoe.domain.model.UserPreferences
import com.kotonosora.tictactoe.domain.repository.GameHistoryRepository
import com.kotonosora.tictactoe.domain.repository.UserPreferencesRepository
import com.kotonosora.tictactoe.domain.usecase.RecordGameFinishedUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Player { X, O, NONE }
enum class GameResult { NONE, X_WINS, O_WINS, DRAW }
enum class Difficulty(val size: Int, val winReq: Int) {
    EASY(3, 3),
    MEDIUM(9, 4),
    HARD(12, 5),
    VERY_HARD(15, 6)
}

data class GameState(
    val difficulty: Difficulty = Difficulty.EASY,
    val board: List<Player> = List(3 * 3) { Player.NONE },
    val currentPlayer: Player = Player.X,
    val result: GameResult = GameResult.NONE,
    val isAiTurn: Boolean = false,
    val winningLine: List<Int>? = null,
    val hintIndex: Int? = null,
    val history: List<List<Player>> = emptyList(),
    val isPvP: Boolean = false,
    val score: Int = 0,
    val reward: Int = 0
) {
    val boardSize get() = difficulty.size
    val winRequirement get() = difficulty.winReq
}

data class MainUiState(
    val userPreferences: UserPreferences = UserPreferences(),
    val gameHistory: List<GameHistory> = emptyList()
)

sealed class MainEvent {
    data class AddCoins(val amount: Int) : MainEvent()
    data class SpendCoins(
        val amount: Int,
        val onSuccess: () -> Unit = {},
        val onFailure: () -> Unit = {}
    ) : MainEvent()

    data class AddHints(val amount: Int) : MainEvent()
    data class ConsumeHint(val onSuccess: () -> Unit = {}, val onFailure: () -> Unit = {}) :
        MainEvent()

    data class AddUndos(val amount: Int) : MainEvent()
    data class ConsumeUndo(val onSuccess: () -> Unit = {}, val onFailure: () -> Unit = {}) :
        MainEvent()

    data class SetSoundEnabled(val enabled: Boolean) : MainEvent()
    data class SetMusicEnabled(val enabled: Boolean) : MainEvent()
    data class RecordGameFinished(
        val won: Boolean,
        val score: Int = 0,
        val reward: Int = 0,
        val onNewHighScore: () -> Unit = {}
    ) : MainEvent()

    object ClearHistory : MainEvent()
}

class MainViewModel(
    val repository: UserPreferencesRepository,
    private val historyRepository: GameHistoryRepository,
    private val recordGameFinishedUseCase: RecordGameFinishedUseCase
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = combine(
        repository.userPreferencesFlow,
        historyRepository.allHistory
    ) { prefs, history ->
        MainUiState(prefs, history)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.AddCoins -> addCoins(event.amount)
            is MainEvent.SpendCoins -> spendCoins(event.amount, event.onSuccess, event.onFailure)
            is MainEvent.AddHints -> addHints(event.amount)
            is MainEvent.ConsumeHint -> consumeHint(event.onSuccess, event.onFailure)
            is MainEvent.AddUndos -> addUndos(event.amount)
            is MainEvent.ConsumeUndo -> consumeUndo(event.onSuccess, event.onFailure)
            is MainEvent.SetSoundEnabled -> setSoundEnabled(event.enabled)
            is MainEvent.SetMusicEnabled -> setMusicEnabled(event.enabled)
            is MainEvent.RecordGameFinished -> recordGameFinished(
                event.won,
                event.score,
                event.reward,
                event.onNewHighScore
            )

            MainEvent.ClearHistory -> clearHistory()
        }
    }

    private fun addCoins(amount: Int) {
        viewModelScope.launch {
            repository.addCoins(amount)
        }
    }

    private fun spendCoins(amount: Int, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val spent = repository.spendCoins(amount)
            if (spent) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    private fun addHints(amount: Int) {
        viewModelScope.launch {
            repository.addHints(amount)
        }
    }

    private fun consumeHint(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val consumed = repository.consumeHint()
            if (consumed) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    private fun addUndos(amount: Int) {
        viewModelScope.launch {
            repository.addUndos(amount)
        }
    }

    private fun consumeUndo(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val consumed = repository.consumeUndo()
            if (consumed) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    private fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setSoundEnabled(enabled)
        }
    }

    private fun setMusicEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setMusicEnabled(enabled)
        }
    }

    private fun recordGameFinished(
        won: Boolean,
        score: Int = 0,
        reward: Int = 0,
        onNewHighScore: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val isNewHigh = repository.updateHighScore(score)
            if (isNewHigh) {
                onNewHighScore()
            }
            recordGameFinishedUseCase(won, score, reward)
        }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }
}

