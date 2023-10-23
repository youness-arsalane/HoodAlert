package com.example.hoodalert.data.repository

import com.example.hoodalert.data.dao.CommunityDao
import com.example.hoodalert.data.model.Community
import kotlinx.coroutines.flow.Flow

class CommunitiesRepository(private val communityDao: CommunityDao) {
    fun getAllCommunitiesStream(): Flow<List<Community>> = communityDao.getAllCommunities()

    fun getCommunityStream(id: Int): Flow<Community?> = communityDao.getCommunity(id)

    suspend fun insertCommunity(community: Community) = communityDao.insert(community)

    suspend fun deleteCommunity(community: Community) = communityDao.delete(community)

    suspend fun updateCommunity(community: Community) = communityDao.update(community)
}
