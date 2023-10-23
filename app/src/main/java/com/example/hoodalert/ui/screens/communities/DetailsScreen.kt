package com.example.hoodalert.ui.screens.communities

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
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.viewmodel.communities.CommunityDetailsUiState
import com.example.hoodalert.ui.viewmodel.communities.CommunityDetailsViewModel
import com.example.hoodalert.ui.viewmodel.communities.toCommunity
import kotlinx.coroutines.launch

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
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(CommunityDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditCommunity(uiState.value.communityDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_community_title),
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        DetailsBody(
            communityDetailsUiState = uiState.value,
            onJoinCommunity = { },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteCommunity()
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
    communityDetailsUiState: CommunityDetailsUiState,
    onJoinCommunity: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        Details(
            community = communityDetailsUiState.communityDetails.toCommunity(), modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onJoinCommunity,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = !communityDetailsUiState.outOfStock
        ) {
            Text(stringResource(R.string.join))
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
    community: Community, modifier: Modifier = Modifier
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
                communityDetail = community.name,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
        }

    }
}

@Composable
private fun DetailsRow(
    @StringRes labelResID: Int, communityDetail: String, modifier: Modifier = Modifier
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

//@Preview(showBackground = true)
//@Composable
//fun DetailsScreenPreview() {
//    HoodAlertTheme {
//        CommunityDetailsBody(CommunityDetailsUiState(
//            outOfStock = true,
//            communityDetails = CommunityDetails(1, "Pen", "$100", "10")
//        ), onJoinCommunity = {}, onDelete = {})
//    }
//}
