package com.example.mobiusexample.datasource

import io.reactivex.Single
import java.util.concurrent.TimeUnit

class FakeLoginApiCall : LoginApiCall {

    override fun login(username: String, password: String): Single<String> {
        return Single.just("auth-token").delay(3000, TimeUnit.MILLISECONDS)
    }
}