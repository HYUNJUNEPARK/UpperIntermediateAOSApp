package com.june.githubrepositoryapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.june.githubrepositoryapp.R
import com.june.githubrepositoryapp.databinding.ActivityRepositoryBinding
import com.june.githubrepositoryapp.extensions.loadCenterInside
import com.june.githubrepositoryapp.retrofit.RetrofitUtil
import com.june.githubrepositoryapp.room.DatabaseProvider
import com.june.githubrepositoryapp.room.GithubRepoEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RepositoryActivity : AppCompatActivity(),CoroutineScope {
    private val binding by lazy { ActivityRepositoryBinding.inflate(layoutInflater) }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    companion object {
        const val REPOSITORY_OWNER_KEY = "repositoryOwner"
        const val REPOSITORY_NAME_KEY = "repositoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val repositoryOwner = intent.getStringExtra(REPOSITORY_OWNER_KEY) ?: kotlin.run {
            Toast.makeText(this, "Repository Owner 이름이 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val repositoryName = intent.getStringExtra(REPOSITORY_NAME_KEY) ?: kotlin.run {
            Toast.makeText(this, "Repository 이름이 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        launch {
            loadRepository(repositoryOwner, repositoryName)?.let { githubRepoEntity ->
                setData(githubRepoEntity)
            } ?: run {
                Toast.makeText(this@RepositoryActivity, "Repository 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private suspend fun loadRepository(repositoryOwner: String, repositoryName: String): GithubRepoEntity? =
        withContext(coroutineContext) {
            var repository: GithubRepoEntity? = null
            withContext(Dispatchers.IO) {
                val response = RetrofitUtil.githubApiService.getRepository(
                    ownerLogin = repositoryOwner,
                    repoName = repositoryName
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    withContext(Dispatchers.Main) {
                        body?.let { repo ->
                            repository = repo
                        }
                    }
                }
            }
            repository
        }

    private fun setData(githubRepoEntity: GithubRepoEntity) = with(binding) {
        Log.e("data", githubRepoEntity.toString())
        showLoading(false)

        ownerProfileImageView.loadCenterInside(githubRepoEntity.owner.avatarUrl, 42f)
        ownerNameAndRepoNameTextView.text= "${githubRepoEntity.owner.login}/${githubRepoEntity.name}"
        stargazersCountText.text = githubRepoEntity.stargazersCount.toString()
        githubRepoEntity.language?.let { language ->
            languageText.isGone = false
            languageText.text = language
        } ?: kotlin.run {
            languageText.isGone = true
            languageText.text = ""
        }
        descriptionTextView.text = githubRepoEntity.description
        updateTimeTextView.text = githubRepoEntity.updatedAt

        setLikeState(githubRepoEntity)
    }

    private fun setLikeState(githubRepoEntity: GithubRepoEntity) = launch {
        withContext(Dispatchers.IO) {
            val repository = DatabaseProvider.provideDB(this@RepositoryActivity)
                .searchHistoryDao()
                .getRepository(githubRepoEntity.fullName)
            val isLike = repository != null
            withContext(Dispatchers.Main) {
                setLikeImage(isLike)
                binding.likeButton.setOnClickListener {
                    likeRepository(githubRepoEntity, isLike)
                }
            }
        }
    }

    private fun showLoading(isShown: Boolean) = with(binding) {
        progressBar.isGone = isShown.not()
    }

    private fun setLikeImage(isLike: Boolean) {
        binding.likeButton.setImageDrawable(
            ContextCompat.getDrawable(this@RepositoryActivity,
            if (isLike) {
                R.drawable.ic_like
            } else {
                R.drawable.ic_dislike
            }
        ))
    }

    private fun likeRepository(githubRepoEntity: GithubRepoEntity, isLike: Boolean) = launch {
        withContext(Dispatchers.IO) {
            val dao = DatabaseProvider.provideDB(this@RepositoryActivity).searchHistoryDao()
            if (isLike) {
                dao.remove(githubRepoEntity.fullName)
            } else {
                dao.insert(githubRepoEntity)
            }
            withContext(Dispatchers.Main) {
                setLikeImage(isLike.not())
            }
        }
    }
}