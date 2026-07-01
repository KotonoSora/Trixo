package com.jn.trixo.data.repository

import com.jn.trixo.data.history.GameHistoryDao
import com.jn.trixo.data.mapper.toDomain
import com.jn.trixo.data.mapper.toEntity
import com.jn.trixo.domain.model.GameHistory
import com.jn.trixo.domain.repository.GameHistoryRepository
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
