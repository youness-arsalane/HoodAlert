package com.example.hoodalert.ui.viewmodel.communities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CommunityListViewModel(private val appContainer: AppDataContainer) : ViewModel() {
    val communityListUiState: StateFlow<CommunityListUiState> =
        appContainer.communitiesRepository.communities
            .map { CommunityListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CommunityListUiState()
            )

    init {
        viewModelScope.launch {
            appContainer.communitiesRepository.getCommunities()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun isMemberOfCommunity(user: User, community: Community): Boolean {
        return appContainer.communityUsersRepository.findByCommunityAndUser(community, user) != null
    }
}

data class CommunityListUiState(val communityList: List<Community> = listOf())

suspend fun Community.isMember(
    communityListViewModel: CommunityListViewModel,
    user: User
): Boolean {
    return communityListViewModel.isMemberOfCommunity(user, this)
}