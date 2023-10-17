package com.example.hoodalert.ui.viewmodel.incidents

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.repository.IncidentsRepository
import com.example.hoodalert.ui.screens.incidents.IncidentDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IncidentDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val incidentsRepository: IncidentsRepository,
) : ViewModel() {

    private val incidentId: Int = checkNotNull(savedStateHandle[IncidentDetailsDestination.incidentIdArg])

    val uiState: StateFlow<IncidentDetailsUiState> =
        incidentsRepository.getIncidentStream(incidentId)
            .filterNotNull()
            .map {
                IncidentDetailsUiState(incidentDetails = it.toIncidentDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = IncidentDetailsUiState()
            )

    suspend fun deleteIncident() {
        incidentsRepository.deleteIncident(uiState.value.incidentDetails.toIncident())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class IncidentDetailsUiState(
    val outOfStock: Boolean = true,
    val incidentDetails: IncidentDetails = IncidentDetails()
)
