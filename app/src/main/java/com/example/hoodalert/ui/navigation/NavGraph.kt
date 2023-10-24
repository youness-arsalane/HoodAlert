package com.example.hoodalert.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hoodalert.data.auth.SharedPreferencesManager
import com.example.hoodalert.ui.screens.DashboardDestination
import com.example.hoodalert.ui.screens.DashboardScreen
import com.example.hoodalert.ui.screens.RegisterDestination
import com.example.hoodalert.ui.screens.RegisterScreen
import com.example.hoodalert.ui.screens.SignInDestination
import com.example.hoodalert.ui.screens.SignInScreen
import com.example.hoodalert.ui.screens.communities.CommunityListDestination
import com.example.hoodalert.ui.screens.communities.ListScreen as CommunityListScreen
import com.example.hoodalert.ui.screens.communities.CommunityDetailsDestination
import com.example.hoodalert.ui.screens.communities.DetailsScreen as CommunityDetailsScreen
import com.example.hoodalert.ui.screens.communities.CommunityEditDestination
import com.example.hoodalert.ui.screens.communities.EditScreen as CommunityEditScreen
import com.example.hoodalert.ui.screens.communities.CommunityEntryDestination
import com.example.hoodalert.ui.screens.communities.EntryScreen as CommunityEntryScreen
import com.example.hoodalert.ui.screens.incidents.IncidentListDestination
import com.example.hoodalert.ui.screens.incidents.ListScreen as IncidentListScreen
import com.example.hoodalert.ui.screens.incidents.IncidentDetailsDestination
import com.example.hoodalert.ui.screens.incidents.DetailsScreen as IncidentDetailsScreen
import com.example.hoodalert.ui.screens.incidents.IncidentEditDestination
import com.example.hoodalert.ui.screens.incidents.EditScreen as IncidentEditScreen
import com.example.hoodalert.ui.screens.incidents.IncidentEntryDestination
import com.example.hoodalert.ui.screens.incidents.EntryScreen as IncidentEntryScreen

@Composable
fun HoodAlertNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SignInDestination.route,
        modifier = modifier
    ) {
        composable(route = SignInDestination.route) {
            SignInScreen(
                context = navController.context,
                onSignInSuccess = { navController.navigate(DashboardDestination.route) },
                onRegister = { navController.navigate(RegisterDestination.route) }
            )
        }
        composable(route = RegisterDestination.route) {
            RegisterScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(route = DashboardDestination.route) {
            DashboardScreen(
                navController = navController,
                onNavUp = navController::navigateUp,
            )
        }
        composable(route = CommunityListDestination.route) {
            CommunityListScreen(
                navigateToCommunityEntry = { navController.navigate(CommunityEntryDestination.route) },
                navigateToCommunityUpdate = {
                    navController.navigate("${CommunityDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = CommunityEntryDestination.route) {
            CommunityEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = CommunityDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(CommunityDetailsDestination.communityIdArg) {
                type = NavType.IntType
            })
        ) {
            CommunityDetailsScreen(
                navigateToEditCommunity = { navController.navigate("${CommunityEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = CommunityEditDestination.routeWithArgs,
            arguments = listOf(navArgument(CommunityEditDestination.communityIdArg) {
                type = NavType.IntType
            })
        ) {
            CommunityEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(route = IncidentListDestination.route) {
            IncidentListScreen(
                navigateToIncidentEntry = { navController.navigate(IncidentEntryDestination.route) },
                navigateToIncidentUpdate = {
                    navController.navigate("${IncidentDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = IncidentEntryDestination.route) {
            IncidentEntryScreen(
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
            IncidentDetailsScreen(
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
            IncidentEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
