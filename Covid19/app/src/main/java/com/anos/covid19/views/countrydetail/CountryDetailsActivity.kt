package com.anos.covid19.views.countrydetail

import android.content.Intent
import android.os.Bundle
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.model.ScreenEventObject
import com.anos.covid19.views.base.BaseActivity
import kotlinx.android.synthetic.main.activity_country_detail.*

class CountryDetailsActivity : BaseActivity() {

    companion object {
        const val NAME = "CountryDetailsActivity"

        fun startActivity(activity: BaseActivity, country: Country) {
            val intent = Intent(activity, CountryDetailsActivity::class.java)
            intent.putExtra("country", country)
            activity.startActivity(intent)
        }
    }

    private lateinit var country: Country

    override fun getLayoutId(): Int {
        return R.layout.activity_country_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            country = it.getParcelable("country") ?: Country()
        }

        val fragment = CountryDetailFragment.newInstance(country)
        addFragment(R.id.main_container, fragment, false)

        imv_back.setOnClickListener {
            onBackPressed()
        }
        imv_share.setOnClickListener {
            fragment.sharePage()
        }
        tv_title.text = country.country
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }

    override fun openNewScreen(screenEventObject: ScreenEventObject) {

    }
}