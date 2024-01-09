package com.example.hoodalert.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hoodalert.data.dao.CommunityDao
import com.example.hoodalert.data.dao.CommunityUserDao
import com.example.hoodalert.data.dao.IncidentDao
import com.example.hoodalert.data.dao.UserDao
import com.example.hoodalert.data.dao.UserSessionDao
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.data.model.UserSession
import com.example.hoodalert.util.Converters

@Database(
    entities = [
        Community::class,
        CommunityUser::class,
        Incident::class,
        User::class,
        UserSession::class,
    ],
    version = 5
)
@TypeConverters(Converters::class)
abstract class HoodAlertDatabase : RoomDatabase() {
    abstract fun communityDao(): CommunityDao
    abstract fun communityUserDao(): CommunityUserDao
    abstract fun incidentDao(): IncidentDao
    abstract fun userDao(): UserDao
    abstract fun userSessionDao(): UserSessionDao

    object DatabaseInstance {
        @Volatile
        private var instance: HoodAlertDatabase? = null

        fun getInstance(context: Context): HoodAlertDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HoodAlertDatabase::class.java,
                        "hood_alert"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance!!
            }
        }
    }
}
