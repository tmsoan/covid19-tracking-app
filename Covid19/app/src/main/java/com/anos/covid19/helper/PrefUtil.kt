package com.anos.covid19.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PrefUtil
@SuppressLint("CommitPrefEdits")
constructor(context: Context) {

    companion object {
        private var instance: PrefUtil? = null
        private lateinit var preferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private val lock = Any()

        fun getInstance(context: Context): PrefUtil {
            synchronized(lock) {
                if (instance == null) {
                    instance = PrefUtil(context)
                }
                return instance as PrefUtil
            }
        }
    }

    init {
        preferences = context.applicationContext
            .getSharedPreferences(context.packageName + ".config", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    @Synchronized
    fun clear() {
        editor.clear().apply()
    }


    @Synchronized
    fun setString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    @Synchronized
    fun getString(key: String, defaultValue: String?): String {
        return preferences.getString(key, defaultValue) ?: ""
    }

    @Synchronized
    fun setInteger(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    @Synchronized
    fun getInteger(key: String, defaultValue: Int?): Int {
        return preferences.getInt(key, defaultValue ?: 0)
    }

    @Synchronized
    fun setLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }

    @Synchronized
    fun getLong(key: String, defaultValue: Long?): Long {
        return preferences.getLong(key, defaultValue ?: 0L)
    }

    @Synchronized
    fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    @Synchronized
    fun getBoolean(key: String, defaultValue: Boolean?): Boolean {
        return preferences.getBoolean(key, defaultValue ?: false)
    }

    @Synchronized
    fun setFloat(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.apply()
    }

    @Synchronized
    fun getFloat(key: String, defaultValue: Float?): Float {
        return preferences.getFloat(key, defaultValue ?: 0.0f)
    }

    @Synchronized
    fun setStringSet(key: String, value: Set<String?>?) {
        editor.putStringSet(key, value)
        editor.apply()
    }

    @Synchronized
    fun getStringSet(key: String, defaultValue: Set<String?>?): Set<String?>? {
        return preferences.getStringSet(key, defaultValue)
    }
}