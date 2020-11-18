package com.anos.covid19.views.countrydetail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.anos.covid19.MyApp
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.model.CountryStatus
import com.anos.covid19.utils.getDaysAgo
import com.anos.covid19.utils.getUpdatedDateString
import com.anos.covid19.utils.obtainViewModel
import com.anos.covid19.utils.saveImage
import com.anos.covid19.viewmodel.DataViewModel
import com.anos.covid19.views.base.BaseFragment
import com.anos.covid19.views.widgets.CountryChartView
import kotlinx.android.synthetic.main.fragment_country_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class CountryDetailFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_country_detail

    companion object {
        const val NAME = "CountryDetailFragment"

        fun newInstance(country: Country): CountryDetailFragment {
            val fragment = CountryDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable("country", country)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var dataViewModel: DataViewModel
    private var country: Country? = null
    private var filteredCountriesAdapter: FilteredCountriesAdapter? = null

    private var datePickerDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            country = it.getParcelable("country")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewModel = (getCurrentActivity() as CountryDetailsActivity).obtainViewModel(DataViewModel::class.java)
        initData()
    }

    override fun initLayout() {
        countryChartView.listener = onCountryViewCallback
        countryChartView.updateFilterLayout(false, true)

        tv_from_date.text = getUpdatedDateString(countryChartView.fromDate)
        tv_from_date.setOnClickListener {
            selectFromDateForChart()
        }
        tv_to_date.text = getUpdatedDateString(countryChartView.toDate)
        tv_to_date.setOnClickListener {
            selectToDateForChart()
        }

        initAllCasesList()
    }

    private fun initAllCasesList() {
        filteredCountriesAdapter = FilteredCountriesAdapter(ArrayList())
        val llManager = LinearLayoutManager(context)
        llManager.orientation = LinearLayoutManager.VERTICAL
        rcAllCases?.setHasFixedSize(false)
        rcAllCases?.layoutManager = llManager
        rcAllCases?.adapter = filteredCountriesAdapter
    }

    private fun initData() {
        country?.let { c ->
            tv_header.text = c.country
            countryCasesView.update(c)
        }

        handleCountryTotalAllStatus()
        handleLoading()

        loadCountryTotalAllStatus(countryChartView.fromDate, countryChartView.toDate)
    }

    private fun loadCountryTotalAllStatus(fromDate: Date, toDate: Date) {
        dataViewModel.getCountryTotalAllStatus(country?.slug ?: "", fromDate, toDate)
    }

    private fun handleCountryTotalAllStatus() {
        // update chart
        dataViewModel.countryTotalAllStatusLiveData.observe(viewLifecycleOwner, Observer {
            if (it == null)
                return@Observer
            // update chart
            countryChartView?.update(it)
            // update list all cases
            updateAllCasesList(it)
        })
    }

    private fun handleLoading() {
        dataViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun updateAllCasesList(statusList: List<CountryStatus>) {
        filteredCountriesAdapter?.update(statusList.sortedWith(compareByDescending { it.date }))
    }

    private val onCountryViewCallback = object : CountryChartView.ICountryViewListener {
        override fun onChartDateChange(fromDate: Date, toDate: Date) {
            loadCountryTotalAllStatus(fromDate, toDate)
        }
    }

    private fun selectFromDateForChart() {
        val cal = Calendar.getInstance()
        cal.time = countryChartView.fromDate
        val day = cal[Calendar.DAY_OF_MONTH]
        val month = cal[Calendar.MONTH]
        val year = cal[Calendar.YEAR]
        datePickerDialog = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.YEAR, year)
            if (cal.time.after(countryChartView.toDate)) {
                Toast.makeText(requireActivity(), "\'Start\' date is invalid. Please try again", Toast.LENGTH_SHORT).show()
            } else {
                tv_from_date?.text = "$dayOfMonth/${monthOfYear + 1}/$year"
                countryChartView.fromDate = cal.time
                performFilterChartAgain()
            }
        }, year, month, day)
        datePickerDialog?.setTitle("Please select \'From\' date")
        datePickerDialog?.datePicker?.maxDate = getDaysAgo(2).time
        datePickerDialog?.show()
    }

    private fun selectToDateForChart() {
        val cal = Calendar.getInstance()
        cal.time = countryChartView.toDate
        val day = cal[Calendar.DAY_OF_MONTH]
        val month = cal[Calendar.MONTH]
        val year = cal[Calendar.YEAR]
        datePickerDialog = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.YEAR, year)
            if (cal.time.before(countryChartView.fromDate)) {
                Toast.makeText(requireActivity(), "\'End\' date is invalid. Please try again", Toast.LENGTH_SHORT).show()
            } else {
                tv_to_date?.text = "$dayOfMonth/${monthOfYear + 1}/$year"
                countryChartView.toDate = cal.time
                performFilterChartAgain()
            }
        }, year, month, day)
        datePickerDialog?.setTitle("Please select \'To\' date")
        datePickerDialog?.datePicker?.maxDate = getDaysAgo(1).time
        datePickerDialog?.show()
    }

    private fun performFilterChartAgain() {
        loadCountryTotalAllStatus(countryChartView.fromDate, countryChartView.toDate)
    }

    fun sharePage() {
        tv_header?.visibility = View.VISIBLE
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap: Bitmap = Bitmap.createBitmap(
                    scrollView.getChildAt(0).width,
                    scrollView.getChildAt(0).height,
                    Bitmap.Config.ARGB_8888)
            val c = Canvas(bitmap)
            scrollView.getChildAt(0).draw(c)

            val uri = saveImage(MyApp.instance.applicationContext, bitmap)
            Timber.e("Image saved: ${uri.toString()}")
            uri?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    tv_header?.visibility = View.GONE
                    hideLoading()
                    shareImageUri(it)
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun shareImageUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        val chooser = Intent.createChooser(intent, "Share with...")
        val resInfoList: List<ResolveInfo> = MyApp.instance.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName: String = resolveInfo.activityInfo.packageName
            getCurrentActivity()?.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        getCurrentActivity()?.startActivity(chooser)
    }
}