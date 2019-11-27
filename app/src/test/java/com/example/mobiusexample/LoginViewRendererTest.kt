package com.example.mobiusexample

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Test

class LoginViewRendererTest {

    @Test
    fun `it should render blank model`() {
        //given
        val loginView = mock<LoginView>()
        val viewRenderer = LoginViewRenderer(loginView)
        val blankModel = LoginModel.BLANK

        //when
        viewRenderer.render(blankModel)

        //then
        verifyZeroInteractions(loginView)
    }
}
