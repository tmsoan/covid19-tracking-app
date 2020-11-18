package com.anos.covid19.views.country

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.model.ScreenEventObject
import com.anos.covid19.views.base.BaseFragment
import com.anos.covid19.views.countrydetail.CountryDetailsActivity
import kotlinx.android.synthetic.main.fragment_all_country.*

class AllCountriesFragment : BaseFragment(), CountriesSearchAdapter.Interaction {

    private var countries: List<Country>? = null
    private var adapter: CountriesSearchAdapter? = null

    companion object {
        const val NAME = "AllCountriesFragment"
        fun getInstance(countries: ArrayList<Country>): AllCountriesFragment{
            val fragment = AllCountriesFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("countries", countries)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_all_country

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countries = it.getParcelableArrayList("countries")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    override fun initLayout() {
        // list view
        adapter = CountriesSearchAdapter(ArrayList(), this)
        val llManager = LinearLayoutManager(context)
        llManager.orientation = LinearLayoutManager.VERTICAL
        rc_countries_search?.setHasFixedSize(false)
        rc_countries_search?.layoutManager = llManager
        rc_countries_search?.adapter = adapter

        btn_close.setOnClickListener {
            et_search_box?.setText("")
        }
        imv_back.setOnClickListener {
            getCurrentActivity()?.onBackPressed()
        }

        searchBoxHandling()
    }

    private fun searchBoxHandling() {
        et_search_box?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                et_search_box?.text?.trim()?.let {
                    adapter?.filter?.filter(it)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun loadData() {
        countries?.let { lstItems ->
            val items = lstItems.sortedWith(compareByDescending {it.totalConfirmed})
            var i = 1
            items.forEach {
                it.order = i++
            }
            adapter?.update(items, "")
        }
    }

    override fun onItemSelected(position: Int, code: String) {
        countries?.forEach { item ->
            if (code == item.countryCode) {
                openScreen(ScreenEventObject(ScreenEventObject.ScreenType.ACTIVITY, CountryDetailsActivity.NAME, item))
                return@forEach
            }
        }
    }
}