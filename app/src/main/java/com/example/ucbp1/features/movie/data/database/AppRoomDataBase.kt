package com.example.ucbp1.features.movie.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ucbp1.features.movie.data.database.dao.IMovieDao
import com.example.ucbp1.features.movie.data.database.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1, // Incrementa si cambias el esquema
    exportSchema = false // Recomendable poner a true para producción y versionar esquemas
)
abstract class AppRoomDataBase : RoomDatabase() {

    abstract fun movieDao(): IMovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDataBase? = null

        fun getDatabase(context: Context): AppRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDataBase::class.java,
                    "movie_feature_database" // Nombre específico para esta BD de feature
                )
                    // Para desarrollo, si cambias el esquema, esto borra y recrea la BD
                    // En producción, usa migraciones.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}