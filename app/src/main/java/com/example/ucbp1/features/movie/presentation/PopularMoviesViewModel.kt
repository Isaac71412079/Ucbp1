package com.example.ucbp1.features.movie.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucbp1.features.movie.domain.model.MovieModel
import com.example.ucbp1.features.movie.domain.usecase.FetchPopularMoviesUseCase
import com.example.ucbp1.features.movie.domain.usecase.ToggleMovieLikeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import android.util.Log

// Estado de la UI para la pantalla de películas populares
data class PopularMoviesUiState(
    val movies: List<MovieModel> = emptyList(),
    val isLoading: Boolean = false, // Para la carga inicial de la pantalla o un refresco completo
    val error: String? = null,
    val isUserInitiatedRefresh: Boolean = false // Para el pull-to-refresh o botón de refresco
)

class PopularMoviesViewModel(
    private val fetchPopularMoviesUseCase: FetchPopularMoviesUseCase,
    private val toggleMovieLikeUseCase: ToggleMovieLikeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PopularMoviesUiState())
    val uiState: StateFlow<PopularMoviesUiState> = _uiState.asStateFlow()

    init {
        // Carga inicial:
        // 1. Empieza a observar el stream de la base de datos.
        // 2. Intenta refrescar los datos desde la red.
        observeMoviesFromLocalSource()
        refreshMoviesFromServer(isInitialLoad = true)
    }

    private fun observeMoviesFromLocalSource() {
        viewModelScope.launch {
            fetchPopularMoviesUseCase() // Este es el Flow desde la BD
                .onStart {
                    // Si es la primera vez que se colecta y no hay datos, mostrar loading
                    if (_uiState.value.movies.isEmpty()) {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
                .catch { e ->
                    Log.e("ViewModel", "Error collecting movies from local source: ${e.message}", e)
                    _uiState.value = _uiState.value.copy(
                        error = "Error al obtener películas: ${e.localizedMessage}",
                        isLoading = false,
                        isUserInitiatedRefresh = false
                    )
                }
                .collect { movies ->
                    _uiState.value = _uiState.value.copy(
                        movies = movies,
                        isLoading = false, // Termina la carga general cuando llegan datos de la BD
                        // isUserInitiatedRefresh se maneja en refreshMoviesFromServer
                        error = if (movies.isNotEmpty()) null else _uiState.value.error
                    )
                }
        }
    }

    fun refreshMoviesFromServer(isInitialLoad: Boolean = false) {
        viewModelScope.launch {
            if (isInitialLoad && _uiState.value.movies.isEmpty()) {
                // Ya se maneja el isLoading en onStart de observeMoviesFromLocalSource
            } else if (!isInitialLoad) { // Solo para refresco iniciado por el usuario
                _uiState.value = _uiState.value.copy(isUserInitiatedRefresh = true, error = null)
            }

            try {
                fetchPopularMoviesUseCase.refresh() // Llama al método refresh del caso de uso
                // El estado de 'isLoading' o 'isUserInitiatedRefresh' se reseteará
                // cuando el Flow de 'observeMoviesFromLocalSource' emita los nuevos datos.
                // Si el refresco es exitoso y no hay error, el colector se encarga.
            } catch (e: Exception) {
                Log.e("ViewModel", "Error refreshing movies from server: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    error = "Fallo al actualizar películas: ${e.localizedMessage}",
                    isLoading = false, // Asegurar que isLoading se quite si el refresco inicial falla
                    isUserInitiatedRefresh = false
                )
            } finally {
                // Asegurarse de que el indicador de refresco se oculte si no se actualiza por el colector.
                // Esto es un fallback, idealmente el colector lo hace.
                if (_uiState.value.isUserInitiatedRefresh && !isInitialLoad) {
                    _uiState.value = _uiState.value.copy(isUserInitiatedRefresh = false)
                }
            }
        }
    }

    fun onLikeClicked(movieId: Int) {
        viewModelScope.launch {
            try {
                toggleMovieLikeUseCase(movieId)
                // La UI se actualiza automáticamente gracias a la observación del Flow
                // que viene de la base de datos (a través de fetchPopularMoviesUseCase).
            } catch (e: Exception) {
                Log.e("ViewModel", "Error toggling like for movie ID $movieId: ${e.message}", e)
                // Opcional: Mostrar un mensaje de error temporal al usuario
                _uiState.value = _uiState.value.copy(error = "No se pudo actualizar el 'Me gusta'.")
            }
        }
    }
}

