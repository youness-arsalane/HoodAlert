package com.example.inventory.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Incident
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapViewModel(private val appContainer: AppDataContainer) : ViewModel() {
    val mapUiState: StateFlow<MapUiState> =
        appContainer.incidentsRepository.incidents
            .map { MapUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MapUiState()
            )

    init {
        viewModelScope.launch {
            appContainer.incidentsRepository.getIncidents()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MapUiState(val incidentList: List<Incident> = listOf())
