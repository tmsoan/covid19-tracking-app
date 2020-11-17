package com.anos.covid19.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun hasNetworkConnected(context: Context): Boolean {
    val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = conMgr.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

fun getIntThousandFormat(value: Int): String {
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    formatter.applyPattern("#,###,###,###")
    return formatter.format(value)
}

@SuppressLint("SimpleDateFormat")
fun getUpdatedDateString(date: Date): String {
    val format = SimpleDateFormat("dd/MM/yyy")
    return format.format(date)
}

fun getDayInMonth(date: Date): String {
    val format = SimpleDateFormat("dd/MM")
    return format.format(date)
}

fun getDayInYear(date: Date): String {
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(date)
}

fun getDaysAgo(daysAgo: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    return calendar.time
}