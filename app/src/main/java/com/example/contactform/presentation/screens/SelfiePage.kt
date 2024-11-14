package com.example.contactform.presentation.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactform.domain.navigation.Routes
import com.example.contactform.presentation.viewmodel.FormViewModel

@Composable
fun SelfiePage(navController: NavController, viewModel: FormViewModel) {
    val context = LocalContext.current
    var selfieUri by remember { mutableStateOf("") }
    var selfieBitmap: Bitmap? by remember { mutableStateOf(null) }

    // Launcher for taking a picture with the front camera
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            selfieBitmap = bitmap
            selfieUri = viewModel.saveBitmapToFile(bitmap)
            viewModel.setSelfiePath(selfieUri)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Take a Selfie",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Display the selfie if captured
        selfieBitmap?.let { bitmap ->
            Surface(
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp),
                shape = CircleShape,
                color = Color.Gray
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Selfie",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Capture Selfie Button
        Button(onClick = { cameraLauncher.launch() }) {
            Text("Capture Selfie")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back Button
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next Button
        Button(onClick = {
            navController.navigate(Routes.Submit.routes)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}
