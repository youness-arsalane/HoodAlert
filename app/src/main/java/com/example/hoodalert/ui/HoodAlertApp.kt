package com.example.hoodalert.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hoodalert.ui.navigation.HoodAlertNavHost

@Composable
fun HoodAlertApp(navController: NavHostController = rememberNavController()) {
    HoodAlertNavHost(navController = navController)
}