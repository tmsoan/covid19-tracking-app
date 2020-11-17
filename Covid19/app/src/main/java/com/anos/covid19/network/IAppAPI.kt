package com.anos.covid19.network

import com.anos.covid19.model.CountryItem
import com.anos.covid19.model.CountryStatus
import com.anos.covid19.model.CountryTotalStatus
import com.anos.covid19.model.response.SummaryResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.*


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

    /**
     * Returns all cases by case type for a country. Country must be the slug from /countries or /summary. Cases must be one of: confirmed, recovered, deaths
     */
    @GET("/country/{country}")
    fun getCountryAllStatus(
            @Path("country") slug: String,
            @Query("from") from: String,
            @Query("to") to: String
    ): Call<List<CountryStatus>>

    /**
     * Returns all cases by case type for a country & status. Country must be the slug from /countries or /summary. Cases must be one of: confirmed, recovered, deaths
     */
    @GET("/total/country/{country}/status/{status}")
    fun getCountryTotalByStatus(
            @Path("country") slug: String,
            @Path("status") status: String,
            @Query("from") from: String,
            @Query("to") to: String
    ): Call<List<CountryTotalStatus>>

    /**
     * Returns all cases by case type for a country & status. Country must be the slug from /countries or /summary. Cases must be one of: confirmed, recovered, deaths
     */
    @GET("/total/country/{country}")
    fun getCountryTotalAllStatus(
            @Path("country") slug: String,
            @Query("from") from: String,
            @Query("to") to: String
    ): Call<List<CountryStatus>>
}