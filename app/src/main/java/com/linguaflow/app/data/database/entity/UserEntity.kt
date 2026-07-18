package com.linguaflow.app.data.database.entity

import androidx.datastore.dataStore
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val nativeLanguage: String,
    val targetLanguage: String,
    val proficiencyLevel: String,
    val dailyGoalMinutes: Int = 10,
    val totalXp: Int = 0,
    val level: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: Long = System.currentTimeMillis()

)
