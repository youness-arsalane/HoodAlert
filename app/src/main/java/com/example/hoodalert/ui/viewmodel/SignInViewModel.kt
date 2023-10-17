package com.example.hoodalert.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hoodalert.data.repository.UserRepository

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun signIn(
        email: String,
        password: String,
        onSignInComplete: () -> Unit,
    ) {
        userRepository.signIn(email, password)
        onSignInComplete()
    }
}

class SignInViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
