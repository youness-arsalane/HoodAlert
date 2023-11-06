package com.example.hoodalert.ui.viewmodel.incidents

import android.util.Log
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
import java.text.DecimalFormat
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

    fun updateUiState(incidentDetails: IncidentDetails) {
        incidentUiState = IncidentUiState(
            incidentDetails = incidentDetails,
            isEntryValid = validateInput(incidentDetails)
        )
    }

    suspend fun saveIncident() {
        if (validateInput()) {
            var incident = incidentUiState.incidentDetails.toIncident()

            if (incident.id == 0) {
                appContainer.getCurrentLocation { location ->
                    Log.d("HOOD_ALERT_DEBUG", "Location:")
                    Log.d("HOOD_ALERT_DEBUG", location.toString())

                    if (location != null) {
                        incident = incidentUiState.incidentDetails.toIncident(
                            specificLatitude = location.first,
                            specificLongitude = location.second
                        )
                    }

                    runBlocking {
                        appContainer.incidentsRepository.insertIncident(incident)
                    }
                }
            } else {
                appContainer.incidentsRepository.insertIncident(incident)
            }
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
    val userId: Int = 0,
    val title: String = "",
    val description: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
)

fun IncidentDetails.toIncident(
    community: Community? = null,
    user: User? = null,
    specificLatitude: Double? = null,
    specificLongitude: Double? = null,
): Incident {
    return Incident(
        id = id,
        communityId = 1,
        userId = 1,
//        communityId = if (communityId == 0 && community != null) community.id else {
//            communityId
//        },
//        userId = if (userId == 0 && user != null) user.id else {
//            userId
//        },
        title = title,
        description = description,
        latitude = specificLatitude ?: latitude,
        longitude = specificLongitude ?: longitude,
        createdAt = Date(),
        updatedAt = Date()
    )
}

fun Incident.toIncidentUiState(isEntryValid: Boolean = false): IncidentUiState {
    return IncidentUiState(
        incidentDetails = this.toIncidentDetails(),
        isEntryValid = isEntryValid
    )
}

fun Incident.toIncidentDetails(): IncidentDetails {
    return IncidentDetails(
        id = id,
        communityId = communityId,
        userId = userId,
        title = title,
        description = description,
        latitude = latitude,
        longitude = longitude
    )
}
