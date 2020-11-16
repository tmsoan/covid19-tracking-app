package com.anos.covid19.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anos.covid19.model.CountryItem
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
    private val _summaryLiveData = MutableLiveData<SummaryResponse>()
    val summaryLiveData: LiveData<SummaryResponse> = _summaryLiveData
    var summaryResult: SummaryResponse? = null

    /**
     * this livedata to listen the RESULT from Countries API's response
     */
    private val _countriesResult = MutableLiveData<List<CountryItem>>()
    val countriesResult: LiveData<List<CountryItem>> = _countriesResult
    var countries: ArrayList<CountryItem>? = null



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
                        summaryResult = dataResponse.body
                        _summaryLiveData.postValue(dataResponse.body)
                    } else {
                        _responseError.postValue((dataResponse as DataErrorResponse).errorMessage)
                    }
                    _loading.postValue(false)
                }
            })
        }
    }

    /**
     * get list countries available,
     */
    fun getCountries(showLoading: Boolean) {
        if (!countries.isNullOrEmpty()) {// load from cache if have
            _countriesResult.value = countries
            return
        }
        viewModelScope.launch {
            _loading.postValue(showLoading)
            dataRepository.getCountries(object : IDataRepository.CountriesCallback {
                override fun onCountriesLoaded(dataResponse: DataResponse<List<CountryItem>>) {
                    if (dataResponse is DataSuccessResponse) {
                        if (!dataResponse.body.isNullOrEmpty()) {
                            countries = ArrayList()
                            countries?.addAll(dataResponse.body)
                        }
                        _countriesResult.postValue(dataResponse.body)
                    } else {
                        _responseError.postValue((dataResponse as DataErrorResponse).errorMessage)
                    }
                    _loading.postValue(false)
                }
            })
        }
    }
}