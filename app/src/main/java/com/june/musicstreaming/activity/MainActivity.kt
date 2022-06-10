package com.june.musicstreaming.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.june.musicstreaming.R
import com.june.musicstreaming.databinding.ActivityMainBinding
import com.june.musicstreaming.fragment.PlayerFragment
import com.june.musicstreaming.network.NetworkConnectionCallback
import com.june.musicstreaming.service.ForegroundService
import com.june.musicstreaming.service.Notification

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val networkStateCheck: NetworkConnectionCallback by lazy { NetworkConnectionCallback(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        networkStateCheck.register()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment.newInstance())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateCheck.unregister()
        Notification(this).cancelNotification()
    }
}