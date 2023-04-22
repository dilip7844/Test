package com.example.test

import android.content.Context
import android.content.SharedPreferences
  import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object SharedPrefs {
    private var editor: SharedPreferences.Editor? = null
    var sharedPreferences: SharedPreferences? = null
    const val ISLOGGEDIN = "isLoggedIn"

    private const val PREF_NAME = "prefs"

    fun initSharedPrefs() {
        sharedPreferences = AppConfig.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
     }

    fun getString(key: String?): String? {
        return sharedPreferences!!.getString(key, "")
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences!!.getBoolean(key, false)
    }

    fun setString(key: String?, value: String?) {
        editor!!.putString(key, value)
        editor!!.commit()
    }

    fun setBoolean(key: String?, value: Boolean?) {
        editor!!.putBoolean(key, value!!)
        editor!!.commit()
    }

    fun <T> setList(key: String?, list: MutableList<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        setString(key, json)
    }

    inline fun <reified T> getList(key: String?): MutableList<T> {
        val arrayItems: MutableList<T>
        val serializedObject = sharedPreferences?.getString(key, "[]")
        val type: Type? = object : TypeToken<MutableList<T>>() {}.type
        arrayItems = Gson().fromJson(serializedObject, type)
        return arrayItems
    }


}