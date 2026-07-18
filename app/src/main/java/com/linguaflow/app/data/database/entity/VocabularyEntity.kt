package com.linguaflow.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary")
data class VocabularyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lessonId: Long?,
    val languageCode: String,
    val word: String,
    val translation: String,
    val pronunciation: String, // IPA notation
    val audioUrl: String? = null,
    val exampleSentence: String,
    val exampleSentenceTranslation: String,
    val category: String,
    val masteryLevel: String = "New", // New, Learning, Mastered
    val timesReviewed: Int = 0,
    val lastReviewedAt: Long? = null,
    val nextReviewDue: Long = System.currentTimeMillis(),
    val isCustom: Boolean = false
)
