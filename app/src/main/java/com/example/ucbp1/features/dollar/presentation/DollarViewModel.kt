package com.example.ucbp1.features.dollar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucbp1.features.dollar.domain.usecase.GetDollarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.ucbp1.features.dollar.domain.model.Dollar
import com.example.ucbp1.features.dollar.data.repository.DollarRepository
import com.example.ucbp1.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class DollarViewModel(
    val getDollarUseCase: GetDollarUseCase
): ViewModel() {

    sealed class DollarUIState {
        object Loading : DollarUIState()
        class Error(val message: String) : DollarUIState()
        class Success(val data: Dollar) : DollarUIState()
    }


    init {
        getDollar()
    }


    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Loading)
    val uiState: StateFlow<DollarUIState> = _uiState.asStateFlow()


    fun getDollar() {
        viewModelScope.launch(Dispatchers.IO) {
            getDollarUseCase.invoke().collect { result ->
                result.onSuccess { data ->
                    _uiState.value = DollarUIState.Success(data)
                }.onFailure { e ->
                    _uiState.value = DollarUIState.Error(e.message ?: "Error desconocido")
                }
            }
        }
    }
}
