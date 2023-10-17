package com.example.hoodalert.ui.navigation.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoodalert.ui.screens.SignInScreen
import com.example.hoodalert.ui.viewmodel.SignInViewModel
import com.example.hoodalert.ui.viewmodel.SignInViewModelFactory

@Composable
fun SignInRoute(
    onSignInSubmitted: () -> Unit
) {
    val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory())
    SignInScreen(
        onSignInSubmitted = { email, password ->
            signInViewModel.signIn(email, password, onSignInSubmitted)
        }
    )
}
