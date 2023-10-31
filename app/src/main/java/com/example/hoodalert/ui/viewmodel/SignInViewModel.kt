package com.example.hoodalert.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.model.UserSession
import com.example.hoodalert.data.repository.UserSessionsRepository
import com.example.hoodalert.data.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date

class SignInViewModel(
    private val usersRepository: UsersRepository,
    private val userSessionsRepository: UserSessionsRepository
) : ViewModel() {
    suspend fun signIn(
        email: String,
        password: String,
        onSignInSuccess: (user: User) -> Unit,
        onSignInFailed: () -> Unit = {},
    ) {

        usersRepository.getUserByEmail(email).collect { user ->
            if (user != null && user.password == password) {
                onSignInSuccess(user)
            } else {
                onSignInFailed();
            }
        }
    }

    suspend fun saveSession(
        user: User,
        token: String
    ) {
        val userSession = UserSession(
            id = 0,
            userId = user.id,
            token = token,
            createdAt = Date(),
            updatedAt = Date()
        )

        userSessionsRepository.insertUserSession(userSession)
    }

    suspend fun getLoggedInUser() : User? = usersRepository.getLoggedInUser()
}
