package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallRecord
import com.example.data.model.RiskLevel
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SubtleAmber
import com.example.ui.theme.SubtleGreen
import com.example.ui.theme.SubtleRed
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextCharcoal
import com.example.ui.theme.TextMutedGray

@Composable
fun CallHistoryScreen(
    records: List<CallRecord>,
    onSelectRecord: (CallRecord) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRiskFilter by remember { mutableStateOf<RiskLevel?>(null) }

    val filteredRecords = records.filter { record ->
        val matchesQuery = searchQuery.isBlank() ||
                record.phoneNumber.contains(searchQuery, ignoreCase = true) ||
                record.callerName.contains(searchQuery, ignoreCase = true) ||
                record.callId.contains(searchQuery, ignoreCase = true) ||
                record.detectionStatus.contains(searchQuery, ignoreCase = true)

        val matchesFilter = selectedRiskFilter == null || record.riskLevel == selectedRiskFilter

        matchesQuery && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Call History & Search",
            color = TextCharcoal,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Search analyzed calls by phone number, risk category, or Call ID",
            color = TextMutedGray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Input Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search phone number or caller name...", color = TextMutedGray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NavyPrimary) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMutedGray)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NavyPrimary,
                unfocusedBorderColor = BorderLight,
                focusedTextColor = TextCharcoal,
                unfocusedTextColor = TextCharcoal,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Risk Level Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedRiskFilter == null,
                    onClick = { selectedRiskFilter = null },
                    label = { Text("All (${records.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NavyPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceWhite,
                        labelColor = TextCharcoal
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedRiskFilter == RiskLevel.HIGH,
                    onClick = { selectedRiskFilter = if (selectedRiskFilter == RiskLevel.HIGH) null else RiskLevel.HIGH },
                    label = { Text("High Risk") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SubtleRed,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceWhite,
                        labelColor = TextCharcoal
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedRiskFilter == RiskLevel.MEDIUM,
                    onClick = { selectedRiskFilter = if (selectedRiskFilter == RiskLevel.MEDIUM) null else RiskLevel.MEDIUM },
                    label = { Text("Medium Risk") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SubtleAmber,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceWhite,
                        labelColor = TextCharcoal
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedRiskFilter == RiskLevel.LOW,
                    onClick = { selectedRiskFilter = if (selectedRiskFilter == RiskLevel.LOW) null else RiskLevel.LOW },
                    label = { Text("Low Risk") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SubtleGreen,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceWhite,
                        labelColor = TextCharcoal
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Results List
        if (filteredRecords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "No Results",
                        tint = TextMutedGray,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No records found matching query",
                        color = TextMutedGray,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredRecords) { record ->
                    CallRecordCard(
                        record = record,
                        onClick = { onSelectRecord(record) }
                    )
                }
            }
        }
    }
}
