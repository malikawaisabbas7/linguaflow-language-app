package com.linguaflow.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String, // e.g. "streak_7", "first_lesson"
    val title: String,
    val description: String,
    val emoji: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)
