package com.dicoding.picodiploma.submissionAwalSelo.service

import com.dicoding.picodiploma.submissionAwalSelo.data.model.LoginRequest
import com.dicoding.picodiploma.submissionAwalSelo.data.model.LoginResponse
import com.dicoding.picodiploma.submissionAwalSelo.data.model.RegisterRequest
import com.dicoding.picodiploma.submissionAwalSelo.data.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}

