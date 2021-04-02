package com.example.breakingBad.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.user.UserProfile
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProfileViewModel : BaseViewModel() {

    init{
        getUserData()
    }

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile : LiveData<UserProfile> get() = _userProfile

    private val _loginRequired = MutableLiveData<Event<Unit>>()
    val loginRequired: LiveData<Event<Unit>> get() = _loginRequired

    fun getUserData(){
        viewModelScope.launch {
            showLoading()
            try {
                val response = withContext(Dispatchers.IO){
                    NetworkClient.userService.getUser()
                }
                _userProfile.postValue(response)
            }catch (e : Exception){
                handleNetworkError(e)
            }finally {
                hideLoading()
            }
        }
    }

    override fun onUnauthorized() {
        _loginRequired.postValue(Event(Unit))
    }
}