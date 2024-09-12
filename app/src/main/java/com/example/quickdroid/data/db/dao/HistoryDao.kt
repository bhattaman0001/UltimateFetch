package com.example.quickdroid.data.db.dao

import androidx.lifecycle.*
import androidx.room.*
import com.example.quickdroid.model.HistoryModel

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(history: HistoryModel): Long

    @Update
    fun update(history: HistoryModel): Int

    @Delete
    fun deleteHistory(history: HistoryModel)

    @Query("SELECT * FROM History")
    fun getAllHistory(): LiveData<List<HistoryModel>>

    @Transaction
    fun insertOrUpdate(history: HistoryModel): Any? {
        val id = insert(history)
        return if (id == -1L) {
            update(history)
            history.queryType
        } else {
            id
        }
    }
}