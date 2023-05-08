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
            explicit = mashup.explicit,
            streams = mashup.streams,
            likes = mashup.likes,
            tracks = mashup.tracks,
            isLiked = likes.contains(mashup.id)
        )
    }.toList()
}

fun ConvertFromUiListItems(uiMashups: List<MashupListItem>): List<Mashup> {
    return uiMashups.map { uiMashup ->
        Mashup(
            id = uiMashup.id,
            name = uiMashup.name,
            owner = uiMashup.owner,
            imageUrl = uiMashup.imageUrl,
            explicit = uiMashup.explicit,
            streams = uiMashup.streams,
            likes = uiMashup.likes,
            tracks = uiMashup.tracks
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