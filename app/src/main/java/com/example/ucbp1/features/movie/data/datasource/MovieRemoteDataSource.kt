package com.example.ucbp1.features.movie.data.datasource

import com.example.ucbp1.features.movie.data.api.MovieService
import com.example.ucbp1.features.movie.domain.model.MovieModel

class MovieRemoteDataSource(
    private val movieService: MovieService,
    private val apiKey: String
) {
    suspend fun fetchPopularMovies(): Result<List<MovieModel>> {
        val response = movieService.fetchPopularMovies(apiKey = apiKey)
        return if (response.isSuccessful) {
            val moviePage = response.body()
            if (moviePage != null) {
                return Result.success(moviePage.results.map { dto ->  MovieModel("https://image.tmdb.org/t/p/w185"+dto.pathUrl, dto.title) } )
            }
            Result.success(emptyList())
        } else {
            Result.failure(Exception("Error para peliculas"))
        }
    }
}