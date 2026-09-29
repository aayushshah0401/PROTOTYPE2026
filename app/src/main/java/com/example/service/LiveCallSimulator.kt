package com.example.service

import com.example.data.ai.AudioClipCapturer
import com.example.data.ai.GeminiVoiceAnalyzer
import com.example.data.ai.LiveMicrophoneCapturer
import com.example.data.ai.VoiceAnalysisResult
import com.example.data.model.CallRecord
import com.example.data.model.RiskLevel
import com.example.data.repository.CallRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class CallState {
    IDLE,
    CONNECTING,
    ACTIVE,
    ANALYZING,
    HIGH_RISK_ALERT,
    ENDED
}

data class SimulatedScenario(
    val id: String,
    val title: String,
    val subtitle: String,
    val phoneNumber: String,
    val callerName: String,
    val isClone: Boolean,
    val sampleTranscript: String
)

data class LiveCallSession(
    val callId: String,
    val phoneNumber: String,
    val callerName: String,
    val startTime: Long,
    val durationSeconds: Int = 0,
    val callState: CallState = CallState.IDLE,
    val currentRiskScore: Int = 0,
    val currentRiskLevel: RiskLevel = RiskLevel.LOW,
    val lastAnalysisResult: VoiceAnalysisResult? = null,
    val isMuted: Boolean = false,
    val analysisLog: List<String> = emptyList(),
    val spectralFrequencies: List<Float> = List(12) { 0.1f },
    val userActionTaken: String = "None"
)

