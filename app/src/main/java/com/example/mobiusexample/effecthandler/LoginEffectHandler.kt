package com.example.mobiusexample.effecthandler

import com.example.mobiusexample.SchedulerProvider
import com.example.mobiusexample.datasource.LoginApiCall
import com.example.mobiusexample.datasource.UserDatabase
import com.example.mobiusexample.domain.*
import com.example.mobiusexample.domain.LoginFailedError.*
import com.example.mobiusexample.view.LoginViewActions
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer

class LoginEffectHandler(
    private val loginApiCall: LoginApiCall,
    private val userDatabase: UserDatabase,
    private val loginViewActions: LoginViewActions,
    private val schedulerProvider: SchedulerProvider
) {
    companion object {
        fun create(
            loginApiCall: LoginApiCall,
            userDatabase: UserDatabase,
            loginViewActions: LoginViewActions,
            schedulerProvider: SchedulerProvider
        ): ObservableTransformer<LoginEffect, LoginEvent> {
            return LoginEffectHandler(
                loginApiCall,
                userDatabase,
                loginViewActions,
                schedulerProvider
            ).create()
        }
    }

    private fun create(): ObservableTransformer<LoginEffect, LoginEvent> {
        return RxMobius
            .subtypeEffectHandler<LoginEffect, LoginEvent>()
            .addTransformer(Validate::class.java, validate())
            .addTransformer(LoginApi::class.java, login())
            .addConsumer(
                SaveToken::class.java,
                { userDatabase.saveAuthToken(it.authToken) },
                schedulerProvider.io
            )
            .addAction(
                ShowHome::class.java,
                loginViewActions::navigateToHome,
                schedulerProvider.main
            )
            .addAction(
                ShowSignUpDialog::class.java,
                loginViewActions::showSignUpDialog,
                schedulerProvider.main
            )
            .addAction(
                NavigateToSignUp::class.java,
                loginViewActions::navigateToSignUp,
                schedulerProvider.main
            )
            .addConsumer(ShowErrorMessage::class.java, ::showError, schedulerProvider.main)
            .addAction(
                ClearUsernameValidationError::class.java,
                loginViewActions::clearUsernameValidationError,
                schedulerProvider.main
            )
            .addAction(
                ClearPasswordValidationError::class.java,
                loginViewActions::clearPasswordValidationError,
                schedulerProvider.main
            )
            .build()
    }

    private fun login(): ObservableTransformer<LoginApi, LoginEvent> {
        return ObservableTransformer { loginApiStream ->
            loginApiStream
                .flatMapSingle { (username, password) -> loginApiCall.login(username, password) }
                .map { authToken ->
                    LoginSuccess(
                        authToken
                    )
                }
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
                .map {
                    if (it.isEmpty()) ValidationSuccess else ValidationFailed(
                        it
                    )
                }
        }
    }
}
