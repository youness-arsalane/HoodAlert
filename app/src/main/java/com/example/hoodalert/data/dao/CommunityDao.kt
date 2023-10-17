package com.example.hoodalert.data.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hoodalert.data.model.Community
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {

    @Query("SELECT * from communities")
    fun getAllCommunities(): Flow<List<Community>>

    @Query("SELECT * from communities WHERE id = :id")
    fun getCommunity(id: Int): Flow<Community>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(community: Community)

    @Update
    suspend fun update(community: Community)

    @Delete
    suspend fun delete(community: Community)
}
