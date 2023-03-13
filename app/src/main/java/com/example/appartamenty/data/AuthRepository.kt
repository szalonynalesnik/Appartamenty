package com.example.appartamenty.data

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import com.example.appartamenty.util.Resource

interface AuthRepository {
    fun loginUser(email: String, password:String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
}