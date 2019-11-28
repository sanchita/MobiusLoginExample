package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.InvalidPassword
import com.example.mobiusexample.ValidationError.InvalidUsername

data class LoginModel(
    val username: String?,
    val password: String?,
    val loginStatus: LoginStatus?,
    val validationErrors: List<ValidationError> = emptyList()
) {
    companion object {
        val BLANK = LoginModel(null, null, null)
    }

    fun enteredCredentials(username: String, password: String): LoginModel {
        return copy(username = username, password = password)
    }

    fun loggingIn(): LoginModel =
        copy(loginStatus = LoginStatus.LOGGING_IN)

    @Deprecated("")
    fun usernameValidationError(): LoginModel =
        validationErrors(listOf(InvalidUsername))

    @Deprecated("")
    fun passwordValidationError(): LoginModel =
        validationErrors(listOf(InvalidPassword))

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
