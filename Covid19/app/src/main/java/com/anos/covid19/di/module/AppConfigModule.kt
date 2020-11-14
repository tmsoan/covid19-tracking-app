package com.anos.covid19.di.module

import android.content.Context
import com.anos.covid19.helper.AppConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppConfigModule {

    @Provides
    @Singleton
    fun provideAppConfig(context: Context): AppConfig {
        return AppConfig.getInstance(context)
    }

}