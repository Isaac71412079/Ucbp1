package com.example.ucbp1.features.movie.domain.repository

import com.example.ucbp1.features.movie.domain.model.MovieModel

interface IMoviesRepository {
    suspend fun fetchPopularMovies(): Result<List<MovieModel>>
}