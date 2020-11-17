package com.anos.covid19.views.country

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anos.covid19.R
import com.anos.covid19.model.Country
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
        fun onDialogSelectCountryDismiss(code: String?)
    }

    companion object {
        fun getInstance(): CountrySearchBottomSheet = CountrySearchBottomSheet()
    }

    var listener: ICountrySearchCallback? = null
    var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var selectedCountry: String? = null
    private var countries: List<Country>? = null
    private var adapter: CountriesSearchAdapter? = null

    private var rcCountries: RecyclerView? = null
    private var etSearchBox: EditText? = null


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

        rcCountries = bottomSheet.findViewById(R.id.rc_countries_search)
        initRecyclerView()

        etSearchBox = bottomSheet.findViewById(R.id.et_search_box)
        searchBoxHandling()

        bottomSheet.findViewById<ImageView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }

        return bottomSheet
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onResume() {
        super.onResume()
        listener?.onDialogShowing()
    }

    private fun initRecyclerView() {
        adapter = CountriesSearchAdapter(ArrayList(), this)
        val llManager = LinearLayoutManager(context)
        llManager.orientation = LinearLayoutManager.VERTICAL
        rcCountries?.setHasFixedSize(false)
        rcCountries?.layoutManager = llManager
        rcCountries?.adapter = adapter
    }

    fun loadCountries(lst: List<Country>, currentCountryCode: String) {
        this.countries = lst
        this.selectedCountry = currentCountryCode
        countries?.let { lstItems ->
            val items = lstItems.sortedWith(compareByDescending {it.totalConfirmed})
            var i = 1
            items.forEach {
                it.order = i++
            }
            adapter?.update(items, currentCountryCode)
        }
    }

    override fun onItemSelected(position: Int, code: String) {
        selectedCountry = code
        listener?.onDialogSelectCountryDismiss(selectedCountry)
        dismiss()
    }

    private fun searchBoxHandling() {
        etSearchBox?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                etSearchBox?.text?.trim()?.let {
                    adapter?.filter?.filter(it)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}