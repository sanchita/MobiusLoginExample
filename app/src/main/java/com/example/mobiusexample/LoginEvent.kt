package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.InvalidPassword
import com.example.mobiusexample.ValidationError.InvalidUsername

sealed class LoginEvent

data class LoginClicked(
    val username: String,
    val password: String
) : LoginEvent()

object ValidationSuccess : LoginEvent()

data class ValidationFailed(val errors: List<ValidationError>) : LoginEvent() {
    companion object {
        fun usernameError(): ValidationFailed {
            return ValidationFailed(listOf(InvalidUsername))
        }

        fun passwordError(): ValidationFailed {
            return ValidationFailed(listOf(InvalidPassword))
        }

        fun usernameAndPasswordError(): ValidationFailed {
            return ValidationFailed(listOf(InvalidUsername, InvalidPassword))
        }
    }
}

data class LoginSuccess(val authToken: String) : LoginEvent()

object LoginServerError : LoginEvent()

object LoginBlockedUser : LoginEvent()

object LoginIncorrectPassword : LoginEvent()

object LoginUserDoesNotExist : LoginEvent()

object SignUpUser : LoginEvent()

object UsernameEntered : LoginEvent()

object PasswordEntered : LoginEvent()
