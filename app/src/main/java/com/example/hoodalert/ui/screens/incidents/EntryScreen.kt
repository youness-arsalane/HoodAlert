package com.example.hoodalert.ui.screens.incidents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.R
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.viewmodel.incidents.IncidentDetails
import com.example.hoodalert.ui.viewmodel.incidents.IncidentEntryViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentUiState
import kotlinx.coroutines.launch

object IncidentEntryDestination : NavigationDestination {
    override val route = "incident_entry"
    override val titleRes = R.string.incident_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: IncidentEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(IncidentEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        EntryBody(
            incidentUiState = viewModel.incidentUiState,
            onIncidentValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveIncident()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun EntryBody(
    incidentUiState: IncidentUiState,
    onIncidentValueChange: (IncidentDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        InputForm(
            incidentDetails = incidentUiState.incidentDetails,
            onValueChange = onIncidentValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = incidentUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputForm(
    incidentDetails: IncidentDetails,
    modifier: Modifier = Modifier,
    onValueChange: (IncidentDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = incidentDetails.title,
            onValueChange = { onValueChange(incidentDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.title)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = incidentDetails.description,
            onValueChange = { onValueChange(incidentDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.description)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}