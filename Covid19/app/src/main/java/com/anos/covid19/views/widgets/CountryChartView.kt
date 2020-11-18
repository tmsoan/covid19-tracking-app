package com.anos.covid19.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
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
import kotlin.collections.HashMap


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

    enum class DataSetIndex(val value: Int) {
        RECOVERED(0),
        ACTIVE(1),
        DEATH(2),
        CONFIRMED(3)
    }

    private var statusLst: List<CountryStatus>? = null
    var listener: ICountryViewListener? = null

    private lateinit var period: Period
    lateinit var fromDate: Date
    lateinit var toDate: Date
    private var isShowingStatus = false
    private var filterStatusMap = HashMap<String, Boolean>()

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

        // filter status
        filterStatusMap[AppConst.Status.CONFIRMED] = true
        filterStatusMap[AppConst.Status.ACTIVE] = true
        filterStatusMap[AppConst.Status.RECOVERED] = true
        filterStatusMap[AppConst.Status.DEATH] = true

        // default value
        period = Period.SEVEN
        fromDate = getDaysAgo(period.days)
        toDate = getDaysAgo(1)

        updatePeriodTime()

        initChartLayout()

        //
        // date options click
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

        //
        // filter status options click
        tv_confirmed.setOnClickListener {
            val stt = !filterStatusMap.getOrDefault(AppConst.Status.CONFIRMED, true)
            filterStatusMap[AppConst.Status.CONFIRMED] = stt
            updateStatusSelectionState(it as TextView, DataSetIndex.CONFIRMED, stt)
        }
        tv_recovered.setOnClickListener {
            val stt = !filterStatusMap.getOrDefault(AppConst.Status.RECOVERED, true)
            filterStatusMap[AppConst.Status.RECOVERED] = stt
            updateStatusSelectionState(it as TextView, DataSetIndex.RECOVERED, stt)
        }
        tv_active.setOnClickListener {
            val stt = !filterStatusMap.getOrDefault(AppConst.Status.ACTIVE, true)
            filterStatusMap[AppConst.Status.ACTIVE] = stt
            updateStatusSelectionState(it as TextView, DataSetIndex.ACTIVE, stt)
        }
        tv_death.setOnClickListener {
            val stt = !filterStatusMap.getOrDefault(AppConst.Status.DEATH, true)
            filterStatusMap[AppConst.Status.DEATH] = stt
            updateStatusSelectionState(it as TextView, DataSetIndex.DEATH, stt)
        }
    }

    /**
     * update chart after selecting status options
     */
    private fun updateStatusSelectionState(tv: TextView?, dataSetIndex: DataSetIndex, status: Boolean) {
        if (isShowingStatus) {
            if (status) {
                tv?.setBackgroundResource(R.drawable.shape_chart_period_btn_selected)
            } else {
                tv?.setBackgroundResource(R.drawable.shape_chart_period_btn)
            }
            chart?.barData?.getDataSetByLabel("DataSet-${dataSetIndex.value}", true)?.isVisible = status
            chart?.invalidate()
        }
    }

    private fun resetStatusFilter() {
        filterStatusMap[AppConst.Status.CONFIRMED] = true
        filterStatusMap[AppConst.Status.ACTIVE] = true
        filterStatusMap[AppConst.Status.RECOVERED] = true
        filterStatusMap[AppConst.Status.DEATH] = true
        updateStatusSelectionState(tv_confirmed, DataSetIndex.CONFIRMED, true)
        updateStatusSelectionState(tv_recovered, DataSetIndex.RECOVERED, true)
        updateStatusSelectionState(tv_active, DataSetIndex.ACTIVE, true)
        updateStatusSelectionState(tv_death, DataSetIndex.DEATH, true)
    }

    /**
     * hide / show options: date-range, status
     */
    fun updateFilterLayout(showDateOptions: Boolean, showStatusOptions: Boolean) {
        if (!showDateOptions) {
            ln_date_options.visibility = View.GONE
        } else {
            ln_date_options.visibility = View.VISIBLE
        }
        if (!showStatusOptions) {
            ln_status_options.visibility = View.GONE
        } else {
            ln_status_options.visibility = View.VISIBLE
        }
        isShowingStatus = showStatusOptions
    }

    /**
     * reload chart after selecting new date range
     */
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

        // enable scaling and dragging
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = false

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
        xAxis.isEnabled = false

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
        val dataSet = BarDataSet(entries, "DataSet-$dataSetIndex")
        dataSet.setDrawIcons(false)
        dataSet.color = getStatusColor(status)
        dataSet.setDrawValues(false)
        dataSet.isHighlightEnabled = false

        return dataSet
    }

    private fun getStatusColor(status: String): Int {
        return when (status) {
            AppConst.Status.CONFIRMED -> resources.getColor(R.color.confirmed_color_chart)
            AppConst.Status.ACTIVE -> resources.getColor(R.color.active_color_chart)
            AppConst.Status.RECOVERED -> resources.getColor(R.color.recovered_color_chart)
            AppConst.Status.DEATH -> resources.getColor(R.color.death_color_chart)
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
        val confirmedLst = ArrayList<DataInDay>()
        val activeLst = ArrayList<DataInDay>()
        val recoveredLst = ArrayList<DataInDay>()
        val deathLst = ArrayList<DataInDay>()
        statusLst?.forEach {
            confirmedLst.add(DataInDay(it.date, it.confirmed))
            activeLst.add(DataInDay(it.date, it.active))
            recoveredLst.add(DataInDay(it.date, it.recovered))
            deathLst.add(DataInDay(it.date, it.deaths))
        }
        val dataSet0 = createDataSet(DataSetIndex.RECOVERED.value, AppConst.Status.RECOVERED, recoveredLst)
        val dataSet1 = createDataSet(DataSetIndex.ACTIVE.value, AppConst.Status.ACTIVE, activeLst)
        val dataSet2 = createDataSet(DataSetIndex.DEATH.value, AppConst.Status.DEATH, deathLst)

        if (isShowingStatus) {
            val dataSet3 = createDataSet(DataSetIndex.CONFIRMED.value, AppConst.Status.CONFIRMED, confirmedLst)
            chart.data = BarData(dataSet3, dataSet0, dataSet1, dataSet2)
        } else {
            chart.data = BarData(dataSet0, dataSet1, dataSet2)
        }

        chart.setFitBars(false)
        chart.invalidate()

        // reload UI
        updatePeriodTime()
        resetStatusFilter()
    }
}