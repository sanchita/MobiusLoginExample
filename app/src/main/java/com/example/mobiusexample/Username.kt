package com.example.mobiusexample

data class Username(val value: String) {
    private val usernameRegex = Regex("[a-zA-Z]([\\w]){0,7}")

    val isValid: Boolean
        get() = value.matches(usernameRegex)
}
