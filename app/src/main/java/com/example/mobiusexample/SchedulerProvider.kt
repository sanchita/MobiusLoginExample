package com.example.mobiusexample

import io.reactivex.Scheduler

interface SchedulerProvider {

    val main: Scheduler

    val io: Scheduler
}
