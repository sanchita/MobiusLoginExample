package com.example.mobiusexample

import com.example.mobiusexample.LoginStatus.FAIL
import com.example.mobiusexample.LoginStatus.LOGGING_IN
import com.example.mobiusexample.ValidationError.InvalidPassword
import com.example.mobiusexample.ValidationError.InvalidUsername

class LoginViewRenderer(private val view: LoginView) {
    fun render(model: LoginModel) {
        if (model.validationErrors.contains(InvalidUsername)) {
            view.showUsernameError()
        }

        if (model.validationErrors.contains(InvalidPassword)) {
            view.showPasswordError()
        }

        if (model.loginStatus == LOGGING_IN) {
            view.showProgress()
        } else if (model.loginStatus == FAIL) {
            view.hideProgress()
        }
    }
}
