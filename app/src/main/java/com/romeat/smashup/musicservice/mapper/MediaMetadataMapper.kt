package com.romeat.smashup.musicservice.mapper

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupUiData
import com.romeat.smashup.musicservice.*
import com.romeat.smashup.util.ImageUrlHelper

object MediaMetadataMapper {

    // TODO replace with DI provided values
    private const val musicBaseUrl = "https://smashup.ru/stream/mashup/"
    private const val defaultBitrateSuffix = "_128000.mp3"


    private fun musicIdToUrl(id: Int) = musicBaseUrl + id.toString() + defaultBitrateSuffix

    fun convertToMedia(mashup: Mashup) : MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.let {
            it.id = mashup.id.toString()
            it.title = mashup.name
            it.artist = mashup.owner
            it.mediaUri = musicIdToUrl(mashup.id)
            it.albumArtUri = ImageUrlHelper.mashupImageIdToUrl400px(mashup.imageUrl)

            it.displayTitle = mashup.name
            it.displaySubtitle = mashup.owner
            it.displayIconUri = ImageUrlHelper.mashupImageIdToUrl400px(mashup.imageUrl)

            it.downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
        }
        return builder.build()
    }

    fun convertToMediaList(mashupList: List<Mashup>) : List<MediaMetadataCompat> {
        return mashupList.map { convertToMedia(it) }
    }


    fun convertFromMedia(media: MediaMetadataCompat) : MashupUiData {
        return MashupUiData(
            id = media.id!!.toInt(),
            name = media.title!!,
            //owner = media.artist ?: "",
            owner = media.displaySubtitle ?: "", // artist property is empty somehow
            imageUrl = media.id!!.toInt(),
        )
    }

    fun convertFromMediaList(mediaList: List<MediaMetadataCompat>) : List<MashupUiData> {
        return mediaList.map { convertFromMedia(it) }
    }
}