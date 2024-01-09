package com.example.hoodalert.ui.viewmodel.incidents

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.screens.incidents.IncidentDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class IncidentDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppDataContainer
) : ViewModel() {
    private val incidentId: Int =
        checkNotNull(savedStateHandle[IncidentDetailsDestination.incidentIdArg])
    private val incidentStream = appContainer.incidentsRepository.getIncidentStream(incidentId)
        .filterNotNull()

    val uiState: StateFlow<IncidentDetailsUiState> = incidentStream
        .map {
            IncidentDetailsUiState(
                incident = it,
                community = appContainer.communitiesRepository.getCommunityStream(incidentStream.first().communityId)
                    .filterNotNull()
                    .first(),
                user = appContainer.usersRepository.getUserStream(incidentStream.first().userId)
                    .filterNotNull()
                    .first()
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = IncidentDetailsUiState()
        )

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
        createdAt = Date(),
        updatedAt = Date()
    ),
    var community: Community? = null,
    var user: User? = null,
)

fun User.getFullName(): String {
    return this.firstName + " " + this.lastName
}