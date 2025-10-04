package com.example.ucbp1.features.movie.data.repository

import android.util.Log
import com.example.ucbp1.features.movie.data.api.MovieService
import com.example.ucbp1.features.movie.data.datasource.MovieLocalDataSource
import com.example.ucbp1.features.movie.data.mapper.toDomainModelList
import com.example.ucbp1.features.movie.data.mapper.toEntityList
import com.example.ucbp1.features.movie.domain.model.MovieModel
import com.example.ucbp1.features.movie.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val movieApiService: MovieService,
    private val localDataSource: MovieLocalDataSource,
    private val apiKey: String
) : IMovieRepository {

    override fun getPopularMoviesStream(): Flow<List<MovieModel>> {

        return localDataSource.getAllMovies().map { entities ->
            entities.toDomainModelList()
        }
    }

    override suspend fun refreshPopularMovies() {
        try {
            val remoteMoviePage = movieApiService.getPopularMovies(apiKey = apiKey)
            val remoteMoviesDto = remoteMoviePage.results

            val currentMoviesInDb = localDataSource.getAllMovies().first()
            val existingLikesMap = currentMoviesInDb.associate { it.id to it.isLiked }

            val movieEntities = remoteMoviesDto.toEntityList(existingLikesMap)
            localDataSource.insertOrUpdateMovies(movieEntities)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error refreshing popular movies: ${e.message}", e)
            throw e
        }
    }

    override suspend fun toggleMovieLike(movieId: Int) {
        val movieEntity = localDataSource.getMovieById(movieId)
        movieEntity?.let {
            val newLikedStatus = !it.isLiked
            localDataSource.updateLikeStatus(movieId, newLikedStatus)
        } ?: run {
            Log.w("MovieRepository", "Movie with ID $movieId not found for toggling like.")
        }
    }

    override suspend fun rateMovie(movieId: Int, rating: Int) {
        localDataSource.updateRating(movieId, rating)
    }
}
