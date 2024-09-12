package com.example.quickdroid.data.db.dao

import androidx.lifecycle.*
import androidx.room.*
import com.example.quickdroid.model.FileDetailedModel

@Dao
interface ResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFile(file: FileDetailedModel): Long

    @Query("SELECT * FROM foundFile")
    fun getAllFile(): LiveData<List<FileDetailedModel>>

    @Delete
    fun deleteFile(file: FileDetailedModel)

    @Update
    fun updateFile(file: FileDetailedModel): Int

    @Transaction
    fun insertOrUpdate(file: FileDetailedModel): Any? {
        val id = insertFile(file)
        return if (id == -1L) {
            updateFile(file)
            file.id
        } else {
            id
        }
    }
}