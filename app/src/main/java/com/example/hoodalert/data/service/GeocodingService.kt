package com.example.hoodalert.data.service

import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Json

interface GeocodingService {
    @GET("maps/api/geocode/json")
    suspend fun getAddresses(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): GeocodeResponse
}

data class GeocodeResponse(
    val results: List<AddressComponents>,
    val status: String
)

data class AddressComponents(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val place_id: String,
    val types: List<String>
)

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)
