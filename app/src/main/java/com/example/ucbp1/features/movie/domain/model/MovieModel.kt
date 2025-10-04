package com.example.ucbp1.features.movie.domain.model

data class MovieModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: String?,
    val isLiked: Boolean,
    val userRating: Int
)