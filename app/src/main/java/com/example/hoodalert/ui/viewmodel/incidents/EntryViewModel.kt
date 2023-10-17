package com.example.hoodalert.ui.viewmodel.incidents


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.repository.IncidentsRepository
import java.text.NumberFormat
import java.util.Date

class IncidentEntryViewModel(private val incidentsRepository: IncidentsRepository) : ViewModel() {
    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    fun updateUiState(incidentDetails: IncidentDetails) {
        incidentUiState =
            IncidentUiState(incidentDetails = incidentDetails, isEntryValid = validateInput(incidentDetails))
    }

    suspend fun saveIncident() {
        if (validateInput()) {
            incidentsRepository.insertIncident(incidentUiState.incidentDetails.toIncident())
        }
    }

    private fun validateInput(uiState: IncidentDetails = incidentUiState.incidentDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }
}

data class IncidentUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val isEntryValid: Boolean = false
)

data class IncidentDetails(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
)

fun IncidentDetails.toIncident(): Incident = Incident(
    id = id,
    userId = 1,
    title = title,
    description = description,
    latitude = null,
    longitude = null,
    createdAt = Date(),
    updatedAt = Date()
)

fun Incident.toIncidentUiState(isEntryValid: Boolean = false): IncidentUiState = IncidentUiState(
    incidentDetails = this.toIncidentDetails(),
    isEntryValid = isEntryValid
)

fun Incident.toIncidentDetails(): IncidentDetails = IncidentDetails(
    id = id,
    title = title,
    description = description
)
