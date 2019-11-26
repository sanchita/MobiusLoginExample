package com.example.mobiusexample

import com.spotify.mobius.test.NextMatchers.hasEffects
import com.spotify.mobius.test.NextMatchers.hasModel
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import org.junit.Test

class LoginUpdateTest {

    private val updateSpec = UpdateSpec<LoginModel, LoginEvent, LoginEffect>(LoginUpdate())

    @Test
    fun `when login is clicked then user input should be validated`() {
        val blankModel = LoginModel.BLANK
        val username = "simple"
        val password = "simple123"

        updateSpec
            .given(blankModel)
            .whenEvent(LoginClicked(username, password))
            .then(
                assertThatNext(
                    hasModel(blankModel.enteredCredentials(username, password)),
                    hasEffects(Validate as LoginEffect)
                )
            )
    }

    @Test
    fun `when validation succeeds, then do login`() {
        val username = "simple"
        val password = "simple123"
        val credentialsModel = LoginModel.BLANK.enteredCredentials(
            username = username,
            password = password
        )

        updateSpec
            .given(credentialsModel)
            .whenEvent(ValidationSuccess)
            .then(
                assertThatNext(
                    hasModel(credentialsModel.loggingIn()),
                    hasEffects(LoginApi as LoginEffect)
                )
            )
    }

}
