package com.anos.covid19.views.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anos.covid19.R
import com.anos.covid19.utils.DialogUtil
import com.anos.covid19.utils.obtainViewModel
import com.anos.covid19.viewmodel.DataViewModel
import com.anos.covid19.views.MainActivity
import com.anos.covid19.views.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeFragment : BaseFragment() {

    private lateinit var dataViewModel: DataViewModel

    override fun getLayoutId() = R.layout.fragment_home

    companion object {
        const val NAME = "HomeFragment"
        fun newInstance() = HomeFragment()
    }

    override fun initLayout() {
        swipe_container.setOnRefreshListener(onSwipeRefreshListener)
        swipe_container.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
    }

    private fun initData() {
        dataViewModel = (getCurrentActivity() as MainActivity).obtainViewModel(DataViewModel::class.java)

        handleLoading()
        handleErrorResponse()

        fetchSummaryData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private val onSwipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        fetchSummaryData()
    }

    private fun handleLoading() {
        dataViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                swipe_container.isRefreshing = it
            }
        })
    }

    private fun handleErrorResponse() {
        dataViewModel.responseError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it.isNotEmpty()) {
                    getCurrentActivity()?.showMyDialog(getString(R.string.app_name), it, DialogUtil.Style.SINGLE_OK, null)
                }
            }
        })
    }

    /**
     * start loading Summary
     */
    private fun fetchSummaryData() {
        dataViewModel.getSummary()
    }
}