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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.android.synthetic.main.layout_country_chart_view.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class CountryChartView : RelativeLayout {

    enum class Period(val days: Int) {
        SEVEN(7),
        THIRTY(30),
        ALL(0),
        CUSTOM(-1)
    }

    private var statusLst: List<CountryStatus>? = null

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

        initChartLayout()
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

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY)

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

    private fun createDataSet(dataSetIndex: Int, status: String, lstData: List<DataInDay>): LineDataSet {
        val entries = ArrayList<Entry>()
        val cal = Calendar.getInstance()
        for (i in lstData.indices) {
            val data = lstData[i]
            cal.time = data.date ?: Date()
            val y = data.value?.toFloat() ?: 0f
            entries.add(Entry(i.toFloat(), y))
            Timber.e("======= ${i?.toFloat()}")
        }
        // create a dataset and give it a type
        val dataSet = LineDataSet(entries, "DataSet $dataSetIndex")
        dataSet.setDrawIcons(false)
        //dataSet.axisDependency = YAxis.AxisDependency.LEFT
        dataSet.color = getStatusColor(status)
        // circle
        /*dataSet.setCircleColor(Color.WHITE)
        dataSet.setDrawCircleHole(false)
        dataSet.circleRadius = 2f*/
        dataSet.setDrawCircles(false)

        dataSet.setDrawValues(false)
        dataSet.lineWidth = 2f

        return dataSet
    }

    private fun getStatusColor(status: String): Int {
        return when (status) {
            AppConst.Status.CONFIRMED -> resources.getColor(R.color.confirmed_color)
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
        statusLst = lstStatus.sortedWith(compareBy { it.date })/*.filter {
            it.date?.after(fromDate) ?: kotlin.run {
                false
            } && it.date?.before(toDate) ?: kotlin.run {
                false
            }
        }*/
        val confirmedLst = ArrayList<DataInDay>()
        val recoveredLst = ArrayList<DataInDay>()
        val deathLst = ArrayList<DataInDay>()
        statusLst?.forEach {
            confirmedLst.add(DataInDay(it.date, it.confirmed))
            recoveredLst.add(DataInDay(it.date, it.recovered))
            deathLst.add(DataInDay(it.date, it.deaths))
        }
        val dataSet0 = createDataSet(0, AppConst.Status.CONFIRMED, confirmedLst)
        val dataSet1 = createDataSet(1, AppConst.Status.RECOVERED, recoveredLst)
        val dataSet2 = createDataSet(2, AppConst.Status.DEATH, deathLst)

        // create a data object with dataset
        chart.data = LineData(dataSet0, dataSet1, dataSet2)

        val xAxisFormatter: IAxisValueFormatter = DayAxisValueFormatter(chart, statusLst!!)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = xAxisFormatter

        // redraw
        chart.invalidate()
    }
}