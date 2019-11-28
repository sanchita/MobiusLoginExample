package com.example.mobiusexample.domain

sealed class ValidationError {
    object InvalidUsername : ValidationError()
    object InvalidPassword : ValidationError()
}
