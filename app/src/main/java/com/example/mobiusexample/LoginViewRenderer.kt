package com.example.mobiusexample

class LoginViewRenderer(val view: LoginView) {
    fun render(model: LoginModel) {
        if (model.usernameError == ValidationError.INVALID) {
            view.showUsernameError()
        }
    }
}
