package com.example.androidappsample

import android.content.Context

private const val PREF_NAME = "config"

class SharedPref(context: Context) {
    private enum class PrefKeys {
        SAMPLE_INT,
        SAMPLE_STR,
    }

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var sampleInt = pref.getInt(PrefKeys.SAMPLE_INT.name, 0)
    var sampleStr = pref.getString(PrefKeys.SAMPLE_STR.name, "")

    fun save() {
        pref.edit()
            .putInt(PrefKeys.SAMPLE_INT.name, sampleInt)
            .putString(PrefKeys.SAMPLE_STR.name, sampleStr)
            .apply()
    }
}