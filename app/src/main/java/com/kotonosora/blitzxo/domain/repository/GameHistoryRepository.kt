package com.kotonosora.blitzxo.domain.repository

import com.kotonosora.blitzxo.domain.model.GameHistory
import kotlinx.coroutines.flow.Flow

interface GameHistoryRepository {
    val allHistory: Flow<List<GameHistory>>
    suspend fun insert(entry: GameHistory)
    suspend fun clearHistory()
}
