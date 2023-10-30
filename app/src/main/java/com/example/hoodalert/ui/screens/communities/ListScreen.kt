package com.example.hoodalert.ui.screens.communities

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.R
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.theme.HoodAlertTheme
import com.example.hoodalert.ui.viewmodel.communities.CommunityListViewModel

object CommunityListDestination : NavigationDestination {
    override val route = "community_list"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(
    navigateToCommunityEntry: () -> Unit,
    navigateToCommunityUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val communityListUiState by viewModel.communityListUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(CommunityListDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCommunityEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.community_entry_title)
                )
            }
        },
    ) { innerPadding ->
        ListBody(
            communityList = communityListUiState.communityList,
            onCommunityClick = navigateToCommunityUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun ListBody(
    communityList: List<Community>, onCommunityClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (communityList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_community_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            HoodAlertList(
                communityList = communityList,
                onCommunityClick = { onCommunityClick(it.id) },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun HoodAlertList(
    communityList: List<Community>,
    onCommunityClick: (Community) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = communityList, key = { it.id }) { community ->
            HoodAlertCommunity(community = community,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onCommunityClick(community) })
        }
    }
}

@Composable
private fun HoodAlertCommunity(
    community: Community, modifier: Modifier = Modifier
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
                    text = community.name,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListBodyEmptyListPreview() {
    HoodAlertTheme {
        ListBody(listOf(), onCommunityClick = {})
    }
}
