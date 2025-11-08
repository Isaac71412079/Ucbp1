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
import com.example.ucbp1.features.dollar.data.repository.DollarRepository
import com.example.ucbp1.navigation.Screen
import kotlinx.coroutines.flow.Flow
class DollarViewModel(
    val getDollarUseCase: GetDollarUseCase
): ViewModel() {

    // La clase Success ahora debe aceptar un Dollar que puede ser nulo (Dollar?).
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
            getToken()
            getDollarUseCase.invoke()
                .collect { data -> _uiState.value = DollarUIState.Success(data) }
        }
    }

    suspend fun getToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FIREBASE", "getInstanceId failed", task.exception)
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error"))
                    return@addOnCompleteListener
                }
                // Si la tarea fue exitosa, se obtiene el token
                val token = task.result
                Log.d("FIREBASE", "FCM Token: $token")


                // Reanudar la ejecución con el token
                continuation.resume(token ?: "")
            }
    }
}