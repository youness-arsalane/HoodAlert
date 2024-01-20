package com.example.hoodalert.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerializedName("id")
    var id: Int,
    @SerializedName("email")
    var email: String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("createdAt")
    var createdAt: LocalDateTime,
    @SerializedName("updatedAt")
    var updatedAt: LocalDateTime
)