package com.anos.covid19.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.utils.getIntThousandFormat
import kotlinx.android.synthetic.main.layout_country_cases_view.view.*


class CountryCasesView : LinearLayout {

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
        addView(LayoutInflater.from(context).inflate(R.layout.layout_country_cases_view, null))
    }

    fun update(country: Country) {
        country.totalConfirmed?.let {
            tv_cases_confirmed?.setText("Total ${getIntThousandFormat(it)}")
        }

        tv_cases_active?.text = getIntThousandFormat(country.getActiveCases())

        country.totalRecovered?.let {
            tv_cases_recovered?.setText(getIntThousandFormat(it))
        }
        country.totalDeaths?.let {
            tv_cases_death?.setText(getIntThousandFormat(it))
        }


        country.newConfirmed?.let {
            tv_cases_new_confirmed?.setText(getNewCaseLabel(it))
        } ?: kotlin.run {
            tv_cases_new_confirmed?.text = ""
        }
        country.newRecovered?.let {
            lb_new_recovered?.setText(getNewCaseLabel(it))
        } ?: kotlin.run {
            lb_new_recovered?.text = ""
        }

        country.newDeaths?.let {
            lb_new_death?.setText(getNewCaseLabel(it))
        } ?: kotlin.run {
            lb_new_death?.text = ""
        }
    }

    private fun getNewCaseLabel(cases: Int?): String {
        if (cases == null || cases == 0) {
            return ""
        }
        return "+${getIntThousandFormat(cases)}"
    }
}