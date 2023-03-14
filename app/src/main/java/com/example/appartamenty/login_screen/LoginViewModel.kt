package com.example.appartamenty.login_screen

import androidx.compose.runtime.mutableStateOf

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

var uiState = mutableStateOf(LoginUiState())
    private set