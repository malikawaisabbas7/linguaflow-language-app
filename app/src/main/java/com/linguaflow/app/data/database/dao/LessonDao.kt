package com.linguaflow.app.data.database.dao

import androidx.room.*
import com.linguaflow.app.data.database.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons WHERE languageCode = :languageCode ORDER BY orderIndex ASC")
    fun observeLessons(languageCode: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE languageCode = :languageCode AND category = :category ORDER BY orderIndex ASC")
    fun observeLessonsByCategory(languageCode: String, category: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :id LIMIT 1")
    suspend fun getLesson(id: Long): LessonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lessons: List<LessonEntity>)

    @Update
    suspend fun update(lesson: LessonEntity)

    @Query("UPDATE lessons SET isCompleted = 1, progressPercent = 100 WHERE id = :id")
    suspend fun markCompleted(id: Long)

    @Query("UPDATE lessons SET isLocked = 0 WHERE id = :id")
    suspend fun unlock(id: Long)

    @Query("SELECT * FROM lessons WHERE languageCode = :languageCode")
    suspend fun getLessonsByLanguage(languageCode: String): List<LessonEntity>
}
