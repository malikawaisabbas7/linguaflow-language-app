package com.linguaflow.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Large animated circular ring showing progress toward the daily learning goal,
 * mirroring the "10,000 steps" ring pattern from fitness apps but for minutes/XP.
 */
@Composable
fun CircularGoalProgress(
    progress: Float, // 0f..1f
    label: String,
    subLabel: String,
    size: androidx.compose.ui.unit.Dp = 180.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 16.dp,
    trackColor: Color = Color(0xFFE5E7EB),
    progressColor: Color = MaterialTheme.colorScheme.primary
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 900),
        label = "goalProgress"
    )

    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = strokeWidth.toPx())
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke,
                size = Size(size.toPx() - stroke.width, size.toPx() - stroke.width),
                topLeft = androidx.compose.ui.geometry.Offset(stroke.width / 2, stroke.width / 2)
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = stroke,
                size = Size(size.toPx() - stroke.width, size.toPx() - stroke.width),
                topLeft = androidx.compose.ui.geometry.Offset(stroke.width / 2, stroke.width / 2)
            )
        }
        Text(text = label, style = MaterialTheme.typography.headlineMedium)
        // subLabel rendered below label in the caller's layout if needed
    }
}
