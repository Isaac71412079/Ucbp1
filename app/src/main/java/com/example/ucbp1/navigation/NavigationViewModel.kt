package com.example.ucbp1.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NavigationViewModel : ViewModel() {

    // Un SharedFlow para emitir comandos de navegación de un solo uso
    private val _navigationCommand = MutableSharedFlow<NavigationCommand>()
    val navigationCommand = _navigationCommand.asSharedFlow()

    // Comandos de navegación que la UI puede recibir
    sealed class NavigationCommand {
        data class NavigateTo(val route: String, val options: NavigationOptions? = null) : NavigationCommand()
        object PopBackStack : NavigationCommand()
    }

    // Opciones para controlar el comportamiento de la navegación
    enum class NavigationOptions {
        CLEAR_BACK_STACK,
        REPLACE_HOME
    }

    // Función para ser llamada desde la UI para navegar a una ruta
    fun navigateTo(route: String, options: NavigationOptions? = null) {
        viewModelScope.launch {
            _navigationCommand.emit(NavigationCommand.NavigateTo(route, options))
        }
    }

    // Función para ser llamada desde la UI para volver atrás
    fun popBackStack() {
        viewModelScope.launch {
            _navigationCommand.emit(NavigationCommand.PopBackStack)
        }
    }
}
