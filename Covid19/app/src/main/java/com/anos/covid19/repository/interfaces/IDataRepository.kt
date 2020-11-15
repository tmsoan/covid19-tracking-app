package com.anos.covid19.repository.interfaces

import com.anos.covid19.model.CountryItem
import com.anos.covid19.model.response.SummaryResponse
import com.anos.covid19.network.DataResponse

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


    suspend fun getSummary(callback: SummaryCallback)

    suspend fun getCountries(callback: CountriesCallback)
}