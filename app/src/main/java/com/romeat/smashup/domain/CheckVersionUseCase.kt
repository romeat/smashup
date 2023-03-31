package com.romeat.smashup.domain

import android.content.Context
import android.util.Log
import com.romeat.smashup.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class CheckVersionUseCase @Inject constructor(
    @ApplicationContext val appContext: Context
) {
    private val VERSION_PREFS_FILE = "VersionPrefsFile"
    private val VERSION_LAST_CHECKED_NAME = "LastCheckedTime"

    private val urlString = "https://romeat.github.io"
    private val regexConst = "<LastSupportedBuildCode>(.*?)</LastSupportedBuildCode>"

    private val threshold = 1000 * 60 * 60 * 4L // min period between checks - 4 hours

    suspend fun isActualVersion(): Boolean =
        withContext(Dispatchers.IO) {
            val lastCheckedTimestamp = appContext
                .getSharedPreferences(VERSION_PREFS_FILE, Context.MODE_PRIVATE)
                .getLong(VERSION_LAST_CHECKED_NAME, 0)

            val currentTime = System.currentTimeMillis()

            if ((currentTime - lastCheckedTimestamp) < threshold) {
                return@withContext true
            } else {
                var conn: HttpURLConnection? = null
                try {
                    val connection = URL(urlString).openConnection() as HttpsURLConnection
                    conn = connection

                    connection.requestMethod = "GET"
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000

                    val data = connection.inputStream.bufferedReader().readText()

                    val supportedCode = parseBuildCode(data)
                    val currentCode = BuildConfig.VERSION_CODE

                    if (currentCode >= supportedCode) {
                        appContext
                            .getSharedPreferences(VERSION_PREFS_FILE, Context.MODE_PRIVATE)
                            .edit()
                            .putLong(VERSION_LAST_CHECKED_NAME, currentTime)
                            .apply()
                    }
                    conn.disconnect()
                    return@withContext currentCode >= supportedCode
                } catch (e: Exception) {
                    // TODO just let user proceed in case it's unable to connect to the server
                    conn?.disconnect()
                    return@withContext true
                }
            }
        }

    private fun parseBuildCode(str: String): Long {
        val buildCode = regexConst.toRegex().find(str)!!.groups[1]!!.value
        return buildCode.toLong()
    }
}