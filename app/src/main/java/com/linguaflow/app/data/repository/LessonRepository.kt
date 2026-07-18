package com.linguaflow.app.data.repository

import com.linguaflow.app.data.database.dao.LessonDao
import com.linguaflow.app.data.database.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

class LessonRepository(private val lessonDao: LessonDao) {

    // This is what HomeViewModel uses.
    // It is reactive: if the DB changes, the UI updates.
    fun observeLessons(languageCode: String): Flow<List<LessonEntity>> =
        lessonDao.observeLessons(languageCode)

    fun observeByCategory(languageCode: String, category: String): Flow<List<LessonEntity>> =
        lessonDao.observeLessonsByCategory(languageCode, category)

    suspend fun getLesson(id: Long): LessonEntity? = lessonDao.getLesson(id)

    // Added: Useful for checking if lessons exist for a specific language
    suspend fun getLessonsByLanguage(languageCode: String): List<LessonEntity> =
        lessonDao.getLessonsByLanguage(languageCode)

    suspend fun seedLessons(lessons: List<LessonEntity>) = lessonDao.insertAll(lessons)

    suspend fun completeLesson(id: Long) {
        lessonDao.markCompleted(id)
    }

    suspend fun unlockNext(id: Long) = lessonDao.unlock(id)
}