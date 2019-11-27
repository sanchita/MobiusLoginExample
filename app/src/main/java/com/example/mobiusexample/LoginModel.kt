package com.example.mobiusexample

data class LoginModel(
    val username: String?,
    val password: String?,
    val loginStatus: LoginStatus?,
    val usernameError: ValidationError?,
    val passwordError: ValidationError?
) {
    companion object {
        val BLANK = LoginModel(null, null, null, null, null)
    }

    fun enteredCredentials(username: String, password: String): LoginModel {
        return copy(username = username, password = password)
    }

    fun loggingIn(): LoginModel =
        copy(loginStatus = LoginStatus.LOGGING_IN)

    fun usernameValidationError(error: ValidationError): LoginModel =
        copy(usernameError = error)

    fun passwordValidationError(error: ValidationError): LoginModel =
        copy(passwordError = error)

    fun loginFailed(): LoginModel =
        copy(loginStatus = LoginStatus.FAIL)

    fun clearUsernameError(): LoginModel =
        copy(usernameError = null)
}
