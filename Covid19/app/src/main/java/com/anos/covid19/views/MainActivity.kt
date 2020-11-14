package com.anos.covid19.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.anos.covid19.R
import com.anos.covid19.model.ScreenEventObject
import com.anos.covid19.utils.*
import com.anos.covid19.views.base.BaseActivity
import com.anos.covid19.views.home.HomeFragment
import com.anos.covid19.views.maps.MapsFragment
import com.anos.covid19.views.maps.MapsFragment2
import com.anos.covid19.views.more.MoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BaseActivity(), FragNavController.RootFragmentListener,
        FragNavController.TransactionListener {

    private val fragNavController = FragNavController(supportFragmentManager, R.id.main_container)
    private val fragments = listOf(
            HomeFragment.newInstance(),
            MapsFragment2.newInstance(),
            MoreFragment.newInstance()
    )

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomBar()
        setupFragmentNavController(savedInstanceState)
    }

    override fun openNewScreen(screenEventObject: ScreenEventObject) {
    }

    private fun setupBottomBar() {
        bottom_navigation.setOnNavigationItemSelectedListener(setOnNavigationItemSelectedListener)
    }

    private val setOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var index = -1
        when (item.itemId) {
            R.id.action_home -> index = 0
            R.id.action_maps -> index = 1
            R.id.action_more -> index = 2
        }
        if (index != -1) {
            if (fragNavController.currentStackIndex == index) {
                clearCurrentStackIfNeed(true)
            }
            switchTabTo(index)
        }
        true
    }

    private fun setupFragmentNavController(savedInstanceState: Bundle?) {
        fragNavController.apply {
            rootFragmentListener = this@MainActivity
            transactionListener = this@MainActivity
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {
                }
            }
            fragmentHideStrategy = FragNavController.HIDE
            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    switchTabTo(index)
                }
            })
        }
        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)
    }

    private fun pushFragmentByFragNavController(event: ScreenEventObject) {
    }

    private fun pushFragmentWithFragNav(fragment: Fragment, hasAnimation: Boolean) {
        fragNavController.currentFrag?.let {
            if (fragment.javaClass.name == it.javaClass.name) {
                return
            }
        }
        fragNavController.pushFragment(fragment, if (hasAnimation) AppConst.fragNavTransactionOptions else null)
    }

    private fun clearCurrentStackIfNeed(reSelected: Boolean) {
        if (reSelected && fragNavController.size > 0) {
            fragNavController.clearStack(AppConst.fragPopTransactionOptions)
        }
    }

    private fun switchTabTo(tab: Int) {
        val currentTab = fragNavController.currentStackIndex
        val animation = if (tab > currentTab) {
            AppConst.fragFlashNavTransactionOptions
        } else {
            AppConst.fragFlashPopTransactionOptions
        }
        fragNavController.switchTab(tab, animation)
        // update bottom bar state
        bottom_navigation?.menu?.getItem(tab)?.isChecked = true
    }

    override val numberOfRootFragments = fragments.size

    override fun getRootFragment(index: Int): Fragment {
        return fragments[index]
    }

    override fun onFragmentTransaction(
            fragment: Fragment?,
            transactionType: FragNavController.TransactionType
    ) {
        Timber.d("onFragmentTransaction: ${fragment?.javaClass?.simpleName}")
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
        Timber.d("onTabTransaction: $index")
    }
}