package com.example.ucbp1.features.movie.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ucbp1.features.movie.data.database.dao.IMovieDao
import com.example.ucbp1.features.movie.data.database.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppRoomDataBase : RoomDatabase() {

    abstract fun movieDao(): IMovieDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `movies_table` ADD COLUMN `title` TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE `movies_table` ADD COLUMN `overview` TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE `movies_table` ADD COLUMN `posterPath` TEXT") // Este es nullable
                db.execSQL("ALTER TABLE `movies_table` ADD COLUMN `releaseDate` TEXT") // Este es nullable
                db.execSQL("ALTER TABLE `movies_table` ADD COLUMN `isLiked` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `movies_table` ADD COLUMN `userRating` INTEGER NOT NULL DEFAULT 0")
            }
        }
        @Volatile
        private var INSTANCE: AppRoomDataBase? = null

        fun getDatabase(context: Context): AppRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDataBase::class.java,
                    "movie_feature_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}