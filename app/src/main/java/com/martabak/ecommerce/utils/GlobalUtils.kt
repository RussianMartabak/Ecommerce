package com.martabak.ecommerce.utils

import android.content.SharedPreferences
import android.util.Log

object GlobalUtils {

    fun SharedPreferences.registerEntry() {
        this.edit().apply {
            putBoolean(FIRST_INSTALL, false)
            apply()
        }
    }

    fun SharedPreferences.isFirstTime(): Boolean {
        return this.getBoolean(FIRST_INSTALL, true)
    }

    fun SharedPreferences.putAccessToken(token: String) {
        this.edit().apply {
            putString(ACCESS_TOKEN, token)
            apply()
        }
    }

    fun SharedPreferences.getBearerToken(): String {
        val accessToken = this.getString(ACCESS_TOKEN, "")
        return "Bearer $accessToken"
    }

    fun SharedPreferences.isLoggedIn(): Boolean {
        return this.getBoolean(LOGGED_IN, false)
    }

    fun SharedPreferences.setUsername(name: String) {
        this.edit().apply {
            putString(USERNAME, name)
            Log.d("zaky", "username is set to $name")
            apply()
        }
    }

    fun SharedPreferences.logout() {
        this.edit().apply {
            putBoolean(LOGGED_IN, false)
            apply()
        }
    }

    fun SharedPreferences.getUsername(): String {
        return this.getString(USERNAME, "")!!
    }

    fun SharedPreferences.hasUsername(): Boolean {
        val result = this.getUsername()
        return result != ""
    }

    fun SharedPreferences.login() {
        this.edit().apply {
            putBoolean(LOGGED_IN, true)
            apply()
        }
    }

    fun SharedPreferences.clearUserData() {
        this.edit().apply{
            remove(LOGGED_IN)
            remove(ACCESS_TOKEN)
            remove(USERNAME)
            apply()
        }
    }

    fun SharedPreferences.setRefreshToken(rToken : String) {
        this.edit().apply{
            putString(REFRESH_TOKEN, rToken)
            apply()
        }
    }

    fun SharedPreferences.getRefreshToken() : String {
        return this.getString(REFRESH_TOKEN, "")!!
    }

    val DATABASE_NAME = "app_db"
    val FIRST_INSTALL = "first_install"
    val LOGGED_IN = "logged_in"
    val ACCESS_TOKEN = "access_token"
    val USERNAME = "username"
    val REFRESH_TOKEN = "refresh_token"
    val BASE_URL = "http://192.168.1.100:8000"
}