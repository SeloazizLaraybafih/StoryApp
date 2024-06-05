package com.dicoding.picodiploma.submissionAwalSelo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val error: Boolean,
    val message: String
)


