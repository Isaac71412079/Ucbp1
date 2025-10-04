package com.example.ucbp1.features.movie.domain.repository

import com.example.ucbp1.features.movie.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getPopularMoviesStream(): Flow<List<MovieModel>>

    suspend fun refreshPopularMovies()

    suspend fun toggleMovieLike(movieId: Int)

    suspend fun rateMovie(movieId: Int, rating: Int)
}
