package com.kotonosora.tictactoe.domain.repository

import com.kotonosora.tictactoe.domain.model.GameHistory
import kotlinx.coroutines.flow.Flow

interface GameHistoryRepository {
    val allHistory: Flow<List<GameHistory>>
    suspend fun insert(entry: GameHistory)
    suspend fun clearHistory()
}
