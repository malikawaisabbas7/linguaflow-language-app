package com.linguaflow.app.data.database.entity

import androidx.room.Entity

@Entity(tableName = "progress", primaryKeys = ["date"])
data class ProgressEntity(
    val date: String, // yyyy-MM-dd
    val minutesStudied: Int = 0,
    val wordsLearned: Int = 0,
    val lessonsCompleted: Int = 0,
    val xpEarned: Int = 0
)
