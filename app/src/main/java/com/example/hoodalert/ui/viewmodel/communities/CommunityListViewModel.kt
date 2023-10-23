package com.example.hoodalert.ui.viewmodel.communities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.repository.CommunitiesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CommunityListViewModel(communitiesRepository: CommunitiesRepository) : ViewModel() {
    val communityListUiState: StateFlow<CommunityListUiState> =
        communitiesRepository.getAllCommunitiesStream().map { CommunityListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CommunityListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CommunityListUiState(val communityList: List<Community> = listOf())
