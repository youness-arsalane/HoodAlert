package com.example.hoodalert.data.repository

import android.content.Context
import com.example.hoodalert.data.auth.SharedPreferencesManager
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.retrofit.RetrofitBuilder
import com.example.hoodalert.data.retrofit.UserSessionsApiService
import com.example.hoodalert.data.retrofit.UsersApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsersRepository(private val usersApiService: UsersApiService, private val context: Context) {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _userDetails = MutableStateFlow<User?>(null)
    val userDetails: StateFlow<User?> = _userDetails.asStateFlow()

    suspend fun getUsers(): List<User> {
        val users = usersApiService.getUsers()
        _users.value = users
        return users
    }

    suspend fun getUser(id: Int): User {
        val user = usersApiService.getUser(id)
        _userDetails.value = user
        return user
    }

    suspend fun getUserByEmail(email: String): User {
        val user = usersApiService.getUserByEmail(email)
        _userDetails.value = user
        return user
    }

    suspend fun insertUser(user: User): User {
        val result = usersApiService.insertUser(user)
        _users.value = _users.value + result
        _userDetails.value = result
        return result
    }

    suspend fun updateUser(user: User): User {
        val result = usersApiService.updateUser(user.id, user)
        _users.value = _users.value.map { if (it.id == result.id) result else it }
        _userDetails.value = result
        return result
    }

    suspend fun deleteUser(user: User) {
        usersApiService.deleteUser(user.id)
        _users.value = _users.value.filter { it.id != user.id }
    }

    suspend fun getLoggedInUser(): User {
        val sharedPreferencesManager = SharedPreferencesManager(context)
        val token: String = sharedPreferencesManager.getUserToken().toString()

        val userSessionsApiService: UserSessionsApiService = RetrofitBuilder.userSessionsApiService
        val userApiService: UsersApiService = RetrofitBuilder.usersApiService

        var loggedInUser: User? = null

        val userSession = userSessionsApiService.getUserSessionByToken(token)
        if (userSession !== null) {
            loggedInUser = userApiService.getUser(userSession.userId)
        }

        if (loggedInUser === null) {
            throw Exception("User is not logged in!")
        }

        return loggedInUser
    }

    fun logout() {
        val sharedPreferencesManager = SharedPreferencesManager(context)
        sharedPreferencesManager.saveUserToken("")
    }
}
