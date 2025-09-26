package com.example.ucbp1.features.movie.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucbp1.features.movie.domain.model.MovieModel
import com.example.ucbp1.features.movie.domain.usecase.FetchPopularMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopularMoviesViewModel(
    private val fetchPopularMovies: FetchPopularMoviesUseCase
): ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Success(val movies: List<MovieModel>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun fetchPopularMovies() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            val result = withContext(Dispatchers.IO) {
                fetchPopularMovies.invoke()
            }
            result.fold(
                onSuccess = { movies ->
                    _state.value = UiState.Success(movies)
                },
                onFailure = {
                    _state.value = UiState.Error("Error cargando películas")
                }
            )
        }
    }
}