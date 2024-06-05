package com.dicoding.picodiploma.submissionAwalSelo.view.signup

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.submissionAwalSelo.data.model.RegisterRequest
import com.dicoding.picodiploma.submissionAwalSelo.data.model.RegisterResponse
import com.dicoding.picodiploma.submissionAwalSelo.data.pref.UserModel
import com.dicoding.picodiploma.submissionAwalSelo.data.pref.UserPreference
import com.dicoding.picodiploma.submissionAwalSelo.data.pref.dataStore
import com.dicoding.picodiploma.submissionAwalSelo.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.submissionAwalSelo.service.RetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var registerJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        registerJob = Job()
    }

    private fun setupView() {
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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        if (registerJob.isActive) {
            registerJob.cancel()
        }

        val apiService = RetrofitClient.apiService
        registerJob = lifecycleScope.launch {
            try {
                val response: RegisterResponse = apiService.register(name, email, password)

                if (!response.error) {
                    // Registration successful, show success message
                    AlertDialog.Builder(this@SignupActivity).apply {
                        setTitle("Success")
                        setMessage(response.message)
                        setPositiveButton("OK") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    // Registration failed, show error message
                    Toast.makeText(this@SignupActivity, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Error occurred during registration, show error message
                AlertDialog.Builder(this@SignupActivity).apply {
                    setTitle("Error")
                    setMessage("Failed to register: ${e.message}")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::registerJob.isInitialized && registerJob.isActive) {
            registerJob.cancel()
        }
    }
}

