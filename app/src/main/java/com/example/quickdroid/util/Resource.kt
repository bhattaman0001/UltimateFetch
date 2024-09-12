package com.example.quickdroid.util

sealed class Resource<T>(
    val responseList: T? = null, val numberOfResponse: String? = null, val message: String? = null
) {
    class Success<T>(responseList: T, numberOfResponse: String) : Resource<T>(responseList, numberOfResponse)
    class Error<T>(message: String) : Resource<T>(null, null, message)
    class Loading<T> : Resource<T>()
}