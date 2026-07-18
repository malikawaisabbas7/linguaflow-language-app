package com.linguaflow.app.navigation

/**
 * Central definition of every navigable route in the app.
 */
sealed class Destinations(val route: String) {
    object Splash : Destinations("splash")

    object Login : Destinations("login")
    object Register : Destinations("register")
    object ForgotPassword : Destinations("forgot_password")

    object Onboarding : Destinations("onboarding")

    object Home : Destinations("home")
    object Lessons : Destinations("lessons")
    object LessonDetail : Destinations("lesson_detail/{lessonId}") {
        fun createRoute(lessonId: Long) = "lesson_detail/$lessonId"
    }

    object Flashcards : Destinations("flashcards")

    object Quiz : Destinations("quiz/{lessonId}") {
        fun createRoute(lessonId: Long) = "quiz/$lessonId"
    }
    object QuizResult : Destinations("quiz_result/{resultId}") {
        fun createRoute(resultId: Long) = "quiz_result/$resultId"
    }

    object Practice : Destinations("practice")

    object Progress : Destinations("progress")

    object Profile : Destinations("profile")
    object Settings : Destinations("settings")
    object VocabularyBank : Destinations("vocabulary_bank")

    companion object {
        // Tabs shown in the bottom navigation bar
        val bottomNavRoutes = listOf(Home.route, Lessons.route, Practice.route, Progress.route, Profile.route)
    }
}
