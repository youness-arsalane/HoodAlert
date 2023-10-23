package com.example.hoodalert.ui.viewmodel.communities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.repository.CommunitiesRepository
import com.example.hoodalert.ui.screens.communities.CommunityEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CommunityEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val communitiesRepository: CommunitiesRepository
) : ViewModel() {
    var communityUiState by mutableStateOf(CommunityUiState())
        private set

    private val communityId: Int = checkNotNull(savedStateHandle[CommunityEditDestination.communityIdArg])

    init {
        viewModelScope.launch {
            communityUiState = communitiesRepository.getCommunityStream(communityId)
                .filterNotNull()
                .first()
                .toCommunityUiState(true)
        }
    }

    suspend fun updateCommunity() {
        if (validateInput(communityUiState.communityDetails)) {
            communitiesRepository.updateCommunity(communityUiState.communityDetails.toCommunity())
        }
    }

    fun updateUiState(communityDetails: CommunityDetails) {
        communityUiState =
            CommunityUiState(communityDetails = communityDetails, isEntryValid = validateInput(communityDetails))
    }

    private fun validateInput(uiState: CommunityDetails = communityUiState.communityDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}
