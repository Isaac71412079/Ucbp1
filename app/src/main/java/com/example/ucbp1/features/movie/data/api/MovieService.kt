package com.example.ucbp1.features.movie.data.api

import com.example.ucbp1.features.movie.data.api.dto.MoviePageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES", // Cambiado a español como ejemplo
        @Query("page") page: Int = 1
    ): MoviePageDto
}
