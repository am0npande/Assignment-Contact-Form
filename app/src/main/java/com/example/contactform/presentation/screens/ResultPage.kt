package com.example.contactform.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactform.domain.models.SubmissionData
import com.example.contactform.presentation.viewmodel.FormViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun ResultPage(navController: NavController, viewModel: FormViewModel) {
    val submissionDataList = remember { mutableStateOf<List<SubmissionData>>(emptyList()) }

    // Function to format GPS to 1 decimal place
    fun formatGps(gps: String): String {
        return gps.split(",").joinToString(", ") { coordinate ->
            try {
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.HALF_UP
                df.format(coordinate.toDouble())
            } catch (e: NumberFormatException) {
                coordinate // Return as-is if parsing fails
            }
        }
    }

    LaunchedEffect(Unit) {
        submissionDataList.value = viewModel.loadSubmissionDataFromJson()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Submitted Answers",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Table headers (without image path)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Q1", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Q2", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Q3", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Audio", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("GPS", modifier = Modifier.weight(1.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Time", modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        VerticalDivider(color = Color.Gray, thickness = 1.dp)

        // Table content (without image path)
        submissionDataList.value.forEach { data ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = data.Q1, modifier = Modifier.weight(1f), fontSize = 10.sp)
                Text(text = data.Q2, modifier = Modifier.weight(1f), fontSize = 10.sp)
                Text(text = data.Q3, modifier = Modifier.weight(1f), fontSize = 10.sp)
                Text(text = data.recording, modifier = Modifier.weight(1f), fontSize = 10.sp)
                Text(text = formatGps(data.gps), modifier = Modifier.weight(1.5f), fontSize = 10.sp)
                Text(text = data.submit_time, modifier = Modifier.weight(1f), fontSize = 10.sp)
            }
            VerticalDivider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}

