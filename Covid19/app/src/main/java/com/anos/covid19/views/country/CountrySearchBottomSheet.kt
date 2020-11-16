package com.anos.covid19.views.country

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anos.covid19.R
import com.anos.covid19.model.CountryItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_bottom_sheet_country.*
import timber.log.Timber


class CountrySearchBottomSheet : BottomSheetDialogFragment(), CountriesSearchAdapter.Interaction {

    interface ICountrySearchCallback {
        fun onDialogShowing()
        fun onDialogDismiss()
    }

    companion object {
        fun getInstance(): CountrySearchBottomSheet = CountrySearchBottomSheet()
    }

    var listener: ICountrySearchCallback? = null
    var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var countries: List<CountryItem>? = null
    private var adapter: CountriesSearchAdapter? = null

    private var rcCountries: RecyclerView? = null


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
                    listener?.onDialogDismiss()
                    dismiss()
                }
            }
        })

        rcCountries = bottomSheet.findViewById(R.id.rc_countries_search)

        initRecyclerView()

        return bottomSheet
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onResume() {
        super.onResume()
        listener?.onDialogShowing()
        adapter?.countries = countries ?: ArrayList()
    }

    private fun initRecyclerView() {
        adapter = CountriesSearchAdapter(ArrayList(), this)
        val llManager = LinearLayoutManager(context)
        llManager.orientation = LinearLayoutManager.VERTICAL
        rcCountries?.setHasFixedSize(false)
        rcCountries?.layoutManager = llManager
        rcCountries?.adapter = adapter
    }

    fun loadCountries(lst: List<CountryItem>) {
        this.countries = lst
        adapter?.countries = countries ?: ArrayList()
    }

    override fun onItemSelected(position: Int, item: CountryItem) {

    }
}