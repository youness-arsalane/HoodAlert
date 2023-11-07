package com.example.hoodalert.ui.viewmodel.incidents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.screens.communities.CommunityEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date

class IncidentEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppDataContainer
) : ViewModel() {
    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    private val communityId: Int =
        checkNotNull(savedStateHandle[CommunityEditDestination.communityIdArg])

    init {
        viewModelScope.launch {
            if (communityId != 0) {
                incidentUiState.community =
                    appContainer.communitiesRepository.getCommunityStream(communityId)
                        .filterNotNull()
                        .first()
            }
        }
    }

    fun updateUiState(incident: Incident) {
        incidentUiState = IncidentUiState(
            incident = incident,
            isEntryValid = validateInput(incident),
            community = incidentUiState.community
        )
    }

    suspend fun saveIncident() {
        if (!validateInput()) {
            return
        }

        val incident = incidentUiState.incident
        if (incident.id != 0) {
            appContainer.incidentsRepository.insertIncident(incident)
            return
        }

        appContainer.getCurrentLocation { location ->
            if (location != null) {
                incident.latitude = location.first
                incident.longitude = location.second
            }

            runBlocking {
                appContainer.incidentsRepository.insertIncident(incident)
            }
        }
    }

    private fun validateInput(
        uiState: Incident = incidentUiState.incident
    ): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }
}

data class IncidentUiState(
    var incident: Incident = emptyIncident(),
    var isEntryValid: Boolean = false,
    var community: Community? = null,
    var user: User? = null
)

fun emptyIncident(): Incident {
    return Incident(
        id = 0,
        communityId = 0,
        userId = 0,
        title = "",
        description = "",
        latitude = null,
        longitude = null,
        createdAt = Date(),
        updatedAt = Date()
    )
}

fun Incident.toIncidentUiState(isEntryValid: Boolean = false): IncidentUiState {
    return IncidentUiState(
        incident = this,
        isEntryValid = isEntryValid
    )
}