package com.example.ucbp1.features.profile.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    // Variable de control para evitar recargas
    private var isDataLoaded = false

    fun loadProfileData() {
        // --- MEJORA: Evita recargar los datos si ya se cargaron ---
        if (isDataLoaded) return

        viewModelScope.launch {
            // Ponemos el estado de carga al inicio
            _state.value = _state.value.copy(isLoading = true)

            // Carga los datos del perfil del usuario
            getProfileUseCase().collect { result ->
                result.onSuccess { profile ->
                    _state.value = _state.value.copy(
                        userName = profile.name.value,
                        userEmail = profile.email.value,
                        avatarUrl = profile.avatarUrl.value
                    )
                    // Una vez que el perfil se carga, cargamos el valor del dólar
                    loadDollarValue()
                }.onFailure { e ->
                    _state.value = _state.value.copy(
                        error = e.message ?: "Error al cargar perfil",
                        isLoading = false
                    )
                }
            }
            isDataLoaded = true // Marcamos que los datos ya fueron cargados
        }
    }

    // --- NUEVA FUNCIÓN ---
    // Simula la obtención de un valor desde una API
    private fun loadDollarValue() {
        viewModelScope.launch {
            // Simulamos un pequeño retraso de red
            delay(500)
            // Asignamos un valor fijo y actualizamos el estado.
            // Cuando todo esté cargado, ponemos isLoading en false.
            _state.value = _state.value.copy(
                dollarValue = 6.96f, // Valor de ejemplo
                isLoading = false // Ahora sí, toda la carga ha terminado
            )
        }
    }
}

data class ProfileState(
    val userName: String = "",
    val userEmail: String = "",
    val avatarUrl: String? = null,
    val dollarValue: Float? = null,
    val isLoading: Boolean = true, // Mantenemos isLoading en true por defecto
    val error: String? = null
)
