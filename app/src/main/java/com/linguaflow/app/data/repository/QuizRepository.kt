package com.linguaflow.app.data.repository

import com.linguaflow.app.data.database.dao.QuizResultDao
import com.linguaflow.app.data.database.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

class QuizRepository(private val quizResultDao: QuizResultDao) {

    fun observeResults(): Flow<List<QuizResultEntity>> = quizResultDao.observeAll()

    suspend fun saveResult(result: QuizResultEntity): Long = quizResultDao.insert(result)

    suspend fun averageAccuracy(): Double = quizResultDao.averageAccuracy() ?: 0.0

    suspend fun totalTaken(): Int = quizResultDao.totalQuizzesTaken()
}
