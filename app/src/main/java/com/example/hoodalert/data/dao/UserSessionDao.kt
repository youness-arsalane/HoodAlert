package com.example.hoodalert.data.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hoodalert.data.model.UserSession
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSessionDao {

    @Query("SELECT * FROM user_sessions")
    fun getAllUserSessions(): Flow<List<UserSession>>

    @Query("SELECT * FROM user_sessions WHERE id = :id")
    fun getUserSession(id: Int): Flow<UserSession>

    @Query("SELECT * FROM user_sessions WHERE token = :token")
    fun getUserSessionByToken(token: String): Flow<UserSession?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userSession: UserSession)

    @Update
    suspend fun update(userSession: UserSession)

    @Delete
    suspend fun delete(userSession: UserSession)
}
