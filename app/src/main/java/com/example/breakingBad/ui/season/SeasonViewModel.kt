package com.example.breakingBad.ui.season

import androidx.lifecycle.*
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Episode
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.ui.cardDetails.CharacterDetailsViewModel
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class SeasonViewModel(private val seasonInf: String) : BaseViewModel() {

    private val _loginRequired = MutableLiveData<Event<Unit>>()
    val loginRequired: LiveData<Event<Unit>> get() = _loginRequired

    private val _episodes = MutableLiveData<List<Episode>>()
    val episodes: LiveData<List<Episode>> get() = _episodes

    init {
        getEpisodesByAppearance()
    }

    fun onRefresh() {
        _episodes.postValue(emptyList())
        getEpisodesByAppearance()
    }

    private fun getEpisodesByAppearance() = viewModelScope.launch {
        try {
            showLoading()
            val series = withContext(Dispatchers.IO) {
                NetworkClient.characterService.getEpisodesBySeries(
                    getSeriesName()
                )
            }
            _episodes.postValue(getEpisodesBySeason(series))
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }

    private fun getEpisodesBySeason(allEpisodes: MutableList<Episode>): List<Episode> {
        val listInfo = seasonInf.split("|")
        val seasonNumber = listInfo[1].toInt()
        if (listInfo.size == 2) {
            return allEpisodes.filter {
                it.season == seasonNumber
            }
        } else
            throw RuntimeException("There is no Season number info in argument")
    }

    private fun getSeriesName(): String {
        val listInfo = seasonInf.split("|")
        if (listInfo.size == 2) {
            val isBcsSeries = listInfo.first().toBoolean()
            return if (isBcsSeries)
                "Better+Call+Soul"
            else
                "Breaking+Bad"
        } else
            throw RuntimeException("Unknown Series")
    }

    override fun onUnauthorized() {
        _loginRequired.postValue(Event(Unit))
    }

    @Suppress("UNCHECKED_CAST")
    class SeasonsViewModelFactory(
        private val seasonInf: String
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SeasonViewModel(seasonInf) as T
        }
    }
}