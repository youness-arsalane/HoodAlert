package com.example.hoodalert.incidents

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hoodalert.Destinations
import com.example.hoodalert.MainActivity
import com.example.hoodalert.R
import com.example.hoodalert.theme.HoodAlertTheme
import com.example.hoodalert.util.DbHelper
import com.example.hoodalert.util.supportWideScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentsScreen(
    context: Context,
    navController: NavController,
    onNavUp: () -> Unit
) {
    Scaffold(
        topBar = {
            IncidentsTopAppBar(
                navController = navController,
                topAppBarText = stringResource(id = R.string.incidents),
                onNavUp = onNavUp
            )
        },
        content = { contentPadding ->
            LazyColumn(
                modifier = Modifier.supportWideScreen(),
                contentPadding = contentPadding
            ) {
                item {
                    IncidentsTable(context)
                }
            }
        }
    )
}

@SuppressLint("Range")
@Composable
fun IncidentsTable(context: Context) {
    val db = DbHelper(context).readableDatabase

    val cursor = db.query("incident", null, null, null, null, null, null)
    while (cursor.moveToNext()) {
        val id = cursor.getLong(cursor.getColumnIndex("id"))
        val title = cursor.getString(cursor.getColumnIndex("title"))

        Text(text = "${id}: ${title}")
    }

    cursor.close()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentsTopAppBar(
    navController: NavController,
    topAppBarText: String,
    onNavUp: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = topAppBarText,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavUp) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
//            Spacer(modifier = Modifier.width(68.dp))
            IconButton(onClick = { navController.navigate(Destinations.INCIDENTS_ROUTE) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
    )
}

@Preview(name = "Incidents")
@Composable
fun IncidentsScreenPreview() {
    val context = MainActivity();

    HoodAlertTheme {
        IncidentsScreen(
            context = context,
            navController = NavController(context),
            onNavUp = {}
        )
    }
}
