package com.anos.covid19.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> List<T>.swap(a: Int, b: Int): List<T> = this
        .toMutableList()
        .also {
            it[a] = this[b]
            it[b] = this[a]
        }

/**
 * create a deep copy
 */
fun<T: Any> T.cloneObj(): T {
    val stringSource = Gson().toJson(this, this::class.java)
    return Gson().fromJson<T>(stringSource, this::class.java)
}

inline fun justTry(block: () -> Unit) = try {
    block()
} catch (e: Throwable) {
    e.printStackTrace()
}

/**
 * execute in Main Thread
 */
inline fun uiThreadExecutor(crossinline block: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        block()
    }
}

/**
 * execute using RxJava observable in a new thread and then sends back to the main thread as a lambda
 */
@SuppressLint("CheckResult")
fun<T> asyncRxExecutor(heavyFunction: () -> T, response: (response: T?) -> Unit) {
    val observable = Single.create<T> {
        it.onSuccess(heavyFunction())
    }
    observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t: T? ->
                response(t)
            }
}

inline fun<E: Any, T: Collection<E>> T?.whenNotNullNorEmpty(block: (T) -> Unit) {
    if (this != null && this.isNotEmpty()) {
        block(this)
    }
}