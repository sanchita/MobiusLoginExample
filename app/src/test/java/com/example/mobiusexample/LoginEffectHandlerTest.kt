package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.INVALID
import org.junit.Test

class LoginEffectHandlerTest {

    @Test
    fun `when the username validation fails, then the validation failed event should be emitted`() {
        // given
        val effectHandlerTestCase = EffectHandlerTestCase(LoginEffectHandler.create())

        // when
        effectHandlerTestCase.dispatch(Validate("", "simple123"))

        // then
        effectHandlerTestCase.assertOutgoingEvents(ValidationFailed(INVALID, null))
    }
}
