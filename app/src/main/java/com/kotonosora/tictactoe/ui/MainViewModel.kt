package com.kotonosora.tictactoe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotonosora.tictactoe.domain.model.GameHistory
import com.kotonosora.tictactoe.domain.model.UserPreferences
import com.kotonosora.tictactoe.domain.repository.GameHistoryRepository
import com.kotonosora.tictactoe.domain.repository.UserPreferencesRepository
import com.kotonosora.tictactoe.domain.usecase.RecordGameFinishedUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    val repository: UserPreferencesRepository,
    private val historyRepository: GameHistoryRepository,
    private val recordGameFinishedUseCase: RecordGameFinishedUseCase
) : ViewModel() {
    val userPreferences: StateFlow<UserPreferences> = repository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    val gameHistory: StateFlow<List<GameHistory>> = historyRepository.allHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCoins(amount: Int) {
        viewModelScope.launch {
            repository.addCoins(amount)
        }
    }

    fun spendCoins(amount: Int, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val spent = repository.spendCoins(amount)
            if (spent) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun addHints(amount: Int) {
        viewModelScope.launch {
            repository.addHints(amount)
        }
    }

    fun consumeHint(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val consumed = repository.consumeHint()
            if (consumed) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun addUndos(amount: Int) {
        viewModelScope.launch {
            repository.addUndos(amount)
        }
    }

    fun consumeUndo(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val consumed = repository.consumeUndo()
            if (consumed) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setSoundEnabled(enabled)
        }
    }

    fun setMusicEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setMusicEnabled(enabled)
        }
    }

    fun recordGameFinished(won: Boolean, score: Int = 0, reward: Int = 0, onNewHighScore: () -> Unit = {}) {
        viewModelScope.launch {
            val isNewHigh = repository.updateHighScore(score)
            if (isNewHigh) {
                onNewHighScore()
            }
            recordGameFinishedUseCase(won, score, reward)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }
}

