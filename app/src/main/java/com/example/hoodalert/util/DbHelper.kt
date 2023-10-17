package com.example.hoodalert.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "HoodAlert"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE incident (id INTEGER PRIMARY KEY, title TEXT)")
        db.execSQL("CREATE TABLE community (id INTEGER PRIMARY KEY, name TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS incident")
        db.execSQL("DROP TABLE IF EXISTS community")
        onCreate(db)
    }
}