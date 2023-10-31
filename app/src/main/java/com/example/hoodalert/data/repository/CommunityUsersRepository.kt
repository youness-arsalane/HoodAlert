package com.example.hoodalert.data.repository

import com.example.hoodalert.data.dao.CommunityUserDao
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.User
import kotlinx.coroutines.flow.Flow

class CommunityUsersRepository(private val communityUserDao: CommunityUserDao) {
    fun getAllCommunityUsersStream(): Flow<List<CommunityUser>> =
        communityUserDao.getAllCommunityUsers()

    fun getCommunityUserStream(id: Int): Flow<CommunityUser?> =
        communityUserDao.getCommunityUser(id)

    fun findByCommunityAndUser(community: Community, user: User): Flow<CommunityUser?> =
        communityUserDao.findByCommunityIdAndUserId(community.id, user.id)

    suspend fun insertCommunityUser(communityUser: CommunityUser) =
        communityUserDao.insert(communityUser)

    suspend fun updateCommunityUser(communityUser: CommunityUser) =
        communityUserDao.update(communityUser)

    suspend fun deleteCommunityUser(communityUser: CommunityUser) =
        communityUserDao.delete(communityUser)
}
