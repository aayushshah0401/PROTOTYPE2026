package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TealAccent

@Composable
fun WaveformVisualizer(
    frequencies: List<Float>,
    isAnalyzing: Boolean,
    accentColor: Color = TealAccent,
    modifier: Modifier = Modifier,
    height: Dp = 60.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveAnim by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveAnim"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val barCount = frequencies.size.coerceAtLeast(8)
        val spaceBetween = 10.dp.toPx()
        val totalSpacing = spaceBetween * (barCount - 1)
        val barWidth = ((size.width - totalSpacing) / barCount).coerceAtLeast(6.dp.toPx())

        for (i in 0 until barCount) {
            val baseFactor = frequencies.getOrElse(i) { 0.2f }
            val dynamicFactor = if (isAnalyzing) {
                ((baseFactor * waveAnim) + (i % 3) * 0.1f).coerceIn(0.15f, 1.0f)
            } else {
                0.2f
            }

            val barHeight = size.height * dynamicFactor
            val x = i * (barWidth + spaceBetween)
            val y = (size.height - barHeight) / 2f

            drawRoundRect(
                color = accentColor.copy(alpha = if (isAnalyzing) 0.85f else 0.3f),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}
