package com.anos.covid19.model

import com.google.gson.annotations.SerializedName

class Premium(
    @SerializedName("CountryStats")
    var countryStats: CountryStats? = null
)