package com.example.mobiusexample

import com.spotify.mobius.Next
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
        }
    }

}
