package com.example.hoodalert.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.screens.DashboardDestination
import com.example.hoodalert.ui.screens.DashboardScreen
import com.example.hoodalert.ui.screens.LoginDestination
import com.example.hoodalert.ui.screens.LoginScreen
import com.example.hoodalert.ui.screens.RegisterDestination
import com.example.hoodalert.ui.screens.RegisterScreen
import com.example.hoodalert.ui.screens.communities.CommunityDetailsDestination
import com.example.hoodalert.ui.screens.communities.CommunityEditDestination
import com.example.hoodalert.ui.screens.communities.CommunityEntryDestination
import com.example.hoodalert.ui.screens.communities.CommunityListDestination
import com.example.hoodalert.ui.screens.incidents.IncidentDetailsDestination
import com.example.hoodalert.ui.screens.incidents.IncidentEditDestination
import com.example.hoodalert.ui.screens.incidents.IncidentEntryDestination
import com.example.hoodalert.ui.screens.incidents.IncidentListDestination
import com.example.hoodalert.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import com.example.hoodalert.ui.screens.communities.DetailsScreen as CommunityDetailsScreen
import com.example.hoodalert.ui.screens.communities.EditScreen as CommunityEditScreen
import com.example.hoodalert.ui.screens.communities.EntryScreen as CommunityEntryScreen
import com.example.hoodalert.ui.screens.communities.ListScreen as CommunityListScreen
import com.example.hoodalert.ui.screens.incidents.DetailsScreen as IncidentDetailsScreen
import com.example.hoodalert.ui.screens.incidents.EditScreen as IncidentEditScreen
import com.example.hoodalert.ui.screens.incidents.EntryScreen as IncidentEntryScreen
import com.example.hoodalert.ui.screens.incidents.ListScreen as IncidentListScreen

@Composable
fun HoodAlertNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        var loggedInUser: User? = null
        loginViewModel.viewModelScope.launch {
            try {
                loggedInUser = loginViewModel.getLoggedInUser()
                navController.navigate(DashboardDestination.route)
            } catch (_: Exception) {
            }
        }

        composable(route = LoginDestination.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { navController.navigate(DashboardDestination.route) },
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
                loggedInUser = loggedInUser,
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate(LoginDestination.route)
                },
            )
        }
        composable(route = CommunityListDestination.route) {
            CommunityListScreen(
                loggedInUser = loggedInUser,
                navigateToCommunityEntry = { navController.navigate(CommunityEntryDestination.route) },
                navigateToCommunityUpdate = {
                    navController.navigate("${CommunityDetailsDestination.route}/${it}")
                },
                onNavigateUp = { navController.navigateUp() }
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
            val communityId = it.arguments?.getInt(CommunityDetailsDestination.communityIdArg) ?: 0

            CommunityDetailsScreen(
                navigateToEditCommunity = { navController.navigate("${CommunityEditDestination.route}/$it") },
                navigateToIncidentEntry = { navController.navigate("${IncidentEntryDestination.route}/$communityId") },
                navigateToIncidentUpdate = { navController.navigate("${IncidentDetailsDestination.route}/${it}") },
                loggedInUser = loggedInUser,
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
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = IncidentEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(IncidentEntryDestination.communityIdArg) {
                type = NavType.IntType
            })
        ) {
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
