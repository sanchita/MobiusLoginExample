package com.example.mobiusexample

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultSchedulerProvider : SchedulerProvider {

    override val main: Scheduler
        get() = AndroidSchedulers.mainThread()

    override val io: Scheduler
        get() = Schedulers.io()
}