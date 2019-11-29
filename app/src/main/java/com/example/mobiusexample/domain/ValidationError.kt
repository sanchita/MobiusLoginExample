package com.example.mobiusexample.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ValidationError : Parcelable {

    @Parcelize
    object InvalidUsername : ValidationError()

    @Parcelize
    object InvalidPassword : ValidationError()
}
