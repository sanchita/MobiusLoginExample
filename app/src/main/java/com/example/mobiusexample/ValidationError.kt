package com.example.mobiusexample

sealed class ValidationError {
    object InvalidUsername : ValidationError()
    object InvalidPassword : ValidationError()
}
