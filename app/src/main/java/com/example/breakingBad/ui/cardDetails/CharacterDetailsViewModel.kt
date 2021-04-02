package com.example.breakingBad.ui.cardDetails

import androidx.lifecycle.*
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Quote
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterDetailsViewModel(private val characters: Character) :
    BaseViewModel() {

    private val _characterModel = MutableLiveData(characters)
    val characterModel: LiveData<Character> get() = _characterModel

    private val _characterSaved = MutableLiveData<CharacterSavedState>(CharacterSavedState.Unknown)
    val characterSaved: LiveData<CharacterSavedState> get() = _characterSaved

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

    fun determineCardSavedState() = viewModelScope.launch {
        try {
            showLoading()
            val cardIds =
                withContext(Dispatchers.IO) { NetworkClient.userService.getUserCharacters() }
            val state =
                if (cardIds.contains(characters.charId)) CharacterSavedState.Saved else CharacterSavedState.NotSaved
            _characterSaved.postValue(state)
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }

    fun buttonClicked() {
        when (characterSaved.value) {
            CharacterSavedState.Saved ->  _showConfirmDialog.postValue(Event(Unit))
            CharacterSavedState.NotSaved -> saveCard()
            CharacterSavedState.Unknown -> _loginRequired.postValue(Event(Unit))
        }
    }

    private fun saveCard() = viewModelScope.launch(Dispatchers.IO) {
        try {
            showLoading()
            NetworkClient.userService.saveUserCharacter(characters.charId)
            _characterSaved.postValue(CharacterSavedState.Saved)
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
            NetworkClient.userService.deleteUserCharacter(characters.charId)
            _characterSaved.postValue(CharacterSavedState.NotSaved)
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }

    private fun getCharacterQuotes() {
        val nameSurname = characters.name.split(" ")

        val name = when (nameSurname.size) {
            1 -> nameSurname[0]
            3 -> nameSurname[0] + "+" + nameSurname[1] + "+" + nameSurname[2]
            4 -> nameSurname[0] + "+" + nameSurname[1] + "+" + nameSurname[2] + "+" + nameSurname[3]
            else -> nameSurname[0] + "+" + nameSurname[1]
        }

        viewModelScope.launch(Dispatchers.IO)
        {
            try {
                showLoading()
                val characterQuotes = NetworkClient.characterService.getQuotesByAuthor(name)
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