package com.romeat.smashup.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

fun Context.getTimeFromPrefs(name: String, prefsFile: String): Long {
    return this
        .getSharedPreferences(prefsFile, Context.MODE_PRIVATE)
        .getLong(name, 0)
}

fun Context.updateLastTime(name: String, prefsFile: String, timestamp: Long) {
    return this
        .getSharedPreferences(prefsFile, Context.MODE_PRIVATE)
        .edit()
        .putLong(name, timestamp)
        .apply()
}