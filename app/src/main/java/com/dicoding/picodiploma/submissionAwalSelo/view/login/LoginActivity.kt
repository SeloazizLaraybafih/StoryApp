package com.dicoding.picodiploma.submissionAwalSelo.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.submissionAwalSelo.data.model.App
import com.dicoding.picodiploma.submissionAwalSelo.data.model.LoginRequest
import com.dicoding.picodiploma.submissionAwalSelo.data.model.LoginResponse
import com.dicoding.picodiploma.submissionAwalSelo.data.pref.UserModel
import com.dicoding.picodiploma.submissionAwalSelo.data.pref.UserPreference
import com.dicoding.picodiploma.submissionAwalSelo.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.submissionAwalSelo.service.RetrofitClient
import com.dicoding.picodiploma.submissionAwalSelo.view.ViewModelFactory
import com.dicoding.picodiploma.submissionAwalSelo.view.main.MainActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginEdt: EditText
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginEdt = binding.passwordEditText

        userPreference = UserPreference.getInstance((application as App).dataStore)

        setupView()
        setupAction()
    }



    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and Password must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            viewModel.saveSession(UserModel(email, "sample_token"))

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && !loginResponse.error) {
                        val user = UserModel(email, loginResponse.loginResult?.token ?: "", true)
                        lifecycleScope.launch {
                            userPreference.saveSession(user)
                            Toast.makeText(this@LoginActivity, "Login Successful! Token: ${loginResponse.loginResult?.token}", Toast.LENGTH_SHORT).show()
                            navigateToMain()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Failed: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login Failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}