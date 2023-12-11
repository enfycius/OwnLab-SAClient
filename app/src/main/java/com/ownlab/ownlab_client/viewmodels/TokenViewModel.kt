package com.ownlab.ownlab_client.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ownlab.ownlab_client.utils.Manager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor (private val tokenManager: Manager): ViewModel() {
    private var _token = MutableLiveData<String?>()
    val token = _token

    init {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.get().collect {
                withContext(Dispatchers.Main) {
                    token.value = it
                }
            }
        }
    }

    fun save(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.save(token)
        }
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.delete()
        }
    }

}