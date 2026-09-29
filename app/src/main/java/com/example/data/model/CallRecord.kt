package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RiskLevel(val label: String, val minScore: Int, val maxScore: Int) {
    LOW("Low Risk", 0, 35),
    MEDIUM("Medium Risk", 36, 60),
    HIGH("High Risk", 61, 100);

    companion object {
        fun fromScore(score: Int): RiskLevel {
            return when {
                score <= 35 -> LOW
                score <= 60 -> MEDIUM
                else -> HIGH
            }
        }
    }
}

@Entity(tableName = "call_records")
data class CallRecordEntity(
    @PrimaryKey val callId: String,
    val phoneNumber: String,
    val callerName: String,
    val timestamp: Long,
    val durationSeconds: Int,
    val riskScore: Int,
    val riskLevel: String,
    val detectionStatus: String,
    val pitchJitterScore: Float,
    val spectralFluxScore: Float,
    val syntheticArtifactRatio: Float,
    val aiAnalysisSummary: String,
    val detectedAnomalies: String, // Comma separated list
    val userAction: String, // "Continued", "Ended Call", "Muted", "Ignored"
    val audioClipUri: String? = null
)

data class CallRecord(
    val callId: String,
    val phoneNumber: String,
    val callerName: String,
    val timestamp: Long,
    val durationSeconds: Int,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val detectionStatus: String,
    val pitchJitterScore: Float,
    val spectralFluxScore: Float,
    val syntheticArtifactRatio: Float,
    val aiAnalysisSummary: String,
    val detectedAnomalies: List<String>,
    val userAction: String,
    val audioClipUri: String? = null
) {
    fun toEntity(): CallRecordEntity {
        return CallRecordEntity(
            callId = callId,
            phoneNumber = phoneNumber,
            callerName = callerName,
            timestamp = timestamp,
            durationSeconds = durationSeconds,
            riskScore = riskScore,
            riskLevel = riskLevel.name,
            detectionStatus = detectionStatus,
            pitchJitterScore = pitchJitterScore,
            spectralFluxScore = spectralFluxScore,
            syntheticArtifactRatio = syntheticArtifactRatio,
            aiAnalysisSummary = aiAnalysisSummary,
            detectedAnomalies = detectedAnomalies.joinToString(";"),
            userAction = userAction,
            audioClipUri = audioClipUri
        )
    }

    companion object {
        fun fromEntity(entity: CallRecordEntity): CallRecord {
            return CallRecord(
                callId = entity.callId,
                phoneNumber = entity.phoneNumber,
                callerName = entity.callerName,
                timestamp = entity.timestamp,
                durationSeconds = entity.durationSeconds,
                riskScore = entity.riskScore,
                riskLevel = RiskLevel.valueOf(entity.riskLevel),
                detectionStatus = entity.detectionStatus,
                pitchJitterScore = entity.pitchJitterScore,
                spectralFluxScore = entity.spectralFluxScore,
                syntheticArtifactRatio = entity.syntheticArtifactRatio,
                aiAnalysisSummary = entity.aiAnalysisSummary,
                detectedAnomalies = if (entity.detectedAnomalies.isBlank()) emptyList() else entity.detectedAnomalies.split(";"),
                userAction = entity.userAction,
                audioClipUri = entity.audioClipUri
            )
        }
    }
}

data class UserProfile(
    val userId: String = "usr_98231",
    val name: String = "Aayush Shah",
    val email: String = "aayush.shah2201@gmail.com",
    val phone: String = "+1 415 555 0198",
    val protectionTier: String = "Pro Cyber Protection",
    val isProtectionActive: Boolean = true,
    val isLoggedIn: Boolean = true
)

data class AppSettings(
    val isProtectionEnabled: Boolean = true,
    val sensitivityThreshold: Int = 60, // Above 60 is high risk
    val autoMuteHighRisk: Boolean = true,
    val alertSoundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val minimalDataRetention: Boolean = true, // Auto purge raw clips
    val customApiKey: String = ""
)
