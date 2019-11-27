package com.example.mobiusexample

import com.nhaarman.mockitokotlin2.mock
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

}
