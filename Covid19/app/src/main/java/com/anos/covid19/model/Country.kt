package com.anos.covid19.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Country(
    @SerializedName("Country")
    var country: String? = "",
    @SerializedName("CountryCode")
    var countryCode: String? = "",
    @SerializedName("Slug")
    var slug: String? = "",
    @SerializedName("NewConfirmed")
    var newConfirmed: Int? = 0,
    @SerializedName("TotalConfirmed")
    var totalConfirmed: Int? = 0,
    @SerializedName("NewDeaths")
    var newDeaths: Int? = 0,
    @SerializedName("TotalDeaths")
    var totalDeaths: Int? = 0,
    @SerializedName("NewRecovered")
    var newRecovered: Int? = 0,
    @SerializedName("TotalRecovered")
    var totalRecovered: Int? = 0,
    @SerializedName("Date")
    var date: Date? = null,
    @SerializedName("Premium")
    var premium: Premium? = null,

    var order: Int = 0
) : Parcelable {
    fun getActiveCases(): Int {
        val total = totalConfirmed ?: 0
        val recovered = totalRecovered ?: 0
        val death = totalDeaths ?: 0
        return total - recovered - death
    }
}