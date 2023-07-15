package com.romeat.smashup.domain

import android.content.Context
import android.util.Log
import com.romeat.smashup.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class CheckVersionUseCase @Inject constructor(
    @ApplicationContext val appContext: Context
) {
    private val VERSION_PREFS_FILE = "VersionPrefsFile"
    private val VERSION_LAST_CHECKED_NAME = "LastCheckedTime"

    private val urlString = "https://romeat.github.io/"
    private val regexLastSupported = "<LastSupportedBuildCode>(.*?)</LastSupportedBuildCode>"
    private val regexLatest = "<LatestBuildCode>(.*?)</LatestBuildCode>"

    private val threshold = if (BuildConfig.DEBUG) TimeUnit.SECONDS.toMillis(1)
        else TimeUnit.DAYS.toMillis(4) // min period between checks - 4 days

    suspend fun getVersion(): SmashupVersion =
        withContext(Dispatchers.IO) {
            val lastCheckedTimestamp = getLastCheckedTime()

            val currentTime = System.currentTimeMillis()

            if ((currentTime - lastCheckedTimestamp) < threshold) {
                return@withContext SmashupVersion.Latest
            } else {
                var connection: HttpURLConnection? = null
                try {
                    connection = URL(urlString).openConnection() as HttpsURLConnection

                    connection.requestMethod = "GET"
                    connection.connectTimeout = 3000
                    connection.readTimeout = 2000

                    val data = connection.inputStream.bufferedReader().readText()
                    val lastSupportedCode = parseLastSupportedBuildCode(data)
                    val latestCode = parseLatestBuildCode(data)

                    val currentCode = BuildConfig.VERSION_CODE

                    if (currentCode < lastSupportedCode) {
                        updateLastCheckedTime(currentTime - TimeUnit.DAYS.toMillis(2))
                        return@withContext SmashupVersion.Outdated
                    } else if (currentCode < latestCode) {
                        updateLastCheckedTime(currentTime)
                        return@withContext SmashupVersion.Actual
                    } else {
                        updateLastCheckedTime(currentTime)
                        return@withContext SmashupVersion.Latest
                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                    Log.e("CheckVersion", e.message ?: "some error")
                    // in case of failure we wait 1 day before next version check
                    updateLastCheckedTime(currentTime - TimeUnit.DAYS.toMillis(3))
                    // just let user proceed in case it's unable to connect to the server
                    return@withContext SmashupVersion.Latest
                }finally {
                    connection?.disconnect()
                }
            }
        }

    private fun parseLastSupportedBuildCode(str: String): Long {
        val buildCode = regexLastSupported.toRegex().find(str)!!.groups[1]!!.value
        return buildCode.toLong()
    }

    private fun parseLatestBuildCode(str: String): Long {
        val buildCode = regexLatest.toRegex().find(str)!!.groups[1]!!.value
        return buildCode.toLong()
    }

    private fun getLastCheckedTime() : Long {
        return appContext
            .getSharedPreferences(VERSION_PREFS_FILE, Context.MODE_PRIVATE)
            .getLong(VERSION_LAST_CHECKED_NAME, 0)
    }

    private fun updateLastCheckedTime(timestamp: Long) {
        appContext
            .getSharedPreferences(VERSION_PREFS_FILE, Context.MODE_PRIVATE)
            .edit()
            .putLong(VERSION_LAST_CHECKED_NAME, timestamp)
            .apply()
    }
}

sealed class SmashupVersion {
    object Latest : SmashupVersion()
    object Actual : SmashupVersion()
    object Outdated : SmashupVersion()
}