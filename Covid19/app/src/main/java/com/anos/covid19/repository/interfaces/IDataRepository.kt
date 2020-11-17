package com.anos.covid19.repository.interfaces

import com.anos.covid19.model.CountryItem
import com.anos.covid19.model.CountryStatus
import com.anos.covid19.model.CountryTotalStatus
import com.anos.covid19.model.response.SummaryResponse
import com.anos.covid19.network.DataResponse
import java.util.*

interface IDataRepository {
    /**
     * this callback for data summary API response
     */
    interface SummaryCallback {
        fun onSummaryLoaded(dataResponse: DataResponse<SummaryResponse>)
    }

    /**
     * this callback for countries API response
     */
    interface CountriesCallback {
        fun onCountriesLoaded(dataResponse: DataResponse<List<CountryItem>>)
    }

    /**
     * Get country's all status API response
     */
    interface CountryAllStatusCallback {
        fun onCountryStatusLoaded(dataResponse: DataResponse<List<CountryStatus>>)
    }

    /**
     * Get country's all status API response
     */
    interface CountryTotalStatusCallback {
        fun onCountryStatusLoaded(dataResponse: DataResponse<List<CountryTotalStatus>>)
    }


    suspend fun getSummary(callback: SummaryCallback)

    suspend fun getCountries(callback: CountriesCallback)

    suspend fun getCountryAllStatus(slug: String, from: String, to: String, callback: CountryAllStatusCallback)

    suspend fun getCountryTotalByStatus(slug: String, status: String, from: String, to: String, callback: CountryTotalStatusCallback)

    suspend fun getCountryTotalAllStatus(slug: String, from: String, to: String, callback: CountryAllStatusCallback)
}