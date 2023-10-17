package com.example.hoodalert.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "community_users",
    foreignKeys = [
        ForeignKey(
            entity = Community::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("community_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CommunityUser(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "community_id")
    val communityId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    val updatedAt: Date
)