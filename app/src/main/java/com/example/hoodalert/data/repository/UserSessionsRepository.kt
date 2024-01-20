package com.example.hoodalert.data.repository

import com.example.hoodalert.data.model.UserSession
import com.example.hoodalert.data.retrofit.UserSessionsApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionsRepository(private val userSessionsApiService: UserSessionsApiService) {
    private val _userSessions = MutableStateFlow<List<UserSession>>(emptyList())
    val userSessions: StateFlow<List<UserSession>> = _userSessions

    private val _userSessionDetails = MutableStateFlow<UserSession?>(null)
    val userSessionDetails: StateFlow<UserSession?> = _userSessionDetails.asStateFlow()

    suspend fun getUserSessions(): List<UserSession> {
        val userSessions = userSessionsApiService.getUserSessions()
        _userSessions.value = userSessions
        return userSessions
    }

    suspend fun getUserSession(id: Int): UserSession {
        val userSession = userSessionsApiService.getUserSession(id)
        _userSessionDetails.value = userSession
        return userSession
    }

    suspend fun insertUserSession(userSession: UserSession): UserSession {
        val result = userSessionsApiService.insertUserSession(userSession)
        _userSessions.value = _userSessions.value + result
        _userSessionDetails.value = result
        return result
    }

    suspend fun updateUserSession(userSession: UserSession): UserSession {
        val result = userSessionsApiService.updateUserSession(userSession.id, userSession)
        _userSessions.value = _userSessions.value.map { if (it.id == result.id) result else it }
        _userSessionDetails.value = result
        return result
    }

    suspend fun deleteUserSession(userSession: UserSession) {
        userSessionsApiService.deleteUserSession(userSession.id)
        _userSessions.value = _userSessions.value.filter { it.id != userSession.id }
    }
}
