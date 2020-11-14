package com.anos.covid19.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    /*private val compositeDisposable = CompositeDisposable()
    fun getCompositeDisposable() = compositeDisposable*/

    override fun onCleared() {
        //compositeDisposable.dispose()
        super.onCleared()
    }

}