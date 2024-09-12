package com.example.quickdroid.api_s.fpApi

import com.example.quickdroid.*
import com.example.quickdroid.model.*
import retrofit2.*
import retrofit2.http.*

interface FileApiInterface {
    @GET("/")
    suspend fun getFileData(
        @Header("X-RapidAPI-Key") header1: String,
        @Header("X-RapidAPI-Host") header2: String,
        @Query("q") parameter1: String,
        @Query("type") parameter2: String,
    ): Response<FileModel>

    @GET("/files")
    suspend fun getFilesFromMyApi(
        @Query("type") type: String, @Query("q") query: String
    ): Response<OneFileModel>

    /******************************IMPORTANT****************************************************************/
    /*CoroutineScope(Dispatchers.IO).launch {
        *//*val response = ApiServiceBuilder.fileApiInterfaceFromMyApi.getFilesFromMyApi(tOFS, qN)
        response?.forEach {
            println(it)
        }*//*
        val client = OkHttpClient()
        val request = Request.Builder().url("https://quickdroid-backend.onrender.com/files/?type=$tOFS&q=$qN").build()
        val response = client.newCall(request).execute()
        println(response.body?.string())
    }*/
}