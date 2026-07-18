package com.linguaflow.app.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.linguaflow.app.navigation.Destinations
import com.linguaflow.app.theme.GradientPurpleEnd
import com.linguaflow.app.theme.GradientPurpleStart
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val scaleAnim = remember { Animatable(0.6f) }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1f, animationSpec = androidx.compose.animation.core.tween(600))
        delay(1400)

        val destination = when {
            FirebaseAuth.getInstance().currentUser == null -> Destinations.Login.route
            else -> Destinations.Home.route
        }
        navController.navigate(destination) {
            popUpTo(Destinations.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientPurpleStart, GradientPurpleEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Language,
                contentDescription = "LinguaFlow logo",
                tint = Color.White,
                modifier = Modifier.size(96.dp).scale(scaleAnim.value)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "LinguaFlow", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Learn. Speak. Connect.", color = Color.White.copy(alpha = 0.85f), fontSize = 15.sp)
        }
    }
}
