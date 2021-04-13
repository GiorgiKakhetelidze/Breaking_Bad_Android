package com.example.breakingBad.ui.savedCharacters

import androidx.lifecycle.*

import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.repository.Repository
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SavedCharactersViewModel : BaseViewModel() {

    private val _requestLogin = MutableLiveData<Event<Unit>>()
    val requestLogin: LiveData<Event<Unit>> get() = _requestLogin


    val characters: LiveData<List<Character>> =
        Repository.getLocalSavedCharactersFlow()
            .map { list ->
                list.map { id ->
                    var character: Character?
                    character = Repository.getLocalCharacterById(id)
                    if (character == null) {
                        showLoading()
                        character = Repository.getRemoteCharacterById(id)
                        hideLoading()
                    }
                    character
                }
            }.catch { error -> handleNetworkError(error) }
            .flowOn(Dispatchers.IO)
            .asLiveData(viewModelScope.coroutineContext)

    init {
        getSavedCharacters()
    }

    fun onRefresh() = viewModelScope.launch(Dispatchers.IO) {
        try {
            showLoading()
            Repository.updateRemoteSavedCharacters()
        } catch (e: java.lang.Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
        getSavedCharacters()
    }

    fun getSavedCharacters() = viewModelScope.launch(Dispatchers.IO) {
        try {
            showLoading()
            if (!Repository.checkSavedIdsValidity()){
                Repository.updateRemoteSavedCharacters()
            }

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