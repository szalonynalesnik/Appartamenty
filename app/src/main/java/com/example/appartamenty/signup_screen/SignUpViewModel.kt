package com.example.appartamenty.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appartamenty.data.AuthRepository
import com.example.appartamenty.login_screen.SignInState
import com.example.appartamenty.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    val _signUpState = Channel<SignInState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun loginUser(email: String, password:String) = viewModelScope.launch{
        repository.loginUser(email, password).collect{result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.send(SignInState(isSuccess = "Login successful"))
                }
                is Resource.Loading -> {
                    _signUpState.send(SignInState(isLoading = true))
                }
                is Resource.Error -> {
                    _signUpState.send(SignInState(isError = result.message))
                }
            }
        }
    }
}