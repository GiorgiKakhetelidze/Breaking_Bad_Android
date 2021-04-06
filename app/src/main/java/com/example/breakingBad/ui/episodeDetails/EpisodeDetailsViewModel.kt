package com.example.breakingBad.ui.episodeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Episode

class EpisodeDetailsViewModel(private val episode: Episode) : BaseViewModel() {






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