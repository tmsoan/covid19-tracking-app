package com.anos.covid19.di

import com.anos.covid19.di.module.AppConfigModule
import com.anos.covid19.di.module.ApplicationModule
import com.anos.covid19.di.module.NetworkModule
import com.anos.covid19.MyApp
import com.anos.covid19.repository.DataRepository
import com.anos.covid19.views.base.BaseActivity
import com.anos.covid19.views.base.BaseFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ApplicationModule::class,
    NetworkModule::class,
    AppConfigModule::class
])
@Singleton
interface IAppComponent {
    fun inject(application: MyApp)
    fun inject(application: BaseActivity)
    fun inject(baseFragment: BaseFragment)
    fun inject(dataRepository: DataRepository)
}