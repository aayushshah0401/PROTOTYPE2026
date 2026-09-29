package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CallRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallRecordDao {

    @Query("SELECT * FROM call_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<CallRecordEntity>>

    @Query("SELECT * FROM call_records WHERE callId = :callId LIMIT 1")
    suspend fun getRecordById(callId: String): CallRecordEntity?

    @Query("""
        SELECT * FROM call_records 
        WHERE phoneNumber LIKE '%' || :query || '%' 
           OR callerName LIKE '%' || :query || '%' 
           OR callId LIKE '%' || :query || '%'
           OR riskLevel LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun searchRecords(query: String): Flow<List<CallRecordEntity>>

    @Query("SELECT * FROM call_records WHERE riskLevel = :riskLevel ORDER BY timestamp DESC")
    fun getRecordsByRiskLevel(riskLevel: String): Flow<List<CallRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: CallRecordEntity)

    @Query("DELETE FROM call_records WHERE callId = :callId")
    suspend fun deleteRecordById(callId: String)

    @Query("DELETE FROM call_records")
    suspend fun clearAllRecords()

    @Query("SELECT COUNT(*) FROM call_records")
    fun getTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM call_records WHERE riskLevel = 'LOW'")
    fun getLowRiskCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM call_records WHERE riskLevel = 'MEDIUM'")
    fun getMediumRiskCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM call_records WHERE riskLevel = 'HIGH'")
    fun getHighRiskCount(): Flow<Int>
}
