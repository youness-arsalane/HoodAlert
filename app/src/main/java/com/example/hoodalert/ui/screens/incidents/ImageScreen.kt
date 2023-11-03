//package com.example.hoodalert.ui.screens.incidents
//
//import android.content.ContentResolver
//import android.content.ContentUris
//import android.content.Context
//import android.content.Intent
//import android.database.Cursor
//import android.net.Uri
//import android.provider.MediaStore
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Image
//import androidx.compose.material3.Icon
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.hoodalert.ImageEntity
//import kotlinx.coroutines.launch
//
//@Composable
//fun ImageList(
//    images: List<Uri>,
//    onImageClick: (Uri) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxSize()) {
//        for (image in images) {
//            Icon(
//                imageVector = Icons.Default.Image,
//                contentDescription = null,
//                modifier = Modifier.clickable { onImageClick(image) }
//            )
//        }
//    }
//}
//
//@Composable
//fun MyScreen(
//    viewModel: MyViewModel
//) {
//    var selectedImages by remember { mutableStateOf(emptyList<Uri>()) }
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    ImageList(images = selectedImages) { selectedImage ->
//        // Handle image click (e.g., show a larger view of the image)
//    }
//
//    Icon(
//        imageVector = Icons.Default.Add,
//        contentDescription = null,
//        modifier = Modifier.clickable(
//            onClick = {
//                // Launch the image selection intent
//                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                intent.type = "image/*"
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                context.startActivityForResult(intent) { result ->
//                    if (result.resultCode == Activity.RESULT_OK) {
//                        result.data?.clipData?.let { clipData ->
//                            val uris = (0 until clipData.itemCount).map { clipData.getItemAt(it).uri }
//                            selectedImages = uris
//                            // Save the selected images to the database
//                            coroutineScope.launch {
//                                viewModel.saveImagesToDatabase(uris)
//                            }
//                        }
//                    }
//                }
//            }
//        )
//    )
//}
//
//class MyViewModel(
//    private val database: MyDatabase
//) : ViewModel() {
//    fun saveImagesToDatabase(uris: List<Uri>) {
//        for (uri in uris) {
//            val imagePath = uri.toString()
//            val imageEntity = ImageEntity(imagePath = imagePath)
//            database.imageDao().insertImage(imageEntity)
//        }
//    }
//}
//
//fun Context.loadImagesFromGallery(): List<Uri> {
//    val images = mutableListOf<Uri>()
//    val contentResolver: ContentResolver = contentResolver
//    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//    val projection = arrayOf(
//        MediaStore.Images.Media._ID,
//        MediaStore.Images.Media.DISPLAY_NAME
//    )
//
//    val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
//    cursor?.use {
//        val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//        while (it.moveToNext()) {
//            val imageId = it.getLong(idColumn)
//            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId)
//            images.add(imageUri)
//        }
//    }
//    return images
//}
//
//@Composable
//fun MyApp() {
//    val context = LocalContext.current
//    val database = MyDatabase.getInstance(context)
//    val viewModel: MyViewModel = viewModel()
//    val imagesFromGallery = context.loadImagesFromGallery()
//
//    MyScreen(viewModel = viewModel)
//}
//
//@Composable
//fun ImageGalleryApp() {
//    CompositionLocalProvider(LocalViewModel provides MyViewModel(MyDatabase.getInstance(LocalContext.current))) {
//        MyApp()
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    ImageGalleryApp()
//}
