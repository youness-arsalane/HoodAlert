package com.example.hoodalert.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class Incident(
    @SerializedName("id")
    var id: Int,
    @SerializedName("communityId")
    var communityId: Int,
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("street")
    var street: String,
    @SerializedName("houseNumber")
    var houseNumber: String,
    @SerializedName("zipcode")
    var zipcode: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("latitude")
    var latitude: Double?,
    @SerializedName("longitude")
    var longitude: Double?,
    @SerializedName("createdAt")
    var createdAt: LocalDateTime,
    @SerializedName("updatedAt")
    var updatedAt: LocalDateTime
)