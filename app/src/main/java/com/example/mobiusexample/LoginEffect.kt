package com.example.mobiusexample

sealed class LoginEffect

object Validate : LoginEffect()

object LoginApi : LoginEffect()
