package com.example.hoodalert.data.retrofit

import com.example.hoodalert.data.model.CommunityUser
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CommunityUsersApiService {
    @GET("/communityUsers")
    suspend fun getCommunityUsers(): List<CommunityUser>

    @GET("/communityUsers/{id}")
    suspend fun getCommunityUser(@Path("id") id: Int): CommunityUser

    @GET("/communityUsers/byCommunityIdAndUserId/{communityId}/{userId}")
    suspend fun findByCommunityIdAndUserId(
        @Path("communityId") communityId: Int,
        @Path("userId") userId: Int
    ): CommunityUser

    @POST("/communityUsers")
    suspend fun insertCommunityUser(@Body communityUser: CommunityUser): CommunityUser

    @PUT("/communityUsers/{id}")
    suspend fun updateCommunityUser(
        @Path("id") id: Int,
        @Body communityUser: CommunityUser
    ): CommunityUser

    @DELETE("/communityUsers/{id}")
    suspend fun deleteCommunityUser(@Path("id") id: Int)
}
