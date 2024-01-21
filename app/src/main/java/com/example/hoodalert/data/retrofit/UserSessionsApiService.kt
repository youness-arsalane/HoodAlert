package com.example.hoodalert.data.retrofit

import com.example.hoodalert.data.model.UserSession
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserSessionsApiService {
    @GET("/userSessions")
    suspend fun getUserSessions(): List<UserSession>

    @GET("/userSessions/{id}")
    suspend fun getUserSession(@Path("id") id: Int): UserSession

    @GET("/userSessions")
    suspend fun getUserSessionByToken(@Query("token") token: String): UserSession

    @POST("/userSessions")
    suspend fun insertUserSession(@Body userSession: UserSession): UserSession

    @PUT("/userSessions/{id}")
    suspend fun updateUserSession(@Path("id") id: Int, @Body userSession: UserSession): UserSession

    @DELETE("/userSessions/{id}")
    suspend fun deleteUserSession(@Path("id") id: Int)
}
