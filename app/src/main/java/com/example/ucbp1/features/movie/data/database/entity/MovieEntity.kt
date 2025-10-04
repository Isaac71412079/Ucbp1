package com.example.ucbp1.features.movie.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_table")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String?,
    var isLiked: Boolean = false,
    // AÑADIDO: Campo para la calificación de 0 a 5.
    val userRating: Int = 0 // 0 = sin calificar, 1-5 = estrellas
)