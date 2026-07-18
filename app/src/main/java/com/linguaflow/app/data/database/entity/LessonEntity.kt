package com.linguaflow.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val languageCode: String,
    val unitNumber: Int,
    val title: String,
    val category: String, // Vocabulary, Grammar, Phrases, Listening, Speaking, Culture
    val difficulty: String, // Beginner, Intermediate, Advanced
    val estimatedMinutes: Int,
    val isLocked: Boolean = true,
    val isCompleted: Boolean = false,
    val progressPercent: Int = 0,
    val orderIndex: Int
)
