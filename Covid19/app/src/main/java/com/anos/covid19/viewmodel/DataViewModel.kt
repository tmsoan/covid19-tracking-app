package com.anos.covid19.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anos.covid19.R
import com.anos.covid19.model.CountryItem
import com.anos.covid19.model.CountryStatus
import com.anos.covid19.model.CountryTotalStatus
import com.anos.covid19.model.response.SummaryResponse
import com.anos.covid19.network.DataErrorResponse
import com.anos.covid19.network.DataResponse
import com.anos.covid19.network.DataSuccessResponse
import com.anos.covid19.repository.DataRepository
import com.anos.covid19.repository.interfaces.IDataRepository
import com.anos.covid19.utils.AppConst
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
     * this livedata to listen the RESULT from GET Country All Status API's response
     */
    private val _countryAllStatus = MutableLiveData<List<CountryStatus>>()
    val countryAllStatus: LiveData<List<CountryStatus>> = _countryAllStatus

    /**
     * this livedata to listen the RESULT from GET Country All Status API's response
     */
    private val _totalDeathByStatusLiveData = MutableLiveData<List<CountryTotalStatus>>()
    val totalDeathByStatusLiveData: LiveData<List<CountryTotalStatus>> = _totalDeathByStatusLiveData

    private val _totalConfirmedByCountryLiveData = MutableLiveData<List<CountryTotalStatus>>()
    val totalConfirmedByCountryLiveData: LiveData<List<CountryTotalStatus>> = _totalConfirmedByCountryLiveData

    private val _totalRecoveredByCountryLiveData = MutableLiveData<List<CountryTotalStatus>>()
    val totalRecoveredByCountryLiveData: LiveData<List<CountryTotalStatus>> = _totalRecoveredByCountryLiveData

    /**
     * this livedata to listen the RESULT from GET Country Total all Status API's response
     */
    private val _countryTotalAllStatusLiveData = MutableLiveData<List<CountryStatus>>()
    val countryTotalAllStatusLiveData: LiveData<List<CountryStatus>> = _countryTotalAllStatusLiveData



    /**
     * fetch new Summary data,
     * and update the result into the livedata object to display UI
     */
    fun getSummary(refresh: Boolean) {
        viewModelScope.launch {
            _loading.postValue(true)
            dataRepository.getSummary(refresh, object : IDataRepository.SummaryCallback {
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

    /**
     * get all status by country
     */
    fun getCountryStatus(slug: String, from: Date, to: Date) {
        viewModelScope.launch {
            _loading.postValue(true)
            val dateFormat = SimpleDateFormat(AppConst.DATE_FORMAT, Locale.getDefault())
            dataRepository.getCountryAllStatus(slug, dateFormat.format(from), dateFormat.format(to), object : IDataRepository.CountryAllStatusCallback {
                override fun onCountryStatusLoaded(dataResponse: DataResponse<List<CountryStatus>>) {
                    if (dataResponse is DataSuccessResponse) {
                        _countryAllStatus.postValue(dataResponse.body)
                    } else {
                        _responseError.postValue((dataResponse as DataErrorResponse).errorMessage)
                    }
                    _loading.postValue(false)
                }
            })
        }
    }

    /**
     * get total case in country by status
     */
    fun getCountryTotalByStatus(slug: String, status: String, from: Date, to: Date) {
        viewModelScope.launch {
            _loading.postValue(true)
            val dateFormat = SimpleDateFormat(AppConst.DATE_FORMAT, Locale.getDefault())
            dataRepository.getCountryTotalByStatus(slug, status, dateFormat.format(from), dateFormat.format(to), object : IDataRepository.CountryTotalStatusCallback {
                override fun onCountryStatusLoaded(dataResponse: DataResponse<List<CountryTotalStatus>>) {
                    if (dataResponse is DataSuccessResponse) {
                        when (status) {
                            AppConst.Status.CONFIRMED -> _totalConfirmedByCountryLiveData.postValue(dataResponse.body)
                            AppConst.Status.RECOVERED -> _totalRecoveredByCountryLiveData.postValue(dataResponse.body)
                            AppConst.Status.DEATH -> _totalDeathByStatusLiveData.postValue(dataResponse.body)
                        }
                    } else {
                        _responseError.postValue((dataResponse as DataErrorResponse).errorMessage)
                    }
                    _loading.postValue(false)
                }
            })
        }
    }

    /**
     * get country total all status
     */
    fun getCountryTotalAllStatus(slug: String, from: Date, to: Date) {
        viewModelScope.launch {
            _loading.postValue(true)
            val dateFormat = SimpleDateFormat(AppConst.DATE_FORMAT, Locale.getDefault())
            dataRepository.getCountryTotalAllStatus(slug, dateFormat.format(from), dateFormat.format(to), object : IDataRepository.CountryAllStatusCallback {
                override fun onCountryStatusLoaded(dataResponse: DataResponse<List<CountryStatus>>) {
                    if (dataResponse is DataSuccessResponse) {
                        _countryTotalAllStatusLiveData.postValue(dataResponse.body)
                    } else {
                        _responseError.postValue((dataResponse as DataErrorResponse).errorMessage)
                    }
                    _loading.postValue(false)
                }
            })
        }
    }
}