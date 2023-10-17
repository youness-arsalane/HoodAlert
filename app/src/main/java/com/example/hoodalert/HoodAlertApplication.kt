package com.example.hoodalert

import android.app.Application
import com.example.hoodalert.data.AppContainer
import com.example.hoodalert.data.AppDataContainer

class HoodAlertApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
