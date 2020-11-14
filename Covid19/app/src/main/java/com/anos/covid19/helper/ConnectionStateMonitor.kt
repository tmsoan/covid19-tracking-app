package com.app.javlininvest.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class ConnectionStateMonitor(private val context: Context) {

    private var networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private var connectivityManager: ConnectivityManager? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    fun enable(callback: ConnectivityManager.NetworkCallback) {
        networkCallback = callback
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.registerNetworkCallback(networkRequest, callback)
    }

    fun disable() {
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
        }
    }
}