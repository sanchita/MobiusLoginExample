package com.example.mobiusexample

import com.spotify.mobius.Next
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update

class LoginUpdate :
    Update<LoginModel, LoginEvent, LoginEffect> {
    override fun update(model: LoginModel, event: LoginEvent): Next<LoginModel, LoginEffect> {
        val loginClicked = event as LoginClicked
        return next(
            model.enteredCredentials(
                username = loginClicked.username,
                password = loginClicked.password
            ),
            setOf(Validate)
        )
    }

}
