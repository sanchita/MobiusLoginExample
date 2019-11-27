package com.example.mobiusexample

sealed class LoginEvent

data class LoginClicked(val username: String, val password: String) : LoginEvent()

object ValidationSuccess : LoginEvent()

data class ValidationFailed(
    val usernameError: ValidationError?,
    val passwordError: ValidationError?
) : LoginEvent()

object LoginSuccess : LoginEvent()

object LoginServerError : LoginEvent()

object LoginBlockedUser : LoginEvent()

object LoginIncorrectPassword : LoginEvent()
