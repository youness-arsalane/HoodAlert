package com.example.hoodalert.ui.viewmodel.communities


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.repository.CommunitiesRepository
import java.util.Date

class CommunityEntryViewModel(private val communitiesRepository: CommunitiesRepository) :
    ViewModel() {
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
            communitiesRepository.insertCommunity(communityUiState.communityDetails.toCommunity())
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
