package com.example.ucbp1.features.movie.data.api.dto

import com.google.gson.annotations.SerializedName

data class MoviePageDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)