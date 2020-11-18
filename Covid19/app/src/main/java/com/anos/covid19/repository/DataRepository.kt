package com.anos.covid19.repository

import com.anos.covid19.MyApp
import com.anos.covid19.helper.SimpleCacheManager
import com.anos.covid19.model.CountryItem
import com.anos.covid19.model.CountryStatus
import com.anos.covid19.model.CountryTotalStatus
import com.anos.covid19.model.response.SummaryResponse
import com.anos.covid19.network.DataResponse
import com.anos.covid19.network.IAppAPI
import com.anos.covid19.repository.interfaces.IDataRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class DataRepository : BaseRepository(), IDataRepository {

    init {
        MyApp.appComponent.inject(this)
    }

    @Inject
    lateinit var api: IAppAPI

    override suspend fun getSummary(refresh: Boolean, callback: IDataRepository.SummaryCallback) {
        // check in cache first
        if (!refresh) {
            SimpleCacheManager.getInstance(MyApp.instance).summaryResponse?.let {
                callback.onSummaryLoaded(DataResponse.create(it))
                return
            }
        }
        api.getSummary().enqueue(object : Callback<SummaryResponse> {
            override fun onResponse(call: Call<SummaryResponse>, response: Response<SummaryResponse>) {
                val dataResponse = DataResponse.create(response)
                callback.onSummaryLoaded(dataResponse)
                SimpleCacheManager.getInstance(MyApp.instance).summaryResponse = response
            }

            override fun onFailure(call: Call<SummaryResponse>, t: Throwable) {
                t.printStackTrace()
                callback.onSummaryLoaded(DataResponse.createError(0, t))
            }
        })
    }

    override suspend fun getCountries(callback: IDataRepository.CountriesCallback) {
        api.getCountries().enqueue(object : Callback<List<CountryItem>> {
            override fun onResponse(call: Call<List<CountryItem>>, response: Response<List<CountryItem>>) {
                val dataResponse = DataResponse.create(response)
                callback.onCountriesLoaded(dataResponse)
            }

            override fun onFailure(call: Call<List<CountryItem>>, t: Throwable) {
                t.printStackTrace()
                callback.onCountriesLoaded(DataResponse.createError(0, t))
            }
        })
    }

    override suspend fun getCountryAllStatus(slug: String, from: String, to: String, callback: IDataRepository.CountryAllStatusCallback) {
        api.getCountryAllStatus(slug, from, to).enqueue(object : Callback<List<CountryStatus>> {
            override fun onResponse(call: Call<List<CountryStatus>>, response: Response<List<CountryStatus>>) {
                val dataResponse = DataResponse.create(response)
                callback.onCountryStatusLoaded(dataResponse)
            }

            override fun onFailure(call: Call<List<CountryStatus>>, t: Throwable) {
                t.printStackTrace()
                callback.onCountryStatusLoaded(DataResponse.createError(0, t))
            }
        })
    }

    override suspend fun getCountryTotalByStatus(slug: String, status: String, from: String, to: String, callback: IDataRepository.CountryTotalStatusCallback) {
        api.getCountryTotalByStatus(slug, status, from, to).enqueue(object : Callback<List<CountryTotalStatus>> {
            override fun onResponse(call: Call<List<CountryTotalStatus>>, response: Response<List<CountryTotalStatus>>) {
                val dataResponse = DataResponse.create(response)
                callback.onCountryStatusLoaded(dataResponse)
            }

            override fun onFailure(call: Call<List<CountryTotalStatus>>, t: Throwable) {
                t.printStackTrace()
                callback.onCountryStatusLoaded(DataResponse.createError(0, t))
            }
        })
    }

    override suspend fun getCountryTotalAllStatus(slug: String, from: String, to: String, callback: IDataRepository.CountryAllStatusCallback) {
        api.getCountryTotalAllStatus(slug, from, to).enqueue(object : Callback<List<CountryStatus>> {
            override fun onResponse(call: Call<List<CountryStatus>>, response: Response<List<CountryStatus>>) {
                val dataResponse = DataResponse.create(response)
                callback.onCountryStatusLoaded(dataResponse)
            }

            override fun onFailure(call: Call<List<CountryStatus>>, t: Throwable) {
                t.printStackTrace()
                callback.onCountryStatusLoaded(DataResponse.createError(0, t))
            }
        })
    }
}