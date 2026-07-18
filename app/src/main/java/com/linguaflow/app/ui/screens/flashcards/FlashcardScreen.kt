package com.linguaflow.app.ui.screens.flashcards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.linguaflow.app.di.AppContainer
import com.linguaflow.app.ui.components.PrimaryGradientButton
import com.linguaflow.app.viewmodel.FlashcardViewModel

@Composable
fun FlashcardScreen(navController: NavHostController) {
    val context = LocalContext.current
    val container = remember { AppContainer.get(context) }
    val viewModel: FlashcardViewModel = viewModel(factory = FlashcardViewModel.factory(container))
    val state by viewModel.uiState.collectAsState()
    var isFlipped by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Flashcards", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }

            state.deck.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No cards due for review right now. Great job! 🎉", textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(32.dp))
            }

            state.sessionComplete -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Session complete! 🎉", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("You reviewed ${state.deck.size} cards", fontSize = 14.sp)
                    Spacer(Modifier.height(20.dp))
                    PrimaryGradientButton(text = "Done", onClick = { navController.popBackStack() }, modifier = Modifier.padding(horizontal = 40.dp))
                }
            }

            else -> {
                val card = state.deck[state.currentIndex]
                LinearProgressIndicator(
                    progress = state.currentIndex / state.deck.size.toFloat(),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(6.dp),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val rotation by animateFloatAsState(if (isFlipped) 180f else 0f, tween(400), label = "flip")

                    Card(
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .graphicsLayerRotation(rotation)
                            .then(Modifier)
                    ) {
                        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                            if (rotation <= 90f) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(card.word, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(8.dp))
                                    IconButton(onClick = { /* play card.audioUrl */ }) {
                                        Icon(Icons.Filled.VolumeUp, contentDescription = "Play audio")
                                    }
                                    Spacer(Modifier.height(12.dp))
                                    Text("Tap to reveal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                }
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.graphicsLayerRotation(180f)
                                ) {
                                    Text(card.translation, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(10.dp))
                                    Text(card.exampleSentence, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                }
                            }
                        }
                    }

                    // Invisible overlay to catch taps and flip the card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .clickableFlip { isFlipped = !isFlipped }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilledIconButton(
                        onClick = { isFlipped = false; viewModel.swipe(false) },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFFEF4444)),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Review again", tint = Color.White)
                    }
                    FilledIconButton(
                        onClick = { isFlipped = false; viewModel.swipe(true) },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF22C55E)),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Known", tint = Color.White)
                    }
                }
            }
        }
    }
}

private fun Modifier.graphicsLayerRotation(rotationY: Float): Modifier =
    this.then(
        Modifier.graphicsLayer { this.rotationY = rotationY; cameraDistance = 12f * density }
    )

private fun Modifier.clickableFlip(onClick: () -> Unit): Modifier =
    this.then(Modifier.clickable(onClick = onClick))
