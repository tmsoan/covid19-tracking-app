package com.anos.covid19.repository

import com.anos.covid19.MyApp
import com.anos.covid19.model.response.SummaryResponse
import com.anos.covid19.network.DataResponse
import com.anos.covid19.network.IAppAPI
import com.anos.covid19.repository.interfaces.IDataRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DataRepository : BaseRepository(), IDataRepository {

    init {
        MyApp.appComponent.inject(this)
    }

    @Inject
    lateinit var api: IAppAPI

    override suspend fun getSummary(callback: IDataRepository.SummaryCallback) {
        api.getSummary().enqueue(object : Callback<SummaryResponse> {
            override fun onResponse(call: Call<SummaryResponse>, response: Response<SummaryResponse>) {
                val dataResponse = DataResponse.create(response)
                callback.onSummaryLoaded(dataResponse)
            }

            override fun onFailure(call: Call<SummaryResponse>, t: Throwable) {
                t.printStackTrace()
                callback.onSummaryLoaded(DataResponse.createError(0, t))
            }
        })
    }
}