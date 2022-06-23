package com.june.githubrepositoryapp.retrofit

import android.content.Context
import android.preference.PreferenceManager
import com.june.githubrepositoryapp.Constants

class AuthTokenProvider(private val context: Context) {
    fun updateToken(token: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(Constants.KEY_AUTH_TOKEN, token)
                .apply()
    }

    val token: String?
        get() = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constants.KEY_AUTH_TOKEN, null)

}
