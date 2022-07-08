package com.june.githubrepositoryapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.june.githubrepositoryapp.databinding.ActivityRepositoryBinding

class RepositoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRepositoryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}