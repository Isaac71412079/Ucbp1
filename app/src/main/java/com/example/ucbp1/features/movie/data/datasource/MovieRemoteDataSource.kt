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

        val likedMoviesMap = mutableMapOf<Int, Boolean>()
        movieDao.getAllMovies().collect { movies ->
            movies.forEach { movie ->
                if (movieIds.contains(movie.id)) {
                    likedMoviesMap[movie.id] = movie.isLiked
                }
            }
        }

        return likedMoviesMap
        return emptyMap()
    }
    
    suspend fun updateRating(movieId: Int, rating: Int) {
        movieDao.updateRating(movieId, rating)
    }
}
