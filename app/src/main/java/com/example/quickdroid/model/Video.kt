package com.example.quickdroid.model

import android.graphics.Bitmap

data class Video(
    var title: String?,
    var path: String,
    var image: Bitmap?,
    var lastModified: String?,
    var size: String?,
)
