package com.dicoding.picodiploma.submissionAwalSelo.di

import android.content.Context
import com.dicoding.picodiploma.submissionAwalSelo.data.UserRepository
import com.dicoding.picodiploma.submissionAwalSelo.data.pref.UserPreference
import com.dicoding.picodiploma.submissionAwalSelo.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}