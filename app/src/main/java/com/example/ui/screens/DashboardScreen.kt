package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallRecord
import com.example.service.LiveCallSimulator
import com.example.service.SimulatedScenario
import com.example.ui.components.RiskBadge
import com.example.ui.components.StatsGauge
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SubtleAmber
import com.example.ui.theme.SubtleAmberBg
import com.example.ui.theme.SubtleGreen
import com.example.ui.theme.SubtleGreenBg
import com.example.ui.theme.SubtleRed
import com.example.ui.theme.SubtleRedBg
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextCharcoal
import com.example.ui.theme.TextMutedGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    isProtectionActive: Boolean,
    onToggleProtection: (Boolean) -> Unit,
    totalCount: Int,
    lowCount: Int,
    mediumCount: Int,
    highCount: Int,
    recentRecords: List<CallRecord>,
    onSelectRecord: (CallRecord) -> Unit,
    onStartScenario: (SimulatedScenario) -> Unit,
    onOpenLiveHud: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "VoxSentinel",
                        color = TextCharcoal,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Voice Clone Security Engine",
                        color = TextMutedGray,
                        fontSize = 12.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            if (isProtectionActive) SubtleGreenBg else SubtleAmberBg,
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (isProtectionActive) SubtleGreen else SubtleAmber,
                            CircleShape
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (isProtectionActive) SubtleGreen else SubtleAmber,
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isProtectionActive) "ACTIVE" else "PAUSED",
                        color = if (isProtectionActive) SubtleGreen else SubtleAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Protection Control Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = if (isProtectionActive) SubtleGreenBg else SurfaceSubtle
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Protection Icon",
                                    tint = if (isProtectionActive) SubtleGreen else TextMutedGray,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Background Protection",
                                color = TextCharcoal,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isProtectionActive) "Real-time voice inspection is active" else "Call protection is disabled",
                                color = TextMutedGray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Switch(
                        checked = isProtectionActive,
                        onCheckedChange = onToggleProtection,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = NavyPrimary,
                            uncheckedThumbColor = TextMutedGray,
                            uncheckedTrackColor = SurfaceSubtle
                        )
                    )
                }
            }
        }

        // Security Analytics Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Call Security Overview",
                        color = TextCharcoal,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatsGauge(
                            totalCalls = totalCount,
                            lowRisk = lowCount,
                            mediumRisk = mediumCount,
                            highRisk = highCount,
                            modifier = Modifier.padding(end = 16.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            RiskStatRow(label = "Low Risk (Authentic)", count = lowCount, color = SubtleGreen)
                            RiskStatRow(label = "Medium Risk (Suspicious)", count = mediumCount, color = SubtleAmber)
                            RiskStatRow(label = "High Risk (Deepfake)", count = highCount, color = SubtleRed)
                        }
                    }
                }
            }
        }

        // Test Live AI Voice Scenarios Section
        item {
            Column {
                Text(
                    text = "Live Call Test Simulations",
                    color = TextCharcoal,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Simulate an active call to test real-time voice clone inspection",
                    color = TextMutedGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                LiveCallSimulator.PRESET_SCENARIOS.forEach { scenario ->
                    ScenarioCard(
                        scenario = scenario,
                        onClick = { onStartScenario(scenario) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Recent Audits Section Header
        item {
            Text(
                text = "Recent Security Audits",
                color = TextCharcoal,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (recentRecords.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Empty Log",
                            tint = TextMutedGray,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No calls analyzed yet",
                            color = TextMutedGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(recentRecords.take(5)) { record ->
                CallRecordCard(
                    record = record,
                    onClick = { onSelectRecord(record) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RiskStatRow(label: String, count: Int, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = TextMutedGray,
                fontSize = 12.sp
            )
        }
        Text(
            text = "$count",
            color = TextCharcoal,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ScenarioCard(
    scenario: SimulatedScenario,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if (scenario.isClone) SubtleRedBg else SubtleGreenBg
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PhoneInTalk,
                            contentDescription = scenario.title,
                            tint = if (scenario.isClone) SubtleRed else SubtleGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = scenario.title,
                        color = TextCharcoal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = scenario.subtitle,
                        color = TextMutedGray,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Test Call",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Test", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CallRecordCard(
    record: CallRecord,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = record.callerName,
                        color = TextCharcoal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RiskBadge(
                        riskScore = record.riskScore,
                        riskLevel = record.riskLevel,
                        showScoreOnly = true
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${record.phoneNumber} • ${record.durationSeconds}s duration",
                    color = TextMutedGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = dateFormat.format(Date(record.timestamp)),
                    color = TextMutedGray,
                    fontSize = 11.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = TextMutedGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
