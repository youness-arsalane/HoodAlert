package com.example.hoodalert.ui.screens.communities

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
import com.example.hoodalert.ui.viewmodel.communities.CommunityDetails
import com.example.hoodalert.ui.viewmodel.communities.CommunityEntryViewModel
import com.example.hoodalert.ui.viewmodel.communities.CommunityUiState
import kotlinx.coroutines.launch

object CommunityEntryDestination : NavigationDestination {
    override val route = "community_entry"
    override val titleRes = R.string.community_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: CommunityEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(CommunityEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        EntryBody(
            communityUiState = viewModel.communityUiState,
            onCommunityValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveCommunity()
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
    communityUiState: CommunityUiState,
    onCommunityValueChange: (CommunityDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        InputForm(
            communityDetails = communityUiState.communityDetails,
            onValueChange = onCommunityValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = communityUiState.isEntryValid,
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
    communityDetails: CommunityDetails,
    modifier: Modifier = Modifier,
    onValueChange: (CommunityDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = communityDetails.name,
            onValueChange = { onValueChange(communityDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.name)) },
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