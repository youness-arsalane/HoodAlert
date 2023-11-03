package com.example.hoodalert.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.model.UserSession
import java.util.Date

class LoginViewModel(private val appContainer: AppDataContainer) : ViewModel() {
    suspend fun login(
        email: String,
        password: String,
        onLoginSuccess: (user: User) -> Unit,
        onLoginFailed: () -> Unit = {},
    ) {

        appContainer.usersRepository.getUserByEmail(email).collect { user ->
            if (user != null && user.password == password) {
                onLoginSuccess(user)
            } else {
                onLoginFailed()
            }
        }
    }

    fun logout() {
        appContainer.usersRepository.logout()
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

        appContainer.userSessionsRepository.insertUserSession(userSession)
    }

    suspend fun getLoggedInUser(): User = appContainer.usersRepository.getLoggedInUser()
}
