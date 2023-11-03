package com.example.hoodalert.data.repository

import com.example.hoodalert.data.dao.IncidentImageDao
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.IncidentImage
import kotlinx.coroutines.flow.Flow

class IncidentImagesRepository(private val incidentImageDao: IncidentImageDao) {
    fun getAllIncidentImagesStream(): Flow<List<IncidentImage>> = incidentImageDao.getAllIncidentImages()

    fun getIncidentImageStream(id: Int): Flow<IncidentImage?> = incidentImageDao.getIncidentImage(id)

    suspend fun insertIncidentImage(incidentImage: IncidentImage) = incidentImageDao.insert(incidentImage)

    suspend fun updateIncidentImage(incidentImage: IncidentImage) = incidentImageDao.update(incidentImage)

    suspend fun deleteIncidentImage(incidentImage: IncidentImage) = incidentImageDao.delete(incidentImage)

    fun findByIncident(incident: Incident): Flow<List<IncidentImage>> =
        incidentImageDao.findByIncidentId(incident.id)
}
