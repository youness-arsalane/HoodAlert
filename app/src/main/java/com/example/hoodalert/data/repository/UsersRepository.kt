package com.example.hoodalert.data.repository

import com.example.hoodalert.data.dao.UserDao
import com.example.hoodalert.data.model.User
import kotlinx.coroutines.flow.Flow

class UsersRepository(private val userDao: UserDao) {
    fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers()

    fun getUserStream(id: Int): Flow<User?> = userDao.getUser(id)

    suspend fun insertUser(user: User) = userDao.insert(user)

    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun updateUser(user: User) = userDao.update(user)

    fun signIn(email: String, password: String) : Boolean {
        return true;
    }
}
