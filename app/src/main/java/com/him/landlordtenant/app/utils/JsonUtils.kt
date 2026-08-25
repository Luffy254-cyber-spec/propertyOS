package com.him.landlordtenant.app.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonUtils {
    val gson = Gson()

    fun <T> toJson(obj: T): String = gson.toJson(obj)

    fun <T> fromJson(json: String, classOfT: Class<T>): T = gson.fromJson(json, classOfT)

    inline fun <reified T> fromJsonList(json: String): List<T> {
        val type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, type)
    }
}
