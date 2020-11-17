package com.anos.covid19.views.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.utils.AppConst
import com.anos.covid19.utils.DialogUtil
import com.anos.covid19.utils.getUpdatedDateString
import com.anos.covid19.utils.obtainViewModel
import com.anos.covid19.viewmodel.DataViewModel
import com.anos.covid19.views.MainActivity
import com.anos.covid19.views.base.BaseFragment
import com.anos.covid19.views.country.CountryQuickViewBottomSheet
import com.anos.covid19.views.country.CountrySearchBottomSheet
import com.anos.covid19.views.topcountry.TopCountriesView
import com.anos.covid19.views.widgets.CountryChartView
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : BaseFragment(), CountrySearchBottomSheet.ICountrySearchCallback {

    private lateinit var dataViewModel: DataViewModel

    private var selectedCountryCode = "VN"
    private var selectedCountrySlug = "vietnam"

    private var topCountryQuickViewDialog: CountryQuickViewBottomSheet? = null
    private var countryDialog: CountrySearchBottomSheet? = null
    private var requireLoading = false



    override fun getLayoutId() = R.layout.fragment_home

    companion object {
        const val NAME = "HomeFragment"
        fun newInstance() = HomeFragment()
    }

    override fun initLayout() {
        swipe_container.setOnRefreshListener(onSwipeRefreshListener)
        swipe_container.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        tv_country_name.setOnClickListener(onCountryNameClicked)
        countryChartView.listener = onCountryViewCallback
        topCountriesView.listener = onTopCountryViewCallback
    }

    private fun initData() {
        dataViewModel = (getCurrentActivity() as MainActivity).obtainViewModel(DataViewModel::class.java)

        handleLoading()
        handleErrorResponse()
        handleDataResponse()
        handleLoadCountries()
        handleCountryTotalAllStatus()

        fetchSummaryData()
        fetchNeededData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        topCountryQuickViewDialog = null
        countryDialog = null
    }

    private val onSwipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        fetchSummaryData()
    }

    private fun handleLoading() {
        dataViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (requireLoading) {
                    if (it) {
                        showLoading()
                    } else {
                        hideLoading()
                        requireLoading = false
                    }
                } else {
                    swipe_container.isRefreshing = it
                }
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
        dataViewModel.summaryLiveData.observe(viewLifecycleOwner, Observer {
            if (it == null)
                return@Observer
            // update global
            it.global?.let { global ->
                globalCasesView?.update(global)
            }
            it.date?.let { date ->
                tv_global_updated_date?.text = getUpdatedDateString(date)
                tv_top_country_updated_date?.text = getUpdatedDateString(date)
            }

            // update current country
            it.countries?.forEach { country ->
                if (country.countryCode == selectedCountryCode) {
                    updateCountryView(false, country)
                    return@forEach
                }
            }

            // top countries
            it.countries?.let { lst ->
                updateTopCountries(lst)
            }
        })
    }

    private fun handleLoadCountries() {
        dataViewModel.countriesResult.observe(viewLifecycleOwner, Observer {
            if (it == null)
                return@Observer
        })
    }

    private fun handleCountryTotalAllStatus() {
        // update chart
        dataViewModel.countryTotalAllStatusLiveData.observe(viewLifecycleOwner, Observer {
            if (it == null)
                return@Observer
            countryChartView?.update(it)
        })
    }

    /**
     * refresh country view
     */
    private fun updateCountryView(loading: Boolean, country: Country) {
        selectedCountrySlug = country.slug ?: selectedCountrySlug
        // update country layout
        countryCaseView?.update(country)
        // country name
        tv_country_name?.text = country.country
        country.date?.let {
            tv_country_updated_date?.text = getUpdatedDateString(it)
        }

        // reload chart data
        countryChartView?.let {
            loadCountryTotalAllStatus(loading, it.fromDate, it.toDate)
        }
    }

    /**
     * load TOP 50 countries
     */
    private fun updateTopCountries(lst: List<Country>) {
        topCountriesView?.loadCountries(lst)
    }

    /**
     * start loading Summary
     */
    private fun fetchSummaryData() {
        dataViewModel.getSummary()
    }

    private fun fetchNeededData() {
        //dataViewModel.getCountries(false)
    }

    private fun loadCountryTotalAllStatus(loading: Boolean, fromDate: Date, toDate: Date) {
        requireLoading = loading
        dataViewModel.getCountryTotalAllStatus(selectedCountrySlug, fromDate, toDate)
    }

    /**
     * change country action
     */
    private val onCountryNameClicked = View.OnClickListener {
        countryDialog = CountrySearchBottomSheet.getInstance()
        countryDialog?.listener = this
        countryDialog?.show(childFragmentManager, CountrySearchBottomSheet::javaClass.name)
    }

    override fun onDialogShowing() {
        dataViewModel.summaryResult?.countries?.let {
            countryDialog?.loadCountries(it, selectedCountryCode)
        }
    }

    override fun onDialogSelectCountryDismiss(code: String?) {
        if (code == selectedCountryCode)
            return
        selectedCountryCode = code ?: selectedCountryCode
        // update current country
        dataViewModel.summaryResult?.countries?.forEach {
            if (it.countryCode == selectedCountryCode) {
                updateCountryView(true, it)
                return@forEach
            }
        }
    }

    private val onCountryViewCallback = object : CountryChartView.ICountryViewListener {
        override fun onChartDateChange(fromDate: Date, toDate: Date) {
            loadCountryTotalAllStatus(true, fromDate, toDate)
        }
    }

    private val onTopCountryViewCallback = object : TopCountriesView.ICallback {
        override fun onCountryItemClicked(country: Country) {
            topCountryQuickViewDialog = CountryQuickViewBottomSheet.getInstance(country)
            topCountryQuickViewDialog?.show(childFragmentManager, CountryQuickViewBottomSheet::javaClass.name)
        }
    }
}