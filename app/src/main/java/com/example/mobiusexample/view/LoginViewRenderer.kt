package com.example.mobiusexample.view

import com.example.mobiusexample.domain.LoginModel
import com.example.mobiusexample.domain.LoginStatus.FAIL
import com.example.mobiusexample.domain.LoginStatus.LOGGING_IN
import com.example.mobiusexample.domain.ValidationError.InvalidPassword
import com.example.mobiusexample.domain.ValidationError.InvalidUsername

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
