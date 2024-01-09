package com.example.hoodalert.ui.screens.incidents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.R
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.viewmodel.incidents.IncidentEditViewModel
import kotlinx.coroutines.launch

object IncidentEditDestination : NavigationDestination {
    override val route = "incident_edit"
    override val titleRes = R.string.edit_incident_title
    const val incidentIdArg = "incidentId"
    val routeWithArgs = "$route/{$incidentIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IncidentEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(IncidentEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        EntryBody(
            incidentUiState = viewModel.incidentUiState,
            onIncidentValueChange = { viewModel.updateUiState(it) },
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateIncident()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}