package com.linguaflow.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
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
import com.linguaflow.app.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.factory(container))
    val user by viewModel.user.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { navController.navigate(Destinations.Settings.route) }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(72.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = "Profile picture", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(user?.name ?: "Learner", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(user?.email ?: "", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(shape = MaterialTheme.shapes.large, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp)) {
                ProfileRow("Native Language", user?.nativeLanguage ?: "—")
                ProfileRow("Learning", user?.targetLanguage ?: "—")
                ProfileRow("Level", user?.proficiencyLevel ?: "—")
                ProfileRow("Total XP", "${user?.totalXp ?: 0}")
                ProfileRow("Level", "${user?.level ?: 1}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ListItem(
            headlineContent = { Text("Vocabulary Bank") },
            supportingContent = { Text("View all the words you've learned") },
            modifier = Modifier.clickableRow { navController.navigate(Destinations.VocabularyBank.route) }
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                viewModel.logout()
                navController.navigate(Destinations.Login.route) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

private fun Modifier.clickableRow(onClick: () -> Unit): Modifier =
    this.then(Modifier.clickable(onClick = onClick))
