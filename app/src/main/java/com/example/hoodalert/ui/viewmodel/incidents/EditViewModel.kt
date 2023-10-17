package com.example.hoodalert.ui.viewmodel.incidents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.repository.IncidentsRepository
import com.example.hoodalert.ui.screens.incidents.IncidentEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val incidentsRepository: IncidentsRepository
) : ViewModel() {
    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    private val incidentId: Int = checkNotNull(savedStateHandle[IncidentEditDestination.incidentIdArg])

    init {
        viewModelScope.launch {
            incidentUiState = incidentsRepository.getIncidentStream(incidentId)
                .filterNotNull()
                .first()
                .toIncidentUiState(true)
        }
    }

    suspend fun updateIncident() {
        if (validateInput(incidentUiState.incidentDetails)) {
            incidentsRepository.updateIncident(incidentUiState.incidentDetails.toIncident())
        }
    }

    fun updateUiState(incidentDetails: IncidentDetails) {
        incidentUiState =
            IncidentUiState(incidentDetails = incidentDetails, isEntryValid = validateInput(incidentDetails))
    }

    private fun validateInput(uiState: IncidentDetails = incidentUiState.incidentDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
}
