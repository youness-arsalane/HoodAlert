package com.example.hoodalert.ui.screens.communities

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.R
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.theme.HoodAlertTheme
import com.example.hoodalert.ui.viewmodel.communities.CommunityDetailsUiState
import com.example.hoodalert.ui.viewmodel.communities.CommunityDetailsViewModel
import com.example.hoodalert.ui.viewmodel.communities.getIncidents
import com.example.hoodalert.ui.viewmodel.communities.toCommunity
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object CommunityDetailsDestination : NavigationDestination {
    override val route = "community_details"
    override val titleRes = R.string.community_detail_title
    const val communityIdArg = "communityId"
    val routeWithArgs = "$route/{$communityIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigateToEditCommunity: (Int) -> Unit,
    navigateToIncidentEntry: () -> Unit,
    navigateToIncidentUpdate: (Int) -> Unit,
    loggedInUser: User?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    if (loggedInUser === null) {
        return
    }

    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var communityUser by remember { mutableStateOf<CommunityUser?>(null) }

    val community = uiState.value.communityDetails.toCommunity()

    LaunchedEffect(community) {
        communityUser = viewModel.findCommunityUser(loggedInUser)
    }

    Scaffold(
        topBar = {
            HoodAlertTopAppBar(
                title = community.name,
                canNavigateBack = true,
                navigateUp = navigateBack,
                actions = {
                    if (communityUser != null && communityUser!!.isAdmin) {
                        IconButton(onClick = { navigateToEditCommunity(uiState.value.communityDetails.id) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_community_title)
                            )
                        }
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.deleteCommunity()
                                    navigateBack()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(id = R.string.delete),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToIncidentEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.incident_entry_title)
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        DetailsBody(
            viewModel = viewModel,
            navigateToIncidentUpdate = navigateToIncidentUpdate,
            communityDetailsUiState = uiState.value,
            modifier = Modifier
                .padding(innerPadding),
            communityUser = communityUser,
            onJoinCommunity = {
                val newCommunityUser = CommunityUser(
                    id = 0,
                    communityId = uiState.value.communityDetails.toCommunity().id,
                    userId = loggedInUser.id,
                    isAdmin = false,
                    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                )

                coroutineScope.launch {
                    viewModel.insertCommunityUser(newCommunityUser)
                    navigateBack()
                }
            },
            onLeaveCommunity = {
                coroutineScope.launch {
                    viewModel.deleteCommunityUser(communityUser!!)
                    navigateBack()
                }
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteCommunity()
                    navigateBack()
                }
            }
        )
    }
}

@Composable
private fun DetailsBody(
    viewModel: CommunityDetailsViewModel,
    navigateToIncidentUpdate: (Int) -> Unit,
    communityDetailsUiState: CommunityDetailsUiState,
    modifier: Modifier = Modifier,
    communityUser: CommunityUser?,
    onJoinCommunity: () -> Unit,
    onLeaveCommunity: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        val community = communityDetailsUiState.communityDetails.toCommunity()

        var incidentList by remember { mutableStateOf(emptyList<Incident>()) }
        LaunchedEffect(community) {
            incidentList = community.getIncidents(viewModel)
        }

        if (communityUser != null) {
            Button(
                onClick = onLeaveCommunity,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(stringResource(R.string.leave))
            }
        } else {
            Button(
                onClick = onJoinCommunity,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(stringResource(R.string.join))
            }
        }

        ListBody(
            incidentList = incidentList,
            onIncidentClick = navigateToIncidentUpdate,
            modifier = modifier
                .fillMaxSize()
        )

        if (communityUser != null && communityUser.isAdmin && deleteConfirmationRequired) {
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
private fun ListBody(
    incidentList: List<Incident>, onIncidentClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (incidentList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_incident_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            IncidentList(
                incidentList = incidentList,
                onIncidentClick = { onIncidentClick(it.id) },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun IncidentList(
    incidentList: List<Incident>, onIncidentClick: (Incident) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = incidentList, key = { it.id }) { incident ->
            Incident(incident = incident,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onIncidentClick(incident) })
        }
    }
}

@Composable
private fun Incident(
    incident: Incident,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = incident.title,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Text(text = incident.description)
        }
    }
}

@Composable
private fun DetailsRow(
    @StringRes labelResID: Int,
    communityDetail: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = communityDetail, fontWeight = FontWeight.Bold)
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

@Preview
@Composable
fun DetailsScreenPreview() {
    HoodAlertTheme {
        DetailsScreen(
            navigateToEditCommunity = {},
            navigateToIncidentEntry = {},
            navigateToIncidentUpdate = {},
            loggedInUser = User(
                id = 0,
                email = "test@test.com",
                firstName = "Test",
                lastName = "Test",
                password = "",
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            ),
            navigateBack = { /*TODO*/ })
    }
}