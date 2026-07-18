package com.linguaflow.app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.linguaflow.app.theme.*
import com.linguaflow.app.ui.components.CircularGoalProgress
import com.linguaflow.app.ui.components.GradientStatCard
import com.linguaflow.app.ui.components.LessonPathNode
import com.linguaflow.app.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }


    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(container))
    val state by viewModel.uiState.collectAsState()

    val currentLanguage by container.preferencesManager.targetLanguageFlow.collectAsState(initial = "Spanish")
    var showQuickMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // HEADER SECTION
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Good Morning,", fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Text(
                            text = (state.userName.ifBlank { "Learner" }) + " \uD83D\uDC4B",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }


                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        onClick = { navController.navigate("onboarding") }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(currentLanguage, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }


            item {
                Card(shape = MaterialTheme.shapes.extraLarge, modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Today's Goal ($currentLanguage)", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(12.dp))
                        val progress = if (state.dailyGoalMinutes > 0)
                            state.minutesStudiedToday / state.dailyGoalMinutes.toFloat() else 0f
                        CircularGoalProgress(
                            progress = progress,
                            label = "${state.minutesStudiedToday}/${state.dailyGoalMinutes}",
                            subLabel = "min"
                        )
                    }
                }
            }


            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GradientStatCard(
                        title = "Day Streak",
                        value = "${state.currentStreak} 🔥",
                        icon = Icons.Filled.LocalFireDepartment,
                        gradientStart = GradientPurpleStart,
                        gradientEnd = GradientPurpleEnd,
                        modifier = Modifier.weight(1f)
                    )
                    GradientStatCard(
                        title = "Total XP",
                        value = "${state.totalXp} 🏆",
                        icon = Icons.Filled.Star,
                        gradientStart = GradientYellowStart,
                        gradientEnd = GradientYellowEnd,
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            item {
                Text("Your $currentLanguage Path", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(state.lessons) { lesson ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            LessonPathNode(
                                isCompleted = lesson.isCompleted,
                                isLocked = lesson.isLocked,
                                isCurrent = !lesson.isCompleted && !lesson.isLocked,
                                onClick = {
                                    navController.navigate(Destinations.LessonDetail.createRoute(lesson.id))
                                }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(lesson.title, fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }

        FloatingActionButton(
            onClick = { showQuickMenu = true },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Quick Actions")
        }

        if (showQuickMenu) {
            ModalBottomSheet(onDismissRequest = { showQuickMenu = false }) {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                    Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(Modifier.height(16.dp))

                    ListActionItem(icon = Icons.Default.MenuBook, title = "Add New Word", sub = "Add to your Vocabulary Bank") {
                        showQuickMenu = false
                        navController.navigate(Destinations.VocabularyBank.route)
                    }

                    ListActionItem(icon = Icons.Default.Style, title = "Review Flashcards", sub = "Practice what you know") {
                        showQuickMenu = false
                        navController.navigate(Destinations.Flashcards.route)
                    }
                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun ListActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, sub: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(sub, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}