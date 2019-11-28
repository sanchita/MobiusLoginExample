package com.example.mobiusexample.domain

sealed class LoginEvent

data class LoginClicked(
    val username: String,
    val password: String
) : LoginEvent()

object ValidationSuccess : LoginEvent()

data class ValidationFailed(val errors: List<ValidationError>) : LoginEvent()

data class LoginSuccess(val authToken: String) : LoginEvent()

object LoginServerError : LoginEvent()

object LoginBlockedUser : LoginEvent()

object LoginIncorrectPassword : LoginEvent()

object LoginUserDoesNotExist : LoginEvent()

object SignUpUser : LoginEvent()

object UsernameEntered : LoginEvent()

object PasswordEntered : LoginEvent()
