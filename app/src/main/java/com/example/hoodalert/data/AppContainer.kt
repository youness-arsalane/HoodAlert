package com.example.hoodalert.data

import android.content.Context
import com.example.hoodalert.data.repository.IncidentsRepository

interface AppContainer {
    val incidentsRepository: IncidentsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val incidentsRepository = IncidentsRepository(HoodAlertDatabase.getDatabase(context).incidentDao())
}
