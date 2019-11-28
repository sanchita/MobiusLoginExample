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
                setOf(LoginApi(model.username!!.value, model.password!!))
            )
            is ValidationFailed -> next(model.validationErrors(event.errors))
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
}
