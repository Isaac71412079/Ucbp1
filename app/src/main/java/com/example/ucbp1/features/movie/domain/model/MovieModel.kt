package com.example.ucbp1.features.movie.domain.model

data class MovieModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?, // URL completa del póster
    val releaseDate: String?,
    val isLiked: Boolean
)