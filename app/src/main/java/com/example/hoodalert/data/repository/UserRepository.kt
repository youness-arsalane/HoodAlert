package com.example.hoodalert.data.repository

import androidx.compose.runtime.Immutable

sealed class User {
    @Immutable
    data class LoggedInUser(val email: String) : User()
    object NoUserLoggedIn : User()
}

object UserRepository {

    private var _user: User = User.NoUserLoggedIn
    val user: User
        get() = _user

    @Suppress("UNUSED_PARAMETER")
    fun signIn(email: String, password: String) {
        _user = User.LoggedInUser(email)
    }
}
