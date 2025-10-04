package com.example.ucbp1.features.movie.domain.usecase

import com.example.ucbp1.features.movie.domain.model.MovieModel
import com.example.ucbp1.features.movie.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class FetchPopularMoviesUseCase(
    private val movieRepository: IMovieRepository
) {
    operator fun invoke(): Flow<List<MovieModel>> {
        return movieRepository.getPopularMoviesStream()
    }

    // Método explícito para refrescar, si se quiere separar la acción
    suspend fun refresh() {
        movieRepository.refreshPopularMovies()
    }
}

class ToggleMovieLikeUseCase(
    private val movieRepository: IMovieRepository
) {
    suspend operator fun invoke(movieId: Int) {
        movieRepository.toggleMovieLike(movieId)
    }
}