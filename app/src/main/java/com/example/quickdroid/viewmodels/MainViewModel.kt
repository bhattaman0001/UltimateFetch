package com.example.quickdroid.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quickdroid.FileApplication
import com.example.quickdroid.data.repository.Repository
import com.example.quickdroid.model.FileDetailedModel
import com.example.quickdroid.model.HistoryModel
import com.example.quickdroid.util.Resource
import kotlinx.coroutines.launch
import java.io.IOException

@Suppress("UNCHECKED_CAST", "LogNotTimber")
class MainViewModel(
    application: Application, private val repository: Repository
) : AndroidViewModel(application) {

    private val _responseLiveData: MutableLiveData<Resource<MutableList<FileDetailedModel>?>> = MutableLiveData()

    private var getHistoryAllData: MutableLiveData<Resource<LiveData<List<HistoryModel>>>> = MutableLiveData()

    private var getSavedAllFiles: MutableLiveData<Resource<LiveData<List<FileDetailedModel>>>> = MutableLiveData()

    fun getAllHistoryFile() = viewModelScope.launch {
        safeGetAllHistoryFile()
    }

    fun getAllSavedFile() = viewModelScope.launch {
        safeGetAllSavedFile()
    }

    private fun safeGetAllSavedFile() {
        getSavedAllFiles.postValue(Resource.Loading())
        val savedFiles = repository.getSavedFiles()
        getSavedAllFiles.postValue(handleSavedFiles(savedFiles))
    }

    private fun handleSavedFiles(savedFiles: LiveData<List<FileDetailedModel>>): Resource<LiveData<List<FileDetailedModel>>> {
        return Resource.Success(savedFiles, "")
    }

    private fun safeGetAllHistoryFile() {
        getHistoryAllData.postValue(Resource.Loading())
        val history = repository.historyGetAllHistory()
        getHistoryAllData.postValue(handleHistory(history))
    }

    private fun handleHistory(history: LiveData<List<HistoryModel>>): Resource<LiveData<List<HistoryModel>>> {
        return Resource.Success(history, "")
    }

    fun getRequestResponse(queryName: String, typeofFileSelected: String) = viewModelScope.launch {
        safeGetRequestResponse(queryName, typeofFileSelected)
    }

    private suspend fun safeGetRequestResponse(queryName: String, typeofFileSelected: String) {
        _responseLiveData.postValue(Resource.Loading())
        try {
            if (isOnline()) {
                repository.getFromExternalApi(queryName, typeofFileSelected) { responseList ->
                    _responseLiveData.postValue(handleResponse(responseList))
                }
            } else {
                _responseLiveData.postValue(Resource.Error("No Internet Connection Available"))
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> _responseLiveData.postValue(Resource.Error("Network Failure"))
                else -> _responseLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleResponse(responseList: MutableList<FileDetailedModel>?): Resource<MutableList<FileDetailedModel>?> {
        return if (responseList != null) {
            Resource.Success(responseList, responseList.size.toString())
        } else {
            Resource.Error("sorry, this file doesn't exists!!")
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getApplication<FileApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    companion object {
        class MainViewModelFactory(
            private val application: Application, private val repository: Repository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(application, repository) as T
            }
        }
    }

    val responseLiveData: LiveData<Resource<MutableList<FileDetailedModel>?>>
        get() = _responseLiveData

    val getAllHistoryFile: LiveData<Resource<LiveData<List<HistoryModel>>>>
        get() = getHistoryAllData

    val getAllSavedFile: LiveData<Resource<LiveData<List<FileDetailedModel>>>>
        get() = getSavedAllFiles

}