package com.anos.covid19.views.countrydetail

import com.anos.covid19.R
import com.anos.covid19.views.base.BaseFragment

class CountryDetailFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_country_detail

    override fun initLayout() {
    }

    companion object {
        const val NAME = "CountryDetailFragment"
        fun newInstance() = CountryDetailFragment()
    }
}