package com.june.githubrepositoryapp.room

import androidx.room.*
import com.june.githubrepositoryapp.Constants.ROOM_TABLE_NAME

@Dao
interface RepositoryDao {
    @Insert
    fun insert(repo: GithubRepoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repoList: List<GithubRepoEntity>)

    @Query("SELECT * FROM githubrepository")
    fun getHistory(): List<GithubRepoEntity>

    @Query("SELECT * FROM githubrepository WHERE fullName = :repoName")
    fun getRepository(repoName: String): GithubRepoEntity?

    @Query("DELETE FROM  githubrepository WHERE fullName = :repoName")
    fun remove(repoName: String)

    @Query("DELETE FROM githubrepository")
    fun clearAll()
}
