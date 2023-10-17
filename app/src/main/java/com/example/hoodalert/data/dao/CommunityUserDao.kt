package com.example.hoodalert.data.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hoodalert.data.model.CommunityUser
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityUserDao {

    @Query("SELECT * from community_users")
    fun getAllCommunityUsers(): Flow<List<CommunityUser>>

    @Query("SELECT * from community_users WHERE id = :id")
    fun getCommunityUser(id: Int): Flow<CommunityUser>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(communityUser: CommunityUser)

    @Update
    suspend fun update(communityUser: CommunityUser)

    @Delete
    suspend fun delete(communityUser: CommunityUser)
}
