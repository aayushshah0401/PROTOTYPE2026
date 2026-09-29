package com.example.data.repository

import com.example.data.local.CallRecordDao
import com.example.data.model.CallRecord
import com.example.data.model.RiskLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CallRepository(private val dao: CallRecordDao) {

    val allRecords: Flow<List<CallRecord>> = dao.getAllRecords().map { entities ->
        entities.map { CallRecord.fromEntity(it) }
    }

    val totalCount: Flow<Int> = dao.getTotalCount()
    val lowRiskCount: Flow<Int> = dao.getLowRiskCount()
    val mediumRiskCount: Flow<Int> = dao.getMediumRiskCount()
    val highRiskCount: Flow<Int> = dao.getHighRiskCount()

    fun searchRecords(query: String): Flow<List<CallRecord>> {
        return dao.searchRecords(query).map { entities ->
            entities.map { CallRecord.fromEntity(it) }
        }
    }

    suspend fun getRecordById(callId: String): CallRecord? {
        val entity = dao.getRecordById(callId) ?: return null
        return CallRecord.fromEntity(entity)
    }

    suspend fun saveRecord(record: CallRecord) {
        dao.insertRecord(record.toEntity())
    }

    suspend fun deleteRecord(callId: String) {
        dao.deleteRecordById(callId)
    }

    suspend fun seedInitialDataIfEmpty() {
        // We check if database is empty by reading first emission or standard query
        // If needed, insert initial realistic security audit entries
    }

    companion object {
        fun createSampleRecords(): List<CallRecord> {
            val now = System.currentTimeMillis()
            val dayMs = 86400000L
            return listOf(
                CallRecord(
                    callId = "CALL-1028",
                    phoneNumber = "+1 (415) 892-0124",
                    callerName = "Chase Security - Fraud Alert",
                    timestamp = now - (15 * 60 * 1000), // 15 mins ago
                    durationSeconds = 142,
                    riskScore = 88,
                    riskLevel = RiskLevel.HIGH,
                    detectionStatus = "AI Voice Clone Detected (Neural Vocoder)",
                    pitchJitterScore = 0.92f,
                    spectralFluxScore = 0.85f,
                    syntheticArtifactRatio = 0.89f,
                    aiAnalysisSummary = "High probability of AI-cloned voice impersonating bank customer support agent. Requesting sensitive 2FA OTP codes.",
                    detectedAnomalies = listOf(
                        "Zero micro-pitch variance in acoustic harmonics",
                        "Elevated spectral phase distortion (>4.2kHz)",
                        "Synthetic neural TTS boundary artifacts detected",
                        "Known deepfake voice signature match (RawNet2)"
                    ),
                    userAction = "Muted & Ended Call"
                ),
                CallRecord(
                    callId = "CALL-1027",
                    phoneNumber = "+91 98765 43210",
                    callerName = "Unknown International Caller",
                    timestamp = now - (2 * 3600 * 1000), // 2 hours ago
                    durationSeconds = 88,
                    riskScore = 54,
                    riskLevel = RiskLevel.MEDIUM,
                    detectionStatus = "Suspicious Voice Pattern (Unusual Pitch)",
                    pitchJitterScore = 0.58f,
                    spectralFluxScore = 0.52f,
                    syntheticArtifactRatio = 0.49f,
                    aiAnalysisSummary = "Acoustic features display robotic cadence and static formant peaks. Caller claiming to be telecom operator representative.",
                    detectedAnomalies = listOf(
                        "Formant bandwidth unnaturally constrained",
                        "Suspicious audio latency delay during speech turns"
                    ),
                    userAction = "Acknowledged Warning"
                ),
                CallRecord(
                    callId = "CALL-1026",
                    phoneNumber = "+1 (650) 321-9988",
                    callerName = "Sarah Connor (Friend)",
                    timestamp = now - (5 * 3600 * 1000), // 5 hours ago
                    durationSeconds = 310,
                    riskScore = 12,
                    riskLevel = RiskLevel.LOW,
                    detectionStatus = "Authentic Voice Verified",
                    pitchJitterScore = 0.12f,
                    spectralFluxScore = 0.15f,
                    syntheticArtifactRatio = 0.08f,
                    aiAnalysisSummary = "Natural human voice characteristics with dynamic breathing patterns and organic pitch variations.",
                    detectedAnomalies = emptyList(),
                    userAction = "None"
                ),
                CallRecord(
                    callId = "CALL-1025",
                    phoneNumber = "+1 (800) 555-0199",
                    callerName = "Utility Provider Inc.",
                    timestamp = now - dayMs, // Yesterday
                    durationSeconds = 205,
                    riskScore = 22,
                    riskLevel = RiskLevel.LOW,
                    detectionStatus = "Authentic Human Voice",
                    pitchJitterScore = 0.20f,
                    spectralFluxScore = 0.18f,
                    syntheticArtifactRatio = 0.14f,
                    aiAnalysisSummary = "Human voice verified with typical room reverberation and organic vocal fold resonance.",
                    detectedAnomalies = emptyList(),
                    userAction = "None"
                ),
                CallRecord(
                    callId = "CALL-1024",
                    phoneNumber = "+1 (312) 774-0012",
                    callerName = "Grandson Emergency Scam",
                    timestamp = now - (2 * dayMs),
                    durationSeconds = 64,
                    riskScore = 95,
                    riskLevel = RiskLevel.HIGH,
                    detectionStatus = "CRITICAL: Cloned Family Voice Scam",
                    pitchJitterScore = 0.98f,
                    spectralFluxScore = 0.92f,
                    syntheticArtifactRatio = 0.96f,
                    aiAnalysisSummary = "Deepfake clone of family member requesting urgent bail money transfer via wire/crypto. High synthesis score.",
                    detectedAnomalies = listOf(
                        "Cloned voice embedding matched ElevenLabs AI engine",
                        "Absence of ambient background room acoustics",
                        "Severe high frequency spectral cutoff at 8kHz",
                        "Mismatched speaker prosody rhythm"
                    ),
                    userAction = "End Call & Blocked Number"
                )
            )
        }
    }
}
