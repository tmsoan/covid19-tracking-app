package com.anos.covid19.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anos.covid19.model.response.SummaryResponse
import com.anos.covid19.network.DataErrorResponse
import com.anos.covid19.network.DataResponse
import com.anos.covid19.network.DataSuccessResponse
import com.anos.covid19.repository.DataRepository
import com.anos.covid19.repository.interfaces.IDataRepository
import kotlinx.coroutines.launch

class DataViewModel : BaseViewModel() {

    private val dataRepository: IDataRepository = DataRepository()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    /**
     * this livedata to listen the error from response
     */
    private val _responseError = MutableLiveData<String?>()
    val responseError: LiveData<String?> = _responseError

    /**
     * this livedata to listen the RESULT from Summary API's response
     */
    private val _summaryResult = MutableLiveData<SummaryResponse>()
    val summaryResult: LiveData<SummaryResponse> = _summaryResult


    /**
     * fetch new Summary data,
     * and update the result into the livedata object to display UI
     */
    fun getSummary() {
        viewModelScope.launch {
            _loading.postValue(true)
            dataRepository.getSummary(object : IDataRepository.SummaryCallback {
                override fun onSummaryLoaded(dataResponse: DataResponse<SummaryResponse>) {
                    if (dataResponse is DataSuccessResponse) {
                        _summaryResult.postValue(dataResponse.body)
                    } else {
                        _responseError.postValue((dataResponse as DataErrorResponse).errorMessage)
                    }
                    _loading.postValue(false)
                }
            })
        }
    }
}