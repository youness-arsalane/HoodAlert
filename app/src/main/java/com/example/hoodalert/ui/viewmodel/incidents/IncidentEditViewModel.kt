package com.example.hoodalert.ui.viewmodel.incidents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.ui.screens.incidents.IncidentEditDestination
import kotlinx.coroutines.launch

class IncidentEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppDataContainer
) : ViewModel() {
    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    private val incidentId: Int =
        checkNotNull(savedStateHandle[IncidentEditDestination.incidentIdArg])

    init {
        viewModelScope.launch {
            val incident = appContainer.incidentsRepository.getIncident(incidentId)
            val community = appContainer.communitiesRepository.getCommunity(incident.communityId)
            val user = appContainer.usersRepository.getUser(incident.userId)

            incidentUiState = incident.toIncidentUiState(true)
            incidentUiState.community = community
            incidentUiState.user = user
        }
    }

    suspend fun updateIncident() {
        if (validateInput(incidentUiState.incident)) {
            appContainer.incidentsRepository.updateIncident(incidentUiState.incident)
        }
    }

    fun updateUiState(incident: Incident) {
        incidentUiState =
            IncidentUiState(
                incident = incident,
                isEntryValid = validateInput(incident)
            )
    }

    private fun validateInput(uiState: Incident = incidentUiState.incident): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }
}
