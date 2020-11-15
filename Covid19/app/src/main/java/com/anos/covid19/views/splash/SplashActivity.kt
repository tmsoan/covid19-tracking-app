package com.anos.covid19.views.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.view.ViewCompat.animate
import com.anos.covid19.MyApp
import com.anos.covid19.R
import com.anos.covid19.model.ScreenEventObject
import com.anos.covid19.utils.ScreenUtils
import com.anos.covid19.utils.obtainViewModel
import com.anos.covid19.viewmodel.DataViewModel
import com.anos.covid19.views.MainActivity
import com.anos.covid19.views.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class SplashActivity : BaseActivity() {

    companion object {
        private const val SPLASH_TIME = 2000L
        private const val MSG_ID_GO_TO_APP = 2
        private const val MSG_ID_CLOSE = 1

        private const val STARTUP_DELAY = 300
        private const val ANIM_ITEM_DURATION = 1000
    }

    private lateinit var timer: Timer
    private var animationStarted = false

    private lateinit var dataViewModel: DataViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun openNewScreen(screenEventObject: ScreenEventObject) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        super.onCreate(savedInstanceState)

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                startFirstActivity()
            }
        }, SPLASH_TIME)

        dataViewModel = obtainViewModel(DataViewModel::class.java)

        fetchNeededData()
    }

    override fun onBackPressed() {
        //super.onBackPressed();
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (!hasFocus || animationStarted) {
            return
        }
        animate()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun startFirstActivity() {
        val intent = Intent(this, MainActivity::class.java)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun animate() {
        animate(img_logo)
                .translationY(-(ScreenUtils.dpToPx(this, 50f)))
                .alpha(1f)
                .setStartDelay(STARTUP_DELAY.toLong())
                .setDuration(ANIM_ITEM_DURATION.toLong())
                .setInterpolator(DecelerateInterpolator(1.2f))
                .start()
    }

    private fun fetchNeededData() {
    }
}