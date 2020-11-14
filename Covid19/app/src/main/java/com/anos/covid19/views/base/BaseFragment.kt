package com.anos.covid19.views.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.anos.covid19.MyApp
import com.anos.covid19.helper.AppConfig
import com.anos.covid19.model.ScreenEventObject
import com.anos.covid19.views.base.BaseActivity
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    private var currentActivity: BaseActivity? = null

    @Inject
    protected lateinit var appConfig: AppConfig

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initLayout()

    //todo: no need for now, coz using livedata
    //abstract fun initData()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            currentActivity = context
            context.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApp.appComponent.inject(this)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
    }

    override fun onDetach() {
        currentActivity = null
        super.onDetach()
    }

    /**
     * if the fragment is doing too much work on load, then the system will basically skip the enter
     * animation and just display the fragment. In my case the solution was to create an animation
     * listener in onCreateAnimation to wait until the enter animation had finished before doing
     * the process intensive work that was causing the animation to be skipped
     */
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim == 0) {
            //initData()
            return null //super.onCreateAnimation(transit, enter, nextAnim)
        }
        val anim = AnimationUtils.loadAnimation(context, nextAnim)
        if (anim != null) {
            view?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    view?.setLayerType(View.LAYER_TYPE_NONE, null)
                    // Do any process intensive work that can wait until after fragment has loaded
                    if (isVisible) {
                        // start load data
                        //initData()
                    }
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        } else {
            //initData()
        }
        return anim
    }

    fun getCurrentActivity(): BaseActivity? {
        return currentActivity
    }

    fun hideKeyboard() {
        currentActivity?.hideKeyboard()
    }

    fun isNetworkConnected(): Boolean {
        return currentActivity?.isNetworkConnected() ?: MyApp.instance.isNetworkConnected
    }

    fun showLoading() {
        currentActivity?.showLoading()
    }

    fun hideLoading() {
        currentActivity?.hideLoading()
    }

    fun openScreen(screenEventObject: ScreenEventObject) {
        getCurrentActivity()?.openNewScreen(screenEventObject)
    }

    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String?)
    }
}