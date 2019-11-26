package com.example.mobiusexample

data class LoginModel(val username: String?, val password: String?) {
    companion object {
        val BLANK = LoginModel(null, null)
    }

    fun enteredCredentials(username: String, password: String): LoginModel {
        return copy(username = username, password = password)
    }
}
