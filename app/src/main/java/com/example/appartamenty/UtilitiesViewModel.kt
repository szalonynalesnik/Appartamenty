package com.example.appartamenty

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appartamenty.data.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtilitiesViewModel @Inject constructor(
    private val repository: UtilitiesRepository,
    propertyId: String
) : ViewModel() {
    var loading = mutableStateOf(false)
    val data: MutableState<DataOrException<List<Utility>, Exception>> = mutableStateOf(
        DataOrException(
            listOf(),
            Exception("")
        )
    )

    init {
        getUtilities(propertyId)
    }

    private fun getUtilities(propertyId: String) {
        viewModelScope.launch {
            loading.value = true
            data.value = repository.getUtilitiesFromFirestore(propertyId)
            loading.value = false
        }
    }
}
