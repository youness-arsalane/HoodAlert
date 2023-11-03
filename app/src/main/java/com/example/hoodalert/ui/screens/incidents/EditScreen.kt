package com.example.hoodalert.ui.screens.incidents

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
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
import com.example.hoodalert.data.model.Incident
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
            viewModel = viewModel,
            incidentUiState = viewModel.incidentUiState,
            onIncidentValueChange = { viewModel.updateUiState(it) },
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateIncident()
                    navigateBack()
                }
            },
            onImageAdded = {
                navigateBack()
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ImageSelectionScreen(
    incident: Incident,
    viewModel: IncidentEditViewModel,
    onImageAdded: () -> Unit = {}
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    selectedImageUri = uri
                }
            }
        }

    LaunchedEffect(selectedImageUri) {
        if (selectedImageUri != null) {
            viewModel.addIncidentImage(incident, selectedImageUri!!)
            onImageAdded()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Button(
            onClick = {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                getContent.launch(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.select_image))
        }
    }
}


//@Composable
//fun ImageCapture() {
//    val imageCapture = ImageCapture.Builder()
//        .build()
//
//    val captureButton = remember { mutableStateOf(false) }
//
//    val context = LocalContext.current
//
//
//    Button(
//        onClick = {
//            val imageCaptureOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()
//            imageCapture.takePicture(
//                imageCaptureOptions,
//                ContextCompat.getMainExecutor(context),
//                object : ImageCapture.OnImageSavedCallback {
//                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                        // De afbeelding is succesvol opgeslagen
//                    }
//
//                    override fun onError(exception: ImageCaptureException) {
//                        // Er is een fout opgetreden bij het opslaan van de afbeelding
//                    }
//                }
//            )
//        }
//    ) {
//        Text("Capture")
//    }
//}
//
//@Composable
//fun uploadImage() {
//    val context = LocalContext.current
//
//    val previewView = rememberUpdatedState(PreviewView(context))
//
//    val imageCapture = ImageCapture.Builder()
//        .build()
//
//    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//
//    cameraProviderFuture.addListener({
//        val cameraProvider = cameraProviderFuture.get()
//
//        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//        val camera = cameraProvider.bindToLifecycle(
//            this,
//            cameraSelector,
//            previewView.preview,
//            imageCapture
//        )
//    }, ContextCompat.getMainExecutor(context))
//
//}

@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {
    HoodAlertTheme {
        EditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}
