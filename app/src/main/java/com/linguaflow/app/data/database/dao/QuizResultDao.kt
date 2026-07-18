package com.linguaflow.app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.linguaflow.app.data.database.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results ORDER BY takenAt DESC")
    fun observeAll(): Flow<List<QuizResultEntity>>

    @Insert
    suspend fun insert(result: QuizResultEntity): Long

    @Query("SELECT AVG(accuracyPercent) FROM quiz_results")
    suspend fun averageAccuracy(): Double?

    @Query("SELECT COUNT(*) FROM quiz_results")
    suspend fun totalQuizzesTaken(): Int
}
