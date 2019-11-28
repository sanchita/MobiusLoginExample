package com.example.mobiusexample

sealed class LoginEffect

data class Validate(
    val username: Username,
    val password: String
) : LoginEffect() {
    companion object {
        fun from(username: String, password: String): Validate =
            Validate(Username(username), password)
    }
}

data class LoginApi(val username: String, val password: String) : LoginEffect()

data class SaveToken(val authToken: String) : LoginEffect()

object ShowHome : LoginEffect()

data class ShowErrorMessage(val error: LoginFailedError) : LoginEffect()

object ShowSignUpDialog : LoginEffect()

object NavigateToSignUp : LoginEffect()

object ClearUsernameValidationError : LoginEffect()

object ClearPasswordValidationError : LoginEffect()
