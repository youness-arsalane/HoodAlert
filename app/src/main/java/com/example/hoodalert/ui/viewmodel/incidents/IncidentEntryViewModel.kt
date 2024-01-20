package com.example.hoodalert.ui.viewmodel.incidents

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.service.GeocodingService
import com.example.hoodalert.ui.screens.communities.CommunityEditDestination
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class IncidentEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContainer: AppDataContainer
) : ViewModel() {
    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    private val communityId: Int =
        checkNotNull(savedStateHandle[CommunityEditDestination.communityIdArg])

    init {
        viewModelScope.launch {
            if (communityId != 0) {
                incidentUiState.community =
                    appContainer.communitiesRepository.getCommunity(communityId)
            }
        }
    }

    fun updateUiState(incident: Incident) {
        incidentUiState = IncidentUiState(
            incident = incident,
            isEntryValid = validateInput(incident),
            community = incidentUiState.community
        )
    }

    suspend fun saveIncident() {
        if (!validateInput()) {
            return
        }

        val incident = incidentUiState.incident

        if (incident.coordinatesEmpty() && !incident.addressEmpty()) {
            loadCoordinatesByAddress(
                street = incident.street,
                houseNumber = incident.houseNumber,
                zipcode = incident.zipcode,
                city = incident.city,
                country = incident.country,
                onFound = { incidentUiState ->
                    viewModelScope.launch {
                        appContainer.incidentsRepository.insertIncident(incidentUiState.incident)
                    }
                }
            )

        } else {
            appContainer.incidentsRepository.insertIncident(incident)
        }
    }

    suspend fun loadAddressByCoordinates(
        coordinates: Pair<Double, Double>
    ) {
        val context = appContainer.context
        val incident = incidentUiState.incident

        val app: ApplicationInfo = context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val bundle = app.metaData

        val apiKey = bundle.getString("com.google.android.geo.API_KEY").toString()

        val geocodingService = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeocodingService::class.java)


        val response = geocodingService.getAddressesByCoordinates(
            "${coordinates.first},${coordinates.second}",
            apiKey
        )

        val addressComponents = response.results.firstOrNull()

        if (addressComponents != null) {
            val components = addressComponents.address_components
            if (components != null) {
                val street = components.find { "route" in it.types }?.long_name ?: ""
                val houseNumber = components.find { "street_number" in it.types }?.long_name ?: ""
                val zipcode = components.find { "postal_code" in it.types }?.long_name ?: ""
                val city = components.find { "locality" in it.types }?.long_name ?: ""
                val country = components.find { "country" in it.types }?.long_name ?: ""

                updateUiState(
                    incident = incident.copy(
                        street = street,
                        houseNumber = houseNumber,
                        zipcode = zipcode,
                        city = city,
                        country = country
                    )
                )
            }
        }
    }

    private suspend fun loadCoordinatesByAddress(
        street: String,
        houseNumber: String,
        zipcode: String,
        city: String,
        country: String,
        onFound: (IncidentUiState) -> Unit = {}
    ) {
        val context = appContainer.context
        val incident = incidentUiState.incident

        val app: ApplicationInfo = context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val bundle = app.metaData

        val apiKey = bundle.getString("com.google.android.geo.API_KEY").toString()

        val geocodingService = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeocodingService::class.java)

        val response = geocodingService.getLocationsByAddress(
            "$street $houseNumber, $zipcode $city, $country",
            apiKey
        )

        val location = response.results.firstOrNull()?.geometry?.location

        if (location != null) {
            updateUiState(
                incident = incident.copy(
                    latitude = location.lat,
                    longitude = location.lng
                )
            )

            onFound(incidentUiState)
        }
    }

    private fun validateInput(
        uiState: Incident = incidentUiState.incident
    ): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }
}

data class IncidentUiState(
    var incident: Incident = emptyIncident(),
    var isEntryValid: Boolean = false,
    var community: Community? = null,
    var user: User? = null
)

fun emptyIncident(): Incident {
    return Incident(
        id = 0,
        communityId = 0,
        userId = 0,
        title = "",
        description = "",
        street = "",
        houseNumber = "",
        zipcode = "",
        city = "",
        country = "",
        latitude = null,
        longitude = null,
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
}

fun Incident.toIncidentUiState(isEntryValid: Boolean = false): IncidentUiState {
    return IncidentUiState(
        incident = this,
        isEntryValid = isEntryValid
    )
}

fun Incident.addressEmpty(): Boolean {
    return this.street == "" ||
            this.houseNumber == "" ||
            this.zipcode == "" ||
            this.city == "" ||
            this.country == ""
}

fun Incident.coordinatesEmpty(): Boolean {
    return this.latitude == null || this.longitude == null
}