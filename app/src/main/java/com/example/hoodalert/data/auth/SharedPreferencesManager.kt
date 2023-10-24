package com.example.hoodalert.data.auth

import android.content.Context
import android.content.SharedPreferences
import com.example.hoodalert.data.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date

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

    fun generateToken(user: User): String? {
        val expiration = Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7))

        return Jwts.builder()
            .setSubject(user.email)
            .claim("userId", user.id)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, "secretKey")
            .compact();
    }
}
