package com.linguaflow.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streaks")
data class StreakEntity(
    @PrimaryKey val id: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastStudyDate: String? = null, // yyyy-MM-dd
    val streakFreezeAvailable: Boolean = true
)
