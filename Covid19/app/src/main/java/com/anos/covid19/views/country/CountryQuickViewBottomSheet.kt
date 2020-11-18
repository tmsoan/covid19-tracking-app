package com.anos.covid19.views.country

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.anos.covid19.MyApp
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.utils.saveImage
import com.anos.covid19.views.widgets.CircleChartCasesView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


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
        bottomSheet.findViewById<ImageView>(R.id.btn_share)?.setOnClickListener {
            country?.let {
                val mainView = bottomSheet.findViewById<LinearLayout>(R.id.ln_main_view)
                mainView?.let {
                    handleSharing(it)
                }
            }
        }
        bottomSheet.findViewById<TextView>(R.id.tv_country_name)?.text = country?.country
        countryCasesView = bottomSheet.findViewById(R.id.countryCasesView)
    }

    private fun handleSharing(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap: Bitmap = Bitmap.createBitmap(
                    view.width,
                    view.height,
                    Bitmap.Config.ARGB_8888)
            val c = Canvas(bitmap)
            view.draw(c)

            val uri = saveImage(MyApp.instance.applicationContext, bitmap)
            Timber.e("Image saved: ${uri.toString()}")
            uri?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    shareImageUri(it)
                    dismiss()
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
            activity?.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        activity?.startActivity(chooser)
    }
}