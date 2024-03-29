package com.romeat.smashup.util

import android.content.Context
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.romeat.smashup.BuildConfig

object ImageUrlHelper {

    private const val imageSuffix800px = "_800x800.png"
    private const val imageSuffix400px = "_400x400.png"
    private const val imageSuffix100px = "_100x100.png"

    private const val mashupImageBaseUrl = "${BuildConfig.API_URL}/uploads/mashup/"
    private const val playlistImageBaseUrl = "${BuildConfig.API_URL}/uploads/playlist/"
    private const val authorImageBaseUrl = "${BuildConfig.API_URL}/uploads/user/"
    private const val sourceImageBaseUrl = "${BuildConfig.API_URL}/uploads/track/"

    fun mashupImageIdToUrl800px(imageId: String) = mashupImageBaseUrl + imageId + imageSuffix800px

    fun mashupImageIdToUrl400px(imageId: String) = mashupImageBaseUrl + imageId + imageSuffix400px

    fun mashupImageIdToUrl100px(imageId: String?) = mashupImageBaseUrl + imageId + imageSuffix100px

    fun playlistImageIdToUrl400px(imageId: String?) = playlistImageBaseUrl + imageId + imageSuffix400px

    fun playlistImageIdToUrl100px(imageId: String?) = playlistImageBaseUrl + imageId + imageSuffix100px

    fun authorImageIdToUrl400px(imageId: String) = authorImageBaseUrl + imageId + imageSuffix400px

    fun authorImageIdToUrl100px(imageId: String) = authorImageBaseUrl + imageId + imageSuffix100px

    fun sourceImageIdToUrl400px(imageId: String) = sourceImageBaseUrl + imageId + imageSuffix400px

    fun sourceImageIdToUrl100px(imageId: String) = sourceImageBaseUrl + imageId + imageSuffix100px
}

interface Launcher {
    fun launch()
}

class PickImage : ActivityResultContracts.PickVisualMedia() {
    override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent {
        val intent = super.createIntent(context, input)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))

        return intent
    }
}