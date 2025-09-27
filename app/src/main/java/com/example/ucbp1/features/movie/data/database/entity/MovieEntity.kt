package com.example.ucbp1.features.movie.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_table") // Un nombre de tabla descriptivo
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String?,
    var isLiked: Boolean = false // Para el estado de "like"
)