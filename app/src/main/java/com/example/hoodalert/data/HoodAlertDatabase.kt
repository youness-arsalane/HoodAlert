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
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.Incident
import com.example.hoodalert.data.model.User
import com.example.hoodalert.util.Converters
import java.io.File

@Database(
    entities = [
        Community::class,
        CommunityUser::class,
        Incident::class,
        User::class,
    ],
    version = 5
//    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HoodAlertDatabase : RoomDatabase() {
    abstract fun communityDao(): CommunityDao
    abstract fun communityUserDao(): CommunityUserDao
    abstract fun incidentDao(): IncidentDao
    abstract fun userDao(): UserDao

    object DatabaseInstance {
        @Volatile
        private var Instance: HoodAlertDatabase? = null

        fun getDatabase(context: Context): HoodAlertDatabase {
            synchronized(this) {
                if (Instance == null) {
                    Instance = Room.databaseBuilder(context, HoodAlertDatabase::class.java, "HoodAlert")
                        .fallbackToDestructiveMigration()
                        .addCallback(HoodAlertCallback(context))
                        .build()
                }

                return Instance!!;
            }

//            return Instance ?: synchronized(this) {
//                Room.databaseBuilder(context, HoodAlertDatabase::class.java, "HoodAlert")
//                    .fallbackToDestructiveMigration()
//                    .addCallback(HoodAlertCallback(context))
//                    .build()
//                    .also { Instance = it }
//            }
        }
    }
}
