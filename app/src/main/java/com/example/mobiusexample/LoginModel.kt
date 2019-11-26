package com.example.mobiusexample

data class LoginModel(
    val username: String?,
    val password: String?,
    val loginStatus: LoginStatus?
) {
    companion object {
        val BLANK = LoginModel(null, null, null)
    }

    fun enteredCredentials(username: String, password: String): LoginModel {
        return copy(username = username, password = password)
    }

    fun loggingIn(): LoginModel =
        copy(loginStatus = LoginStatus.LOGGING_IN)
}
