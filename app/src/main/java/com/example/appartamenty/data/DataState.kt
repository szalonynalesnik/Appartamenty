package com.example.appartamenty.data

sealed class DataState {
    class Success(val data: MutableList<Property>) : DataState()
    class Failure(val message: String) : DataState()
    object Loading : DataState()
    object Empty : DataState()
}