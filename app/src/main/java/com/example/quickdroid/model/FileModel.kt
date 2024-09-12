package com.example.quickdroid.model

data class FileModel(
    val files_found: MutableList<FileDetailedModel>,
    val status: String,
)
