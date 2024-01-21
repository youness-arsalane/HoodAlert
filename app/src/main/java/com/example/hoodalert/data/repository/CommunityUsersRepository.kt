package com.example.hoodalert.data.repository

import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.retrofit.CommunityUsersApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommunityUsersRepository(private val communityUsersApiService: CommunityUsersApiService) {
    private val _communityUsers = MutableStateFlow<List<CommunityUser>>(emptyList())
    val communityUsers: StateFlow<List<CommunityUser>> = _communityUsers

    private val _communityUserDetails = MutableStateFlow<CommunityUser?>(null)
    val communityUserDetails: StateFlow<CommunityUser?> = _communityUserDetails.asStateFlow()

    suspend fun getCommunityUsers(): List<CommunityUser> {
        val communityUsers = communityUsersApiService.getCommunityUsers()
        _communityUsers.value = communityUsers
        return communityUsers
    }

    suspend fun getCommunityUser(id: Int): CommunityUser? {
        return try {
            val communityUser = communityUsersApiService.getCommunityUser(id)
            _communityUserDetails.value = communityUser
            return communityUser
        } catch(e: Exception) {
            null
        }
    }

    suspend fun findByCommunityAndUser(community: Community, user: User): CommunityUser? {
        return try {
            val communityUser =
                communityUsersApiService.findByCommunityIdAndUserId(community.id, user.id)
            _communityUserDetails.value = communityUser
            communityUser
        } catch(e: Exception) {
            null
        }
    }

    suspend fun insertCommunityUser(communityUser: CommunityUser): CommunityUser {
        val result = communityUsersApiService.insertCommunityUser(communityUser)
        _communityUsers.value = _communityUsers.value + result
        _communityUserDetails.value = result
        return result
    }

    suspend fun updateCommunityUser(communityUser: CommunityUser): CommunityUser {
        val result = communityUsersApiService.updateCommunityUser(communityUser.id, communityUser)
        _communityUsers.value = _communityUsers.value.map { if (it.id == result.id) result else it }
        _communityUserDetails.value = result
        return result
    }

    suspend fun deleteCommunityUser(communityUser: CommunityUser) {
        communityUsersApiService.deleteCommunityUser(communityUser.id)
        _communityUsers.value = _communityUsers.value.filter { it.id != communityUser.id }
    }
}
