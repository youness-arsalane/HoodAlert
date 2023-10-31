package com.example.hoodalert.data.repository

import android.content.Context
import com.example.hoodalert.data.HoodAlertDatabase
import com.example.hoodalert.data.auth.SharedPreferencesManager
import com.example.hoodalert.data.dao.UserDao
import com.example.hoodalert.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UsersRepository(private val userDao: UserDao, private val context: Context) {
    fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers()

    fun getUserStream(id: Int): Flow<User?> = userDao.getUser(id)

    fun getUserByEmail(email: String): Flow<User?> = userDao.getUserByEmail(email)

    suspend fun insertUser(user: User) = userDao.insert(user)

    suspend fun updateUser(user: User) = userDao.update(user)

    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun getLoggedInUser(): User {
        val sharedPreferencesManager = SharedPreferencesManager(context)
        val token: String = sharedPreferencesManager.getUserToken().toString()

        val userSessionDao =
            HoodAlertDatabase.DatabaseInstance.getInstance(context).userSessionDao()
        val userDao = HoodAlertDatabase.DatabaseInstance.getInstance(context).userDao()

        var loggedInUser: User? = null

        val userSession = userSessionDao.getUserSessionByToken(token).firstOrNull()
        if (userSession !== null) {
            loggedInUser = userDao.getUser(userSession.userId).firstOrNull()
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
