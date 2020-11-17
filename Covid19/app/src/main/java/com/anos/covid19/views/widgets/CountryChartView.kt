package com.anos.covid19.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.anos.covid19.R
import com.anos.covid19.model.CountryStatus
import com.anos.covid19.model.DataInDay
import com.anos.covid19.utils.AppConst
import com.anos.covid19.utils.getDaysAgo
import com.anos.covid19.utils.getUpdatedDateString
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.layout_country_chart_view.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class CountryChartView : RelativeLayout {

    interface ICountryViewListener {
        fun onChartDateChange(fromDate: Date, toDate: Date)
    }

    enum class Period(val days: Int) {
        SEVEN(7),
        A_MONTH(30),
        SIX_MONTH(180),
        ALL(365),
        CUSTOM(-1)
    }

    private var statusLst: List<CountryStatus>? = null
    var listener: ICountryViewListener? = null

    private lateinit var period: Period
    lateinit var fromDate: Date
    lateinit var toDate: Date

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)

    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    @SuppressLint("InflateParams")
    private fun init(attrs: AttributeSet?) {
        addView(LayoutInflater.from(context).inflate(R.layout.layout_country_chart_view, null))

        // default value
        period = Period.SEVEN
        fromDate = getDaysAgo(period.days)
        toDate = getDaysAgo(1)

        updatePeriodTime()

        initChartLayout()

        tv_7_days.setOnClickListener {
            tv_7_days.setBackgroundResource(R.drawable.shape_chart_period_btn_selected)
            tv_30_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_180_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_all.setBackgroundResource(R.drawable.shape_chart_period_btn)
            reloadData(Period.SEVEN)
        }
        tv_30_days.setOnClickListener {
            tv_7_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_30_days.setBackgroundResource(R.drawable.shape_chart_period_btn_selected)
            tv_180_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_all.setBackgroundResource(R.drawable.shape_chart_period_btn)
            reloadData(Period.A_MONTH)
        }
        tv_180_days.setOnClickListener {
            tv_7_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_30_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_180_days.setBackgroundResource(R.drawable.shape_chart_period_btn_selected)
            tv_all.setBackgroundResource(R.drawable.shape_chart_period_btn)
            reloadData(Period.SIX_MONTH)
        }
        tv_all.setOnClickListener {
            tv_7_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_30_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_180_days.setBackgroundResource(R.drawable.shape_chart_period_btn)
            tv_all.setBackgroundResource(R.drawable.shape_chart_period_btn_selected)
            reloadData(Period.ALL)
        }
    }

    private fun reloadData(period: Period) {
        if (this.period == period)
            return
        fromDate = getDaysAgo(period.days)
        this.period = period
        listener?.onChartDateChange(fromDate, toDate)
        updatePeriodTime()
    }

    private fun updatePeriodTime() {
        tv_time?.text = "${getUpdatedDateString(fromDate)} - ${getUpdatedDateString(toDate)}"
    }

    /**
     * setup layout for Chart
     */
    private fun initChartLayout() {
        // no description text
        chart.description.isEnabled = false

        chart.setPinchZoom(false)
        chart.isDoubleTapToZoomEnabled = false

        chart.dragDecelerationFrictionCoef = 0.7f

        // enable scaling and dragging
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = true

        chart.animateX(1500)

        // get the legend (only possible after setting data)
        val l = chart.legend
        // modify the legend ...
        l.form = Legend.LegendForm.LINE
        l.textSize = 10f
        l.textColor = Color.TRANSPARENT//hide dataset label
        l.form = Legend.LegendForm.NONE//hide dataset label
        l.setDrawInside(false)
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.HORIZONTAL

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawGridLines(false)

        val leftAxis = chart.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.textSize = 10f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis = chart.axisRight
        rightAxis.setDrawLabels(false)
        rightAxis.setDrawGridLines(false)
    }

    private fun createDataSet(dataSetIndex: Int, status: String, lstData: List<DataInDay>): BarDataSet {
        val entries = ArrayList<BarEntry>()
        for (i in lstData.indices) {
            val data = lstData[i]
            val y = data.value?.toFloat() ?: 0f
            entries.add(BarEntry(i.toFloat(), y))
        }
        // create a dataset and give it a type
        val dataSet = BarDataSet(entries, "DataSet $dataSetIndex")
        dataSet.setDrawIcons(false)
        dataSet.color = getStatusColor(status)
        dataSet.setDrawValues(false)

        return dataSet
    }

    private fun getStatusColor(status: String): Int {
        return when (status) {
            AppConst.Status.CONFIRMED -> resources.getColor(R.color.confirmed_color)
            AppConst.Status.ACTIVE -> resources.getColor(R.color.active_color)
            AppConst.Status.RECOVERED -> resources.getColor(R.color.recovered_color)
            AppConst.Status.DEATH -> resources.getColor(R.color.death_color)
            else -> resources.getColor(R.color.colorPrimary)
        }
    }

    /**
     * update data chart
     */
    fun update(lstStatus: List<CountryStatus>) {
        Timber.e("update >> ${lstStatus.size}")
        // filter data again
        statusLst = lstStatus.sortedWith(compareBy { it.date })
        val activeLst = ArrayList<DataInDay>()
        val recoveredLst = ArrayList<DataInDay>()
        val deathLst = ArrayList<DataInDay>()
        statusLst?.forEach {
            activeLst.add(DataInDay(it.date, it.active))
            recoveredLst.add(DataInDay(it.date, it.recovered))
            deathLst.add(DataInDay(it.date, it.deaths))
        }
        val dataSet0 = createDataSet(0, AppConst.Status.ACTIVE, activeLst)
        val dataSet1 = createDataSet(1, AppConst.Status.RECOVERED, recoveredLst)
        val dataSet2 = createDataSet(2, AppConst.Status.DEATH, deathLst)

        // create a data object with dataset
        chart.data = BarData(dataSet0, dataSet1, dataSet2)

        /*val xAxisFormatter: IAxisValueFormatter = DayAxisValueFormatter(chart, statusLst!!)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = xAxisFormatter*/

        chart.setFitBars(true)
        chart.invalidate()
    }
}