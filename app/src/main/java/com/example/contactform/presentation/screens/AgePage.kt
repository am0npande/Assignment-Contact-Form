package com.example.contactform.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactform.domain.navigation.Routes
import com.example.contactform.presentation.viewmodel.FormViewModel

@Composable
fun AgePage(navController: NavController, viewModel: FormViewModel) {
    var age by remember { mutableStateOf<Int?>(null) }
    var context = LocalContext.current

    // Using Box to position the buttons at the bottom
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Column for the content (TextField and instructions)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Enter your Age", fontSize = 20.sp)
            TextField(
                value = age?.toString() ?: "",
                onValueChange = {
                    age = it.toIntOrNull() // Ensure to handle non-numeric input gracefully
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(16.dp)
            )
        }

        // Row for positioning the buttons at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp) // Add padding for spacing from the bottom
        ) {
            // Back Button
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Back")
            }

            // Next Button
            Button(
                onClick = {
                    if (age != null) {
                        viewModel.setAge(age.toString())
                        navController.navigate(Routes.Page3.routes)
                    } else {
                        Toast.makeText(context, "This Info is Mandatory", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Next")
            }
        }
    }
}
