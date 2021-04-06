package com.example.breakingBad.ui.login

import androidx.lifecycle.*
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.repository.Repository
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : BaseViewModel() {
    private val _inputError = MutableLiveData<Int>()
    val inputError: LiveData<Int> get() = _inputError

    private val _loginSuccess = MutableLiveData<Event<Unit>>()
    val loginSuccess: LiveData<Event<Unit>> get() = _loginSuccess

    private val _loginFlowFinished = MutableLiveData<Event<Boolean>>()
    val loginFlowFinished: LiveData<Event<Boolean>> get() = _loginFlowFinished

    fun login(username: CharSequence?, password: CharSequence?) = viewModelScope.launch {
        withContext(Dispatchers.IO) { clearData() }
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            _inputError.postValue(R.string.login_toast)
            return@launch
        }
        showLoading()
        try {
            val response = withContext(Dispatchers.IO) {
                NetworkClient.userService.login(
                    username = username.toString(),
                    password = password.toString()
                )
            }
            DataStore.authToken = response.accessToken
            _loginSuccess.postValue(Event(Unit))
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }

    private suspend fun clearData() {
        DataStore.authToken = null
        Repository.clearSavedCards()
        Repository.invalidateSavedIds()
        Repository.clearProfile()
    }

    fun logOut() = viewModelScope.launch(Dispatchers.IO) {
        clearData()
    }

    internal fun loginFragmentDestroyed() {
        _loginFlowFinished.postValue(Event(!DataStore.authToken.isNullOrBlank()))
    }

    internal fun loginFragmentStarted() {
        DataStore.authToken = null
    }

    override fun onUnauthorized() {
        _inputError.postValue(R.string.login_screen_wrong_credentials)
    }
}