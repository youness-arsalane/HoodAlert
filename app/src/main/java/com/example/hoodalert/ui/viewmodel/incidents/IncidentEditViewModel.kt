package com.example.hoodalert.ui.viewmodel.incidents

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.IncidentImage
import com.example.hoodalert.ui.screens.incidents.IncidentEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

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

    suspend fun addIncidentImage(incident: Incident, uri: Uri) {
        Log.d("HOOD_ALERT_DEBUG", "addIncidentImage")
        Log.d("HOOD_ALERT_DEBUG", uri.toString())

        val incidentImage = IncidentImage(
            id = 0,
            incidentId = incident.id,
            path = uri.toString(),
            createdAt = Date(),
            updatedAt = Date()
        )

        appContainer.incidentImagesRepository.insertIncidentImage(incidentImage)
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
