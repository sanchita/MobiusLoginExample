package com.example.mobiusexample.datasource

import android.util.Log

class FakeUserDatabase : UserDatabase {

    override fun saveAuthToken(authToken: String) {
        Log.d(javaClass.simpleName, "Saving auth token: $authToken")
    }
}