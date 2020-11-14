package com.anos.covid19.views.maps

import android.os.Bundle
import com.anos.covid19.R
import com.anos.covid19.views.base.BaseFragment

class MapsFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_maps

    override fun initLayout() {
    }

    companion object {
        const val NAME = "MapsFragment"
        fun newInstance() = MapsFragment()
    }
}