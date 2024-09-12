package com.example.quickdroid.model

import androidx.room.*

@Entity(
    tableName = "foundFile",
    indices = [Index(value = ["file_link", "file_name"], unique = true)]
)
data class FileDetailedModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "file_link")
    var file_link: String?,
    var file_id: String?,
    var file_type: String?,
    @ColumnInfo(name = "file_name")
    var file_name: String?,
    var date_added: String?,
    var time_ago: String?,
    var file_size: String?,
    var file_size_bytes: String?,
    var referrer_link: String?,
    var referrer_host: String?,
    var readable_path: String?,
)
