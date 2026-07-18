package com.linguaflow.app.ui.screens.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.navigation.Destinations
import com.linguaflow.app.ui.components.PrimaryGradientButton
import com.linguaflow.app.viewmodel.QuizViewModel

@Composable
fun QuizScreen(navController: NavHostController, lessonId: Long) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val viewModel: QuizViewModel = viewModel(factory = QuizViewModel.factory(container, lessonId))
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            navController.navigate(Destinations.QuizResult.createRoute(state.lastResultId)) {
                popUpTo(Destinations.Practice.route)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Quiz", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            state.questions.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Not enough vocabulary saved yet to build a quiz.", modifier = Modifier.padding(32.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
            else -> {
                val question = state.questions[state.currentIndex]

                LinearProgressIndicator(
                    progress = state.currentIndex / state.questions.size.toFloat(),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(6.dp),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                    Text(
                        "Translate:",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(question.word.word, fontSize = 28.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(28.dp))

                    question.options.forEach { option ->
                        val isSelected = state.selectedAnswer == option
                        val isCorrectOption = option == question.correctAnswer
                        val containerColor = when {
                            !state.showFeedback -> MaterialTheme.colorScheme.surface
                            isCorrectOption -> Color(0xFF22C55E).copy(alpha = 0.2f)
                            isSelected && !isCorrectOption -> Color(0xFFEF4444).copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .then(Modifier)
                                .quizClickable { viewModel.selectAnswer(option) }
                        ) {
                            Text(option, modifier = Modifier.padding(18.dp), fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    AnimatedVisibility(visible = state.showFeedback) {
                        PrimaryGradientButton(
                            text = if (state.currentIndex == state.questions.size - 1) "Finish" else "Next Question",
                            onClick = { viewModel.nextQuestion() }
                        )
                    }
                }
            }
        }
    }
}

private fun androidx.compose.ui.Modifier.quizClickable(onClick: () -> Unit): androidx.compose.ui.Modifier =
    this.then(androidx.compose.ui.Modifier.clickable(onClick = onClick))
