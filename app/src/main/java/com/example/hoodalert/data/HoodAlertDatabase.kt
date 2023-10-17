package com.example.hoodalert.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hoodalert.data.dao.IncidentDao
import com.example.hoodalert.data.model.Incident

@Database(entities = [Incident::class], version = 2, exportSchema = false)
abstract class HoodAlertDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao

    companion object {
        @Volatile
        private var Instance: HoodAlertDatabase? = null

        fun getDatabase(context: Context): HoodAlertDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HoodAlertDatabase::class.java, "HoodAlert")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
