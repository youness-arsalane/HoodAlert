package com.example.hoodalert.ui.screens.incidents

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.R
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.theme.HoodAlertTheme
import com.example.hoodalert.ui.viewmodel.incidents.IncidentEntryViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentUiState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


object IncidentEntryDestination : NavigationDestination {
    override val route = "incident_entry"
    override val titleRes = R.string.incident_entry_title
    const val communityIdArg = "communityId"
    val routeWithArgs = "${route}/{$communityIdArg}"
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    loggedInUser: User?,
    canNavigateBack: Boolean = true,
    viewModel: IncidentEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    if (loggedInUser == null) {
        return
    }

    val context = LocalContext.current
    val activity = context as Activity
    val coroutineScope = rememberCoroutineScope()
    var coordinates: Pair<Double, Double>? by remember { mutableStateOf(null) }

    var locationPermissionsGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var shouldShowPermissionRationale by remember {
        mutableStateOf(
            activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    var shouldDirectUserToApplicationSettings by remember { mutableStateOf(false) }
    var shouldLaunchPermissionRationale by remember { mutableStateOf(false) }
    var retrieveLocation by remember { mutableStateOf(false) }

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            locationPermissionsGranted =
                permissions.values.reduce { acc, isPermissionGranted ->
                    acc && isPermissionGranted
                }

            if (!locationPermissionsGranted) {
                Toast.makeText(
                    context,
                    "Permission not granted",
                    Toast.LENGTH_SHORT
                ).show()

                shouldShowPermissionRationale =
                    activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            shouldDirectUserToApplicationSettings =
                !shouldShowPermissionRationale && !locationPermissionsGranted
        })

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START &&
                    !locationPermissionsGranted &&
                    !shouldShowPermissionRationale
                ) {
                    shouldLaunchPermissionRationale = true
//                    locationPermissionLauncher.launch(locationPermissions)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    if (shouldDirectUserToApplicationSettings) {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also {
            context.startActivity(it)
        }
    }

    if (retrieveLocation && locationPermissionsGranted) {
        retrieveLocation = false

        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val incidentUiState = viewModel.incidentUiState
                    val incidentDetails = incidentUiState.incident

                    viewModel.updateUiState(
                        incidentDetails.copy(
                            latitude = latitude,
                            longitude = longitude
                        )
                    )

                    coordinates = Pair(latitude, longitude)
                    coroutineScope.launch {
                        viewModel.loadAddressByCoordinates(
                            coordinates = coordinates!!
                        )
                    }
                }
            }
    }

    viewModel.incidentUiState.user = loggedInUser
    viewModel.incidentUiState.incident.userId = loggedInUser.id

    LaunchedEffect(viewModel.incidentUiState) {
        if (viewModel.incidentUiState.community != null) {
            viewModel.incidentUiState.incident.communityId =
                viewModel.incidentUiState.community!!.id
        }
    }

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
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveIncident()
                    navigateBack()
                }
            },
            onValueChange = { viewModel.updateUiState(it) },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            coordinates = coordinates,
            onLoadAddress = {
                retrieveLocation = true

                if (shouldLaunchPermissionRationale) {
                    locationPermissionLauncher.launch(locationPermissions)
                } else if (coordinates != null) {
                    try {
                        coroutineScope.launch {
                            viewModel.loadAddressByCoordinates(
                                coordinates = coordinates!!
                            )
                        }
                    } catch (e: Exception) {
//                        throw e
                        Toast.makeText(
                            context,
                            "Error: " + e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }
}

@Composable
fun EntryBody(
    incidentUiState: IncidentUiState,
    onSaveClick: () -> Unit,
    onValueChange: (Incident) -> Unit,
    modifier: Modifier = Modifier,
    coordinates: Pair<Double, Double>? = null,
    onLoadAddress: () -> Unit = {}
) {

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "Community: " + incidentUiState.community?.name.toString())

        val incident = incidentUiState.incident

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = incident.title,
                onValueChange = {
                    onValueChange(
                        incident.copy(title = it)
                    )
                },
                label = { Text(stringResource(R.string.title)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = incident.description,
                onValueChange = {
                    onValueChange(
                        incident.copy(description = it)
                    )
                },
                label = { Text(stringResource(R.string.description)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }

        Button(
            onClick = onLoadAddress
        ) {
            Text(text = "Load current address")
        }

        AddressFields(
            incident = incident,
            coordinates = coordinates,
            onValueChange = onValueChange
        )

        Text(
            text = stringResource(R.string.required_fields),
            modifier = Modifier.padding(start = 16.dp)
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

@Composable
fun AddressFields(
    incident: Incident,
    coordinates: Pair<Double, Double>? = null,
    onValueChange: (Incident) -> Unit = {}
) {
    Text(
        text = "Address",
        fontWeight = FontWeight.Bold
    )

    if (coordinates != null) {
        Text(text = "Coordinates: ${coordinates.first}, ${coordinates.second}")
    } else {
        Text(text = "Coordinates: unknown")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = incident.street,
            onValueChange = {
                onValueChange(
                    incident.copy(street = it)
                )
            },
            label = { Text(stringResource(R.string.street)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier
                .weight(1f / 12 * 8)
                .padding(end = 4.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = incident.houseNumber,
            onValueChange = {
                onValueChange(
                    incident.copy(houseNumber = it)
                )
            },
            label = { Text(stringResource(R.string.house_number_short)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier
                .weight(1f / 12 * 4)
                .padding(start = 4.dp),
            singleLine = true
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = incident.zipcode,
            onValueChange = {
                onValueChange(
                    incident.copy(zipcode = it)
                )
            },
            label = { Text(stringResource(R.string.zipcode)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier
                .weight(1f / 12 * 4)
                .padding(end = 4.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = incident.city,
            onValueChange = {
                onValueChange(
                    incident.copy(city = it)
                )
            },
            label = { Text(stringResource(R.string.city)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier
                .weight(1f / 12 * 8)
                .padding(start = 4.dp),
            singleLine = true
        )
    }
    OutlinedTextField(
        value = incident.country,
        onValueChange = {
            onValueChange(
                incident.copy(country = it)
            )
        },
        label = { Text(stringResource(R.string.country)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Preview
@Composable
fun EntryScreenPreview() {
    HoodAlertTheme {
        EntryBody(
            incidentUiState = IncidentUiState(
                incident = Incident(
                    id = 0,
                    communityId = 0,
                    userId = 0,
                    title = "",
                    description = "",
                    street = "",
                    houseNumber = "",
                    zipcode = "",
                    city = "",
                    country = "",
                    latitude = null,
                    longitude = null,
                    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                )
            ),
            onSaveClick = {},
            onValueChange = {}
//            navigateBack = {},
//            onNavigateUp = {},
//            loggedInUser = User(
//                id = 0,
//                email = "test@test.com",
//                firstName = "Test",
//                lastName = "Test",
//                password = "",
//                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
//                updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
//            )
        )
    }
}