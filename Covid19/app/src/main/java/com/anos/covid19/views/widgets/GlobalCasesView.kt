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
import java.util.ArrayList


class GlobalCasesView : RelativeLayout {

    private val parties = arrayOf(
        "Confirmed", "Recovered", "Death"
    )

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
        loadChartData(3, 100f)
    }

    /**
     * setup layout for Chart
     */
    private fun initChartLayout() {
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
//        chart.setExtraOffsets(5f, 5f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

//        chart.centerText = generateCenterSpannableText()
//        chart.setDrawCenterText(true)

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

        chart.animateY(1400, Easing.EaseInOutQuad)

        // disable default legend
        chart.legend.isEnabled = false
    }

    /**
     * load chart data
     */
    private fun loadChartData(count: Int, range: Float) {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties[i % parties.size],
                    resources.getDrawable(R.drawable.ic_launcher_foreground)
                )
            )
        }

        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 0.0f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 2f

        // add a lot of colors
        val colors = ArrayList<Int>()
        val ta: TypedArray = resources.obtainTypedArray(R.array.pieChartColors)
        for (i in 0 until ta.length()) {
            colors.add(ta.getColor(i, 0))
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
        val s1 = "Sharpe Ratio"
        val s2 = "1.96"
        val s = SpannableString("$s1\n$s2")
        s.setSpan(RelativeSizeSpan(1.9f), 0, s1.length, 0)
        s.setSpan(RelativeSizeSpan(3.5f), s1.length + 1, s.length, 0)
        s.setSpan(
            StyleSpan(Typeface.NORMAL),
            0,
            s.length,
            0
        )
        s.setSpan(
            StyleSpan(Typeface.BOLD),
            s1.length + 1,
            s.length,
            0
        )
        s.setSpan(
            ForegroundColorSpan(ColorTemplate.getHoloBlue()),
            s1.length + 1,
            s.length,
            0
        )
        return s
    }
}