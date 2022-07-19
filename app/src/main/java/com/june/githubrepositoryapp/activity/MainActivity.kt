package com.june.githubrepositoryapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import com.june.githubrepositoryapp.R
import com.june.githubrepositoryapp.adapter.RepositoryRecyclerAdapter
import com.june.githubrepositoryapp.autosigin.SettingActivity
import com.june.githubrepositoryapp.databinding.ActivityMainBinding
import com.june.githubrepositoryapp.room.DatabaseProvider
import com.june.githubrepositoryapp.room.GithubRepoEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityMainBinding
    private val repositoryDao by lazy { DatabaseProvider.provideDB(applicationContext).searchHistoryDao() }
    private lateinit var adapter: RepositoryRecyclerAdapter
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.mainActivity = this
        initAdapter()
        //initViews()


        launch {
//            addMockData()
            val githubRepositories = loadGithubRepositories()
            withContext(coroutineContext) {
                Log.e("testLog", "onCreate: ${githubRepositories.toString()}")
            }
        }
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        launch(coroutineContext) {
            loadLikedRepositoryList()
        }
    }

    private suspend fun loadLikedRepositoryList() = withContext(Dispatchers.IO) {
        val repoList = DatabaseProvider.provideDB(this@MainActivity).searchHistoryDao().getHistory()
        withContext(Dispatchers.Main) {
            setData(repoList)
        }
    }

    private fun setData(githubRepositoryList: List<GithubRepoEntity>) = with(binding) {
        if (githubRepositoryList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        }
        else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setSearchResultList(githubRepositoryList) {
                startActivity(
                    Intent(this@MainActivity, RepositoryActivity::class.java).apply {
                        putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
                        putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)
                    }
                )
            }
        }
    }

    //initViews()
    fun searchButtonClicked() {
        Log.d("testLog", "searchButtonClicked: ")
        val intent = Intent(this@MainActivity, SearchActivity::class.java)
        startActivity(intent)
    }

    private suspend fun loadGithubRepositories() = withContext(Dispatchers.IO) {
        val repositories = repositoryDao.getHistory()
        return@withContext repositories
    }

//setting menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settingMenu -> {
                val intent = Intent(this, SettingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return true
            }
        }
        return false
    }

//    private suspend fun addMockData() = withContext(Dispatchers.IO) {
//        val mockData = (0 until 10).map {
//            GithubRepoEntity(
//                name = "repo $it",
//                fullName = "name $it",
//                owner = GithubOwner(
//                    "login",
//                    "avatarUrl"
//                ),
//                description = null,
//                language = null,
//                updatedAt = Date().toString(),
//                stargazersCount = it
//            )
//        }
//        repositoryDao.insertAll(mockData)
//    }
}