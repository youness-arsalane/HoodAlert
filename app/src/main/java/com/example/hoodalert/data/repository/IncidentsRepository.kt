package com.example.hoodalert.data.repository

import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.retrofit.IncidentsApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IncidentsRepository(private val incidentsApiService: IncidentsApiService) {
    private val _incidents = MutableStateFlow<List<Incident>>(emptyList())
    val incidents: StateFlow<List<Incident>> = _incidents

    private val _incidentDetails = MutableStateFlow<Incident?>(null)
    val incidentDetails: StateFlow<Incident?> = _incidentDetails.asStateFlow()

    suspend fun getIncidents(): List<Incident> {
        val incidents = incidentsApiService.getIncidents()
        _incidents.value = incidents
        return incidents
    }

    suspend fun findByCommunity(community: Community): List<Incident> =
        incidentsApiService.findByCommunityId(community.id)

    suspend fun getIncident(id: Int): Incident? {
        return try {
            val incident = incidentsApiService.getIncident(id)
            _incidentDetails.value = incident
            return incident
        } catch(e: Exception) {
            null
        }
    }

    suspend fun insertIncident(incident: Incident): Incident {
        val result = incidentsApiService.insertIncident(incident)
        _incidents.value = _incidents.value + result
        _incidentDetails.value = result
        return result
    }

    suspend fun updateIncident(incident: Incident): Incident {
        val result = incidentsApiService.updateIncident(incident.id, incident)
        _incidents.value = _incidents.value.map { if (it.id == result.id) result else it }
        _incidentDetails.value = result
        return result
    }

    suspend fun deleteIncident(incident: Incident) {
        incidentsApiService.deleteIncident(incident.id)
        _incidents.value = _incidents.value.filter { it.id != incident.id }
    }
}
