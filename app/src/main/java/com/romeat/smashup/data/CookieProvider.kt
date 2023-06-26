package com.romeat.smashup.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/*
@Singleton // TODO test if it works as singleton
class CookieProvider @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    private val COOKIE_PREFS_FILE = "CookiePrefsFile"
    private val COOKIE_PREFS_NAME = "AuthCookie"
    private val COOKIE_DOMAIN = "smashup.ru"

    fun getCookiesSet() : HashSet<String> {
        return appContext
                .getSharedPreferences(COOKIE_PREFS_FILE, Context.MODE_PRIVATE)
                .getStringSet(COOKIE_PREFS_NAME, HashSet<String>())
                ?.toHashSet() ?: HashSet<String>()
    }

    fun setCookies(cookies: HashSet<String>) {
        appContext
            .getSharedPreferences(COOKIE_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(
                COOKIE_PREFS_NAME,
                cookies.filter { cookie ->
                    !cookie.contains("token=;") // filter empty tokens. TODO: where they come from?
                }.toHashSet())
            .apply()
    }

    fun clearAuthCookies() {
        appContext
            .getSharedPreferences(COOKIE_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .remove(COOKIE_PREFS_NAME)
            .apply()
    }
}

 */