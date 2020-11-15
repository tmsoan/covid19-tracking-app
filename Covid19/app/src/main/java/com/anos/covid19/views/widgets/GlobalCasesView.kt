package com.anos.covid19.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.anos.covid19.R
import com.anos.covid19.model.Global
import com.anos.covid19.model.GlobalChartItem
import com.anos.covid19.utils.getIntThousandFormat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.layout_global_cases_view.view.*
import timber.log.Timber
import java.util.*


class GlobalCasesView : RelativeLayout {

    private val parties = arrayOf(
        "Active", "Recovered", "Death"
    )

    private var global: Global? = null

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
        addView(LayoutInflater.from(context).inflate(R.layout.layout_global_cases_view, null))

        initChartLayout()
    }

    /**
     * setup layout for Chart
     */
    private fun initChartLayout() {
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.centerText = generateCenterSpannableText()
        chart.setDrawCenterText(true)

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)
        chart.holeRadius = 70f

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)
        chart.transparentCircleRadius = 70.5f

        // enable values
        chart.setDrawEntryLabels(false)

        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        // add a selection listener
        chart.setOnChartValueSelectedListener(onChartValueSelectedListener)

        // disable default legend
        chart.legend.isEnabled = false
    }

    /**
     * load chart data
     */
    private fun loadChartData(list: List<GlobalChartItem>) {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in list.indices) {
            entries.add(PieEntry(list[i].value.toFloat(), parties[i % parties.size],0))
        }

        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 0.0f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 2f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (i in list.indices) {
            colors.add(resources.getColor(list[i].colorRes))
        }
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setDrawValues(false)
        chart.data = data

        // undo all highlights
        chart.highlightValues(null)

        chart.animateY(1200, Easing.EaseInOutQuad)

        chart.invalidate()
    }

    private val onChartValueSelectedListener = object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            Timber.e("onValueSelected: ${e?.toString()}")
        }

        override fun onNothingSelected() {
        }
    }

    private fun generateCenterSpannableText(): SpannableString? {
        val s1 = "Total"
        val s = SpannableString("$s1")
        s.setSpan(RelativeSizeSpan(2f), 0, s1.length, 0)
        s.setSpan(
            StyleSpan(Typeface.NORMAL),
            0,
            s.length,
            0
        )
        return s
    }

    fun update(data: Global) {
        global = data
        val lstData = ArrayList<GlobalChartItem>()
        // legend information
        global?.getActiveCases()?.let {
            lstData.add(GlobalChartItem("Active", it, R.color.active_color))
            tv_no_active?.setText(getIntThousandFormat(it))
        }
        global?.totalRecovered?.let {
            lstData.add(GlobalChartItem("Recovered", it, R.color.recovered_color))
            tv_no_recovered?.setText(getIntThousandFormat(it))
        }
        global?.totalDeaths?.let {
            lstData.add(GlobalChartItem("Death", it, R.color.death_color))
            tv_no_death?.setText(getIntThousandFormat(it))
        }
        global?.newDeaths?.let {
            tv_no_death_increase?.setText("+${getIntThousandFormat(it)}")
        }
        // total
        global?.totalConfirmed?.let {
            tv_total_cases?.setText("Total ${getIntThousandFormat(it)}")
        }

        // update chart
        loadChartData(lstData)
    }
}