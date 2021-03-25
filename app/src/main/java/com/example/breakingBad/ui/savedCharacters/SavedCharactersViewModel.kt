package com.example.breakingBad.ui.savedCharacters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.launch

class SavedCharactersViewModel : BaseViewModel() {


    private val _requestLogin = MutableLiveData<Event<Unit>>()
    val requestLogin: LiveData<Event<Unit>> get() = _requestLogin

    init {
        getSavedCards()
    }

    fun getSavedCards() = viewModelScope.launch {
        try {
            showLoading()
            val characters = NetworkClient.userService.getUserCharacters()
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }

    override fun onUnauthorized() {
        _requestLogin.postValue(Event(Unit))
    }
}