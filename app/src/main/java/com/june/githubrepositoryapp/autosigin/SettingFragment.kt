package com.june.githubrepositoryapp.autosigin

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.june.githubrepositoryapp.R

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}