package com.jn.trixo.data.history

import kotlinx.coroutines.flow.Flow

class GameHistoryRepository(private val gameHistoryDao: GameHistoryDao) {
    val allHistory: Flow<List<GameHistoryEntry>> = gameHistoryDao.getAllHistory()

    suspend fun insert(entry: GameHistoryEntry) {
        gameHistoryDao.insertEntry(entry)
    }

    suspend fun clearHistory() {
        gameHistoryDao.clearHistory()
    }
}
