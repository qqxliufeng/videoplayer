package com.android.ql.lf.videoplayer.utils

import com.google.gson.Gson
import org.json.JSONArray


const val VERIFY_CODE_TIME = 60L


inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, T::class.java)

inline fun <reified T> JSONArray.toArrayList(): ArrayList<T>? {
    try {
        if (this.length() > 0) {
            val tempList = arrayListOf<T>()
            (0 until this.length()).forEach {
                tempList.add(Gson().fromJson(this[it].toString()))
            }
            return tempList
        }
    } catch (e: Exception) {
        return null
    }
    return null
}