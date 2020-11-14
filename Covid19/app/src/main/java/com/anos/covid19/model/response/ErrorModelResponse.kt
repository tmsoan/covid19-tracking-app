package com.anos.covid19.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ErrorModelResponse(
    @SerializedName("message")
    var message: String? = null
) : Parcelable {
}