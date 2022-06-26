package com.june.githubrepositoryapp.room

import android.content.Context
import androidx.room.Room
import com.june.githubrepositoryapp.Constants.DB_NAME

object DataBaseProvider {
    fun provideDB(applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        SimpleGithubDatabase::class.java, DB_NAME
    ).build()
}
