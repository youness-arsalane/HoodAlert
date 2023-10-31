package com.example.hoodalert.ui.viewmodel.communities


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.repository.CommunitiesRepository
import com.example.hoodalert.data.repository.CommunityUsersRepository
import com.example.hoodalert.data.repository.UsersRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date

class CommunityEntryViewModel(
    private val communitiesRepository: CommunitiesRepository,
    private val communityUsersRepository: CommunityUsersRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    var communityUiState by mutableStateOf(CommunityUiState())
        private set

    fun updateUiState(communityDetails: CommunityDetails) {
        communityUiState =
            CommunityUiState(
                communityDetails = communityDetails,
                isEntryValid = validateInput(communityDetails)
            )
    }

    suspend fun saveCommunity() {
        if (validateInput()) {
            val community = communityUiState.communityDetails.toCommunity();
            val communityId = communitiesRepository.insertCommunity(community)

            val loggedInUser = usersRepository.getLoggedInUser()
            if (loggedInUser === null) {
                throw Exception("User is not logged in!")
            }

            val communityUser = CommunityUser(
                id = 0,
                communityId = communityId,
                userId = loggedInUser.id,
                createdAt = Date(),
                updatedAt = Date()
            )

            communityUsersRepository.insertCommunityUser(communityUser)
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

fun Community.toCommunityUiState(isEntryValid: Boolean = false): CommunityUiState =
    CommunityUiState(
        communityDetails = this.toCommunityDetails(),
        isEntryValid = isEntryValid
    )

fun Community.toCommunityDetails(): CommunityDetails = CommunityDetails(
    id = id,
    name = name
)
