package com.example.mobiusexample.test

import com.example.mobiusexample.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : SchedulerProvider {

    override val main: Scheduler
        get() = Schedulers.trampoline()

    override val io: Scheduler
        get() = Schedulers.trampoline()
}