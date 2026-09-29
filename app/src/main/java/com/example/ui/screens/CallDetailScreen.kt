package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallRecord
import com.example.data.model.RiskLevel
import com.example.ui.components.RiskBadge
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SubtleAmber
import com.example.ui.theme.SubtleGreen
import com.example.ui.theme.SubtleRed
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextCharcoal
import com.example.ui.theme.TextMutedGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CallDetailScreen(
    record: CallRecord,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm a", Locale.getDefault())

    val riskColor = when (record.riskLevel) {
        RiskLevel.LOW -> SubtleGreen
        RiskLevel.MEDIUM -> SubtleAmber
        RiskLevel.HIGH -> SubtleRed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Navigation bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextCharcoal
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Security Audit Report",
                    color = TextCharcoal,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID: ${record.callId}",
                    color = TextMutedGray,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = record.callerName,
                            color = TextCharcoal,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = record.phoneNumber,
                            color = TextMutedGray,
                            fontSize = 13.sp
                        )
                    }

                    RiskBadge(
                        riskScore = record.riskScore,
                        riskLevel = record.riskLevel
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailInfoItem(label = "Date & Time", value = dateFormat.format(Date(record.timestamp)))
                    DetailInfoItem(label = "Duration", value = "${record.durationSeconds}s")
                    DetailInfoItem(label = "User Action", value = record.userAction)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Verdict Summary
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "AI Verdict",
                        tint = NavyPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Voice Inspection Verdict",
                        color = TextCharcoal,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = record.detectionStatus,
                    color = riskColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = record.aiAnalysisSummary,
                    color = TextCharcoal,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Acoustic Feature Breakdown
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Acoustics",
                        tint = NavyPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Acoustic Biometric Feature Metrics",
                        color = TextCharcoal,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AcousticFeatureBar(
                    label = "Pitch Jitter Variance",
                    score = record.pitchJitterScore,
                    hint = if (record.pitchJitterScore > 0.6f) "Abnormally rigid pitch floor (Synthetic artifact)" else "Natural pitch variation"
                )

                Spacer(modifier = Modifier.height(12.dp))

                AcousticFeatureBar(
                    label = "Spectral Flux Instability",
                    score = record.spectralFluxScore,
                    hint = if (record.spectralFluxScore > 0.6f) "Unusual spectral energy shift across 2-4kHz" else "Organic vocal resonance"
                )

                Spacer(modifier = Modifier.height(12.dp))

                AcousticFeatureBar(
                    label = "Neural Vocoder Artifact Ratio",
                    score = record.syntheticArtifactRatio,
                    hint = if (record.syntheticArtifactRatio > 0.6f) "Matched ElevenLabs/RawNet2 TTS synthesis signature" else "No neural synthesis detected"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detected Anomalies
        if (record.detectedAnomalies.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BugReport,
                            contentDescription = "Anomalies",
                            tint = SubtleRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Detected Acoustic Anomalies (${record.detectedAnomalies.size})",
                            color = TextCharcoal,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    record.detectedAnomalies.forEach { anomaly ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "• ", color = SubtleRed, fontWeight = FontWeight.Bold)
                            Text(text = anomaly, color = TextCharcoal, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Export Share Report
        Button(
            onClick = {
                val reportText = """
                    [VoxSentinel Voice Security Audit Report]
                    Call ID: ${record.callId}
                    Caller: ${record.callerName} (${record.phoneNumber})
                    Date: ${dateFormat.format(Date(record.timestamp))}
                    Risk Score: ${record.riskScore}% (${record.riskLevel.label})
                    Status: ${record.detectionStatus}
                    Verdict: ${record.aiAnalysisSummary}
                    Anomalies Detected: ${record.detectedAnomalies.joinToString(", ")}
                    
                    Generated by VoxSentinel AI Security System.
                """.trimIndent()

                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, reportText)
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share Security Report"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = NavyPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share Report")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Export & Share Audit Report", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DetailInfoItem(label: String, value: String) {
    Column {
        Text(text = label, color = TextMutedGray, fontSize = 11.sp)
        Text(text = value, color = TextCharcoal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AcousticFeatureBar(label: String, score: Float, hint: String) {
    val progress = score.coerceIn(0f, 1f)
    val color = when {
        progress < 0.35f -> SubtleGreen
        progress < 0.60f -> SubtleAmber
        else -> SubtleRed
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = TextCharcoal, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = "${(progress * 100).toInt()}%", color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { progress },
            color = color,
            trackColor = BorderLight,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(text = hint, color = TextMutedGray, fontSize = 10.sp)
    }
}
