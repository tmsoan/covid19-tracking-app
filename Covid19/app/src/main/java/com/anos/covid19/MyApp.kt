package com.anos.covid19

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import com.anos.covid19.di.DaggerIAppComponent
import com.anos.covid19.di.module.AppConfigModule
import com.anos.covid19.di.module.ApplicationModule
import com.anos.covid19.di.module.NetworkModule
import com.anos.covid19.eventbus.EventKey
import com.anos.covid19.eventbus.EventStore
import com.anos.covid19.helper.AppConfig
import com.anos.covid19.utils.hasNetworkConnected
import com.anos.covid19.di.IAppComponent
import com.app.javlininvest.helper.ConnectionStateMonitor
import timber.log.Timber
import javax.inject.Inject

class MyApp : Application() {

    companion object {
        @get:Synchronized
        lateinit var instance: MyApp

        lateinit var appComponent: IAppComponent
    }

    init {
        instance = this
    }

    @Inject
    lateinit var appConfig: AppConfig


    /** Handle network state */
    var isNetworkConnected: Boolean = false
    private lateinit var connectionStateMonitor: ConnectionStateMonitor

    // user login token
    var token: String? = null


    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        registerNetworkReceiver()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    private fun initAppComponent() {
        appComponent = DaggerIAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .appConfigModule(AppConfigModule())
            .networkModule(NetworkModule())
            .build()
        appComponent.inject(this)
    }

    fun isLoggedIn(): Boolean {
        return (!token.isNullOrEmpty())
    }

    private fun registerNetworkReceiver() {
        isNetworkConnected = hasNetworkConnected(this)
        connectionStateMonitor = ConnectionStateMonitor(this)
        connectionStateMonitor.enable(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isNetworkConnected = true
                EventStore.getInstance().postEventAction(EventKey.NETWORK_RECONNECTED)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isNetworkConnected = false
                EventStore.getInstance().postEventAction(EventKey.NETWORK_DISCONNECTED)
            }
        })
    }
}