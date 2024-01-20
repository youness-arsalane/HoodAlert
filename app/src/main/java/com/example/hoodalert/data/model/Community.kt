package com.example.hoodalert.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class Community(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("createdAt")
    var createdAt: LocalDateTime,
    @SerializedName("updatedAt")
    var updatedAt: LocalDateTime
)