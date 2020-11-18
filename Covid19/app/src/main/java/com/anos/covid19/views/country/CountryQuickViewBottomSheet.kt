package com.anos.covid19.views.country

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.views.widgets.CircleChartCasesView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CountryQuickViewBottomSheet : BottomSheetDialogFragment() {

    interface ICallback {
        fun onViewDetailsClicked(country: Country)
    }

    companion object {
        fun newInstance(country: Country): CountryQuickViewBottomSheet{
            val fragment = CountryQuickViewBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable("country", country)
            fragment.arguments = bundle
            return fragment
        }
    }

    var listener: ICallback? = null
    var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var country: Country? = null
    private var countryCasesView: CircleChartCasesView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog)

        arguments?.let {
            country = it.getParcelable("country")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.layout_bottom_sheet_country_quick_view, null)
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

        initLayout(bottomSheet)

        return bottomSheet
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onResume() {
        super.onResume()
        country?.let {
            countryCasesView?.update(it)
        }
    }

    private fun initLayout(bottomSheet: BottomSheetDialog) {
        bottomSheet.findViewById<ImageView>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }
        bottomSheet.findViewById<Button>(R.id.btn_view_details)?.setOnClickListener {
            country?.let {
                dismiss()
                listener?.onViewDetailsClicked(it)
            }
        }
        bottomSheet.findViewById<TextView>(R.id.tv_country_name)?.text = country?.country
        countryCasesView = bottomSheet.findViewById(R.id.countryCasesView)
    }

}