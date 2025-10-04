package com.example.ucbp1.features.movie.domain.usecase

import com.example.ucbp1.features.movie.domain.repository.IMovieRepository

class RateMovieUseCase(private val movieRepository: IMovieRepository) {
    suspend operator fun invoke(movieId: Int, rating: Int) {
        movieRepository.rateMovie(movieId, rating)
    }
}
