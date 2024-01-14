package com.example.hoodalert.data.service

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {
    @GET("maps/api/geocode/json")
    suspend fun getAddressesByCoordinates(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): GeocodeResponse

    @GET("maps/api/geocode/json")
    suspend fun getLocationsByAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodeResponse
}

data class GeocodeResponse(
    val results: List<AddressComponents>,
    val status: String
)

data class AddressComponents(
    val address_components: List<AddressComponent>,
    val geometry: Geometry,
    val formatted_address: String,
    val place_id: String,
    val types: List<String>
)

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

data class Geometry(
    val location: GeometryLocation
)

data class GeometryLocation(
    val lat: Double,
    val lng: Double
)
