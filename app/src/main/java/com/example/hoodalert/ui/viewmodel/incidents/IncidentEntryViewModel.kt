package com.example.hoodalert.ui.viewmodel.incidents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.screens.communities.CommunityEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class IncidentEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppContainer
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

    fun updateUiState(incidentDetails: IncidentDetails) {
        incidentUiState = IncidentUiState(
            incidentDetails = incidentDetails,
            isEntryValid = validateInput(incidentDetails)
        )
    }

    suspend fun saveIncident() {
        if (validateInput()) {
            appContainer.incidentsRepository.insertIncident(incidentUiState.incidentDetails.toIncident())
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
    val isEntryValid: Boolean = false,
    var community: Community? = null,
    var user: User? = null
)

data class IncidentDetails(
    val id: Int = 0,
    val communityId: Int = 0,
    val title: String = "",
    val description: String = ""
)

fun IncidentDetails.toIncident(): Incident = Incident(
    id = id,
    communityId = 1,
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
