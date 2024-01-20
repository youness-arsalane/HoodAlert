package com.example.hoodalert.data.retrofit

import com.example.hoodalert.data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApiService {
    @GET("/users")
    suspend fun getUsers(): List<User>

    @GET("/users/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @GET("/users/byEmail/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @POST("/users")
    suspend fun insertUser(@Body user: User): User

    @PUT("/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): User

    @DELETE("/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int)
}
