package com.example.hoodalert.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "incidents",
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
data class Incident(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "community_id")
    var communityId: Int,

    @ColumnInfo(name = "user_id")
    var userId: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "street")
    var street: String,

    @ColumnInfo(name = "houseNumber")
    var houseNumber: String,

    @ColumnInfo(name = "zipcode")
    var zipcode: String,

    @ColumnInfo(name = "city")
    var city: String,

    @ColumnInfo(name = "country")
    var country: String,

    @ColumnInfo(name = "latitude")
    var latitude: Double?,

    @ColumnInfo(name = "longitude")
    var longitude: Double?,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt: Date,
)