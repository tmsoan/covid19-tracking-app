package com.anos.covid19.repository.interfaces

import com.anos.covid19.model.response.SummaryResponse
import com.anos.covid19.network.DataResponse

interface IDataRepository {
    /**
     * this callback for data summary API response
     */
    interface SummaryCallback {
        fun onSummaryLoaded(dataResponse: DataResponse<SummaryResponse>)
    }

    suspend fun getSummary(callback: SummaryCallback)
}