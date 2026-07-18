package com.linguaflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A rounded gradient card used across the Home dashboard for metrics like
 * "Words Learned", "Day Streak", "XP Today", etc.
 */
@Composable
fun GradientStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    gradientStart: Color,
    gradientEnd: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(gradientStart, gradientEnd)))
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, color = Color.White, fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(text = title, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
        }
    }
}
