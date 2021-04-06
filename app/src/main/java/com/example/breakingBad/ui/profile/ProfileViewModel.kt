package com.example.breakingBad.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.user.UserProfile
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.repository.Repository
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProfileViewModel : BaseViewModel() {

    val userProfile: LiveData<UserProfile> =
        DataStore.db.getUserProfileDao()
            .getUserProfile()
            .onEach {
                if (it == null) {
                    showLoading()
                    Repository.getRemoteAndSaveProfile()
                    hideLoading()
                }
            }.filter { it != null }
            .map { it!! }
            .catch {
                hideLoading()
                handleNetworkError(it)
            }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val _loginRequired = MutableLiveData<Event<Unit>>()
    val loginRequired: LiveData<Event<Unit>> get() = _loginRequired

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoading()
                Repository.getRemoteAndSaveProfile()
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
}