package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Test

class LoginViewRendererTest {

    private val loginView = mock<LoginView>()
    private val viewRenderer = LoginViewRenderer(loginView)

    @Test
    fun `it should render blank model`() {
        //given
         val blankModel = LoginModel.BLANK

        //when
        viewRenderer.render(blankModel)

        //then
        verifyZeroInteractions(loginView)
    }

    @Test
    fun `it should render validation model`() {
        //given
        val validationModel = LoginModel.BLANK.enteredCredentials("simple", "simple123")

        //when
        viewRenderer.render(validationModel)

        //then
        verifyZeroInteractions(loginView)
    }

    @Test
    fun `it should render username validation error`() {
        // given
        val usernameValidationError = LoginModel.BLANK.usernameValidationError(INVALID)

        // when
        viewRenderer.render(usernameValidationError)

        // then
        verify(loginView).showUsernameError()
        verifyNoMoreInteractions(loginView)
    }

    @Test
    fun `it should render password validation error`() {
        // given
        val passwordValidationError = LoginModel.BLANK.passwordValidationError(INVALID)

        // when
        viewRenderer.render(passwordValidationError)

        // then
        verify(loginView).showPasswordError()
        verifyNoMoreInteractions(loginView)
    }

    @Test
    fun `it should render logging in`() {
        // given
        val loggingIn = LoginModel.BLANK.loggingIn()

        // when
        viewRenderer.render(loggingIn)

        // then
        verify(loginView).showProgress()
        verifyNoMoreInteractions(loginView)
    }
}
