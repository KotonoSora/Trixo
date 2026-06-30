package com.jn.trixo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jn.trixo.data.UserPreferences
import com.jn.trixo.data.UserPreferencesRepository
import com.jn.trixo.data.history.GameHistoryEntry
import com.jn.trixo.data.history.GameHistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(
    val repository: UserPreferencesRepository,
    private val historyRepository: GameHistoryRepository
) : ViewModel() {
    val userPreferences: StateFlow<UserPreferences> = repository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    val gameHistory: StateFlow<List<GameHistoryEntry>> = historyRepository.allHistory
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

    fun recordGameFinished(won: Boolean, score: Int = 0, reward: Int = 0) {
        viewModelScope.launch {
            repository.incrementGamesPlayed()

            if (won) {
                repository.incrementGamesWon()
            }

            if (reward > 0) {
                repository.addCoins(reward)
            }

            // Save to history
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val currentDateTime = sdf.format(Date())
            historyRepository.insert(
                GameHistoryEntry(
                    dateTime = currentDateTime,
                    rewardCoins = reward,
                    score = score
                )
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }
}

class MainViewModelFactory(
    private val repository: UserPreferencesRepository,
    private val historyRepository: GameHistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository, historyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
