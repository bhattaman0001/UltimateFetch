package com.example.quickdroid.api_s.fpApi

import com.example.quickdroid.model.PostMethodSendModel
import com.example.quickdroid.util.Constants
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceBuilder {
    fun getResponse(file: PostMethodSendModel): Response {
        val client = OkHttpClient()
        val mediaType = "application/x-www-form-urlencoded".toMediaType()
        val body = "file_type=${file.fileType}&file_name=${file.fileName}&file_link=${file.fileLink}&date_added=${file.dateAdded}&time_ago=9&file_size=${file.fileSize}".toRequestBody(
            mediaType
        )
        val request = Request.Builder().url("https://quickdroid-backend.onrender.com/files/addFile").post(body).addHeader("Content-Type", "application/x-www-form-urlencoded").build()
        return client.newCall(request).execute()
    }

    private val retrofitFromExternalApi by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        Retrofit.Builder().baseUrl(Constants.BASE_URL_FP).addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }

    val fileApiInterfaceFromExtApi: FileApiInterface by lazy {
        // this is service
        retrofitFromExternalApi.create(FileApiInterface::class.java)
    }

    private val retrofitFromMyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://quickdroid-backend.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val fileApiInterfaceFromMyApi: FileApiInterface by lazy {
        // this is service
        retrofitFromMyApi.create(FileApiInterface::class.java)
    }


}