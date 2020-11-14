package com.anos.covid19.di.module

import android.content.Context
import com.anos.covid19.MyApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val app: MyApp) {

    @Provides
    @Singleton
    fun provideApplication(): MyApp {
        return app
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app.applicationContext
    }
}