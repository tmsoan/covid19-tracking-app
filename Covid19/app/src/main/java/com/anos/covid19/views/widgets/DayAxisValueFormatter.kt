package com.anos.covid19.views.widgets

import android.annotation.SuppressLint
import com.anos.covid19.model.CountryStatus
import com.anos.covid19.model.DataInDay
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DayAxisValueFormatter(
    private val chart: BarLineChartBase<*>,
    private val lstData: List<CountryStatus>
) : IAxisValueFormatter {

    @SuppressLint("SimpleDateFormat")
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        Timber.e("==== dateaaaa: ${value}")
        val index = value.toInt()
        lstData[index].date?.let {
            val format = SimpleDateFormat("dd/MM")
            return format.format(it)
        } ?: kotlin.run {
            return ""
        }
    }

}