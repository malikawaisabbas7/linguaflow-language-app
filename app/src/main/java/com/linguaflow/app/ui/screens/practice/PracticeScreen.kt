package com.linguaflow.app.ui.screens.practice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.linguaflow.app.navigation.Destinations

private data class PracticeMode(val title: String, val subtitle: String, val icon: ImageVector, val route: (NavHostController) -> Unit)

private val modes = listOf(
    PracticeMode("Flashcards", "Review words due today", Icons.Filled.Style) { it.navigate(Destinations.Flashcards.route) },
    PracticeMode("Quick Quiz", "Multiple choice practice", Icons.Filled.Quiz) { it.navigate(Destinations.Quiz.createRoute(0L)) },
    PracticeMode("Listen & Type", "Test your listening skills", Icons.Filled.Headphones) { it.navigate(Destinations.Quiz.createRoute(0L)) },
    PracticeMode("Pronunciation", "Record and compare", Icons.Filled.Mic) { it.navigate(Destinations.Flashcards.route) }
)

@Composable
fun PracticeScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Practice", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            "Choose how you want to practice today",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(modes) { mode ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().clickablePractice { mode.route(navController) }
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(mode.icon, contentDescription = mode.title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(mode.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Text(mode.subtitle, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }
            }
        }
    }
}

private fun Modifier.clickablePractice(onClick: () -> Unit): Modifier =
    this.then(Modifier.clickable(onClick = onClick))
