package com.example.mobiusexample

import com.example.mobiusexample.LoginFailedError.*
import com.example.mobiusexample.Validate.Companion
import com.example.mobiusexample.ValidationError.InvalidPassword
import com.example.mobiusexample.ValidationError.InvalidUsername
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
                    hasEffects(Validate.from(username, password) as LoginEffect)
                )
            )
    }

    @Test
    fun `when validation succeeds, then do login`() {
        val username = "simple"
        val password = "simple123"
        val credentialsModel = LoginModel
            .BLANK
            .enteredCredentials(username, password)

        updateSpec
            .given(credentialsModel)
            .whenEvent(ValidationSuccess)
            .then(
                assertThatNext(
                    hasModel(credentialsModel.loggingIn()),
                    hasEffects(LoginApi(username, password) as LoginEffect)
                )
            )
    }

    @Test // FIXME Test asymmetry - Not the right kind of test.
    fun `when validation fails then show validation error`() {
        val username = "simple"
        val password = "simple123"
        val credentialsModel = LoginModel
            .BLANK
            .enteredCredentials(username, password)

        val expectedValidationError = credentialsModel
            .validationErrors(listOf(InvalidUsername, InvalidPassword))

        updateSpec
            .given(credentialsModel)
            .whenEvent(ValidationFailed.usernameAndPasswordError())
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
        val authToken = "real-auth-token"
        val loggingInModel = LoginModel
            .BLANK
            .enteredCredentials(username, password)
            .loggingIn()

        updateSpec
            .given(loggingInModel)
            .whenEvent(LoginSuccess(authToken))
            .then(
                assertThatNext(
                    hasNoModel(),
                    hasEffects(SaveToken(authToken), ShowHome)
                )
            )
    }

    @Test
    fun `when login fails with server error, then show error message`() {
        val username = "simple"
        val password = "simple123"
        val loggingInModel = LoginModel
            .BLANK
            .enteredCredentials(username, password)
            .loggingIn()

        updateSpec
            .given(loggingInModel)
            .whenEvent(LoginServerError)
            .then(
                assertThatNext(
                    hasModel(loggingInModel.loginFailed()),
                    hasEffects(ShowErrorMessage(SERVER_ERROR) as LoginEffect)
                )
            )
    }

    @Test
    fun `when login fails due to user being blocked, then show error message`() {
        val username = "simple"
        val password = "simple123"
        val loggingInModel = LoginModel
            .BLANK
            .enteredCredentials(username, password)
            .loggingIn()

        updateSpec
            .given(loggingInModel)
            .whenEvent(LoginBlockedUser)
            .then(
                assertThatNext(
                    hasModel(loggingInModel.loginFailed()),
                    hasEffects(ShowErrorMessage(BLOCKED_USER) as LoginEffect)
                )
            )
    }

    @Test
    fun `when login fails due to incorrect password then show error message`() {
        val username = "simple"
        val password = "simple123"
        val loggingInModel = LoginModel
            .BLANK
            .enteredCredentials(username, password)
            .loggingIn()

        updateSpec
            .given(loggingInModel)
            .whenEvent(LoginIncorrectPassword)
            .then(
                assertThatNext(
                    hasModel(loggingInModel.loginFailed()),
                    hasEffects(ShowErrorMessage(INCORRECT_PASSWORD) as LoginEffect)
                )
            )
    }

    @Test
    fun `when login fails because user does not exist then show a signup dialog`() {
        val username = "simple"
        val password = "simple123"
        val loggingInModel = LoginModel
            .BLANK
            .enteredCredentials(username, password)
            .loggingIn()

        updateSpec
            .given(loggingInModel)
            .whenEvent(LoginUserDoesNotExist)
            .then(
                assertThatNext(
                    hasModel(loggingInModel.loginFailed()),
                    hasEffects(ShowSignUpDialog as LoginEffect)
                )
            )
    }

    @Test
    fun `when user agrees to sign-up then navigate to sign up screen`() {
        val username = "simple"
        val password = "simple123"
        val loginFailedModel = LoginModel
            .BLANK
            .enteredCredentials(username, password)
            .loggingIn()
            .loginFailed()

        updateSpec
            .given(loginFailedModel)
            .whenEvent(SignUpUser)
            .then(
                assertThatNext(
                    hasNoModel(),
                    hasEffects(NavigateToSignUp as LoginEffect)
                )
            )
    }

    @Test
    fun `when user starts typing username, then clear username error`() {
        val username = "simple"
        val password = "simple123"
        val usernameValidationError = LoginModel
            .BLANK
            .enteredCredentials(username, password)
            .validationErrors(listOf(InvalidUsername))

        updateSpec
            .given(usernameValidationError)
            .whenEvent(UsernameEntered)
            .then(
                assertThatNext(
                    hasModel(usernameValidationError.clearUsernameError()),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun `when user starts typing password, then clean password error`() {
        val username = "simple"
        val password = "simple123"
        val passwordValidationError = LoginModel
            .BLANK
            .enteredCredentials(username, password)
            .validationErrors(listOf(InvalidPassword))

        updateSpec
            .given(passwordValidationError)
            .whenEvent(PasswordEntered)
            .then(
                assertThatNext(
                    hasModel(passwordValidationError.clearPasswordError()),
                    hasNoEffects()
                )
            )
    }
}
