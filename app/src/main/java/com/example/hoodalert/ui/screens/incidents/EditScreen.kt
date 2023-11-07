package com.example.hoodalert.ui.screens.incidents

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.R
import com.example.hoodalert.ui.AppViewModelProvider
import com.example.hoodalert.ui.components.HoodAlertTopAppBar
import com.example.hoodalert.ui.navigation.NavigationDestination
import com.example.hoodalert.ui.theme.HoodAlertTheme
import com.example.hoodalert.ui.viewmodel.incidents.IncidentEditViewModel
import kotlinx.coroutines.launch

object IncidentEditDestination : NavigationDestination {
    override val route = "incident_edit"
    override val titleRes = R.string.edit_incident_title
    const val incidentIdArg = "incidentId"
    val routeWithArgs = "$route/{$incidentIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IncidentEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HoodAlertTopAppBar(
                title = stringResource(IncidentEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        EntryBody(
            incidentUiState = viewModel.incidentUiState,
            onIncidentValueChange = { viewModel.updateUiState(it) },
            showImages = true,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateIncident()
                    navigateBack()
                }
            },
            onAddImage = { uri ->
                Log.d("HOOD_ALERT_DEBUG", "EditScreen onAddImage")
                Log.d("HOOD_ALERT_DEBUG", "EditScreen uri + $uri")
                coroutineScope.launch {
                    Log.d("HOOD_ALERT_DEBUG", "EditScreen coroutineScope.launch")
                    viewModel.addIncidentImage(uri)
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun ImageSelectionScreen(
    onAddImage: (Uri) -> Unit = {}
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

//    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            selectedImageUri = uri
//        }

    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("HOOD_ALERT_DEBUG", "rememberLauncherForActivityResult 1 + $result")
            Log.d("HOOD_ALERT_DEBUG", "rememberLauncherForActivityResult 2 + " + result.resultCode.toString())
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                val data: Intent? = result.data
                Log.d("HOOD_ALERT_DEBUG", "rememberLauncherForActivityResult 3 + $data")
                Log.d("HOOD_ALERT_DEBUG", "rememberLauncherForActivityResult 4 + ${data?.data}")
                data?.data?.let { uri ->
                    Log.d("HOOD_ALERT_DEBUG", "rememberLauncherForActivityResult 5 + $uri")
                    selectedImageUri = uri
                }
            }
        }

    LaunchedEffect(selectedImageUri) {
        Log.d("HOOD_ALERT_DEBUG", "LaunchedEffect")
        if (selectedImageUri != null) {
            Log.d("HOOD_ALERT_DEBUG", "onAddImage")
            Log.d("HOOD_ALERT_DEBUG", "selectedImageUri + " + selectedImageUri.toString())
            onAddImage(selectedImageUri!!)
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                getContent.launch(intent)
//                getContent.launch("image/*")
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.select_image))
        }
    }
}
