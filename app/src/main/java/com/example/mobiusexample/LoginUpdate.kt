package com.example.mobiusexample

import com.example.mobiusexample.LoginFailedError.BLOCKED_USER
import com.example.mobiusexample.LoginFailedError.SERVER_ERROR
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update

class LoginUpdate :
    Update<LoginModel, LoginEvent, LoginEffect> {
    override fun update(model: LoginModel, event: LoginEvent): Next<LoginModel, LoginEffect> {
        return when (event) {
            is LoginClicked -> {
                next(
                    model.enteredCredentials(
                        username = event.username,
                        password = event.password
                    ),
                    setOf(Validate)
                )
            }
            ValidationSuccess -> {
                next(
                    model.loggingIn(),
                    setOf(LoginApi)
                )
            }
            is ValidationFailed -> {
                next(modelForValidationError(event, model))
            }
            LoginSuccess -> {
                dispatch(setOf(SaveToken, ShowHome))
            }
            LoginServerError -> {
                next(model.loginFailed(), setOf(ShowErrorMessage(SERVER_ERROR)))
            }
            LoginBlockedUser -> {
                next(model.loginFailed(), setOf(ShowErrorMessage(BLOCKED_USER)))
            }
        }
    }

    private fun modelForValidationError(
        event: ValidationFailed,
        model: LoginModel
    ): LoginModel {
        val isUsernameError = event.usernameError != null
        val isPasswordError = event.passwordError != null

        return when {
            isUsernameError && isPasswordError -> model
                .usernameValidationError(event.usernameError!!)
                .passwordValidationError(event.passwordError!!)
            isUsernameError -> model.usernameValidationError(event.usernameError!!)
            isPasswordError -> model.passwordValidationError(event.passwordError!!)
            else -> throw IllegalStateException("Run! Invalid usernameError and passwordError case!")
        }
    }

}
