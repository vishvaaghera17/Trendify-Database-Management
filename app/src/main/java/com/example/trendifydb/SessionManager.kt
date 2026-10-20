package com.example.trendifydb

import android.content.Context

object SessionManager {
    private const val PREFS_NAME = "trendify_session"
    private const val KEY_LOGGED_IN_EMAIL = "logged_in_email"
    private const val KEY_REMEMBERED_EMAIL = "remembered_email"

    fun setLoggedInEmail(context: Context, email: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_LOGGED_IN_EMAIL, email).apply()
    }

    fun getLoggedInEmail(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LOGGED_IN_EMAIL, null)
    }

    fun clearLoggedInEmail(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().remove(KEY_LOGGED_IN_EMAIL).apply()
    }

    fun setRememberedEmail(context: Context, email: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_REMEMBERED_EMAIL, email).apply()
    }

    fun getRememberedEmail(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_REMEMBERED_EMAIL, null)
    }

    fun clearRememberedEmail(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().remove(KEY_REMEMBERED_EMAIL).apply()
    }
}