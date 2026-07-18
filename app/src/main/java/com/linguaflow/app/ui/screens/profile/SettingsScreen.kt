package com.linguaflow.app.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.linguaflow.app.viewmodel.ProfileViewModel

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.factory(container))
    val darkMode by viewModel.darkMode.collectAsState()

    var notificationsEnabled by remember { mutableStateOf(true) }
    var soundEffectsEnabled by remember { mutableStateOf(true) }
    var useMetric by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        SettingsSwitchRow("Dark Mode", darkMode) { viewModel.toggleDarkMode(it) }
        SettingsSwitchRow("Notifications", notificationsEnabled) { notificationsEnabled = it }
        SettingsSwitchRow("Sound Effects", soundEffectsEnabled) { soundEffectsEnabled = it }
        SettingsSwitchRow("Use Metric Units", useMetric) { useMetric = it }

        ListItem(headlineContent = { Text("Privacy Policy") })
        ListItem(headlineContent = { Text("About LinguaFlow") })
        ListItem(
            headlineContent = { Text("Reset Progress", color = MaterialTheme.colorScheme.error) }
        )
    }
}

@Composable
private fun SettingsSwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 15.sp)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
