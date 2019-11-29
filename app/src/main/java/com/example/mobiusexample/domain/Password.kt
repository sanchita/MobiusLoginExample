package com.example.mobiusexample.domain

import android.os.Parcelable
import com.example.mobiusexample.domain.ValidationError.InvalidPassword
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Password(val value: String) : Parcelable {
    private val isValid: Boolean
        get() = value.isNotBlank() && value.length >= 8

    fun validate(): List<ValidationError> =
        if (isValid) emptyList() else listOf(InvalidPassword)
}
