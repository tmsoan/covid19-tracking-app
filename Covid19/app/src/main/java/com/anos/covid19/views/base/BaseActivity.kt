package com.anos.covid19.views.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.anos.covid19.MyApp
import com.anos.covid19.R
import com.anos.covid19.helper.AppConfig
import com.anos.covid19.model.ScreenEventObject
import com.anos.covid19.utils.DialogUtil
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity(), BaseFragment.Callback {

    private var progressDialog: AlertDialog? = null

    @Inject
    protected lateinit var appConfig: AppConfig

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun openNewScreen(screenEventObject: ScreenEventObject)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        MyApp.appComponent.inject(this)
    }

    fun hideKeyboard() {
        this.currentFocus?.let {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                it.windowToken,
                0
            )
        }
    }

    fun showMyDialog(title: String?, message: String, style: DialogUtil.Style, listener: DialogUtil.IDialogListener?) {
        DialogUtil.get(this).showDialog(title, message, style, listener)
    }

    @SuppressLint("InflateParams")
    fun showLoading() {
        hideLoading()
        val view = layoutInflater.inflate(R.layout.progress_custom, null, false)
        val builder = AlertDialog.Builder(this, R.style.MyProgressDialogTheme).setView(view)
        if (!this.isFinishing) {
            progressDialog = builder.create()
            progressDialog?.apply {
                setCanceledOnTouchOutside(false)
                setCancelable(true)
                show()
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                findViewById<ImageView>(R.id.spinnerImageView)?.let {
                    val spinner = it.background as AnimationDrawable
                    spinner.start()
                }
            }
        }
    }

    fun hideLoading() {
        progressDialog?.dismiss()
    }

    fun isNetworkConnected(): Boolean {
        return MyApp.instance.isNetworkConnected
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String?) {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //-------------------------------------
    // Fragment handler
    //-------------------------------------
    protected open fun replaceFragment(
        fragmentContentId: Int,
        fragment: Fragment,
        animation: Boolean
    ) {
        // If the new fragment is the same with the last one in the back stack -> return
        getTopFragmentTagFromBackStack()?.let { tag ->
            if (tag == fragment.javaClass.name) return
        }
        val manager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        val tagName: String = fragment.javaClass.name
        manager.popBackStack(tagName, 1)
        if (animation) {
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }
        transaction.replace(fragmentContentId, fragment, tagName)
        transaction.addToBackStack(tagName)
        transaction.commit()
    }

    protected open fun replaceFragmentAndPopBackStack(
        fragmentContentId: Int,
        fragment: Fragment,
        animation: Boolean,
        isPopBackStackImmediate: Boolean
    ) {
        // If the new fragment is the same with the last one in the back stack -> return
        getTopFragmentTagFromBackStack()?.let { tag ->
            if (tag == fragment.javaClass.name) return
        }
        val tagName: String = fragment.javaClass.name
        if (isPopBackStackImmediate) {
            val manager = supportFragmentManager
            val fragmentPopped = manager.popBackStackImmediate(tagName, 0)
            if (fragmentPopped) return
        }
        replaceFragment(fragmentContentId, fragment, animation)
    }

    fun addFragment(
        fragmentContentId: Int,
        fragment: Fragment,
        animation: Boolean
    ) {
        // If the new fragment is the same with the last one in the back stack -> return
        getTopFragmentTagFromBackStack()?.let { tag ->
            if (tag == fragment.javaClass.name) return
        }
        val tagName: String = fragment.javaClass.name
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        if (animation) {
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }
        fragmentManager.popBackStack(tagName, 1)
        transaction.add(fragmentContentId, fragment, tagName)
        transaction.addToBackStack(tagName)
        transaction.commit()
    }

    private fun getTopFragmentTagFromBackStack(): String? {
        val index = supportFragmentManager.backStackEntryCount - 1
        if (index < 0)
            return null
        val backEntry: FragmentManager.BackStackEntry = supportFragmentManager.getBackStackEntryAt(
            index
        )
        return backEntry.name
    }

    protected fun clearHomeBackStack() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount <= 0) {
            return
        }
        val entry = fragmentManager.getBackStackEntryAt(0) ?: return
        fragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentManager.executePendingTransactions()
    }

    protected fun getTopFragmentFromBackStack(): Fragment? {
        val index = supportFragmentManager.backStackEntryCount - 1
        if (index < 0) return null
        val backEntry = supportFragmentManager.getBackStackEntryAt(index)
        return supportFragmentManager.findFragmentByTag(backEntry.name)
    }
}