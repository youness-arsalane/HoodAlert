package com.example.hoodalert.ui.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.model.UserSession
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LoginViewModel(private val appContainer: AppDataContainer) : ViewModel() {
    suspend fun login(
        email: String,
        password: String,
        onLoginSuccess: (user: User) -> Unit,
        onLoginFailed: () -> Unit = {},
    ) {
        val user = appContainer.usersRepository.getUserByEmail(email)

        if (user != null && user.password == password) {
            onLoginSuccess(user)
        } else {
            onLoginFailed()
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
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )

        appContainer.userSessionsRepository.insertUserSession(userSession)
    }

    suspend fun getLoggedInUser(): User = appContainer.usersRepository.getLoggedInUser()
}
