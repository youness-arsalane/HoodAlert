package com.example.hoodalert.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hoodalert.data.repository.CommunitiesRepository
import com.example.hoodalert.data.repository.CommunityUsersRepository
import com.example.hoodalert.data.repository.IncidentImagesRepository
import com.example.hoodalert.data.repository.IncidentsRepository
import com.example.hoodalert.data.repository.UserSessionsRepository
import com.example.hoodalert.data.repository.UsersRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

interface AppContainer {
    val communitiesRepository: CommunitiesRepository
    val communityUsersRepository: CommunityUsersRepository
    val incidentsRepository: IncidentsRepository
    val incidentImagesRepository: IncidentImagesRepository
    val usersRepository: UsersRepository
    val userSessionsRepository: UserSessionsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val communitiesRepository = CommunitiesRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).communityDao()
    )
    override val communityUsersRepository = CommunityUsersRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).communityUserDao()
    )
    override val incidentsRepository = IncidentsRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).incidentDao()
    )
    override val incidentImagesRepository = IncidentImagesRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).incidentImageDao()
    )
    override val usersRepository = UsersRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).userDao(),
        context
    )
    override val userSessionsRepository = UserSessionsRepository(
        HoodAlertDatabase.DatabaseInstance.getInstance(context).userSessionDao()
    )

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getCurrentLocation(callback: (Pair<Double, Double>?) -> Unit) {
        if (checkLocationPermission(context)) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        callback(Pair(latitude, longitude))
                    } else {
                        callback(null)
                    }
                }
        } else {
            callback(null)
        }
    }

    private fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
