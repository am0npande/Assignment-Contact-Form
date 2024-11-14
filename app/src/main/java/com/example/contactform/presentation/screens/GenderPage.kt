package com.example.contactform.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactform.domain.navigation.Routes
import com.example.contactform.presentation.viewmodel.FormViewModel

@Composable
fun GenderPage(navController: NavController, viewModel: FormViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Select your Gender", fontSize = 20.sp)

        GenderDropdownMenu(viewModel)

        Button(
            onClick = { navController.navigate(Routes.Page2.routes) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Next")
        }
    }
}

@Composable
fun GenderDropdownMenu(viewModel: FormViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female", "Other")
    val selectedGender by viewModel.gender.collectAsState()
    val selectedGenderText = if (selectedGender == 0) "Select Gender" else options[selectedGender - 1]

    Box(modifier = Modifier.wrapContentSize(Alignment.Center)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp)
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = selectedGenderText,
                fontSize = 16.sp,
                color = if (selectedGender == 0) Color.Gray else Color.Black
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown Arrow",
                modifier = Modifier.padding(start = 4.dp),
                tint = Color.Gray
            )

        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .padding(horizontal = 16.dp)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        viewModel.setGender(index + 1)
                        expanded = false
                    }
                )
            }
        }
    }
}
