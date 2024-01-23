package com.example.hoodalert.ui.viewmodel.communities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.ui.screens.communities.CommunityEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class CommunityFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppDataContainer
) : ViewModel() {
    var communityUiState by mutableStateOf(CommunityUiState())
        private set

    private val communityId: Int? =
        savedStateHandle[CommunityEditDestination.communityIdArg]

    init {
        if (communityId != null) {
            viewModelScope.launch {
                communityUiState =
                    appContainer.communitiesRepository.getCommunityStream(communityId)
                        .filterNotNull()
                        .first()
                        .toCommunityUiState(true)
            }
        }
    }

    fun isNew(): Boolean = communityId == null

    fun updateUiState(communityDetails: CommunityDetails) {
        communityUiState = CommunityUiState(
            communityDetails = communityDetails,
            isEntryValid = validateInput(communityDetails)
        )
    }

    suspend fun saveCommunity() {
        if (!validateInput()) {
            return
        }

        val community = communityUiState.communityDetails.toCommunity()

        if (isNew()) {
            val communityId = appContainer.communitiesRepository.insertCommunity(community)

            val loggedInUser = appContainer.usersRepository.getLoggedInUser()

            val communityUser = CommunityUser(
                id = 0,
                communityId = communityId,
                userId = loggedInUser.id,
                isAdmin = true,
                createdAt = Date(),
                updatedAt = Date()
            )

            appContainer.communityUsersRepository.insertCommunityUser(communityUser)
        } else {
            appContainer.communitiesRepository.updateCommunity(community)
        }
    }

    private fun validateInput(uiState: CommunityDetails = communityUiState.communityDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

data class CommunityUiState(
    val communityDetails: CommunityDetails = CommunityDetails(),
    val isEntryValid: Boolean = false
)

data class CommunityDetails(
    val id: Int = 0,
    val name: String = "",
)

fun CommunityDetails.toCommunity(): Community = Community(
    id = id,
    name = name,
    createdAt = Date(),
    updatedAt = Date()
)

fun Community.toCommunityUiState(isEntryValid: Boolean = false): CommunityUiState {
    return CommunityUiState(
        communityDetails = this.toCommunityDetails(),
        isEntryValid = isEntryValid
    )
}

fun Community.toCommunityDetails(): CommunityDetails {
    return CommunityDetails(
        id = id,
        name = name
    )
}