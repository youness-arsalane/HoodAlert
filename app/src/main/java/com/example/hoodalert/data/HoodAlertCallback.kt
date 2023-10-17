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
        val database = HoodAlertDatabase.DatabaseInstance.getDatabase(context)

        val userDao = database.userDao()
        val communityDao = database.communityDao()
        val communityUserDao = database.communityUserDao()

        val user = User(1, "youness@adaptivity.nl", "Youness", "Arsalane", "password", Date(), Date())
        val community = Community(1, "Tilburg West", Date(), Date())
        val communityUser = CommunityUser(1, 1, 1, Date(), Date())

        CoroutineScope(Dispatchers.IO).launch {
            userDao.insert(user)
            communityDao.insert(community)
            communityUserDao.insert(communityUser)
        }
    }
}
