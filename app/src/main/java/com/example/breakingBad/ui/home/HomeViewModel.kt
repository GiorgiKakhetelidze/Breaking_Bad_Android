package com.example.breakingBad.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.base.DialogData
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Quote
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : BaseViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    private val _loadingMore = MutableLiveData(false)
    val loadingMore: LiveData<Boolean> get() = _loadingMore

    private var noMoreCharacters = false
    private var offset = 0

    init {
        loadCharacters()
    }

    fun onScrollEndReached() {
        if (loadingMore.value == true || noMoreCharacters) return
        loadCharacters()
    }

    fun onRefresh() {
        offset = 0
        _characters.postValue(emptyList())
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            _loadingMore.value = true
            try {
                val data = withContext(Dispatchers.IO) {
                    NetworkClient.characterService.getLimitedCharacters(
                        LIMIT,
                        offset
                    )
                }
                DataStore.db.getCharacterDao().insert(data)
                offset += data.size
                _characters.postValue((_characters.value ?: emptyList()) + data)
                noMoreCharacters = data.size != LIMIT

            } catch (e: Exception) {
                showDialog(DialogData(title = R.string.common_error, message = e.message ?: ""))
            } finally {
                _loadingMore.value = false
            }
        }
    }

    companion object {
        const val LIMIT = 10
    }
}