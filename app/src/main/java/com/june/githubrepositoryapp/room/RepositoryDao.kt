package com.june.githubrepositoryapp.room

import androidx.room.*
import com.june.githubrepositoryapp.Constants.ROOM_TABLE_NAME

@Dao
interface RepositoryDao {
    @Insert
    suspend fun insert(repo: GithubRepoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repoList: List<GithubRepoEntity>)

    @Query("SELECT * FROM $ROOM_TABLE_NAME")
    suspend fun getHistory(): List<GithubRepoEntity>

    @Query("SELECT * FROM $ROOM_TABLE_NAME WHERE fullName = :repoName")
    suspend fun getRepository(repoName: String): GithubRepoEntity?

    @Query("DELETE FROM $ROOM_TABLE_NAME WHERE fullName = :repoName")
    suspend fun remove(repoName: String)

    @Query("DELETE FROM $ROOM_TABLE_NAME")
    suspend fun clearAll()
}
