package com.example.breakingBad.ui.savedCharacters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedCharactersViewModel : BaseViewModel() {


    private val _requestLogin = MutableLiveData<Event<Unit>>()
    val requestLogin: LiveData<Event<Unit>> get() = _requestLogin

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    init {
        getSavedCharacters()
    }

    fun onRefresh() {
        _characters.postValue(emptyList())
        getSavedCharacters()
    }

    fun getSavedCharacters() = viewModelScope.launch {
        try {
            showLoading()
            val characterIds =
                withContext(Dispatchers.IO) { NetworkClient.userService.getUserCharacters() }
            val characters = withContext(Dispatchers.IO) {
                characterIds.map {
                    NetworkClient.characterService.getCharacterBydId(it).first()
                }
            }
            _characters.postValue(characters)
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