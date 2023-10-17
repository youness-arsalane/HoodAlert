package com.example.hoodalert.signin

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignInRoute(
    context: Context,
    onSignInSubmitted: () -> Unit
) {
    val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory())
    SignInScreen(
        context = context,
        onSignInSubmitted = { email, password ->
            signInViewModel.signIn(email, password, onSignInSubmitted)
        }
    )
}
