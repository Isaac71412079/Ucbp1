package com.example.ucbp1.features.dollar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucbp1.features.dollar.domain.model.Dollar
import com.example.ucbp1.features.dollar.domain.repository.IDollarRepository // ¡Importante!
import com.example.ucbp1.features.dollar.domain.usecase.GetDollarUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DollarViewModel(
    private val getDollarUseCase: GetDollarUseCase,
    private val dollarRepository: IDollarRepository // <-- **Inyectar el Repositorio**
): ViewModel() {

    // La clase Success ahora debe aceptar un Dollar que puede ser nulo (Dollar?).
    sealed class DollarUIState {
        object Loading : DollarUIState()
        class Error(val message: String) : DollarUIState()
        class Success(val data: Dollar?) : DollarUIState()
    }

    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Loading)
    val uiState: StateFlow<DollarUIState> = _uiState.asStateFlow()

    init {
        // 1. Inicia la recolección de datos para la UI. Esto es rápido.
        observeLocalData()
        // 2. Inicia la sincronización con Firebase en segundo plano.
        startFirebaseSync()
    }

    private fun observeLocalData() {
        viewModelScope.launch(Dispatchers.IO) {
            getDollarUseCase.invoke().collect { dollarData ->
                _uiState.value = DollarUIState.Success(dollarData)
            }
        }
    }

    private fun startFirebaseSync() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Esta función se quedará corriendo en segundo plano,
                // escuchando a Firebase y actualizando la BD.
                dollarRepository.syncFirebaseToLocal()
            } catch (e: Exception) {
                Log.e("DollarViewModel", "Firebase sync failed", e)
            }
        }
    }

    // La función getDollar() ya no es necesaria. El init() hace todo.
    // La función getToken() puede quedarse si la usas para otra cosa.
}
