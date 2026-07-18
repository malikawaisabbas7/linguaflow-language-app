package com.linguaflow.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lessonId: Long?,
    val languageCode: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val xpEarned: Int,
    val accuracyPercent: Int,
    val takenAt: Long = System.currentTimeMillis()
)
