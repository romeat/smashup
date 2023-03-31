package com.romeat.smashup.util

object ImageUrlHelper {

    private const val mashupImageBaseUrl = "https://smashup.ru/uploads/mashup/"
    private const val imageSuffix400px = "_400x400.png"
    private const val imageSuffix100px = "_100x100.png"

    private const val playlistImageBaseUrl = "https://smashup.ru/uploads/playlist/"
    private const val authorImageBaseUrl = "https://smashup.ru/uploads/user/"
    private const val sourceImageBaseUrl = "https://smashup.ru/uploads/track/"

    fun mashupImageIdToUrl400px(imageId: String) = mashupImageBaseUrl + imageId + imageSuffix400px

    fun mashupImageIdToUrl100px(imageId: String?) = mashupImageBaseUrl + imageId + imageSuffix100px

    fun playlistImageIdToUrl400px(imageId: String?) = playlistImageBaseUrl + imageId + imageSuffix400px

    fun authorImageIdToUrl400px(imageId: String) = authorImageBaseUrl + imageId + imageSuffix400px

    fun sourceImageIdToUrl400px(imageId: String) = sourceImageBaseUrl + imageId + imageSuffix400px
}