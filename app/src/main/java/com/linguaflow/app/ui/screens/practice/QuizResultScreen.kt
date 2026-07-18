package com.linguaflow.app.ui.screens.practice

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.linguaflow.app.data.database.entity.QuizResultEntity
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.navigation.Destinations
import com.linguaflow.app.ui.components.PrimaryGradientButton
import kotlinx.coroutines.flow.first

@Composable
fun QuizResultScreen(navController: NavHostController, resultId: Long) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    var result by remember { mutableStateOf<QuizResultEntity?>(null) }

    LaunchedEffect(resultId) {
        result = container.quizRepository.observeResults().first().find { it.id == resultId }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Quiz Complete!", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(28.dp))

        result?.let { r ->
            Card(shape = MaterialTheme.shapes.large, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                    ResultRow("Correct Answers", "${r.correctAnswers}/${r.totalQuestions}")
                    Spacer(modifier = Modifier.height(12.dp))
                    ResultRow("Accuracy", "${r.accuracyPercent}%")
                    Spacer(modifier = Modifier.height(12.dp))
                    ResultRow("XP Earned", "+${r.xpEarned} XP")
                }
            }
        } ?: CircularProgressIndicator()

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryGradientButton(
            text = "Back to Practice",
            onClick = {
                navController.navigate(Destinations.Practice.route) {
                    popUpTo(Destinations.Home.route)
                }
            }
        )
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}
