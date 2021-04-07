package com.example.breakingBad.ui.characterDetails

import androidx.lifecycle.*
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Quote
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.repository.Repository
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.breakingBad.utils.getSplitName

class CharacterDetailsViewModel(private val characters: Character) :
    BaseViewModel() {

    private val _characterModel = MutableLiveData(characters)
    val characterModel: LiveData<Character> get() = _characterModel

    val characterSaved = Repository.getLocalSavedCharactersFlow()
        .map { characterIds ->
            if (characterIds.contains(characters.charId))
                CharacterSavedState.Saved
            else CharacterSavedState.NotSaved
        }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val _loginRequired = MutableLiveData<Event<Unit>>()
    val loginRequired: LiveData<Event<Unit>> get() = _loginRequired

    private val _quotes = MutableLiveData<MutableList<Quote>>()
    val quotes: LiveData<MutableList<Quote>> get() = _quotes

    private val _showConfirmDialog = MutableLiveData<Event<Unit>>()
    val showConfirmDialog: LiveData<Event<Unit>> get() = _showConfirmDialog

    init {
        determineCardSavedState()
        getCharacterQuotes()
    }

    fun determineCardSavedState() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (!Repository.checkSavedIdsValidity())
                showLoading()
            Repository.updateRemoteSavedCharacters()
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }

    fun buttonClicked() {
        when (characterSaved.value) {
            CharacterSavedState.Saved -> _showConfirmDialog.postValue(Event(Unit))
            CharacterSavedState.NotSaved -> saveCard()
            CharacterSavedState.Unknown -> _loginRequired.postValue(Event(Unit))
        }
    }

    private fun saveCard() = viewModelScope.launch(Dispatchers.IO) {
        try {
            showLoading()
            Repository.saveCard(characters)
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }

    fun deleteConfirmed() {
        deleteCard()
    }

    private fun deleteCard() = viewModelScope.launch(Dispatchers.IO) {
        try {
            showLoading()
            Repository.deleteCard(characters)
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }

    private fun getCharacterQuotes() {
        viewModelScope.launch(Dispatchers.IO)
        {
            try {
                showLoading()
                val characterQuotes =
                    NetworkClient.characterService.getQuotesByAuthor(getSplitName(characters.name))
                _quotes.postValue(characterQuotes)
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                hideLoading()
            }
        }
    }

    override fun onUnauthorized() {
        _loginRequired.postValue(Event(Unit))
    }

    enum class CharacterSavedState {
        Unknown, Saved, NotSaved
    }

    @Suppress("UNCHECKED_CAST")
    class CharacterDetailsViewModelFactory(
        private val characters: Character
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterDetailsViewModel(characters) as T
        }
    }
}