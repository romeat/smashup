package com.romeat.smashup.util

import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupListItem
import com.romeat.smashup.data.dto.Playlist

fun ConvertToUiListItems(mashups: List<Mashup>, likes: Set<Int>): List<MashupListItem> {
    return mashups.map { mashup ->
        MashupListItem(
            id = mashup.id,
            name = mashup.name,
            owner = mashup.owner,
            imageUrl = mashup.imageUrl,
            explicit = false, // todo explicit,
            streams = mashup.streams,
            likes = mashup.likes,
            tracks = mashup.tracks,
            isLiked = likes.contains(mashup.id)
        )
    }.toList()
}

data class SquareDisplayItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

fun MashupsToSquareDisplayItems(mashups: List<Mashup>): List<SquareDisplayItem> {
    return mashups.map { mashup ->
        SquareDisplayItem(
            id = mashup.id,
            title = mashup.name,
            subtitle = mashup.owner,
            imageUrl = ImageUrlHelper.mashupImageIdToUrl400px(mashup.imageUrl)
        )
    }.toList()
}

fun PlaylistsToSquareDisplayItems(mashups: List<Playlist>): List<SquareDisplayItem> {
    return mashups.map { playlist ->
        SquareDisplayItem(
            id = playlist.id,
            title = playlist.name,
            subtitle = "",
            imageUrl = ImageUrlHelper.playlistImageIdToUrl400px(playlist.imageUrl)
        )
    }.toList()
}