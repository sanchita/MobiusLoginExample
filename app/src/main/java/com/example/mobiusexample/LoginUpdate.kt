package com.example.mobiusexample

import com.example.mobiusexample.LoginFailedError.*
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update

class LoginUpdate :
    Update<LoginModel, LoginEvent, LoginEffect> {
    override fun update(model: LoginModel, event: LoginEvent): Next<LoginModel, LoginEffect> {
        return when (event) {
            is LoginClicked -> next(
                model.enteredCredentials(event.username, event.password),
                setOf(Validate(event.username, event.password))
            )
            ValidationSuccess -> next(
                model.loggingIn(),
                setOf(LoginApi(model.username!!, model.password!!))
            )
            is ValidationFailed -> next(modelForValidationError(event, model))
            is LoginSuccess -> dispatch(setOf(SaveToken(event.authToken), ShowHome))
            LoginServerError -> next(
                model.loginFailed(),
                setOf(ShowErrorMessage(SERVER_ERROR))
            )
            LoginBlockedUser -> next(
                model.loginFailed(),
                setOf(ShowErrorMessage(BLOCKED_USER))
            )
            LoginIncorrectPassword -> next(
                model.loginFailed(),
                setOf(ShowErrorMessage(INCORRECT_PASSWORD))
            )
            LoginUserDoesNotExist -> next(
                model.loginFailed(),
                setOf(ShowSignUpDialog)
            )
            SignUpUser -> dispatch(setOf(NavigateToSignUp))
            UsernameEntered -> next(model.clearUsernameError())
            PasswordEntered -> next(model.clearPasswordError())
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
