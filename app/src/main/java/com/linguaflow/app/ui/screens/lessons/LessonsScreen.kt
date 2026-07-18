package com.linguaflow.app.ui.screens.lessons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.linguaflow.app.navigation.Destinations
import com.linguaflow.app.viewmodel.LessonsViewModel

@Composable
fun LessonsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val viewModel: LessonsViewModel = viewModel(factory = LessonsViewModel.factory(container))
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) {
        Text(
            "Lessons",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(LessonsViewModel.categories) { category ->
                val selected = state.selectedCategory == category
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.selectCategory(category) },
                    label = { Text(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val filtered = if (state.selectedCategory == "All") state.allLessons
        else state.allLessons.filter { it.category == state.selectedCategory }

        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No lessons yet in this category.\nSeed data via LessonRepository.seedLessons(...)",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(32.dp)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filtered) { lesson ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().clickableLesson {
                            navController.navigate(Destinations.LessonDetail.createRoute(lesson.id))
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(lesson.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${lesson.category} · ${lesson.difficulty} · ${lesson.estimatedMinutes} min",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                if (lesson.progressPercent in 1..99) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LinearProgressIndicator(
                                        progress = lesson.progressPercent / 100f,
                                        modifier = Modifier.fillMaxWidth().height(6.dp),
                                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            when {
                                lesson.isCompleted -> Text("✅")
                                lesson.isLocked -> Text("🔒")
                                else -> Text("▶️")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Modifier.clickableLesson(onClick: () -> Unit): Modifier =
    this.then(Modifier.clickable(onClick = onClick))
