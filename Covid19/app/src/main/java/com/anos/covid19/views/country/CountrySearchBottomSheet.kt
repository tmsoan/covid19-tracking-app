package com.anos.covid19.views.country

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.anos.covid19.R
import com.anos.covid19.model.CountryItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_bottom_sheet_country.*
import timber.log.Timber


class CountrySearchBottomSheet : BottomSheetDialogFragment(), CountriesSearchAdapter.Interaction {

    var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var countries: List<CountryItem>? = null
    private lateinit var adapter: CountriesSearchAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(context, R.layout.layout_bottom_sheet_country, null)
        bottomSheet.setContentView(view)

        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        bottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss()
                }
            }
        })

        initRecyclerView()

        return bottomSheet
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        // init listview
//        adapter = CountriesSearchAdapter(countries ?: ArrayList(), this)
//        val llManager = LinearLayoutManager(context)
//        llManager.orientation = LinearLayoutManager.VERTICAL
//        rc_countries?.let {
//            it.setHasFixedSize(false)
//            it.layoutManager = llManager
//            it.adapter = adapter
//        }
    }

    private fun initRecyclerView() {
        adapter = CountriesSearchAdapter(countries ?: ArrayList(), this)
        val llManager = LinearLayoutManager(context)
        llManager.orientation = LinearLayoutManager.VERTICAL
        rc_countries_search?.setHasFixedSize(false)
        rc_countries_search?.layoutManager = llManager
        rc_countries_search?.adapter = adapter

        Timber.e("============0 loadCountries: ${countries?.size} $rc_countries_search")
    }

    fun loadCountries(lst: List<CountryItem>) {
        this.countries = lst
//        adapter?.countries = countries ?: ArrayList()
        et_search_box?.setText("loadCountries")
        Timber.e("============1 loadCountries: ${countries?.size} $rc_countries_search")
    }

    override fun onItemSelected(position: Int, item: CountryItem) {

    }
}