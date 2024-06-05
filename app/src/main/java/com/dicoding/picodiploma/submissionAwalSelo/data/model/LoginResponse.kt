package com.dicoding.picodiploma.submissionAwalSelo.data.model

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult?
)