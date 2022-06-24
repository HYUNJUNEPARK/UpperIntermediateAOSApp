package com.june.githubrepositoryapp.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.june.githubrepositoryapp.databinding.ActivityMainBinding
import com.june.githubrepositoryapp.model.GithubOwner
import com.june.githubrepositoryapp.model.GithubRepoEntity
import com.june.githubrepositoryapp.room.DataBaseProvider
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    val repositoryDao by lazy { DataBaseProvider.provideDB(applicationContext).repositoryDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        launch {
            addMockData()
            val githubRepositories = loadGithubRepositories()
            withContext(coroutineContext) {
                Log.e("testLog", "onCreate: ${githubRepositories.toString()}")
            }
        }
    }

    private suspend fun addMockData() = withContext(Dispatchers.IO) {
        val mockData = (0 until 10).map {
            GithubRepoEntity(
                name = "repo $it",
                fullName = "name $it",
                owner = GithubOwner(
                    "login",
                    "avatarUrl"
                ),
                description = null,
                language = null,
                updatedAt = Date().toString(),
                stargazersCount = it
            )
        }
        repositoryDao.insertAll(mockData)
    }

    private suspend fun loadGithubRepositories() = withContext(Dispatchers.IO) {
        val repositories = repositoryDao.getHistory()
        return@withContext repositories
    }
}