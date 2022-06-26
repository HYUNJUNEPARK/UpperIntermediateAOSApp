package com.june.githubrepositoryapp.retrofit

import android.content.Context
import androidx.preference.PreferenceManager
import com.june.githubrepositoryapp.Constants

class AuthTokenProvider(private val context: Context) {
    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun updateToken(token: String) {
        sharedPref.edit().run {
            putString(Constants.PREFERENCE_NAME_KEY_AUTH_TOKEN, token)
            apply()
        }

    }
    val token: String?
        get() = sharedPref
                    .getString(Constants.PREFERENCE_NAME_KEY_AUTH_TOKEN, null)

}
