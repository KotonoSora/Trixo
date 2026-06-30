package com.jn.trixo.data.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {
    @Query("SELECT * FROM game_history ORDER BY rewardCoins DESC")
    fun getAllHistory(): Flow<List<GameHistoryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: GameHistoryEntry)

    @Query("DELETE FROM game_history")
    suspend fun clearHistory()
}
