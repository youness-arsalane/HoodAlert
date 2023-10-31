package com.example.hoodalert.ui.viewmodel.incidents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppContainer
import com.example.hoodalert.data.model.Incident
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class IncidentListViewModel(private val appContainer: AppContainer) : ViewModel() {
    val incidentListUiState: StateFlow<IncidentListUiState> =
        appContainer.incidentsRepository.getAllIncidentsStream().map { IncidentListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = IncidentListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class IncidentListUiState(val incidentList: List<Incident> = listOf())
