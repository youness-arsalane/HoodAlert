package com.example.hoodalert.data

import android.content.Context
import com.example.hoodalert.data.repository.CommunitiesRepository
import com.example.hoodalert.data.repository.IncidentsRepository
import com.example.hoodalert.data.repository.UsersRepository

interface AppContainer {
    val communitiesRepository: CommunitiesRepository
    val incidentsRepository: IncidentsRepository
    val usersRepository: UsersRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val communitiesRepository = CommunitiesRepository(HoodAlertDatabase.DatabaseInstance.getDatabase(context).communityDao())
    override val incidentsRepository = IncidentsRepository(HoodAlertDatabase.DatabaseInstance.getDatabase(context).incidentDao())
    override val usersRepository = UsersRepository(HoodAlertDatabase.DatabaseInstance.getDatabase(context).userDao())
}
