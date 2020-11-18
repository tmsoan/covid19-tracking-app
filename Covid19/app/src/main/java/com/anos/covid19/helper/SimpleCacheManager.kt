package com.anos.covid19.helper

import android.content.Context
import com.anos.covid19.model.response.SummaryResponse
import retrofit2.Response

class SimpleCacheManager(private val context: Context) {

    companion object {
        private var INSTANCE: SimpleCacheManager? = null

        fun getInstance(context: Context): SimpleCacheManager {
            if (INSTANCE == null) {
                INSTANCE = SimpleCacheManager(context)
            }
            return INSTANCE as SimpleCacheManager
        }
    }

    var summaryResponse: Response<SummaryResponse>? = null

}