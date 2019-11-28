package com.example.mobiusexample

import com.example.mobiusexample.LoginStatus.LOGGING_IN
import com.example.mobiusexample.ValidationError.*

class LoginViewRenderer(val view: LoginView) {
    fun render(model: LoginModel) {
        if (model.usernameError == INVALID) {
            view.showUsernameError()
        }

        if (model.passwordError == INVALID) {
            view.showPasswordError()
        }

        if (model.loginStatus == LOGGING_IN) {
            view.showProgress()
        }
    }
}
