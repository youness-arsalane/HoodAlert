package com.example.hoodalert.ui.viewmodel.communities

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.repository.CommunitiesRepository
import com.example.hoodalert.data.repository.CommunityUsersRepository
import com.example.hoodalert.ui.screens.communities.CommunityDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CommunityDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val communitiesRepository: CommunitiesRepository,
    private val communityUsersRepository: CommunityUsersRepository
) : ViewModel() {

    private val communityId: Int =
        checkNotNull(savedStateHandle[CommunityDetailsDestination.communityIdArg])

    val uiState: StateFlow<CommunityDetailsUiState> =
        communitiesRepository.getCommunityStream(communityId)
            .filterNotNull()
            .map {
                CommunityDetailsUiState(communityDetails = it.toCommunityDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CommunityDetailsUiState()
            )

    suspend fun deleteCommunity() {
        communitiesRepository.deleteCommunity(uiState.value.communityDetails.toCommunity())
    }

    suspend fun findCommunityUser(loggedInUser: User?): CommunityUser? {
        if (loggedInUser === null) {
            return null;
        }

        return communityUsersRepository.findByCommunityAndUser(
            community = uiState.value.communityDetails.toCommunity(),
            user = loggedInUser
        ).firstOrNull()
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CommunityDetailsUiState(
    val communityDetails: CommunityDetails = CommunityDetails()
)
