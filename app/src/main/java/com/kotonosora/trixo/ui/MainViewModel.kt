package com.kotonosora.trixo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kotonosora.trixo.data.UserPreferences
import com.kotonosora.trixo.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(val repository: UserPreferencesRepository) : ViewModel() {
    val userPreferences: StateFlow<UserPreferences> = repository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
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

    fun recordGameFinished(won: Boolean, isDraw: Boolean = false) {
        viewModelScope.launch {
            repository.incrementGamesPlayed()
            if (won) {
                repository.incrementGamesWon()
                repository.addCoins(50) // Reward for winning
            } else if (isDraw) {
                repository.addCoins(10) // Small reward for draw
            }
        }
    }
}

class MainViewModelFactory(private val repository: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
