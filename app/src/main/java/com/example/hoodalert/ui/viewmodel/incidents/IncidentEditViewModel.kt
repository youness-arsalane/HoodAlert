package com.example.hoodalert.ui.viewmodel.incidents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppContainer
import com.example.hoodalert.ui.screens.incidents.IncidentEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class IncidentEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppContainer
) : ViewModel() {
    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    private val incidentId: Int =
        checkNotNull(savedStateHandle[IncidentEditDestination.incidentIdArg])

    init {
        viewModelScope.launch {
            val incident = appContainer.incidentsRepository.getIncidentStream(incidentId)
                .filterNotNull()
                .first()

            val community =
                appContainer.communitiesRepository.getCommunityStream(incident.communityId)
                    .filterNotNull()
                    .first()

            val user =
                appContainer.usersRepository.getUserStream(incident.userId)
                    .filterNotNull()
                    .first()

            incidentUiState = incident.toIncidentUiState(true)
            incidentUiState.community = community
            incidentUiState.user = user
        }
    }

    suspend fun updateIncident() {
        if (validateInput(incidentUiState.incidentDetails)) {
            appContainer.incidentsRepository.updateIncident(incidentUiState.incidentDetails.toIncident())
        }
    }

    fun updateUiState(incidentDetails: IncidentDetails) {
        incidentUiState =
            IncidentUiState(
                incidentDetails = incidentDetails,
                isEntryValid = validateInput(incidentDetails)
            )
    }

    private fun validateInput(uiState: IncidentDetails = incidentUiState.incidentDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }
}
