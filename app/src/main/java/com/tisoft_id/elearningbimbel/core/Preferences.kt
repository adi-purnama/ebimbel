package com.tisoft_id.elearningbimbel.core

import android.content.Context
import android.content.SharedPreferences

import org.json.JSONArray


class Preferences(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences("preferences", 0)
    private val editor: SharedPreferences.Editor

    init {
        editor = preferences.edit()
    }

    operator fun set(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    operator fun set(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    operator fun set(key: String, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    operator fun set(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    operator fun set(key: String, data: JSONArray) {
        val stringData = data.toString().replace("\"", "")
        set(key, stringData.substring(1, stringData.length - 1))
    }

    fun getString(key: String?): String? {
        return preferences.getString(key, "")
    }

    fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    fun getLong(key: String): Long {
        return preferences.getLong(key, 0)
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun delete(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun clear() {
        editor.clear().commit()
    }
}
