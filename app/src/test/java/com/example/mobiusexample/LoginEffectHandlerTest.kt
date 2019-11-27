package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.INVALID
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Test

class LoginEffectHandlerTest {

    private val loginApiCall = mock<LoginApiCall>()
    private val userDatabase = mock<UserDatabase>()
    private val loginViewActions = mock<LoginViewActions>()

    private val effectHandler =
        LoginEffectHandler.create(loginApiCall, userDatabase, loginViewActions)
    private val effectHandlerTestCase = EffectHandlerTestCase(effectHandler)

    @After
    fun tearDown() {
        effectHandlerTestCase.dispose()
    }

    @Test
    fun `when the username validation fails, then the validation failed event should be emitted`() {
        // when
        effectHandlerTestCase.dispatch(Validate("", "simple123"))

        // then
        effectHandlerTestCase.assertOutgoingEvents(ValidationFailed(INVALID, null))
    }

    @Test
    fun `when login api succeeds then success event should be emitted`() {
        //given
        val username = "username"
        val password = "password123"
        val authToken = "real-auth-token"

        whenever(loginApiCall.login(username, password)).thenReturn(Single.just(authToken))

        //when
        effectHandlerTestCase.dispatch(LoginApi(username, password))

        //then
        effectHandlerTestCase.assertOutgoingEvents(LoginSuccess(authToken))
    }

    @Test
    fun `when login is successful, then save token`() {
        // given
        val authToken = "real-auth-token"

        // when
        effectHandlerTestCase.dispatch(SaveToken(authToken))

        // then
        effectHandlerTestCase.assertNoOutgoingEvents()
        verify(userDatabase).saveAuthToken(authToken)
    }

    @Test
    fun `when login is successful then navigate to home`() {
        //when
        effectHandlerTestCase.dispatch(ShowHome)

        //then
        effectHandlerTestCase.assertNoOutgoingEvents()
        verify(loginViewActions).navigateToHome()
        verifyNoMoreInteractions(loginViewActions)
    }

    @Test
    fun `when login fails with user does not exist, then show sign up dialog`() {
        // when
        effectHandlerTestCase.dispatch(ShowSignUpDialog)

        // then
        effectHandlerTestCase.assertNoOutgoingEvents()
        verify(loginViewActions).showSignUpDialog()
        verifyNoMoreInteractions(loginViewActions)
    }
}
