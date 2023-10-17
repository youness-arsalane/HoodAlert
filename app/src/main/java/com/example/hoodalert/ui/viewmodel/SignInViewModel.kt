package com.example.hoodalert.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.repository.UsersRepository

class SignInViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    fun signIn(
        email: String,
        password: String,
        onSignInComplete: () -> Unit,
    ) {
        if (usersRepository.signIn(email, password)) {
            onSignInComplete()
        }
    }
}
