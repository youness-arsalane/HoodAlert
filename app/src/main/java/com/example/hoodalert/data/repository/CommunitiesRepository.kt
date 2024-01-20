package com.example.hoodalert.data.repository

import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.retrofit.CommunitiesApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommunitiesRepository(private val communitiesApiService: CommunitiesApiService) {
    private val _communities = MutableStateFlow<List<Community>>(emptyList())
    val communities: StateFlow<List<Community>> = _communities

    private val _communityDetails = MutableStateFlow<Community?>(null)
    val communityDetails: StateFlow<Community?> = _communityDetails.asStateFlow()

    suspend fun getCommunities(): List<Community> {
        val communities = communitiesApiService.getCommunities()
        _communities.value = communities
        return communities
    }

    suspend fun getCommunity(id: Int): Community {
        val community = communitiesApiService.getCommunity(id)
        _communityDetails.value = community
        return community
    }

    suspend fun insertCommunity(community: Community): Community {
        val result = communitiesApiService.insertCommunity(community)
        _communities.value = _communities.value + result
        _communityDetails.value = result
        return result
    }

    suspend fun updateCommunity(community: Community): Community {
        val result = communitiesApiService.updateCommunity(community.id, community)
        _communities.value = _communities.value.map { if (it.id == result.id) result else it }
        _communityDetails.value = result
        return result
    }

    suspend fun deleteCommunity(community: Community) {
        communitiesApiService.deleteCommunity(community.id)
        _communities.value = _communities.value.filter { it.id != community.id }
    }
}
