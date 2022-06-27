package com.june.githubrepositoryapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}