package com.example.hoodalert.data.auth

import android.content.Context
import android.content.SharedPreferences

private const val USER_TOKEN_KEY = "userToken";

class SharedPreferencesManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("HoodAlertPrefs", Context.MODE_PRIVATE)

    fun saveUserToken(token: String) {
        sharedPreferences.edit().putString(USER_TOKEN_KEY, token).apply()
    }

    fun getUserToken(): String? {
        return sharedPreferences.getString(USER_TOKEN_KEY, null)
    }

    fun clearUserToken() {
        sharedPreferences.edit().remove(USER_TOKEN_KEY).apply()
    }

    fun generateToken(tokenLength: Int = 32): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..tokenLength)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
