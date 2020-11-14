package com.anos.covid19.helper

import android.content.Context

class AppConfig(context: Context) {

    companion object {
        private var instance: AppConfig? = null
        private val lock = Any()

        fun getInstance(context: Context): AppConfig {
            synchronized(lock) {
                if (instance == null) {
                    instance = AppConfig(context)
                }
                return instance as AppConfig
            }
        }
    }

    private var prefUtil = PrefUtil.getInstance(context)

    object Key {
        const val TOKEN = "token"
        const val USER_INFO = "user_info"
    }

    fun clearAll() {
        prefUtil.clear()
    }

    fun setToken(token: String) {
        prefUtil.setString(Key.TOKEN, token)
    }

    fun getToken(): String {
        return prefUtil.getString(Key.TOKEN, "")
    }
}