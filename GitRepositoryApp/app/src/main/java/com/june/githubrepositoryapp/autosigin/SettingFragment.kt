package com.june.githubrepositoryapp.autosigin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.june.githubrepositoryapp.Constants.PREFERENCE_NAME_AUTO_SIGN_IN
import com.june.githubrepositoryapp.R
import com.june.githubrepositoryapp.activity.SignInActivity
import com.june.githubrepositoryapp.retrofit.AuthTokenProvider

class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, preferenceKey: String?) {

        val isAutoSignIn = PreferenceManager.getDefaultSharedPreferences(context)
                                    .getBoolean(PREFERENCE_NAME_AUTO_SIGN_IN, false)

        if (isAutoSignIn) {
            AlertDialog.Builder(requireContext())
                .setTitle("로그인 설정")
                .setMessage("자동로그인 기능이 설정되었습니다. \n 다시 로그인 해주세요")
                .setPositiveButton("확인") { _, _ ->
                    val intent = Intent(
                        requireActivity().applicationContext,
                        SignInActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                .create()
                .show()
        }
        else {
            AlertDialog.Builder(requireContext())
                .setTitle("로그인 설정")
                .setMessage("자동로그인 기능이 해제되었습니다. \n 다시 로그인 해주세요")
                .setPositiveButton("확인") { _, _ ->
                    AuthTokenProvider(requireContext()).initializeToken()

                    val intent = Intent(
                        requireActivity().applicationContext,
                        SignInActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                .create()
                .show()
        }

        Log.d("testLog", "onSharedPreferenceChanged p0: $preferences")
        Log.d("testLog", "onSharedPreferenceChanged p1: $preferenceKey")


    }

    override fun onResume() {
        super.onResume()

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

}