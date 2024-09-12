package com.example.quickdroid.model

import androidx.room.*

@Entity(
    tableName = "History",
    primaryKeys = ["queryName", "queryType"]
)
data class HistoryModel(
    var queryName: String,
    var queryType: String,
)
