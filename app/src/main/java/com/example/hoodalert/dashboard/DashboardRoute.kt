package com.example.hoodalert.dashboard

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun DashboardRoute(
    context: Context,
    navController: NavController,
    onNavUp: () -> Unit,
) {
    DashboardScreen(
        context = context,
        navController = navController,
        onNavUp = onNavUp
    )
}
