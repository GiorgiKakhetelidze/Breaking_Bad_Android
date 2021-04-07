package com.example.breakingBad.ui.episodeDetails

import androidx.lifecycle.*
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Episode
import com.example.breakingBad.data.repository.Repository
import com.example.breakingBad.utils.getSplitName
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class EpisodeDetailsViewModel(private val episode: Episode) : BaseViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    init {
        getCharacters()
    }

    private fun getCharacters() {
        viewModelScope.launch {
            try {
                showLoading()
                val characters = episode.characters.map {
                    withContext(Dispatchers.IO) {
                        Repository.getRemoteCharacterByName(
                            getSplitName(
                                it
                            )
                        ).first()
                    }
                }
                _characters.postValue(characters)
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                hideLoading()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class EpisodeDetailsViewModelFactory(
        private val episode: Episode
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EpisodeDetailsViewModel(episode) as T
        }
    }
}