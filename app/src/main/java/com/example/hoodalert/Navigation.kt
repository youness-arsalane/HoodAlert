package com.example.hoodalert

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hoodalert.Destinations.ADD_INCIDENT_ROUTE
import com.example.hoodalert.Destinations.DASHBOARD_ROUTE
import com.example.hoodalert.Destinations.INCIDENTS_ROUTE
import com.example.hoodalert.Destinations.SIGN_IN_ROUTE
import com.example.hoodalert.dashboard.DashboardRoute
import com.example.hoodalert.incidents.AddIncidentRoute
import com.example.hoodalert.incidents.IncidentsRoute
import com.example.hoodalert.signin.SignInRoute

object Destinations {
    const val SIGN_IN_ROUTE = "sign_in"
    const val DASHBOARD_ROUTE = "dashboard"
    const val INCIDENTS_ROUTE = "incidents"
    const val ADD_INCIDENT_ROUTE = "add_incident"
}

@Composable
fun HoodAlertNavHost(
    navController: NavHostController = rememberNavController(),
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = SIGN_IN_ROUTE,
    ) {
        composable(SIGN_IN_ROUTE) {
            SignInRoute(
                context = context,
                onSignInSubmitted = {
                    navController.navigate(DASHBOARD_ROUTE)
                }
            )
        }
        composable(DASHBOARD_ROUTE) {
            DashboardRoute(
                context = context,
                navController = navController,
                onNavUp = navController::navigateUp,
            )
        }
        composable(INCIDENTS_ROUTE) {
            IncidentsRoute(
                context = context,
                navController = navController,
                onNavUp = {
                    navController.navigate(DASHBOARD_ROUTE)
                }
            )
        }
        composable(ADD_INCIDENT_ROUTE) {
            AddIncidentRoute(
                context = context,
                navController = navController,
                onNavUp = {
                    navController.navigate(INCIDENTS_ROUTE)
                }
            )
        }
    }
}
