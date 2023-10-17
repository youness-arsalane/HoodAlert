package com.example.hoodalert.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident")
data class Incident(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
)
