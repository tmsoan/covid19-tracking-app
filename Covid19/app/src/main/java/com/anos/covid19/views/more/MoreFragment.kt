package com.anos.covid19.views.more

import android.os.Bundle
import com.anos.covid19.R
import com.anos.covid19.views.base.BaseFragment

class MoreFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_more

    override fun initLayout() {
    }

    companion object {
        const val NAME = "MoreFragment"
        fun newInstance() = MoreFragment()
    }
}