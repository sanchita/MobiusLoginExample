package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.INVALID
import com.spotify.mobius.test.NextMatchers.*
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

    @Test
    fun `when validation fails then show validation error`() {
        val username = "simple"
        val password = "simple123"
        val credentialsModel = LoginModel.BLANK.enteredCredentials(
            username = username,
            password = password
        )

        val expectedValidationError = credentialsModel
            .usernameValidationError(INVALID)
            .passwordValidationError(INVALID)

        updateSpec
            .given(credentialsModel)
            .whenEvent(ValidationFailed(INVALID, INVALID))
            .then(
                assertThatNext(
                    hasModel(
                        expectedValidationError
                    ),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun `when login succeeds, then save token and navigate to home`() {
        val username = "simple"
        val password = "simple123"
        val loggingInModel = LoginModel.BLANK.enteredCredentials(
            username = username,
            password = password
        ).loggingIn()

        updateSpec
            .given(loggingInModel)
            .whenEvent(LoginSuccess)
            .then(
                assertThatNext(
                    hasNoModel(),
                    hasEffects(SaveToken, ShowHome)
                )
            )
    }

    @Test
    fun `when login fails with server error, then show error message`() {
        val username = "simple"
        val password = "simple123"
        val loggingInModel = LoginModel.BLANK
            .enteredCredentials(
                username = username,
                password = password
            )
            .loggingIn()

        updateSpec
            .given(loggingInModel)
            .whenEvent(LoginServerError)
            .then(
                assertThatNext(
                    hasModel(loggingInModel.loginFailed()),
                    hasEffects(ShowErrorMessage as LoginEffect)
                )
            )
    }

}
