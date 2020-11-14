package com.anos.covid19.di.module

import android.content.Context
import com.anos.covid19.BuildConfig
import com.anos.covid19.network.IAppAPI
import com.anos.covid19.network.LogInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Named("standard")
    @Singleton
    fun provideOkHttp(@Named("standard") builder: OkHttpClient.Builder, @Named("standard") interceptor: Interceptor): OkHttpClient {
        //HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory())
        if (BuildConfig.DEBUG) {
            // development build
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        builder.readTimeout(60, TimeUnit.SECONDS)
        builder.connectTimeout(60, TimeUnit.SECONDS)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    @Provides
    @Named("standard")
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return LogInterceptor()
    }

    @Provides
    @Named("standard")
    @Singleton
    fun provideOkHttpClient(context: Context): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @Provides
    @Named("standard")
    @Singleton
    fun provideRetrofit(@Named("standard") okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        return retrofitBuilder.build()
    }

    @Provides
    @Singleton
    fun provideAPI(@Named("standard") retrofit: Retrofit): IAppAPI {
        return retrofit.create(IAppAPI::class.java)
    }
}