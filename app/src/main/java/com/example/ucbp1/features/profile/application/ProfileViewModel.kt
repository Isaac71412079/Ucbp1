package com.example.ucbp1.features.profile.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucbp1.features.profile.domain.model.ProfileModel
import com.example.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine
class ProfileViewModel(

    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadProfileData()

    }
    fun loadProfileData() {
        viewModelScope.launch {
            getProfileUseCase().collect { result ->
                result.onSuccess { profile ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        userName = profile.name.value,
                        userEmail = profile.email.value,
                        avatarUrl = profile.avatarUrl.value
                    )
                }.onFailure { e ->
                    _state.value = _state.value.copy(
                        error = e.message ?: "Error al cargar perfil",
                        isLoading = false
                    )
                }
            }
        }
    }

}

data class ProfileState(
    val userName: String = "",
    val userEmail: String = "",
    val avatarUrl: String? = null,
    val dollarValue: Float? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)