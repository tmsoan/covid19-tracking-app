package com.anos.covid19.views.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    private val isLoading = ObservableBoolean()

    private val compositeDisposable = CompositeDisposable()
    fun getCompositeDisposable() = compositeDisposable

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getIsLoading(): ObservableBoolean {
        return isLoading
    }

    fun setIsLoading(value: Boolean) {
        isLoading.set(value)
    }
}