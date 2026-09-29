package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VoxDatabase
import com.example.data.model.AppSettings
import com.example.data.model.CallRecord
import com.example.data.repository.AuthRepository
import com.example.data.repository.CallRepository
import com.example.service.CallState
import com.example.service.LiveCallSimulator
import com.example.service.VoiceProtectionService
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CallDetailScreen
import com.example.ui.screens.CallHistoryScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LiveCallHudScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.*
import kotlinx.coroutines.launch

enum class MainTab(val title: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Shield),
    LIVE_HUD("Live Protection", Icons.Default.PhoneInTalk),
    HISTORY("Call History", Icons.Default.History),
    SETTINGS("Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = VoxDatabase.getDatabase(applicationContext)
        val callRepository = CallRepository(database.callRecordDao())
        val authRepository = AuthRepository()
        val callSimulator = LiveCallSimulator(callRepository)

        setContent {
            VoxSentinelTheme {
                VoxSentinelApp(
                    callRepository = callRepository,
                    authRepository = authRepository,
                    callSimulator = callSimulator
                )
            }
        }
    }
}

@Composable
fun VoxSentinelApp(
    callRepository: CallRepository,
    authRepository: AuthRepository,
    callSimulator: LiveCallSimulator
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val isLoggedIn by authRepository.isLoggedIn.collectAsState()
    val userProfile by authRepository.userProfile.collectAsState()

    val records by callRepository.allRecords.collectAsState(initial = emptyList())
    val totalCount by callRepository.totalCount.collectAsState(initial = 0)
    val lowCount by callRepository.lowRiskCount.collectAsState(initial = 0)
    val medCount by callRepository.mediumRiskCount.collectAsState(initial = 0)
    val highCount by callRepository.highRiskCount.collectAsState(initial = 0)

    val currentSession by callSimulator.currentSession.collectAsState()

    var currentTab by remember { mutableStateOf(MainTab.DASHBOARD) }
    var selectedDetailRecord by remember { mutableStateOf<CallRecord?>(null) }

    var appSettings by remember { mutableStateOf(AppSettings()) }

    // Seed sample data if database is empty on first launch
    LaunchedEffect(Unit) {
        scope.launch {
            if (records.isEmpty()) {
                CallRepository.createSampleRecords().forEach { sample ->
                    callRepository.saveRecord(sample)
                }
            }
        }
    }

    // Auto navigate to Live Call HUD when a call simulation starts
    LaunchedEffect(currentSession?.callState) {
        if (currentSession != null && currentSession?.callState != CallState.IDLE) {
            currentTab = MainTab.LIVE_HUD
        }
    }

    if (!isLoggedIn) {
        AuthScreen(
            onLogin = { email, pass, onResult ->
                authRepository.login(email, pass, onResult)
            },
            onRegister = { name, email, pass, onResult ->
                authRepository.register(name, email, pass, onResult)
            }
        )
        return
    }

    // Secondary screen back button handling
    if (selectedDetailRecord != null) {
        BackHandler { selectedDetailRecord = null }
        CallDetailScreen(
            record = selectedDetailRecord!!,
            onBack = { selectedDetailRecord = null }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceWhite,
                contentColor = TextPrimary,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                MainTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    val hasActiveCall = tab == MainTab.LIVE_HUD && currentSession != null && currentSession?.callState != CallState.ENDED

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            if (hasActiveCall) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = SubtleRed)
                                    }
                                ) {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title,
                                        tint = if (isSelected) NavyPrimary else SubtleRed
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) NavyPrimary else TextMuted
                                )
                            }
                        },
                        label = {
                            Text(
                                text = tab.title,
                                color = if (isSelected) NavyPrimary else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = SurfaceSubtle
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CyberNavy)
        ) {
            when (currentTab) {
                MainTab.DASHBOARD -> {
                    DashboardScreen(
                        isProtectionActive = appSettings.isProtectionEnabled,
                        onToggleProtection = { enabled ->
                            appSettings = appSettings.copy(isProtectionEnabled = enabled)
                            if (enabled) {
                                VoiceProtectionService.startService(context)
                            } else {
                                VoiceProtectionService.stopService(context)
                            }
                        },
                        totalCount = totalCount,
                        lowCount = lowCount,
                        mediumCount = medCount,
                        highCount = highCount,
                        recentRecords = records,
                        onSelectRecord = { record ->
                            selectedDetailRecord = record
                        },
                        onStartScenario = { scenario ->
                            callSimulator.startSimulatedCall(scenario)
                            currentTab = MainTab.LIVE_HUD
                        },
                        onOpenLiveHud = { currentTab = MainTab.LIVE_HUD }
                    )
                }

                MainTab.LIVE_HUD -> {
                    LiveCallHudScreen(
                        session = currentSession,
                        onToggleMute = { callSimulator.toggleMute() },
                        onContinueCall = { callSimulator.continueCallAfterWarning() },
                        onEndCall = { callSimulator.endCall("User Ended Call") },
                        onCloseHud = { callSimulator.dismissSession() }
                    )
                }

                MainTab.HISTORY -> {
                    CallHistoryScreen(
                        records = records,
                        onSelectRecord = { record ->
                            selectedDetailRecord = record
                        }
                    )
                }

                MainTab.SETTINGS -> {
                    SettingsScreen(
                        userProfile = userProfile,
                        appSettings = appSettings,
                        onUpdateSettings = { newSettings ->
                            appSettings = newSettings
                        },
                        onLogout = { authRepository.logout() }
                    )
                }
            }
        }
    }
}
