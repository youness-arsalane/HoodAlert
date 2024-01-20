package com.example.hoodalert.data.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitBuilder {
//    private const val BASE_URL = "http://127.0.0.1:8080"
    private const val BASE_URL = "http://192.168.2.13:8080"
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .build()
    }

    val communitiesApiService: CommunitiesApiService =
        getRetrofit().create(CommunitiesApiService::class.java)
    val communityUsersApiService: CommunityUsersApiService =
        getRetrofit().create(CommunityUsersApiService::class.java)
    val incidentsApiService: IncidentsApiService =
        getRetrofit().create(IncidentsApiService::class.java)
    val usersApiService: UsersApiService =
        getRetrofit().create(UsersApiService::class.java)
    val userSessionsApiService: UserSessionsApiService =
        getRetrofit().create(UserSessionsApiService::class.java)
}