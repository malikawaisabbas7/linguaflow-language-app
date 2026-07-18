package com.linguaflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.linguaflow.app.theme.GradientPurpleEnd
import com.linguaflow.app.theme.GradientPurpleStart

@Composable
fun PrimaryGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(),
        modifier = modifier.fillMaxWidth().height(52.dp)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    Brush.horizontalGradient(listOf(GradientPurpleStart, GradientPurpleEnd)),
                    RoundedCornerShape(20.dp)
                ),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(text = text, color = Color.White)
        }
    }
}
