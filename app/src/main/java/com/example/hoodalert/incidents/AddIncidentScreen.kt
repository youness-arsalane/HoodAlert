package com.example.hoodalert.incidents

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hoodalert.Destinations
import com.example.hoodalert.MainActivity
import com.example.hoodalert.R
import com.example.hoodalert.signin.EmailState
import com.example.hoodalert.signin.TextFieldError
import com.example.hoodalert.signin.TextFieldState
import com.example.hoodalert.theme.HoodAlertTheme
import com.example.hoodalert.util.DbHelper
import com.example.hoodalert.util.supportWideScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncidentScreen(
    context: Context,
    navController: NavController,
    onNavUp: () -> Unit
) {
    Scaffold(
        topBar = {
            AddIncidentTopAppBar(
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TitleField()
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleField(
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = {
            Text(
                text = stringResource(id = R.string.title),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncidentTopAppBar(
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
                    imageVector = Icons.Filled.Save,
                    contentDescription = stringResource(id = R.string.save),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
    )
}

@Preview(name = "Incidents")
@Composable
fun AddIncidentScreenPreview() {
    val context = MainActivity();

    HoodAlertTheme {
        AddIncidentScreen(
            context = context,
            navController = NavController(context),
            onNavUp = {}
        )
    }
}
