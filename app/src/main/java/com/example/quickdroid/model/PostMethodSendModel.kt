package com.example.quickdroid.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

data class PostMethodSendModel(
    @SerializedName("file_type") val fileType: String,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("file_link") val fileLink: String,
    @SerializedName("date_added") val dateAdded: String,
    @SerializedName("time_ago") val timeAgo: String,
    @SerializedName("file_size") val fileSize: String
)

