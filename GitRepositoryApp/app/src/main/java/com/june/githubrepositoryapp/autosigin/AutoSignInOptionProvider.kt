package com.june.githubrepositoryapp.autosigin

import android.content.Context
import androidx.preference.PreferenceManager
import com.june.githubrepositoryapp.Constants.AUTO_SIGN_IN_OFF
import com.june.githubrepositoryapp.Constants.PREFERENCE_NAME_AUTO_SIGN_IN

class AutoSignInOptionProvider(context: Context) {
    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun updateAutoSignInValue(option: Boolean) {
        sharedPref.edit().run {
            putBoolean(PREFERENCE_NAME_AUTO_SIGN_IN, option)
            apply()
        }
    }

    val option: Boolean?
        get() = sharedPref.getBoolean(PREFERENCE_NAME_AUTO_SIGN_IN, false)
}