package com.example.hoodalert.data

import android.content.Context
import com.example.hoodalert.data.repository.CommunitiesRepository
import com.example.hoodalert.data.repository.CommunityUsersRepository
import com.example.hoodalert.data.repository.IncidentsRepository
import com.example.hoodalert.data.repository.UserSessionsRepository
import com.example.hoodalert.data.repository.UsersRepository
import com.example.hoodalert.data.retrofit.RetrofitBuilder

interface AppContainer {
    val communitiesRepository: CommunitiesRepository
    val communityUsersRepository: CommunityUsersRepository
    val incidentsRepository: IncidentsRepository
    val usersRepository: UsersRepository
    val userSessionsRepository: UserSessionsRepository
}

class AppDataContainer(val context: Context) : AppContainer {
    override val communitiesRepository =
        CommunitiesRepository(RetrofitBuilder.communitiesApiService)
    override val communityUsersRepository =
        CommunityUsersRepository(RetrofitBuilder.communityUsersApiService)
    override val incidentsRepository =
        IncidentsRepository(RetrofitBuilder.incidentsApiService)
    override val usersRepository =
        UsersRepository(RetrofitBuilder.usersApiService, context)
    override val userSessionsRepository =
        UserSessionsRepository(RetrofitBuilder.userSessionsApiService)
}
