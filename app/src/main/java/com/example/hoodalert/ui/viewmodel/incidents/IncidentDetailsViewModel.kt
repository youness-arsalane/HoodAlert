package com.example.hoodalert.ui.viewmodel.incidents

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.screens.communities.CommunityDetailsDestination
import com.example.hoodalert.ui.screens.incidents.IncidentDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class IncidentDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppDataContainer
) : ViewModel() {
    private val communityId: Int =
        checkNotNull(savedStateHandle[IncidentDetailsDestination.communityIdArg])

    private val incidentId: Int =
        checkNotNull(savedStateHandle[IncidentDetailsDestination.incidentIdArg])

    val uiState: StateFlow<IncidentDetailsUiState> =
        appContainer.incidentsRepository.incidentDetails
            .map {
                if (it !== null) {
                    IncidentDetailsUiState(
                        incident = it,
                        community = appContainer.communitiesRepository.getCommunity(it.communityId),
                        user = appContainer.usersRepository.getUser(it.userId)
                    )
                } else {
                    IncidentDetailsUiState()
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = IncidentDetailsUiState()
            )

    init {
        viewModelScope.launch {
            appContainer.incidentsRepository.getIncident(incidentId)
        }
    }

    suspend fun deleteIncident() {
        appContainer.incidentsRepository.deleteIncident(uiState.value.incident)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class IncidentDetailsUiState(
    val incident: Incident = Incident(
        id = 0,
        communityId = 0,
        userId = 0,
        title = "",
        description = "",
        street = "",
        houseNumber = "",
        zipcode = "",
        city = "",
        country = "",
        latitude = null,
        longitude = null,
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ),
    var community: Community? = null,
    var user: User? = null,
)

fun User.getFullName(): String {
    return this.firstName + " " + this.lastName
}