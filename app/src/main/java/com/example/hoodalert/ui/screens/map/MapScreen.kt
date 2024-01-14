package com.example.inventory.ui.map

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.R
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState

object MapDestination : NavigationDestination {
    override val route = "map"
    override val titleRes = R.string.map
}

/**
 * Entry route for Map screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val mapUiState by viewModel.mapUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(MapDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        MapBody(
            incidentList = mapUiState.incidentList
        )
    }
}

@Composable
private fun MapBody(
    incidentList: List<Incident>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = CameraPositionState(
                position = CameraPosition(LatLng(52.16, 5.31), 7f, 0f, 0f)
            ),
            properties = MapProperties(
                mapType = MapType.TERRAIN
            )
        ) {
            incidentList.forEach {
                if (it.latitude == null || it.longitude == null) {
                    return@forEach
                }

                MarkerInfoWindow(
                    state = MarkerState(position = LatLng(it.latitude!!, it.longitude!!))
                ) { marker ->
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp)
                            ),
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column {
                                Text(
                                    text = "Adres:",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = it.title,
                                    fontStyle = FontStyle.Italic
                                )
                                Text("${it.street} ${it.houseNumber}")
                                Text("${it.zipcode} ${it.city}")
                                Text(it.country)
                            }
                        }
                    }
                }

//                Marker(
//                    state = MarkerState(position = LatLng(it.latitude!!, it.longitude!!)),
//                    title = it.title,
//                    snippet = "Adres:\n${it.title}\n${it.street} ${it.houseNumber}\n${it.zipcode} ${it.city}\n${it.country}"
//                )
            }
        }
    }
}