package com.anos.covid19.network

import com.anos.covid19.model.CountryItem
import com.anos.covid19.model.response.SummaryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface IAppAPI {

    /**
     * A summary of new and total cases per country updated daily.
     */
    @GET("/summary")
    fun getSummary(): Call<SummaryResponse>

    /**
     * Returns all the available countries and provinces, as well as the country slug for per country requests.
     */
    @GET("/countries")
    fun getCountries(): Call<List<CountryItem>>
}