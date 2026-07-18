package com.linguaflow.app.di

import android.content.Context
import com.linguaflow.app.data.database.AppDatabase
import com.linguaflow.app.data.firebase.FirebaseAuthManager
import com.linguaflow.app.data.firebase.FirestoreSyncManager
import com.linguaflow.app.data.repository.*
import com.linguaflow.app.utils.PreferencesManager

/**
 * Lightweight manual dependency container (no Hilt/Koin required).
 * Instantiated once per process via [get] and reused by ViewModel factories.
 */
class AppContainer(context: Context) {

    private val database = AppDatabase.getInstance(context)
    private val authManager = FirebaseAuthManager()
    private val syncManager = FirestoreSyncManager()

    val preferencesManager = PreferencesManager(context)

    val authRepository = AuthRepository(authManager, syncManager, database.userDao())
    val lessonRepository = LessonRepository(database.lessonDao())
    val vocabularyRepository = VocabularyRepository(database.vocabularyDao())
    val quizRepository = QuizRepository(database.quizResultDao())
    val progressRepository = ProgressRepository(database.progressDao(), database.streakDao(), database.achievementDao())

    companion object {
        @Volatile private var INSTANCE: AppContainer? = null

        fun get(context: Context): AppContainer =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppContainer(context.applicationContext).also { INSTANCE = it }
            }
    }
}
