package com.example.mobiusexample

import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.mobiusexample.datasource.FakeLoginApiCall
import com.example.mobiusexample.datasource.FakeUserDatabase
import com.example.mobiusexample.domain.*
import com.example.mobiusexample.effecthandler.LoginEffectHandler
import com.example.mobiusexample.view.LoginView
import com.example.mobiusexample.view.LoginViewActions
import com.example.mobiusexample.view.LoginViewRenderer
import com.spotify.mobius.Update
import com.spotify.mobius.functions.Consumer
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : BaseActivity<LoginModel, LoginEvent, LoginEffect>(), LoginView,
    LoginViewActions {

    private val loginApiCall = FakeLoginApiCall()
    private val userDatabase = FakeUserDatabase()
    private val loginViewRenderer: LoginViewRenderer by lazy {
        LoginViewRenderer(this)
    }
    private val schedulerProvider = DefaultSchedulerProvider()

    override fun layoutResId(): Int = R.layout.activity_main

    override fun eventDispatcher(output: Consumer<LoginEvent>) {
        username.addTextChangedListener {
            output.accept(UsernameEntered)
        }

        password.addTextChangedListener {
            output.accept(PasswordEntered)
        }

        loginButton.setOnClickListener {
            output.accept(
                LoginClicked(
                    username.text?.toString().orEmpty(),
                    password.text?.toString().orEmpty()
                )
            )
        }
    }

    override fun render(model: LoginModel) {
        loginViewRenderer.render(model)
    }

    override fun initialModel(): LoginModel = LoginModel.BLANK

    override fun update(): Update<LoginModel, LoginEvent, LoginEffect> = LoginUpdate()

    override fun effectHandler(): ObservableTransformer<LoginEffect, LoginEvent> {
        return LoginEffectHandler.create(loginApiCall, userDatabase, this, schedulerProvider)
    }

    override fun cleanUp() {
        username.addTextChangedListener(null)
        password.addTextChangedListener(null)
        loginButton.setOnClickListener(null)
    }

    override fun showUsernameError() {
        usernameLayout.error = getString(R.string.invalid_username)
    }

    override fun showPasswordError() {
        passwordLayout.error = getString(R.string.invalid_password)
    }

    override fun showProgress() {
        progressBar.isVisible = true
        window.setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE)
        enableInteractiveViews(false)
    }

    override fun hideProgress() {
        progressBar.isVisible = false
        window.clearFlags(FLAG_NOT_TOUCHABLE)
        enableInteractiveViews(true)
    }

    override fun navigateToHome() {
        showToast(R.string.login_success)
    }

    override fun showSignUpDialog() {
        // Show sign up dialog
    }

    override fun navigateToSignUp() {
        // Navigate to sign up
    }

    override fun showServerError() {
        showToast(R.string.error_server)
    }

    override fun showBlockedUserError() {
        showToast(R.string.error_blocked_user)
    }

    override fun showIncorrectPasswordError() {
        showToast(R.string.error_incorrect_password)
    }

    override fun clearUsernameValidationError() {
        usernameLayout.error = null
        usernameLayout.isErrorEnabled = false
    }

    override fun clearPasswordValidationError() {
        passwordLayout.error = null
        passwordLayout.isErrorEnabled = false
    }

    private fun showToast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun enableInteractiveViews(isEnabled: Boolean) {
        usernameLayout.isEnabled = isEnabled
        passwordLayout.isEnabled = isEnabled
        loginButton.isEnabled = isEnabled
    }
}
