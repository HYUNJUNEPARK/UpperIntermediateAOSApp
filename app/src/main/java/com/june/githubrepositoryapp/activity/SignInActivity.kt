package com.june.githubrepositoryapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.june.githubrepositoryapp.BuildConfig
import com.june.githubrepositoryapp.Constants.AUTO_SIGN_IN_OFF
import com.june.githubrepositoryapp.Constants.AUTO_SIGN_IN_ON
import com.june.githubrepositoryapp.autosigin.AutoSignInOptionProvider
import com.june.githubrepositoryapp.databinding.ActivitySignInBinding
import com.june.githubrepositoryapp.retrofit.AuthTokenProvider
import com.june.githubrepositoryapp.retrofit.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SignInActivity : AppCompatActivity(), CoroutineScope {
    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private val authTokenProvider by lazy { AuthTokenProvider(this) }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        autoSignIn()
        initAutoLoginSwitch()
    }

    private fun initAutoLoginSwitch() {
        binding.autoLoginSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AutoSignInOptionProvider(this).updateAutoSignInValue(AUTO_SIGN_IN_ON)
            }
            else {
                AutoSignInOptionProvider(this).updateAutoSignInValue(AUTO_SIGN_IN_OFF)
            }
        }
    }

    private fun autoSignIn() {
        val option = AutoSignInOptionProvider(this).option
        if (option == AUTO_SIGN_IN_ON && checkAuthCodeExist()) {
            launchMainActivity()
        }
    }

    fun loginButtonClicked(v: View) {
        //loginUri : https://githunb.com/login/oauth/authorize?client_id=5d9e****
        val loginUri = Uri.Builder().scheme("https").authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
            .build()

        //현재화면에서 커스텀 tab 으로 이동할 수 있는 intent
        CustomTabsIntent.Builder().build().also { customTabsIntent ->
            customTabsIntent.launchUrl(this, loginUri)
        }
    }

    private fun checkAuthCodeExist(): Boolean = authTokenProvider.token.isNullOrEmpty().not()

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //SignIn Activity 가 종료됨
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    //intent 를 받아서 여기서 처리
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.getQueryParameter("code")?.let { code ->
            launch(coroutineContext) {
                showProgress()
                accessToken(code)
                dismissProgress()
            }
        }
    }

    private suspend fun accessToken(code: String) = withContext(Dispatchers.IO) {
        val response = RetrofitUtil.authApiService.getAccessToken(
            clientId = BuildConfig.GITHUB_CLIENT_ID,
            clientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
            code = code
        )
        if (response.isSuccessful) {
            val accessToken = response.body()?.accessToken ?: ""
            Log.d("testLog", "accessToken: $accessToken")
            if (accessToken.isNotEmpty()) {
                authTokenProvider.updateToken(accessToken)
            }
            else {
                Toast.makeText(this@SignInActivity, "access token is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun showProgress() = withContext(coroutineContext) {
        with(binding) {
            loginButton.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            progressTextView.visibility = View.VISIBLE
        }
    }

    private suspend fun dismissProgress() = withContext(coroutineContext) {
        with(binding) {
            loginButton.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
            progressTextView.visibility = View.INVISIBLE
        }
    }
}