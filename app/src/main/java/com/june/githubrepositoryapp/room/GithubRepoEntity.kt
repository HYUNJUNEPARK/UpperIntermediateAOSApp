package com.june.githubrepositoryapp.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.june.githubrepositoryapp.Constants.ROOM_TABLE_NAME
import com.june.githubrepositoryapp.model.GithubOwner

@Entity(tableName = "GithubRepository")
data class GithubRepoEntity(
    val name: String,
    @PrimaryKey
    val fullName: String,
    @Embedded
    val owner: GithubOwner,
    val description: String?,
    val language: String?,
    val updatedAt: String,
    val stargazersCount: Int
)
