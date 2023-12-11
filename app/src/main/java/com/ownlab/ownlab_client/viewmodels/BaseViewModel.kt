package com.ownlab.ownlab_client.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ownlab.ownlab_client.viewmodels.`interface`.CoroutinesErrorHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel: ViewModel() {
    private var job: Job? = null

    override fun onCleared() {
        super.onCleared()

        job?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }

    protected fun<T> baseRequest(liveData: MutableLiveData<T>, errorHandler: CoroutinesErrorHandler, request: () -> Flow<T>) {
        job = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            viewModelScope.launch(Dispatchers.Main) {
                errorHandler.onError(e.localizedMessage ?: "Error")
            }
        }) {
            request().collect {
                withContext(Dispatchers.Main) {
                    liveData.value = it
                }
            }
        }
    }
}