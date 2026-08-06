package com.kotonosora.blitzxo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotonosora.blitzxo.data.history.GameHistoryDao
import com.kotonosora.blitzxo.data.history.GameHistoryEntry

@Database(entities = [GameHistoryEntry::class], version = 1, exportSchema = false)
abstract class TrixoDatabase : RoomDatabase() {
    abstract fun gameHistoryDao(): GameHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: TrixoDatabase? = null

        fun getDatabase(context: Context): TrixoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrixoDatabase::class.java,
                    "trixo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
