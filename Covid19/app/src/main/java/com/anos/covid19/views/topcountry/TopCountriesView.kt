package com.anos.covid19.views.topcountry

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.views.country.CountryQuickViewBottomSheet
import kotlinx.android.synthetic.main.layout_top_countries_view.view.*


class TopCountriesView : LinearLayout, TopCountriesAdapter.Interaction {

    interface ICallback {
        fun onCountryItemClicked(country: Country)
        fun onViewAllCountriesClicked(countries: List<Country>)
    }

    var listener: ICallback? = null
    private var countries: List<Country>? = null
    private var adapter: TopCountriesAdapter? = null

    private var countryDialog: CountryQuickViewBottomSheet? = null

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

        btn_view_all.setOnClickListener {
            viewAllCountries()
        }
    }

    private fun initRecyclerView() {
        adapter = TopCountriesAdapter(ArrayList(), this)
        val llManager = LinearLayoutManager(context)
        llManager.orientation = LinearLayoutManager.VERTICAL
        rc_top_countries?.setHasFixedSize(false)
        rc_top_countries?.layoutManager = llManager
        rc_top_countries?.adapter = adapter
    }

    fun loadCountries(lst: List<Country>) {
        initRecyclerView()
        this.countries = lst
        countries?.let { lstItems ->
            var count = 0
            val sortedList = lstItems.sortedWith(compareByDescending {it.totalConfirmed})
            val tmpLst: ArrayList<Country> = ArrayList()
            sortedList.forEach {
                if (count == 50)
                    return@forEach
                tmpLst.add(it)
                count++
            }
            adapter?.update(tmpLst)
        }
    }

    override fun onTopCountryItemSelected(country: Country) {
        listener?.onCountryItemClicked(country)
    }

    private fun viewAllCountries() {
        countries?.let {
            listener?.onViewAllCountriesClicked(it)
        }
    }

}