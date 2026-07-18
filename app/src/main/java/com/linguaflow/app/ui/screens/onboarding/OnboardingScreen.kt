package com.linguaflow.app.ui.screens.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.navigation.Destinations
import com.linguaflow.app.ui.components.PrimaryGradientButton
import com.linguaflow.app.utils.Constants
import kotlinx.coroutines.launch

private data class LanguageOption(val name: String, val flag: String)

private val languageOptions = listOf(
    LanguageOption("Spanish", "🇪🇸"),
    LanguageOption("French", "🇫🇷"),
    LanguageOption("German", "🇩🇪"),
    LanguageOption("Japanese", "🇯🇵"),
    LanguageOption("Chinese", "🇨🇳"),
    LanguageOption("Arabic", "🇸🇦"),
    LanguageOption("Italian", "🇮🇹"),
    LanguageOption("Korean", "🇰🇷")
)

@Composable
fun OnboardingScreen(navController: NavHostController) {
    // 1. Setup Data Access
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val scope = rememberCoroutineScope()

    var step by remember { mutableStateOf(0) }
    var selectedLanguage by remember { mutableStateOf<String?>(null) }
    var selectedLevel by remember { mutableStateOf<String?>(null) }
    var selectedGoal by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        LinearProgressIndicator(
            progress = (step + 1) / 3f,
            modifier = Modifier.fillMaxWidth().height(6.dp),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (step) {
            0 -> {
                Text("Which language do you want to learn?", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(languageOptions) { option ->
                        val selected = selectedLanguage == option.name
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.fillMaxWidth().clickableCard { selectedLanguage = option.name }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(option.flag, fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(option.name, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
            1 -> {
                Text("What's your current level?", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                Constants.PROFICIENCY_LEVELS.forEach { level ->
                    val selected = selectedLevel == level
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickableCard { selectedLevel = level }
                    ) {
                        Text(level, modifier = Modifier.padding(18.dp), fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            2 -> {
                Text("Set your daily goal", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                Constants.DAILY_GOAL_OPTIONS.forEach { minutes ->
                    val selected = selectedGoal == minutes
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickableCard { selectedGoal = minutes }
                    ) {
                        Text("$minutes minutes / day", modifier = Modifier.padding(18.dp), fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val canContinue = when (step) {
            0 -> selectedLanguage != null
            1 -> selectedLevel != null
            else -> selectedGoal != null
        }

        PrimaryGradientButton(
            text = if (step < 2) "Continue" else "Get Started",
            enabled = canContinue,
            onClick = {
                if (step < 2) {
                    step += 1
                } else {
                    // 2. SAVE CHOICES TO PREFERENCES
                    scope.launch {
                        selectedLanguage?.let { container.preferencesManager.setTargetLanguage(it) }
                        selectedGoal?.let { container.preferencesManager.setDailyGoal(it) }
                        container.preferencesManager.setOnboardingDone(true)

                        // 3. Navigate to Home
                        navController.navigate(Destinations.Home.route) {
                            popUpTo(Destinations.Onboarding.route) { inclusive = true }
                        }
                    }
                }
            }
        )
    }
}

private fun Modifier.clickableCard(onClick: () -> Unit): Modifier =
    this.then(Modifier.clickable(onClick = onClick))