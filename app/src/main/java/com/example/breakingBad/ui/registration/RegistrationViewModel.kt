package com.example.breakingBad.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.user.UserRegistrationRequest
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.data.repository.Repository
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.utils.Event
import com.example.breakingBad.utils.handleNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class RegistrationViewModel : BaseViewModel() {

    private val _registrationComplete = MutableLiveData<Event<Unit>>()
    val registrationComplete: LiveData<Event<Unit>> get() = _registrationComplete

    private val _validationError = MutableLiveData<ValidationError>()
    val validationError: LiveData<ValidationError> get() = _validationError

    fun onRegister(
        username: CharSequence?,
        name: CharSequence?,
        password: CharSequence?,
        repeatPassword: CharSequence?
    ) = viewModelScope.launch {
        val error = when {
            username.isNullOrEmpty() -> ValidationError.EmptyUsername
            name.isNullOrEmpty() -> ValidationError.EmptyName
            password.isNullOrEmpty() -> ValidationError.EmptyPassword
            password.toString() != repeatPassword.toString() -> ValidationError.PasswordsNotMatching
            else -> ValidationError.None
        }
        _validationError.postValue(error)
        if (error != ValidationError.None) return@launch
        showLoading()
        try {
            withContext(Dispatchers.IO) {
                Repository.registerAndLogin(
                    name = name.toString(),
                    userName = username.toString(),
                    password = password.toString()
                )
                _registrationComplete.postValue(Event(Unit))
            }
            _registrationComplete.postValue(Event(Unit))
        } catch (e: Exception) {
            handleNetworkError(e)
        } finally {
            hideLoading()
        }
    }


    enum class ValidationError {
        EmptyUsername, EmptyName, EmptyPassword, PasswordsNotMatching, None
    }
}