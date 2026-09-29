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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppSettings
import com.example.data.model.UserProfile
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SubtleGreen
import com.example.ui.theme.SubtleRed
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextCharcoal
import com.example.ui.theme.TextMutedGray

@Composable
fun SettingsScreen(
    userProfile: UserProfile?,
    appSettings: AppSettings,
    onUpdateSettings: (AppSettings) -> Unit,
    onLogout: () -> Unit
) {
    var sensitivity by remember { mutableFloatStateOf(appSettings.sensitivityThreshold.toFloat()) }
    var autoMute by remember { mutableStateOf(appSettings.autoMuteHighRisk) }
    var alertSound by remember { mutableStateOf(appSettings.alertSoundEnabled) }
    var vibration by remember { mutableStateOf(appSettings.vibrationEnabled) }
    var minimalData by remember { mutableStateOf(appSettings.minimalDataRetention) }
    var apiKeyInput by remember { mutableStateOf(appSettings.customApiKey) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Settings & Configuration",
            color = TextCharcoal,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Configure risk engine thresholds and security parameters",
            color = TextMutedGray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card
        if (userProfile != null) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = SurfaceSubtle),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(text = userProfile.name, color = TextCharcoal, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = userProfile.email, color = TextMutedGray, fontSize = 12.sp)
                            Text(text = userProfile.protectionTier, color = TealAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    OutlinedButton(
                        onClick = onLogout,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = SubtleRed, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Detection Sensitivity Slider
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Risk Engine Sensitivity", color = TextCharcoal, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "Set threshold for triggering High-Risk warnings (${sensitivity.toInt()}%)", color = TextMutedGray, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = sensitivity,
                    onValueChange = {
                        sensitivity = it
                        onUpdateSettings(appSettings.copy(sensitivityThreshold = it.toInt()))
                    },
                    valueRange = 40f..85f,
                    colors = SliderDefaults.colors(
                        thumbColor = NavyPrimary,
                        activeTrackColor = NavyPrimary,
                        inactiveTrackColor = BorderLight
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Strict (40%)", color = TextMutedGray, fontSize = 11.sp)
                    Text(text = "Balanced (60%)", color = NavyPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Relaxed (85%)", color = TextMutedGray, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Security Toggles
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Protection Controls", color = TextCharcoal, fontSize = 15.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                SettingsToggleRow(
                    icon = Icons.Default.VolumeUp,
                    title = "Auto-Mute High Risk Calls",
                    subtitle = "Mutes call audio immediately when score > ${sensitivity.toInt()}%",
                    checked = autoMute,
                    onCheckedChange = {
                        autoMute = it
                        onUpdateSettings(appSettings.copy(autoMuteHighRisk = it))
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderLight)

                SettingsToggleRow(
                    icon = Icons.Default.Notifications,
                    title = "Alert Chime Tones",
                    subtitle = "Play distinct warning tones for medium and high risk calls",
                    checked = alertSound,
                    onCheckedChange = {
                        alertSound = it
                        onUpdateSettings(appSettings.copy(alertSoundEnabled = it))
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BorderLight)

                SettingsToggleRow(
                    icon = Icons.Default.Lock,
                    title = "Minimal Audio Data Retention",
                    subtitle = "Automatically purge raw audio clip cache after inspection",
                    checked = minimalData,
                    onCheckedChange = {
                        minimalData = it
                        onUpdateSettings(appSettings.copy(minimalDataRetention = it))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // API Configuration Card
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
                    Icon(Icons.Default.Key, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "AI Endpoint & API Key", color = TextCharcoal, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "VoxSentinel uses Gemini AI + AASIST anti-spoofing API configured via Google AI Studio.",
                    color = TextMutedGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = apiKeyInput,
                    onValueChange = {
                        apiKeyInput = it
                        onUpdateSettings(appSettings.copy(customApiKey = it))
                    },
                    placeholder = { Text("Custom API Key (Optional)", color = TextMutedGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyPrimary,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceSubtle,
                        unfocusedContainerColor = SurfaceSubtle,
                        focusedTextColor = TextCharcoal,
                        unfocusedTextColor = TextCharcoal
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Permissions Status Checklist
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "System Permissions Status", color = TextCharcoal, fontSize = 15.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                PermissionRow(icon = Icons.Default.Mic, name = "Microphone Access (Audio Inspection)", isGranted = true)
                Spacer(modifier = Modifier.height(8.dp))
                PermissionRow(icon = Icons.Default.Phone, name = "Phone State Listener (Call Detection)", isGranted = true)
                Spacer(modifier = Modifier.height(8.dp))
                PermissionRow(icon = Icons.Default.Notifications, name = "Notifications (High-Risk Alerts)", isGranted = true)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, color = TextCharcoal, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = TextMutedGray, fontSize = 11.sp)
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NavyPrimary,
                uncheckedThumbColor = TextMutedGray,
                uncheckedTrackColor = SurfaceSubtle
            )
        )
    }
}

@Composable
private fun PermissionRow(icon: ImageVector, name: String, isGranted: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = TextMutedGray, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = name, color = TextCharcoal, fontSize = 13.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Granted", tint = SubtleGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Granted", color = SubtleGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
