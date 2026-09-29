package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.ui.theme.SubtleAmber
import com.example.ui.theme.SubtleAmberBg
import com.example.ui.theme.SubtleAmberBorder
import com.example.ui.theme.SubtleGreen
import com.example.ui.theme.SubtleGreenBg
import com.example.ui.theme.SubtleGreenBorder
import com.example.ui.theme.SubtleRed
import com.example.ui.theme.SubtleRedBg
import com.example.ui.theme.SubtleRedBorder

@Composable
fun RiskBadge(
    riskScore: Int,
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier,
    showScoreOnly: Boolean = false
) {
    val (bgColor, textColor, borderColor, icon) = when (riskLevel) {
        RiskLevel.LOW -> Quadruple(
            SubtleGreenBg,
            SubtleGreen,
            SubtleGreenBorder,
            Icons.Default.CheckCircle
        )
        RiskLevel.MEDIUM -> Quadruple(
            SubtleAmberBg,
            SubtleAmber,
            SubtleAmberBorder,
            Icons.Default.WarningAmber
        )
        RiskLevel.HIGH -> Quadruple(
            SubtleRedBg,
            SubtleRed,
            SubtleRedBorder,
            Icons.Default.Warning
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = riskLevel.label,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (showScoreOnly) "$riskScore%" else "$riskScore% • ${riskLevel.label}",
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
