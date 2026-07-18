package com.linguaflow.app.ui.screens.vocabulary

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Line 6 was deleted here to prevent the "insert" conflict
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.navigation.NavHostController
import com.linguaflow.app.data.database.entity.VocabularyEntity
import com.linguaflow.app.di.AppContainer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyBankScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val scope = rememberCoroutineScope()

    // States
    var query by remember { mutableStateOf("") }
    var words by remember { mutableStateOf<List<VocabularyEntity>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Language State
    val currentLanguage by container.preferencesManager.targetLanguageFlow.collectAsState(initial = "Spanish")

    // Automatically reload words when the language changes
    LaunchedEffect(currentLanguage) {
        val langCode = mapLanguageToCode(currentLanguage)
        container.vocabularyRepository.observeAll(langCode).collectLatest { words = it }
    }

    val filtered = words.filter {
        query.isBlank() || it.word.contains(query, ignoreCase = true) || it.translation.contains(query, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Word", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Header
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Column {
                    Text("Vocabulary Bank", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Learning: $currentLanguage", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
            }

            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                placeholder = { Text("Search in $currentLanguage...") },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if(query.isEmpty()) "No words saved in $currentLanguage yet.\nTap + to add your first word!"
                        else "No matches found for '$query'",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(32.dp)
                    )
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filtered) { word ->
                        Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(14.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(word.word, fontWeight = FontWeight.SemiBold)
                                    Text(word.translation, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                                AssistChip(onClick = {}, label = { Text(word.masteryLevel, fontSize = 11.sp) })
                            }
                        }
                    }
                }
            }
        }
    }

    // --- ADD WORD DIALOG ---
    if (showAddDialog) {
        var newWord by remember { mutableStateOf("") }
        var newTranslation by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New $currentLanguage Word") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newWord,
                        onValueChange = { newWord = it },
                        label = { Text("Word (e.g. Bonjour)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTranslation,
                        onValueChange = { newTranslation = it },
                        label = { Text("Translation (e.g. Hello)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newWord.isNotBlank() && newTranslation.isNotBlank()) {
                            scope.launch {
                                container.vocabularyRepository.insert(
                                    VocabularyEntity(
                                        id = 0L,                  // 1. Add this (Room uses 0 to auto-generate a new ID)
                                        word = newWord,
                                        translation = newTranslation,
                                        languageCode = mapLanguageToCode(currentLanguage),
                                        category = "Manual Entry",
                                        masteryLevel = "Learning",
                                        lessonId = 0L,

                                        pronunciation = "",
                                        exampleSentence = "",
                                        exampleSentenceTranslation = ""
                                    )
                                )
                                showAddDialog = false
                                Toast.makeText(context, "Added to $currentLanguage bank!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

private fun mapLanguageToCode(name: String): String {
    return when (name) {
        "Spanish" -> "es"
        "French" -> "fr"
        "German" -> "de"
        "Italian" -> "it"
        "Japanese" -> "ja"
        else -> "es"
    }
}