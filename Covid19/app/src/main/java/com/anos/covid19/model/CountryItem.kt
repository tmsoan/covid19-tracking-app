package com.anos.covid19.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryItem (
    @SerializedName("Country")
    var country: String? = "",
    @SerializedName("ISO2")
    var iSO2: String? = "",
    @SerializedName("Slug")
    var slug: String? = ""
) : Parcelable