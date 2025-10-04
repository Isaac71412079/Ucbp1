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

data class PopularMoviesUiState(
    val movies: List<MovieModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUserInitiatedRefresh: Boolean = false
)

class PopularMoviesViewModel(
    private val fetchPopularMoviesUseCase: FetchPopularMoviesUseCase,
    private val toggleMovieLikeUseCase: ToggleMovieLikeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PopularMoviesUiState())
    val uiState: StateFlow<PopularMoviesUiState> = _uiState.asStateFlow()

    init {
        observeMoviesFromLocalSource()
        refreshMoviesFromServer(isInitialLoad = true)
    }

    private fun observeMoviesFromLocalSource() {
        viewModelScope.launch {
            fetchPopularMoviesUseCase() // Este es el Flow desde la BD
                .onStart {
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

            } else if (!isInitialLoad) {
                _uiState.value = _uiState.value.copy(isUserInitiatedRefresh = true, error = null)
            }

            try {
                fetchPopularMoviesUseCase.refresh() // Llama al método refresh del caso de uso

            } catch (e: Exception) {
                Log.e("ViewModel", "Error refreshing movies from server: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    error = "Fallo al actualizar películas: ${e.localizedMessage}",
                    isLoading = false, // Asegurar que isLoading se quite si el refresco inicial falla
                    isUserInitiatedRefresh = false
                )
            } finally {

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

            } catch (e: Exception) {
                Log.e("ViewModel", "Error toggling like for movie ID $movieId: ${e.message}", e)

                _uiState.value = _uiState.value.copy(error = "No se pudo actualizar el 'Me gusta'.")
            }
        }
    }
}

