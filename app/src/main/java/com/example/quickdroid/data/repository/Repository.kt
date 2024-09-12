package com.example.quickdroid.data.repository

import android.annotation.SuppressLint
import androidx.room.RoomDatabase
import com.example.quickdroid.BuildConfig
import com.example.quickdroid.api_s.fpApi.ApiServiceBuilder
import com.example.quickdroid.data.db.database.HistoryDatabase
import com.example.quickdroid.data.db.database.SavedFileRoomDatabase
import com.example.quickdroid.model.FileDetailedModel
import com.example.quickdroid.model.HistoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Repository(
    val db: RoomDatabase
) {
    @SuppressLint("LogNotTimber")
    suspend fun getFromExternalApi(searchQuery: String, searchType: String, callback: (MutableList<FileDetailedModel>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response1Async = async {
                ApiServiceBuilder.fileApiInterfaceFromExtApi.getFileData(header1 = BuildConfig.header1FP, header2 = BuildConfig.header2FP, parameter1 = searchQuery, parameter2 = searchType)
            }
            val response2Async = async { ApiServiceBuilder.fileApiInterfaceFromMyApi.getFilesFromMyApi(searchType, searchQuery) }

            val response1 = response1Async.await()
            val response2 = response2Async.await()

            if (!response1.isSuccessful && !response2.isSuccessful) {
                callback(null)
            } else if (!response1.isSuccessful) {
                callback(response2.body()?.data)
            } else if (!response2.isSuccessful) {
                callback(response1.body()?.files_found)
            } else {
                response2.body()?.data?.let { response1.body()?.files_found?.addAll(it) }
                callback(response1.body()?.files_found)
            }
        }

    }

    fun historyGetAllHistory() = (db as HistoryDatabase).getHistoryDao().getAllHistory()

    fun historyInsertOrUpdate(history: HistoryModel) = (db as HistoryDatabase).getHistoryDao().insertOrUpdate(history)

    fun historyDelete(history: HistoryModel) = (db as HistoryDatabase).getHistoryDao().deleteHistory(history)

    fun insertOrUpdateSavedFiles(file: FileDetailedModel) = (db as SavedFileRoomDatabase).getFileDaa().insertOrUpdate(file)

    fun getSavedFiles() = (db as SavedFileRoomDatabase).getFileDaa().getAllFile()

    fun deleteSavedFile(file: FileDetailedModel) = (db as SavedFileRoomDatabase).getFileDaa().deleteFile(file)

}