package com.june.githubrepositoryapp.model

import com.june.githubrepositoryapp.room.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)
