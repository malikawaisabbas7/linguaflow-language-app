package com.linguaflow.app.ui.screens.lessons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
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
import com.linguaflow.app.ui.components.PrimaryGradientButton
import com.linguaflow.app.viewmodel.LessonDetailViewModel

@Composable
fun LessonDetailScreen(navController: NavHostController, lessonId: Long) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val viewModel: LessonDetailViewModel = viewModel(factory = LessonDetailViewModel.factory(container, lessonId))
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                state.lesson?.title ?: "Lesson",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.vocabulary) { word ->
                    Card(shape = MaterialTheme.shapes.large, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(word.word, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { /* play word.audioUrl */ }) {
                                    Icon(Icons.Filled.VolumeUp, contentDescription = "Play pronunciation")
                                }
                            }
                            Text(word.pronunciation, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(word.translation, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(word.exampleSentence, fontSize = 14.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            Text(word.exampleSentenceTranslation, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }

                if (state.vocabulary.isEmpty()) {
                    item {
                        Text(
                            "No vocabulary linked to this lesson yet.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 40.dp)
                        )
                    }
                }
            }

            Box(modifier = Modifier.padding(20.dp)) {
                PrimaryGradientButton(
                    text = "Mark as Learned",
                    onClick = {
                        viewModel.markLessonLearned()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
