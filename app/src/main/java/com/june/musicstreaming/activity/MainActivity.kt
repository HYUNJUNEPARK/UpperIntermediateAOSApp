package com.june.musicstreaming.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.june.musicstreaming.R
import com.june.musicstreaming.databinding.ActivityMainBinding
import com.june.musicstreaming.fragment.PlayerFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        attachFragment()
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment.newInstance())
            .commit()
    }
}