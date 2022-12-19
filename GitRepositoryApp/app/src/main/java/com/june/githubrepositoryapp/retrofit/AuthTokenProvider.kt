package com.june.githubrepositoryapp.retrofit

import android.content.Context
import androidx.preference.PreferenceManager
import com.june.githubrepositoryapp.Constants.PREFERENCE_NAME_KEY_AUTH_TOKEN

class AuthTokenProvider(context: Context) {
    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun updateToken(token: String) {
        sharedPref.edit().run {
            putString(PREFERENCE_NAME_KEY_AUTH_TOKEN, token)
            apply()
        }
    }

    fun initializeToken() {
        sharedPref.edit().run {
            putString(PREFERENCE_NAME_KEY_AUTH_TOKEN, null)
            apply()
        }
    }

    val token: String?
        get() = sharedPref.getString(PREFERENCE_NAME_KEY_AUTH_TOKEN, null)
}
