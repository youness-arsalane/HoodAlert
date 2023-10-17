package com.example.hoodalert.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hoodalert.ui.navigation.route.SignInRoute
import com.example.hoodalert.ui.screens.DashboardDestination
import com.example.hoodalert.ui.screens.DashboardScreen
import com.example.hoodalert.ui.screens.SignInDestination
import com.example.hoodalert.ui.screens.incidents.IncidentListDestination
import com.example.hoodalert.ui.screens.incidents.ListScreen
import com.example.hoodalert.ui.screens.incidents.IncidentDetailsDestination
import com.example.hoodalert.ui.screens.incidents.DetailsScreen
import com.example.hoodalert.ui.screens.incidents.IncidentEditDestination
import com.example.hoodalert.ui.screens.incidents.EditScreen
import com.example.hoodalert.ui.screens.incidents.IncidentEntryDestination
import com.example.hoodalert.ui.screens.incidents.EntryScreen

@Composable
fun HoodAlertNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = IncidentListDestination.route,
        modifier = modifier
    ) {
        composable(route = SignInDestination.route) {
            SignInRoute(
                onSignInSubmitted = {
                    navController.navigate(DashboardDestination.route)
                }
            )
        }

        composable(route = DashboardDestination.route) {
            DashboardScreen(
                navController = navController,
                onNavUp = navController::navigateUp,
            )
        }

        composable(route = IncidentListDestination.route) {
            ListScreen(
                navigateToIncidentEntry = { navController.navigate(IncidentEntryDestination.route) },
                navigateToIncidentUpdate = {
                    navController.navigate("${IncidentDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = IncidentEntryDestination.route) {
            EntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = IncidentDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(IncidentDetailsDestination.incidentIdArg) {
                type = NavType.IntType
            })
        ) {
            DetailsScreen(
                navigateToEditIncident = { navController.navigate("${IncidentEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = IncidentEditDestination.routeWithArgs,
            arguments = listOf(navArgument(IncidentEditDestination.incidentIdArg) {
                type = NavType.IntType
            })
        ) {
            EditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
