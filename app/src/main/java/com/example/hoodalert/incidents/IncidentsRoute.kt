package com.example.hoodalert.incidents

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun IncidentsRoute(
    context: Context,
    navController: NavController,
    onNavUp: () -> Unit,
) {
    IncidentsScreen(
        context = context,
        navController = navController,
        onNavUp = onNavUp
    )
}
