package com.example.hoodalert.ui.viewmodel.communities


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CommunityEntryViewModel(
    private val appContainer: AppDataContainer
) : ViewModel() {
    var communityUiState by mutableStateOf(CommunityUiState())
        private set

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
        val insertedCommunity = appContainer.communitiesRepository.insertCommunity(community)

        val loggedInUser = appContainer.usersRepository.getLoggedInUser()

        val communityUser = CommunityUser(
            id = 0,
            communityId = insertedCommunity.id,
            userId = loggedInUser.id,
            isAdmin = true,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )

        appContainer.communityUsersRepository.insertCommunityUser(communityUser)
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
    createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
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