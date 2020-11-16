package com.anos.covid19.views.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anos.covid19.R
import com.anos.covid19.model.CountryItem
import com.anos.covid19.utils.DialogUtil
import com.anos.covid19.utils.getUpdatedDateString
import com.anos.covid19.utils.obtainViewModel
import com.anos.covid19.viewmodel.DataViewModel
import com.anos.covid19.views.MainActivity
import com.anos.covid19.views.base.BaseFragment
import com.anos.covid19.views.country.CountrySearchBottomSheet
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeFragment : BaseFragment(), CountrySearchBottomSheet.ICountrySearchCallback {

    private lateinit var dataViewModel: DataViewModel
    private var defaultCountryCode = "VN"

    private var countryDialog: CountrySearchBottomSheet? = null
    private var showingCountrySearch = false



    override fun getLayoutId() = R.layout.fragment_home

    companion object {
        const val NAME = "HomeFragment"
        fun newInstance() = HomeFragment()
    }

    override fun initLayout() {
        swipe_container.setOnRefreshListener(onSwipeRefreshListener)
        swipe_container.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        tv_country_name.setOnClickListener(onCountryNameClicked)
    }

    private fun initData() {
        dataViewModel = (getCurrentActivity() as MainActivity).obtainViewModel(DataViewModel::class.java)

        handleLoading()
        handleErrorResponse()
        handleDataResponse()
        handleLoadCountries()

        fetchSummaryData()
        fetchNeededData()
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

    private fun handleDataResponse() {
        dataViewModel.summaryResult.observe(viewLifecycleOwner, Observer {
            if (it == null)
                return@Observer
            // update global
            it.global?.let { global ->
                globalCasesView?.update(global)
            }
            it.date?.let { date ->
                tv_global_updated_date?.text = getUpdatedDateString(date)
                tv_country_updated_date?.text = getUpdatedDateString(date)
            }

            // update current country
            it.countries?.single { country ->
                country.countryCode == defaultCountryCode
            }?.let { country ->
                // update country layout
                countryCaseView?.update(country)
                // country name
                tv_country_name?.text = country.country
            }
        })
    }

    private fun handleLoadCountries() {
        dataViewModel.countriesResult.observe(viewLifecycleOwner, Observer {
            if (it == null)
                return@Observer
        })
    }

    /**
     * start loading Summary
     */
    private fun fetchSummaryData() {
        dataViewModel.getSummary()
    }

    private fun fetchNeededData() {
        dataViewModel.getCountries(false)
    }

    /**
     * change country action
     */
    private val onCountryNameClicked = View.OnClickListener {
        showingCountrySearch = true
        countryDialog = CountrySearchBottomSheet.getInstance()
        countryDialog?.listener = this
        countryDialog?.show(childFragmentManager, CountrySearchBottomSheet::javaClass.name)

        // fetch countries
        dataViewModel.getCountries(false)
    }

    override fun onDialogShowing() {
        dataViewModel.countries?.let {
            countryDialog?.loadCountries(it)
        }
    }

    override fun onDialogDismiss() {
        showingCountrySearch = false
    }
}