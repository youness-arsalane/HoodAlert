package com.example.hoodalert.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class CommunityUser(
    @SerializedName("id")
    var id: Int,
    @SerializedName("communityId")
    var communityId: Int,
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("isAdmin")
    var isAdmin: Boolean,
    @SerializedName("createdAt")
    var createdAt: LocalDateTime,
    @SerializedName("updatedAt")
    var updatedAt: LocalDateTime
)