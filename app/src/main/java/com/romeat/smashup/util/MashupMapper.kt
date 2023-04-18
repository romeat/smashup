package com.romeat.smashup.util

import com.romeat.smashup.data.dto.Mashup
import com.romeat.smashup.data.dto.MashupListItem

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