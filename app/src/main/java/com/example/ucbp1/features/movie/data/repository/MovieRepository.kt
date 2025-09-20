package com.example.ucbp1.features.movie.data.repository

import com.example.ucbp1.features.movie.data.datasource.MovieRemoteDataSource
import com.example.ucbp1.features.movie.domain.model.MovieModel
import com.example.ucbp1.features.movie.domain.repository.IMoviesRepository

class MovieRepository(
    private val movieRemoteDataSource: MovieRemoteDataSource
): IMoviesRepository {
    override suspend fun fetchPopularMovies(): Result<List<MovieModel>>
            = movieRemoteDataSource.fetchPopularMovies()

}