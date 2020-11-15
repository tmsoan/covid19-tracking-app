package com.anos.covid19.model
import com.google.gson.annotations.SerializedName

data class GlobalChartItem(
    var title: String = "",
    var value: Int = 0,
    var colorRes: Int = 0
)