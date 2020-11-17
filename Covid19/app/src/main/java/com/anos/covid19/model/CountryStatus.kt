package com.anos.covid19.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CountryStatus(
    @SerializedName("Country")
    var country: String? = "",
    @SerializedName("CountryCode")
    var countryCode: String? = "",
    @SerializedName("Province")
    var province: String? = "",
    @SerializedName("City")
    var city: String? = "",
    @SerializedName("CityCode")
    var cityCode: String? = "",
    @SerializedName("Lat")
    var lat: String? = "",
    @SerializedName("Lon")
    var lon: String? = "",
    @SerializedName("Confirmed")
    var confirmed: Int? = 0,
    @SerializedName("Deaths")
    var deaths: Int? = 0,
    @SerializedName("Recovered")
    var recovered: Int? = 0,
    @SerializedName("Active")
    var active: Int? = 0,
    @SerializedName("Date")
    var date: Date? = null
) : Parcelable