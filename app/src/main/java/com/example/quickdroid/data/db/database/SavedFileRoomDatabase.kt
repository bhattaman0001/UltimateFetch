package com.example.quickdroid.data.db.database

import android.content.*
import androidx.room.*
import com.example.quickdroid.data.db.dao.ResponseDao
import com.example.quickdroid.model.FileDetailedModel

@Database(entities = [FileDetailedModel::class], version = 1)
abstract class SavedFileRoomDatabase : RoomDatabase() {
    abstract fun getFileDaa(): ResponseDao

    companion object {
        @Volatile
        private var instance: SavedFileRoomDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SavedFileRoomDatabase::class.java,
            "saved_file_database"
        ).build()

        /*fun getInstance(context: Context): SavedFileRoomDatabase {
            return instance ?: synchronized(LOCK) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    SavedFileRoomDatabase::class.java,
                    "saved_file_db"
                ).build()
                instance = database
                database
            }
        }*/
    }
}