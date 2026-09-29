package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberOutline
import com.example.ui.theme.RiskAmber
import com.example.ui.theme.RiskGreen
import com.example.ui.theme.RiskRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun StatsGauge(
    totalCalls: Int,
    lowRisk: Int,
    mediumRisk: Int,
    highRisk: Int,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp
) {
    val total = totalCalls.coerceAtLeast(1)
    val lowSweep = (lowRisk.toFloat() / total) * 360f
    val medSweep = (mediumRisk.toFloat() / total) * 360f
    val highSweep = (highRisk.toFloat() / total) * 360f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = 14.dp.toPx()
            val arcSize = size.toPx() - strokeWidth

            // Background circle
            drawArc(
                color = CyberOutline.copy(alpha = 0.3f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            if (totalCalls > 0) {
                var startAngle = -90f

                // Green Low risk arc
                if (lowSweep > 0) {
                    drawArc(
                        color = RiskGreen,
                        startAngle = startAngle,
                        sweepAngle = lowSweep - 4f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    startAngle += lowSweep
                }

                // Amber Medium risk arc
                if (medSweep > 0) {
                    drawArc(
                        color = RiskAmber,
                        startAngle = startAngle,
                        sweepAngle = medSweep - 4f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    startAngle += medSweep
                }

                // Red High risk arc
                if (highSweep > 0) {
                    drawArc(
                        color = RiskRed,
                        startAngle = startAngle,
                        sweepAngle = highSweep - 4f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$totalCalls",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Calls Analyzed",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
