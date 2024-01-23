package com.example.hoodalert.ui.screens.incidents

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.R
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.viewmodel.incidents.IncidentDetailsUiState
import com.example.hoodalert.ui.viewmodel.incidents.IncidentDetailsViewModel
import com.example.hoodalert.ui.viewmodel.incidents.getFullName
import kotlinx.coroutines.launch

object IncidentDetailsDestination : NavigationDestination {
    override val route = "incident_details"
    override val titleRes = R.string.incident_detail_title
    const val communityIdArg = "communityId"
    const val incidentIdArg = "incidentId"
    val routeWithArgs = "$route/{$communityIdArg}/{$incidentIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigateToEditIncident: (communityId: Int, incidentId: Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IncidentDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(IncidentDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditIncident(uiState.value.community!!.id, uiState.value.incident.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_incident_title),
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        DetailsBody(
            incidentDetailsUiState = uiState.value,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteIncident()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun DetailsBody(
    incidentDetailsUiState: IncidentDetailsUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        Details(
            incidentDetailsUiState = incidentDetailsUiState,
            incident = incidentDetailsUiState.incident,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun Details(
    incidentDetailsUiState: IncidentDetailsUiState,
    incident: Incident,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailsRow(
                labelResID = R.string.community,
                incidentDetail = incidentDetailsUiState.community?.name.toString(),
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.user,
                incidentDetail = incidentDetailsUiState.user?.getFullName().toString(),
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.incident,
                incidentDetail = incident.title,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.description,
                incidentDetail = incident.description,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.street,
                incidentDetail = incident.street,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.house_number,
                incidentDetail = incident.houseNumber,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.zipcode,
                incidentDetail = incident.zipcode,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.city,
                incidentDetail = incident.city,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.country,
                incidentDetail = incident.country,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.latitude,
                incidentDetail = if (incident.latitude != null) "%.6f".format(incident.latitude!! / 1.0)
                else "Unknown",
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.longitude,
                incidentDetail = if (incident.longitude != null) "%.6f".format(incident.longitude!! / 1.0)
                else "Unknown",
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
        }

    }
}

@Composable
private fun DetailsRow(
    @StringRes labelResID: Int, incidentDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = incidentDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        })
}