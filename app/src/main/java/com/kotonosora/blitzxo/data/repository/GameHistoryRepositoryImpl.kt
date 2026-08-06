package com.kotonosora.blitzxo.data.repository

import com.kotonosora.blitzxo.data.history.GameHistoryDao
import com.kotonosora.blitzxo.data.mapper.toDomain
import com.kotonosora.blitzxo.data.mapper.toEntity
import com.kotonosora.blitzxo.domain.model.GameHistory
import com.kotonosora.blitzxo.domain.repository.GameHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameHistoryRepositoryImpl(private val gameHistoryDao: GameHistoryDao) :
    GameHistoryRepository {
    override val allHistory: Flow<List<GameHistory>> = gameHistoryDao.getAllHistory().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun insert(entry: GameHistory) {
        gameHistoryDao.insertEntry(entry.toEntity())
    }

    override suspend fun clearHistory() {
        gameHistoryDao.clearHistory()
    }
}
