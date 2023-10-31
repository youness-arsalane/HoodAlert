package com.example.hoodalert.data.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hoodalert.data.model.IncidentImage
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentImageDao {

    @Query("SELECT * FROM incident_images")
    fun getAllIncidentImages(): Flow<List<IncidentImage>>

    @Query("SELECT * FROM incident_images WHERE id = :id")
    fun getIncidentImage(id: Int): Flow<IncidentImage>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(incidentImage: IncidentImage)

    @Update
    suspend fun update(incidentImage: IncidentImage)

    @Delete
    suspend fun delete(incidentImage: IncidentImage)
}
