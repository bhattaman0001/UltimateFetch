package com.example.quickdroid.util

import android.content.Context
import android.widget.Toast
import com.example.quickdroid.data.db.database.HistoryDatabase
import com.example.quickdroid.data.db.database.SavedFileRoomDatabase
import com.example.quickdroid.model.FileDetailedModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Constants {
    const val BASE_URL_FP = "https://filepursuit.p.rapidapi.com/"
    // this is my second api
    /*https://quickdroid-backend.onrender.com/files/?q=internship&type=pdf*/
    const val BASE_URL_EB = "https://book-finder1.p.rapidapi.com/"
    const val message =
        "Thanks for using our application, below is the link of the file\n\n"


    /*
    https://medium.com/acmvit/getting-started-with-lottie-animations-android-2c225ad2c467
    */


    /*
    val downloadsFolder =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    val files: Array<File> = downloadsFolder.listFiles()!!
    */


    /*
        var storageManager = getSystemService(STORAGE_SERVICE) as StorageManager?
        var storageVolumeList = storageManager!!.storageVolumes
        var storageVolumeInternal = storageVolumeList[0]
        var fileListDownloadDir = File(
            storageVolumeInternal.directory!!.path + "/Download"
        )
        var fileList = fileListDownloadDir.listFiles()
    */


    /*private fun createApplicationFolder() {
        val folderPath =
            applicationContext.filesDir.absolutePath + File.separator + Constants.folderName
        val folder = File(folderPath)
        if (!folder.exists()) {
            val created = folder.mkdirs()
            if (created) {
                Constants.showFolderCreatedToast(this)
            } else {
                Constants.showFolderNotCreatedToast(this)
            }
        } else {
            Constants.checkIfFolderExists(this)
        }
    }*/


    /*fun saveFile(file: ResponseModel, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase.invoke(context).getFileDaa().insertFile(file)
    }

    fun insertOrUpdate(file: ResponseModel, context: Context) =
        CoroutineScope(Dispatchers.IO).launch {
            SavedFileRoomDatabase(context).getFileDaa().insertOrUpdate(file)
        }

    fun insertOrUpdateHistory(history: HistoryModel, context: Context) =
        CoroutineScope(Dispatchers.IO).launch {
            HistoryDatabase(context).getHistoryDao().insertOrUpdate(history)
        }

    fun updateFile(file: ResponseModel, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase.invoke(context).getFileDaa().updateFile(file)
    }*/

    fun deleteAllSavedFile(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase(context).clearAllTables()
    }

    fun deleteAllHistory(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        HistoryDatabase(context).clearAllTables()
    }

    fun deleteFile(context: Context, file: FileDetailedModel) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase(context).getFileDaa().deleteFile(file)
    }

    fun showInsertToast(context: Context) {
        Toast.makeText(context, "Item Saved", Toast.LENGTH_SHORT).show()
    }

    fun showDeleteToast(context: Context) {
        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
    }

    fun alreadyExistToast(context: Context) {
        Toast.makeText(context, "Item Already Saved", Toast.LENGTH_SHORT).show()
    }

    fun showComingSoonToast(context: Context) {
        Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
    }

    fun checkIfFolderExists(context: Context) {
        Toast.makeText(context, "folder already existed", Toast.LENGTH_SHORT).show()
    }

    fun showFolderCreatedToast(context: Context) {
        Toast.makeText(context, "folder created", Toast.LENGTH_SHORT).show()
    }

    fun showFolderNotCreatedToast(context: Context) {
        Toast.makeText(context, "folder not created", Toast.LENGTH_SHORT).show()
    }

}