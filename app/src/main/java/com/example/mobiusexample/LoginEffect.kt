package com.example.mobiusexample

sealed class LoginEffect

object Validate : LoginEffect()

object LoginApi : LoginEffect()

object SaveToken : LoginEffect()

object ShowHome : LoginEffect()

data class ShowErrorMessage(val error: LoginFailedError) : LoginEffect()