class LiveCallSimulator(
    private val callRepository: CallRepository
) {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var simulationJob: Job? = null
    private var durationTimerJob: Job? = null

    private val _currentSession = MutableStateFlow<LiveCallSession?>(null)
    val currentSession: StateFlow<LiveCallSession?> = _currentSession.asStateFlow()

    companion object {
        val PRESET_SCENARIOS = listOf(
            SimulatedScenario(
                id = "scen_mic_test",
                title = "Live Mic Voice Test Session",
                subtitle = "Supported real-time session capturing your live microphone stream",
                phoneNumber = "+1 (800) 555-0199",
                callerName = "Live Microphone Session",
                isClone = false,
                sampleTranscript = "Speak into your microphone naturally to test real-time AI voice clone inspection."
            ),
            SimulatedScenario(
                id = "scen_bank",
                title = "Chase Fraud Security (AI Clone)",
                subtitle = "Deepfake impersonating bank fraud team asking for 2FA OTP",
                phoneNumber = "+1 (800) 432-3117",
                callerName = "Chase Security Rep (AI Clone)",
                isClone = true,
                sampleTranscript = "Hello, this is Chase Fraud Prevention. We detected an urgent $1,250 transfer. Read back the 6-digit OTP code sent to your phone."
            ),
            SimulatedScenario(
                id = "scen_grandson",
                title = "Emergency Family Voice Scam",
                subtitle = "Cloned grandson voice claiming car crash emergency",
                phoneNumber = "+1 (415) 555-0199",
                callerName = "Alex (Cloned Grandson Voice)",
                isClone = true,
                sampleTranscript = "Grandma, it's Alex! I got into a horrible car crash and need $2,000 for bail right now. Don't tell my parents, please send via wire!"
            ),
            SimulatedScenario(
                id = "scen_friend",
                title = "Legitimate Friend (Natural Voice)",
                subtitle = "Real human call with natural vocal pitch and breath",
                phoneNumber = "+1 (650) 891-2300",
                callerName = "David Miller (Friend)",
                isClone = false,
                sampleTranscript = "Hey! Just calling to check if we are still meeting up for dinner tonight at 7? Let me know!"
            )
        )
    }

    fun startSimulatedCall(scenario: SimulatedScenario) {
        stopCall()

        val callId = "CALL-${(1000..9999).random()}"
        val initialSession = LiveCallSession(
            callId = callId,
            phoneNumber = scenario.phoneNumber,
            callerName = scenario.callerName,
            startTime = System.currentTimeMillis(),
            callState = CallState.CONNECTING,
            analysisLog = listOf("Initializing supported real-time voice channel...", "VoxSentinel background protection active")
        )

        _currentSession.value = initialSession

        simulationJob = scope.launch {
            delay(1000) // Session connecting

            // Start live microphone capture if supported
            LiveMicrophoneCapturer.startCapture()

            _currentSession.value = _currentSession.value?.copy(
                callState = CallState.ACTIVE,
                analysisLog = _currentSession.value?.analysisLog.orEmpty() + "Voice session active. Capturing short 2.5s audio chunk windows..."
            )

            startTimer()

            var chunkIndex = 1
            while (_currentSession.value?.callState != CallState.ENDED && _currentSession.value?.callState != CallState.IDLE) {
                
                // 1. Automatic Short Audio Capture (2.5s chunk)
                // Read short audio chunk from live mic input or session stream
                var pcmAudioBytes = LiveMicrophoneCapturer.readShortChunkWindow(durationSeconds = 2.5f)
                if (scenario.isClone) {
                    pcmAudioBytes = AudioClipCapturer.captureAudioClipBuffer(
                        durationSeconds = 2.5f,
                        isSyntheticScenario = true
                    )
                }

                // Calculate real spectral frequencies for live visualization
                val realFreqs = List(12) { idx ->
                    val chunkStart = (idx * (pcmAudioBytes.size / 12))
                    var chunkSum = 0f
                    for (c in 0 until 100.coerceAtMost(pcmAudioBytes.size / 12)) {
                        if (chunkStart + c < pcmAudioBytes.size) {
                            chunkSum += Math.abs(pcmAudioBytes[chunkStart + c].toFloat())
                        }
                    }
                    (chunkSum / 10000f).coerceIn(0.15f, 1.0f)
                }

                _currentSession.value = _currentSession.value?.copy(
                    callState = CallState.ANALYZING,
                    spectralFrequencies = realFreqs,
                    analysisLog = _currentSession.value?.analysisLog.orEmpty() + "Chunk #$chunkIndex (${pcmAudioBytes.size} bytes) sent to AI Anti-Spoofing Model..."
                )

                // 2. Send short audio chunk securely to AI model
                val analysisResult = GeminiVoiceAnalyzer.analyzeAudioClip(
                    pcmAudioBytes = pcmAudioBytes,
                    callerContext = scenario.callerName
                )

                val newRiskLevel = analysisResult.riskLevel
                val isHighRisk = newRiskLevel == RiskLevel.HIGH

                val logMessage = "Chunk #$chunkIndex Output: Logit ${analysisResult.rawModelOutput} | Prob ${(analysisResult.confidenceProbability * 100).toInt()}% → Risk Score ${analysisResult.riskScore}% (${newRiskLevel.label})"

                val updatedState = if (isHighRisk) CallState.HIGH_RISK_ALERT else CallState.ACTIVE

                // 3. Update UI Risk Status
                _currentSession.value = _currentSession.value?.copy(
                    callState = updatedState,
                    currentRiskScore = analysisResult.riskScore,
                    currentRiskLevel = newRiskLevel,
                    lastAnalysisResult = analysisResult,
                    isMuted = isHighRisk,
                    analysisLog = _currentSession.value?.analysisLog.orEmpty() + logMessage
                )

                // 4. AUTOMATIC AUDIO PURGE / DELETION: Zero out temporary RAM audio buffer
                java.util.Arrays.fill(pcmAudioBytes, 0.toByte())

                chunkIndex++
                delay(3000) // Continue capturing next short audio chunk automatically
            }
        }
    }

    private fun startTimer() {
        durationTimerJob?.cancel()
        durationTimerJob = scope.launch {
            while (_currentSession.value?.callState != CallState.ENDED && _currentSession.value?.callState != CallState.IDLE) {
                delay(1000)
                _currentSession.value = _currentSession.value?.let { session ->
                    session.copy(durationSeconds = session.durationSeconds + 1)
                }
            }
        }
    }

    fun toggleMute() {
        _currentSession.value = _currentSession.value?.let {
            it.copy(isMuted = !it.isMuted)
        }
    }

    fun continueCallAfterWarning() {
        _currentSession.value = _currentSession.value?.let {
            it.copy(
                callState = CallState.ACTIVE,
                isMuted = false,
                userActionTaken = "Acknowledged & Continued Session",
                analysisLog = it.analysisLog + "User acknowledged warning and resumed voice session."
            )
        }
    }

    fun endCall(userReason: String = "User Ended Session") {
        val session = _currentSession.value ?: return

        LiveMicrophoneCapturer.stopCapture()

        _currentSession.value = session.copy(
            callState = CallState.ENDED,
            userActionTaken = userReason,
            analysisLog = session.analysisLog + "Session ended ($userReason). Saving security metadata..."
        )

        // Save session METADATA ONLY to Room database and Firebase (Zero audio recordings saved)
        scope.launch {
            val record = CallRecord(
                callId = session.callId,
                phoneNumber = session.phoneNumber,
                callerName = session.callerName,
                timestamp = session.startTime,
                durationSeconds = session.durationSeconds,
                riskScore = session.currentRiskScore,
                riskLevel = session.currentRiskLevel,
                detectionStatus = session.lastAnalysisResult?.detectionStatus ?: "Analysis Complete",
                pitchJitterScore = session.lastAnalysisResult?.pitchJitterScore ?: 0.1f,
                spectralFluxScore = session.lastAnalysisResult?.spectralFluxScore ?: 0.1f,
                syntheticArtifactRatio = session.lastAnalysisResult?.syntheticArtifactRatio ?: 0.1f,
                aiAnalysisSummary = session.lastAnalysisResult?.aiAnalysisSummary ?: "Voice analysis completed.",
                detectedAnomalies = session.lastAnalysisResult?.detectedAnomalies ?: emptyList(),
                userAction = userReason
            )
            callRepository.saveRecord(record)
        }

        durationTimerJob?.cancel()
        simulationJob?.cancel()
    }

    fun dismissSession() {
        stopCall()
        _currentSession.value = null
    }

    private fun stopCall() {
        LiveMicrophoneCapturer.stopCapture()
        durationTimerJob?.cancel()
        simulationJob?.cancel()
    }
}
