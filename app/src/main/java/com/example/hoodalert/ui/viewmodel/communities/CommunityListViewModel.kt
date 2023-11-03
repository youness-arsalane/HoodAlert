package com.example.hoodalert.ui.viewmodel.communities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CommunityListViewModel(private val appContainer: AppDataContainer) : ViewModel() {
    val communityListUiState: StateFlow<CommunityListUiState> =
        appContainer.communitiesRepository.getAllCommunitiesStream()
            .map { CommunityListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CommunityListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun isMemberOfCommunity(user: User, community: Community): Boolean {
        Log.d("HOOD_ALERT_DEBUG", "VM => User ID: " + user.id.toString())
        Log.d("HOOD_ALERT_DEBUG", "VM => Community ID: " + community.id.toString())

        return appContainer.communityUsersRepository.findByCommunityAndUser(community, user)
            .count() == 0
    }
}

data class CommunityListUiState(val communityList: List<Community> = listOf())

suspend fun Community.isMember(
    communityListViewModel: CommunityListViewModel,
    user: User
): Boolean {
    return communityListViewModel.isMemberOfCommunity(user, this)
}