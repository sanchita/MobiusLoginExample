package com.example.mobiusexample.domain

import android.os.Parcelable
import com.example.mobiusexample.domain.LoginStatus.FAIL
import com.example.mobiusexample.domain.LoginStatus.LOGGING_IN
import com.example.mobiusexample.domain.ValidationError.InvalidPassword
import com.example.mobiusexample.domain.ValidationError.InvalidUsername
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginModel(
    val username: Username? = null,
    val password: Password? = null,
    val loginStatus: LoginStatus?,
    val validationErrors: List<ValidationError> = emptyList()
) : Parcelable {
    companion object {
        val BLANK =
            LoginModel(loginStatus = null)
    }

    fun enteredCredentials(username: String, password: String): LoginModel {
        return copy(
            username = Username(username), password = Password(password)
        )
    }

    fun loggingIn(): LoginModel =
        copy(loginStatus = LOGGING_IN)

    fun loginFailed(): LoginModel =
        copy(loginStatus = FAIL)

    fun clearUsernameError(): LoginModel =
        copy(validationErrors = validationErrors - InvalidUsername)

    fun clearPasswordError(): LoginModel =
        copy(validationErrors = validationErrors - InvalidPassword)

    fun validationErrors(validationErrors: List<ValidationError>): LoginModel {
        return copy(validationErrors = validationErrors)
    }
}
