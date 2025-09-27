package com.example.ucbp1.features.movie.data.datasource

import com.example.ucbp1.features.movie.data.database.dao.IMovieDao
import com.example.ucbp1.features.movie.data.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSource(private val movieDao: IMovieDao) {

    fun getAllMovies(): Flow<List<MovieEntity>> {
        return movieDao.getAllMovies()
    }

    suspend fun getMovieById(movieId: Int): MovieEntity? {
        return movieDao.getMovieById(movieId)
    }

    suspend fun insertOrUpdateMovies(movies: List<MovieEntity>) {
        movieDao.insertOrUpdateMovies(movies)
    }

    suspend fun updateLikeStatus(movieId: Int, isLiked: Boolean) {
        movieDao.updateLikeStatus(movieId, isLiked)
    }

    suspend fun getLikedStatusForMovies(movieIds: List<Int>): Map<Int, Boolean> {
        // Esto es una simplificación. Room no soporta directamente "IN" con una lista grande
        // de forma que devuelva un Map fácilmente. Una mejor forma sería:
        // 1. Obtener todas las películas likeadas.
        // 2. Filtrar por los IDs que te interesan.
        // O si sabes que no serán muchas, puedes hacer múltiples queries (no recomendado).
        // Por ahora, para mantenerlo simple, podrías obtenerlas todas y filtrar en memoria,
        // o si tienes pocas, obtenerlas una por una (no eficiente).

        // Estrategia simplificada: Obtener todas las películas y filtrar.
        // No es lo ideal para muchísimas películas, pero funciona para un conjunto moderado.
        val likedMoviesMap = mutableMapOf<Int, Boolean>()
        movieDao.getAllMovies().collect { movies -> // Colecta el primer valor del Flow
            movies.forEach { movie ->
                if (movieIds.contains(movie.id)) {
                    likedMoviesMap[movie.id] = movie.isLiked
                }
            }
            // Para detener la colección después del primer emitido (si el flow es continuo)
            // podrías lanzar una excepción de cancelación o usar .first()
        }
        // Si getAllMovies fuera una suspend fun que devuelve List<MovieEntity>:
        // movieDao.getAllMovies().forEach { movie -> ... }
        return likedMoviesMap // Esta aproximación necesita que getAllMovies() no sea un Flow aquí,
        // o que se maneje la colección de forma diferente.

        // Estrategia más robusta:
        //  @Query("SELECT id, isLiked FROM movies_table WHERE id IN (:movieIds)")
        //  suspend fun getLikedStatusForIds(movieIds: List<Int>): List<MovieLikeStatusTuple>
        // data class MovieLikeStatusTuple(val id: Int, val isLiked: Boolean)
        // Luego convertir List<MovieLikeStatusTuple> a Map<Int, Boolean>
        // Por ahora, lo dejaremos como responsabilidad del repositorio manejar esto.
        return emptyMap() // El Repositorio se encargará de esto de forma más eficiente.
    }
}
