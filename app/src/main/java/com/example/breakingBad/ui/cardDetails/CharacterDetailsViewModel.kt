package com.example.breakingBad.ui.cardDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.breakingBad.data.models.character.Character

class CharacterDetailsViewModel : ViewModel() {

    private val _characterModel = MutableLiveData<Character>()
    val characterModel: LiveData<Character> get() = _characterModel

    fun setCardData(data: Character) {
        _characterModel.postValue(data)
    }
}