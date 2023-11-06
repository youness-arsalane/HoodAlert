package com.example.hoodalert.ui.screens.incidents

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.hoodalert.R
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.IncidentImage
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.viewmodel.incidents.IncidentDetailsUiState
import com.example.hoodalert.ui.viewmodel.incidents.IncidentDetailsViewModel
import com.example.hoodalert.ui.viewmodel.incidents.getFullName
import com.example.hoodalert.ui.viewmodel.incidents.getIncidentImages
import com.example.hoodalert.ui.viewmodel.incidents.toIncident
import kotlinx.coroutines.launch
import java.io.IOException

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

    val incident = uiState.value.incidentDetails.toIncident()

    var incidentImageList by remember { mutableStateOf(emptyList<IncidentImage>()) }
    LaunchedEffect(incident) {
        incidentImageList = incident.getIncidentImages(viewModel)
    }

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
            incidentImageList = incidentImageList,
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
    incidentImageList: List<IncidentImage>,
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
            incidentImageList = incidentImageList,
            incidentDetailsUiState = incidentDetailsUiState,
            incident = incidentDetailsUiState.incidentDetails.toIncident(),
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
    incidentImageList: List<IncidentImage>,
    incidentDetailsUiState: IncidentDetailsUiState,
    incident: Incident,
    modifier: Modifier = Modifier
) {
    ImageSlider(incidentImageList)

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
                labelResID = R.string.latitude,
                incidentDetail = if (incident.latitude != null) "%.6f".format(incident.latitude!! / 1.0)
                else "Niet bekend",
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            DetailsRow(
                labelResID = R.string.longitude,
                incidentDetail = if (incident.longitude != null) "%.6f".format(incident.longitude!! / 1.0)
                else "Niet bekend",
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
        }

    }
}

@Composable
fun ImageSlider(incidentImageList: List<IncidentImage>)
{
    val context = LocalContext.current
    val density = LocalDensity.current.density

    Log.d("HOOD_ALERT_DEBUG", "ImageCount: " + incidentImageList.count().toString())

    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp)
            .background(Color.Gray)
    ) {
        items(items = incidentImageList, key = { it.id }) { incidentImage ->
            Log.d("HOOD_ALERT_DEBUG", "path: " + incidentImage.path)
            Log.d("HOOD_ALERT_DEBUG", "uri: " + incidentImage.path.toUri().toString())
            val bitmap = loadBitmap(context.contentResolver, incidentImage.path.toUri())
            if (bitmap != null) {
                Log.d("HOOD_ALERT_DEBUG", "bitmap not empty")

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(4.dp)
                        .width((100 * density).dp)
                        .height((100 * density).dp)
                )
            } else {
                Log.d("HOOD_ALERT_DEBUG", "bitmap empty")
            }
        }
    }
}

@Composable
fun loadBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        null
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
