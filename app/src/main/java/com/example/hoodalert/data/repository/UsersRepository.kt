package com.example.hoodalert.data.repository

import android.util.Log
import com.example.hoodalert.data.dao.UserDao
import com.example.hoodalert.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first

class UsersRepository(private val userDao: UserDao) {
    fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers()

    fun getUserStream(id: Int): Flow<User?> = userDao.getUser(id)

    fun getUserByEmail(email: String): Flow<User?> = userDao.getUserByEmail(email)

    suspend fun insertUser(user: User) = userDao.insert(user)

    suspend fun updateUser(user: User) = userDao.update(user)

    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun findUserByCredentials(email: String, password: String) : User? {
        val user = userDao.getUserByEmail(email);
        if (user.count() == 0) {
            return null;
        } else if (user.first().password != password) {
            return null;
        }

        return user.first();
    }
}
