package com.anos.covid19.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Premium(
    @SerializedName("CountryStats")
    var countryStats: CountryStats? = null
) : Parcelable