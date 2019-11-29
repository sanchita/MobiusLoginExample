package com.example.mobiusexample.domain

import android.os.Parcelable
import com.example.mobiusexample.domain.ValidationError.InvalidUsername
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Username(val value: String) : Parcelable {
    private val isValid: Boolean
        get() = value.matches(Regex("[a-zA-Z]([\\w]){0,7}"))

    fun validate(): List<ValidationError> =
        if (isValid) emptyList() else listOf(InvalidUsername)
}
