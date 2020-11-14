package com.anos.covid19.model.response
import com.anos.covid19.model.Country
import com.anos.covid19.model.Global
import com.google.gson.annotations.SerializedName
import java.util.*


data class SummaryResponse(
    @SerializedName("Message")
    var message: String? = "",

    @SerializedName("Global")
    var global: Global? = null,

    @SerializedName("Countries")
    var countries: List<Country>? = null,

    @SerializedName("Date")
    var date: Date? = null
)