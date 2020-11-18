package com.anos.covid19.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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


/**
 * Saves the image as PNG to the app's cache directory.
 * @param image Bitmap to save.
 * @return Uri of the saved file or null
 */
fun saveImage(context: Context, image: Bitmap): Uri? {
    // Should be processed in another thread
    val imagesFolder = File(context.cacheDir, "images")
    var uri: Uri? = null
    try {
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "shared_image.png")
        val stream = FileOutputStream(file)
        image.compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.flush()
        stream.close()
        uri = FileProvider.getUriForFile(context, "com.anos.covid19.fileprovider", file)
    } catch (e: IOException) {
        Timber.e("IOException while trying to write file for sharing: " + e.message)
    }
    return uri
}