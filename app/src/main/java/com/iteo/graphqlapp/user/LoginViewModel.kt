package com.iteo.graphqlapp.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iteo.graphqlapp.JobRepository
import com.iteo.graphqlapp.UserRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val mutableViewState = MutableLiveData<LoginViewState>()
    val viewState: LiveData<LoginViewState> = mutableViewState

    fun login(name: String, email: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.register(name, email)
                mutableViewState.postValue(LoggedIn(response.data?.subscribe?.id ?: ""))
            } catch (e: Exception) {
                mutableViewState.postValue(Error(e.message ?: "Unknown error"))
            }
        }
    }
}

sealed class LoginViewState
data class LoggedIn(val id: String) : LoginViewState()
data class Error(val error: String) : LoginViewState()