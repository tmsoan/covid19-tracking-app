package com.anos.covid19.utils

import android.content.Context
import android.net.ConnectivityManager

fun hasNetworkConnected(context: Context): Boolean {
    val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = conMgr.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}
