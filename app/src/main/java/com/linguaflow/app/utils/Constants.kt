package com.linguaflow.app.utils

object Constants {
    const val DATABASE_NAME = "linguaflow_db"

    const val XP_PER_LESSON = 10
    const val XP_PER_QUIZ_CORRECT_ANSWER = 5
    const val XP_PER_FLASHCARD_SESSION = 8

    const val DEFAULT_HEARTS = 5

    val SUPPORTED_LANGUAGES = listOf(
        "Spanish", "French", "German", "Japanese", "Chinese", "Arabic", "Italian", "Korean"
    )

    val PROFICIENCY_LEVELS = listOf("Beginner", "Intermediate", "Advanced")

    val DAILY_GOAL_OPTIONS = listOf(5, 10, 15, 20)

    object Firestore {
        const val USERS_COLLECTION = "users"
        const val PROGRESS_COLLECTION = "progress"
        const val LEADERBOARD_COLLECTION = "leaderboard"
    }
}
