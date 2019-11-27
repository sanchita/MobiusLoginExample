package com.example.mobiusexample

import io.reactivex.Single

interface LoginApiCall {
    fun login(username: String, password: String): Single<String>
}
