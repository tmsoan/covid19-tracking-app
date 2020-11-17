package com.anos.covid19.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

data class CountryTotalStatus(
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
    @SerializedName("Cases")
    var cases: Int? = 0,
    @SerializedName("Status")
    var status: String? = "",
    @SerializedName("Date")
    var date: Date? = null
)