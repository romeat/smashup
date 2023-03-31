package com.romeat.smashup.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggedUser @Inject constructor(
    @ApplicationContext val appContext: Context
) {
    private val USER_PREFS_FILE = "UserPrefsFile"
    private val USER_PREFS_NAME = "UserName"

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    init {
        _name.value = appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .getString(USER_PREFS_NAME, null)
    }

    fun setName(user: String) {
        appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(
                USER_PREFS_NAME,
                user
            )
            .apply()
        _name.value = user
    }

    fun logOut() {
        appContext
            .getSharedPreferences(USER_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .remove(USER_PREFS_NAME)
            .apply()
        _name.value = null
    }
}