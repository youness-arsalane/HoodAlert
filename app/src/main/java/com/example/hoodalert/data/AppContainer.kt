package com.example.hoodalert.data

import android.content.Context
import com.example.hoodalert.data.repository.CommunitiesRepository
import com.example.hoodalert.data.repository.CommunityUsersRepository
import com.example.hoodalert.data.repository.IncidentsRepository
import com.example.hoodalert.data.repository.UserSessionsRepository
import com.example.hoodalert.data.repository.UsersRepository

interface AppContainer {
    val communitiesRepository: CommunitiesRepository
    val communityUsersRepository: CommunityUsersRepository
    val incidentsRepository: IncidentsRepository
    val usersRepository: UsersRepository
    val userSessionsRepository: UserSessionsRepository
}

class AppDataContainer(val context: Context) : AppContainer {
    override val communitiesRepository = CommunitiesRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).communityDao()
    )
    override val communityUsersRepository = CommunityUsersRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).communityUserDao()
    )
    override val incidentsRepository = IncidentsRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).incidentDao()
    )
    override val usersRepository = UsersRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).userDao(),
        context
    )
    override val userSessionsRepository = UserSessionsRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).userSessionDao()
    )
}
