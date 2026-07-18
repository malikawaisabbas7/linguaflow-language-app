package com.linguaflow.app.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.ui.components.AchievementBadgeCard
import com.linguaflow.app.viewmodel.ProgressViewModel

@Composable
fun ProgressScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val viewModel: ProgressViewModel = viewModel(factory = ProgressViewModel.factory(container))
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Progress", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Current Streak", "${state.currentStreak} 🔥", Modifier.weight(1f))
            StatCard("Longest Streak", "${state.longestStreak} 🏆", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Words Learned", "${state.totalWordsLearned}", Modifier.weight(1f))
            StatCard("Lessons Done", "${state.totalLessonsCompleted}", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Avg Accuracy", "${state.averageQuizAccuracy}%", Modifier.weight(1f))
            StatCard("Quizzes Taken", "${state.totalQuizzesTaken}", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Achievements", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (state.achievements.isEmpty()) {
            Text(
                "Complete lessons and quizzes to unlock badges.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 13.sp
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.achievements) { achievement ->
                    AchievementBadgeCard(
                        emoji = achievement.emoji,
                        title = achievement.title,
                        isUnlocked = achievement.isUnlocked,
                        modifier = Modifier.width(110.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Weekly Activity", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Card(shape = MaterialTheme.shapes.large, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val maxMinutes = (state.recentProgress.maxOfOrNull { it.minutesStudied } ?: 1).coerceAtLeast(1)
                state.recentProgress.take(7).reversed().forEach { day ->
                    val heightFraction = day.minutesStudied / maxMinutes.toFloat()
                    Box(
                        modifier = Modifier
                            .width(18.dp)
                            .fillMaxHeight(heightFraction.coerceIn(0.05f, 1f))
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
                    )
                }
                if (state.recentProgress.isEmpty()) {
                    Text("No activity logged yet", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(shape = MaterialTheme.shapes.large, modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}
