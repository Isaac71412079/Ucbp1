package com.example.ucbp1.features.movie.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ucbp1.features.movie.data.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMovie(movie: MovieEntity)
    /*
    @Query("SELECT * FROM movies_table ORDER BY isLiked DESC, title ASC")
    fun getAllMovies(): Flow<List<MovieEntity>>
    */
    // MODIFICADO: La query principal ahora ordena por calificación.
    // Las películas con 5 estrellas aparecerán primero.
    @Query("SELECT * FROM movies_table ORDER BY userRating DESC, title ASC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies_table WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Query("UPDATE movies_table SET isLiked = :isLiked WHERE id = :movieId")
    suspend fun updateLikeStatus(movieId: Int, isLiked: Boolean)

    // AÑADIDO: Nueva función para actualizar la calificación.
    @Query("UPDATE movies_table SET userRating = :rating WHERE id = :movieId")
    suspend fun updateRating(movieId: Int, rating: Int)

    @Query("DELETE FROM movies_table")
    suspend fun deleteAllMovies()
}
