package com.anos.covid19.views.country

import android.os.Bundle
import com.anos.covid19.R
import com.anos.covid19.views.base.BaseFragment

class CountryFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_country

    override fun initLayout() {
    }

    companion object {
        const val NAME = "CountryFragment"
        fun newInstance() = CountryFragment()
    }
}