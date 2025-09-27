package com.example.ucbp1.features.movie.domain.repository

import com.example.ucbp1.features.movie.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    /**
     * Obtiene un Flow de la lista de películas populares desde la fuente local,
     * ordenadas con las "likeadas" primero.
     */
    fun getPopularMoviesStream(): Flow<List<MovieModel>>

    /**
     * Refresca las películas populares desde la red y actualiza la fuente local.
     * Preserva el estado de "like" de las películas existentes.
     */
    suspend fun refreshPopularMovies()

    /**
     * Cambia el estado de "like" para una película específica en la fuente local.
     */
    suspend fun toggleMovieLike(movieId: Int)
}
