package com.dicoding.picodiploma.submissionAwalSelo.data.model

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore

class App : Application() {
    val dataStore by preferencesDataStore(name = "user_prefs")
}