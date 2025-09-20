package com.example.ucbp1.features.movie.data.api.dto

data class MoviePageDto(
    val page: Int,
    val results: List<MovieDto>
)