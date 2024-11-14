package com.example.contactform.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactform.domain.navigation.Routes
import com.example.contactform.presentation.viewmodel.FormViewModel

@Composable
fun SubmitPage(navController: NavController, viewModel: FormViewModel) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween, // Distribute space evenly
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text("Review and Submit", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(32.dp)) // Spacer for some spacing between text and buttons

        // Action Button Section (Back and Submit)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between buttons
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth() // Button will take up full width
            ) {
                Text("Back")
            }

            Button(
                onClick = {
                    viewModel.captureLocation()
                    viewModel.captureTimestamp()
                    viewModel.stopRecording()

                    Log.d(
                        "SubmitPage",
                        "Captured Data: ${viewModel.gender.value}, ${viewModel.age.value}, ${viewModel.selfiePath.value}"
                    )

                    viewModel.saveDataToLocal()
                    navController.navigate(Routes.Result.routes) {
                        popUpTo(Routes.Page1.routes) { inclusive = true }  // Remove all screens up to and including Page1
                    }
                },
                modifier = Modifier.fillMaxWidth() // Button will take up full width
            ) {
                Text("Submit")
            }
        }
    }
}
