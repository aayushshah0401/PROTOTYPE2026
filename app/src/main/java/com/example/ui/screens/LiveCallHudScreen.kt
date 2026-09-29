package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.service.CallState
import com.example.service.LiveCallSession
import com.example.ui.components.CallOverlayDialog
import com.example.ui.components.RiskBadge
import com.example.ui.components.WaveformVisualizer
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SubtleAmber
import com.example.ui.theme.SubtleGreen
import com.example.ui.theme.SubtleRed
import com.example.ui.theme.SubtleRedBg
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextCharcoal
import com.example.ui.theme.TextMutedGray

@Composable
fun LiveCallHudScreen(
    session: LiveCallSession?,
    onToggleMute: () -> Unit,
    onContinueCall: () -> Unit,
    onEndCall: () -> Unit,
    onCloseHud: () -> Unit
) {
    if (session == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = NavyPrimary,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Active Call Inspection",
                    color = TextCharcoal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "VoxSentinel protection is active in background and will connect automatically during calls.",
                    color = TextMutedGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    val riskColor = when (session.currentRiskLevel) {
        RiskLevel.LOW -> SubtleGreen
        RiskLevel.MEDIUM -> SubtleAmber
        RiskLevel.HIGH -> SubtleRed
    }

    val minutes = session.durationSeconds / 60
    val seconds = session.durationSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    val lastResult = session.lastAnalysisResult

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Live Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(riskColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE VOICE INSPECTION",
                        color = riskColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = formattedTime,
                    color = TextCharcoal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Active Caller Info Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(containerColor = SurfaceSubtle),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneInTalk,
                                        contentDescription = "Call",
                                        tint = NavyPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = session.callerName,
                                    color = TextCharcoal,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = session.phoneNumber,
                                    color = TextMutedGray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        RiskBadge(
                            riskScore = session.currentRiskScore,
                            riskLevel = session.currentRiskLevel
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Separated AI Model Prediction Metrics Card (Req #7)
            if (lastResult != null) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Neural Model Analysis Output",
                                    color = TextCharcoal,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = lastResult.modelIdentifier,
                                color = TextMutedGray,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ModelMetricPill(label = "Raw Model Logit", value = String.format("%.2f", lastResult.rawModelOutput))
                            ModelMetricPill(label = "Probability", value = "${(lastResult.confidenceProbability * 100).toInt()}%")
                            ModelMetricPill(label = "VoxSentinel Risk", value = "${lastResult.riskScore}/100")
                            ModelMetricPill(label = "Classification", value = lastResult.riskLevel.name)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Waveform Audio Spectrum Visualizer Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PCM 16kHz Acoustic Spectrum Stream",
                            color = TextMutedGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = if (session.isMuted) "AUDIO MUTED" else "INSPECTING",
                            color = if (session.isMuted) SubtleRed else TealAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    WaveformVisualizer(
                        frequencies = session.spectralFrequencies,
                        isAnalyzing = session.callState == CallState.ANALYZING || session.callState == CallState.ACTIVE,
                        accentColor = riskColor,
                        height = 48.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Real-Time Analysis Inspection Console Log
            Text(
                text = "Live Model Prediction Feed",
                color = TextCharcoal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(session.analysisLog.takeLast(15)) { log ->
                        Text(
                            text = "> $log",
                            color = if (log.contains("HIGH")) SubtleRed else if (log.contains("Model Output")) TealAccent else TextMutedGray,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mute button
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if (session.isMuted) SubtleRedBg else SurfaceWhite
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .size(48.dp)
                        .border(1.dp, if (session.isMuted) SubtleRed else BorderLight, CircleShape)
                ) {
                    IconButton(
                        onClick = onToggleMute,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (session.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mute",
                            tint = if (session.isMuted) SubtleRed else TextCharcoal
                        )
                    }
                }

                // End Call Button
                Button(
                    onClick = onEndCall,
                    colors = ButtonDefaults.buttonColors(containerColor = SubtleRed),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "End Call Now",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Prominent High Risk Overlay Dialog
        if (session.callState == CallState.HIGH_RISK_ALERT) {
            CallOverlayDialog(
                session = session,
                onContinue = onContinueCall,
                onEndCall = onEndCall
            )
        }
    }
}

@Composable
private fun ModelMetricPill(label: String, value: String) {
    Column {
        Text(text = label, color = TextMutedGray, fontSize = 10.sp)
        Text(text = value, color = TextCharcoal, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}
