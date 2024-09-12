package com.example.quickdroid

import android.app.Application
import com.example.quickdroid.data.db.database.HistoryDatabase
import com.example.quickdroid.data.db.database.SavedFileRoomDatabase
import com.example.quickdroid.data.repository.Repository

class FileApplication : Application() {
    lateinit var hRepo: Repository
    lateinit var sRepo: Repository
    private var isVisible: Boolean = false

    override fun onCreate() {
        super.onCreate()
        initialize()
//        setButtonVisibility()
    }

    private fun initialize() {
        hRepo = Repository(HistoryDatabase(this))
        sRepo = Repository(SavedFileRoomDatabase(this))
    }

}