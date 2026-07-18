package com.linguaflow.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey val code: String, // e.g. "es", "fr", "ja"
    val displayName: String,
    val flagEmoji: String
)
