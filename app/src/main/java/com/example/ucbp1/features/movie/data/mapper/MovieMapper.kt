package com.example.ucbp1.features.movie.data.mapper

import com.example.ucbp1.features.movie.data.api.dto.MovieDto
import com.example.ucbp1.features.movie.data.database.entity.MovieEntity
import com.example.ucbp1.features.movie.domain.model.MovieModel

const val TMDB_IMAGE_BASE_URL_W185 = "https://image.tmdb.org/t/p/w185"
const val TMDB_IMAGE_BASE_URL_W500 = "https://image.tmdb.org/t/p/w500"

fun MovieDto.toEntity(isLikedInitially: Boolean = false): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        isLiked = isLikedInitially
    )
}

fun List<MovieDto>.toEntityList(existingLikesMap: Map<Int, Boolean> = emptyMap()): List<MovieEntity> {
    return this.map { dto ->
        MovieEntity(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            posterPath = dto.posterPath,
            releaseDate = dto.releaseDate,
            isLiked = existingLikesMap[dto.id] ?: false
        )
    }
}

fun MovieEntity.toDomainModel(): MovieModel {
    return MovieModel(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = if (this.posterPath != null) "$TMDB_IMAGE_BASE_URL_W500${this.posterPath}" else null,
        releaseDate = this.releaseDate,
        isLiked = this.isLiked
    )
}

fun List<MovieEntity>.toDomainModelList(): List<MovieModel> {
    return this.map { it.toDomainModel() }
}
