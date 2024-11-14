package com.example.contactform.domain.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.contactform.presentation.screens.AgePage
import com.example.contactform.presentation.screens.GenderPage
import com.example.contactform.presentation.screens.ResultPage
import com.example.contactform.presentation.screens.SelfiePage
import com.example.contactform.presentation.screens.SubmitPage
import com.example.contactform.presentation.viewmodel.FormViewModel

@Composable
fun FormNavGraph(modifier: Modifier, navController: NavHostController, viewModel: FormViewModel) {
    NavHost(modifier = modifier, navController = navController, startDestination = Routes.Page1.routes) {
        composable(Routes.Page1.routes) { GenderPage(navController,viewModel) }
        composable(Routes.Page2.routes) { AgePage(navController,viewModel) }
        composable(Routes.Page3.routes) { SelfiePage(navController,viewModel) }
        composable(Routes.Submit.routes) { SubmitPage(navController,viewModel) }
        composable(Routes.Result.routes) { ResultPage(navController,viewModel) }
    }
}