package com.linguaflow.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.material3.Scaffold
import com.linguaflow.app.ui.components.LinguaFlowBottomNavBar
import com.linguaflow.app.ui.screens.auth.ForgotPasswordScreen
import com.linguaflow.app.ui.screens.auth.LoginScreen
import com.linguaflow.app.ui.screens.auth.RegisterScreen
import com.linguaflow.app.ui.screens.flashcards.FlashcardScreen
import com.linguaflow.app.ui.screens.home.HomeScreen
import com.linguaflow.app.ui.screens.lessons.LessonDetailScreen
import com.linguaflow.app.ui.screens.lessons.LessonsScreen
import com.linguaflow.app.ui.screens.onboarding.OnboardingScreen
import com.linguaflow.app.ui.screens.practice.PracticeScreen
import com.linguaflow.app.ui.screens.practice.QuizResultScreen
import com.linguaflow.app.ui.screens.practice.QuizScreen
import com.linguaflow.app.ui.screens.profile.ProfileScreen
import com.linguaflow.app.ui.screens.profile.SettingsScreen
import com.linguaflow.app.ui.screens.progress.ProgressScreen
import com.linguaflow.app.ui.screens.splash.SplashScreen
import com.linguaflow.app.ui.screens.vocabulary.VocabularyBankScreen

@Composable
fun LinguaFlowNavGraph(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in Destinations.bottomNavRoutes) {
                LinguaFlowBottomNavBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destinations.Splash.route) { SplashScreen(navController) }

            composable(Destinations.Login.route) { LoginScreen(navController) }
            composable(Destinations.Register.route) { RegisterScreen(navController) }
            composable(Destinations.ForgotPassword.route) { ForgotPasswordScreen(navController) }

            composable(Destinations.Onboarding.route) { OnboardingScreen(navController) }

            composable(Destinations.Home.route) { HomeScreen(navController) }
            composable(Destinations.Lessons.route) { LessonsScreen(navController) }
            composable(Destinations.LessonDetail.route) { backStack ->
                val lessonId = backStack.arguments?.getString("lessonId")?.toLongOrNull() ?: 0L
                LessonDetailScreen(navController, lessonId)
            }

            composable(Destinations.Flashcards.route) { FlashcardScreen(navController) }

            composable(Destinations.Practice.route) { PracticeScreen(navController) }
            composable(Destinations.Quiz.route) { backStack ->
                val lessonId = backStack.arguments?.getString("lessonId")?.toLongOrNull() ?: 0L
                QuizScreen(navController, lessonId)
            }
            composable(Destinations.QuizResult.route) { backStack ->
                val resultId = backStack.arguments?.getString("resultId")?.toLongOrNull() ?: 0L
                QuizResultScreen(navController, resultId)
            }

            composable(Destinations.Progress.route) { ProgressScreen(navController) }

            composable(Destinations.Profile.route) { ProfileScreen(navController) }
            composable(Destinations.Settings.route) { SettingsScreen(navController) }
            composable(Destinations.VocabularyBank.route) { VocabularyBankScreen(navController) }
        }
    }
}
