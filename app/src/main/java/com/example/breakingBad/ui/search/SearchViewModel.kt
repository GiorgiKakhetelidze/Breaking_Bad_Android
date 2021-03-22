package com.example.breakingBad.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.network.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    fun onSearchTextChange(name: CharSequence?) {
        if (name.isNullOrEmpty()) _characters.postValue(emptyList())
        if (name?.length ?: 0 < 3) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val characters = NetworkClient.characterService.getCharacterByName("$name")
                _characters.postValue(characters)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}