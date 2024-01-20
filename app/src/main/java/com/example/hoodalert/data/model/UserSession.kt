package com.example.hoodalert.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class UserSession(
    @SerializedName("id")
    var id: Int,
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("token")
    var token: String,
    @SerializedName("createdAt")
    var createdAt: LocalDateTime,
    @SerializedName("updatedAt")
    var updatedAt: LocalDateTime
)