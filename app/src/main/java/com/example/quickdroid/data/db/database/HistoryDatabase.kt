package com.example.quickdroid.data.db.database

import android.content.*
import androidx.room.*
import com.example.quickdroid.data.db.dao.HistoryDao
import com.example.quickdroid.model.HistoryModel

@Database(entities = [HistoryModel::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun getHistoryDao(): HistoryDao

    companion object {
        @Volatile
        private var instance: HistoryDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            HistoryDatabase::class.java,
            "history_db"
        ).build()
    }
}