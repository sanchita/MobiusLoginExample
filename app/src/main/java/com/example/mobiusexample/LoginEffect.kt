package com.example.mobiusexample

sealed class LoginEffect

data class Validate(val username: String, val password: String) : LoginEffect()

object LoginApi : LoginEffect()

object SaveToken : LoginEffect()

object ShowHome : LoginEffect()

data class ShowErrorMessage(val error: LoginFailedError) : LoginEffect()

object ShowSignUpDialog : LoginEffect()

object NavigateToSignUp : LoginEffect()
