package com.example.mobiusexample

import com.example.mobiusexample.LoginFailedError.*
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer

class LoginEffectHandler(
    private val loginApiCall: LoginApiCall,
    private val userDatabase: UserDatabase,
    private val loginViewActions: LoginViewActions
) {
    companion object {
        fun create(
            loginApiCall: LoginApiCall,
            userDatabase: UserDatabase,
            loginViewActions: LoginViewActions
        ): ObservableTransformer<LoginEffect, LoginEvent> {
            return LoginEffectHandler(loginApiCall, userDatabase, loginViewActions).create()
        }
    }

    private fun create(): ObservableTransformer<LoginEffect, LoginEvent> {
        return RxMobius.subtypeEffectHandler<LoginEffect, LoginEvent>()
            .addTransformer(Validate::class.java) { effectStream ->
                effectStream.map {
                    val validationErrors = it.username.validate() + it.password.validate()
                    if (validationErrors.isEmpty()) {
                        ValidationSuccess
                    } else {
                        ValidationFailed(validationErrors)
                    }
                }
            }
            .addTransformer(LoginApi::class.java) { effectStream ->
                effectStream
                    .flatMapSingle { loginApiCall.login(it.username, it.password) }
                    .map { authToken -> LoginSuccess(authToken) }
            }
            .addConsumer(SaveToken::class.java) {
                userDatabase.saveAuthToken(it.authToken)
            }
            .addAction(ShowHome::class.java, loginViewActions::navigateToHome)
            .addAction(ShowSignUpDialog::class.java, loginViewActions::showSignUpDialog)
            .addAction(NavigateToSignUp::class.java, loginViewActions::navigateToSignUp)
            .addConsumer(ShowErrorMessage::class.java) {
                when (it.error) {
                    SERVER_ERROR -> {
                        loginViewActions.showServerError()
                    }
                    BLOCKED_USER -> {
                        loginViewActions.showBlockedUserError()
                    }
                    INCORRECT_PASSWORD -> {
                        loginViewActions.showIncorrectPasswordError()
                    }
                }
            }
            .addAction(
                ClearUsernameValidationError::class.java,
                loginViewActions::clearUsernameValidationError
            )
            .addAction(
                ClearPasswordValidationError::class.java,
                loginViewActions::clearPasswordValidationError
            )
            .build()
    }
}
