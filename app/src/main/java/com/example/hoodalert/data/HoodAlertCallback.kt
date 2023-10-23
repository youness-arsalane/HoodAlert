package com.example.hoodalert.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase.Callback
import com.example.hoodalert.data.model.Community
import com.example.hoodalert.data.model.CommunityUser
import com.example.hoodalert.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class HoodAlertCallback(private val context: Context) : Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val database = HoodAlertDatabase.DatabaseInstance.getDatabase(context)
            val userDao = database.userDao()
            val communityDao = database.communityDao()
            val communityUserDao = database.communityUserDao()

            val user = User(
                id = 1,
                email = "youness@adaptivity.nl",
                firstName = "Youness",
                lastName = "Arsalane",
                password = "password",
                createdAt = Date(),
                updatedAt = Date()
            )

            val community = Community(
                id = 1,
                name = "Tilburg West",
                createdAt = Date(),
                updatedAt = Date()
            )

            val communityUser = CommunityUser(
                id = 1,
                communityId = 1,
                userId = 1,
                createdAt = Date(),
                updatedAt = Date()
            )

            userDao.insert(user)
            communityDao.insert(community)
            communityUserDao.insert(communityUser)
        }
    }
}
