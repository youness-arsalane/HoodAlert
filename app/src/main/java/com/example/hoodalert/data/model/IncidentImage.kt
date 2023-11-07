package com.example.hoodalert.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "incident_images",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("incident_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IncidentImage(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "incident_id")
    var incidentId: Int,

    @ColumnInfo(name = "path")
    var path: String,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt: Date,
)