package com.example.hoodalert

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.hoodalert.data.AppContainer
import com.example.hoodalert.data.AppDataContainer
import java.io.File
import java.io.FileOutputStream

class HoodAlertApplication : Application() {
    lateinit var container: AppDataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
