package com.june.githubrepositoryapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import com.june.githubrepositoryapp.adapter.RepositoryRecyclerAdapter
import com.june.githubrepositoryapp.databinding.ActivitySearchBinding
import com.june.githubrepositoryapp.retrofit.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()
        initViews()
        bindViews()
    }

    private fun initAdapter() = with(binding) {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        recyclerView.adapter = adapter

    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            val searchKeyword = searchBarInputView.text.toString()
            searchKeyword(searchKeyword)
        }
    }

    private fun searchKeyword(keywordString: String) = launch {
        withContext(Dispatchers.IO) {
            val response = RetrofitUtil.githubApiService.searchRepositories(keywordString)

            if (response.isSuccessful) {
                val body = response.body()
                withContext(coroutineContext) {
                    Log.e("testLog", "${body.toString()}")
                }
            }
        }
    }
}