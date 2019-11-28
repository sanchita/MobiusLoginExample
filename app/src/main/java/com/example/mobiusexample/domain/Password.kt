package com.example.mobiusexample.domain

import com.example.mobiusexample.domain.ValidationError.InvalidPassword

data class Password(val value: String) {
    private val isValid: Boolean
        get() = value.isNotBlank() && value.length >= 8

    fun validate(): List<ValidationError> =
        if (isValid) emptyList() else listOf(InvalidPassword)
}