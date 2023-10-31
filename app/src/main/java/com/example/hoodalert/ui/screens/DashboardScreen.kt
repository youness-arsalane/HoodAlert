package com.example.hoodalert.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hoodalert.R
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.screens.communities.CommunityListDestination
import com.example.hoodalert.ui.screens.incidents.IncidentListDestination
import com.example.hoodalert.ui.theme.HoodAlertTheme
import com.example.hoodalert.util.supportWideScreen
import java.util.Date

object DashboardDestination : NavigationDestination {
    override val route = "dashboard"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController? = null,
    loggedInUser: User?,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(id = R.string.dashboard),
                canNavigateBack = false,
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = stringResource(id = R.string.back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        content = { contentPadding ->
            LazyColumn(
                modifier = Modifier.supportWideScreen(),
                contentPadding = contentPadding
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Welcome, " + loggedInUser?.firstName + "!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CenteredCard(
                                text = stringResource(R.string.communities),
                                onClick = {
                                    navController?.navigate(CommunityListDestination.route)
                                },
                                modifier = Modifier.weight(1f)
                            )
//                            CenteredCard(
//                                text = stringResource(R.string.incidents),
//                                onClick = {
//                                    navController?.navigate(IncidentListDestination.route)
//                                },
//                                modifier = Modifier.weight(1f)
//                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CenteredCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .height(96.dp)
                .clickable {
                    onClick()
                }
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    HoodAlertTheme {
        DashboardScreen(
            loggedInUser = User(
                id = 0,
                email = "test@test.com",
                firstName = "Test",
                lastName = "Test",
                password = "",
                createdAt = Date(),
                updatedAt = Date(),
            )
        ) {}
    }
}