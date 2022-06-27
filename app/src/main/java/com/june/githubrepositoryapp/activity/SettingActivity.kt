package com.june.githubrepositoryapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.june.githubrepositoryapp.R
import com.june.githubrepositoryapp.autosigin.SettingFragment
import com.june.githubrepositoryapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SettingFragment())
            .commit()
    }
}