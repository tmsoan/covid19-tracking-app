package com.anos.covid19.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.anos.covid19.R


class TopCountriesView : LinearLayout {

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
        addView(LayoutInflater.from(context).inflate(R.layout.layout_top_countries_view, null))

    }
}