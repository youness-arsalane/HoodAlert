package com.example.hoodalert.data.retrofit

import com.example.hoodalert.data.model.Incident
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IncidentsApiService {
    @GET("/incidents")
    suspend fun getIncidents(): List<Incident>

    @GET("/incidents/byCommunityId{communityId}")
    suspend fun findByCommunityId(@Path("communityId") communityId: Int): List<Incident>

    @GET("/incidents/{id}")
    suspend fun getIncident(@Path("id") id: Int): Incident

    @POST("/incidents")
    suspend fun insertIncident(@Body incident: Incident): Incident

    @PUT("/incidents/{id}")
    suspend fun updateIncident(@Path("id") id: Int, @Body incident: Incident): Incident

    @DELETE("/incidents/{id}")
    suspend fun deleteIncident(@Path("id") id: Int)
}
