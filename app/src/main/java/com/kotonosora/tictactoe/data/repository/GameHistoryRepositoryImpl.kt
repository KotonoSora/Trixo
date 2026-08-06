package com.kotonosora.tictactoe.data.repository

import com.kotonosora.tictactoe.data.history.GameHistoryDao
import com.kotonosora.tictactoe.data.mapper.toDomain
import com.kotonosora.tictactoe.data.mapper.toEntity
import com.kotonosora.tictactoe.domain.model.GameHistory
import com.kotonosora.tictactoe.domain.repository.GameHistoryRepository
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
