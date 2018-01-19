package com.anjlab.btdriving.app

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Preferences(private val context: Context) {
    var enabled: Boolean
        get() = getBoolean(KEY_ENABLED)
        set(value) = setBoolean(KEY_ENABLED, value)

    var driving: Boolean
        get() = getBoolean(KEY_DRIVING)
        set(value) = setBoolean(KEY_DRIVING, value)

    var debug: Boolean
        get() = getBoolean(KEY_DEBUG)
        set(value) = setBoolean(KEY_DEBUG, value)

    private fun getBoolean(key: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false)
    }

    private fun setBoolean(key: String, value: Boolean) {
        getPreferencesEditor()
            .putBoolean(key, value)
            .apply()
    }

    private fun getPreferencesEditor() : SharedPreferences.Editor {
        return PreferenceManager.getDefaultSharedPreferences(context).edit()
    }

    companion object {
        const val KEY_DEBUG = "debug"
        const val KEY_ENABLED = "enabled"
        const val KEY_DRIVING = "driving"
    }
}