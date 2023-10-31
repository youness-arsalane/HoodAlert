package com.example.hoodalert.data.repository

import com.example.hoodalert.data.dao.IncidentDao
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.Incident
import kotlinx.coroutines.flow.Flow

class IncidentsRepository(private val incidentDao: IncidentDao) {
    fun getAllIncidentsStream(): Flow<List<Incident>> = incidentDao.getAllIncidents()

    fun getIncidentStream(id: Int): Flow<Incident?> = incidentDao.getIncident(id)

    suspend fun insertIncident(incident: Incident) = incidentDao.insert(incident)

    suspend fun updateIncident(incident: Incident) = incidentDao.update(incident)

    suspend fun deleteIncident(incident: Incident) = incidentDao.delete(incident)

    fun findByCommunity(community: Community): Flow<List<Incident>> =
        incidentDao.findByCommunityId(community.id)
}
