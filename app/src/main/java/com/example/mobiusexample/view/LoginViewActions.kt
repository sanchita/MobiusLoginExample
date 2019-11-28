package com.example.mobiusexample.view

interface LoginViewActions {
    fun navigateToHome()
    fun showSignUpDialog()
    fun navigateToSignUp()
    fun showServerError()
    fun showBlockedUserError()
    fun showIncorrectPasswordError()
    fun clearUsernameValidationError()
    fun clearPasswordValidationError()
}
