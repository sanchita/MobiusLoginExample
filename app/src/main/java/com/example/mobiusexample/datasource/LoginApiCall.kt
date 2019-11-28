package com.example.mobiusexample.datasource

import io.reactivex.Single

interface LoginApiCall {
    fun login(username: String, password: String): Single<String>
}
