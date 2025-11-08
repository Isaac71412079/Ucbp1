package com.example.ucbp1.features.dollar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develoop.logs.LogApi
import com.example.ucbp1.features.dollar.domain.model.Dollar
import com.example.ucbp1.features.dollar.domain.repository.IDollarRepository
import com.example.ucbp1.features.logs.data.datasource.LogsRemoteDataSource
import com.google.firebase.messaging.FirebaseMessaging
import com.google.protobuf.ByteString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DollarViewModel(
    private val dollarRepository: IDollarRepository,
    private val logsRemoteDataSource: LogsRemoteDataSource
) : ViewModel() {

    sealed class DollarUIState {
        object Loading : DollarUIState()
        class Error(val message: String) : DollarUIState()
        class Success(val data: Dollar) : DollarUIState()
    }

    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Loading)
    val uiState: StateFlow<DollarUIState> = _uiState.asStateFlow()


    init {
        // --- 1. Lanzamos una corrutina para obtener los datos del dólar ---
        viewModelScope.launch {
            _uiState.value = DollarUIState.Loading
            try {
                // Como estamos en una corrutina, ahora SÍ podemos llamar a la función suspend
                dollarRepository.getDollarUpdates()
                    .catch { e ->
                        // Si el Flow falla, emitimos el estado de Error
                        _uiState.value = DollarUIState.Error(e.message ?: "Error desconocido")
                    }
                    .collect { dollarData ->
                        // Por cada nuevo dato del Flow, actualizamos el estado a Success
                        _uiState.value = DollarUIState.Success(dollarData)
                    }
            } catch (e: Exception) {
                _uiState.value = DollarUIState.Error(e.message ?: "Error desconocido")
            }
        }

        // --- 2. Lanzamos la corrutina para enviar el log (se ejecuta en paralelo) ---
        sendTestLog()

        // --- 3. Lanzamos la corrutina para obtener el token de Firebase ---
        viewModelScope.launch {
            getToken()
        }
    }

    // La función getDollar() se elimina porque ya no es necesaria.

    private fun sendTestLog() {
        viewModelScope.launch {
            Log.d("gRPC_Test", "Preparando para enviar log...")

            val logData = LogApi.LogData.newBuilder()
                .setAndroidId(ByteString.copyFromUtf8("abc123_android_id"))
                .setAppInstanceId(ByteString.copyFromUtf8("instance_001_app"))
                .setLogLevel(LogApi.ELogLevel.LEVEL_INFO)
                .setMessage("¡Prueba exitosa desde DollarViewModel!")
                .setStackTrace("Este es el stacktrace de la prueba.")
                .setMobileTimeStamp(System.currentTimeMillis())
                .setVersionCode(101)
                .setUserId("user_android_test")
                .build()

            val request = LogApi.LogRequest.newBuilder()
                .addLogs(logData)
                .build()

            try {
                // La llamada a la red se hace dentro de esta corrutina.
                val response = logsRemoteDataSource.send(request)
                Log.d("gRPC_Test", "Respuesta del servidor: ${response.resultCode}")
            } catch (e: Exception) {
                // Si algo falla, lo veremos aquí.
                Log.e("gRPC_Test", "Error al enviar log: ${e.message}", e)
            }
        }
    }

    suspend fun getToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FIREBASE", "Obtención del token FCM fallida", task.exception)
                    continuation.resumeWithException(task.exception ?: Exception("Error desconocido en FCM"))
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FIREBASE", "Token FCM: $token")
                continuation.resume(token ?: "")
            }
    }
}
