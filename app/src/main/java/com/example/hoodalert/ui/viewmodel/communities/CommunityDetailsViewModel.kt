package com.example.hoodalert.ui.viewmodel.communities

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.ui.screens.communities.CommunityDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CommunityDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppDataContainer
) : ViewModel() {

    private val communityId: Int =
        checkNotNull(savedStateHandle[CommunityDetailsDestination.communityIdArg])

    val uiState: StateFlow<CommunityDetailsUiState> =
        appContainer.communitiesRepository.getCommunityStream(communityId)
            .filterNotNull()
            .map {
                CommunityDetailsUiState(communityDetails = it.toCommunityDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CommunityDetailsUiState()
            )

    suspend fun deleteCommunity() {
        appContainer.communitiesRepository.deleteCommunity(uiState.value.communityDetails.toCommunity())
    }

    suspend fun findCommunityUser(loggedInUser: User): CommunityUser? {
        return appContainer.communityUsersRepository.findByCommunityAndUser(
            community = uiState.value.communityDetails.toCommunity(),
            user = loggedInUser
        ).firstOrNull()
    }

    suspend fun insertCommunityUser(communityUser: CommunityUser) {
        return appContainer.communityUsersRepository.insertCommunityUser(communityUser)
    }

    suspend fun deleteCommunityUser(communityUser: CommunityUser) {
        return appContainer.communityUsersRepository.deleteCommunityUser(communityUser)
    }

    fun getIncidents(community: Community) =
        appContainer.incidentsRepository.findByCommunity(community)

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CommunityDetailsUiState(
    val communityDetails: CommunityDetails = CommunityDetails()
)

suspend fun Community.getIncidents(communityDetailsViewModel: CommunityDetailsViewModel): List<Incident> {
    return communityDetailsViewModel.getIncidents(this).first()
}

//fun Community.getIncidents(communityDetailsViewModel: CommunityDetailsViewModel): List<Incident> {
//    var incidents = emptyList<Incident>()
//
//    var community = this
//    runBlocking {
//        communityDetailsViewModel.getIncidents(community).collect { results ->
//            incidents = results
//        }
//    }
//
//    return incidents
//}