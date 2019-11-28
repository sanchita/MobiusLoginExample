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
        return RxMobius
            .subtypeEffectHandler<LoginEffect, LoginEvent>()
            .addTransformer(Validate::class.java, validate())
            .addTransformer(LoginApi::class.java, login())
            .addConsumer(SaveToken::class.java) { userDatabase.saveAuthToken(it.authToken) }
            .addAction(ShowHome::class.java, loginViewActions::navigateToHome)
            .addAction(ShowSignUpDialog::class.java, loginViewActions::showSignUpDialog)
            .addAction(NavigateToSignUp::class.java, loginViewActions::navigateToSignUp)
            .addConsumer(ShowErrorMessage::class.java, ::showError)
            .addAction(ClearUsernameValidationError::class.java, loginViewActions::clearUsernameValidationError)
            .addAction(ClearPasswordValidationError::class.java, loginViewActions::clearPasswordValidationError)
            .build()
    }

    private fun login(): ObservableTransformer<LoginApi, LoginEvent> {
        return ObservableTransformer { loginApiStream ->
            loginApiStream
                .flatMapSingle { (username, password) -> loginApiCall.login(username, password) }
                .map { authToken -> LoginSuccess(authToken) }
        }
    }

    private fun showError(errorMessage: ShowErrorMessage) {
        when (errorMessage.error) {
            SERVER_ERROR -> loginViewActions.showServerError()
            BLOCKED_USER -> loginViewActions.showBlockedUserError()
            INCORRECT_PASSWORD -> loginViewActions.showIncorrectPasswordError()
        }
    }

    private fun validate(): ObservableTransformer<Validate, LoginEvent> {
        return ObservableTransformer { validateStream ->
            validateStream
                .map { (username, password) -> username.validate() + password.validate() }
                .map { if (it.isEmpty()) ValidationSuccess else ValidationFailed(it) }
        }
    }
}
