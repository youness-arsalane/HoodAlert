package com.example.hoodalert.data.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hoodalert.data.model.Incident
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {

    @Query("SELECT * from incident")
    fun getAllIncidents(): Flow<List<Incident>>

    @Query("SELECT * from incident WHERE id = :id")
    fun getIncident(id: Int): Flow<Incident>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(incident: Incident)

    @Update
    suspend fun update(incident: Incident)

    @Delete
    suspend fun delete(incident: Incident)
}
