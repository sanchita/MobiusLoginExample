package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.InvalidUsername

data class Username(val value: String) {
    private val usernameRegex = Regex("[a-zA-Z]([\\w]){0,7}")

    private val isValid: Boolean
        get() = value.matches(usernameRegex)

    fun validate(): List<ValidationError> =
        if (isValid) emptyList() else listOf(InvalidUsername)
}
