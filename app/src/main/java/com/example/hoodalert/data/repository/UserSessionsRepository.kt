package com.example.hoodalert.data.repository

import com.example.hoodalert.data.dao.UserSessionDao
import com.example.hoodalert.data.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first

class UserSessionsRepository(private val userSessionDao: UserSessionDao) {
    fun getAllUserSessionsStream(): Flow<List<UserSession>> = userSessionDao.getAllUserSessions()

    fun getUserSessionStream(id: Int): Flow<UserSession?> = userSessionDao.getUserSession(id)

    fun getUserSessionByToken(token: String): Flow<UserSession?> =
        userSessionDao.getUserSessionByToken(token)

    suspend fun insertUserSession(userSession: UserSession) = userSessionDao.insert(userSession)

    suspend fun updateUserSession(userSession: UserSession) = userSessionDao.update(userSession)

    suspend fun deleteUserSession(userSession: UserSession) = userSessionDao.delete(userSession)

    suspend fun findUserSessionByToken(token: String): UserSession? {
        userSessionDao.getUserSessionByToken(token).collect { userSession ->

        }

        val userSession = userSessionDao.getUserSessionByToken(token);
        if (userSession.count() == 0) {
            return null;
        }

        return userSession.first();
    }
}
