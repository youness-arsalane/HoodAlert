package com.example.hoodalert.data.retrofit

import com.example.hoodalert.data.model.Community
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CommunitiesApiService {
    @GET("/communities")
    suspend fun getCommunities(): List<Community>

    @GET("/communities/{id}")
    suspend fun getCommunity(@Path("id") id: Int): Community

    @POST("/communities")
    suspend fun insertCommunity(@Body community: Community): Community

    @PUT("/communities/{id}")
    suspend fun updateCommunity(@Path("id") id: Int, @Body community: Community): Community

    @DELETE("/communities/{id}")
    suspend fun deleteCommunity(@Path("id") id: Int)
}
