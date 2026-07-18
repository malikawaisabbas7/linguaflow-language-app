package com.linguaflow.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.linguaflow.app.data.database.dao.*
import com.linguaflow.app.data.database.entity.*
import com.linguaflow.app.utils.Constants

@Database(
    entities = [
        UserEntity::class,
        LanguageEntity::class,
        LessonEntity::class,
        VocabularyEntity::class,
        QuizResultEntity::class,
        AchievementEntity::class,
        ProgressEntity::class,
        StreakEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun lessonDao(): LessonDao
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun achievementDao(): AchievementDao
    abstract fun progressDao(): ProgressDao
    abstract fun streakDao(): StreakDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
