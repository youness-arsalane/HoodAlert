package com.example.hoodalert.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    val updatedAt: Date
)