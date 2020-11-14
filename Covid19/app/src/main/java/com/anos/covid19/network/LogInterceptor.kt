package com.anos.covid19.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber


class LogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.d("LogInterceptor >>>")
        val oldRequest = chain.request()
        val requestBuilder = Request.Builder()
        requestBuilder.method(oldRequest.method, oldRequest.body)

        val header = customizeHeader()
        val headerPrintOut = StringBuffer()
        header.iterator().forEach {
            requestBuilder.header(it.key, it.value)
            headerPrintOut.append("$it\n")
        }
        Timber.d("Header:\n${header}")

        try {
            val newRequest: Request = requestBuilder.url(oldRequest.url).build()

            var response = chain.proceed(newRequest)

            // auto call refresh token
            checkTokenExpire(response.code)

            val rawJson = response.body?.string()
            rawJson?.let {
                response = response.newBuilder().body(ResponseBody.create(response.body?.contentType(), it)).build()
            }
            return response
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }

    private fun customizeHeader(): Map<String, String> {
        val header = HashMap<String, String>()
        header["Content-Type"] = "application/json"

        /*if (!TextUtils.isEmpty(MyApp.instance.getTokenString())) {
            header["Authorization"] = MyApp.instance.getTokenType() + MyApp.instance.getTokenString()
        }*/
        return header
    }

    private fun checkTokenExpire(responseCode: Int? = 0) {
        /*if (responseCode == 401 || MyApp.instance.checkLoginState() == -1) {
            EventStore.getInstance().postEventAction(ActionKey.REFRESH_TOKEN)
        }*/
    }
}