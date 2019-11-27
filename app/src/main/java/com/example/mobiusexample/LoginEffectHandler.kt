package com.example.mobiusexample

import com.example.mobiusexample.ValidationError.INVALID
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer

object LoginEffectHandler {

    fun create(): ObservableTransformer<LoginEffect, LoginEvent> {
        return RxMobius.subtypeEffectHandler<LoginEffect, LoginEvent>()
            .addTransformer(LoginEffect::class.java) { effectStream ->
                effectStream.map { ValidationFailed(INVALID, null) }
            }
            .build()
    }
}
