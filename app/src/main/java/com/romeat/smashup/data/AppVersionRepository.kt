package com.romeat.smashup.data

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.romeat.smashup.BuildConfig
import com.romeat.smashup.util.getTimeFromPrefs
import com.romeat.smashup.util.updateLastTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer
import okio.sink
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

const val VERSION_PREFS_FILE = "VersionPrefsFile"

class AppVersionRepository @Inject constructor(
    @ApplicationContext val appContext: Context
) {
    // last known time when version and updates were checked
    private val VERSION_LAST_CHECKED_TIME = "LastCheckedTime"

    private val urlString = "https://romeat.github.io/"
    private val regexLastSupported = "<LastSupportedBuildCode>(.*?)</LastSupportedBuildCode>"
    private val regexLatest = "<LatestBuildCode>(.*?)</LatestBuildCode>"
    private val regexDownloadLink = "<DownloadLink>(.*?)</DownloadLink>"

    private var apkUrl: String? = null
    private var apkFile: File? = null

    private var latestCode: Long? = null

    private val versionCheckThreshold = if (BuildConfig.DEBUG) TimeUnit.SECONDS.toMillis(30)
        // min period between checks - 1 day
        else TimeUnit.DAYS.toMillis(1)

    var progressFlow = MutableStateFlow(0f)

    fun getVersion(): SmashupVersion {
        val lastCheckedTimestamp = appContext
            .getTimeFromPrefs(VERSION_LAST_CHECKED_TIME, VERSION_PREFS_FILE)
        val currentTime = System.currentTimeMillis()

        if ((currentTime - lastCheckedTimestamp) < versionCheckThreshold) {
            return SmashupVersion.Latest
        } else {
            var connection: HttpURLConnection? = null
            try {
                connection = URL(urlString).openConnection() as HttpsURLConnection

                connection.requestMethod = "GET"
                connection.connectTimeout = 3000
                connection.readTimeout = 2000

                val data = connection.inputStream.bufferedReader().readText()
                val lastSupportedCode = parseLastSupportedBuildCode(data)
                latestCode = parseLatestBuildCode(data)
                parseDownloadLink(data)

                val currentVersionCode = BuildConfig.VERSION_CODE

                if (currentVersionCode < lastSupportedCode) { // App is outdated
                    return SmashupVersion.Outdated
                } else if (currentVersionCode < latestCode!!) { // App is actual but not latest version
                    appContext.updateLastTime(
                        name = VERSION_LAST_CHECKED_TIME,
                        prefsFile = VERSION_PREFS_FILE,
                        timestamp = currentTime
                    )
                    return SmashupVersion.Actual
                } else { // App is latest version
                    appContext.updateLastTime(
                        name = VERSION_LAST_CHECKED_TIME,
                        prefsFile = VERSION_PREFS_FILE,
                        timestamp = currentTime
                    )
                    return SmashupVersion.Latest
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CheckVersion", e.message ?: "some error")
                // in case of failure we wait at least 2 hours before next version check
                appContext.updateLastTime(
                    name = VERSION_LAST_CHECKED_TIME,
                    prefsFile = VERSION_PREFS_FILE,
                    timestamp = currentTime - TimeUnit.HOURS.toMillis(22)
                )
                // just let user proceed in case it's unable to connect to the server
                return SmashupVersion.Latest
            } finally {
                connection?.disconnect()
            }
        }
    }

    fun downloadNewVersion() {
        apkFile = appContext.createApkFile(latestCode.toString())

        val progressListener = object : ProgressListener {
            override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                progressFlow.update { bytesRead.toFloat() / contentLength }
                //Log.d("downloadNewVersion", "update: $progress")
                if (done) installApk()
            }
        }

        val okHttpClient = OkHttpClient.Builder().addNetworkInterceptor {
            val originalResponse = it.proceed(it.request())
            val responseBody = originalResponse.body ?: return@addNetworkInterceptor originalResponse

            return@addNetworkInterceptor originalResponse.newBuilder()
                .body(DownloadProgressBody(responseBody, progressListener))
                .build()
        }.build()

        val request = Request.Builder().url(apkUrl!!).build()
        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) throw Error("Request failed")

        response.body?.source()?.use { bufferedSource ->
            val bufferedSink = apkFile!!.sink().buffer()
            bufferedSink.writeAll(bufferedSource)
            bufferedSink.close()
        }
    }

    private fun installApk() {
        val uri = FileProvider.getUriForFile(
            appContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            apkFile!!
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        appContext.startActivity(intent)
    }

    private fun parseLastSupportedBuildCode(str: String): Long {
        val buildCode = regexLastSupported.toRegex().find(str)!!.groups[1]!!.value
        return buildCode.toLong()
    }

    private fun parseLatestBuildCode(str: String): Long {
        val buildCode = regexLatest.toRegex().find(str)!!.groups[1]!!.value
        return buildCode.toLong()
    }

    private fun parseDownloadLink(str: String) {
        apkUrl = regexDownloadLink.toRegex().find(str)!!.groups[1]!!.value
    }
}

private const val EXHAUSTED_SOURCE = -1L

/**
 * A [ResponseBody] that informs a [ProgressListener] about the download progress.
 */
class DownloadProgressBody(
    private val responseBody: ResponseBody,
    private val progressListener: ProgressListener
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? = responseBody.contentType()

    override fun contentLength(): Long = responseBody.contentLength()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != EXHAUSTED_SOURCE) bytesRead else 0L
                progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == EXHAUSTED_SOURCE)
                return bytesRead
            }
        }
    }
}

/**
 * Callback getting informed when the download progress of [DownloadProgressBody] updates.
 */
interface ProgressListener {

    /**
     * Informs this listener that the download progress was updated.
     *
     * @param bytesRead The bytes that have been read.
     * @param contentLength The total bytes that are being read.
     * @param done Whether the download is complete.
     */
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}

fun Context.createApkFile(code: String): File {
    // Create an image file name
//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    return File.createTempFile(
        "smashup_$code", /* prefix */
        ".apk", /* suffix */
        externalCacheDir      /* directory */
    )
}

sealed class SmashupVersion {
    object Latest : SmashupVersion()
    object Actual : SmashupVersion()
    object Outdated : SmashupVersion()
}