package com.romeat.smashup.musicservice.mapper

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.romeat.smashup.BuildConfig
import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupMediaItem
import com.romeat.smashup.musicservice.*
import com.romeat.smashup.util.ImageUrlHelper

object MediaMetadataMapper {
    
    private const val musicBaseUrl = "${BuildConfig.API_URL}/uploads/mashup/"

    fun convertToMedia(mashup: Mashup, bitrate: String) : MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.let {
            it.id = mashup.id.toString()
            it.title = mashup.name
            it.artist = mashup.owner
            it.mediaUri = musicBaseUrl + mashup.id.toString() + ".mp3?bitrate=" + bitrate
            it.albumArtUri = ImageUrlHelper.mashupImageIdToUrl400px(mashup.imageUrl)

            it.displayTitle = mashup.name
            it.displaySubtitle = mashup.owner
            it.displayIconUri = ImageUrlHelper.mashupImageIdToUrl400px(mashup.imageUrl)

            it.downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
        }
        return builder.build()
    }

    fun convertToMediaList(mashupList: List<Mashup>, bitrate: String) : List<MediaMetadataCompat> {
        return mashupList.map { convertToMedia(it, bitrate) }
    }


    fun convertFromMedia(media: MediaMetadataCompat) : MashupMediaItem {
        return MashupMediaItem(
            id = media.id!!.toInt(),
            name = media.title!!,
            //owner = media.artist ?: "",
            owner = media.displaySubtitle ?: "", // artist property is empty somehow
            imageUrl = media.id!!.toInt(),
        )
    }

    fun convertFromMediaList(mediaList: List<MediaMetadataCompat>) : List<MashupMediaItem> {
        return mediaList.map { convertFromMedia(it) }
    }
}