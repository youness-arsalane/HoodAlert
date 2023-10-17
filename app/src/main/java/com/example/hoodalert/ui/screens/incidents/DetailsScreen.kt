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
import androidx.compose.material3.Button
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
import com.example.hoodalert.ui.viewmodel.incidents.toIncident
import kotlinx.coroutines.launch

object IncidentDetailsDestination : NavigationDestination {
    override val route = "incident_details"
    override val titleRes = R.string.incident_detail_title
    const val incidentIdArg = "incidentId"
    val routeWithArgs = "$route/{$incidentIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigateToEditIncident: (Int) -> Unit,
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
                onClick = { navigateToEditIncident(uiState.value.incidentDetails.id) },
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
            onSellIncident = { },
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
    onSellIncident: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        Details(
            incident = incidentDetailsUiState.incidentDetails.toIncident(), modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSellIncident,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = !incidentDetailsUiState.outOfStock
        ) {
            Text(stringResource(R.string.sell))
        }
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
    incident: Incident, modifier: Modifier = Modifier
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

//@Preview(showBackground = true)
//@Composable
//fun DetailsScreenPreview() {
//    HoodAlertTheme {
//        IncidentDetailsBody(IncidentDetailsUiState(
//            outOfStock = true,
//            incidentDetails = IncidentDetails(1, "Pen", "$100", "10")
//        ), onSellIncident = {}, onDelete = {})
//    }
//}
