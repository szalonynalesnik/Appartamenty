package com.example.appartamenty.login_screen

data class SignInState (
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)