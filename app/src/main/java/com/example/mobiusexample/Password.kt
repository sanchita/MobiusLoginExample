package com.example.mobiusexample

data class Password(val value: String) {

    val isValid: Boolean
        get() = value.isNotBlank() && value.length >= 8
}
