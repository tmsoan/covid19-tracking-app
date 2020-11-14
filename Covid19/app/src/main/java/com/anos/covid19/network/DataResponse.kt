package com.anos.covid19.network

import com.anos.covid19.model.response.ErrorModelResponse
import com.google.gson.Gson
import retrofit2.Response

internal const val UNKNOWN_CODE = -1

sealed class DataResponse<T> {
    companion object {
        fun <T> create(response: Response<T>): DataResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    DataEmptyResponse()
                } else {
                    DataSuccessResponse(body)
                }
            } else {
                return DataErrorResponse(response.code(), getErrorMessage(response))
            }
        }

        fun <T> createError(errorCode: Int, error: Throwable): DataResponse<T> {
            return DataErrorResponse(errorCode, error.message ?: "An Unexpected Error Occurred")
        }

        private fun <T> getErrorMessage(response: Response<T>): String {
            var errorMsg = ""
            response.errorBody()?.string()?.let {
                try {
                    val errorObj = Gson().fromJson(it, ErrorModelResponse::class.java)
                    errorMsg = errorObj?.message ?: ""
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (errorMsg.isEmpty()) {
                errorMsg = "An unexpected error occurred"
            }
            return errorMsg
        }
    }
}

class DataEmptyResponse<T> : DataResponse<T>()
data class DataErrorResponse<T>(val errorCode: Int, val errorMessage: String): DataResponse<T>()
data class DataSuccessResponse<T>(val body: T): DataResponse<T>()