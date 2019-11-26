package com.example.mobiusexample

sealed class LoginEvent

data class LoginClicked(val username: String, val password: String) : LoginEvent()
