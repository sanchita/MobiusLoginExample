package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.InvalidPassword
import com.example.mobiusexample.ValidationError.InvalidUsername

data class LoginModel(
    val username: Username? = null,
    val password: Password? = null,
    val loginStatus: LoginStatus?,
    val validationErrors: List<ValidationError> = emptyList()
) {
    companion object {
        val BLANK = LoginModel(loginStatus = null)
    }

    fun enteredCredentials(username: String, password: String): LoginModel {
        return copy(
            username = Username(username),
            password = Password(password)
        )
    }

    fun loggingIn(): LoginModel =
        copy(loginStatus = LoginStatus.LOGGING_IN)

    fun loginFailed(): LoginModel =
        copy(loginStatus = LoginStatus.FAIL)

    fun clearUsernameError(): LoginModel =
        copy(validationErrors = validationErrors - InvalidUsername)

    fun clearPasswordError(): LoginModel =
        copy(validationErrors = validationErrors - InvalidPassword)

    fun validationErrors(validationErrors: List<ValidationError>): LoginModel {
        return copy(validationErrors = validationErrors)
    }
}
