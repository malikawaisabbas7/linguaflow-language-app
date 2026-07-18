package com.linguaflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A single circular node on the winding lesson path shown on the Home screen,
 * similar to Duolingo's skill tree.
 */
@Composable
fun LessonPathNode(
    isCompleted: Boolean,
    isLocked: Boolean,
    isCurrent: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isCompleted -> Color(0xFF22C55E)
        isCurrent -> MaterialTheme.colorScheme.primary
        isLocked -> Color(0xFFD1D5DB)
        else -> MaterialTheme.colorScheme.secondary
    }

    Box(
        modifier = modifier
            .size(64.dp)
            .background(backgroundColor, CircleShape)
            .clickable(enabled = !isLocked, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when {
                isCompleted -> Icons.Filled.Check
                isLocked -> Icons.Filled.Lock
                else -> Icons.Filled.Star
            },
            contentDescription = null,
            tint = Color.White
        )
    }
}
