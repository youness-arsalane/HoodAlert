package com.example.hoodalert.incidents

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AddIncidentRoute(
    context: Context,
    navController: NavController,
    onNavUp: () -> Unit,
) {
    AddIncidentScreen(
        context = context,
        navController = navController,
        onNavUp = onNavUp
    )
}
