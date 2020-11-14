package com.anos.covid19.model
import com.google.gson.annotations.SerializedName

data class Global(
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
    var totalRecovered: Int? = 0
)